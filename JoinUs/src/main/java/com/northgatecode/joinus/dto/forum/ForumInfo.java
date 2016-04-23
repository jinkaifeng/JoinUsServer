package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.user.UserInfo;
import com.northgatecode.joinus.mongodb.Forum;
import com.northgatecode.joinus.mongodb.Image;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by qianliang on 20/4/2016.
 */
public class ForumInfo {
    private ObjectId id;
    private String name;
    private String desc;
    private String icon;
    private int posts;
    private int watch;
    private int activity;
    private UserInfo createdBy;
    private Date createDate;

    public ForumInfo() {
    }

    public ForumInfo(Forum forum) {
        this.id = forum.getId();
        this.name = forum.getName();
        this.desc = forum.getDesc();
        Image image = MorphiaHelper.getDatastore().find(Image.class).field("id").equal(forum.getIconImageId()).get();
        this.icon = image != null ? image.getName() : null;
        this.posts = forum.getPosts();
        this.watch = forum.getWatch();
        this.activity = forum.getActivity();
        User user = MorphiaHelper.getDatastore().find(User.class).field("id").equal(forum.getCreatedByUserId()).get();
        this.createdBy = new UserInfo(user);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getWatch() {
        return watch;
    }

    public void setWatch(int watch) {
        this.watch = watch;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public UserInfo getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserInfo createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
