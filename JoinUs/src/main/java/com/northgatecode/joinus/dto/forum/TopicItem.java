package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.user.UserInfo;
import com.northgatecode.joinus.mongodb.Forum;
import com.northgatecode.joinus.mongodb.Post;
import com.northgatecode.joinus.mongodb.Topic;
import com.northgatecode.joinus.mongodb.User;
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

    public TopicItem() {
    }

    public TopicItem(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.postedBy = new ForumUserInfo(topic.getPostedByUserId(), topic.getForumId());
        this.posts = topic.getPosts();
        this.views = topic.getViews();
        Post firstPost = MorphiaHelper.getDatastore().find(Post.class).field("id").equal(topic.getFirstPostId()).get();
        this.firstPost = new PostInfo(firstPost);
        this.firstPostDate = topic.getFirstPostDate();
        Post lastPost = MorphiaHelper.getDatastore().find(Post.class).field("id").equal(topic.getLastPostId()).get();
        this.lastPost = new PostInfo(lastPost);
        this.lastPostDate = topic.getLastPostDate();
        this.onTop = topic.isOnTop();
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
}
