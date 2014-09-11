package com.entboost.im.contact;

import net.yunim.service.EntboostUM;
import net.yunim.service.listener.EditContactListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ContactGroupActivity extends EbActivity {
	@ViewInject(R.id.contactgroup_group)
	private EditText contactgroup_group;
	private String contact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_contact_group);
		ViewUtils.inject(this);
		contact=getIntent().getStringExtra("contact");
	}

	@OnClick(R.id.contactgroup_save_btn)
	public void save(View view) {
		final String contactgroup_group_str=contactgroup_group.getText().toString();
		showProgressDialog("修改联系人分组");
		EntboostUM.editContact(contact,contactgroup_group_str, null, null, new  EditContactListener(){

			@Override
			public void onFailure(String errMsg) {
				pageInfo.showError(errMsg);
				removeProgressDialog();
			}

			@Override
			public void onEditContactSuccess() {
				removeProgressDialog();
				finish();
			}});
	}
	
	@OnClick(R.id.contactgroup_cancel_btn)
	public void cancel(View view){
		finish();
	}

}
