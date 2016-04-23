package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.user.UserInfo;
import com.northgatecode.joinus.mongodb.Post;
import com.northgatecode.joinus.mongodb.Reply;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 21/4/2016.
 */
public class PostItem {
    private ObjectId id;
    private UserInfo postedBy;
    private String content;
    private Date postDate;
    private List<ReplyItem> replies;

    public PostItem() {
    }

    public PostItem(Post post) {
        this.id = post.getId();
        User user = MorphiaHelper.getDatastore().find(User.class).field("id").equal(post.getPostedByUserId()).get();
        this.postedBy = new UserInfo(user);
        this.content = post.getContent();
        this.postDate = post.getPostDate();
        this.replies = new ArrayList<>();
        List<Reply> replies = MorphiaHelper.getDatastore().createQuery(Reply.class).field("postId").equal(post.getId())
                .field("deleted").equal(false).order("-replyDate").asList();
        for (Reply reply : replies) {
            this.replies.add(new ReplyItem(reply));
        }
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public UserInfo getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(UserInfo postedBy) {
        this.postedBy = postedBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public List<ReplyItem> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyItem> replies) {
        this.replies = replies;
    }
}
