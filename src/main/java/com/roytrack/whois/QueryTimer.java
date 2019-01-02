package com.roytrack.whois;

import java.io.IOException;
import java.util.ArrayList;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainWhoisInfoRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainWhoisInfoResponse;
import com.aliyuncs.domain.model.v20180129.CheckDomainRequest;
import com.aliyuncs.domain.model.v20180129.CheckDomainResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.dingtalk.chatbot.demo.TestConfig;
import com.roytrack.message.TextMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class QueryTimer {
  public static void main(String[] args) {
    VertxOptions vertxOptions = new VertxOptions();
    //由于阿里云可能会存在调用时间过长，所以将vertx检查时间变长
    vertxOptions.setWarningExceptionTime(100000000000L)
            .setBlockedThreadCheckInterval(60000);
    Vertx vertx = Vertx.vertx(vertxOptions);

    vertx.setPeriodic(ParamConst.PERIOD, v -> {
      DingtalkChatbotClient ddclient = new DingtalkChatbotClient();
      DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ParamConst.AK, ParamConst.SK);
      IAcsClient client = new DefaultAcsClient(profile);
      ObjectMapper mapper = new ObjectMapper();
      // 创建API请求并设置参数
      CheckDomainRequest request = new CheckDomainRequest();
      request.setDomainName(ParamConst.DOMAIN);
      CheckDomainResponse response;
      try {
        StringBuilder sb = new StringBuilder();
        response = client.getAcsResponse(request);
        sb.append(mapper.writeValueAsString(response)+"#");
        DescribeDomainWhoisInfoRequest request1 = new DescribeDomainWhoisInfoRequest();
        request1.setDomainName(ParamConst.DOMAIN);
        request1.setUserClientIp("127.0.0.1");
        DescribeDomainWhoisInfoResponse response1 = client.getAcsResponse(request1);
        String val = mapper.writeValueAsString(response1);
        sb.append(val);
        if (val.indexOf("redemptionPeriod") < 0&&response1.getStatusList().size()>0) {
          TextMessage message = new TextMessage("域名状态变化 https://whois.aliyun.com/whois/domain/"+ParamConst.DOMAIN+"\n"
                  +sb.toString());
          ArrayList<String> atMobiles = new ArrayList<String>();
          atMobiles.add(ParamConst.AT_MOBILE);
          message.setAtMobiles(atMobiles);
          SendResult result = ddclient.send(ParamConst.WEB_HOOK, message);
          System.out.println(result);
        }
        System.out.println("this round result is "+sb.toString());
      } catch (ClientException | IOException e) {
        System.out.println("error occur");
        e.printStackTrace();
      }
    });

  }
}
