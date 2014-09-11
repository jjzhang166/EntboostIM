package com.entboost.im.setting;

import org.apache.commons.lang.StringUtils;

import net.yunim.service.EntboostLC;
import net.yunim.service.EntboostUM;
import net.yunim.service.listener.EditInfoListener;
import net.yunim.utils.UIUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SetLogonServiceAddrActivity extends EbActivity {
	@ViewInject(R.id.setService_name)
	private EditText setService_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_set_logon_service_addr);
		ViewUtils.inject(this);
		((EditText) findViewById(R.id.setService_name)).setText(EntboostLC
				.getLogonCenterAddr());
	}

	@OnClick(R.id.setService_save_btn)
	public void save(View view) {
		String name = setService_name.getText().toString();
		EntboostLC.setLogonCenterAddr(name);
		UIUtils.showToast(this, "设置服务器地址成功！");
		finish();
	}

	@OnClick(R.id.setService_cancel_btn)
	public void cancel(View view) {
		finish();
	}

}
