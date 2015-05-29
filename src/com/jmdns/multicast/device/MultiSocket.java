package com.jmdns.multicast.device;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;

/*
 * File：MultiSocket.java
 *
 * Copyright (C) 2015 multiCastClient Project
 * Date：2015年5月15日 下午12:05:35
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */

/**
 * @description: 设计SOCKET的客户端与服务端，这样就可以直接广播出去了
 * @author: HuJun
 * @date: 2015年5月15日 下午12:05:35
 */

public class MultiSocket implements Runnable {
	private Context context = null;
	private boolean isStop = false;
	private MulticastSocket multicastSocket = null;
	private InetAddress inetAddress = null;
	private int disPort = 0;
	private Thread socketThread = null;
	private String Tag = MultiSocket.class.getName();

	public MultiSocket(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		initSocket();

	}

	/**
	 * @description: 设置设备服务端
	 * 
	 * @throws:
	 * @author: HuJun
	 * @date: 2015年5月29日 上午9:03:39
	 */
	private void initSocket() {
		try {
			multicastSocket = new MulticastSocket(8003);
			inetAddress = InetAddress.getByName("239.0.0.1"); // 必须使用D类地址
			multicastSocket.joinGroup(inetAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 以D类地址为标识，加入同一个组才能实现广播

	}

	/**
	 * @description:
	 * 
	 * @throws:
	 * @author: HuJun
	 * @date: 2015年5月29日 上午8:59:01
	 */
	public void startSocket() {
		if (socketThread == null || !socketThread.isAlive()) {
			socketThread = new Thread(MultiSocket.this);
			socketThread.start();
		}
	}

	/**
	 * @description:
	 * 
	 * @throws:
	 * @author: HuJun
	 * @date: 2015年5月29日 上午8:59:13
	 */
	public void stopSocket() {
		isStop = true;
	}

	public void setPort(int port) {
		this.disPort = port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		byte buf[] = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, 1024);

		while (!isStop) {

			try {
				multicastSocket.receive(dp);
				String str = new String(buf, 0, dp.getLength());
				Log.e(Tag, "服务器接收: " + str);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (multicastSocket != null) {
			try {
				multicastSocket.leaveGroup(inetAddress);
				multicastSocket.close();
				multicastSocket = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
