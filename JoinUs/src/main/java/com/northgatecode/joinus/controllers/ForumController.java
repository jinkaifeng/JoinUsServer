package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.forum.*;
import com.northgatecode.joinus.mongodb.*;
import com.northgatecode.joinus.services.ForumService;
import com.northgatecode.joinus.services.ImageService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.apache.commons.lang3.RandomUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qianliang on 1/4/2016.
 */
@Path("forum")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ForumController {
    @GET
    public Response getForums(@QueryParam("search") String search, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {

        try {
            Thread.sleep(RandomUtils.nextInt(500, 2000)); //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        if (limit == 0) limit = 10;

        Datastore datastore = MorphiaHelper.getDatastore();
        List<Forum> forums;
        if (search != null && search.length() > 0) {
            forums = datastore.createQuery(Forum.class).search(search).field("deleted").equal(false).order("-activity").offset(offset).limit(limit).asList();
        } else {
            forums = datastore.createQuery(Forum.class).field("deleted").equal(false).order("-activity").offset(offset).limit(limit).asList();
        }

        return Response.ok(new ForumListLimited(forums, offset, limit)).build();
    }

    @GET
    @Path("watching")
    @Authenticated
    public Response watching(@Context SecurityContext securityContext, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Datastore datastore = MorphiaHelper.getDatastore();
        List<ForumWatch> forumWatches = datastore.createQuery(ForumWatch.class).field("userId").equal(userId)
                .order("-lastPostDate").offset(offset).limit(limit).asList();
        List<ObjectId> forumIds = new ArrayList<>();
        for (ForumWatch forumWatch : forumWatches) {
            forumIds.add(forumWatch.getForumId());
        }

        List<Forum> forums = datastore.createQuery(Forum.class).field("id").in(forumIds).field("deleted").equal(false)
                .offset(offset).limit(limit).asList();
        return Response.ok(new ForumListLimited(forums, offset, limit)).build();
    }

    @GET
    @Path("created")
    @Authenticated
    public Response created(@Context SecurityContext securityContext, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Datastore datastore = MorphiaHelper.getDatastore();
        List<Forum> forums = datastore.createQuery(Forum.class).field("createdByUserId").equal(userId)
                .field("deleted").notEqual(true).order("-activity").offset(offset).limit(limit).asList();

        return Response.ok(new ForumListLimited(forums, offset, limit)).build();
    }

    private String nameRegex = "^[\\s\\w\u4E00-\u9FA5]{3,16}$";
    @POST
    @Path("validateName")
    public Response validateName(ForumName forumName) {

        if (forumName.getName() == null || forumName.getName().length() < 3) {
            throw new BadRequestException("论坛名称过短,论坛名称不少于3个字符.");
        }

        if (forumName.getName().length() > 16) {
            throw new NotAcceptableException("论坛名称过长,论坛名称不应超过16个字符.");
        }

        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(forumName.getName());
        if (!matcher.matches()) {
            throw new NotAcceptableException("论坛名称含有非法字符,论坛名称只可以使用中文或英文字母。");
        }

        Datastore datastore = MorphiaHelper.getDatastore();
        List<Forum> sameNameForums = datastore.createQuery(Forum.class).field("name").equalIgnoreCase(forumName.getName()).asList();
        if (sameNameForums.size() > 0) {
            throw new NotAcceptableException("已存在同名论坛,请重新选择论坛名称.");
        }

        return Response.ok(forumName).build();
    }

    @PUT
    @Authenticated
    public Response createForum(@Context SecurityContext securityContext, ForumAdd forumAdd) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        if (forumAdd.getName() == null || forumAdd.getName().length() < 3) {
            throw new BadRequestException("论坛名称过短,论坛名称不少于3个字符.");
        }

        if (forumAdd.getName().length() > 16) {
            throw new BadRequestException("论坛名称过长,论坛名称不应超过16个字符.");
        }

        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(forumAdd.getName());
        if (!matcher.matches()) {
            throw new BadRequestException("论坛名称含有非法字符,论坛名称只可以使用中文或英文字母。");
        }

        List<Forum> sameNameForums = datastore.createQuery(Forum.class).field("name").equalIgnoreCase(forumAdd.getName()).asList();
        if (sameNameForums.size() > 0) {
            throw new BadRequestException("已存在同名论坛,请重新选择论坛名称.");
        }

        if (!Pattern.compile("^[\\S\\s\u4E00-\u9FA5]{6,100}$").matcher(forumAdd.getDesc()).matches()) {
            throw new BadRequestException("论坛说明少于6个字符或内容含有非法字符。");
        }

        if (forumAdd.getIconImageId() == null) {
            throw new BadRequestException("请为您的论坛设置图标");
        }

        if (datastore.createQuery(Image.class).field("id").equal(forumAdd.getIconImageId()).asList().size() == 0) {
            throw new BadRequestException("无效的论坛图标");
        }

        int score = 10;

        Forum forum = new Forum();
        forum.setName(forumAdd.getName());
        forum.setDesc(forumAdd.getDesc());
        forum.setIconImageId(forumAdd.getIconImageId());
        forum.setPosts(0);
        forum.setWatch(1);
        forum.setActivity(1);
        forum.setDeleted(false);
        forum.setCreatedByUserId(userId);
        forum.setCreateDate(new Date());
        datastore.save(forum);

        String categories = "";
        for (int categoryId : forumAdd.getCategoryIds()) {
            Category category = datastore.find(Category.class).field("id").equal(categoryId).get();
            if (category != null) {
                categories += category.getName() + " ";

                ForumCategory forumCategory = new ForumCategory();
                forumCategory.setForumId(forum.getId());
                forumCategory.setCategoryId(categoryId);
                datastore.save(forumCategory);
            }
        }
        forum.setCategories(categories);
        datastore.save(forum);

        ForumWatch forumWatch = new ForumWatch();
        forumWatch.setForumId(forum.getId());
        forumWatch.setUserId(userId);
        forumWatch.setPosts(0);
        forumWatch.setScore(score);
        forumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
        forumWatch.setAdmin(true);
        forumWatch.setJoinDate(new Date());
        forumWatch.setLastPostDate(new Date());
        forumWatch.setDeleted(false);
        datastore.save(forumWatch);

        return Response.ok().build();
    }

    @GET
    @Path("categories")
    public Response getCategories() {
        Datastore datastore = MorphiaHelper.getDatastore();
        List<Category> categories = datastore.createQuery(Category.class).asList();
        return Response.ok(new CategoryList(categories)).build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    public Response deleteForum(@Context SecurityContext securityContext, @PathParam("id") String forumId) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();
        User user = datastore.createQuery(User.class).field("id").equal(userId).get();

        Forum forum = datastore.createQuery(Forum.class).field("id").equal(new ObjectId(forumId)).get();
        if (forum == null) {
            throw new BadRequestException("您要删除的论坛不存在");
        }
        if (!forum.getCreatedByUserId().equals(userId) && user.getRoleId() < 100 ) {
            throw new BadRequestException("您无权删除此论坛");
        }
        forum.setDeleted(true);
        datastore.save(forum);

        return Response.ok().build();
    }

    @GET
    @Path("{id}/watch")
    @Authenticated
    public Response watch(@Context SecurityContext securityContext, @PathParam("id") String forumId) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        Forum forum = datastore.createQuery(Forum.class).field("id").equal(new ObjectId(forumId)).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("您要关注的论坛不存在或已经删除");
        }

        ForumWatch forumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("userId").equal(userId).get();
        if (forumWatch == null) {

            forumWatch = new ForumWatch();
            forumWatch.setForumId(forum.getId());
            forumWatch.setUserId(userId);
            forumWatch.setPosts(0);
            forumWatch.setScore(1);
            forumWatch.setLevel(ForumService.getLeveByScore(forumWatch.getScore()));
            if (forum.getCreatedByUserId() == userId) {
                forumWatch.setAdmin(true);
            } else {
                forumWatch.setAdmin(false);
            }
            forumWatch.setJoinDate(new Date());
            forumWatch.setDeleted(false);
            datastore.save(forumWatch);

            forum.setWatch((int)datastore.createQuery(ForumWatch.class).field("forumId").equal(forum.getId())
                    .field("deleted").equal(false).countAll());
            datastore.save(forum);

        } else {
            forumWatch.setDeleted(false);
            datastore.save(forumWatch);
        }


        return Response.ok().build();
    }

    @GET
    @Path("{id}/unwatch")
    @Authenticated
    public Response unwatch(@Context SecurityContext securityContext, @PathParam("id") String forumId) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        Forum forum = datastore.createQuery(Forum.class).field("id").equal(new ObjectId(forumId)).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("您要取消关注的论坛不存在或已经删除");
        }

        ForumWatch forumWatch = datastore.find(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("userId").equal(userId).get();
        if (forumWatch != null) {
            forumWatch.setDeleted(true);
            datastore.save(forumWatch);

            forum.setWatch((int)datastore.createQuery(ForumWatch.class).field("forumId").equal(forum.getId())
                    .field("deleted").equal(false).countAll());
            datastore.save(forum);
        }

        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response getForumAndTopics(@PathParam("id") String forumId, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        Datastore datastore = MorphiaHelper.getDatastore();
        Forum forum = datastore.createQuery(Forum.class).field("id").equal(new ObjectId(forumId)).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("无效的论坛id或论坛已删除");
        }
        if (limit == 0) limit = 10;
        List<Topic> topics = datastore.createQuery(Topic.class).field("forumId").equal(forum.getId())
                .field("deleted").equal(false).order("onTop").order("-lastPostDate").offset(offset).limit(limit).asList();
        return Response.ok(new TopicListLimited(forum, topics, offset, limit)).build();
    }

    @GET
    @Path("{id}/updateWatch")
    public Response updateWatch(@PathParam("id") String forumId) {
        Datastore datastore = MorphiaHelper.getDatastore();
        Forum forum = datastore.find(Forum.class).field("id").equal(new ObjectId(forumId)).get();
        if (forum == null || forum.isDeleted()) {
            throw new BadRequestException("论坛不存在或此论坛已删除");
        }
        forum.setWatch((int)datastore.createQuery(ForumWatch.class).field("forumId").equal(forum.getId())
                .field("deleted").equal(false).countAll());
        datastore.save(forum);
        return Response.ok().build();
    }

}
