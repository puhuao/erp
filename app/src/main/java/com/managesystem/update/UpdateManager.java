package com.managesystem.update;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.managesystem.R;
import com.managesystem.model.RemoteVersion;
import com.managesystem.tools.LogUtil;
import com.wksc.framwork.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;


/**
 * 应用程序更新工具包
 * 
 * @author wanglin(linw@jumei.com) 2013-1-30下午2:45:14
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private static final int DIALOG_TYPE_LATEST = 0;

	private static final int DIALOG_TYPE_FAIL = 1;

	private static UpdateManager updateManager;

	private Context mContext;

	// 通知对话框
	private Dialog noticeDialog;

	// 下载对话框
	private Dialog downloadDialog;

	// '已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;

	// 进度条
	private ProgressBar mProgress;

	// 显示下载数值
	private TextView mProgressText;

	// 显示总数值
	private TextView mTotalText;

	// 查询动画
	private ProgressDialog mProDialog;

	public Dialog getNoticeDialog() {
		return noticeDialog;
	}

	public Dialog getDownloadDialog() {
		return downloadDialog;
	}

	public ProgressDialog getmProDialog() {
		return mProDialog;
	}

	// 进度值
	private int progress;

	// 下载线程
	private Thread downLoadThread;

	// 终止标记
	private boolean interceptFlag;

	// 提示语
	private String updateMsg = "";

	// 返回的安装包url
	private String apkUrl = "";

	// 下载包保存路径
	private String savePath = "";

	// apk保存完整路径
	private String apkFilePath = "";

	// 临时下载文件路径
	private String tmpFilePath = "";

	// 下载文件大小
	private String apkFileSize;

	// 已下载文件大小
	private String tmpFileSize;

	public String curVersionName = "";

	public int curVersionCode;

	private RemoteVersion.NewVersionBean mUpdate;

	private Boolean isInterceptable;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize);
				mTotalText.setText(apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	public static UpdateManager getUpdateManager() {
		if (updateManager == null) {
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		updateManager.isInterceptable = true;

		return updateManager;
	}

	public void setContext(Context mContext){
		this.mContext = mContext;
	}

//	/**
//	 * 检查App更新
//	 *
//	 * @param context
//	 * @param isShowMsg
//	 *            是否显示提示消息
//	 */
//	public void checkAppUpdate(Context context, final boolean isShowMsg) {
//		this.mContext = context;
//		getCurrentVersion();
//		if (isShowMsg) {
//			if (mProDialog == null)
//				mProDialog = ProgressDialog.show(mContext, null, "正在检测，请稍后...",
//						true, true);
//			else if (mProDialog.isShowing()
//					|| (latestOrFailDialog != null && latestOrFailDialog
//							.isShowing()))
//				return;
//		}
//		final Handler handler = new Handler() {
//			public void handleMessage(Message msg) {
//				// 进度条对话框不显示 - 检测结果也不显示
//				if (mProDialog != null && !mProDialog.isShowing()) {
//					return;
//				}
//				// 关闭并释放释放进度条对话框
//				if (isShowMsg && mProDialog != null) {
//					mProDialog.dismiss();
//					mProDialog = null;
//				}
//				// 显示检测结果
//				if (msg.what == 1) {
//					mUpdate = (Update) msg.obj;
//					if (mUpdate != null) {
//						if (curVersionCode < Integer.parseInt(mUpdate.vcode)) {
//							apkUrl = mUpdate.url;
//							updateMsg = mUpdate.note;
//							String[] s = curVersionName.split("\\.");
//							int n = Integer.valueOf(s[0]);
//							mUpdate.vname = mUpdate.vname.substring(1);
//							String[] ss = mUpdate.vname.split("\\.");
//							int m = Integer.valueOf(ss[0]);
//
//							if (m > n) {
//								isInterceptable = false;
//							}
//
//							LogUtil.info("m=" + m + " n=" + n);
//							showNoticeDialog();
//						} else if (isShowMsg) {
//							showLatestOrFailDialog(DIALOG_TYPE_LATEST);
//						}
//					}
//				} else if (isShowMsg) {
//					showLatestOrFailDialog(DIALOG_TYPE_FAIL);
//				}
//			}
//		};
//		new Thread() {
//			public void run() {
//				Message msg = new Message();
//				try {
//					// 从服务端获取JSON数据信息！
//					Update com.managesystem.update = ApiClient.getInstance(mContext)
//							.getVersionInfo();
//
//					LogUtil.info("com.managesystem.update----->" + com.managesystem.update);
//					msg.what = 1;
//					msg.obj = com.managesystem.update;
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				handler.sendMessage(msg);
//			}
//		}.start();
//	}

	/**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			// 关闭并释放之前的对话框
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		Builder builder = new Builder(mContext);
		builder.setTitle("系统提示");
		if (dialogType == DIALOG_TYPE_LATEST) {
			// builder.setMessage("您当前已经是最新版本");
			Toast.makeText(mContext, "您当前已经是最新版本！", Toast.LENGTH_SHORT).show();
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			builder.setMessage("无法获取版本更新信息");
			builder.setPositiveButton("确定", null);
			latestOrFailDialog = builder.create();
			latestOrFailDialog.show();
		}

	}

	/**
	 * 获取当前客户端版本信息
	 */
	public void getCurrentVersion() {
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	public void setUpdateInfo(RemoteVersion.NewVersionBean info){
		this.mUpdate = info;
	}

	/**
	 * 显示版本更新通知对话框
	 */
	public void showNoticeDialog() {
		final Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(mUpdate.getVersionName());
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialogDismiss(dialog);
				showDownloadDialog();
			}
		});
		if (!isInterceptable) {
			builder.setCancelable(false);

		}
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!isInterceptable) {
					ToastUtil.showShortMessage(mContext,
							"本次版本更新较大\n若不更新将会影响相关功能使用!");
					dialogShow(dialog);

				} else {
					dialog.dismiss();
				}
			}
		});
		noticeDialog = builder.create();

		if (!isInterceptable) {
			noticeDialog.setCanceledOnTouchOutside(false);
			noticeDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int arg1,
						KeyEvent arg2) {
					// TODO Auto-generated method stub
					LogUtil.info("keycode=" + arg2.getKeyCode());
					if (arg2.getKeyCode() == KeyEvent.KEYCODE_BACK) {
						return true;
					}
					return false;
				}
			});
		}

		noticeDialog.show();
	}

	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("正在下载新版本");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.ui_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		mTotalText = (TextView) v.findViewById(R.id.total_progress_text);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!isInterceptable) {
					ToastUtil.showShortMessage(mContext,
							"本次版本更新较大\n若不更新将会影响相关功能使用!");
					dialogShow(dialog);
				} else {
					dialog.dismiss();
					interceptFlag = true;
				}
			}
		});
		if (!isInterceptable) {
			builder.setCancelable(false);
		}
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();

				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		if (!isInterceptable) {
			downloadDialog.setCanceledOnTouchOutside(false);
			downloadDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int arg1,
						KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getKeyCode() == KeyEvent.KEYCODE_BACK) {
						return true;
					}
					return false;
				}
			});
		}
		downloadDialog.show();

		downloadApk();
	}

	private void dialogShow(DialogInterface dialog) {
		Field field;
		try {
			field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, false);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void dialogDismiss(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String apkName = "ERP_" + mUpdate.getVersionCode() + ".apk";
				String tmpApk = "ERP_" + mUpdate.getVersionCode() + ".tmp";
				// 判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + File.separator+"erp"+File.separator
					+ "com/managesystem/update" +File.separator;
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					if (file.exists()){
						LogUtil.info(file.getAbsolutePath());
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}

				// 没有挂载SD卡，无法下载文件
				if (apkFilePath == null || apkFilePath == "") {
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}

				File ApkFile = new File(savePath,apkName);
				// 是否已下载更新文件
				if (ApkFile.exists()) {
					downloadDialog.dismiss();
					installApk();
					return;
				}

				// 输出临时下载文件
				File tmpFile = new File(savePath,tmpApk);
				if (!tmpFile.exists())
					tmpFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(tmpFile);
				apkUrl = mUpdate.getDownloadUrl();
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				// 显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				// 进度条下面显示的总文件大小
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					// 进度条下面显示的当前下载文件大小
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					// 当前进度值
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成 - 将临时下载文件转成APK文件
						if (tmpFile.renameTo(ApkFile)) {
							// 通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.fromFile(apkfile),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
