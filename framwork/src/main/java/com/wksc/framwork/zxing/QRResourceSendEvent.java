package com.wksc.framwork.zxing;

import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;

/**
 * Created by Administrator on 2016/11/21.
 */
public class QRResourceSendEvent {
    public int type = 1;
   public  QRresourceSend qRresourceSend;

    public QRResourceSendEvent(QRresourceSend qrResourceSend) {
        this.qRresourceSend = qrResourceSend;
    }

}
