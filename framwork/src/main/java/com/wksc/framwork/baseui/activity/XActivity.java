package com.wksc.framwork.baseui.activity;

import android.os.Bundle;
import android.util.Log;

import com.wksc.framwork.baseui.activity.CubeFragmentActivity;
import com.wksc.framwork.baseui.lifecycle.IComponentContainer;
import com.wksc.framwork.baseui.lifecycle.LifeCycleComponent;
import com.wksc.framwork.baseui.lifecycle.LifeCycleComponentManager;
import com.wksc.framwork.util.AppDebug;

public abstract class XActivity extends CubeFragmentActivity implements IComponentContainer {

     LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

    private static final boolean DEBUG = AppDebug.DEBUG_LIFE_CYCLE;

    @Override
    protected void onRestart() {
        super.onStart();
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
        if (DEBUG) {
            showStatus("onRestart");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mComponentContainer.onBecomesPartiallyInvisible();
        if (DEBUG) {
            showStatus("onPause");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mComponentContainer.onBecomesVisibleFromPartiallyInvisible();
        if (DEBUG) {
            showStatus("onResume");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) {
            showStatus("onCreate");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mComponentContainer.onBecomesTotallyInvisible();
        if (DEBUG) {
            showStatus("onStop");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComponentContainer.onDestroy();
        if (DEBUG) {
            showStatus("onDestroy");
        }
    }

    @Override
    public void addComponent(LifeCycleComponent component) {
        mComponentContainer.addComponent(component);
    }

    private void showStatus(String status) {
        final String[] className = ((Object) this).getClass().getName().split("\\.");
        Log.d("cube-lifecycle", String.format("%s %s", className[className.length - 1], status));
    }
}
