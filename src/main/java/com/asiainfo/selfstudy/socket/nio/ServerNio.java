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
        // 1. 创建服务端的channel对象
        ServerSocketChannel socketServer = ServerSocketChannel.open();
        socketServer.configureBlocking(false);

        // 2. 创建Selector
        Selector selector = Selector.open();

        // 3. 把服务端的channel注册到selector，注册accept事件
        SelectionKey selectionKey = socketServer.register(selector, 0);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

        // 4. 绑定端口，启动服务
        socketServer.socket().bind(new InetSocketAddress(20001), 1024);

        while (true) {
            //  获取客户端的连接信息
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selection = iterator.next();
                iterator.remove();
                // 对于服务端注册的accept时间进行处理
                if (selection.isValid() && selection.isAcceptable()) {
                    //  服务端获取到客户端的连接信息
                    SocketChannel client = ((ServerSocketChannel) selection.channel()).accept();
                    client.configureBlocking(false);
                    //  针对于连接注册op_read监听，当服务端的socketchannel可以读数据的时候会触发读取的操作的
                    client.register(selector, SelectionKey.OP_READ);
                } else if (selection.isValid() && selection.isReadable()) {
                    SocketChannel client = (SocketChannel) selection.channel();
                  //  System.out.println("开始接受客户端的信息");
                    ByteBuffer  byteBuffer=  ByteBuffer.allocate(1024);
                    int length=0;
                    while ((length=client.read(byteBuffer))>0){
                        //if (byteBuffer.position()==0) break;
                    }
                    //if (byteBuffer.position() == 0) continue;
                    byteBuffer.flip();
                    //  处理需要多次读取的问题的，使用长度一次性的读取的
                    byte[] content = new byte[byteBuffer.remaining()];
                    byteBuffer.get(content);
                    System.out.println(new String(content));
                    String  message=new String(content);
                    System.out.println(message);
                    client.register(selector, SelectionKey.OP_WRITE);
                    //  切换到感兴趣的写
                    //selection.interestOps(SelectionKey.OP_WRITE);
                } else if (selection.isValid() && selection.isWritable()) {
                    SocketChannel client = (SocketChannel) selection.channel();
                    client.configureBlocking(false);
                    String  message="服务端发送给客户端的数据，客户端请接收";
                    ByteBuffer  byteBuffer=ByteBuffer.wrap(message.getBytes());
                    while (byteBuffer.hasRemaining()){
                        client.write(byteBuffer);
                    }
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
        }
    }
}
