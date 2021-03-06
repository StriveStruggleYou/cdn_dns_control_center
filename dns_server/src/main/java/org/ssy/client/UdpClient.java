package org.ssy.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by manager on 2018/8/27.
 */
public class UdpClient {

  public static void main(String args[]) {

    byte[] buf = new byte[256];
    DatagramSocket socket = null;
    try {
      socket = new DatagramSocket();
    } catch (SocketException e) {
      e.printStackTrace();
    }
    InetAddress address = null;
    try {
      address = InetAddress.getLocalHost();
      System.out.println(address);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    buf = "我是中国人".getBytes();
    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 9956);
    try {
      socket.send(packet);
      System.out.println(new String(buf));
      byte[] receiveData = new byte[512];
      DatagramPacket receivePacket = new DatagramPacket(
          receiveData, receiveData.length);
      socket.receive(receivePacket);
      System.out.println(new String(receiveData));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
