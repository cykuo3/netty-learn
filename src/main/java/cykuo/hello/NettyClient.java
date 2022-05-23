package cykuo.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.ByteBufFormat;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

/**
 * @author chengyuankuo
 * @date 2022/05/21
 */
@Slf4j
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ByteBuf buf = ctx.alloc().buffer();
                                Random random = new Random();
                                char c = '0';
                                for (int i = 0; i < 10; i++) {
                                    int randomInt = random.nextInt(10) + 1;
                                    byte[] bytes = getByte(c, randomInt, 10);
                                    buf.writeBytes(bytes);
                                    c++;
                                }
                                ctx.channel().writeAndFlush(buf);
                                log.debug("channelActive");
                            }
                        });

                    }
                })
                .connect(new InetSocketAddress(9090));

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                ChannelFuture closeFuture = channel.closeFuture();
                closeFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        log.debug("channel close done");
                        group.shutdownGracefully();
                    }
                });
            }
        });
    }

    private static byte[] getByte(char c,int clen,int totalLen){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalLen; i++) {
            if(i < clen){
                sb.append(c);
            }else {
                sb.append("_");
            }
        }
        return sb.toString().getBytes(Charset.defaultCharset());
    }
}
