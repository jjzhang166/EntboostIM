package com.entboost.im.message;

import net.yunim.service.EntboostCache;
import net.yunim.service.entity.CardInfo;
import net.yunim.service.entity.ChatRoomRichMsg;
import net.yunim.service.entity.DynamicNews;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.entboost.im.R;
import com.entboost.im.base.EbFragment;
import com.entboost.im.base.EbMainActivity;
import com.entboost.im.chat.CallListActivity;
import com.entboost.im.chat.ChatActivity;

public class MessageListFragment extends EbFragment {

	private ListView mAbPullListView;
	private MessageAdapter dynamicNewsAdapter;

	public MessageAdapter getDynamicNewsAdapter() {
		return dynamicNewsAdapter;
	}

	@Override
	public void onResume() {
		super.onResume();
		dynamicNewsAdapter.setList(EntboostCache.getHistoryMsgList());
		dynamicNewsAdapter.notifyDataSetChanged();
		int noReadNums = EntboostCache.getUnreadNumDynamicNews();
		if (noReadNums > 0) {
			((EbMainActivity) activity).mBottomTabView.getItem(0).showTip(
					noReadNums);
		} else {
			((EbMainActivity) activity).mBottomTabView.getItem(0).hideTip();
		}
	}
	
	@Override
	public void onCallIncoming(CardInfo arg0) {
		super.onCallIncoming(arg0);
		dynamicNewsAdapter.setList(EntboostCache.getHistoryMsgList());
		dynamicNewsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onReceiveUserMessage(ChatRoomRichMsg msg) {
		super.onReceiveUserMessage(msg);
		dynamicNewsAdapter.setList(EntboostCache.getHistoryMsgList());
		dynamicNewsAdapter.notifyDataSetChanged();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = onCreateEbView(R.layout.fragment_message_list, inflater,
				container);
		mAbPullListView = (ListView) view.findViewById(R.id.mListView);
		dynamicNewsAdapter = new MessageAdapter(view.getContext(), inflater,
				EntboostCache.getHistoryMsgList());
		mAbPullListView.setAdapter(dynamicNewsAdapter);
		// item被点击事件
		mAbPullListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取该行的数据
				DynamicNews newsInfo = (DynamicNews) dynamicNewsAdapter
						.getItem(position);
				if (newsInfo != null) {
					newsInfo.readAll();
					int noReadNums = EntboostCache.getUnreadNumDynamicNews();
					if (noReadNums > 0) {
						((EbMainActivity) activity).mBottomTabView.getItem(0)
								.showTip(noReadNums);
					} else {
						((EbMainActivity) activity).mBottomTabView.getItem(0)
								.hideTip();
					}
					if (newsInfo.getType() == DynamicNews.TYPE_GROUPCHAT
							|| newsInfo.getType() == DynamicNews.TYPE_USERCHAT) {
						Intent intent = new Intent(MessageListFragment.this
								.getActivity(), ChatActivity.class);
						if (newsInfo.getType() == DynamicNews.TYPE_GROUPCHAT) {
							intent.putExtra(ChatActivity.INTENT_CHATTYPE,
									ChatActivity.CHATTYPE_GROUP);
						}
						intent.putExtra(ChatActivity.INTENT_TITLE,
								newsInfo.getTitle());
						intent.putExtra(ChatActivity.INTENT_UID,
								newsInfo.getSender());
						startActivity(intent);
					}else if(newsInfo.getType() == DynamicNews.TYPE_CALL){
						Intent intent = new Intent(MessageListFragment.this
								.getActivity(), CallListActivity.class);
						startActivity(intent);
					}
					
				}

			}
		});
		return view;
	}
}
