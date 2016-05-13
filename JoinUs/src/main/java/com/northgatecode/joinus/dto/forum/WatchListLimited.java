package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Forum;
import com.northgatecode.joinus.mongodb.ForumWatch;
import com.northgatecode.joinus.mongodb.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 8/5/2016.
 */
public class WatchListLimited {
    private ForumInfo forumInfo;
    private List<ForumUserItem> forumUserItems;
    private int offset;
    private int limit;

    public WatchListLimited(Forum forum, List<ForumWatch> forumWatches, int offset, int limit) {
        this.forumInfo = new ForumInfo(forum);
        this.forumUserItems = new ArrayList<>();
        for (ForumWatch forumWatch : forumWatches) {
            this.forumUserItems.add(new ForumUserItem(forumWatch));
        }
        this.offset = offset;
        this.limit = limit;
    }


    public ForumInfo getForumInfo() {
        return forumInfo;
    }

    public void setForumInfo(ForumInfo forumInfo) {
        this.forumInfo = forumInfo;
    }

    public List<ForumUserItem> getForumUserItems() {
        return forumUserItems;
    }

    public void setForumUserItems(List<ForumUserItem> forumUserItems) {
        this.forumUserItems = forumUserItems;
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
