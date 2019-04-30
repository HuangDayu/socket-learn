package cn.huangdayu.socket.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;
import java.nio.charset.Charset;

/***
 *  处理出站数据，允许拦截各种操作
 * 当 ChannelHandler 添加到 ChannelPipeline，或者从 ChannelPipeline 移除后，这些将会调用
 * @author huangdayu
 * @date 20190430
 */
public class ClientOutHandler extends ChannelOutboundHandlerAdapter {

    /****
     * 根据请求调用Channel以将其绑定到本地地址
     * @param ctx
     * @param localAddress
     * @param promise
     * @throws Exception
     */
    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
    }

    /***
     * 根据请求调用以将Channel连接到远程对等方
     * @param ctx
     * @param remoteAddress
     * @param localAddress
     * @param promise
     * @throws Exception
     */
    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        System.out.println("正在建立连接!");
    }

    /***
     * 根据请求调用以断开Channel与远程对等体的连接
     * @param ctx
     * @param promise
     * @throws Exception
     */
    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
        System.out.println("正在断开连接！");
    }

    /***
     * 根据请求调用以关闭频道
     * @param ctx
     * @param promise
     * @throws Exception
     */
    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
        System.out.println("正在关闭通道！");
    }

    /***
     * 根据请求调用以从其EventLoop取消注册Channel
     * @param ctx
     * @param promise
     */
    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) {
        System.out.println("正在注销注册！");
    }

    /***
     * 根据请求调用以从Channel中读取更多数据
     * @param ctx
     */
    @Override
    public void read(ChannelHandlerContext ctx) {
        System.out.println("正在读取数据！");
    }

    /***
     * 根据请求调用，以通过Channel将排队数据刷新到远程对等方
     * @param ctx
     * @param msg
     * @param promise
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        System.out.println("正在写数据！");
    }

    /***
     * 根据请求调用，通过Channel将数据写入远程对等体
     * @param ctx
     */
    @Override
    public void flush(ChannelHandlerContext ctx) {
        System.out.println("完成写数据！");
    }



    public String byteBuf2String(Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        return byteBuf.toString(Charset.forName("utf-8"));
    }


    private ByteBuf string2ByteBuf(ChannelHandlerContext ctx,String str) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = str.getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }
}
