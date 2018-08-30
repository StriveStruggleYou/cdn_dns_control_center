package org.ssy.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by manager on 2018/8/30.
 */
public class NettyClient {

  public static Map<String, String> map = new ConcurrentHashMap<String, String>();


  public static void main(String[] args) {

    //worker负责读写数据

    ClientHandler4 clientHandler4=new ClientHandler4();

    connection(clientHandler4);


  }


  public static void connection(ClientHandler4 clientHandler4){
    EventLoopGroup worker = new NioEventLoopGroup();


    try {
      //辅助启动类
      Bootstrap bootstrap = new Bootstrap();

      //设置线程池
      bootstrap.group(worker);

      //设置socket工厂
      bootstrap.channel(NioSocketChannel.class);

      //设置管道
      bootstrap.handler(new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
          //获取管道
          ChannelPipeline pipeline = socketChannel.pipeline();
          //字符串解码器
          pipeline.addLast(new StringDecoder());
          //字符串编码器
          pipeline.addLast(new StringEncoder());
          //处理类
          pipeline.addLast(clientHandler4);
        }
      });

      //发起异步连接操作
      ChannelFuture futrue = bootstrap.connect(new InetSocketAddress("127.0.0.1",8866)).sync();
      Thread thread=new Thread(new ClientException(clientHandler4));
      thread.start();

      //等待客户端链路关闭
      futrue.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      System.out.println("重新连接");
      //优雅的退出，释放NIO线程组
      worker.shutdownGracefully();
      connection(clientHandler4);
    }
  }

}
