package cn.huangdayu.socket.netty.client;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.huangdayu.socket.netty.client.handler.ClientInHandler;
import cn.huangdayu.socket.netty.client.handler.ClientOutHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

public class NettyClient {


    private static int clientId = 1;

    private static String inetHost = "127.0.0.1";

    private static int inetPort = 8000;

    NettyClient nettyClient;

    NettyClient getInstance() {
        return nettyClient;
    }

    public static void main(String[] args) throws InterruptedException {

        /** 客户端启动的引导类是 Bootstrap 负责启动客户端以及连接服务端 */
        Bootstrap bootstrap = new Bootstrap();

        /** 线程组 */
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        NettyClient nettyClient = new NettyClient();

        nettyClient.createBootstrap(bootstrap, workerGroup);

//        Channel channel = getConnect(bootstrap, "127.0.0.1", 8001, MAX_RETRY).channel();
//
//
//        while (true) {
//            channel.writeAndFlush(new Date() + ": hello world!");
//            Thread.sleep(2000);
//        }

    }

    public Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop) {
        bootstrap
                // 1.指定线程模型
                .group(eventLoop)
                // 2.指定 IO 类型为 NIO , Netty 的优势在于 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑 定义连接的业务处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        /** ch.pipeline() 返回的是和这条连接相关的逻辑处理链，采用了责任链模式 */
                        /** 调用 addLast() 方法 添加一个逻辑处理器，这个逻辑处理器为的就是在客户端建立连接成功之后，向服务端写数据 */
                        ch.pipeline().addLast(new ClientInHandler(nettyClient));
                        //ch.pipeline().addLast(new ClientOutHandler());
                    }
                })

                /** 给客户端 Channel，也就是NioSocketChannel绑定自定义属性，然后我们可以通过channel.attr()取出这个属性 */
                .attr(AttributeKey.newInstance("clientName"), "clientId" + clientId++)
                /** option() 方法可以给连接设置一些 TCP 底层相关的属性 */
                /** ChannelOption.CONNECT_TIMEOUT_MILLIS 表示连接的超时时间 */
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                /** 表示是否开启 TCP 底层心跳机制，true 为开启 */
                .option(ChannelOption.SO_KEEPALIVE, true)
                /** 表示是否开始 Nagle 算法，true 表示关闭，false 表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就设置为 true 关闭，如果需要减少发送次数减少网络交互，就设置为 false 开启 */
                .option(ChannelOption.TCP_NODELAY, true)
                /** 4.建立连接 异步的 */
                .connect(inetHost, inetPort).addListener(new ConnectionListener(this));

        return bootstrap;
    }


//    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
//        bootstrap.connect(host, port).addListener(future -> {
//            if (future.isSuccess()) {
//                System.out.println("连接成功!");
//            } else if (retry == 0) {
//                System.err.println("重试次数已用完，放弃连接！");
//            } else {
//                // 第几次重连
//                int order = (MAX_RETRY - retry) + 1;
//                // 本次重连的间隔
//                int delay = 1 << order;
//                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
//                bootstrap
//                        /** 这个方法返回的是 BootstrapConfig，他是对 Bootstrap 配置参数的抽象 */
//                        .config()
//                        /** 返回的就是我们在一开始的时候配置的线程模型 workerGroup， */
//                        .group()
//                        /** 调 workerGroup 的 schedule 方法即可实现定时任务逻辑。 */
//                        .schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
//                                .SECONDS);
//            }
//        });
//    }

    /***
     * 重连逻辑
     * @param bootstrap 引导对象
     * @param host 域名或者IP
     * @param port 端口
     * @param retry 重试次数
     */
//    private static ChannelFuture getConnect(Bootstrap bootstrap, String host, int port, int retry) {
//        return bootstrap.connect(host, port).addListener(future -> {
//            if (future.isSuccess()) {
//                System.out.println("连接成功!");
//            } else if (retry == 0) {
//                System.err.println("重试次数已用完，放弃连接！");
//            } else {
//                // 第几次重连
//                int order = (MAX_RETRY - retry) + 1;
//                // 本次重连的间隔
//                int delay = 1 << order;
//                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
//                bootstrap
//                        /** 这个方法返回的是 BootstrapConfig，他是对 Bootstrap 配置参数的抽象 */
//                        .config()
//                        /** 返回的就是我们在一开始的时候配置的线程模型 workerGroup， */
//                        .group()
//                        /** 调 workerGroup 的 schedule 方法即可实现定时任务逻辑。 */
//                        .schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
//                                .SECONDS);
//            }
//        });
//    }
}
