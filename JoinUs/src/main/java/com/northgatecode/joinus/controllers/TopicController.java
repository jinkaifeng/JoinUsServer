package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.forum.ForumListLimited;
import com.northgatecode.joinus.dto.forum.PostListLimited;
import com.northgatecode.joinus.dto.forum.TopicAdd;
import com.northgatecode.joinus.dto.forum.TopicListLimited;
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
import java.util.List;

/**
 * Created by qianliang on 20/4/2016.
 */
@Path("topic")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TopicController {
    @PUT
    @Authenticated
    public Response createTopic(@Context SecurityContext securityContext, TopicAdd topicAdd) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        if (topicAdd.getForumId() == null) {
            throw new BadRequestException("必须提供有效的论坛Id");
        }

        if (topicAdd.getTitle() == null || topicAdd.getTitle().length() < 3) {
            throw new BadRequestException("标题不能为空且不得少于3个字符");
        }

        if (topicAdd.getFirstPost() == null || topicAdd.getFirstPost().getContent() == null
                || topicAdd.getFirstPost().getContent().length() < 3) {
            throw new BadRequestException("内容不能为空且不得少于3个字符");
        }

        Forum forum = datastore.find(Forum.class).field("id").equal(topicAdd.getForumId()).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("论坛不存在或此论坛已删除");
        }

        int score = 10;

        Topic topic = new Topic();
        topic.setForumId(forum.getId());
        topic.setTitle(topicAdd.getTitle());
        topic.setPostedByUserId(userId);
        topic.setOnTop(false);
        topic.setDeleted(false);
        datastore.save(topic);

        Post firstPost = new Post();
        firstPost.setTopicId(topic.getId());
        firstPost.setForumId(forum.getId());
        firstPost.setContent(topicAdd.getFirstPost().getContent());
        firstPost.setPostedByUserId(userId);
        firstPost.setPostDate(new Date());
        firstPost.setDeleted(false);
        datastore.save(firstPost);

        for (ObjectId imageId : topicAdd.getFirstPost().getImageIds()) {
            if (ImageService.addDimensions(imageId, new int[]{240, 120})) {
                PostImage postImage = new PostImage();
                postImage.setPostId(firstPost.getId());
                postImage.setImageId(imageId);
                datastore.save(postImage);
                score += 3;
            }
        }

        topic.setFirstPostId(firstPost.getId());
        topic.setFirstPostDate(firstPost.getPostDate());
        topic.setLastPostId(firstPost.getId());
        topic.setLastPostDate(firstPost.getPostDate());
        topic.setPosts(1);
        topic.setViews(1);
        datastore.save(topic);

        ForumWatch forumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("userId").equal(userId).get();
        if (forumWatch == null) {
            forumWatch = new ForumWatch();
            forumWatch.setForumId(forum.getId());
            forumWatch.setUserId(userId);
            forumWatch.setPosts(1);
            forumWatch.setScore(score);
            forumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
            forumWatch.setJoinDate(new Date());
            forumWatch.setLastPostDate(new Date());
            forumWatch.setAdmin(false);
            forumWatch.setDeleted(false);

            forum.setWatch((int)datastore.createQuery(ForumWatch.class).field("forumId").equal(forum.getId())
                    .field("userId").equal(userId).field("deleted").equal(false).countAll());
        } else {
            forumWatch.setPosts(forumWatch.getPosts() + 1);
            forumWatch.setLastPostDate(new Date());
            forumWatch.setScore(forumWatch.getScore() + score);
            forumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
        }
        datastore.save(forumWatch);

        forum.setPosts(forum.getPosts() + 1);
        forum.setActivity(forum.getActivity() + score);
        datastore.save(forum);

        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    public Response deleteTopic(@Context SecurityContext securityContext, @PathParam("id") String topicId) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        Topic topic = datastore.find(Topic.class).field("id").equal(new ObjectId(topicId)).get();
        if (topic == null || topic.isDeleted()) {
            throw new BadRequestException("主题不存在或已被删除");
        }

        if (topic.getPostedByUserId() != userId) {
            throw new BadRequestException("您无权删除此主题");
        }

        topic.setDeleted(true);
        datastore.save(topic);

        return Response.ok().build();
    }

    @GET
    @Path("{id}/pinTop")
    public Response pinTop(@Context SecurityContext securityContext, @PathParam("id") String topicId) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        Topic topic = datastore.find(Topic.class).field("id").equal(new ObjectId(topicId)).get();
        if (topic == null || topic.isDeleted()) {
            throw new BadRequestException("主题不存在或已被删除");
        }

        Forum forum = datastore.find(Forum.class).field("id").equal(topic.getForumId()).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("论坛不存在或此论坛已删除");
        }

        ForumWatch forumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("userId").equal(userId).get();
        if (forumWatch == null || !forumWatch.isAdmin()) {
            throw new BadRequestException("您无权进行此操作");
        }

        topic.setOnTop(true);
        datastore.save(topic);

        return Response.ok().build();
    }

    @GET
    @Path("{id}/unpinTop")
    public Response unpinTop(@Context SecurityContext securityContext, @PathParam("id") String topicId) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        Topic topic = datastore.find(Topic.class).field("id").equal(new ObjectId(topicId)).get();
        if (topic == null || topic.isDeleted()) {
            throw new BadRequestException("主题不存在或已被删除");
        }

        Forum forum = datastore.find(Forum.class).field("id").equal(topic.getForumId()).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("论坛不存在或此论坛已删除");
        }

        ForumWatch forumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("userId").equal(userId).get();
        if (forumWatch == null || !forumWatch.isAdmin()) {
            throw new BadRequestException("您无权进行此操作");
        }

        topic.setOnTop(false);
        datastore.save(topic);

        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response getTopicAndPosts(@PathParam("id") String topicId, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        Datastore datastore = MorphiaHelper.getDatastore();
        Topic topic = datastore.createQuery(Topic.class).field("id").equal(new ObjectId(topicId)).get();
        if (topic == null || topic.isDeleted()) {
            throw new BadRequestException("无效的主题id或主题已删除");
        }

        Forum forum = datastore.createQuery(Forum.class).field("id").equal(topic.getForumId()).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("论坛不存在或此论坛已删除");
        }

        if (offset == 0) {
            topic.setViews(topic.getViews() + 1);
            datastore.save(topic);
        }
        if (limit == 0) limit = 10;
        List<Post> posts = datastore.createQuery(Post.class).field("topicId").equal(topic.getId())
                .field("deleted").equal(false).order("postDate").offset(offset).limit(limit).asList();

        return Response.ok(new PostListLimited(topic, posts, offset, limit)).build();
    }

}
