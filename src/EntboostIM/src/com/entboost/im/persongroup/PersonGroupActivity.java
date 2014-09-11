package com.entboost.im.persongroup;

import net.yunim.service.EntboostCache;
import net.yunim.service.entity.GroupInfo;
import net.yunim.service.entity.MemberInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PersonGroupActivity extends EbActivity {
	@ViewInject(R.id.departmentlist)
	private ExpandableListView listView;
	private PersonGroupAdapter friendAdapter;

	@Override
	public void onLoadAllMemberInfo() {
		friendAdapter.initFriendList(
				EntboostCache.getGroupMemberInfos(),
				EntboostCache.getPersonGroups());
		friendAdapter.notifyDataSetChanged();
		int groupCount = listView.getCount();
		for (int i = 0; i < groupCount; i++) {
			listView.expandGroup(i);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_department);
		ViewUtils.inject(this);
		listView.setGroupIndicator(null);// 将控件默认的左边箭头去掉
		friendAdapter = new PersonGroupAdapter(this);
		friendAdapter.initFriendList(
				EntboostCache.getGroupMemberInfos(),
				EntboostCache.getPersonGroups());
		listView.setAdapter(friendAdapter);
		int groupCount = listView.getCount();
		for (int i = 0; i < groupCount; i++) {
			listView.expandGroup(i);
		}
		listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				Object obj = friendAdapter.getGroup(groupPosition);
				if (obj instanceof GroupInfo) {
					GroupInfo departmentInfo = (GroupInfo) obj;
					Intent intent = new Intent(PersonGroupActivity.this,
							PersonGroupInfoActivity.class);
					if (departmentInfo != null) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("departmentInfo", departmentInfo);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
				return true;
			}
		});
		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Object obj = friendAdapter.getChild(groupPosition,
						childPosition);
				if (obj instanceof MemberInfo) {
					MemberInfo memberInfo = (MemberInfo) obj;
					Intent intent = new Intent(PersonGroupActivity.this,
							PersonInfoActivity.class);
					if (memberInfo != null) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("memberInfo", memberInfo);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
				return true;
			}
		});
	}

}
