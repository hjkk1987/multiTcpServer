package com.atet.jmdns.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class JmdnsService extends Service {
	private ServiceBinder mBinder = new ServiceBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public class ServiceBinder extends Binder {
		public JmdnsService getService() {
			return JmdnsService.this;
		}
	}

}
