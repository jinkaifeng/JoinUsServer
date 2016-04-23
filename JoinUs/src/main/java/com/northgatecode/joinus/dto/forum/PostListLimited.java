package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Forum;
import com.northgatecode.joinus.mongodb.Post;
import com.northgatecode.joinus.mongodb.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 21/4/2016.
 */
public class PostListLimited {
    private TopicInfo topic;
    private List<PostItem> list;
    private int offset;
    private int limit;

    public PostListLimited(Topic topic, List<Post> posts, int offset, int limit) {
        this.topic = new TopicInfo(topic);
        this.list = new ArrayList<>();
        for (Post post : posts) {
            this.list.add(new PostItem(post));
        }
        this.offset = offset;
        this.limit = limit;
    }

    public List<PostItem> getList() {
        return list;
    }

    public void setList(List<PostItem> list) {
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
