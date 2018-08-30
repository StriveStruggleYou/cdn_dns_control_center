package org.ssy.netty;

/**
 * Created by manager on 2018/8/30.
 */
public class ClientException implements Runnable {

  ClientHandler4 clientHandler4;


  public ClientException(ClientHandler4 clientHandler4) {
    clientHandler4=clientHandler4;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    NettyClient.map.put("name","nihao");

  }
}
