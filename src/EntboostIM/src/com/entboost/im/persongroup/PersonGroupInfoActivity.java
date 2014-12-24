package com.entboost.im.persongroup;

import net.yunim.service.entity.GroupInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.entboost.im.chat.ChatActivity;
import com.entboost.im.department.MemberListActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PersonGroupInfoActivity extends EbActivity {

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
	
	@OnClick(R.id.department_member_layout)
	public void showMemberlist(View view){
		Intent intent = new Intent(this, MemberListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("group", departmentInfo);
		this.startActivity(intent);
	}

	private void init() {
		TextView name = (TextView) findViewById(R.id.department_username);
		TextView description = (TextView) findViewById(R.id.department_description);
		TextView creator = (TextView) findViewById(R.id.department_account);
		TextView memberNum = (TextView) findViewById(R.id.department_member);
		if (departmentInfo != null) {
			memberNum.setText(departmentInfo.getEmp_count() + "");
			creator.setText(departmentInfo.getCreator());
			name.setText(departmentInfo.getDep_name());
			description.setText(departmentInfo.getDescription());
		}
	}
	
	@OnClick(R.id.department_send_btn)
	public void sendMsg(View view){
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
