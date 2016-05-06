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
public class PostInfo {
    private ObjectId id;
    private ForumUserInfo postedBy;
    private String content;
    private Date postDate;
    private List<String> images;

    public PostInfo() {
    }

    public PostInfo(Post post) {
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
}
