package org.ssy.share;

import java.util.concurrent.ConcurrentHashMap;
import org.ssy.vo.DnsVO;

/**
 * Created by manager on 2018/8/21.
 */
public class CommonUtil {

  public static ConcurrentHashMap<String, DnsVO> dns = new ConcurrentHashMap<String, DnsVO>();


  public static DnsVO getDnsInfo(String dnsName) {
    return dns.get(dnsName);
  }

}
