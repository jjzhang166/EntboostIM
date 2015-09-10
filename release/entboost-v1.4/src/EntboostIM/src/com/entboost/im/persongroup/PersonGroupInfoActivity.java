package com.entboost.im.persongroup;

import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostUM;
import net.yunim.service.constants.EB_GROUP_TYPE;
import net.yunim.service.constants.EB_MANAGER_LEVEL;
import net.yunim.service.entity.GroupInfo;
import net.yunim.service.entity.MemberInfo;
import net.yunim.service.listener.DelGroupListener;
import net.yunim.service.listener.LoadMemberListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.entboost.im.chat.ChatActivity;
import com.entboost.im.department.EditGroupNameActivity;
import com.entboost.im.department.MemberListActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PersonGroupInfoActivity extends EbActivity {

	private GroupInfo groupInfo;
	private Long depid;

	@Override
	protected void onResume() {
		super.onResume();
		//获取群组编号
		depid = getIntent().getLongExtra("depid", -1);
		//从缓存中，根据群组编号获取群组对象
		groupInfo = EntboostCache.getGroup(depid);
		init();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_department_info);
		ViewUtils.inject(this);
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

	@OnClick(R.id.department_member_layout)
	public void showMemberlist(View view) {
		Intent intent = new Intent(this, MemberListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("depid", depid);
		this.startActivity(intent);
	}

	private void init() {
		// 设置群组信息
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
		final Button umbtn = (Button) findViewById(R.id.department_usermanager);
		if (groupInfo != null) {
			memberNum.setText(groupInfo.getEmp_count() + "");
			depId.setText(groupInfo.getDep_code()+"");
			name.setText(groupInfo.getDep_name());
			type.setText(getResources().getStringArray(
				R.array.group_type)[EB_GROUP_TYPE.getIndex(groupInfo.getType())]);
			tel.setText(groupInfo.getPhone());
			fax.setText(groupInfo.getFax());
			email.setText(groupInfo.getEmail());
			home.setText(groupInfo.getUrl());
			addr.setText(groupInfo.getAddress());
			description.setText(groupInfo.getDescription());
			// 群组的创建者
			if (groupInfo.getCreate_uid() - EntboostCache.getUid() == 0) {
				umbtn.setVisibility(View.VISIBLE);
			} else {
				umbtn.setVisibility(View.GONE);
			}
			// 拥有群组管理权限
			if (groupInfo.getMy_emp_id() != null
					|| groupInfo.getMy_emp_id() > 0) {
				//首先需要加载登录用户在当前群组的成员信息，获取在群组的权限
				EntboostUM.loadMemberByCode(groupInfo.getMy_emp_id(),
						new LoadMemberListener() {

							@Override
							public void onFailure(String errMsg) {
							}

							@Override
							public void onLoadMemberSuccess() {
								MemberInfo member = EntboostCache
										.getMemberByCode(groupInfo
												.getMy_emp_id());
								if ((member.getManager_level() & EB_MANAGER_LEVEL.EB_LEVEL_EMP_EDIT
										.getValue()) == EB_MANAGER_LEVEL.EB_LEVEL_EMP_EDIT
										.getValue()) {
									umbtn.setVisibility(View.VISIBLE);
								}
							}
						});
			}
		}
	}

	@OnClick(R.id.department_usermanager)
	public void usermanager(View view) {
		Intent intent = new Intent(this, MemberSelectActivity.class);
		intent.putExtra("groupid", depid);
		startActivity(intent);
	}

	@OnClick(R.id.department_send_btn)
	public void sendMsg(View view) {
		Intent intent = new Intent(this, ChatActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ChatActivity.INTENT_TITLE, groupInfo.getDep_name());
		intent.putExtra(ChatActivity.INTENT_UID, groupInfo.getDep_code());
		intent.putExtra(ChatActivity.INTENT_CHATTYPE,
				ChatActivity.CHATTYPE_GROUP);
		this.startActivity(intent);
	}

	@OnClick(R.id.department_del_btn)
	public void del(View view) {
		showDialog("提示", "确定要解散群组吗？", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showProgressDialog("正在解散群组");
				if (groupInfo != null) {
					EntboostUM.delPersonGroup(groupInfo.getDep_code(),
							new DelGroupListener() {

								@Override
								public void onFailure(String errMsg) {
									removeProgressDialog();
									pageInfo.showError(errMsg);
								}

								@Override
								public void onDelGroupSuccess() {
									removeProgressDialog();
									finish();
								}
							});
				} else {
					removeProgressDialog();
					showToast("解散群组失败！");
				}
			}
		});

	}

}
