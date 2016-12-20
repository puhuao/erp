package com.managesystem.fragment.pps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.PPSCommentAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingRoomSelectEvent;
import com.managesystem.event.PPSDeleteEvent;
import com.managesystem.event.PPSListUpdateEvent;
import com.managesystem.fragment.BaseNestListRefreshFragment;
import com.managesystem.model.PPSComment;
import com.managesystem.model.PPSModel;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.EmojiTextView;
import com.managesystem.widegt.multiImageView.MultiImageView;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.widget.CircleImageView;

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
public class PPSDetailFragment extends BaseNestListRefreshFragment<PPSComment> {
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
    @Bind(R.id.delete)
    TextView delete;
    @Bind(R.id.ll_edit_comment)
    View llEditComment;
    @Bind(R.id.cirimg_user)
    CircleImageView header;

    private String reactContent;
    PPSModel ppsModel;
    ArrayList<PPSComment> ppsComments = new ArrayList<>();
    PPSCommentAdapter ppsCommentAdapter;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        userId = config.getString("userId", "");
        EventBus.getDefault().register(this);
        getContext().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_pps_detail, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @Subscribe
    public void onEvent(MeetingRoomSelectEvent event) {
    }

    @OnClick({R.id.zan, R.id.react, R.id.delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zan:
                praise();
                break;
            case R.id.react:
                reactContent = etReact.getText().toString();
                if (StringUtils.isBlank(reactContent)) {
                    ToastUtil.showShortMessage(getContext(), "请输入回复内容");
                    break;
                }
                react();
                break;
            case R.id.delete:
                delete();
                break;
        }
    }

    private void initView() {
        isfirstFragment = true;
        ppsModel = (PPSModel) getmDataIn();
        ppsCommentAdapter = new PPSCommentAdapter(getContext());
        setData(ppsComments, ppsCommentAdapter);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEditComment.setVisibility(View.VISIBLE);
            }
        });
        detail();
        setHeaderTitle(ppsModel.getTitle());
        String pic = null;
        if (!StringUtils.isBlank(ppsModel.getHeadPic())){
            pic = Urls.GETPICS+ppsModel.getHeadPic();
        }
        Glide.with(getContext()).load(pic)
                .asBitmap().centerCrop()
                .error(R.drawable.ic_header_defalt)
                .placeholder(R.drawable.ic_header_defalt)
                .into(new BitmapImageViewTarget(header) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                       header.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    private void praise() {

        StringBuilder sb = new StringBuilder(Urls.PPS_ZAN);
        UrlUtils.getInstance(sb).praseToUrl("topicId", ppsModel.getTopicId())
                .praseToUrl("userId", userId)
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    ToastUtil.showShortMessage(getContext(), "点赞成功");
                    detail();
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void react() {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.PPS_COMMECT);
        UrlUtils.getInstance(sb).praseToUrl("topicId", ppsModel.getTopicId())
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("content", reactContent)
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                ToastUtil.showShortMessage(getContext(), "回帖成功");
                hideSoftInput(etReact);
                etReact.setText("");
                handler.sendEmptyMessage(0);
            }
        };
        callback.setDialogHide();
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
        StringBuilder sb = new StringBuilder(Urls.PPS_COMMENT_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo", String.valueOf(pageNo))
                .praseToUrl("topicId", ppsModel.getTopicId())
                .praseToUrl("pageSize", "20")
                .removeLastWord();
        excute(sb.toString(), PPSComment.class);
    }

    private void delete() {
        StringBuilder sb = new StringBuilder(Urls.DELETE_TOPIC);
        UrlUtils.getInstance(sb)
                .praseToUrl("userId", userId)
                .praseToUrl("topicId", ppsModel.getTopicId())
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    ToastUtil.showShortMessage(getContext(), "删除动态成功");
                    EventBus.getDefault().post(new PPSListUpdateEvent());
                    getContext().popTopFragment(null);
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);

    }

    private void detail() {
        StringBuilder sb = new StringBuilder(Urls.PPS_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo", String.valueOf(pageNo))
                .praseToUrl("userId", userId)
                .praseToUrl("pageSize", "20")
                .praseToUrl("topicId", ppsModel.getTopicId())
                .removeLastWord();

        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    ToastUtil.showShortMessage(getContext(), "获取详情成功");
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        String list = jsonObject.getString("list");
                        List<PPSModel> ppsModels = GsonUtil.fromJsonList(list, PPSModel.class);
                        ppsModel = ppsModels.get(0);
                        name.setText(ppsModel.getName());
                        time.setText(ppsModel.getCtime());
                        content.setText(ppsModel.getContent());
                        if (userId.equals(ppsModel.getUserId())) {
                            delete.setVisibility(View.VISIBLE);
                        } else {
                            delete.setVisibility(View.GONE);
                        }
                        if (ppsModel.getPics() != null) {
                            List<String> imgs = new ArrayList<>();
                            for (String s :
                                    ppsModel.getPics()) {
                                imgs.add(Urls.GETPICS + s);
                            }
                            multiImageView.setList(imgs);
                        }
                        check.setText(String.valueOf(ppsModel.getScanCount()));
                        if (ppsModel.getPraise()) {
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
        callback.setDialogHide();
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
