package com.entboost.im.contact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostUM;
import net.yunim.service.entity.ContactInfo;
import net.yunim.service.entity.DepartmentInfo;
import net.yunim.service.entity.EnterpriseInfo;
import net.yunim.service.entity.GroupInfo;
import net.yunim.service.listener.LoadEnterpriseListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.entboost.im.R;
import com.entboost.im.base.EbFragment;
import com.entboost.im.common.tree.Node;
import com.entboost.im.department.DepartmentAdapter;
import com.entboost.im.department.DepartmentInfoActivity;
import com.entboost.im.department.DepartmentTreeListAdapter;
import com.entboost.im.persongroup.PersonGroupAdapter;
import com.entboost.im.persongroup.PersonGroupInfoActivity;

public class FriendMainFragment extends EbFragment {

	private ContactAdapter friendAdapter;

	private ExpandableListView listView;

	private int selectPage = 0;
	private static int SELECTPAGE_CONTACT = 0;
	private static int SELECTPAGE_GROUP = 1;
	private static int SELECTPAGE_MYDEPARTMENT = 2;
	private static int SELECTPAGE_ENT = 3;

	private View layout_contact;
	private View layout_group;
	private View layout_department;
	private View layout_ent;

	private OnChildClickListener contactListener;

	private PersonGroupAdapter groupAdapter;

	private OnItemClickListener groupListener;

	private TextView text_listname;

	private DepartmentAdapter departmentAdapter;

	private OnItemClickListener departmentListener;

	private ListView grouplistView;

	private ListView departmentlistView;

	private ListView entlistView;

	private DepartmentTreeListAdapter entAdapter;

	public void selectPageBtn(View v) {
		initPageShow();
		v.setBackgroundResource(R.drawable.bottom_line_green);
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}



	@Override
	public void onResume() {
		super.onResume();
		refreshPage();
	}

	private void initPageShow() {
		layout_contact.setBackgroundResource(0);
		layout_group.setBackgroundResource(0);
		layout_department.setBackgroundResource(0);
		layout_ent.setBackgroundResource(0);
		listView.setVisibility(View.GONE);
		grouplistView.setVisibility(View.GONE);
		departmentlistView.setVisibility(View.GONE);
	}

	@Override
	public void refreshPage() {
		super.refreshPage();
		if (selectPage == SELECTPAGE_CONTACT) {
			notifyContactChanged();
		} else if (selectPage == SELECTPAGE_GROUP) {
			notifyGroupChanged();
		} else if (selectPage == SELECTPAGE_MYDEPARTMENT) {
			notifyDepartmentChanged();
		} else if (selectPage == SELECTPAGE_ENT) {
			notifyEntChanged();
		}
	}

	public void notifyEntChanged() {
		text_listname.setText("企业部门");
		entAdapter.setInput(getRootNode());
		entAdapter.notifyDataSetChanged();
		entlistView.setVisibility(View.VISIBLE);
	}

	public void notifyGroupChanged() {
		text_listname.setText("个人群组");
		groupAdapter.setInput(EntboostCache.getPersonGroups());
		groupAdapter.notifyDataSetChanged();
		grouplistView.setVisibility(View.VISIBLE);
	}

	public void notifyDepartmentChanged() {
		text_listname.setText("我的部门");
		departmentAdapter.setInput(EntboostCache.getMyDepartments());
		departmentAdapter.notifyDataSetChanged();
		departmentlistView.setVisibility(View.VISIBLE);
	}

	public void notifyContactChanged() {
		if(text_listname!=null){
			text_listname.setText("联系人分组");
			friendAdapter.initFriendList(EntboostCache.getContactInfos());
			friendAdapter.notifyDataSetChanged();
			listView.setVisibility(View.VISIBLE);
		}
	}

	private Node getRootNode() {
		List<DepartmentInfo> groups = EntboostCache.getDepartments();
		EnterpriseInfo ent = EntboostCache.getEnterpriseInfo();
		if (ent == null) {
			return null;
		}
		Node root = new Node(null, ent.getEnt_code() + "", ent.getEnt_name(),
				false, R.drawable.group_head, R.drawable.group_head);
		root.setExpanded(true);
		int allNum = 0;
		Map<Long, Node> temp = new HashMap<Long, Node>();
		for (int i = 0; i < groups.size(); i++) {
			DepartmentInfo group = groups.get(i);
			Node node = new Node(null, group.getDep_code() + "",
					group.getDep_name(), true, R.drawable.a4041,
					R.drawable.a4040);
			allNum = allNum + group.getEmp_count();
			node.setDesc(group.getEmp_count() + "");
			node.setValue(group);
			temp.put(group.getDep_code(), node);
		}
		root.setDesc(allNum + "");
		for (int i = 0; i < groups.size(); i++) {
			DepartmentInfo group = groups.get(i);
			Node node = temp.get(group.getDep_code());
			Node parent = null;
			if (group.getParent_code() == null || group.getParent_code() == 0) {
				parent = root;
			} else {
				parent = temp.get(group.getParent_code());
			}
			if (parent != null) {
				node.setParent(parent);
				parent.addChildNode(node);
				parent.setLeaf(false);
			}
		}
		return root;
	}

	private void initEnt(final View view) {
		entlistView = (ListView) view.findViewById(R.id.entlist);
		entlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				entAdapter.OnListItemClick(position);
			}
		});
		entAdapter = new DepartmentTreeListAdapter(view.getContext());
		entAdapter.setInput(getRootNode());
		entlistView.setAdapter(entAdapter);
	}

	private void initMyDepartment(final View view) {
		departmentlistView = (ListView) view.findViewById(R.id.departmentlist);
		departmentAdapter = new DepartmentAdapter(view.getContext());
		departmentListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = departmentAdapter.getItem(position);
				GroupInfo departmentInfo = (GroupInfo) obj;
				Intent intent = new Intent(view.getContext(),
						DepartmentInfoActivity.class);
				if (departmentInfo != null) {
					intent.putExtra("depid", departmentInfo.getDep_code());
					startActivity(intent);
				}
			}
		};
		departmentlistView.setAdapter(departmentAdapter);
		departmentlistView.setOnItemClickListener(departmentListener);
	}

	private void initGroup(final View view) {
		grouplistView = (ListView) view.findViewById(R.id.grouplist);
		groupAdapter = new PersonGroupAdapter(view.getContext());
		groupListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = groupAdapter.getItem(position);
				GroupInfo departmentInfo = (GroupInfo) obj;
				Intent intent = new Intent(view.getContext(),
						PersonGroupInfoActivity.class);
				if (departmentInfo != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("departmentInfo", departmentInfo);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		};
		grouplistView.setAdapter(groupAdapter);
		grouplistView.setOnItemClickListener(groupListener);
	}

	private void initContactView(View view) {
		listView = (ExpandableListView) view.findViewById(R.id.friendlist);
		friendAdapter = new ContactAdapter(view.getContext());
		contactListener = new ExpandableListView.OnChildClickListener() {

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
		};
		listView.setAdapter(friendAdapter);
		listView.setOnChildClickListener(contactListener);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = onCreateEbView(R.layout.fragment_friend_main,
				inflater, container);
		text_listname = (TextView) view.findViewById(R.id.text_listname);
		layout_contact = view.findViewById(R.id.layout_contact);
		layout_group = view.findViewById(R.id.layout_group);
		layout_department = view.findViewById(R.id.layout_department);
		layout_ent = view.findViewById(R.id.layout_ent);
		layout_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPage = SELECTPAGE_CONTACT;
				selectPageBtn(v);
				refreshPage();
			}
		});
		layout_group.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPage = SELECTPAGE_GROUP;
				selectPageBtn(v);
				refreshPage();
			}
		});
		layout_department.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPage = SELECTPAGE_MYDEPARTMENT;
				selectPageBtn(v);
				refreshPage();
				EntboostUM.loadEnterprise(new LoadEnterpriseListener() {

					@Override
					public void onFailure(String errMsg) {
						activity.pageInfo.showError("无法加载部门信息");
					}

					@Override
					public void onLoadEntDepartmentSuccess() {
						refreshPage();
					}

				});
			}
		});
		layout_ent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPage = SELECTPAGE_ENT;
				selectPageBtn(v);
				refreshPage();
			}
		});
		initContactView(view);
		initGroup(view);
		initMyDepartment(view);
		initEnt(view);
		selectPage = SELECTPAGE_MYDEPARTMENT;
		selectPageBtn(layout_department);
		friendAdapter.notifyDataSetChanged();
		return view;
	}
}
