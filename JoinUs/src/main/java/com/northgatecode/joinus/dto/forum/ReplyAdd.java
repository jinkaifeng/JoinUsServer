package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.user.UserInfo;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by qianliang on 21/4/2016.
 */
public class ReplyAdd {
    private ObjectId postId;
    private String content;

    public ObjectId getPostId() {
        return postId;
    }

    public void setPostId(ObjectId postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
