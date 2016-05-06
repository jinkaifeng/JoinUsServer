package com.northgatecode.joinus.services;

import com.northgatecode.joinus.dto.CodeMessage;
import com.northgatecode.joinus.mongodb.Sms;
import com.northgatecode.joinus.providers.GsonMessageBodyHandler;
import com.northgatecode.joinus.utils.Config;
import com.northgatecode.joinus.utils.JerseyHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.mongodb.morphia.Datastore;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Created by qianliang on 4/5/2016.
 */
public class SMSService {
    public static void sendVerifyCodeSMS(String mobile, String code) {
        if (mobile == null || mobile.length() != 11) {
            throw new BadRequestException("手机号码不能为空");
        }
        Datastore datastore = MorphiaHelper.getDatastore();
        Sms sms = datastore.find(Sms.class).field("mobile").equalIgnoreCase(mobile).get();
        if (sms != null && sms.getLastSentDate().after(DateUtils.addSeconds(new Date(), -59))) {
            throw new BadRequestException("你点那么快干嘛? 一个5分钱你知不知到, 等会再点!!!");
        }
        if (sms == null) {
            sms = new Sms();
            sms.setMobile(mobile);
            sms.setCounter(1);
            sms.setLastSentDate(new Date());
        } else {
            sms.setCounter(sms.getCounter() + 1);
            sms.setLastSentDate(new Date());
        }

        datastore.save(sms);

        CodeMessage codeMessage = JerseyHelper.getClient().target("http://api.weimi.cc/2/sms/send.html")
                .queryParam("uid", Config.getWeimiId())
                .queryParam("pas", Config.getWeimiPas())
                .queryParam("mob", mobile)
                .queryParam("cid", "b77b4s65tjjU")
                .queryParam("p1", "-Join Us-")
                .queryParam("p2", code)
                .queryParam("type", "json")
                .request(MediaType.APPLICATION_JSON_TYPE).get(CodeMessage.class);
        if (codeMessage.getCode() != 0) {
            throw new BadRequestException(codeMessage.getMsg());
        }
    }
}
