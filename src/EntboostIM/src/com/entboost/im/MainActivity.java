package com.entboost.im;

import net.yunim.service.EntboostCache;
import net.yunim.service.EntboostLC;
import net.yunim.service.entity.ChatRoomRichMsg;
import net.yunim.service.entity.DynamicNews;
import net.yunim.utils.UIUtils;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.entboost.global.AbConstant;
import com.entboost.im.base.EbMainActivity;
import com.entboost.im.chat.ChatActivity;
import com.entboost.im.contact.AddContactActivity;
import com.entboost.im.contact.ContactListFragment;
import com.entboost.im.contact.SearchContactActivity;
import com.entboost.im.function.FunctionListFragment;
import com.entboost.im.global.MyApplication;
import com.entboost.im.message.MessageListFragment;
import com.entboost.im.setting.SettingFragment;
import com.entboost.im.user.LoginActivity;
import com.entboost.im.user.UserInfoActivity;
import com.entboost.ui.base.view.pupmenu.PopMenuItem;
import com.entboost.ui.base.view.pupmenu.PopMenuItemOnClickListener;

public class MainActivity extends EbMainActivity {
	private MessageListFragment messageListFragment;
	private ContactListFragment contactListFragment;
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
	public void onReceiveUserMessage(ChatRoomRichMsg msg) {
		super.onReceiveUserMessage(msg);
		if (MyApplication.getInstance().isShowNotificationMsg()) {
			DynamicNews newsInfo = EntboostCache.getHistoryMsg(msg.getSender());
			Intent intent = new Intent(this, ChatActivity.class);
			if (newsInfo.getType() == DynamicNews.TYPE_GROUPCHAT) {
				intent.putExtra(ChatActivity.INTENT_CHATTYPE,
						ChatActivity.CHATTYPE_GROUP);
			}
			intent.putExtra(ChatActivity.INTENT_TITLE, newsInfo.getTitle());
			intent.putExtra(ChatActivity.INTENT_UID, newsInfo.getSender());
			UIUtils.sendNotificationMsg(this, R.drawable.head,
					R.drawable.notify, newsInfo.getTitle(),
					newsInfo.getContent(), newsInfo.getNoReadNum(), intent);
		}
		mBottomTabView.getItem(0).showTip(
				EntboostCache.getUnreadNumDynamicNews());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		messageListFragment = new MessageListFragment();
		addView("聊天", messageListFragment,
				this.getResources().getDrawable(R.drawable.menu1_n), this
						.getResources().getDrawable(R.drawable.menu1_f));
		contactListFragment = new ContactListFragment();
		addView("联系人", contactListFragment,
				this.getResources().getDrawable(R.drawable.menu2_n), this
						.getResources().getDrawable(R.drawable.menu2_f));
		functionListFragment = new FunctionListFragment();
		addView("应用", functionListFragment,
				this.getResources().getDrawable(R.drawable.menu3_n), this
						.getResources().getDrawable(R.drawable.menu3_f));
		settingFragment = new SettingFragment();
		addView("设置", settingFragment,
				this.getResources().getDrawable(R.drawable.menu4_n), this
						.getResources().getDrawable(R.drawable.menu4_f));
		mBottomTabView.initItemsTip(R.drawable.tab_red_circle);
		initMenu();
	}

	public void initMenu() {
		this.getTitleBar().addRightImageButton(
				R.drawable.actionbar_search_icon,
				new PopMenuItem(new PopMenuItemOnClickListener() {

					@Override
					public void onItemClick() {
						Intent intent = new Intent(MainActivity.this,
								SearchContactActivity.class);
						startActivity(intent);
					}

				}));
		this.getTitleBar().addRightImageButton(R.drawable.actionbar_add_icon,
				new PopMenuItem("发起群聊", new PopMenuItemOnClickListener() {

					@Override
					public void onItemClick() {
						UIUtils.showToast(MainActivity.this, "发起群聊正在建设中...");
					}

				}), new PopMenuItem("添加联系人", new PopMenuItemOnClickListener() {

					@Override
					public void onItemClick() {
						Intent intent = new Intent(MainActivity.this,
								AddContactActivity.class);
						startActivity(intent);
					}

				}));
		this.getTitleBar().addRightImageButton(
				R.drawable.actionbar_more_icon,
				new PopMenuItem(EntboostCache.getUser().getUsername(), this
						.getResources().getDrawable(R.drawable.head1),
						new PopMenuItemOnClickListener() {

							@Override
							public void onItemClick() {
								Intent intent = new Intent(MainActivity.this,
										UserInfoActivity.class);
								startActivity(intent);
							}

						}),
				new PopMenuItem("清空会话", this.getResources().getDrawable(
						android.R.drawable.ic_menu_delete),
						new PopMenuItemOnClickListener() {

							@Override
							public void onItemClick() {
								showDialog("提示", "确认要清空所有的会话吗？",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												EntboostCache
														.clearAllMsgHistory();
												messageListFragment
														.getDynamicNewsAdapter()
														.setList(
																EntboostCache
																		.getHistoryMsgList());
												messageListFragment
														.getDynamicNewsAdapter()
														.notifyDataSetChanged();
											}

										});
							}

						}),
				new PopMenuItem("退出登录", this.getResources().getDrawable(
						android.R.drawable.ic_menu_delete),
						new PopMenuItemOnClickListener() {

							@Override
							public void onItemClick() {
								View view = mInflater.inflate(
										R.layout.dialog_exit, null);
								showDialog(AbConstant.DIALOGBOTTOM, view);
								view.findViewById(R.id.logoutBtn)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														EntboostLC.logout();
														getBottomDialog()
																.cancel();
														finish();
														Intent intent = new Intent(
																MainActivity.this,
																LoginActivity.class);
														startActivity(intent);
													}
												});
								view.findViewById(R.id.exitBtn)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														EntboostLC.exit();
														getBottomDialog()
																.cancel();
														finish();
														android.os.Process.killProcess(android.os.Process.myPid());
													}
												});
								view.findViewById(R.id.cancelBtn)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														getBottomDialog()
																.cancel();
													}
												});
							}

						}));
	}

}
