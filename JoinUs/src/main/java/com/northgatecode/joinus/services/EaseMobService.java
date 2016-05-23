package com.northgatecode.joinus.services;

import com.northgatecode.joinus.dto.easemob.AuthToken;
import com.northgatecode.joinus.dto.easemob.ClientCredential;
import com.northgatecode.joinus.dto.easemob.EaseMobUser;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.JerseyHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by qianliang on 18/5/2016.
 */
public class EaseMobService {
    private static AuthToken authToken = null;
    private static Date expirationDate = null ;
    public static AuthToken getAuthToken() {
        Logger logger = Logger.getLogger(EaseMobService.class.getName());

        if (authToken != null && expirationDate != null && new Date().before(expirationDate)) {
            return authToken;
        }

        authToken = null;

        ClientCredential clientCredential = new ClientCredential();
        clientCredential.setClientId("client_credentials");
        clientCredential.setClientId("YXA6RGRbcOOYEeWi61GnSwWLaA");
        clientCredential.setClientSecret("YXA6_QoLuYLXhbl-RDouhBigdXiwi2I");

        Invocation.Builder builder = JerseyHelper.getSSLClient()
                .target("https://a1.easemob.com/northgatecode/joinus/token").request(MediaType.APPLICATION_JSON_TYPE);
        builder.header("Content-Type", MediaType.APPLICATION_JSON);

        Response response = builder.post(Entity.entity(clientCredential, MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() == 200) {
            AuthToken tempAuthToken = response.readEntity(AuthToken.class);
            if (tempAuthToken != null) {
                authToken = tempAuthToken;
                expirationDate = DateUtils.addSeconds(new Date(), tempAuthToken.getExpires_in());
            }
        } else {
            logger.log(Level.WARNING, response.toString());
        }

        return authToken;
    }

    public static void addUser(User user) {
        Logger logger = Logger.getLogger(EaseMobService.class.getName());

        if (user.getEaseMobPassword() != null && user.getEaseMobPassword().length() > 0) {
            return;
        }

        AuthToken tempAuthToken = getAuthToken();

        if (tempAuthToken == null) {
            return;
        }

        EaseMobUser easeMobUser = new EaseMobUser();
        easeMobUser.setUserName(user.getId().toHexString());
        easeMobUser.setPassword(RandomStringUtils.randomAlphanumeric(6));
        easeMobUser.setNickName(user.getName());

        Invocation.Builder builder = JerseyHelper.getSSLClient()
                .target("https://a1.easemob.com/northgatecode/joinus/users").request(MediaType.APPLICATION_JSON_TYPE);
        builder.header("Content-Type", MediaType.APPLICATION_JSON);
        builder.header("Authorization", "Bearer " + tempAuthToken.getAccess_token());

        Response response = builder.post(Entity.entity(easeMobUser, MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() == 200) {
            user.setEaseMobPassword(easeMobUser.getPassword());
            MorphiaHelper.getDatastore().save(user);
        } else {
            logger.log(Level.WARNING, response.toString());
        }

    }
}
