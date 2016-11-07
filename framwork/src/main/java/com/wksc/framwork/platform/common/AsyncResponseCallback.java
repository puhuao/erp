package com.wksc.framwork.platform.common;

/**
 * An interface between the requester and responder of an asynchronous request
 *
 * Created by Gou Zhuang <gouzhuang@gmail.com> on 2015-02-06.
 */
public interface AsyncResponseCallback<T> {
    public void asyncResponse(boolean success, String message, T result);
}
