package com.entboost.im.department;

import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostUM;
import net.yunim.service.entity.MemberInfo;
import net.yunim.service.listener.EditInfoListener;
import net.yunim.utils.ResourceUtils;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.entboost.im.chat.ChatActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MemberInfoActivity extends EbActivity {

	private MemberInfo memberInfo;
	@ViewInject(R.id.member_username)
	private TextView member_username;
	@ViewInject(R.id.member_name)
	private TextView member_name;
	@ViewInject(R.id.member_account)
	private TextView member_account;
	@ViewInject(R.id.member_description)
	private TextView member_description;
	@ViewInject(R.id.member_head)
	private ImageView member_head;
	@ViewInject(R.id.member_send_btn)
	private Button member_send_btn;
	@ViewInject(R.id.member_set_default)
	private Button member_set_default;
	private long memberCode;

	private boolean selfFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_member_info);
		ViewUtils.inject(this);
		memberCode = getIntent().getLongExtra("memberCode", -1);
		selfFlag = getIntent().getBooleanExtra("selfFlag", false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		memberInfo = EntboostCache.getMemberByCode(memberCode);
		if (memberInfo == null) {
			memberInfo = (MemberInfo) getIntent().getSerializableExtra(
					"memberInfo");
		}
		if (memberInfo != null) {
			member_account.setText(memberInfo.getEmp_account());
			member_username.setText(memberInfo.getUsername());
			member_name.setText(memberInfo.getUsername());
			member_description.setText(memberInfo.getDescription());
			Bitmap img = ResourceUtils.getHeadBitmap(memberInfo.getH_r_id());
			if (img != null) {
				member_head.setImageBitmap(img);
			} else {
				member_head.setImageResource(R.drawable.head1);
			}
			if (selfFlag) {
				member_send_btn.setVisibility(View.GONE);
				member_set_default.setVisibility(View.VISIBLE);
			} else {
				member_send_btn.setVisibility(View.VISIBLE);
				member_set_default.setVisibility(View.GONE);
			}
		}
	}

	@OnClick(R.id.member_head)
	public void openInfoHeader(View view) {
		if (selfFlag) {
			Intent intent = new Intent(this, SelectHeadImgActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("memberCode", memberCode);
			this.startActivity(intent);
		}
	}

	@OnClick(R.id.member_send_btn)
	public void sendMsg(View view) {
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra(ChatActivity.INTENT_TITLE, memberInfo.getUsername());
		intent.putExtra(ChatActivity.INTENT_UID, memberInfo.getEmp_uid());
		this.startActivity(intent);
	}

	@OnClick(R.id.member_set_default)
	public void sendDefault(View view) {
		showProgressDialog("设置默认名片");
		EntboostUM.setMyDefaultMemberCode(memberCode, new EditInfoListener() {

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

}
