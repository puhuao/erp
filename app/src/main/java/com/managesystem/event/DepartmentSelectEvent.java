package com.managesystem.event;

import com.managesystem.model.Department;

/**
 * Created by Administrator on 2016/11/6.
 */
public class DepartmentSelectEvent {
    private Department department;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public DepartmentSelectEvent(Department department) {
        this.department = department;
    }
}
