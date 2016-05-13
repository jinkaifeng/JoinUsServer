package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.forum.PostAdd;
import com.northgatecode.joinus.dto.forum.TopicAdd;
import com.northgatecode.joinus.mongodb.*;
import com.northgatecode.joinus.services.ForumService;
import com.northgatecode.joinus.services.ImageService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Date;

/**
 * Created by qianliang on 22/4/2016.
 */

@Path("post")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostController {
    @PUT
    @Authenticated
    public Response createPost(@Context SecurityContext securityContext, PostAdd postAdd) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        if (postAdd.getTopicId() == null) {
            throw new BadRequestException("必须提供有效的主题Id");
        }

        Topic topic = datastore.find(Topic.class).field("id").equal(postAdd.getTopicId()).get();

        if (topic == null || topic.isDeleted()) {
            throw new BadRequestException("无效的主题Id或此主题已删除");
        }

        Forum forum = datastore.find(Forum.class).field("id").equal(topic.getForumId()).get();

        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("论坛不存在或此论坛已删除");
        }

        if (postAdd.getContent() == null || postAdd.getContent().length() < 2) {
            throw new BadRequestException("内容不能为空且不得少于2个字符");
        }

        int score = 5;

        Post post = new Post();
        post.setTopicId(topic.getId());
        post.setForumId(forum.getId());
        post.setContent(postAdd.getContent());
        post.setPostedByUserId(userId);
        post.setPostDate(new Date());
        post.setDeleted(false);
        datastore.save(post);

        if (postAdd.getImageIds() != null) {
            for (ObjectId imageId : postAdd.getImageIds()) {
                Image image = datastore.find(Image.class).field("id").equal(imageId).get();
                if (image != null) {
                    PostImage postImage = new PostImage();
                    postImage.setPostId(post.getId());
                    postImage.setImageId(imageId);
                    datastore.save(postImage);
                    score += 2;
                }
            }
        }


        // poster
        ForumWatch forumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("userId").equal(userId).get();
        if (forumWatch == null) {
            forumWatch = new ForumWatch();
            forumWatch.setForumId(forum.getId());
            forumWatch.setUserId(userId);
            forumWatch.setPosts(1);
            forumWatch.setScore(score);
            forumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
            forumWatch.setAdmin(false);
            forumWatch.setJoinDate(new Date());
            forumWatch.setLastPostDate(new Date());
            forumWatch.setDeleted(false);
            datastore.save(forumWatch);

            forum.setWatch((int)datastore.createQuery(ForumWatch.class).field("forumId").equal(forum.getId())
                    .field("deleted").equal(false).countAll());
        } else {
            forumWatch.setPosts(forumWatch.getPosts() + 1);
            forumWatch.setLastPostDate(new Date());
            forumWatch.setScore(forumWatch.getScore() + score);
            forumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
            datastore.save(forumWatch);
        }


        // topic owner
        ForumWatch ownerForumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("userId").equal(topic.getPostedByUserId()).get();
        if (ownerForumWatch != null) {
            ownerForumWatch.setScore(forumWatch.getScore() + 2);
            ownerForumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
            datastore.save(ownerForumWatch);
        }

        topic.setPosts(topic.getPosts() + 1);
        topic.setLastPostId(post.getId());
        topic.setLastPostDate(post.getPostDate());
        datastore.save(topic);

        forum.setPosts(forum.getPosts() + 1);
        forum.setActivity(forum.getActivity() + 5);
        datastore.save(forum);

        return Response.ok().build();
    }


    @DELETE
    @Path("{id}")
    @Authenticated
    public Response deletePost(@Context SecurityContext securityContext, @PathParam("id") String postId) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();
        User user = datastore.createQuery(User.class).field("id").equal(userId).get();

        Post post = datastore.find(Post.class).field("id").equal(new ObjectId(postId)).get();
        if (post == null || post.isDeleted()) {
            throw new BadRequestException("帖子不存在或已被删除");
        }

        if (post.getPostedByUserId() != userId && user.getRoleId() < 100) {
            throw new BadRequestException("您无权删除此贴");
        }

        post.setDeleted(true);
        datastore.save(post);

        return Response.ok().build();
    }

}
