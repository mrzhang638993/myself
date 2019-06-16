package com.asiainfo.selfstudy.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BioClient {
    public static void main(String[] args) throws IOException {
        //  连接服务器
        Socket socket = new Socket("127.0.0.1", 20000);

        //while (true) {
            OutputStream os = socket.getOutputStream();
            String message="客户端连接服务端成功，开始发送客户端信息：bye";
            os.write(message.getBytes());
            os.flush();
            //  开始接受服务端的信息的
            InputStream is = socket.getInputStream();
            byte[]  bytes=  new byte[4096];
            int  len=0;
            while ((len=is.read(bytes))!=0){
                String result = new String(bytes);
                System.out.println(result);
                if (result.contains("bye")){
                    break;
                }
            }
        }
    //}
}
