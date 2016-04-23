package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.user.UserInfo;
import com.northgatecode.joinus.mongodb.Reply;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by qianliang on 21/4/2016.
 */
public class ReplyItem {
    private ObjectId id;
    private UserInfo repliedBy;
    private String content;
    private Date replyDate;

    public ReplyItem() {
    }

    public ReplyItem(Reply reply) {
        this.id = reply.getId();
        User user = MorphiaHelper.getDatastore().find(User.class).field("id").equal(reply.getRepliedByUserId()).get();
        this.repliedBy = new UserInfo(user);
        this.content = reply.getContent();
        this.replyDate = reply.getReplyDate();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public UserInfo getRepliedBy() {
        return repliedBy;
    }

    public void setRepliedBy(UserInfo repliedBy) {
        this.repliedBy = repliedBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }
}
