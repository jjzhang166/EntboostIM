package com.entboost.im.user;

import net.yunim.service.EntboostUM;
import net.yunim.service.listener.EditInfoListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class InfoNameEditActivity extends EbActivity {
	@ViewInject(R.id.infoname_username)
	private EditText infoname_username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_info_name_edit);
		ViewUtils.inject(this);
	}

	@OnClick(R.id.infoname_save_btn)
	public void save(View view) {
		String sNewUserName=infoname_username.getText().toString();
		showProgressDialog("修改用户名称");
		EntboostUM.changeUserName(sNewUserName, new EditInfoListener() {
			
			@Override
			public void onFailure(String errMsg) {
				pageInfo.showError(errMsg);
				removeProgressDialog();
			}
			
			@Override
			public void onEditInfoSuccess() {
				removeProgressDialog();
				finish();
			}
		});
	}
	
	@OnClick(R.id.infoname_cancel_btn)
	public void cancel(View view){
		finish();
	}

}
