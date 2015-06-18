package com.entboost.im.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.yunim.service.constants.EB_GROUP_TYPE;
import net.yunim.service.entity.DepartmentInfo;
import net.yunim.service.entity.GroupInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entboost.im.R;

public class DepartmentAdapter extends BaseAdapter {
	private Context mContext;
	private List<DepartmentInfo> groups=new ArrayList<DepartmentInfo>();

	public Context getmContext() {
		return mContext;
	}

	public DepartmentAdapter(Context context) {
		mContext = context;
	}

	public void setInput(List<DepartmentInfo> groups) {
		Collections.sort(groups);
		this.groups = groups;
	}

	/**
	 * View元素
	 */
	private class GroupViewHolder {
		ImageView userImg;
		TextView itemsText;
		TextView description;
	}

	@Override
	public int getCount() {
		return groups.size();
	}

	@Override
	public Object getItem(int position) {
		return groups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GroupInfo group = (GroupInfo) getItem(position);
		GroupViewHolder holder1;
		if (convertView == null) {
			// 使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_user, parent, false);
			// 减少findView的次数
			holder1 = new GroupViewHolder();
			// 初始化布局中的元素
			holder1.userImg = ((ImageView) convertView
					.findViewById(R.id.user_head));
			holder1.itemsText = ((TextView) convertView
					.findViewById(R.id.user_name));
			holder1.description = ((TextView) convertView
					.findViewById(R.id.user_description));
			convertView.setTag(holder1);
		} else {
			holder1 = (GroupViewHolder) convertView.getTag();
		}
		holder1.userImg.setImageResource(R.drawable.group_head);
		holder1.itemsText.setText(group.getDep_name()+"("+group.getEmp_count()+")");
		holder1.description.setText(mContext.getResources().getStringArray(
				R.array.group_type)[EB_GROUP_TYPE.getIndex(group.getType())]);
		return convertView;
	}

}
