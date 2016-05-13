package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.mongodb.Forum;
import com.northgatecode.joinus.mongodb.ForumWatch;
import com.northgatecode.joinus.mongodb.Topic;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.MorphiaHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 21/4/2016.
 */
public class TopicListLimited {
    private ForumInfo forumInfo;
    private List<TopicItem> topicItems;
    private int offset;
    private int limit;

    public TopicListLimited() {
    }

    public TopicListLimited(Forum forum, User user, ForumWatch forumWatch, List<Topic> topics, int offset, int limit) {

        this.forumInfo = new ForumInfo(forum, user);
        this.topicItems = new ArrayList<>();
        for (Topic topic : topics) {
            this.topicItems.add(new TopicItem(topic, user, forumWatch));
        }
        this.offset = offset;
        this.limit = limit;
    }

    public ForumInfo getForumInfo() {
        return forumInfo;
    }

    public void setForumInfo(ForumInfo forumInfo) {
        this.forumInfo = forumInfo;
    }

    public List<TopicItem> getTopicItems() {
        return topicItems;
    }

    public void setTopicItems(List<TopicItem> topicItems) {
        this.topicItems = topicItems;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
