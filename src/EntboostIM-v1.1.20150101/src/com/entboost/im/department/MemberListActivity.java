package com.entboost.im.department;

import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostUM;
import net.yunim.service.entity.GroupInfo;
import net.yunim.service.entity.MemberInfo;
import net.yunim.service.listener.LoadMemberListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;

public class MemberListActivity extends EbActivity {

	private GroupInfo group;
	private ListView memberlistView;
	private MemberAdapter memberAdapter;
	private OnItemClickListener memberListener;

	@Override
	protected void onResume() {
		super.onResume();
		memberAdapter.setInput(EntboostCache.getGroupMemberInfos().get(
				group.getDep_code()));
		memberAdapter.notifyDataSetChanged();
	}
	
	

	@Override
	public void onUserStateChange(Long uid) {
		memberAdapter.setInput(EntboostCache.getGroupMemberInfos().get(
				group.getDep_code()));
		memberAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_member_list);
		ViewUtils.inject(this);
		memberlistView = (ListView) findViewById(R.id.memberlist);
		memberAdapter = new MemberAdapter(this);
		memberListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = memberAdapter.getItem(position);
				MemberInfo memberInfo = (MemberInfo) obj;
				Intent intent = new Intent(view.getContext(),
						MemberInfoActivity.class);
				if (memberInfo != null) {
					intent.putExtra("memberCode", memberInfo.getEmp_code());
					if (memberInfo.getEmp_uid() - EntboostCache.getUid() == 0) {
						intent.putExtra("selfFlag", true);
					}
					startActivity(intent);
				}
			}
		};
		group = (GroupInfo) getIntent().getSerializableExtra("group");
		memberAdapter.setInput(EntboostCache.getGroupMemberInfos().get(
				group.getDep_code()));
		memberlistView.setAdapter(memberAdapter);
		memberlistView.setOnItemClickListener(memberListener);
		memberlistView.setOnScrollListener(new PauseOnScrollListener(
				memberAdapter.getBitmapUtils(), false, true));
		showProgressDialog("加载群组成员信息");
		EntboostUM.loadMember(group.getDep_code(), new LoadMemberListener() {

			@Override
			public void onFailure(String errMsg) {
				removeProgressDialog();
				pageInfo.showError("无法加载群组成员信息");
			}

			@Override
			public void onLoadMemberSuccess() {
				removeProgressDialog();
				pageInfo.hide();
				memberAdapter.setInput(EntboostCache.getGroupMemberInfos().get(
						group.getDep_code()));
				memberAdapter.notifyDataSetChanged();
			}
		});

	}

}
