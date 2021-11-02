package com.ltp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * @Description: TODO
 * @Author: Ltp
 * @Date: 2021/10/31 23:47
 */
public class Client {

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9000));) {
            //切换为非阻塞式
            socketChannel.configureBlocking(false);

            //创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String str = scanner.next();
                //存入数据
                buffer.put((str + "\n" + LocalDateTime.now()).getBytes());
                buffer.flip();
                //将数据写出
                socketChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
