package org.ssy.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import java.net.InetAddress;

public class IncommingPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {


  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext,
      DatagramPacket datagramPacket) throws Exception {
    final InetAddress srcAddr = datagramPacket.sender().getAddress();
    final ByteBuf buf = datagramPacket.content();
    final int rcvPktLength = buf.readableBytes();
    final byte[] rcvPktBuf = new byte[rcvPktLength];
    buf.readBytes(rcvPktBuf);
    System.out.println("Inside incomming packet handler:" + new String(rcvPktBuf));
    //主动释放
//    ReferenceCountUtil.release(buf);
    channelHandlerContext.write("nihao");
    channelHandlerContext.flush();
  }
}