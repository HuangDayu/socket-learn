package cn.huangdayu.socket.nio;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class NIOSocketClient {
	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Socket socket = new Socket("127.0.0.1", 8001);
					while (true) {
						int len = 0;
						byte[] data = new byte[1024];

						try {
							socket.getOutputStream().write((new Date() + ": hello world").getBytes());
							Thread.sleep(2000);
						} catch (Exception e) {
						}
					/*
					try {
						if ((len = socket.getInputStream().read(data)) != -1) {
							System.out.println(new String(data, 0, len));
							Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
					}
					*/
					}
				} catch (IOException e) {
				}
			}
		}).start();
	}
}
