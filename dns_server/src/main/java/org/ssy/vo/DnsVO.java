package org.ssy.vo;

/**
 * Created by manager on 2018/8/21.
 */
public class DnsVO {

  private String dnsType;//dns类型，如 A,NX

  private String domainPrefix;//域名前缀

  private String domainIps;//域名对应IP列表

  private Integer ttlValue;//ttl值

  private Integer dnsStatus;//dns状态值

  public String getDnsType() {
    return dnsType;
  }

  public void setDnsType(String dnsType) {
    this.dnsType = dnsType;
  }

  public String getDomainPrefix() {
    return domainPrefix;
  }

  public void setDomainPrefix(String domainPrefix) {
    this.domainPrefix = domainPrefix;
  }

  public String getDomainIps() {
    return domainIps;
  }

  public void setDomainIps(String domainIps) {
    this.domainIps = domainIps;
  }

  public Integer getTtlValue() {
    return ttlValue;
  }

  public void setTtlValue(Integer ttlValue) {
    this.ttlValue = ttlValue;
  }

  public Integer getDnsStatus() {
    return dnsStatus;
  }

  public void setDnsStatus(Integer dnsStatus) {
    this.dnsStatus = dnsStatus;
  }
}
