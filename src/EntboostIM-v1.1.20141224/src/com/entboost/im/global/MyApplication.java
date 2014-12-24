package com.entboost.im.global;

import net.yunim.service.Entboost;
import android.app.Application;

public class MyApplication extends Application {

	private static MyApplication myInstance;

	private boolean showNotificationMsg = false;

	public boolean isShowNotificationMsg() {
		return showNotificationMsg;
	}

	public void setShowNotificationMsg(boolean showNotificationMsg) {
		this.showNotificationMsg = showNotificationMsg;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		myInstance = this;
		initEbConfig();
	}

	public void initEbConfig() {
		Entboost.init(getApplicationContext());
		Entboost.showSotpLog(true);
	}

	public static MyApplication getInstance() {
		return myInstance;
	}

}
