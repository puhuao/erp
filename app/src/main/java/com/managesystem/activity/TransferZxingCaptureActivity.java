package com.managesystem.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.R;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by puhua on 2016/3/28.
 *
 * @二维码扫描页面_交接
 */
public class TransferZxingCaptureActivity extends Activity implements QRCodeView.Delegate {
    private static final String TAG = TransferZxingCaptureActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private QRCodeView mQRCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bd = getIntent().getExtras();
        setContentView(R.layout.layout_zxing);
        initView();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        }else{
            mayRequestContacts();
        }

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
        EventBus.getDefault().unregister(this);
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
            if ("4".equals(type)) {
                String s = jsonObject.getString("param");
                QRresourceSend q = GsonUtil.fromJson(s, QRresourceSend.class);
                String url = Urls.RESOURCE_SEND_TRANSFER + q.getPStr();
                DialogCallback callback = new DialogCallback<String>(TransferZxingCaptureActivity.this, String.class) {
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        ToastUtil.showShortMessage(TransferZxingCaptureActivity.this, "网络错误");
                    }

                    @Override
                    public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                        if (o != null) {
                            ToastUtil.showShortMessage(TransferZxingCaptureActivity.this, "物资交接成功");
                        }
                    }
                };
                IConfig config = BaseApplication.getInstance().getCurrentConfig();
                StringBuilder sb = new StringBuilder(url);
                sb.append("&");
                UrlUtils.getInstance(sb)
                        .praseToUrl("toUserId", config.getString("userId", ""))
                        .removeLastWord();
                OkHttpUtils.post(sb.toString())//
                        .tag(this)//
                        .execute(callback);
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

    private static final int REQUEST_READ_CONTACTS = 0;
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setTitle("请提供相机权限");
            builder.setMessage("请提供相机权限");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_READ_CONTACTS);
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                setContentView(R.layout.layout_zxing);
                initView();
            }else{
                finish();
                ToastUtil.showShortMessage(this,"没有授权");
            }
        }
    }

}
