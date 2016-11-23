package com.managesystem.fragment.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.managesystem.adapter.ResourceApplyAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.ResourceApplyLost;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 物资申请记录
 */
public class ResourceApplyListFragment extends BaseListRefreshFragment<ResourceApplyLost> {
    ResourceApplyAdapter adapter;
    ArrayList<ResourceApplyLost> resourceApplyLosts = new ArrayList<>();
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
       hideTitleBar();
        adapter = new ResourceApplyAdapter(getContext());
        setData(resourceApplyLosts,adapter);
//        getRecords();
    }

//    private void getRecords(){
//
//        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
//            @Override
//            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
//                super.onError(isFromCache, call, response, e);
//                ToastUtil.showShortMessage(getContext(),"网络错误");
//            }
//
//            @Override
//            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
//                if (o!=null){
//                    try {
//                        JSONObject jsonObject = new JSONObject(o);
//                        String list = jsonObject.getString("list");
//                        resourceApplyLosts.addAll(GsonUtil.fromJsonList(list, ResourceApplyLost.class));
//                        adapter.notifyDataSetChanged();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        };
//        OkHttpUtils.get(sb.toString())//
//                .tag(this)//
//                .execute(callback);
//    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_APPLY_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("type","1")
                .removeLastWord();
        excute(sb.toString(),ResourceApplyLost.class);
    }
}
