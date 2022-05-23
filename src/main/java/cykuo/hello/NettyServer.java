package cykuo.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author chengyuankuo
 * @date 2022/05/21
 */
@Slf4j
public class NettyServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR,new AdaptiveRecvByteBufAllocator(16,16,16));
        serverBootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
//                        ch.pipeline().addLast("h1",new ChannelInboundHandlerAdapter() {
//                            @Override
//                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                if(msg instanceof ByteBuf buf){
//                                    String str = buf.toString(Charset.defaultCharset());
//                                    log.debug("recv msg---{}",str);
//                                    ctx.write(buf);
//                                }
//                            }
//
//                            @Override
//                            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//                                ctx.flush();
//                            }
//                        });
                    }
                })
                .bind(9090);


    }
}
