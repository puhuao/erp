package model;

import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;

/**
 * Created by Administrator on 2016/11/14.
 */
public class QRCodeModel{
    private String type;
   private QRChecInModel param;

    public QRChecInModel getParam() {
        return param;
    }

    public void setParam(QRChecInModel param) {
        this.param = param;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
