package com.entboost.im.contact;

import java.util.ArrayList;

import net.yunim.service.EntboostCache;
import net.yunim.service.entity.ContactInfo;
import net.yunim.service.entity.GroupInfo;
import net.yunim.service.entity.MemberInfo;
import net.yunim.service.entity.SearchContact;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.entboost.im.department.DepartmentInfoActivity;
import com.entboost.im.department.MemberInfoActivity;
import com.lidroid.xutils.ViewUtils;

public class SearchContactActivity extends EbActivity {

	private ListView mListView;
	private SearchContactAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_search_contact);
		ViewUtils.inject(this);
		mListView = (ListView) findViewById(R.id.mListView);
		adapter = new SearchContactAdapter(this, mInflater,
				new ArrayList<SearchContact>());
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取该行的数据
				SearchContact info = (SearchContact) adapter.getItem(position);
				if (info != null) {
					if (info.getType() == SearchContact.TYPE_GROUPCHAT) {
						GroupInfo departmentInfo = (GroupInfo) info.getObj();
						Intent intent = new Intent(SearchContactActivity.this,
								DepartmentInfoActivity.class);
						if (departmentInfo != null) {
							Bundle bundle = new Bundle();
							bundle.putSerializable("departmentInfo",
									departmentInfo);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					} else if (info.getType() == SearchContact.TYPE_CONTACTCHAT) {
						ContactInfo contactInfo = (ContactInfo) info.getObj();
						Intent intent = new Intent(SearchContactActivity.this,
								ContactInfoActivity.class);
						if (contactInfo != null) {
							Bundle bundle = new Bundle();
							bundle.putSerializable("contactInfo", contactInfo);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					} else if (info.getType() == SearchContact.TYPE_USERCHAT) {
						MemberInfo memberInfo = (MemberInfo) info.getObj();
						Intent intent = new Intent(SearchContactActivity.this,
								MemberInfoActivity.class);
						if (memberInfo != null) {
							Bundle bundle = new Bundle();
							bundle.putSerializable("memberInfo", memberInfo);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					}
				}
			}
		});
		EditText search = (EditText) findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				EntboostCache.searchContact(adapter.getList(), s.toString());
				adapter.notifyDataSetChanged();
			}
		});
	}

}
