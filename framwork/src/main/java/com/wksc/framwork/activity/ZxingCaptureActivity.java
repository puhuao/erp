package com.wksc.framwork.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;

import com.wksc.framwork.R;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.QRResourceRecycleEvent;
import com.wksc.framwork.zxing.QRResourceSendEvent;
import com.wksc.framwork.zxing.SignInOrUpEvent;
import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;
import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.qrcode.core.QRCodeView;


/**
 * Created by puhua on 2016/3/28.
 *
 * @二维码扫描页面
 */
public class ZxingCaptureActivity extends Activity implements QRCodeView.Delegate {
    public static  final int MEETING_SIGN_IN = 2;//会议签到
    public static  final int MEETING_SIGN_UP = 1;//会议报名
    public static final int RESORCE_SEND = 3;//物资发放
    public static final int RESOURCE_TRANSFER = 4;//物资交接回收
    private static final String TAG = ZxingCaptureActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private QRCodeView mQRCodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bd = getIntent().getExtras();
        setContentView(R.layout.layout_zxing);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
    }

    private void initView() {
        mQRCodeView = (QRCodeView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mQRCodeView.stopCamera();
    }

    /**
     * 停止活动监控器,保存最后选中的扫描类型
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQRCodeView.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        mQRCodeView.stopSpot();
        vibrate();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String type = jsonObject.getString("type");
            String s = jsonObject.getString("param");
            switch (Integer.valueOf(type)){
                case MEETING_SIGN_IN:
                    //到会议签到
                    QRChecInModel qrChecInModel = GsonUtil.fromJson(s,QRChecInModel.class);
                    EventBus.getDefault().post(new SignInOrUpEvent(qrChecInModel));
                    break;
                case  MEETING_SIGN_UP:
                    //到会议报名

                    QRChecInModel qrChecInModel1 =  GsonUtil.fromJson(s,QRChecInModel.class);
                    EventBus.getDefault().post(new SignInOrUpEvent(qrChecInModel1));
                    break;
                case RESORCE_SEND:
                    //到物资发放

                    QRresourceSend qRresourceSend = GsonUtil.fromJson(s,QRresourceSend.class);
                    QRResourceSendEvent event =  new QRResourceSendEvent(qRresourceSend);
                    event.type = 2;
                    EventBus.getDefault().post(event);
                    break;
                case RESOURCE_TRANSFER:
                    //到物资交接
                    QRresourceSend q = GsonUtil.fromJson(s,QRresourceSend.class);
                    QRResourceSendEvent e =  new QRResourceSendEvent(q);
                    e.type = 1;
                    EventBus.getDefault().post(e);

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtil.showShortMessage(this, "请允许相机权限");
    }
}
