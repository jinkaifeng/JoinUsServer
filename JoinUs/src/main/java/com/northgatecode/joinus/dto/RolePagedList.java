package com.northgatecode.joinus.dto;

import java.util.List;

/**
 * Created by qianliang on 22/2/2016.
 */
public class RolePagedList {
    private List<RoleInfo> roleInfos;
    private int totalRecords;

    public List<RoleInfo> getRoleInfos() {
        return roleInfos;
    }

    public void setRoleInfos(List<RoleInfo> roleInfos) {
        this.roleInfos = roleInfos;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
