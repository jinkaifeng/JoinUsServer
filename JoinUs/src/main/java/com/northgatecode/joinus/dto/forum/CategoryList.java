package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Category;

import java.util.List;

/**
 * Created by qianliang on 3/4/2016.
 */
public class CategoryList {
    private List<Category> list;

    public CategoryList() {
    }

    public CategoryList(List<Category> list) {
        this.list = list;
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
    }
}
