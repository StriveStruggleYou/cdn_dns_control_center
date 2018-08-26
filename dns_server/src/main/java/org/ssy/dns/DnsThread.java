package org.ssy.dns;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssy.data.DataBlock;
import org.ssy.data.DbConfig;
import org.ssy.data.DbSearcher;
import org.ssy.data.Util;
import org.ssy.share.CommonUtil;

/**
 * Created by manager on 2018/8/21.
 */
public class DnsThread implements Runnable {

  private static Logger log = LoggerFactory.getLogger(DnsThread.class);


  private String dnsName;

  private Integer dnsPort;

  byte[] defaultIp1;

  byte[] defaultIp;

  Map cityIdMap;

  private DbSearcher searcher = null;


  public DnsThread(String defaultIp, String dataPath, Integer port,
      String cityIds) {
    this.dnsName = "yidaren.top";
    this.dnsPort = port;
    this.defaultIp = ByteBuffer.allocate(4)
        .putInt(ipToInt(defaultIp)).array();
    cityIdMap = new HashMap<>();

    //设置cityIds
    if (cityIds != null) {
      String[] cityIdArray = cityIds.split(",");
      for (String cityId : cityIdArray) {
        cityIdMap.put(cityId, 0);
      }
    }
    defaultIp1 = ByteBuffer.allocate(4).putInt(ipToInt("120.79.90.108")).array();
    try {
      DbConfig config = new DbConfig();
      searcher = new DbSearcher(config, dataPath);
    } catch (Exception e) {
      log.error("DbSearcher load index error", e);
      return;
    }

    
  }

  @Override
  public void run() {

    try (DatagramSocket serverSocket = new DatagramSocket(dnsPort)) {
      byte[] receiveData = new byte[512];
      log.info("DNSd started at :" + dnsPort);
      while (true) {
        try {

          DatagramPacket receivePacket = new DatagramPacket(
              receiveData, receiveData.length);
          serverSocket.receive(receivePacket);

          StringBuilder qname = new StringBuilder();
          int idx = 12;// skip
          // transaction/id/flags/questions/answer/authority/additional
          int len = receiveData[idx];
          while (len > 0) {
            qname.append(".").append(
                new String(receiveData, idx + 1, len));
            idx += len + 1;
            len = receiveData[idx];
          }
          if (qname.length() > 0) {
            String name = qname.substring(1).toLowerCase();
            int type = receiveData[idx + 1] * 256
                + receiveData[idx + 2];

            String ip = receivePacket.getAddress().toString().substring(1);
            log.info(ip + ":"
                + receivePacket.getPort() + "\t" + name + "\t"
                + type);

            if (Util.isIpAddress(ip) == false) {
              log.warn("Error: Invalid ip address");
              continue;
            }
            DataBlock dataBlock = searcher.btreeSearch(ip);

            if (dataBlock != null) {
              log.info("find ip info:" + dataBlock.toString() + " test:" + cityIdMap
                  .get(String.valueOf(dataBlock.getCityId())));
            }
//            if ((!name.equals(host))
//                && (!name.endsWith("." + host))) {
//              continue;// keep silence
//            }
//            if (type != 1 && !name.equals(host)) {
//              continue;// we only response for A records, except
//              // for MX
//              // for host
//            }
            if (type != 1) {
              continue;
            }
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bo.write(new byte[]{receiveData[0], receiveData[1],
                (byte) 0x81, (byte) 0x80, 0x00, 0x01, 0x00,
                0x01, 0x00, 0x00, 0x00, 0x00});
            // write query
            byte[] req = Arrays.copyOfRange(receiveData, 12,
                idx + 5);
            bo.write(req);

            //write answer
            bo.write(req);
            bo.write(ByteBuffer.allocate(4)
                .putInt(name.equals(dnsName) ? 60 : 10).array());// ttl，ttl
            if (type == 1) {
              bo.write(new byte[]{0x00, 0x04});
//              int val = bytesToInt(receivePacket.getAddress()
//                  .getAddress());
//              bo.write((!name.equals(host))
//                  && resolved.containsKey(val) ? resolved
//                  .get(val) : defaultIp);
              //设置默认ip信息

              //设置基础默认重定向
              if (CommonUtil.getDnsInfo(name) == null) {
                bo.write(defaultIp);
              } else {
                String newIps = CommonUtil.getDnsInfo(name).getDomainIps();
                bo.write(ipToBytes(newIps));
              }

//              if (dataBlock != null
//                  && cityIdMap.get(String.valueOf(dataBlock.getCityId())) != null) {
//                bo.write(defaultIp1);
//              } else {
//                bo.write(defaultIp);
//              }


            } else {// for MX
              String mx = "mxdomain.qq.com";
              bo.write(ByteBuffer.allocate(2)
                  .putShort((short) (mx.length() + 4))
                  .array());
              bo.write(0x00);
              bo.write(0x05);// preference
              for (String s : mx.split("\\.")) {
                bo.write((byte) s.length());
                bo.write(s.getBytes());
              }
              bo.write(0x00);
            }

            byte[] sendData = bo.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(
                sendData, sendData.length,
                receivePacket.getAddress(),
                receivePacket.getPort());
            serverSocket.send(sendPacket);

          }

        } catch (Exception e) {
          log.error("dns error", e.getMessage());
        }
      }
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }


  // ip string byte[] converts
  private static int ipToInt(String ipAddress) {
    long result = 0;
    String[] ipAddressInArray = ipAddress.split("\\.");
    for (int i = 3; i >= 0; i--) {
      long ip = Long.parseLong(ipAddressInArray[3 - i]);
      result |= ip << (i * 8);
    }
    return (int) result;
  }

  private static byte[] ipToBytes(String ip) {
    return ByteBuffer.allocate(4).putInt(ipToInt(ip)).array();
  }

  private static String intToIp(int i) {
    return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
        + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
  }

  private static int bytesToInt(byte[] bytes) {
    int val = 0;
    for (int i = 0; i < bytes.length; i++) {
      val <<= 8;
      val |= bytes[i] & 0xff;
    }
    return val;
  }

  private static String bytesToIp(byte[] bytes) {
    return ((bytes[0]) & 0xFF) + "." + ((bytes[1]) & 0xFF) + "."
        + ((bytes[2]) & 0xFF) + "." + (bytes[3] & 0xFF);
  }


}
