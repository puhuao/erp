package com.managesystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kelin.calendarlistview.library.CalendarHelper;
import com.kelin.calendarlistview.library.CalendarListView;
import com.managesystem.R;
import com.managesystem.adapter.CalendarItemAdapter;
import com.managesystem.adapter.DayNewsListAdapter;
import com.managesystem.model.News;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 */
public class MeetingRoomFragment extends CommonFragment {
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat("yyyy年MM月");

    @Bind(R.id.calendar_list_view)
    CalendarListView calendarListView;
    private DayNewsListAdapter dayNewsListAdapter;
    private CalendarItemAdapter calendarItemAdapter;
    //key:date "yyyy-mm-dd" format.
    private TreeMap<String, List<News.StoriesBean>> listTreeMap = new TreeMap<>();

    private Handler handler = new Handler();
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_room, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        dayNewsListAdapter = new DayNewsListAdapter(getContext());
        calendarItemAdapter = new CalendarItemAdapter(getContext());
        calendarListView.setCalendarListViewAdapter(calendarItemAdapter, dayNewsListAdapter);

        // set start time,just for test.
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -7);
        loadNewsList(DAY_FORMAT.format(calendar.getTime()));

        calendarListView.setOnListPullListener(new CalendarListView.onListPullListener() {
            @Override
            public void onRefresh() {
//                String date = listTreeMap.firstKey();
//                Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
//                calendar.add(Calendar.MONTH, -1);
//                calendar.set(Calendar.DAY_OF_MONTH, 1);
//                loadNewsList(DAY_FORMAT.format(calendar.getTime()));
            }

            @Override
            public void onLoadMore() {
                String date = listTreeMap.lastKey();
                Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
//                loadNewsList(DAY_FORMAT.format(calendar.getTime()));
            }
        });

        calendarListView.setOnMonthChangedListener(new CalendarListView.OnMonthChangedListener() {
            @Override
            public void onMonthChanged(String yearMonth) {
                Calendar calendar = CalendarHelper.getCalendarByYearMonth(yearMonth);
            }
        });

        calendarListView.setOnCalendarViewItemClickListener(new CalendarListView.OnCalendarViewItemClickListener() {
            @Override
            public void onDateSelected(View View, String selectedDate, int listSection, SelectedDateRegion selectedDateRegion) {

            }
        });
    }

    private void loadNewsList(String format) {
        Calendar calendar = getCalendarByYearMonthDay(format);
        String key = CalendarHelper.YEAR_MONTH_FORMAT.format(calendar.getTime());

        // just not care about how data to create.
        Random random = new Random();
        final List<String> set = new ArrayList<>();
        while (set.size() < 5) {
            int i = random.nextInt(29);
            if (i > 0) {
                if (!set.contains(key + "-" + i)) {
                    if (i < 10) {
                        set.add(key + "-0" + i);
                    } else {
                        set.add(key + "-" + i);
                    }
                }
            }
        }
    }

    public static Calendar getCalendarByYearMonthDay(String yearMonthDay) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(DAY_FORMAT.parse(yearMonthDay).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
