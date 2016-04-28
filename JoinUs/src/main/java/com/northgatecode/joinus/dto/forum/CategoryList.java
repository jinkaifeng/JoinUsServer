package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Category;

import java.util.List;

/**
 * Created by qianliang on 3/4/2016.
 */
public class CategoryList {
    private List<Category> categories;

    public CategoryList() {
    }

    public CategoryList(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
