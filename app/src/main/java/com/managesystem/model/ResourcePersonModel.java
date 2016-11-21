package com.managesystem.model;

/**
 * Created by Administrator on 2016/11/15.
 */
public class ResourcePersonModel {


    private String materialName;
    private String model;
    private int isNeedSerial;
    private int price;
    private String materialnameId;
    private String param;
    private String materialId;
    private Object userId;
    private Object serialNumber;
    private String brand;
    private String materialtypeName;
    private String materialtypeId;
    public Boolean isCheck = false;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getIsNeedSerial() {
        return isNeedSerial;
    }

    public void setIsNeedSerial(int isNeedSerial) {
        this.isNeedSerial = isNeedSerial;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMaterialnameId() {
        return materialnameId;
    }

    public void setMaterialnameId(String materialnameId) {
        this.materialnameId = materialnameId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Object serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMaterialtypeName() {
        return materialtypeName;
    }

    public void setMaterialtypeName(String materialtypeName) {
        this.materialtypeName = materialtypeName;
    }

    public String getMaterialtypeId() {
        return materialtypeId;
    }

    public void setMaterialtypeId(String materialtypeId) {
        this.materialtypeId = materialtypeId;
    }
}
