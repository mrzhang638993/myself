package com.asiainfo.selfstudy.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ClientNio {

    public static void main(String[] args) throws IOException {
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(false);
        Selector selector = Selector.open();
        //  不断的重新连接的话，会导致之前的连接挂起的操作的
        client.register(selector,SelectionKey.OP_CONNECT);
        client.connect(new InetSocketAddress("localhost", 20001));
            //  开始获取到服务端的时间通知的
          while (true) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isValid() && selectionKey.isReadable()) {
                            SocketChannel server = (SocketChannel) selectionKey.channel();
                            //  对应的是服务端的信息是可以读取的操作的。
                            System.out.println("获取到了服务端的信息的");
                            // 給服務端返回信息
                           server.register(selector,SelectionKey.OP_WRITE);
                        } else if (selectionKey.isValid() && selectionKey.isWritable()) {
                            SocketChannel server = (SocketChannel) selectionKey.channel();
                            //  对应的通道是可以写的。
                          /*  String message = "客户端执行写操作了，告知服务端信息的";
                            ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
                            byteBuffer.flip();
                            server.write(byteBuffer);*/
                            System.out.println("给服务端发送消息请求的");
                            server.register(selector,SelectionKey.OP_READ);
                        }else if(selectionKey.isValid() && selectionKey.isConnectable()){
                            //  对应的是处于连接的状态的
                            SocketChannel server = (SocketChannel) selectionKey.channel();
                            if (server.finishConnect()){
                                server.register(selector,SelectionKey.OP_WRITE);
                            }
                        }
                    }
                }
          }
   // }
}
