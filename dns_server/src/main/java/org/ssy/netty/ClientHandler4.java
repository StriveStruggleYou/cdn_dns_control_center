package org.ssy.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Random;

/**
 * Created by manager on 2018/8/30.
 */
@Sharable
public class ClientHandler4 extends SimpleChannelInboundHandler<String> {


  public boolean close = true;

  private String sessionUUID;

  //接受服务端发来的消息
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//    if (close){
//      close = false;
//      throw new Exception("new");
//    }
    sessionUUID = msg;
    if (sessionUUID != null) {
      System.out.println("sessionUUID:" + sessionUUID + "server response ： " + msg);
      ctx.channel().writeAndFlush("sessionUUID_" + sessionUUID + ";" + "nihao server");
    } else {
      System.out.println("server response ： " + msg);
    }
    if (NettyClient.map.size() == 1) {
      NettyClient.map.remove("name");
      throw new Exception("new");
    }
  }

  //与服务器建立连接
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {

    if (sessionUUID != null) {
      //给服务器发消息
      ctx.channel().writeAndFlush("sessionUUID_" + sessionUUID + ";" + "nihao server");
    } else {
      ctx.channel().writeAndFlush("nihao server");
    }

    System.out.println("channelActive");
  }

  //与服务器断开连接
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelInactive");
  }

  //异常
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    //关闭管道
    ctx.channel().close();
    //打印异常信息
    cause.printStackTrace();


  }

}
