package com.northgatecode.joinus.utils;

import com.northgatecode.joinus.dto.CodeMessage;
import com.northgatecode.joinus.providers.GsonMessageBodyHandler;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by qianliang on 4/5/2016.
 */
public class JerseyHelper {
    public static Client getClient() {
        return ClientBuilder.newClient().register(GsonMessageBodyHandler.class);
    }
}
