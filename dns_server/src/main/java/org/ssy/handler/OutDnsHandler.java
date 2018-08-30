package org.ssy.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.ssy.vo.DnsVO;

/**
 * Created by manager on 2018/8/28.
 */
public class OutDnsHandler extends ChannelOutboundHandlerAdapter {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
//    super.write(ctx, msg, promise);
//    super.write();
//    super.write("nihao");

//    System.out.println(msg);
//    ctx.write("nihao");
//
//    ctx.write(new DatagramPacket(
//        Unpooled.copiedBuffer("QOTM: " + "Got UDP Message!" , CharsetUtil.UTF_8), ctx.sender()));


    ctx.flush();



  }
}
