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
    private TopicInfo topicInfo;
    private List<PostItem> postItems;
    private int offset;
    private int limit;

    public PostListLimited(Topic topic, List<Post> posts, int offset, int limit) {
        this.topicInfo = new TopicInfo(topic);
        this.postItems = new ArrayList<>();
        for (Post post : posts) {
            this.postItems.add(new PostItem(post));
        }
        this.offset = offset;
        this.limit = limit;
    }

    public TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public void setTopicInfo(TopicInfo topicInfo) {
        this.topicInfo = topicInfo;
    }

    public List<PostItem> getPostItems() {
        return postItems;
    }

    public void setPostItems(List<PostItem> postItems) {
        this.postItems = postItems;
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
