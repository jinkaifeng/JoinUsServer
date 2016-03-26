package com.northgatecode.joinus.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 25/3/2016.
 */
public class MorphiaHelper {

    private static Morphia morphia;
    private static MongoClient client;
    private static Datastore datastore;

    static {
        try {
            morphia = new Morphia();
            // tell Morphia where to find your classes
            // can be called multiple times with different packages or classes
            morphia.mapPackage("com.northgatecode.joinus.mongodb");

            ServerAddress serverAddress = new ServerAddress("127.0.0.1", 27017);

//            List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
//            MongoCredential credentia = MongoCredential.createCredential(
//                    "username", "dbname", "password".toCharArray());
//            credentialsList.add(credentia);

            client = new MongoClient(serverAddress);

        } catch (ExceptionInInitializerError e) {
            throw e;
        }
    }

    final public static Datastore getDatastore() {
        Datastore datastore = morphia.createDatastore(client, "joinus");
        datastore.ensureIndexes();
        return datastore;
    }
}
