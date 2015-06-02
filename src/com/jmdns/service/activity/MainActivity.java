package com.jmdns.service.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	public String Tag = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		try {
			new Thread(new networkRecieve()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int SERVERPORT = 60034;

	class networkRecieve implements Runnable {

		@Override
		public void run() {
			ServerSocket messageserver = null;
			try {
				messageserver = new ServerSocket(SERVERPORT);
				while (true) {
					Socket messagesocket = messageserver.accept();
					Log.e(Tag, "text:开始接收数据");
					InputStream inputStream = messagesocket.getInputStream();
					// 读取客户端socket的输入流的内容并输出
					byte[] buffer = new byte[512];
					int temp = 0;
					while ((temp = inputStream.read(buffer)) != -1) {
						Log.e(Tag, "接收数据为:" + new String(buffer, 0, temp));
					}
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					messageserver.close();
					messageserver.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	class networkSend implements Runnable {

		Socket messagesendsocket;
		OutputStream ops;

		@Override
		public void run() {
			try {
				messagesendsocket = new Socket(InetAddress.getLocalHost(),
						SERVERPORT);
				ops = messagesendsocket.getOutputStream();
				String datatosend = new String();

				byte[] buf = datatosend.getBytes();
				ops.write(buf);
				ops.flush();
				messagesendsocket.close();
				ops.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
