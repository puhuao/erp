package com.managesystem.fragment.goodnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.ImageActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.GoodeNewsCheckEvent;
import com.managesystem.event.OnGoodNewsSignIn;
import com.managesystem.model.GoodNews;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.multiImageView.MultiImageView;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 好消息_详情页面
 */
public class GoodNewsDetailFragment extends CommonFragment {
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.sign_in)
    Button sign;
    @Bind(R.id.multi_imageView)
    MultiImageView multiImageView;
GoodNews goodNew;
    ArrayList<String> imgs = new ArrayList<>();
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_good_new_detail, null);
        ButterKnife.bind(this, container);
        goodNew = (GoodNews) getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(goodNew.getTitle());
        content.setText(goodNew.getInfor());
        if (goodNew.isIsApply()){
            sign.setText("已报名");
            sign.setBackgroundColor(getContext().getResources().getColor(R.color.text_hint));
            sign.setClickable(false);
        }else{
            check(goodNew.getWealId());
        }

        if (goodNew.getPics() != null) {

            for (String s :
                    goodNew.getPics()) {
                imgs.add(Urls.GETPICS + s);
            }
            multiImageView.setVisibility(View.VISIBLE);
            multiImageView.setList(imgs);
        }
        multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), ImageActivity.class);
                Bundle bd = null;
                if (bd == null) {
                    bd = new Bundle();
                }
                bd.putStringArrayList("list", imgs);
                bd.putInt("position", position);
                intent.putExtras(bd);
                getContext().startActivity(intent);
            }
        });
    }

    @OnClick({R.id.sign_in})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                signIn(goodNew.getWealId());
                break;
        }
        }

    private void signIn(String id){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.WEAL_SIGN);
        UrlUtils.getInstance(sb) .praseToUrl("wealId", id)
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("type","2")//报名
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
                    ToastUtil.showShortMessage(getContext(),"报名成功");
                    sign.setText("已报名");
                    sign.setBackgroundColor(getContext().getResources().getColor(R.color.text_hint));
                    sign.setClickable(false);
                    EventBus.getDefault().post(new OnGoodNewsSignIn());
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void check(String id){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.WEAL_SIGN);
        UrlUtils.getInstance(sb) .praseToUrl("wealId", id)
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("type","1")//报名
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
//                    ToastUtil.showShortMessage(getContext(),"报名成功");
                    EventBus.getDefault().post(new GoodeNewsCheckEvent());
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
