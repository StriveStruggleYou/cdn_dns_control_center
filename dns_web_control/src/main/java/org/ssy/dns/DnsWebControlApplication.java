package org.ssy.dns;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.ssy.share.CommonUtil;
import org.ssy.vo.DnsVO;

/**
 * Created by manager on 2018/8/21.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class})
public class DnsWebControlApplication {

  private static Logger log = LoggerFactory.getLogger(DnsServer.class);


  public static void main(String args[]) {

    //设置第一个dnsvo的基础信息
    DnsVO dnsVO = new DnsVO();
    dnsVO.setDnsStatus(0);
    dnsVO.setDnsType("A");
    dnsVO.setDomainIps("192.168.1.100");
    dnsVO.setDomainPrefix("a.server");
    dnsVO.setTtlValue(360);
    CommonUtil.dns.put("a.server.yidaren.top", dnsVO);
    //设置第一个dnsvo的基础信息

    DnsThread dnsThread = null;
    try {
      Properties properties = new Properties();
      InputStream in = DnsServer.class.getClassLoader().getResourceAsStream("conf.properties");
      properties.load(in);
      //获取key对应的value值
      String ip = properties.getProperty("defaultIp");
      String dataPath = properties.getProperty("dataPath");
      Integer dnsPort = Integer.valueOf(properties.getProperty("port"));
      String cityIds = properties.getProperty("cityIds");
      dnsThread = new DnsThread(ip, dataPath, dnsPort, cityIds);
    } catch (Exception e) {
      log.error("load properties error", e);
      return;
    }
    Thread dnsStartThread = new Thread(dnsThread);
    dnsStartThread.start();

    SpringApplication.run(DnsWebControlApplication.class, args);
  }


}
