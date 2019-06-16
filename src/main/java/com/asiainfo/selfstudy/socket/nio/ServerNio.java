package com.asiainfo.selfstudy.socket.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = server.accept();
                    //*SocketChannel accept = server.accept();*//*
                    client.configureBlocking(false);
                    //  监听客户端发送到信息的。
                    client.register(selector, SelectionKey.OP_WRITE);
                } else if (selectionKey.isValid() && selectionKey.isReadable()) {
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = server.accept();
                    //  读取客户端发送的信息的
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);
                    int len = 0;
                    while ((len = client.read(byteBuffer)) != 0) {
                        System.out.println(new String(byteBuffer.array()));
                    }
                    //  需要注册告诉服务端，现在可以执行读操作的。
                    client.register(selector, SelectionKey.OP_WRITE);
                } else if (selectionKey.isValid() && selectionKey.isWritable()) {
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = server.accept();
                    String message = "客户端执行写操作了，告知服务端信息的";
                    ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
                    client.write(byteBuffer);
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
        }
    }
}
