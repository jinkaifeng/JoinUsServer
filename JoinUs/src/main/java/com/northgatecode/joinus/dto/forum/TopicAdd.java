package com.northgatecode.joinus.dto.forum;

import org.bson.types.ObjectId;

/**
 * Created by qianliang on 21/4/2016.
 */
public class TopicAdd {
    private ObjectId forumId;
    private String title;
    private PostAdd firstPost;

    public ObjectId getForumId() {
        return forumId;
    }

    public void setForumId(ObjectId forumId) {
        this.forumId = forumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PostAdd getFirstPost() {
        return firstPost;
    }

    public void setFirstPost(PostAdd firstPost) {
        this.firstPost = firstPost;
    }
}
