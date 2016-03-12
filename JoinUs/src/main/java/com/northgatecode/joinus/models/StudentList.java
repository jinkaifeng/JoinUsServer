package com.northgatecode.joinus.models;

import java.util.List;

/**
 * Created by qianliang on 12/3/2016.
 */
public class StudentList {
    private List<Student> list;
    private int count;

    public StudentList() {}

    public StudentList(List<Student> list) {
        this.list = list;
        this.count = list.size();
    }

    public List<Student> getList() {
        return list;
    }

    public void setList(List<Student> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
