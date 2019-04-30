package cn.huangdayu.socket.netty.server;

import cn.huangdayu.socket.netty.server.handler.ServerInHandler;
import cn.huangdayu.socket.netty.server.handler.ServerOutHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.UUID;

/***
 *
 * 要启动一个Netty服务端，必须要指定三类属性，分别是线程模型、IO 模型、连接读写处理逻辑
 * 创建一个引导类，然后给他指定线程模型，IO模型，连接读写处理逻辑，绑定端口之后，服务端就启动起来了。
 */
public class NettyServer {

    private static int port = 8000;

    private static int clientId = 1;

    private static int serverId = 1;

    public static void main(String[] args) {
        /** 引导类 ServerBootstrap，这个类将引导我们进行服务端的启动工作 */
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        /** 接受新连接线程组，主要负责创建新连接 ,bossGroup接收完连接，扔给workerGroup去处理。 */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        /** 负责读取数据的线程组，主要用于读取数据以及业务逻辑处理 */
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        /***
         * 引导类配置两大线程组
         */
        serverBootstrap
                /***
                 * 设定线程组
                 */
                .group(bossGroup, workerGroup)
                /***
                 * 指定 IO 模型
                 * nio : NioServerSocketChannel.class
                 * bio :OioServerSocketChannel.class
                 */
                .channel(NioServerSocketChannel.class)

                /** 用于指定在服务端启动过程中的一些逻辑 */
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    protected void initChannel(NioServerSocketChannel ch) {
                        System.out.println("服务端[" + ch.attr(AttributeKey.valueOf("serverName")) + "]启动中");
                    }
                })

                /***
                 * 给这个引导类创建一个ChannelInitializer，这里主要就是定义后续每条连接的数据读写，业务处理逻辑
                 * NioServerSocketChannel和NioSocketChannel的概念可以和 BIO
                 * 编程模型中的ServerSocket以及Socket两个概念对应上
                 */
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new ServerInHandler());
                        //ch.pipeline().addLast(new ServerOutHandler());


//                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//                                System.out.println(ctx.channel().attr(AttributeKey.valueOf("clientKey"))+" : " + msg);
//                            }
//                        });
                    }
                })

                /** 可以给服务端的 channel，也就是NioServerSocketChannel指定一些自定义属性，然后我们可以通过channel.attr()取出这个属性 */
                .attr(AttributeKey.newInstance("serverName"), "serverId-" + serverId)
                /** 每一条连接指定自定义属性，然后后续我们可以通过channel.attr()取出该属性。 */
                .childAttr(AttributeKey.newInstance("clientKey"), "clientId-" + UUID.randomUUID().toString())


                /** 每条连接设置一些TCP底层相关的属性 */
                /** 表示是否开启TCP底层心跳机制，true为开启 */
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                /** 表示是否开启Nagle算法，true表示关闭，false表示开启 */
                .childOption(ChannelOption.TCP_NODELAY, true)
                /** 给服务端channel设置一些属性 */
                /** 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数 */
                .option(ChannelOption.SO_BACKLOG, 1024);
        /***
         * 绑定端口
         */
        bind(serverBootstrap, port);
    }

    /***
     * 绑定端口，冲突导致失败则递归递增
     * @param serverBootstrap
     * @param port
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        /** 添加绑定监听 */
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功!");
                } else {
                    System.err.println("端口[" + port + "]绑定失败!");
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }
}
