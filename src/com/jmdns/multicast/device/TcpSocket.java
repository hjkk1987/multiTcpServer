package com.jmdns.multicast.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

public class TcpSocket implements Runnable {

	private Context context = null;

	public Thread serverThread = null;

	private ServerSocket serverSocket = null;

	public String Tag = "TcpSocket";

	private static int SERVERPORT = 60034;

	private static List<Socket> mClientList = new ArrayList<Socket>();
	// 线程池
	private ExecutorService mExecutorService;

	private boolean isRunning = false;

	public TcpSocket(Context context) {
		this.context = context;

	}

	/**
	 * @description: 对不同的客户端分别用线程处理,注意启动的时候需要将数据
	 * 
	 * @throws:
	 * @author: HuJun
	 * @date: 2015年6月2日 下午4:31:32
	 */
	public void startTcp() {
		try {
			serverSocket = new ServerSocket(SERVERPORT);
			mExecutorService = Executors.newCachedThreadPool();
			Socket client = null;
			isRunning = true;
			while (isRunning) {
				client = serverSocket.accept();
				mClientList.add(client);
				mExecutorService.execute(new ThreadServer(client));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		isRunning = true;
		startTcp();
	}

	public void stopTcp() {
		isRunning = false;
		if (serverSocket != null) {
			try {
				serverSocket.close();
				serverSocket = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		mExecutorService.shutdownNow();
	}

	/**
	 * @description: 服务端数据接收
	 * 
	 * @author: HuJun
	 * @date: 2015年6月2日 下午4:32:08
	 */
	private class ThreadServer implements Runnable {
		private Socket mSocket;

		public ThreadServer(Socket socket) {
			this.mSocket = socket;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			InputStream inputStream;
			try {
				inputStream = mSocket.getInputStream();
				byte[] buffer = new byte[512];
				int temp = 0;
				while ((temp = inputStream.read(buffer)) != -1) {
					Log.e(Tag, "接收数据为:" + new String(buffer, 0, temp));
				}
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
