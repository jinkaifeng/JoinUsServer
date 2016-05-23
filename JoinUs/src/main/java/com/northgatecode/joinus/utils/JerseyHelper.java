package com.northgatecode.joinus.utils;

import com.northgatecode.joinus.dto.CodeMessage;
import com.northgatecode.joinus.providers.GsonMessageBodyHandler;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.SecureRandom;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


/**
 * Created by qianliang on 4/5/2016.
 */
public class JerseyHelper {
    public static Client getClient() {
        return ClientBuilder.newClient().register(GsonMessageBodyHandler.class);
    }

    public static Client getSSLClient() {
        ClientBuilder clientBuilder = JerseyClientBuilder.newBuilder().register(GsonMessageBodyHandler.class);

        // Create a secure JerseyClient
            try {
                HostnameVerifier verifier = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };

                TrustManager[] tm = new TrustManager[]{new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }
                }};

                SSLContext sslContext = SSLContext.getInstance("SSL");

                sslContext.init(null, tm, new SecureRandom());

                clientBuilder.sslContext(sslContext).hostnameVerifier(verifier);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

        return (JerseyClient) clientBuilder.build();
    }

}
