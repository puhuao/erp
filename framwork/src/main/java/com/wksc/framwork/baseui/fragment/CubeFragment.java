package com.wksc.framwork.baseui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wksc.framwork.baseui.activity.CubeFragmentActivity;
import com.wksc.framwork.baseui.lifecycle.IComponentContainer;
import com.wksc.framwork.baseui.lifecycle.LifeCycleComponent;
import com.wksc.framwork.baseui.lifecycle.LifeCycleComponentManager;
import com.wksc.framwork.interfaces.ICubeFragment;
import com.wksc.framwork.util.AppDebug;
import com.wksc.framwork.util.CLog;

/**
 *  * Implement {@link ICubeFragment}, {@link IComponentContainer}
 * <p/>
 * Ignore {@link LifeCycleComponentManager#onBecomesPartiallyInvisible}
 *
 * Created by wanglin on 2015/6/3.
 */
public abstract class CubeFragment extends Fragment implements ICubeFragment,IComponentContainer {

    private static final boolean DEBUG = AppDebug.DEBUG_LIFE_CYCLE;

    public Object getmDataIn() {
        return mDataIn;
    }

    protected Object mDataIn;

    private boolean mFirstResume = true;

    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

    protected abstract View createView(LayoutInflater inflater,
                                       ViewGroup container,
                                       Bundle savedInstanceState);

    public CubeFragmentActivity getContext() {
        return (CubeFragmentActivity) getActivity();
    }

    /**
     * =========================================================== Implements
     * {@link ICubeFragment}
     * ===========================================================
     */
    @Override
    public void onEnter(Object data) {
        mDataIn = data;
        if (DEBUG) {
            showStatus("fragment onEnter");
        }
    }

    @Override
    public void onLeave() {
        if (DEBUG) {
            showStatus("fragment onLeave");
        }
        mComponentContainer.onBecomesTotallyInvisible();
    }

    @Override
    public void onBackWithData(Object data) {
        if (DEBUG) {
            showStatus("fragment onBackWithData");
        }
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    public boolean processBackPressed() {
        return false;
    }

    @Override
    public void onBack() {
        if (DEBUG) {
            showStatus("fragment onBack");
        }
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    /**
     * =========================================================== Implements
     * {@link IComponentContainer}
     * ===========================================================
     */
    @Override
    public void addComponent(LifeCycleComponent component) {
        mComponentContainer.addComponent(component);
    }

    /**
     * Not add self to back stack when removed, so only when Activity stop
     */
    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG) {
            showStatus("fragment onStop");
        }
        onLeave();
    }

    /**
     * Only when Activity resume, not very precise. When activity recover from
     * partly invisible, onBecomesPartiallyInvisible will be triggered.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!mFirstResume) {
            onBack();
        }
        if (mFirstResume) {
            mFirstResume = false;
        }
        if (DEBUG) {
            showStatus("fragment onResume");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (DEBUG) {
            showStatus("fragment onAttach");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) {
            showStatus("fragment onCreate");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DEBUG) {
            showStatus("fragment onActivityCreated");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG) {
            showStatus("fragment onStart");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (DEBUG) {
            showStatus("fragment onPause");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (DEBUG) {
            showStatus("fragment onDestroyView");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            showStatus("fragment onDestroy");
        }
        mComponentContainer.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (DEBUG) {
            showStatus("fragment onDetach");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) {
            showStatus("fragment onCreateView");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void showStatus(String status) {
        final String[] className = ((Object) this).getClass()
                .getName()
                .split("\\.");
        CLog.d("cube-lifecycle",
                "%s %s",
                className[className.length - 1],
                status);
    }


}
