package com.roytrack.whois;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainWhoisInfoRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainWhoisInfoResponse;
import com.aliyuncs.domain.model.v20180129.CheckDomainRequest;
import com.aliyuncs.domain.model.v20180129.CheckDomainResponse;
import com.aliyuncs.domain.transform.v20180129.CheckDomainResponseUnmarshaller;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;

public class QueryTimer {
  public static void main(String[] args) throws JsonProcessingException {


//    Vertx vertx = Vertx.vertx();
//    vertx.setPeriodic(10000,v->{
//
//
//    });

    DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou","aaa","vvv");
    IAcsClient client = new DefaultAcsClient(profile);
    ObjectMapper mapper = new ObjectMapper();
    // 创建API请求并设置参数
    CheckDomainRequest request = new CheckDomainRequest();
    request.setDomainName("liujifen.com");
    CheckDomainResponse response ;
    try {
      response = client.getAcsResponse(request);
      System.out.println(mapper.writeValueAsString(response));

      DescribeDomainWhoisInfoRequest request1 = new DescribeDomainWhoisInfoRequest();
      request1.setDomainName("liujifen.com");
      request1.setUserClientIp("127.0.0.1");
      DescribeDomainWhoisInfoResponse response1 = client.getAcsResponse(request1);
      String val = mapper.writeValueAsString(response1);
      System.out.println(mapper.writeValueAsString(response1));
      if(val.indexOf("redemptionPeriod")<0){

      }
    } catch (ClientException e) {
      e.printStackTrace();
    }
  }
}
