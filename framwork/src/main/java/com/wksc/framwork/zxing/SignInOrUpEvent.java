package com.wksc.framwork.zxing;

import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;

import model.QRCodeModel;

/**
 * Created by Administrator on 2016/11/14.
 */
public class SignInOrUpEvent {
    public QRChecInModel qrCodeModel;

    public SignInOrUpEvent(QRChecInModel qrCodeModel) {
        this.qrCodeModel = qrCodeModel;
    }
}
