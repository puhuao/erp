package com.managesystem.fragment.loginAndRegister;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import java.util.logging.Handler;
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
 */
public class RegisterFragment extends CommonFragment {
    @Bind(R.id.et_phone_number)
    EditText editTextPhoneNumber;
    @Bind(R.id.et_valid_code)
    EditText editTextValidCode;
    @Bind(R.id.et_password)
    EditText editTextPassword;
    @Bind(R.id.tv_get_valid_code)
    TextView textView;
    private String saveValidCode;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_register, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @OnClick({R.id.fab,R.id.tv_get_valid_code})
    public void onClick(View v) {
        String phoneNumber = editTextPhoneNumber.getText().toString();
        if (StringUtils.isBlank(phoneNumber)){
            ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.hint_input_phone_number));
            return;
        }
        switch (v.getId()) {
            case R.id.fab:
                String validCode = editTextValidCode.getText().toString();
                if (StringUtils.isBlank(validCode)){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.hint_input_valid_code));
                    break;
                }
                if (!saveValidCode.equals(validCode)){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.error_valid_code_increct));
                    break;
                }
                String password = editTextPassword.getText().toString();
                if (StringUtils.isBlank(password)){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.hint_input_password));
                    break;
                }

                if (!(password.length()>=6&&password.length()<=15)){
                    ToastUtil.showShortMessage(getContext(),"请输入6到15位密码");
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putString("phoneNumber",phoneNumber);
                bundle.putString("validCode",validCode);
                bundle.putString("password",password);
                getContext().pushFragmentToBackStack(FinishPersonalInformationFragment.class,bundle);
                break;
            case R.id.tv_get_valid_code:
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[6,7,8]))\\d{8}$");
                Matcher m = p.matcher(phoneNumber);
                if (!m.matches()) {
                    ToastUtil.showShortMessage(getContext(), "请输入正确的手机号");
                    break;
                }
                getVailidCode(phoneNumber);


                break;
        }
    }

    private void countdown(final TextView button){
        new CountDownTimer(120*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                button.setText(String.valueOf(millisUntilFinished/1000));
                button.setClickable(false);
                button.setEnabled(false);
            }

            public void onFinish() {
                button.setText("获取验证码");
                button.setClickable(true);
                button.setEnabled(true);
            }

        }.start();
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.page_name_register));
    }

/*获取验证码*/
    private void getVailidCode(String phoneNumber){
        StringBuilder sb = new StringBuilder(Urls.GET_VALIDCODE);
        UrlUtils.getInstance(sb).praseToUrl("phoneNum", phoneNumber).removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (!StringUtils.isBlank(o)){
                    countdown(textView);
                    saveValidCode = o;
                    ToastUtil.showShortMessage(getContext(),"验证码已用短信的方式发送到你手机\n请注意查收");
                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void getValidCode(String phone) {

        if (StringUtils.isBlank(phone)) {
            ToastUtil.showShortMessage(getContext(), "请输入手机号");
            return;
        }
    }


}
