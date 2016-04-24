package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.forum.PostAdd;
import com.northgatecode.joinus.dto.forum.ReplyAdd;
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
 * Created by qianliang on 24/4/2016.
 */
@Path("reply")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReplyController {

    @PUT
    @Authenticated
    public Response createReply(@Context SecurityContext securityContext, ReplyAdd replyAdd) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        if (replyAdd.getPostId() == null) {
            throw new BadRequestException("必须提供有效的帖子Id");
        }

        if (replyAdd.getContent() == null || replyAdd.getContent().length() < 1) {
            throw new BadRequestException("回复内容不能为空");
        }

        Post post = datastore.find(Post.class).field("id").equal(replyAdd.getPostId()).get();
        if (post == null || post.isDeleted()) {
            throw new BadRequestException("无效的帖子Id或此贴已删除");
        }

        Topic topic = datastore.find(Topic.class).field("id").equal(post.getTopicId()).get();

        if (topic == null || topic.isDeleted()) {
            throw new BadRequestException("无效的主题Id或此主题已删除");
        }

        Forum forum = datastore.find(Forum.class).field("id").equal(topic.getForumId()).get();

        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("论坛不存在或此论坛已删除");
        }

        Reply reply = new Reply();
        reply.setPostId(replyAdd.getPostId());
        reply.setTopicId(topic.getId());
        reply.setForumId(forum.getId());
        reply.setContent(replyAdd.getContent());
        reply.setRepliedByUserId(userId);
        reply.setReplyDate(new Date());
        reply.setDeleted(false);
        datastore.save(reply);

        ForumWatch forumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("userId").equal(userId).get();
        if (forumWatch == null) {
            forumWatch = new ForumWatch();
            forumWatch.setForumId(forum.getId());
            forumWatch.setUserId(userId);
            forumWatch.setPosts(1);
            forumWatch.setScore(2);
            forumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
            forumWatch.setAdmin(false);
            forumWatch.setJoinDate(new Date());
            forumWatch.setLastPostDate(new Date());
            forumWatch.setDeleted(false);
        } else {
            forumWatch.setPosts(forumWatch.getPosts() + 1);
            forumWatch.setLastPostDate(new Date());
            forumWatch.setScore(forumWatch.getScore() + 2);
            forumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
        }
        datastore.save(forumWatch);

        forum.setActivity(forum.getActivity() + 2);
        datastore.save(forum);

        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    public Response deleteReply(@Context SecurityContext securityContext, @PathParam("id") String replyId) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        Reply reply = datastore.find(Reply.class).field("id").equal(new ObjectId(replyId)).get();
        if (reply == null || reply.isDeleted()) {
            throw new BadRequestException("回复不存在或已被删除");
        }

        if (reply.getRepliedByUserId() != userId) {
            throw new BadRequestException("您无权删除此回复");
        }

        reply.setDeleted(true);
        datastore.save(reply);

        return Response.ok().build();
    }
}
