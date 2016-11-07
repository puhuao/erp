package com.managesystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

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
                Bundle bundle = new Bundle();
                bundle.putString("phoneNumber",phoneNumber);
                bundle.putString("validCode",validCode);
                bundle.putString("password",password);
                getContext().pushFragmentToBackStack(FinishPersonalInformationFragment.class,bundle);
                break;
            case R.id.tv_get_valid_code:

                getVailidCode(phoneNumber);
                break;
        }
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
                    saveValidCode = o;
                    ToastUtil.showShortMessage(getContext(),"验证码已用短信的方式发送到你手机\n请注意查收");
                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
