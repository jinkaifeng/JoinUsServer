package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.*;
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
    private ForumUserInfo postedBy;
    private String content;
    private Date postDate;
    private List<String> images;
    private List<ReplyItem> replyItems;

    public PostItem() {
    }

    public PostItem(Post post) {
        this.id = post.getId();
        this.postedBy = new ForumUserInfo(post.getPostedByUserId(), post.getForumId());
        this.content = post.getContent();
        this.postDate = post.getPostDate();
        this.images = new ArrayList<>();
        List<PostImage> postImages = MorphiaHelper.getDatastore().createQuery(PostImage.class)
                .field("postId").equal(post.getId()).asList();
        for (PostImage postImage : postImages) {
            Image image = MorphiaHelper.getDatastore().find(Image.class).field("id").equal(postImage.getImageId()).get();
            if (image != null) {
                this.images.add(image.getName());
            }
        }
        this.replyItems = new ArrayList<>();
        List<Reply> replies = MorphiaHelper.getDatastore().createQuery(Reply.class).field("postId").equal(post.getId())
                .field("deleted").equal(false).order("replyDate").asList();
        for (Reply reply : replies) {
            this.replyItems.add(new ReplyItem(reply));
        }
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ForumUserInfo getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(ForumUserInfo postedBy) {
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<ReplyItem> getReplyItems() {
        return replyItems;
    }

    public void setReplyItems(List<ReplyItem> replyItems) {
        this.replyItems = replyItems;
    }
}
