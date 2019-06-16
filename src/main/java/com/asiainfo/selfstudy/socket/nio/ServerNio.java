package com.asiainfo.selfstudy.socket.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class ServerNio {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketServer = ServerSocketChannel.open();
        socketServer.configureBlocking(false);
        socketServer.socket().bind(new InetSocketAddress(20001), 1024);
        Selector selector = Selector.open();
        socketServer.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            //  获取客户端的连接信息
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isValid() && selectionKey.isAcceptable()) {
                    //  注意此处的区别的
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                   SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    //  监听客户端发送到信息的。
                    client.register(selector, SelectionKey.OP_WRITE);
                } else if (selectionKey.isValid() && selectionKey.isReadable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    System.out.println("收到来自客户端的请求信息的");
                    client.register(selector, SelectionKey.OP_WRITE);
                } else if (selectionKey.isValid() && selectionKey.isWritable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    //ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    //SocketChannel client = server.accept();
                    //  执行相关的监听的处理机制的
                  /*  ByteBuffer sendbuffer = ByteBuffer.allocate(1024);
                    sendbuffer.put(new Date(System.currentTimeMillis()).toString() .getBytes());
                    sendbuffer.flip();*/
                    //将客户端响应消息写入到客户端Channel中。
                   /* client.write(sendbuffer);*/
                    System.out.println("给客户端发送数据文件的");
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
        }
    }
}
