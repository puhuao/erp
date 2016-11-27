package com.wksc.framwork.zxing;

import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;

/**
 * Created by Administrator on 2016/11/21.
 */
public class QRResourceRecycleEvent {
   public  QRresourceSend qRresourceSend;

    public QRResourceRecycleEvent(QRresourceSend qrResourceSend) {
        this.qRresourceSend = qrResourceSend;
    }

}
