package com.entboost.im.user;

import net.yunim.service.EntboostLC;
import net.yunim.service.entity.AccountInfo;
import net.yunim.service.listener.FindPWDListener;
import net.yunim.service.listener.LogonAccountListener;
import net.yunim.utils.UIUtils;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.entboost.im.MainActivity;
import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.entboost.im.global.MyApplication;
import com.entboost.im.setting.SetLogonServiceAddrActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class LoginActivity extends EbActivity {
	@ViewInject(R.id.login_username)
	private EditText loginName;
	@ViewInject(R.id.login_passwd)
	private EditText loginPWD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.login_setService)
	public void setServiceAddr(View view){
		Intent intent = new Intent(LoginActivity.this, SetLogonServiceAddrActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.login_login_btn)
	public void login(View view) {
		String name = loginName.getText().toString();
		String pwd = loginPWD.getText().toString();
		// 空值校验
		if (StringUtils.isBlank(name)) {
			pageInfo.showError("帐号不能为空！");
			return;
		}
		if (StringUtils.isBlank(pwd)) {
			pageInfo.showError("密码不能为空！");
			return;
		}
		showProgressDialog("努力登录中...");
		EntboostLC.logon(MyApplication.getInstance().getApp_id(), MyApplication
				.getInstance().getApp_pwd(), name, pwd,
				new LogonAccountListener() {

					@Override
					public void onFailure(String errMsg) {
						pageInfo.showError(errMsg);
						removeProgressDialog();
					}

					@Override
					public void onLogonSuccess(AccountInfo pAccountInfo) {
						removeProgressDialog();
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}
				});
	}

	@OnClick(R.id.login_forget_passwd)
	public void forgetPwd(View view) {
		// TODO 校验
		String name = loginName.getText().toString();
		if (StringUtils.isBlank(name)) {
			pageInfo.showError("帐号不能为空！");
			return;
		}
		showProgressDialog("重置密码正在发往注册邮箱中，请稍候");
		EntboostLC.findPwd(MyApplication.getInstance().getApp_id(),
				MyApplication.getInstance().getApp_pwd(), name,
				new FindPWDListener() {

					@Override
					public void onFailure(String errMsg) {
						pageInfo.showError(errMsg);
						removeProgressDialog();
					}

					@Override
					public void onFindPWDSuccess() {
						removeProgressDialog();
						UIUtils.showToast(LoginActivity.this,
								"成功找回密码，请到注册邮箱中查看！");
					}
				});
	}

	@OnClick(R.id.login_register)
	public void register(View view) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
