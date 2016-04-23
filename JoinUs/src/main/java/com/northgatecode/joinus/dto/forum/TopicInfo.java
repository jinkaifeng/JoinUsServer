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
    private UserInfo postedBy;
    private int views;

    public TopicInfo() {
    }

    public TopicInfo(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        User user = MorphiaHelper.getDatastore().find(User.class).field("id").equal(topic.getPostedByUserId()).get();
        this.postedBy = new UserInfo(user);
        this.views = topic.getViews();
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

    public UserInfo getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(UserInfo postedBy) {
        this.postedBy = postedBy;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
