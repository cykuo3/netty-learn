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
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                if(msg instanceof ByteBuf buf){
                                    String str = buf.toString(Charset.defaultCharset());
                                    System.out.println("recv echo");
                                    log.debug("recv echo msg---{}",str);
                                }
                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                ctx.flush();
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

                //TODO: 2022/5/23 12:55 AM 程远阔： 这里为什么死循环就无法打印日志了
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    channel.close();
                }
                log.debug("send {}", line);
                ByteBuf byteBuf = Unpooled.copiedBuffer(line.getBytes(Charset.defaultCharset()));
                channel.writeAndFlush(byteBuf);
            }
        });
    }
}
