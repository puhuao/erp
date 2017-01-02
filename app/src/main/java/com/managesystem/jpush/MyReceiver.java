package com.managesystem.jpush;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import com.managesystem.activity.MeetingMsgDetailActivity;
import com.managesystem.event.UpdateMsgListEvent;
import com.managesystem.model.Message;
import com.wksc.framwork.util.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction());

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
		}else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			System.out.println("收到了通知。消息内容是：" + bundle.getString("cn.jpush.android.ALERT"));
			EventBus.getDefault().post(new UpdateMsgListEvent());
			if(isApplicationBroughtToBackground(context)){
				JPushInterface.clearAllNotifications(context);
			}
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			System.out.println("用户点击打开了通知" + bundle.getString("cn.jpush.android.ALERT"));
			JPushInterface.clearAllNotifications(context);
			String s = bundle.getString("cn.jpush.android.EXTRA");
			try {
				JSONObject jsonObject = new JSONObject(s);
			} catch (JSONException e) {
				e.printStackTrace();
			}
//			String alert = bundle.getString("cn.jpush.android.ALERT");
//			try {
//				JSONObject object = new JSONObject(alert);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			Message message = GsonUtil.fromJson(s,Message.class);
			Intent i = new Intent(context, MeetingMsgDetailActivity.class);
			i.putExtra("obj",message);
			i.putExtra("flag",0);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		} else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}

	public boolean isApplicationBroughtToBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (tasks != null && !tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
//			Debug.i(TAG, "topActivity:" + topActivity.flattenToString());
//			Debug.f(TAG, "topActivity:" + topActivity.flattenToString());
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
