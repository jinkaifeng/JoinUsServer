package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Forum;

import java.util.List;

/**
 * Created by qianliang on 3/4/2016.
 */
public class ForumListLimited {
    private List<ForumItem> list;
    private int offset;
    private int limit;

    public ForumListLimited() {
    }

    public ForumListLimited(List<ForumItem> list, int offset, int limit) {
        this.list = list;
        this.offset = offset;
        this.limit = limit;
    }

    public List<ForumItem> getList() {
        return list;
    }

    public void setList(List<ForumItem> list) {
        this.list = list;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
