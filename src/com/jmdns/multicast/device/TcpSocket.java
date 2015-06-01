package com.jmdns.multicast.device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class TcpSocket implements Runnable {
	private String Tag = "TcpSocket";
	private Thread tcpThread = null;

	public void startSocket() {
		if (tcpThread == null || !tcpThread.isAlive()) {
			tcpThread = new Thread(TcpSocket.this);
			tcpThread.start();
		}
	}

	public void run() {
		try {
			// 创建ServerSocket
			ServerSocket serverSocket = new ServerSocket(8060);
			while (true) {
				// 接受客户端请求
				Log.e(Tag, "获取客户端");
				Socket client = serverSocket.accept();
				try {
					// 接收客户端消息
					BufferedReader in = new BufferedReader(
							new InputStreamReader(client.getInputStream()));
					String str = in.readLine();
					Log.e(Tag, "read:" + str);
					in.close();
				} catch (Exception e) {
					Log.e(Tag, e.getMessage());
					e.printStackTrace();
				} finally {
					// 关闭
					client.close();
					Log.e(Tag, "close");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}