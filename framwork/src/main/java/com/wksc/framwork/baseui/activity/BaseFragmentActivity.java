package com.wksc.framwork.baseui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.wksc.framwork.R;
import com.wksc.framwork.util.AppManager;

public class BaseFragmentActivity extends CubeFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public void finish()
	{
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void  startActivity(Class<? extends Activity> activityClass) {
		startActivity(getLocalIntent(activityClass, null));
		overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}


	public Intent getLocalIntent(Class<? extends Context> localIntent, Bundle bd) {
		Intent intent = new Intent(this, localIntent);
		if (null != bd) {
			intent.putExtras(bd);
		}

		return intent;
	}
	public void defaultFinish()
	{
		super.finish();
	}

	@Override
	protected String getCloseWarning() {
		return null;
	}

	@Override
	protected int getFragmentContainerId() {
		return 0;
	}
}
