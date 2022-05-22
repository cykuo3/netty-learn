package cykuo.hello;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author chengyuankuo
 * @date 2022/05/22
 */
public class TestByteBuf {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buffer);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <300 ; i++) {
            sb.append(i);
        }
        buffer.writeBytes(sb.toString().getBytes());
        System.out.println(buffer);
    }
}
