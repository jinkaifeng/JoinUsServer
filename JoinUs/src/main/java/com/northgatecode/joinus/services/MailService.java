package com.northgatecode.joinus.services;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.northgatecode.joinus.utils.Config;

import javax.ws.rs.InternalServerErrorException;

/**
 * Created by qianliang on 4/5/2016.
 */
public class MailService {
    public static void sendSingleMail(String address, String subject, String body) {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Config.getAliKey(), Config.getAliSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            request.setAccountName("joinus@notice.northgatecode.com");
            request.setFromAlias("Join US");
            request.setAddressType(1);
            request.setTagName("JoinUs");
            request.setReplyToAddress(true);
            request.setToAddress(address);
            request.setSubject(subject);
            request.setHtmlBody(body);
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
            return;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        throw new InternalServerErrorException("邮件发送失败");
    }
}
