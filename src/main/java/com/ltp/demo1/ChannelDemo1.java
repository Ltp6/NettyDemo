package com.ltp.demo1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @Description: TODO
 * @Author: Ltp
 * @Date: 2021/11/2 23:07
 */
@Slf4j
public class ChannelDemo1 {


    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("data.txt", "rw")) {
            var channel = file.getChannel();
            var buffer = ByteBuffer.allocate(4);
            do {
                var len = channel.read(buffer);
                log.info("len={}", len);
                if (len == -1) {
                    break;
                }
                //切换 buffer 读模式
                buffer.flip();
                while (buffer.hasRemaining()) {
                    log.info("char={}",(char) buffer.get());
                }
                //切换到写模式
                buffer.clear();
            } while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
