package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.forum.ForumListLimited;
import com.northgatecode.joinus.dto.forum.PostListLimited;
import com.northgatecode.joinus.dto.forum.TopicAdd;
import com.northgatecode.joinus.dto.forum.TopicListLimited;
import com.northgatecode.joinus.mongodb.Forum;
import com.northgatecode.joinus.mongodb.Post;
import com.northgatecode.joinus.mongodb.PostImage;
import com.northgatecode.joinus.mongodb.Topic;
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
        Forum forum = datastore.find(Forum.class).field("id").equal(topicAdd.getForumId()).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("论坛不存在或已删除");
        }

        if (topicAdd.getTitle() == null || topicAdd.getTitle().length() < 3) {
            throw new BadRequestException("标题不能为空且不得少于3个字符");
        }

        if (topicAdd.getFirstPost() == null || topicAdd.getFirstPost().getContent() == null
                || topicAdd.getFirstPost().getContent().length() < 3) {
            throw new BadRequestException("内容不能为空且不得少于3个字符");
        }

        Topic topic = new Topic();
        topic.setForumId(topicAdd.getForumId());
        topic.setTitle(topicAdd.getTitle());
        topic.setPostedByUserId(userId);
        topic.setViews(0);
        topic.setDeleted(false);
        datastore.save(topic);

        Post firstPost = new Post();
        firstPost.setTopicId(topic.getId());
        firstPost.setContent(topicAdd.getFirstPost().getContent());
        firstPost.setPostedByUserId(userId);
        firstPost.setPostDate(new Date());
        firstPost.setDeleted(false);
        datastore.save(firstPost);

        for (ObjectId imageId : topicAdd.getFirstPost().getImageIds()) {
            PostImage postImage = new PostImage();
            postImage.setPostId(firstPost.getId());
            postImage.setImageId(imageId);
            datastore.save(postImage);
        }

        topic.setFirstPostId(firstPost.getId());
        topic.setFirstPostDate(firstPost.getPostDate());
        topic.setLastPostId(firstPost.getId());
        topic.setLastPostDate(firstPost.getPostDate());
        datastore.save(topic);

        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response getTopicAndPosts(@PathParam("id") String topicId, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        if (limit == 0) limit = 10;
        Datastore datastore = MorphiaHelper.getDatastore();
        Topic topic = datastore.createQuery(Topic.class).field("id").equal(topicId).get();
        if (topic == null) {
            throw new BadRequestException("无效的主题id");
        }
        List<Post> posts = datastore.createQuery(Post.class).field("topicId").equal(topicId)
                .field("deleted").equal(false).order("-postDate").offset(offset).limit(limit).asList();

        return Response.ok(new PostListLimited(topic, posts, offset, limit)).build();
    }

}
