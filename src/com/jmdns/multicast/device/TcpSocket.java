package com.jmdns.multicast.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

/*
 * File：TcpSocket.java
 *
 * Copyright (C) 2015 JmdnsService Project
 * Date：2015年6月1日 下午3:45:51
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */

/**
 * @description:
 * 
 * @author: HuJun
 * @date: 2015年6月1日 下午3:45:51
 */

public class TcpSocket {

	private Context context = null;

	public Thread serverThread = null;

	private serverSocket serSocket = null;

	public String Tag = "TcpSocket";

	public TcpSocket(Context context) {
		this.context = context;

	}

	public void startSocket() {
		if (serverThread == null || !serverThread.isAlive()) {
			serSocket = new serverSocket();
			serverThread = new Thread(serSocket);
			serverThread.start();
		}
	}

	public class serverSocket implements Runnable {

		private ServerSocket mSockets = null;

		private Socket socket = null;

		private static final int SERVERPORT = 60034;// 服务端的端口号是

		private boolean isRunning = false;

		private BufferedReader bufferedReader = null;
		private String msg = "";

		public serverSocket() {
			isRunning = true;
			initSocket();
		}

		/**
		 * @description:
		 * 
		 * @throws:
		 * @author: HuJun
		 * @date: 2015年6月1日 下午4:05:43
		 */
		private void initSocket() {
			try {
				Log.e(Tag, "初始化Socket");
				mSockets = new ServerSocket(SERVERPORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void stop() {
			isRunning = false;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRunning) {
				try {
					socket = mSockets.accept();
					InputStream inputStream = socket.getInputStream();
					// 读取客户端socket的输入流的内容并输出
					byte[] buffer = new byte[512];
					int temp = 0;
					while ((temp = inputStream.read(buffer)) != -1) {
						Log.e(Tag, "接收数据为:" + new String(buffer, 0, temp));
					}
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					close();
				}

			}
		}

		public void close() {
			Log.e(Tag, "服务socket断开");
			isRunning = false;
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bufferedReader = null;
			}

			if (socket != null) {
				try {
					socket.close();
					socket = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public void close() {
		if (serSocket != null) {
			serSocket.close();
		}
	}

}
