package com.entboost.im;

import net.yunim.eb.signlistener.EntboostIMListener;
import net.yunim.service.Entboost;
import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostLC;
import net.yunim.service.entity.AccountInfo;
import net.yunim.service.listener.LogonAccountListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.entboost.im.global.MyApplication;
import com.entboost.im.user.LoginActivity;

public class WelcomeActivity extends Activity {

	@Override
	protected void onResume() {
		super.onResume();
		Entboost.addListener("WelcomeListener", new EntboostIMListener() {

			@Override
			public void localNoNetwork() {
				new AlertDialog.Builder(WelcomeActivity.this).setTitle("提示")
						.setMessage(R.string.msg_error_localNoNetwork)
						.setPositiveButton("确定", null).show();
			}

			@Override
			public void serviceNoNetwork() {
				new AlertDialog.Builder(WelcomeActivity.this).setTitle("提示")
						.setMessage(R.string.msg_error_serviceNoNetwork)
						.setPositiveButton("确定", null).show();
			}

		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		Entboost.removeListener("WelcomeListener");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		SharedPreferences preferences = getSharedPreferences("first",
				Context.MODE_PRIVATE);
		boolean isFirst = preferences.getBoolean("isfrist", true);
		if (isFirst) {
			createDeskShortCut();
		}
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("isfrist", false);
		editor.commit();

		AccountInfo user = EntboostCache.getLocalAccountInfo();
		if (user == null) {
			finish();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else {
			MyApplication.getInstance().initEbConfig();
			EntboostLC.logon(MyApplication.getInstance().getApp_id(),
					MyApplication.getInstance().getApp_pwd(),
					user.getAccount(), new LogonAccountListener() {

						@Override
						public void onFailure(String errMsg) {
							finish();
							Intent intent = new Intent(WelcomeActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}

						@Override
						public void onLogonSuccess(AccountInfo pAccountInfo) {
							finish();
							Intent intent = new Intent(WelcomeActivity.this,
									MainActivity.class);
							startActivity(intent);
						}
					});
		}
	}

	/**
	 * 创建快捷方式
	 */
	public void createDeskShortCut() {

		Log.i("coder", "------createShortCut--------");
		// 创建快捷方式的Intent
		Intent shortcutIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建
		shortcutIntent.putExtra("duplicate", false);
		// 需要现实的名称
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));

		// 快捷图片
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				getApplicationContext(), R.drawable.ic_launcher);

		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		Intent intent = new Intent(getApplicationContext(),
				WelcomeActivity.class);
		// 下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		// 点击快捷图片，运行的程序主入口
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 发送广播。OK
		sendBroadcast(shortcutIntent);
	}
}
