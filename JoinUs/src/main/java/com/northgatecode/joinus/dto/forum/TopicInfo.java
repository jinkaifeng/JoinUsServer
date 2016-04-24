package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.user.UserInfo;
import com.northgatecode.joinus.mongodb.Topic;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by qianliang on 22/4/2016.
 */
public class TopicInfo {
    private ObjectId id;
    private String title;
    private ForumUserInfo postedBy;
    private int posts;
    private int views;

    public TopicInfo() {
    }

    public TopicInfo(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.postedBy = new ForumUserInfo(topic.getPostedByUserId(), topic.getForumId());
        this.views = topic.getViews();
        this.posts = topic.getPosts();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ForumUserInfo getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(ForumUserInfo postedBy) {
        this.postedBy = postedBy;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
