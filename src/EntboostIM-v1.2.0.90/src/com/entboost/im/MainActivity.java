package com.entboost.im;

import net.yunim.service.EntboostCache;
import net.yunim.service.entity.DynamicNews;
import net.yunim.service.entity.FuncInfo;
import net.yunim.service.entity.SearchResultInfo;
import net.yunim.utils.UIUtils;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.entboost.im.base.EbMainActivity;
import com.entboost.im.chat.CallListActivity;
import com.entboost.im.chat.ChatActivity;
import com.entboost.im.contact.AddContactActivity;
import com.entboost.im.contact.FriendMainFragment;
import com.entboost.im.contact.SearchContactActivity;
import com.entboost.im.function.FunctionListFragment;
import com.entboost.im.function.FunctionMainActivity;
import com.entboost.im.global.MyApplication;
import com.entboost.im.message.BroadcastMessageListActivity;
import com.entboost.im.message.MessageListFragment;
import com.entboost.im.persongroup.PersonGroupEditActivity;
import com.entboost.im.setting.SettingFragment;
import com.entboost.ui.base.view.pupmenu.PopMenuConfig;
import com.entboost.ui.base.view.pupmenu.PopMenuItem;
import com.entboost.ui.base.view.pupmenu.PopMenuItemOnClickListener;

public class MainActivity extends EbMainActivity {
	private MessageListFragment messageListFragment;
	private FriendMainFragment friendMainFragment;
	private FunctionListFragment functionListFragment;
	private SettingFragment settingFragment;

	@Override
	public void onBackPressed() {
		// 实现Home键效果
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
	}

	@Override
	public void onReceiveDynamicNews(DynamicNews news) {
		super.onReceiveDynamicNews(news);
		// 接收到消息后，如果程序位于后台运行，需要发送系统通知
		if (MyApplication.getInstance().isShowNotificationMsg()) {
			Intent intent = null;
			if (news.getType() == DynamicNews.TYPE_GROUPCHAT
					|| news.getType() == DynamicNews.TYPE_USERCHAT) {
				intent = new Intent(this, ChatActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setData(Uri.parse("custom://"
						+ System.currentTimeMillis()));
				if (news.getType() == DynamicNews.TYPE_GROUPCHAT) {
					intent.putExtra(ChatActivity.INTENT_CHATTYPE,
							ChatActivity.CHATTYPE_GROUP);
				}
				intent.putExtra(ChatActivity.INTENT_TITLE, news.getTitle());
				intent.putExtra(ChatActivity.INTENT_UID, news.getSender());
			} else if (news.getType() == DynamicNews.TYPE_CALL) {
				intent = new Intent(this, CallListActivity.class);
			} else if (news.getType() == DynamicNews.TYPE_MYMESSAGE) {
				FuncInfo funcInfo = EntboostCache.getMyMessageFuncInfo();
				if (funcInfo != null) {
					intent = new Intent(this, FunctionMainActivity.class);
					intent.putExtra("funcInfo", funcInfo);
				}
			} else if (news.getType() == DynamicNews.TYPE_BMESSAGE) {
				intent = new Intent(this, BroadcastMessageListActivity.class);
			}
			if (intent != null) {
				UIUtils.sendNotificationMsg(this, R.drawable.notify,
						news.getTitle(), news.getContentText(),
						EntboostCache.getUnreadNumDynamicNews(), intent);
			}
		}
		// 更新主菜单的未读数量标记
		mBottomTabView.getItem(0).showTip(
				EntboostCache.getUnreadNumDynamicNews());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		messageListFragment = new MessageListFragment();
		addView("聊天", messageListFragment,
				this.getResources().getDrawable(R.drawable.menu1), this
						.getResources().getDrawable(R.drawable.menu1_n));
		friendMainFragment = new FriendMainFragment();
		addView("联系人", friendMainFragment,
				this.getResources().getDrawable(R.drawable.menu2), this
						.getResources().getDrawable(R.drawable.menu2_n));
		functionListFragment = new FunctionListFragment();
		addView("应用", functionListFragment,
				this.getResources().getDrawable(R.drawable.menu3), this
						.getResources().getDrawable(R.drawable.menu3_n));
		settingFragment = new SettingFragment();
		addView("设置", settingFragment,
				this.getResources().getDrawable(R.drawable.menu4), this
						.getResources().getDrawable(R.drawable.menu4_n));
		mBottomTabView.initItemsTip(R.drawable.tab_red_circle);
		initMenu();
	}

	public void initMenu() {
		PopMenuConfig config = new PopMenuConfig();
		config.setBackground_resId(R.drawable.popmenu);
		config.setTextColor(Color.WHITE);
		config.setShowAsDropDownYoff(28);
		this.getTitleBar().addRightImageButton(R.drawable.main_search, config,
				new PopMenuItem("查找联系人", new PopMenuItemOnClickListener() {

					@Override
					public void onItemClick() {
						Intent intent = new Intent(MainActivity.this,
								SearchContactActivity.class);
						intent.putExtra("type",
								SearchResultInfo.TYPE_CONTACTCHAT);
						startActivity(intent);
					}

				}), new PopMenuItem("查找群组", new PopMenuItemOnClickListener() {

					@Override
					public void onItemClick() {
						Intent intent = new Intent(MainActivity.this,
								SearchContactActivity.class);
						intent.putExtra("type", SearchResultInfo.TYPE_GROUPCHAT);
						startActivity(intent);
					}

				}), new PopMenuItem("查找群组成员", new PopMenuItemOnClickListener() {

					@Override
					public void onItemClick() {
						Intent intent = new Intent(MainActivity.this,
								SearchContactActivity.class);
						intent.putExtra("type", SearchResultInfo.TYPE_USERCHAT);
						startActivity(intent);
					}

				}));
		this.getTitleBar().addRightImageButton(
				R.drawable.main_add,
				config,
				new PopMenuItem("添加联系人", R.drawable.menu_add_contact,
						new PopMenuItemOnClickListener() {

							@Override
							public void onItemClick() {
								Intent intent = new Intent(MainActivity.this,
										AddContactActivity.class);
								startActivity(intent);
							}

						}),
				new PopMenuItem("创建个人群组", R.drawable.menu_add_tempgroup,
						new PopMenuItemOnClickListener() {

							@Override
							public void onItemClick() {
								Intent intent = new Intent(MainActivity.this,
										PersonGroupEditActivity.class);
								startActivity(intent);
							}

						}));
	}

}
