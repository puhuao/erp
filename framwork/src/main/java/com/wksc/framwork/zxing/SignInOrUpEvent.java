package com.wksc.framwork.zxing;

import model.QRCodeModel;

/**
 * Created by Administrator on 2016/11/14.
 */
public class SignInOrUpEvent {
    public QRCodeModel qrCodeModel;

    public SignInOrUpEvent(QRCodeModel qrCodeModel) {
        this.qrCodeModel = qrCodeModel;
    }
}
