package com.managesystem.model;

import com.managesystem.widegt.sortView.SortModel;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/6.
 */
public class Department extends SortModel implements Serializable {

    /**
     * departmentName : 业务部
     * departmentId : sssssadwxcafwasd
     */

    public String departmentName;
    public String departmentId;
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
