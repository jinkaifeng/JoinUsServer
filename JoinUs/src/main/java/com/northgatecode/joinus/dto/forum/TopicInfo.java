package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.ForumWatch;
import com.northgatecode.joinus.mongodb.Topic;
import com.northgatecode.joinus.mongodb.User;
import org.bson.types.ObjectId;

/**
 * Created by qianliang on 22/4/2016.
 */
public class TopicInfo {
    private ObjectId id;
    private String title;
    private ForumUserInfo postedBy;
    private int posts;
    private int views;
    private boolean deleteable;

    public TopicInfo() {
    }

    public TopicInfo(Topic topic, User user, ForumWatch forumWatch) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.postedBy = new ForumUserInfo(topic.getPostedByUserId(), topic.getForumId());
        this.views = topic.getViews();
        this.posts = topic.getPosts();

        if (user != null) {

            if (this.postedBy.getUserId().equals(user.getId())) {
                this.deleteable = true;
            } else if (user.getRoleId() >= 100) {
                this.deleteable = true;
            } else if (forumWatch != null && !forumWatch.isDeleted() && forumWatch.isAdmin()) {
                this.deleteable = true;
            }
        }
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

    public boolean isDeleteable() {
        return deleteable;
    }

    public void setDeleteable(boolean deleteable) {
        this.deleteable = deleteable;
    }
}
