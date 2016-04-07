package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.forum.CategoryList;
import com.northgatecode.joinus.dto.forum.ForumEdit;
import com.northgatecode.joinus.dto.forum.ForumItem;
import com.northgatecode.joinus.dto.forum.ForumListLimited;
import com.northgatecode.joinus.mongodb.*;
import com.northgatecode.joinus.services.ImageService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import com.northgatecode.joinus.utils.Utils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 1/4/2016.
 */
@Path("forum")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ForumController {

    @GET
    public Response getForums(@QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        if (limit == 0) limit = 10;
        Datastore datastore = MorphiaHelper.getDatastore();
        List<Forum> forums = datastore.createQuery(Forum.class).field("deleted").notEqual(true).order("-activity").offset(offset).limit(limit).asList();
        List<ForumItem> forumItems = new ArrayList<>();
        for (Forum forum : forums) {
            forumItems.add(new ForumItem(forum));
        }
        return Response.ok(new ForumListLimited(forumItems, offset, limit)).build();
    }

    @GET
    @Path("search")
    public Response searchForums(@QueryParam("search") String search, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        Datastore datastore = MorphiaHelper.getDatastore();
        List<Forum> forumList = datastore.createQuery(Forum.class).search(search).order("-activity").offset(offset).limit(limit).asList();
        return Response.ok(forumList).build();
    }

    @GET
    @Path("myForums")
    public Response myForums() {

        return Response.ok().build();
    }

    @PUT
    @Authenticated
    public Response createForum(@Context SecurityContext securityContext, ForumEdit forumEdit) throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();

        Forum forum = new Forum();
        forum.setName(forumEdit.getName());
        forum.setDesc(forumEdit.getDesc());
        forum.setIconImageId(forumEdit.getIcon().getImageId());
        forum.setPosts(0);
        forum.setWatch(1);
        forum.setActivity(1);
        forum.setDeleted(false);
        forum.setCreatedByUserId(userId);
        forum.setCreateDate(new Date());
        datastore.save(forum);

        ForumWatch forumWatch = new ForumWatch();
        forumWatch.setForumId(forum.getId());
        forumWatch.setUserId(userId);
        forumWatch.setLevel(1);
        forumWatch.setAdmin(true);
        forumWatch.setJoinDate(new Date());
        datastore.save(forumWatch);

        String categories = "";
        for (Category category : forumEdit.getCategories()) {
            categories += datastore.find(Category.class).field("id").equal(category.getId()).get().getName() + " ";

            ForumCategory forumCategory = new ForumCategory();
            forumCategory.setForumId(forum.getId());
            forumCategory.setCategoryId(category.getId());
            datastore.save(forumCategory);

        }
        forum.setCategories(categories);
        datastore.save(forum);

        ImageService.addDimensions(forum.getIconImageId(), new int[]{320, 160, 80});

        return Response.ok().build();
    }

    @GET
    @Path("categories")
    public Response getCategories() {
        Datastore datastore = MorphiaHelper.getDatastore();
        List<Category> categories = datastore.createQuery(Category.class).asList();
        return Response.ok(new CategoryList(categories)).build();
    }
}
