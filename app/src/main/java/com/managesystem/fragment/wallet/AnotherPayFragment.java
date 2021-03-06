package com.managesystem.fragment.wallet;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.PaySuccessEvent;
import com.managesystem.fragment.loginAndRegister.FinishPersonalInformationFragment;
import com.managesystem.model.Account;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 其他支付页面
 */
public class AnotherPayFragment extends CommonFragment {
    @Bind(R.id.ll_custom_pay)
    RelativeLayout llCustomPay;
    @Bind(R.id.cancel)
    Button cancel;
    @Bind(R.id.sure)
            Button sure;
    @Bind(R.id.edit_text)
            EditText editText;
    String QrCodeMessage;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_another_pay, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }
String money;
    @OnClick({R.id.make_qrcode,R.id.custom_pay,R.id.cancel,R.id.sure})
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.sure:
                money = editText.getText().toString();
                if (StringUtils.isBlank(money)){
                    ToastUtil.showShortMessage(getContext(),"请先输入消费金额");
                    break;
                }
                final CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                builder.setTitle("确认消费金额");
                builder.setMessage("本次消费"+money+"元");
                builder.setPositiveButton("确认支付", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        customPay(v,money);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.cancel:
                editText.clearComposingText();
                llCustomPay.setVisibility(View.GONE);
                break;
            case R.id.make_qrcode:
                getQrCode(v);
                break;
            case R.id.custom_pay:
                llCustomPay.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initView() {
        setHeaderTitle("其他支付");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 1) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(1);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getQrCode(final View v){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.GET_PAY_QRCODE);
        UrlUtils.getInstance(sb)
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    QrCodeMessage = o;
                    try {
                        Bitmap bitmap = CreateQrCode.createQRCode(GsonUtil.objectToJson(QrCodeMessage), 300);
                        QrcodeViewPopupwindow popupwindow = new QrcodeViewPopupwindow(getContext(),bitmap);
                        popupwindow.showPopupwindow(v);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void customPay(final View v,String money){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.SAVE_PAY_RECORD);
        UrlUtils.getInstance(sb).praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("type","3")//用餐
                .praseToUrl("money",String.valueOf(money))
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    ToastUtil.showShortMessage(getContext(),"消费成功");
                    EventBus.getDefault().post(new PaySuccessEvent());
                    getContext().popTopFragment(null);
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
