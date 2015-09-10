package com.entboost.im.department;

import net.yunim.service.EntboostCache;
import net.yunim.service.constants.EB_GROUP_TYPE;
import net.yunim.service.entity.GroupInfo;
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
	private Long depid;

	
	@Override
	protected void onResume() {
		super.onResume();
		//获取群组编号
		depid = getIntent().getLongExtra("depid", -1);
		//从缓存中，根据群组编号获取群组对象
		departmentInfo = EntboostCache.getGroup(depid);
		init();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_department_info);
		ViewUtils.inject(this);
//		//获取群组编号
//		depid = getIntent().getLongExtra("depid", -1);
//		//从缓存中，根据群组编号获取群组对象
//		departmentInfo = EntboostCache.getGroup(depid);
//		init();
	}

	private void init() {
		//设置群组信息
		TextView name = (TextView) findViewById(R.id.department_username);
		TextView description = (TextView) findViewById(R.id.department_description);
		TextView depId = (TextView) findViewById(R.id.department_account);
		TextView memberNum = (TextView) findViewById(R.id.department_member);
		TextView type=(TextView) findViewById(R.id.department_type);
		TextView tel=(TextView) findViewById(R.id.department_tel);
		TextView fax=(TextView) findViewById(R.id.department_fax);
		TextView email=(TextView) findViewById(R.id.department_email);
		TextView home=(TextView) findViewById(R.id.department_home);
		TextView addr=(TextView) findViewById(R.id.department_addr);
		View send_btn = findViewById(R.id.department_send_btn);
		findViewById(R.id.department_del_btn).setVisibility(View.GONE);
		if (departmentInfo != null) {
			memberNum.setText(departmentInfo.getEmp_count() + "");
			depId.setText(departmentInfo.getDep_code()+"");
			type.setText(getResources().getStringArray(
					R.array.group_type)[EB_GROUP_TYPE.getIndex(departmentInfo.getType())]);
			tel.setText(departmentInfo.getPhone());
			fax.setText(departmentInfo.getFax());
			email.setText(departmentInfo.getEmail());
			home.setText(departmentInfo.getUrl());
			addr.setText(departmentInfo.getAddress());
			name.setText(departmentInfo.getDep_name());
			description.setText(departmentInfo.getDescription());
			//当前登录用户不在该群组时，不能进行群组会话
			if (departmentInfo.getMy_emp_id() == null
					|| departmentInfo.getMy_emp_id() <= 0) {
				send_btn.setVisibility(View.GONE);
			}
		}
	}

	@OnClick(R.id.department_member_layout)
	public void showMemberlist(View view) {
		Intent intent = new Intent(this, MemberListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("depid", depid);
		this.startActivity(intent);
	}
	
	@OnClick(R.id.department_name_layout)
	public void editGroupName(View view){
		Intent intent = new Intent(this, EditGroupNameActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("depid", depid);
		this.startActivity(intent);
	}

	@OnClick(R.id.department_send_btn)
	public void sendMsg(View view) {
		Intent intent = new Intent(this, ChatActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ChatActivity.INTENT_TITLE, departmentInfo.getDep_name());
		intent.putExtra(ChatActivity.INTENT_UID, departmentInfo.getDep_code());
		intent.putExtra(ChatActivity.INTENT_CHATTYPE,
				ChatActivity.CHATTYPE_GROUP);
		this.startActivity(intent);
	}

}
