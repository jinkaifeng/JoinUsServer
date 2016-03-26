package com.northgatecode.joinus.providers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Created by qianliang on 25/3/2016.
 */
public class ObjectIdTypeAdapter extends TypeAdapter<ObjectId> {
    @Override
    public void write(final JsonWriter out, final ObjectId value) throws IOException {
        out.value(value.toHexString());
//        out.beginObject()
//                .name("$oid")
//                .value(value.toString())
//                .endObject();
    }

    @Override
    public ObjectId read(final JsonReader in) throws IOException {
        String objectId = in.nextString();
//        in.beginObject();
//        assert "$oid".equals(in.nextName());
//        String objectId = in.nextString();
//        in.endObject();
        return new ObjectId(objectId);
    }
}
