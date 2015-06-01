package com.jmdns.service.activity;

import java.io.BufferedReader;
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

	private static final String SERVERIP = "127.0.0.1";
	private static final int SERVERPORT = 60034;

	class networkRecieve implements Runnable {

		ServerSocket messageserver = null;
		Socket messagesocket = null;

		@Override
		public void run() {
			try {
				messageserver = new ServerSocket(SERVERPORT);
				Log.e(Tag, "text:开始接收数据");
				while (true) {
					messagesocket = messageserver.accept();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(
									messagesocket.getInputStream()));
					String text = "";
					while ((text = br.readLine()) != null) {
						// show.setText(text);
						Log.e(Tag, "text:" + text);
					}

					messageserver.close();
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
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
