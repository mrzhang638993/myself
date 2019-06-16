package com.asiainfo.selfstudy.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
        boolean  connect= client.connect(new InetSocketAddress("localhost", 20001));
            //  开始获取到服务端的时间通知的
            while (true) {
                if (connect){
                    ByteBuffer sendbuffer = ByteBuffer.allocate(1024);
                    sendbuffer.put("QUERY TIME ORDER".getBytes());
                    sendbuffer.flip();
                    //向Channel中写入客户端的请求指令  写到服务端
                    client.write(sendbuffer);
                    if(!sendbuffer.hasRemaining()){
                        System.out.println("Send order to server succeed.");
                    }
                    selector.select(1000);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        SocketChannel server = (SocketChannel) selectionKey.channel();
                        if (selectionKey.isValid() && selectionKey.isReadable()) {
                            //  对应的是服务端的信息是可以读取的操作的。
                            System.out.println("获取到了服务端的信息的");
                            server.register(selector,SelectionKey.OP_WRITE);
                        } else if (selectionKey.isValid() && selectionKey.isWritable()) {
                            //  对应的通道是可以写的。
                            server.register(selector,SelectionKey.OP_READ);
                            String message = "客户端执行写操作了，告知服务端信息的";
                            ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
                            server.write(byteBuffer);
                        }
                    }
                }
            }
    }
}
