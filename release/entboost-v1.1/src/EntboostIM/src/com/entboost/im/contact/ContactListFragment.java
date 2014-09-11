package com.entboost.im.contact;

import net.yunim.service.EntboostCache;
import net.yunim.service.entity.ContactInfo;
import net.yunim.utils.UIUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.entboost.im.MainActivity;
import com.entboost.im.R;
import com.entboost.im.base.EbFragment;
import com.entboost.im.department.DepartmentActivity;
import com.entboost.im.persongroup.PersonGroupActivity;

public class ContactListFragment extends EbFragment {
	private ContactAdapter friendAdapter;

	private ExpandableListView listView;

	private View addContact;

	@Override
	public void refreshPage() {
		super.refreshPage();
	}

	@Override
	public void onResume() {
		super.onResume();
		onLoadAllContactInfo();
	}

	@Override
	public void onLoadAllContactInfo() {
		friendAdapter.initFriendList(EntboostCache.getContactInfos());
		friendAdapter.notifyDataSetChanged();
		if (addContact != null) {
			if (EntboostCache.getContactInfos().size() == 0) {
				addContact.setVisibility(View.VISIBLE);
			} else {
				addContact.setVisibility(View.GONE);
			}
		}
		int groupCount = listView.getCount();
		for (int i = 0; i < groupCount; i++) {
			listView.expandGroup(i);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = onCreateEbView(R.layout.fragment_contact_list, inflater,
				container);
		listView = (ExpandableListView) view.findViewById(R.id.friendlist);
		listView.setGroupIndicator(null);// 将控件默认的左边箭头去掉
		friendAdapter = new ContactAdapter(view.getContext());
		friendAdapter.initFriendList(EntboostCache.getContactInfos());
		listView.setAdapter(friendAdapter);
		int groupCount = listView.getCount();
		for (int i = 0; i < groupCount; i++) {
			listView.expandGroup(i);
		}
		addContact = view.findViewById(R.id.item_add_contact_layout);
		if (EntboostCache.getContactInfos().size() == 0) {
			addContact.setVisibility(View.VISIBLE);
		}
		addContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, AddContactActivity.class);
				startActivity(intent);
			}
		});
		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Object obj = friendAdapter.getChild(groupPosition,
						childPosition);
				if (obj instanceof ContactInfo) {
					ContactInfo contactInfo = (ContactInfo) obj;
					Intent intent = new Intent(activity,
							ContactInfoActivity.class);
					if (contactInfo != null) {
						intent.putExtra("contact", contactInfo.getContact());
						startActivity(intent);
					}
				}
				return true;
			}
		});
		view.findViewById(R.id.contactlist_ent_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity,
								DepartmentActivity.class);
						startActivity(intent);
					}
				});
		view.findViewById(R.id.contactlist_persongroup_layout)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity,
								PersonGroupActivity.class);
						startActivity(intent);
					}
				});
		return view;
	}

}
