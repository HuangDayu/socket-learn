package cn.huangdayu.socket.io;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class SocketClient {
	public static void main(String[] args) {
		new Thread(() -> {
			try {
				Socket socket = new Socket("127.0.0.1", 8000);
				while (true) {
					int len = 0;
					byte[] data = new byte[1024];

					try {
						socket.getOutputStream().write((new Date() + ": hello world").getBytes());
						Thread.sleep(2000);
					} catch (Exception e) {
					}

					try {
						if ((len = socket.getInputStream().read(data)) != -1) {
							System.out.println(new String(data, 0, len));
							Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
					}
					
				}
			} catch (IOException e) {
			}
		}).start();
	}
}
