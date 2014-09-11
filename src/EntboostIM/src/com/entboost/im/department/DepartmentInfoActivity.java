package com.entboost.im.department;

import net.yunim.service.entity.GroupInfo;
import net.yunim.utils.UIUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.entboost.im.chat.ChatActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class DepartmentInfoActivity extends EbActivity {

	private GroupInfo departmentInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_department_info);
		ViewUtils.inject(this);
		departmentInfo = (GroupInfo) getIntent().getSerializableExtra(
				"departmentInfo");
		init();
	}

	private void init() {
		TextView name = (TextView) findViewById(R.id.department_name);
		TextView description = (TextView) findViewById(R.id.department_description);
		if (departmentInfo != null) {
			name.setText(departmentInfo.getDep_name());
			description.setText(departmentInfo.getDescription());
		}
	}
	
	@OnClick(R.id.department_send_btn)
	public void sendMsg(View view){
		if(departmentInfo.getOwnType()==1){
			UIUtils.showToast(this, "不是所属企业群组，无法进行群组会话！");
			return;
		}
		Intent intent = new Intent(this, ChatActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ChatActivity.INTENT_TITLE, departmentInfo.getDep_name());
		intent.putExtra(ChatActivity.INTENT_UID, departmentInfo.getDep_code());
		intent.putExtra(ChatActivity.INTENT_CHATTYPE, ChatActivity.CHATTYPE_GROUP);
		this.startActivity(intent);
	}

}
