package com.managesystem.fragment.phoneBooke;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.ebook.FragmentPhonenumberList;
import com.managesystem.model.Department;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.managesystem.widegt.sortView.CharacterParser;
import com.managesystem.widegt.sortView.PinyinComparator;
import com.managesystem.widegt.sortView.SideBar;
import com.managesystem.widegt.sortView.SortAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentDepartmentList extends CommonFragment {
    @Bind(R.id.country_lvcountry)
    ListView sortListView;
    @Bind(R.id.sidrbar)
    SideBar sideBar;
    @Bind(R.id.dialog)
    TextView dialog;
    private SortAdapter adapter;
    private List<Department> departments = new ArrayList<>();
    private CharacterParser characterParser;

    private PinyinComparator pinyinComparator;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_phone_book, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
    setHeaderTitle("部门");
        // Add the sticky headers decoration
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });
        getDepartMents();
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                getContext().pushFragmentToBackStack(com.managesystem.fragment.phoneBooke.FragmentPhonenumberList.class,departments.get(position));
            }
        });
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
                    List<Department> list = filledData(departments);
                    Collections.sort(list, pinyinComparator);
                    adapter = new SortAdapter(getContext(), list);
                    sortListView.setAdapter(adapter);
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private List<Department> filledData(List<Department> date){
        List<Department> mSortList = new ArrayList<Department>();

        for(int i=0; i<date.size(); i++){
            Department sortModel = new Department();
            sortModel.setDepartmentName(date.get(i).departmentName);
            //����ת����ƴ��
            String pinyin = characterParser.getSelling(date.get(i).departmentName);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }
}
