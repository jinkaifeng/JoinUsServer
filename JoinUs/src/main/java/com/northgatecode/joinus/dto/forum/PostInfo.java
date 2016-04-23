package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.user.UserInfo;
import com.northgatecode.joinus.mongodb.Image;
import com.northgatecode.joinus.mongodb.Post;
import com.northgatecode.joinus.mongodb.PostImage;
import com.northgatecode.joinus.mongodb.User;
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
    private UserInfo postedBy;
    private String content;
    private Date postDate;
    private List<String> images;

    public PostInfo() {
    }

    public PostInfo(Post post) {
        this.id = post.getId();
        User user = MorphiaHelper.getDatastore().find(User.class).field("id").equal(post.getPostedByUserId()).get();
        this.postedBy = new UserInfo(user);
        this.content = post.getContent();
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
}
