package com.entboost.im.group;

import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostUM;
import net.yunim.service.entity.GroupInfo;
import net.yunim.service.listener.EditGroupListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.entboost.handler.HandlerToolKit;
import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class EditGroupEmailActivity extends EbActivity {

	@ViewInject(R.id.group_email)
	private EditText group_email;
	private Long depid;
	private GroupInfo groupInfo;

	@OnClick(R.id.group_save_btn)
	public void save(View view) {
		final String sGroup_email = group_email.getText().toString();
		showProgressDialog("修改邮箱");
		EntboostUM.editGroup(depid, null, null, null, null, sGroup_email, null, null, null, groupInfo.getType(), new EditGroupListener() {

			@Override
			public void onFailure(int code, final String errMsg) {
				HandlerToolKit.runOnMainThreadAsync(new Runnable() {
					@Override
					public void run() {
						showToast(errMsg);
						removeProgressDialog();
					}
				});
			}

			@Override
			public void onEditGroupSuccess(Long dep_code) {
				HandlerToolKit.runOnMainThreadAsync(new Runnable() {
					@Override
					public void run() {
						removeProgressDialog();
						finish();
					}
				});
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_edit_group_email);
		ViewUtils.inject(this);
		depid = getIntent().getLongExtra("depid", -1);
		groupInfo = EntboostCache.getGroup(depid);
		group_email.setText(groupInfo.getEmail());
	}

	@OnClick(R.id.group_cancel_btn)
	public void cancel(View view) {
		finish();
	}

}
