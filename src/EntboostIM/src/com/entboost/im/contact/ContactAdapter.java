package com.entboost.im.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.yunim.service.entity.ContactInfo;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entboost.im.R;

public class ContactAdapter extends BaseExpandableListAdapter {
	private List<List<ContactInfo>> contactList = new ArrayList<List<ContactInfo>>();
	private Map<String, List<ContactInfo>> mList = new HashMap<String, List<ContactInfo>>();
	private List<String> grouplist = new ArrayList<String>();
	private Context mContext;

	public Context getmContext() {
		return mContext;
	}

	public void initFriendList(List<ContactInfo> contactInfos) {
		grouplist.clear();
		mList.clear();
		contactList.clear();
		Collections.sort(contactInfos);
		for (ContactInfo gi : contactInfos) {
			if (StringUtils.isBlank(gi.getGroupname())) {
				gi.setGroupname("未分组联系人");
			}
			if (!grouplist.contains(gi.getGroupname())) {
				grouplist.add(gi.getGroupname());
			}
			if (!mList.containsKey(gi.getGroupname())) {
				mList.put(gi.getGroupname(), new ArrayList<ContactInfo>());
			}
			mList.get(gi.getGroupname()).add(gi);
		}
		Collections.sort(grouplist);
		for (String groupname : grouplist) {
			contactList.add(mList.get(groupname));
		}
	}

	public ContactAdapter(Context context) {
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
		ImageView itemsHead;
		TextView itemsText;
		TextView itemsDesc;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return contactList.get(groupPosition).get(childPosition);
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
			holder2.description = ((TextView) convertView
					.findViewById(R.id.user_description));
			convertView.setTag(holder2);
		} else {
			holder2 = (ItemViewHolder) convertView.getTag();
		}
		ContactInfo mi = (ContactInfo) contactList.get(groupPosition).get(
				childPosition);
		String name = null;
		if (StringUtils.isNotBlank(mi.getName())) {
			name = mi.getName();
		} else {
			name = mi.getContact();
		}
		holder2.userName.setText(name);
		String type = "";
		if (mi.getContact_uid() == null) {
			type = "[非系统用户]";
		} else {
			type = "[系统用户]";
		}
		holder2.description.setText(type + mi.getDescription());
		holder2.userImg.setImageResource(R.drawable.head1);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition >= contactList.size()) {
			return 0;
		}
		return contactList.get(groupPosition).size();
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
		String groupname = grouplist.get(groupPosition);
		GroupViewHolder holder1;
		if (convertView == null
				|| convertView.getTag() instanceof ItemViewHolder) {
			// 使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_contact_group, parent, false);
			// 减少findView的次数
			holder1 = new GroupViewHolder();
			// 初始化布局中的元素
			holder1.itemsHead = ((ImageView) convertView
					.findViewById(R.id.item_contact_group_head));
			holder1.itemsText = ((TextView) convertView
					.findViewById(R.id.item_contact_group_name));
			holder1.itemsDesc = ((TextView) convertView
					.findViewById(R.id.item_contact_group_type));
			convertView.setTag(holder1);
		} else {
			holder1 = (GroupViewHolder) convertView.getTag();
		}
		if (isExpanded) {
			holder1.itemsHead.setImageResource(R.drawable.a4041);
		} else {
			holder1.itemsHead.setImageResource(R.drawable.a4040);
		}
		if (mList.get(groupname) != null) {
			holder1.itemsText.setText(groupname);
			holder1.itemsDesc.setText(mList.get(groupname).size() + "");
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
