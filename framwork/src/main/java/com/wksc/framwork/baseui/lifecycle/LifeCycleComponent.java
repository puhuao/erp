package com.wksc.framwork.baseui.lifecycle;

public interface LifeCycleComponent {

    /**
     * UI 部分可见
     * like {@link android.app.Activity#onPause}
     */
    public void onBecomesPartiallyInvisible();

    /**
     * UI 可见
     * like {@link android.app.Activity#onResume}
     */
    public void onBecomesVisible();

    /**
     * UI 全部不可见
     * like {@link android.app.Activity#onStop}
     */
    public void onBecomesTotallyInvisible();


    /**
     * UI 从不可见，变成可见
     * <p/>
     * like {@link android.app.Activity#onRestart}
     */
    public void onBecomesVisibleFromTotallyInvisible();

    /**
     * like {@link android.app.Activity#onDestroy}
     */
    public void onDestroy();
}
