package com.managesystem.fragment.resource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.GoodNewsActivity;
import com.managesystem.adapter.GoodNewsAdapter;
import com.managesystem.adapter.ResourcePersonAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.goodnews.GoodNewsSingInFragment;
import com.managesystem.model.GoodNews;
import com.managesystem.model.MeetingRoomDetail;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.NestedListView;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 *我的物资
 */
public class ResourcePersonalFragment extends CommonFragment {
    @Bind(R.id.list_view)
    ListView listView;
    ResourcePersonAdapter resourcePersonAdapter;
    ArrayList<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    View empty;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_person, null);
        empty = inflater.inflate(R.layout.empty_view, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.resource_my));
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.check_all));
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        resourcePersonAdapter = new ResourcePersonAdapter(getContext());
        listView.setAdapter(resourcePersonAdapter);
        ((ViewGroup) (listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        resourcePersonAdapter.setList(resourcePersonModels);
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check_all
            }
        });
        getResource();
    }


    private void getResource(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo","1")
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("keyword","")
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
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        String list = jsonObject.getString("list");
                        resourcePersonModels.addAll(GsonUtil.fromJsonList(list, ResourcePersonModel.class));
                        resourcePersonAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
