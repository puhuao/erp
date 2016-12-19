package com.managesystem.model;

/**
 * Created by Administrator on 2016/12/19.
 */
public class RemoteVersion {

    /**
     * isNew : false
     * newVersion : {"ctime":"2016-12-12 17:44","downloadUrl":"http://101.200.172.229:8080/ERP//appuser/getFile?path=20161212/3a97bbf6c03545688d093eba57760e53.apk","versionCode":3,"versionId":4,"versionName":"测试最新"}
     */

    private boolean isNew;
    /**
     * ctime : 2016-12-12 17:44
     * downloadUrl : http://101.200.172.229:8080/ERP//appuser/getFile?path=20161212/3a97bbf6c03545688d093eba57760e53.apk
     * versionCode : 3
     * versionId : 4
     * versionName : 测试最新
     */

    private NewVersionBean newVersion;

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public NewVersionBean getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(NewVersionBean newVersion) {
        this.newVersion = newVersion;
    }

    public static class NewVersionBean {
        private String ctime;
        private String downloadUrl;
        private int versionCode;
        private int versionId;
        private String versionName;

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
    }
}
