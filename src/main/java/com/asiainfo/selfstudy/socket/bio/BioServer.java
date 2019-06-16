package com.asiainfo.selfstudy.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BioServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(20000);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(6));
        pool.execute(new Runnable() {
           @Override
           public void run() {
               //  获取来自客户端的连接信息
               Socket accept=null;
               while (true) {
                   try {
                       accept= serverSocket.accept();
                       InputStream is = accept.getInputStream();
                       byte[] bytes = new byte[1024];
                       int len = 0;
                       //read(bytes)：需要等待有数据的话，否则的话，会一直等待数据传入操作的
                       while ((len=is.read(bytes))!=0) {
                           String result = new String(bytes);
                           System.out.println(result);
                           if (result.contains("bye")){
                               break;
                           }
                       }
                       //  给客户端发送信息
                       OutputStream os = accept.getOutputStream();
                       String message = "服务端收到你的信息了：bye";
                       os.write(message.getBytes());
                       os.flush();
                   } catch (IOException e) {
                       try {
                           accept.close();
                       } catch (IOException ex) {
                           ex.printStackTrace();
                       }
                   }
               }
           }
       });
    }
}
