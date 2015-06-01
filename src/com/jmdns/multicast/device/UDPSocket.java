package com.jmdns.multicast.device;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.content.Context;
import android.util.Log;

/*
 * File：UDPSocket.java
 *
 * Copyright (C) 2015 JmdnsClient Project
 * Date：2015年6月1日 上午9:34:38
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */

/**
 * @description:
 * 
 * @author: HuJun
 * @date: 2015年6月1日 上午9:34:38
 */

public class UDPSocket implements Runnable {
	private Context context = null;
	private DatagramSocket socket = null;
	private boolean isRunning = false;
	private String Tag = UDPSocket.class.getName();
	private Thread socketThread = null;

	/**
	 * Constructors： UDPSocket.
	 * 
	 * @param context
	 * @param inetAddress
	 *            服务端对应的IP地址
	 */
	public UDPSocket(Context context) {
		this.context = context;
		initSocket();
		startSocket();
	}

	/**
	 * @description: 初始化Socket
	 * 
	 * @throws:
	 * @author: HuJun
	 * @date: 2015年6月1日 上午9:36:32
	 */
	private void initSocket() {

		try {
			socket = new DatagramSocket(60034);// 将服务器的端口号标记为60034
			socket.setSoTimeout(20);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// new 一个本地的socket
	}

	/**
	 * @description:
	 * 
	 * @throws:
	 * @author: HuJun
	 * @date: 2015年6月1日 下午1:52:03
	 */
	public void startSocket() {
		if (socketThread == null || !socketThread.isAlive()) {
			socketThread = new Thread(UDPSocket.this);
			socketThread.start();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		isRunning = true;
		byte[] buff = new byte[19];
		while (isRunning) {
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			int length;
			while (packet.getLength() != 0) {
				try {
					socket.receive(packet);
					byte[] recver = packet.getData();
					String strRec = new String(recver);
					Log.e(Tag, "接收到数据:" + strRec);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * @description: 退出APP
	 * 
	 * @throws:
	 * @author: HuJun
	 * @date: 2015年6月1日 上午11:49:50
	 */
	public void stop() {
		isRunning = false;// 是否运行
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}
}
