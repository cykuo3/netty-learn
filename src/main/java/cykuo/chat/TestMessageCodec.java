package cykuo.chat;

import cykuo.chat.message.LoginRequestMessage;
import cykuo.chat.protocol.MessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * 测试自定义协议
 *
 * @author chengyuankuo
 * @date 2022/05/24
 */
public class TestMessageCodec {

    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(),
                new LengthFieldBasedFrameDecoder(1024,12,4,0,0),
                new MessageCodec());

        //encode
        channel.writeOutbound(new LoginRequestMessage("zhangsan","123456","张三"));

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        MessageCodec codec = new MessageCodec();
        codec.encode(null,
                new LoginRequestMessage("zhangsan", "123456", "张三"), buf);
        ByteBuf slice1 = buf.slice(0, 100);
        ByteBuf slice2 = buf.slice(100, buf.readableBytes() - 100);
        slice1.retain();
        channel.writeInbound(slice1);
        slice2.retain();
        channel.writeInbound(slice2);
    }
}
