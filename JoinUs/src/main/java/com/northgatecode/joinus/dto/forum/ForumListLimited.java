package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Forum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 3/4/2016.
 */
public class ForumListLimited {
    private List<ForumItem> forumItems;
    private int offset;
    private int limit;

    public ForumListLimited() {

    }

    public ForumListLimited(List<Forum> forums, int offset, int limit) {
        this.forumItems = new ArrayList<>();
        for (Forum forum : forums) {
            this.forumItems.add(new ForumItem(forum));
        }
        this.offset = offset;
        this.limit = limit;
    }

    public List<ForumItem> getForums() {
        return forumItems;
    }

    public void setForums(List<ForumItem> forums) {
        this.forumItems = forums;
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
