package com.northgatecode.joinus.dto.forum;
import org.bson.types.ObjectId;
import java.util.List;

/**
 * Created by qianliang on 21/4/2016.
 */
public class PostAdd {
    private ObjectId topicId;
    private String content;
    private List<ObjectId> imageIds;

    public ObjectId getTopicId() {
        return topicId;
    }

    public void setTopicId(ObjectId topicId) {
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ObjectId> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<ObjectId> imageIds) {
        this.imageIds = imageIds;
    }
}
