package com.entboost.im.persongroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.yunim.service.entity.GroupInfo;
import net.yunim.service.entity.MemberInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entboost.im.R;

public class PersonGroupAdapter extends BaseExpandableListAdapter {
	private List<List<MemberInfo>> memberList = new ArrayList<List<MemberInfo>>();
	private Map<Long, List<MemberInfo>> mList = new HashMap<Long, List<MemberInfo>>();
	private List<GroupInfo> grouplist = new ArrayList<GroupInfo>();
	private Context mContext;

	public Context getmContext() {
		return mContext;
	}

	public void initFriendList(Map<Long, List<MemberInfo>> mList,
			List<GroupInfo> groups) {
		this.grouplist = groups;
		this.mList = mList;
		memberList.clear();
		for (GroupInfo group : grouplist) {
			memberList.add(mList.get(group.getDep_code()));
		}
	}

	public PersonGroupAdapter(Context context) {
		mContext = context;
	}

	/**
	 * View元素
	 */
	private class ItemViewHolder {
		ImageView userImg;
		TextView userName;
		TextView description;
	}

	private class GroupViewHolder {
		TextView itemsText;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if(memberList.get(groupPosition)==null){
			return null;
		}
		return memberList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ItemViewHolder holder2 = null;
		if (convertView == null
				|| convertView.getTag() instanceof GroupViewHolder) {
			// 使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_user, parent, false);
			holder2 = new ItemViewHolder();
			// 初始化布局中的元素
			holder2.userImg = ((ImageView) convertView
					.findViewById(R.id.user_head));
			holder2.userName = ((TextView) convertView
					.findViewById(R.id.user_name));
			holder2.description= ((TextView) convertView
					.findViewById(R.id.user_description));
			convertView.setTag(holder2);
		} else {
			holder2 = (ItemViewHolder) convertView.getTag();
		}
		MemberInfo mi = (MemberInfo) memberList.get(groupPosition).get(
				childPosition);
		holder2.userName.setText(mi.getUsername());
		holder2.description.setText(mi.getDescription());
		holder2.userImg.setImageResource(R.drawable.head1);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(memberList.get(groupPosition)==null){
			return 0;
		}
		return memberList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return grouplist.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return grouplist.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupInfo group = grouplist.get(groupPosition);
		GroupViewHolder holder1;
		if (convertView == null
				|| convertView.getTag() instanceof ItemViewHolder) {
			// 使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_contact_group, parent, false);
			// 减少findView的次数
			holder1 = new GroupViewHolder();
			// 初始化布局中的元素
			holder1.itemsText = ((TextView) convertView
					.findViewById(R.id.item_contact_group_name));
			convertView.setTag(holder1);
		} else {
			holder1 = (GroupViewHolder) convertView.getTag();
		}
		if (mList.get(group.getDep_code()) != null) {
			holder1.itemsText.setText(group.getDep_name() + "("
					+ mList.get(group.getDep_code()).size() + ")");
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
