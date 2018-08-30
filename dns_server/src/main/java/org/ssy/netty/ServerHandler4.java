package org.ssy.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.UUID;

/**
 * Created by manager on 2018/8/30.
 */
class ServerHandler4 extends SimpleChannelInboundHandler<String> {

  //读取客户端发送的数据
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    System.out.println("client response :" + msg);
    if (msg.startsWith("sessionUUID_")) {
      String[] str = msg.split(";");
      String sessionId = str[0].substring("sessionUUID_".length(), "sessionUUID_".length()+UUID.randomUUID().toString().length());
      System.out.println("client response :" + sessionId);
      if (NettyServer.map.get(sessionId) != null
          && !NettyServer.map.get(sessionId).equals(ctx.channel())) {
        System.out.println("change channel");
        NettyServer.map.put(sessionId, ctx.channel());
      }
    } else {
      NettyServer.map.put(UUID.randomUUID().toString(), ctx.channel());
      ctx.channel().writeAndFlush(UUID.randomUUID().toString());
    }
//  ctx.writeAndFlush("i am server !").addListener(ChannelFutureListener.CLOSE);
  }

  //新客户端接入
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelActive");
//    NettyServer.map.put(getIPString(ctx), ctx.channel());
  }

  //客户端断开
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelInactive");
  }

  //异常
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    //关闭通道
    ctx.channel().close();
    //打印异常
    cause.printStackTrace();
  }

  public static String getIPString(ChannelHandlerContext ctx) {
    String ipString = "";
    String socketString = ctx.channel().remoteAddress().toString();
    int colonAt = socketString.indexOf(":");
    ipString = socketString.substring(1, colonAt);
    return ipString;
  }

}