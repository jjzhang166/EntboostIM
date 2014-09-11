package com.entboost.im.global;

import net.yunim.service.Entboost;
import android.app.Application;

public class MyApplication extends Application {
	
	private Long app_id = 874562130982l;
	private String app_pwd = "ec289op09uh5axs34152bnm7856debva";
	
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
		Entboost.init(getApplicationContext());
		initEbConfig();
	}
	
	public void initEbConfig(){
		Entboost.showSotpLog(true);
	}
	
	public static MyApplication getInstance() {
		return myInstance;
	}

	public Long getApp_id() {
		return app_id;
	}

	public String getApp_pwd() {
		return app_pwd;
	}
	
}
