package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.*;
import com.northgatecode.joinus.services.ImageService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.util.Date;

/**
 * Created by qianliang on 24/4/2016.
 */
public class ForumUserInfo {

    private ObjectId userId;
    private String name;
    private String photo;
    private Gender gender;
    private Date registerDate;

    private int level;
    private int posts;
    private boolean isAdmin;
    private Date joinDate;
    private Date lastPostDate;

    public ForumUserInfo(ObjectId userId, ObjectId forumId) {
        Datastore datastore = MorphiaHelper.getDatastore();

        User user = datastore.find(User.class).field("id").equal(userId).get();
        this.userId = user.getId();
        this.name = user.getName();
        this.photo = ImageService.getImageName(user.getPhotoImageId());
        if (user.getGenderId() != 0) {
            this.gender = datastore.find(Gender.class).field("id").equal(user.getGenderId()).get();
        }
        this.registerDate = user.getRegisterDate();

        ForumWatch forumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forumId)
                .field("userId").equal(userId).get();
        if (forumWatch != null) {
            this.level = forumWatch.getLevel();
            this.posts = forumWatch.getPosts();
            this.isAdmin = forumWatch.isAdmin();
            this.joinDate = forumWatch.getJoinDate();
            this.lastPostDate = forumWatch.getLastPostDate();
        }
    }

    public ForumUserInfo(User user, ForumWatch forumWatch) {
        Datastore datastore = MorphiaHelper.getDatastore();

        this.userId = user.getId();
        this.name = user.getName();
        this.photo = ImageService.getImageName(user.getPhotoImageId());
        if (user.getGenderId() != 0) {
            this.gender = datastore.find(Gender.class).field("id").equal(user.getGenderId()).get();
        }
        this.registerDate = user.getRegisterDate();

        this.level = forumWatch.getLevel();
        this.posts = forumWatch.getPosts();
        this.isAdmin = forumWatch.isAdmin();
        this.joinDate = forumWatch.getJoinDate();
        this.lastPostDate = forumWatch.getLastPostDate();
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

}
