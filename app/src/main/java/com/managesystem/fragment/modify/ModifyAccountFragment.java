package com.managesystem.fragment.modify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.MeetingMsgDetailActivity;
import com.managesystem.adapter.MsgReadAdapter;
import com.managesystem.adapter.PopDepartmentAdapter;
import com.managesystem.adapter.PopMeetingRoomAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnDepartmentModifyedEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.fragment.loginAndRegister.FinishPersonalInformationFragment;
import com.managesystem.model.Department;
import com.managesystem.model.MeetingRoom;
import com.managesystem.model.Message;
import com.managesystem.model.PersonalInfo;
import com.managesystem.popupwindow.MeetingRoomSelectPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
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
 * 修改账号
 */
public class ModifyAccountFragment extends CommonFragment {
    @Bind(R.id.et_phone_number)
    EditText editTextPhoneNumber;
    @Bind(R.id.et_valid_code)
    EditText editTextValidCode;
    @Bind(R.id.tv_get_valid_code)
    TextView tvGetValidCode;
    @Bind(R.id.fab)
    Button fab;

    private IConfig config;
    private int step = 0;//0验证原号码阶段1修改阶段
    private String saveValidCode;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_modify_account, null);
        ButterKnife.bind(this, container);
        config = BaseApplication.getInstance().getCurrentConfig();
        initView();
        return container;
    }

    private void initView() {
        if (step==0){
            setHeaderTitle("验证原手机号");
        }else{
            setHeaderTitle("验证新手机号");
        }

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
                if (step == 0){
                    ToastUtil.showShortMessage(getContext(),"验证成功，继续输入新手机号码");
                    editTextPhoneNumber.setText("");
                    editTextPhoneNumber.requestFocus();
                    editTextValidCode.setText("");
                    fab.setText("修改");
                    step = 1;
                    initView();
                    break;
                }else{
                    modify(phoneNumber,validCode);
                }
                break;
            case R.id.tv_get_valid_code:
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[6,7,8]))\\d{8}$");
                Matcher m = p.matcher(phoneNumber);
                if (!m.matches()) {
                    ToastUtil.showShortMessage(getContext(), "请输入正确的手机号");
                    break;
                }
                if (step == 0){
                    getVailidCode(phoneNumber,"1");
                }else{
                    getVailidCode(phoneNumber,"0");
                }

                break;
        }
    }

    /*获取验证码*/
    private void getVailidCode(String phoneNumber,String type){
        StringBuilder sb = new StringBuilder(Urls.GET_VALIDCODE);
        UrlUtils.getInstance(sb).praseToUrl("phoneNum", phoneNumber)
                .praseToUrl("type",type).removeLastWord();
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


    private void modify(String newPhone,String validCode){
        StringBuilder sb = new StringBuilder(Urls.SAVE_USER);
        String s = UrlUtils.getInstance(sb)
                .praseToUrl("userId",config.getString("userId",""))
                .praseToUrl("phone", newPhone)
                .praseToUrl("code",validCode).praseToUrl("ispublish","1")
                .praseToUrl("type","3")
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
                    ToastUtil.showShortMessage(getContext(),"修改成功");
                    config.setString("roleName",o.getRoleName());
                    config.setString("name",o.getName());
                    config.setString("stationName",o.getStationName());
                    config.setString("department",o.getDepartmentName());
                    config.setString("cphone",o.getCphone());
                    config.setString("headerIcon",o.getHeadPic());
                    config.setBoolean("isLogin",true);
                    config.setString("phone",o.getPhone());
                    config.setString("sign",o.getSign());
                    EventBus.getDefault().post(new OnDepartmentModifyedEvent());
                    getContext().popTopFragment(null);
                }
            }
        };
        OkHttpUtils.post(s)//
                .tag(this)//
                .execute(callback);
    }

}
