package com.managesystem.fragment.loginAndRegister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

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
public class ForgetPasswordFragment extends CommonFragment {
    @Bind(R.id.et_phone_number)
    EditText editTextPhoneNumber;
    @Bind(R.id.et_valid_code)
    EditText editTextValidCode;
    @Bind(R.id.et_password)
    EditText editTextPassword;
    private String saveValidCode;
    @Bind(R.id.fab)
    Button fab;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_register, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.page_name_forget_password));
        fab.setText(getStringFromResource(R.string.confirm));
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        editTextPassword.setHint(getStringFromResource(R.string.hint_input_new_password));
        editTextPhoneNumber.setText(config.getString("username", ""));
        editTextPhoneNumber.setEnabled(false);
        editTextPhoneNumber.setFocusable(false);
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
                setNewPassword(phoneNumber,password,validCode);
                break;
            case R.id.tv_get_valid_code:

                getValidCode(phoneNumber);
                break;
        }
    }

    /*获取验证码*/
    private void getValidCode(String phoneNumber){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[6,7,8]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        if (!m.matches()) {
            ToastUtil.showShortMessage(getContext(), "请输入正确的手机号");
            return;
        }
        StringBuilder sb = new StringBuilder(Urls.GET_VALIDCODE);
        UrlUtils.getInstance(sb).praseToUrl("phoneNum", phoneNumber).praseToUrl("type","1").removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (!StringUtils.isBlank(o)){
                    saveValidCode = o;
                    ToastUtil.showShortMessage(getContext(),"验证码已用短信的方式发送到你手机\n请注意查收");
                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }


    private void setNewPassword(String phoneNumber, String password, String code){


        StringBuilder sb = new StringBuilder(Urls.REGISTER);
        String s = UrlUtils.getInstance(sb).praseToUrl("phone", phoneNumber)
                .praseToUrl("password",password)
                .praseToUrl("code", code)
                .praseToUrl("type","1")
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String personalInfo, Request request, @Nullable Response response) {
                if (personalInfo!=null){
                    ToastUtil.showShortMessage(getContext(),"修改密码成功\n请重新登录");
                    getContext().popToRoot(null);
                }
            }
        };
        OkHttpUtils.post(s)//
                .tag(this)//
                .execute(callback);
    }
}
