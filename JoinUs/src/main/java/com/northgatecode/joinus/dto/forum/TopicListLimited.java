package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Forum;
import com.northgatecode.joinus.mongodb.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 21/4/2016.
 */
public class TopicListLimited {
    private ForumInfo forum;
    private List<TopicItem> list;
    private int offset;
    private int limit;

    public TopicListLimited() {
    }

    public TopicListLimited(Forum forum, List<Topic> topics, int offset, int limit) {
        this.forum = new ForumInfo(forum);
        this.list = new ArrayList<>();
        for (Topic topic : topics) {
            this.list.add(new TopicItem(topic));
        }
        this.offset = offset;
        this.limit = limit;
    }

    public ForumInfo getForum() {
        return forum;
    }

    public void setForum(ForumInfo forum) {
        this.forum = forum;
    }

    public List<TopicItem> getList() {
        return list;
    }

    public void setList(List<TopicItem> list) {
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
