package org.ssy.netty;

import io.netty.channel.Channel;

/**
 * Created by manager on 2018/8/30.
 */
public class ServerSendInfo implements Runnable {

  @Override
  public void run() {
    try {
      while (true) {
        for (String str : NettyServer.map.keySet()) {
          Channel channel = NettyServer.map.get(str);
          if (channel.isOpen()) {
            channel.writeAndFlush(str+":nihaonihao");
          }
        }
        Thread.sleep(2000);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
