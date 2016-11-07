package com.wksc.framwork.interfaces;


public interface ICubeFragment {

    void onEnter(Object data);

    void onLeave();

    void onBack();

    void onBackWithData(Object data);

    boolean processBackPressed();

}
