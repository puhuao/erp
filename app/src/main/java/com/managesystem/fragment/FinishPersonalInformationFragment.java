package com.managesystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.DepartmentSelectEvent;
import com.managesystem.model.Department;
import com.managesystem.model.PersonalInfo;
import com.managesystem.model.RegisterInfo;
import com.managesystem.popupwindow.DepartmentSelectPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 */
public class FinishPersonalInformationFragment extends CommonFragment {
    @Bind(R.id.tv_department)
    TextView textViewDepartment;
    @Bind(R.id.et_name)
    EditText editTextName;
    @Bind(R.id.et_door_number)
    EditText editTextDoorNumber;
    @Bind(R.id.et_telephone_number)
    EditText editTextTelephoneNumber;
    @Bind(R.id.et_area)
    EditText editTextArea;
    @Bind(R.id.et_floor_number)
    EditText editTextFloorNumber;
    RegisterInfo registerInfo;
    private Department selectedDepartment;
    private List<Department> departments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_finish_personal_infomation, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        departments = new ArrayList<>();
        registerInfo = new RegisterInfo();
        Bundle bundle = (Bundle) getmDataIn();
        registerInfo.setPhone(bundle.getString("phoneNumber"));
        registerInfo.setCode(bundle.getString("validCode"));
        registerInfo.setPassword(bundle.getString("password"));
        setHeaderTitle(getStringFromResource(R.string.page_name_register));
    }

    private void getDepartMents(){
        StringBuilder sb = new StringBuilder(Urls.GET_DEPARTMENT);
        UrlUtils.getInstance(sb);
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    departments.addAll(GsonUtil.fromJsonList(o, Department.class));
                    DepartmentSelectPopupwindow popupwindow = new DepartmentSelectPopupwindow(getContext(),departments);
                    popupwindow.showPopupwindow(textViewDepartment);
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @OnClick({R.id.fab,R.id.tv_department})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                registerInfo.setName(editTextName.getText().toString());
                if (StringUtils.isBlank(registerInfo.getName())){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.hint_input_name));
                    return;
                }
                registerInfo.setOfficeNo(editTextDoorNumber.getText().toString());
                if (selectedDepartment==null){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.hint_text_department));
                    break;
                }
                registerInfo.setArea(editTextArea.getText().toString());
                if (StringUtils.isBlank(registerInfo.getArea())){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.hint_input_area));
                    break;
                }
                registerInfo.setFloor(editTextFloorNumber.getText().toString());
                if (StringUtils.isBlank(registerInfo.getFloor())){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.hint_input_floor_number));
                    break;
                }
                registerInfo.setFloor(editTextTelephoneNumber.getText().toString());
                register();
                break;
            case R.id.tv_department:
                if (departments.size()>0){
                    DepartmentSelectPopupwindow popupwindow = new DepartmentSelectPopupwindow(getContext(),departments);
                    popupwindow.showPopupwindow(textViewDepartment);
                }else{
                    getDepartMents();
                }
                break;
        }
    }

    @Subscribe
    public void onEvent(DepartmentSelectEvent event){
        selectedDepartment = event.getDepartment();
        textViewDepartment.setText(event.getDepartment().getDepartmentName());
    }

    private void register(){
        StringBuilder sb = new StringBuilder(Urls.REGISTER);
        String s = UrlUtils.getInstance(sb).praseToUrl("phone", registerInfo.getPhone()).praseToUrl("name",registerInfo.getName())
        .praseToUrl("departmentId",selectedDepartment.getDepartmentId()).praseToUrl("officeNo",registerInfo.getOfficeNo())
        .praseToUrl("cphone",registerInfo.getCphone()).praseToUrl("area",registerInfo.getArea())
        .praseToUrl("floor",registerInfo.getFloor()).praseToUrl("deviceType","1").praseToUrl("code",registerInfo.getCode())
                .praseToUrl("password", registerInfo.getPassword())
//        .praseToUrl("registerId","")
        .removeLastWord();
        DialogCallback callback = new DialogCallback<PersonalInfo>(getContext(), PersonalInfo.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, PersonalInfo personalInfo, Request request, @Nullable Response response) {
                if (personalInfo!=null){
                    ToastUtil.showShortMessage(getContext(),"注册成功，等待后台验证");
                    getContext().popToRoot(null);
                }
            }
        };
        OkHttpUtils.post(s)//
                .tag(this)//
                .execute(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
