package com.managesystem.fragment.loginAndRegister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.MainActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.MD5Utils;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by puhua on 2016/5/26.
 *
 * @
 */
public class LoginFragment extends CommonFragment {
    @Bind(R.id.et_username)
    EditText userName;
    @Bind(R.id.et_password)
    EditText passWord;
    private IConfig config;
    private String username;
    private String password;
    private Boolean isAotuLogin = false;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTitleHeaderBar.setVisibility(View.GONE);
        final View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, view);
        config = BaseApplication.getInstance().getCurrentConfig();
        username = config.getString("username", "");
        password = config.getString("password","");
        userName.setText(username);
        passWord.setText(password);
        isAotuLogin = config.getBoolean("isLogin",false);
        if (isAotuLogin){
            doLogin();
        }
        return view;
    }
    @OnClick({R.id.fab,R.id.register_new_user,R.id.tv_forgot_password})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                doLogin();
                break;
            case R.id.register_new_user:
                getContext().pushFragmentToBackStack(RegisterFragment.class,null);
                break;
            case R.id.tv_forgot_password:
                getContext().pushFragmentToBackStack(ForgetPasswordFragment.class,null);
                break;

        }
    }


    private void doLogin() {

        username = userName.getText().toString();
        password = passWord.getText().toString();
        if (StringUtils.isBlank(username)) {
            ToastUtil.showShortMessage(getContext(), "请输入用户名");
            return;
        }
        if (StringUtils.isBlank(password)) {
            ToastUtil.showShortMessage(getContext(), "请输入密码");
            return;
        }
        StringBuilder sb = new StringBuilder(Urls.LOGIN);
        UrlUtils.getInstance(sb).praseToUrl("phone", username).praseToUrl("password", MD5Utils.encode(password))
                .removeLastWord();
        DialogCallback callback = new DialogCallback<PersonalInfo>(getContext(), PersonalInfo.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, PersonalInfo o, Request request, @Nullable Response response) {
                if (o!=null){
                    config.setString("username", username);
                    config.setString("userId",o.getUserId());
                    config.setString("password", password);
                    config.setString("roleName",o.getRoleName());
                    config.setString("name",o.getName());
                    config.setString("stationName",o.getStationName());
                    config.setString("department",o.getDepartmentName());
                    config.setString("cphone",o.getCphone());
                    config.setString("headerIcon",Urls.GETPICS+o.getHeadPic());
                    config.setBoolean("isLogin",true);
                    config.setString("sign",o.getSign());
                    config.setString("phone",o.getPhone());
                    if (o.getIspublish()==1){
                        config.setBoolean("ispublish",true);
                    }else{
                        config.setBoolean("ispublish",false);
                    }

                    JPushInterface.setAlias(getContext(), o.getUserId(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Log.i("TAG","jpush:设置别名成功");
                        }
                    });

                    startActivity(MainActivity.class);
                    getActivity().finish();
                }

            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
