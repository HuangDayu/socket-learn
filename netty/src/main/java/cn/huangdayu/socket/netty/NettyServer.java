package cn.huangdayu.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/***
 * 
 * @author Administrator 要启动一个Netty服务端，必须要指定三类属性，分别是线程模型、IO 模型、连接读写处理逻辑
 */
public class NettyServer {
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
		serverBootstrap.group(bossGroup, workerGroup)
				/***
				 * 指定 IO 模型 nio : NioServerSocketChannel.class bio :
				 * OioServerSocketChannel.class
				 */
				.channel(NioServerSocketChannel.class)
				/***
				 * 给这个引导类创建一个ChannelInitializer，这里主要就是定义后续每条连接的数据读写，业务处理逻辑
				 * NioServerSocketChannel和NioSocketChannel的概念可以和 BIO
				 * 编程模型中的ServerSocket以及Socket两个概念对应上
				 */
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					protected void initChannel(NioSocketChannel ch) {
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, String msg) {
								System.out.println(msg);
							}
						});
					}
				})
				/***
				 * 绑定8000 端口
				 */
				.bind(8000);
	}
}
