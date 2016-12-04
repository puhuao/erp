package com.managesystem.fragment.phoneBooke;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.ebook.DividerDecoration;
import com.managesystem.fragment.ebook.PersonListAdapter;
import com.managesystem.model.Department;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.sortView.CharacterParser;
import com.managesystem.widegt.sortView.PinyinComparator;
import com.managesystem.widegt.sortView.SideBar;
import com.managesystem.widegt.sortView.SortAdapter;
import com.managesystem.widegt.sortView.SortModel;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentPhonenumberList extends CommonFragment {
    @Bind(R.id.country_lvcountry)
    ListView sortListView;
    @Bind(R.id.sidrbar)
    SideBar sideBar;
    @Bind(R.id.dialog)
    TextView dialog;
    private PhonenumberSortAdapter adapter;
    private List<PersonalInfo> departments = new ArrayList<>();
    Department department;
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

        department = (Department) getmDataIn();
        setHeaderTitle(department.departmentName);
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
        getPhoneNumbers();
    }



    private void getPhoneNumbers(){
        StringBuilder sb = new StringBuilder(Urls.PHONE_NUMBERS);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        String userid = null;
        userid = config.getString("userId", "");
        UrlUtils.getInstance(sb)
                .praseToUrl("userId",userid)
                .praseToUrl("departmentId",department.getDepartmentId())
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
                    departments.addAll(GsonUtil.fromJsonList(o, PersonalInfo.class));
                    List<PersonalInfo> list = filledData(departments);
                    Collections.sort(list, pinyinComparator);
                    adapter = new PhonenumberSortAdapter(getContext(), list);
                    sortListView.setAdapter(adapter);
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
    private List<PersonalInfo> filledData(List<PersonalInfo> date){
        List<PersonalInfo> mSortList = new ArrayList<PersonalInfo>();

        for(int i=0; i<date.size(); i++){
            PersonalInfo sortModel = new PersonalInfo();
            sortModel.setDepartmentName(date.get(i).getName());
            //����ת����ƴ��
            String pinyin = characterParser.getSelling(date.get(i).getName());
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
