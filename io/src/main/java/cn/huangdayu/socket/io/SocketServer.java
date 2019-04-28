package cn.huangdayu.socket.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SocketServer {
	
	private static int sum = 1;

	
	public static void main(String[] args) throws Exception {

		// 创建 Socket Server 监听 8000 端口
		ServerSocket serverSocket = new ServerSocket(8000);
		
		// (1) 接收新连接线程
		new Thread(() -> {
			while (true) {
				try {
					// (1) 阻塞方法获取新的连接
					Socket socket = serverSocket.accept();

					// (2) 每一个新的连接都创建一个线程，负责读取数据
					new Thread(() -> {
						try {
							int len;
							byte[] data = new byte[1024];
							InputStream inputStream = socket.getInputStream();
							// (3) 按字节流方式读取数据
							while ((len = inputStream.read(data)) != -1) {
								System.out.println(new String(data, 0, len));
								OutputStream outputStream = socket.getOutputStream();
								outputStream.write((new Date()+":Hi "+(sum++)).getBytes());
							}
						} catch (IOException e) {
						}
					}).start();

				} catch (IOException e) {
				}

			}
		}).start();
	}
}
