package com.managesystem.fragment.meeting;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.HorizontalListViewAdapter;
import com.managesystem.adapter.MeetingRoomRecordAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingRoomConditionSelectEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.HorizontalCalenderModel;
import com.managesystem.model.MeetingRoom;
import com.managesystem.model.MeetingRoomDetail;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.recycler.OnRecyclerItemClickListener;
import com.wksc.framwork.baseui.ActivityManager;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 */
public class MeetingRoomFragment extends BaseListRefreshFragment<MeetingRoomDetail> {
    /*@Bind(R.id.horizontal_list_view)
    HorizontalListView horizontalListView;*/
    @Bind(R.id.search)
    EditText etSearch;
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.title_bar_title)
    TextView title;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.img_search)
            ImageView imgSearch;
    @Bind(R.id.room_select)
            TextView roomSelect;
    HorizontalListViewAdapter adapter;
    MeetingRoomRecordAdapter meetingRoomRecordAdapter;
    ArrayList<HorizontalCalenderModel> models = new ArrayList<>();
    ArrayList<MeetingRoomDetail> details = new ArrayList<>();
    private MeetingSelectCondition meetingSelectCondition;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Boolean isSearch = false;
    float x;
    private ArrayList<MeetingRoom> meetingRooms = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            getContext().getWindow().getDecorView().setSystemUiVisibility(options);

            getContext().getWindow().setStatusBarColor(getResources().getColor(com.wksc.framwork.R.color.transparent));

        }
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_room, null);
        ButterKnife.bind(this, container);
        initView();

        return container;
    }


    int firstVisialePositon = 0;

    private void initView() {
        isfirstFragment = true;
        int currentPosition = 0;
        Calendar calendar = Calendar.getInstance();
        hideTitleBar();
        title.setText(formatter.format(calendar.getTime()));
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getTitleHeaderBar().setBackground(getContext().getResources().getDrawable(R.drawable.horizontal_background));
        }

        String d = formatter.format(calendar.getTime());
        int month = calendar.get(Calendar.MONTH);
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int wd = calendar.get(Calendar.DAY_OF_WEEK);
        HorizontalCalenderModel model = getMondayOFWeek(calendar);
        calendar.add(Calendar.DATE, -30);
        for (int i = 0; i < 60; i++) {
            HorizontalCalenderModel horizontalCalenderModel = new HorizontalCalenderModel();
            int m = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            horizontalCalenderModel.name = day;
            horizontalCalenderModel.weekDay = weekDay;
            horizontalCalenderModel.calendar = formatter.format(calendar.getTime());
            models.add(horizontalCalenderModel);
            if (model.month == m && model.name == day) {
                firstVisialePositon = i;
            }
            if (m == month && today == day)
                currentPosition = i;
            calendar.add(Calendar.DATE, 1);
        }
        meetingSelectCondition = new MeetingSelectCondition();

        meetingSelectCondition.setDate(d);
        adapter = new HorizontalListViewAdapter(getContext());
        adapter.currentPositon = currentPosition;
        final LinearLayoutManager rvManager = new LinearLayoutManager(getContext());
        rvManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(rvManager);
        adapter.setmList(models);
        recyclerView.setAdapter(adapter);
        final int finalCurrentPosition = currentPosition;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        x = dm.widthPixels / 7;
        adapter.setX(x);
        rvManager.smoothScrollToPosition(recyclerView, null, finalCurrentPosition + 6);
        meetingRoomRecordAdapter = new MeetingRoomRecordAdapter(getContext());
        setData(details, meetingRoomRecordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != details.size()) {

                    meetingSelectCondition.setMeetingId(details.get(position).getMeetingId());
                    getContext().pushFragmentToBackStack(MeetingRoomTakenInformationFragment.class, meetingSelectCondition);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int position = vh.getAdapterPosition();
                title.setText(models.get(position).calendar);
                meetingSelectCondition.setDate(models.get(position).calendar);
                meetingSelectCondition.setMeetingName("");
                adapter.currentPositon = position;
                adapter.notifyDataSetChanged();
                rvManager.smoothScrollToPosition(recyclerView, null, position);
//                horizontalListView.scrollTo((int) (position*x));
                roomSelect.setText("");
                pageNo = 1;
                loadMore(1);
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearch = true;
                meetingSelectCondition.setMeetingName(etSearch.getText().toString());
                pageNo = 1;
                loadMore(1);
            }
        });
        roomSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (meetingRooms.size() > 0) {
//                    hideSoftInput(v);
//                    getContext().pushFragmentToBackStack(MeetingRoomConditionSelectFragment.class, meetingRooms);
//                } else {
                    hideSoftInput(v);
                    getMeetingRooms();
//                }
            }
        });

    }


    // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
    private int getMondayPlus(Calendar cd) {
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK); // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    public HorizontalCalenderModel getMondayOFWeek(Calendar calendar) {
        HorizontalCalenderModel model = new HorizontalCalenderModel();
        int mondayPlus = getMondayPlus(calendar);
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        model.month = currentDate.get(Calendar.MONTH);
        model.name = currentDate.get(Calendar.DAY_OF_MONTH);
        model.weekDay = currentDate.get(Calendar.DAY_OF_WEEK);
        return model;
    }

    @Override
    public void loadMore(int pageNo) {
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo", String.valueOf(pageNo))
                .praseToUrl("pageSize", meetingSelectCondition.getPageSize())
                .praseToUrl("meetingName", meetingSelectCondition.getMeetingName())
                .praseToUrl("date", meetingSelectCondition.getDate())
                .removeLastWord();
        excute(sb.toString(), MeetingRoomDetail.class);
    }


    @Override
    public void onLeave() {
        super.onLeave();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            getContext().getWindow().getDecorView().setSystemUiVisibility(options);

            getContext().getWindow().setStatusBarColor(getResources().getColor(com.wksc.framwork.R.color.black));

        }
    }
    private MeetingRoom meetingRoom;
    @Subscribe
    public void onEvent(MeetingRoomConditionSelectEvent event) {
        meetingRoom = event.getDepartment();
        roomSelect.setText(meetingRoom.getMeetingroomName());
        isSearch = true;
        if (StringUtils.isBlank(meetingRoom.getMeetingroomId())){
            meetingSelectCondition.setMeetingName("");
        }else{
            meetingSelectCondition.setMeetingName(meetingRoom.getMeetingroomName());
        }
        pageNo = 1;
        loadMore(1);
    }

    private void getMeetingRooms() {
        StringBuilder sb = new StringBuilder(Urls.MEETING_ROOM);
        UrlUtils.getInstance(sb);
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    meetingRooms.clear();
                    meetingRooms.addAll(GsonUtil.fromJsonList(o, MeetingRoom.class));
                    MeetingRoom room = new MeetingRoom();
                    room.setMeetingroomName("全部");
                    meetingRooms.add(room);
                    getContext().pushFragmentToBackStack(MeetingRoomConditionSelectFragment.class, meetingRooms);
                }
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
}
