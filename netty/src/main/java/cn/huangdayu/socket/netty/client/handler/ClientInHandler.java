package cn.huangdayu.socket.netty.client.handler;

import cn.huangdayu.socket.netty.client.NettyClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/***
 * 处理进站数据，并且所有状态都更改
 * 当 ChannelHandler 添加到 ChannelPipeline，或者从 ChannelPipeline 移除后，这些将会调用
 * @author huangdayu
 * @date 20190430
 */
public class ClientInHandler extends ChannelInboundHandlerAdapter {

    private NettyClient client;

    public ClientInHandler(NettyClient client) {
        this.client = client;
    }

    /***
     * channel 注册到一个 EventLoop.
     * @param ctx
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("客户端注册成功！");
    }

    /***
     * channel创建但未注册到一个 EventLoop
     * @param ctx
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        System.out.println("客户端注册失败！");
    }

    /***
     * channel 的活动的(连接到了它的 remote peer（远程对等方）)，现在可以接收和发送数据了
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接成功！");
        // 1. 获取数据
        ByteBuf buffer = string2ByteBuf(ctx, "hi，长连接建立了！");

        // 2. 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    /***
     * channel 没有连接到 remote peer（远程对等方）
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接失败！");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("正在重连....1");
                client.createBootstrap(new Bootstrap(), eventLoop);
            }
        }, 1L, TimeUnit.SECONDS);
    }

    /***
     * 如果从Channel读取数据，则调用。
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("客户端接收到数据：" + byteBuf2String(msg));
    }

    /***
     * 在Channel上的读取操作完成时调用。
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("客户端读取数据完成！");
    }

    /***
     * 当用户调用Channel.fireUserEventTriggered（...）以通过ChannelPipeline传递pojo时调用。
     * 这可用于通过ChannelPipeline传递用户特定事件，因此允许处理这些事件。
     * @param ctx
     * @param evt
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        System.out.println("客户端执行特定事件！");
    }

    /***
     * 当Channel的可写性状态发生变化时调用。
     * @param ctx
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) {
        System.out.println("客户端可写性状态发生改变！");
    }

    /***
     * 当 ChannelPipeline 执行发生错误时调用
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("客户端连接服务端出现异常：" + cause.getMessage());
        ctx.close();
        System.out.println("客户端连接通道关闭，状态为：" + ctx.isRemoved());
    }


    public String byteBuf2String(Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        return byteBuf.toString(Charset.forName("utf-8"));
    }


    private ByteBuf string2ByteBuf(ChannelHandlerContext ctx, String str) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = str.getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }
}
