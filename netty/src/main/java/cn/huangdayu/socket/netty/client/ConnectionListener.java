package cn.huangdayu.socket.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ConnectionListener implements ChannelFutureListener {

    /**
     * 最大重连次数
     */
    private static final int MAX_RETRY = 5;

    private NettyClient client;

    public ConnectionListener(NettyClient client) {
        this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) {
        if (!channelFuture.isSuccess()) {
            System.out.println("Reconnect");
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    while (!channelFuture.isSuccess()){
                        System.out.println("正在重连....2");
                        client.createBootstrap(new Bootstrap(), loop);
                    }
                }
            }, 1L, TimeUnit.SECONDS);
        }
    }
}
