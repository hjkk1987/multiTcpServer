package com.atet.jmdns.app;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.atet.jmdns.connect.ConnectionWrapper;
import com.atet.jmdns.connect.ConnectionWrapper.OnCreatedListener;
import com.atet.jmdns.fun.Jmdns;
import com.jmdns.multicast.device.MultiSocket;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

public class JmdnsAPP extends Application {
	private String Tag = JmdnsAPP.class.getName();
	public static Jmdns mJmdns = null;
	private WifiManager wifiManager = null;
	private int defaultPort = 8080;
	private ConnectionWrapper mConnectionWrapper = null;
	public static MultiSocket multiSocket = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("Applications", "start!");
		multiSocket = new MultiSocket(getApplicationContext());
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		new Thread() {
			public void run() {
				mJmdns = new Jmdns(getApplicationContext());
				mJmdns.startDiscover("haha");
			};
		}.start();
		createConnectionWrapper(new OnCreatedListener() {

			@Override
			public void onCreated() {
				// TODO Auto-generated method stub
				Log.e(Tag, "CreateConnectWrapper");
				mConnectionWrapper.startServer();
			}
		});

	}

	public void createConnectionWrapper(
			ConnectionWrapper.OnCreatedListener listener) {
		mConnectionWrapper = new ConnectionWrapper(getApplicationContext(),
				listener);
	}

	public ConnectionWrapper getConnectionWrapper() {
		return mConnectionWrapper;
	}

	public void setHandler(Handler handler) {
		mConnectionWrapper.setHandler(handler);
	}

	/**
	 * ַ 获取Ip地址
	 * 
	 * @param wifi
	 * @return
	 */
	private InetAddress getDeviceIpAddress(WifiManager wifi) {
		InetAddress result = null;
		try {
			// default to Android localhost
			result = InetAddress.getByName("10.0.0.2");
			// figure out our wifi address, otherwise bail
			WifiInfo wifiinfo = wifi.getConnectionInfo();
			int intaddr = wifiinfo.getIpAddress();
			byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff),
					(byte) (intaddr >> 8 & 0xff),
					(byte) (intaddr >> 16 & 0xff),
					(byte) (intaddr >> 24 & 0xff) };
			result = InetAddress.getByAddress(byteaddr);
		} catch (UnknownHostException ex) {
			Log.d("",
					String.format("getDeviceIpAddress Error: %s",
							ex.getMessage()));
		}

		return result;
	}

}
