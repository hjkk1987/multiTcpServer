package com.jmdns.service.activity;

import com.atet.jmdns.app.JmdnsAPP;
import com.atet.jmdns.connect.Communication;
import com.atet.jmdns.connect.ConnectionWrapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {

	private Button btnRegist = null;
	private String Tag = MainActivity.class.getName();
	private TextView tvMsg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((JmdnsAPP) getApplication()).setHandler(mHandler);
		btnRegist = (Button) findViewById(R.id.btn_register);
		tvMsg = (TextView) findViewById(R.id.tvMsg);
		JmdnsAPP.multiSocket.startSocket();
		btnRegist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String strMsg = bundle.getString(Communication.MESSAGE);
			tvMsg.setText("receiver:" + strMsg);
			Log.e(Tag, "receiver:" + strMsg);
		};
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		JmdnsAPP.mJmdns.exit();
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
