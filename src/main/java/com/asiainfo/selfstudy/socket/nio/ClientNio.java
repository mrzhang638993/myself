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
                            ByteBuffer  byteBuffer=  ByteBuffer.allocate(1024);
                            int length=0;
                            while ((length=client.read(byteBuffer))>0){
                               // if (byteBuffer.position()==0) break;
                            }
                            //if (byteBuffer.position() == 0) break;
                            byteBuffer.flip();
                            //  处理需要多次读取的问题的，使用长度一次性的读取的
                            byte[] content = new byte[byteBuffer.remaining()];
                            byteBuffer.get(content);
                            System.out.println(new String(content));
                            String  message=new String(content);
                            System.out.println(message);
                           server.register(selector,SelectionKey.OP_WRITE);
                        } else if (selectionKey.isValid() && selectionKey.isWritable()) {
                            SocketChannel server = (SocketChannel) selectionKey.channel();
                            String message = "客户端执行写操作了，告知服务端信息的";
                            ByteBuffer  byteBuffer=ByteBuffer.wrap(message.getBytes());
                            while (byteBuffer.hasRemaining()){
                                server.write(byteBuffer);
                            }
                           server.register(selector,SelectionKey.OP_READ);
                        }else if(selectionKey.isValid() && selectionKey.isConnectable()){
                            SocketChannel server = (SocketChannel) selectionKey.channel();
                            //  客户端已经和服务端创建了连接的
                            if (server.finishConnect()){
                                //  监听连接的读
                                selectionKey.interestOps(SelectionKey.OP_WRITE);
                            }
                        }
                    }
                }
          }
   // }
}
