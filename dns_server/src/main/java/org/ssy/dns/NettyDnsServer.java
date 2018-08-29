package org.ssy.dns;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.net.InetAddress;
import org.ssy.handler.IncommingPacketHandler;
import org.ssy.handler.OutDnsHandler;

/**
 * Created by manager on 2018/8/27.
 */
public class NettyDnsServer {

  private int port;

  public NettyDnsServer(int port) {
    this.port = port;
  }

  public void run() throws Exception {
    final NioEventLoopGroup group = new NioEventLoopGroup();
    try {
      final Bootstrap b = new Bootstrap();
      b.group(group).channel(NioDatagramChannel.class)
          .option(ChannelOption.SO_BROADCAST, true)
          .handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            public void initChannel(final NioDatagramChannel ch) throws Exception {
              ChannelPipeline p = ch.pipeline();
              p.addLast(new OutDnsHandler());
              p.addLast(new IncommingPacketHandler());
            }
          });

      // Bind and start to accept incoming connections.
      Integer pPort = port;
      InetAddress address = InetAddress.getLocalHost();
      System.out.printf("waiting for message %s %s", String.format(pPort.toString()),
          String.format(address.toString()));
      b.bind(address, port).sync().channel().closeFuture().await();
    } finally {
      System.out.print("In Server Finally");
    }
  }

  public static void main(String[] args) throws Exception {
    int port = 9956;
    new NettyDnsServer(port).run();
  }
}
