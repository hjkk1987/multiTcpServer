package com.atet.jmdns.fun;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;

import com.atet.jmdns.com.LOG;
import com.atet.jmdns.com.Param;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class Jmdns implements ServiceListener, ServiceTypeListener {
	private String searchType = "";
	private JmDNS jmdns = null;
	private LOG log = null;
	private WifiManager wifiManager = null;
	private String defaultType = "_teleplus._tcp.local.";
	/**
	 * Allows an application to receive Wifi Multicast packets.
	 */
	private static MulticastLock multicastLock = null;
	private static final String HOSTNAME = "melloware";
	private Handler mHandler = null;
	public final static String REMOTE_TYPE = "_teleplus._tcp.local.";
	private ServiceInfo pairService = null;

	public Jmdns(final Context context) {
		// TODO Auto-generated constructor stub
		log = new LOG();
		new Thread() {
			public void run() {
				Logger logger = Logger.getLogger(JmDNS.class.getName());
				ConsoleHandler handler = new ConsoleHandler();
				logger.addHandler(handler);
				logger.setLevel(Level.FINER);
				handler.setLevel(Level.FINER);
				wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				multicastLock = wifiManager.createMulticastLock(getClass()
						.getName());
				multicastLock.setReferenceCounted(true);
				multicastLock.acquire();
				// Create(inetAddress);
				createService();
				log.e("jmdns start");
			};
		}.start();

	}

	public void createService() {
		boolean log = true;
		if (log) {
			ConsoleHandler handler = new ConsoleHandler();
			handler.setLevel(Level.FINEST);
			for (Enumeration<String> enumerator = LogManager.getLogManager()
					.getLoggerNames(); enumerator.hasMoreElements();) {
				String loggerName = enumerator.nextElement();
				Logger logger = Logger.getLogger(loggerName);
				logger.addHandler(handler);
				logger.setLevel(Level.FINEST);
			}
		}

		try {
			JmDNS jmdns = JmDNS.create();
			pairService = ServiceInfo.create(REMOTE_TYPE, "TestService", 0,
					"create a service for jmdns");
			jmdns.registerService(pairService);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setHandler(Handler mHandler) {
		// TODO Auto-generated method stub
		this.mHandler = mHandler;
	}

	public void clear() {
		mHandler = null;
	}

	private void sendMessage(int action, Object obj) {
		if (mHandler != null) {
			Message msg = Message.obtain();
			msg.arg1 = action;
			msg.obj = obj;
			mHandler.sendMessage(msg);
		}
	}

	

	/**
	 * ����Jmdns����
	 */
	public void Create(final InetAddress address) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					jmdns = JmDNS.create(address, HOSTNAME);
					jmdns.addServiceTypeListener(Jmdns.this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

	public void serviceResolved(ServiceInfo serviceInfo) {
		if (serviceInfo != null && !TextUtils.isEmpty(serviceInfo.getName())
				&& !TextUtils.isEmpty(serviceInfo.getType())) {

		}
	}

	/**
	 * 
	 */
	public void startDiscover(String serviceType) {

	}

	@Override
	public void serviceTypeAdded(ServiceEvent arg0) {
		// TODO Auto-generated method stub
		String type = arg0.getType();
		if (defaultType.equals(type)) {
			jmdns.addServiceListener(arg0.getType(), Jmdns.this);
			log.e("jmdns type:" + arg0.getType());
		}
	}

	@Override
	public void subTypeForServiceTypeAdded(ServiceEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serviceAdded(ServiceEvent arg0) {
		// TODO Auto-generated method stub

		ServiceInfo serviceInfo = arg0.getInfo();
		String name = serviceInfo.getName();
		sendMessage(Param.GET_A_DEVICE, serviceInfo);
		jmdns.requestServiceInfo(arg0.getType(), arg0.getName(), 1);
		if (name != null) {
			log.e(name);
			log.e("地址为:" + serviceInfo.getHostAddress());
		}
	}

	public void connect(ServiceInfo serviceInfo) {
		String hostAddress = serviceInfo.getHostAddress();
		log.e(hostAddress);

	}

	@Override
	public void serviceRemoved(ServiceEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serviceResolved(ServiceEvent arg0) {
		// TODO Auto-generated method stub
		log.e("serviceResolved");
	}

	public void exit() {
		if (jmdns != null) {
			try {
				jmdns.close();
				jmdns.unregisterService(pairService);
				jmdns.unregisterAllServices();
				jmdns.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
