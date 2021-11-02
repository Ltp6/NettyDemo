package com.ltp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description: TODO
 * @Author: Ltp
 * @Date: 2021/10/31 23:38
 */
public class Server {

    public static void main(String[] args) {

        SocketChannel socketChannel = null;
        ServerSocketChannel serverSocketChannel = null;
        try {
            //获取通道
            serverSocketChannel = ServerSocketChannel.open();

            //切换为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(9000));
            //获取选择器
            Selector selector = Selector.open();
            //将通道注册到选择器中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //轮询式的获取选择器上已经“准备就绪”的事件
            while (selector.select() > 0) {
                //获取当前选择器中所有注册的“选择键(已就绪的监听事件)”
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    //获取当前选择器中所有注册的“选择键(已就绪的监听事件)”
                    SelectionKey key = it.next();
                    //判断具体是什么事件准备就绪
                    if (key.isAcceptable()) {
                        //若是接收事件
                        socketChannel = serverSocketChannel.accept();
                        //切换为非阻塞模式
                        socketChannel.configureBlocking(false);
                        //注册进入选择器中
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        //获取当前选择器上“读就绪”状态的通道
                        socketChannel = (SocketChannel) key.channel();

                        //创建缓冲区
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int len;
                        while ((len = socketChannel.read(buffer)) > 0) {
                            buffer.flip();
                            System.out.println(new String(buffer.array(), 0, len));
                            buffer.clear();
                        }
                    }
                    //取消选择键 SelectionKey
                    it.remove();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != socketChannel) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != socketChannel) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
