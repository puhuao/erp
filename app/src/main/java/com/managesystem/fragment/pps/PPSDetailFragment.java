package com.managesystem.fragment.pps;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.widget.ExpandGridView;
import com.managesystem.R;
import com.managesystem.adapter.GridImageAdapter;
import com.managesystem.adapter.PPSCommentAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingRoomSelectEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.PPSComment;
import com.managesystem.model.PPSModel;
import com.managesystem.tools.GlideImageLoader;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.EmojiTextView;
import com.managesystem.widegt.multiImageView.MultiImageView;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

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
 * 发布新消息
 */
public class PPSDetailFragment extends BaseListRefreshFragment<PPSComment> {
    @Bind(R.id.tv_name)
    TextView name;
    @Bind(R.id.tv_time)
    TextView time;
    @Bind(R.id.tv_content)
    EmojiTextView content;
    @Bind(R.id.multi_imageView)
    MultiImageView multiImageView;
    @Bind(R.id.zan)
    TextView zan;
    @Bind(R.id.comment)
    TextView comment;
    @Bind(R.id.check)
    TextView check;
    @Bind(R.id.et_react)
            EditText etReact;
    @Bind(R.id.react)
            TextView react;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
private String reactContent;
    PPSModel ppsModel;
    ArrayList<PPSComment> ppsComments = new ArrayList<>();
    PPSCommentAdapter ppsCommentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_pps_detail, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @Subscribe
    public void onEvent(MeetingRoomSelectEvent event){
    }

    @OnClick({R.id.zan,R.id.react})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zan:
                praise();
                break;
            case R.id.react:
                reactContent = etReact.getText().toString();
                if (StringUtils.isBlank(reactContent)){
                    ToastUtil.showShortMessage(getContext(),"请输入回复内容");
                    break;
                }
                react();
                break;
        }
    }

    private void initView() {
        ppsModel = (PPSModel) getmDataIn();
        ppsCommentAdapter = new PPSCommentAdapter(getContext());
        setData(ppsComments,ppsCommentAdapter);
        detail();
        setHeaderTitle(ppsModel.getTitle());
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
                    }
                }
            });
        }
    }

    private void praise(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.PPS_ZAN);
        UrlUtils.getInstance(sb) .praseToUrl("topicId", ppsModel.getTopicId())
                .praseToUrl("userId", config.getString("userId", ""))
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
                ToastUtil.showShortMessage(getContext(),"点赞成功");
                detail();
            }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
    private void react(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.PPS_COMMECT);
        UrlUtils.getInstance(sb) .praseToUrl("topicId", ppsModel.getTopicId())
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("content",reactContent)
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                ToastUtil.showShortMessage(getContext(),"回帖成功");
                handler.sendEmptyMessage(0);
            }
        };

        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.PPS_COMMENT_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("topicId",ppsModel.getTopicId())
                .praseToUrl("pageSize","20")
                .removeLastWord();
        excute(sb.toString(),PPSComment.class);
    }

    private void detail(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.PPS_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("topicId",ppsModel.getTopicId())
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
                    ToastUtil.showShortMessage(getContext(),"获取详情成功");
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        String list = jsonObject.getString("list");
                        List<PPSModel> ppsModels = GsonUtil.fromJsonList(list, PPSModel.class);
                        ppsModel = ppsModels.get(0);
                        name.setText(ppsModel.getName());
                        time.setText(ppsModel.getCtime());
                        content.setText(ppsModel.getContent());

                        if (ppsModel.getPics()!=null){
                            List<String> imgs = new ArrayList<>();
                            for (String s :
                                    ppsModel.getPics()) {
                                imgs.add(Urls.GETPICS+s);
                            }
                            multiImageView.setList(imgs);
                        }
                        check.setText(String.valueOf(ppsModel.getScanCount()));
                        if (ppsModel.getPraise()){
                            Drawable drawable = getResources().getDrawable(R.drawable.img_zan_select);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                            zan.setCompoundDrawables(drawable, null, null, null);
                            zan.setFocusable(false);
                        }
                        zan.setText(String.valueOf(ppsModel.getPraiseCount()));
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
