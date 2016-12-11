package com.managesystem.update;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.tools.NetWorkTool;
import com.wksc.framwork.util.ToastUtil;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


public class ApiClient {

	private static ApiClient manger;

	private Context context;

	private ApiClient(Context context) {
		this.context = context;
	}

	public static ApiClient getInstance(Context context) {
		if (manger == null)
			manger = new ApiClient(context);

		return manger;
	}

	public Update getVersionInfo() {
//		StringBuilder sb = new StringBuilder(Urls.UPDATEINFO);
////        UrlUtils.getInstance().addSession(sb, config);
//		if (!NetWorkTool.isNetworkAvailable((Activity) context)) {
//			ToastUtil.showShortMessage(context, "网络错误");
//			return null;
//		}

//		DialogCallback dialogCallback = new DialogCallback<UpdateInfo>((Activity) context, UpdateInfo.class) {
//
//			@Override
//			public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
//				super.onError(isFromCache, call, response, e);
//			}
//
//			@Override
//			public void onResponse(boolean isFromCache, UpdateInfo o, Request request, @Nullable Response response) {
//				if (o != null) {
//					PackageManager manager = context.getPackageManager();
//					try {
//						int remoteVersionCode = Integer.valueOf(o.version);
//						PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//						int versionCode = info.versionCode;
////                        ToastUtil.showShortMessage(getActivity(), "versonCode=" + remoteVersionCode + " thisVer"
////                                + versionCode);
//						if (remoteVersionCode > versionCode) {
//
//						}
//					} catch (PackageManager.NameNotFoundException e) {
//						e.printStackTrace();
//					}
//
//				}
//			}
//		};
//		dialogCallback.setDialogHide();
//		OkHttpUtils.post(sb.toString())//
//				.tag(this)//
//				.execute(dialogCallback);
		return null;
	}
}
