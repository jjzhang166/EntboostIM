package com.entboost.im.department;

import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostUM;
import net.yunim.service.entity.GroupInfo;
import net.yunim.service.listener.EditGroupListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class EditGroupNameActivity extends EbActivity {

	@ViewInject(R.id.groupname_username)
	private EditText groupname_username;
	private Long depid;
	private GroupInfo groupInfo;
	
	@OnClick(R.id.groupname_save_btn)
	public void save(View view) {
		final String sNewUserName=groupname_username.getText().toString();
		showProgressDialog("修改名称");
		EntboostUM.editGroupName(depid, sNewUserName, new EditGroupListener() {
			
			@Override
			public void onFailure(String errMsg) {
				showToast(errMsg);
				removeProgressDialog();
			}
			
			@Override
			public void onEditGroupSuccess(Long dep_code) {
				removeProgressDialog();
				finish();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_edit_group_name);
		ViewUtils.inject(this);
		depid = getIntent().getLongExtra("depid", -1);
		groupInfo = EntboostCache.getGroup(depid);
		groupname_username.setText(groupInfo.getDep_name());
	}
	
	@OnClick(R.id.groupname_cancel_btn)
	public void cancel(View view){
		finish();
	}


}
