package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Forum;
import com.northgatecode.joinus.mongodb.Image;
import com.northgatecode.joinus.utils.MorphiaHelper;

/**
 * Created by qianliang on 4/4/2016.
 */
public class ForumItem {
    private String name;
    private String desc;
    private String icon;
    private int posts;
    private int watch;

    public ForumItem() {
    }

    public ForumItem(Forum forum) {
        this.name = forum.getName();
        this.desc = forum.getDesc();
        this.icon = MorphiaHelper.getDatastore().find(Image.class).field("id").equal(forum.getIconImageId()).get().getName();
        this.posts = forum.getPosts();
        this.watch = forum.getWatch();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getWatch() {
        return watch;
    }

    public void setWatch(int watch) {
        this.watch = watch;
    }
}
