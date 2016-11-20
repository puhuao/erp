package com.wksc.framwork.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.lzy.okhttputils.OkHttpUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.R;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.QRResourceSendEvent;
import com.wksc.framwork.zxing.SignInOrUpEvent;
import com.wksc.framwork.zxing.ZxingConfig;
import com.wksc.framwork.zxing.camera.BeepManager;
import com.wksc.framwork.zxing.camera.CameraManager;
import com.wksc.framwork.zxing.decode.CaptureActivityHandler;
import com.wksc.framwork.zxing.decode.DecodeThread;
import com.wksc.framwork.zxing.decode.FinishListener;
import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;
import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;
import com.wksc.framwork.zxing.view.ViewfinderView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.QRCodeModel;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by puhua on 2016/3/28.
 *
 * @二维码扫描页面
 */
public class ZxingCaptureActivity extends Activity implements SurfaceHolder.Callback {
    public static  final int MEETING_SIGN_IN = 2;//会议签到
    public static  final int MEETING_SIGN_UP = 1;//会议报名
    public static final int RESORCE_SEND = 3;//物资发放

    private boolean hasSurface;
    private BeepManager beepManager;// 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
    public SharedPreferences mSharedPreferences;// 存储二维码条形码选择的状态
    public static String currentState;// 条形码二维码选择状态
    private String characterSet;

    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    //    private InactivityTimer inactivityTimer;
    private CameraManager cameraManager;
    private Vector<BarcodeFormat> decodeFormats;// 编码格式
    private CaptureActivityHandler mHandler;// 解码线程
    //    private ImageView bitmap;

    private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet
            .of(ResultMetadataType.ISSUE_NUMBER,
                    ResultMetadataType.SUGGESTED_PRICE,
                    ResultMetadataType.ERROR_CORRECTION_LEVEL,
                    ResultMetadataType.POSSIBLE_COUNTRY);
    private static final String TAG = ZxingCaptureActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bd = getIntent().getExtras();
        initSetting();
        setContentView(R.layout.layout_zxing);
        initComponent();
        initView();
    }

    private void initSetting() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕处于点亮状态
        // window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
    }

    private void initComponent() {
        hasSurface = false;
        //        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        currentState = this.mSharedPreferences.getString("currentState",
                "qrcode");
        cameraManager = new CameraManager(getApplication());
    }

    private void initView() {

        surfaceView = (SurfaceView) findViewById(R.id.preview_view);

    }

    private void setScanType() {
        do {
            if ((ZxingCaptureActivity.currentState != null)
                    && (ZxingCaptureActivity.currentState.equals("onecode"))) {
                viewfinderView.setVisibility(View.VISIBLE);
                onecodeSetting();
                return;
            }
        }
        while ((ZxingCaptureActivity.currentState == null)
                || (!ZxingCaptureActivity.currentState.equals("qrcode")));

        viewfinderView.setVisibility(View.VISIBLE);
        qrcodeSetting();
    }

    /**
     * 主要对相机进行初始化工作
     */
    @Override
    protected void onResume() {
        super.onResume();
        //        inactivityTimer.onActivity();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        surfaceHolder = surfaceView.getHolder();
        setScanType();
        resetStatusView();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            // 如果SurfaceView已经渲染完毕，会回调surfaceCreated，在surfaceCreated中调用initCamera()
            surfaceHolder.addCallback(this);
        }
        // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        beepManager.updatePrefs();
        // 恢复活动监控器
        //        inactivityTimer.onResume();
    }

    /**
     * 展示状态视图和扫描窗口，隐藏结果视图
     */
    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 初始化摄像头。打开摄像头，检查摄像头是否被开启及是否被占用
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the mHandler starts the preview, which can also throw a
            // RuntimeException.
            if (mHandler == null) {
                mHandler = new CaptureActivityHandler(this, decodeFormats,
                        characterSet, cameraManager);
            }
            // decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 若摄像头被占用或者摄像头有问题则跳出提示对话框
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("抱歉，Android相机可能被占用，您可能需要重启设备。");
        builder.setPositiveButton("确定", new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    /**
     * 暂停活动监控器,关闭摄像头
     */
    @Override
    protected void onPause() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        // 暂停活动监控器
        //        inactivityTimer.onPause();
        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    /**
     * 停止活动监控器,保存最后选中的扫描类型
     */
    @Override
    protected void onDestroy() {
        // 停止活动监控器
        //        inactivityTimer.shutdown();
        saveScanTypeToSp();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 保存退出进程前选中的二维码条形码的状态
     */
    private void saveScanTypeToSp() {
        SharedPreferences.Editor localEditor = this.mSharedPreferences.edit();
        localEditor.putString("currentState", ZxingCaptureActivity.currentState);
        localEditor.commit();
    }

    /**
     * 获取扫描结果
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            beepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, scaleFactor, rawResult);
        }
        handleDecodeInternally(rawResult, barcode);
        try {
            JSONObject jsonObject = new JSONObject(rawResult.getText());
            String type = jsonObject.getString("type");
                switch (Integer.valueOf(type)){
                    case MEETING_SIGN_IN:
                        //到会议签到
                        QRChecInModel qrChecInModel = (QRChecInModel) jsonObject.get("param");
                        EventBus.getDefault().post(new SignInOrUpEvent(qrChecInModel));
                        break;
                    case  MEETING_SIGN_UP:
                        //到会议报名
                        EventBus.getDefault().post(new SignInOrUpEvent((QRChecInModel) jsonObject.get("param")));
                        break;
                    case RESORCE_SEND:
                        //到会议报名
                        EventBus.getDefault().post(new QRResourceSendEvent((QRresourceSend) jsonObject.get("param")));
                        break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.finish();

    }

    /**
     * 在扫描图片结果中绘制绿色的点
     *
     * @param barcode
     * @param scaleFactor
     * @param rawResult
     */
    private void drawResultPoints(Bitmap barcode, float scaleFactor,
                                  Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(0xc099cc00);
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4
                    && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
                    .getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(),
                                scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    /**
     * 在扫描图片结果中绘制绿色的线
     *
     * @param canvas
     * @param paint
     * @param a
     * @param b
     * @param scaleFactor
     */
    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
                                 ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(),
                    scaleFactor * b.getX(), scaleFactor * b.getY(), paint);
        }
    }

    /**
     * 显示扫描结果
     *
     * @param rawResult
     * @param barcode
     */

    private void handleDecodeInternally(Result rawResult, Bitmap barcode) {
        viewfinderView.setVisibility(View.GONE);

        Map<ResultMetadataType, Object> metadata = rawResult
                .getResultMetadata();
        if (metadata != null) {
            StringBuilder metadataText = new StringBuilder(20);
            for (Map.Entry<ResultMetadataType, Object> entry : metadata
                    .entrySet()) {
                if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
                    metadataText.append(entry.getValue()).append('\n');
                }
            }
            if (metadataText.length() > 0) {
                metadataText.setLength(metadataText.length() - 1);
            }
        }

    }

    private void onecodeSetting() {
        decodeFormats = new Vector<BarcodeFormat>(7);
        decodeFormats.clear();
        decodeFormats.addAll(DecodeThread.ONE_D_FORMATS);
        if (null != mHandler) {
            mHandler.setDecodeFormats(decodeFormats);
        }

        viewfinderView.refreshDrawableState();
        cameraManager.setManualFramingRect(360, 222);
        viewfinderView.refreshDrawableState();

    }

    private void qrcodeSetting() {
        decodeFormats = new Vector<BarcodeFormat>(2);
        decodeFormats.clear();
        decodeFormats.add(BarcodeFormat.QR_CODE);
        decodeFormats.add(BarcodeFormat.DATA_MATRIX);
        if (null != mHandler) {
            mHandler.setDecodeFormats(decodeFormats);
        }

        viewfinderView.refreshDrawableState();
        cameraManager.setManualFramingRect(500, 500);
        viewfinderView.refreshDrawableState();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG,
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 在经过一段延迟后重置相机以进行下一次扫描。 成功扫描过后可调用此方法立刻准备进行下次扫描
     *
     * @param delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(ZxingConfig.Zxing_Restart_Decode, delayMS);
        }
        //        Handler handler = new Handler(){
        //            @Override
        //            public void handleMessage(Message msg) {
        //            bitmap.setVisibility(View.GONE);
        //                resetStatusView();
        //            }
        //        };
        //        handler.sendEmptyMessageDelayed(0,1000);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}
