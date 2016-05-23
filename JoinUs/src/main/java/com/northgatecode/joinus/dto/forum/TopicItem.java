package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.*;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by qianliang on 20/4/2016.
 */
public class TopicItem {
    private ObjectId id;
    private String title;
    private ForumUserInfo postedBy;
    private int posts;
    private int views;
    private PostInfo firstPost;
    private Date firstPostDate;
    private PostInfo lastPost;
    private Date lastPostDate;
    private boolean onTop;
    private boolean deleteable;

    public TopicItem() {
    }

    public TopicItem(Topic topic, User user, ForumWatch forumWatch) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.postedBy = new ForumUserInfo(topic.getPostedByUserId(), topic.getForumId());
        this.posts = topic.getPosts();
        this.views = topic.getViews();
        Post firstPost = MorphiaHelper.getDatastore().find(Post.class).field("id").equal(topic.getFirstPostId()).get();
        this.firstPost = firstPost != null ? new PostInfo(firstPost) : null;
        this.firstPostDate = topic.getFirstPostDate();
        Post lastPost = MorphiaHelper.getDatastore().find(Post.class).field("id").equal(topic.getLastPostId()).get();
        this.lastPost = lastPost != null ? new PostInfo(lastPost) : null;
        this.lastPostDate = topic.getLastPostDate();
        this.onTop = topic.isOnTop();
        this.deleteable = false;
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

    public PostInfo getFirstPost() {
        return firstPost;
    }

    public void setFirstPost(PostInfo firstPost) {
        this.firstPost = firstPost;
    }

    public Date getFirstPostDate() {
        return firstPostDate;
    }

    public void setFirstPostDate(Date firstPostDate) {
        this.firstPostDate = firstPostDate;
    }

    public PostInfo getLastPost() {
        return lastPost;
    }

    public void setLastPost(PostInfo lastPost) {
        this.lastPost = lastPost;
    }

    public Date getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public boolean isOnTop() {
        return onTop;
    }

    public void setOnTop(boolean onTop) {
        this.onTop = onTop;
    }

    public boolean isDeleteable() {
        return deleteable;
    }

    public void setDeleteable(boolean deleteable) {
        this.deleteable = deleteable;
    }
}
