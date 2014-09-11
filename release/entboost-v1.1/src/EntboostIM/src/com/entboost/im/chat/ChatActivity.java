package com.entboost.im.chat;

import net.yunim.service.EntboostCM;
import net.yunim.service.EntboostCache;
import net.yunim.service.entity.ChatRoomRichMsg;
import net.yunim.service.entity.Resource;
import net.yunim.utils.UIUtils;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.entboost.im.R;
import com.entboost.im.base.EbActivity;
import com.entboost.ui.base.view.titlebar.AbTitleBar;
import com.entboost.utils.NetworkUtils;
import com.entboost.voice.ExtAudioRecorder;

public class ChatActivity extends EbActivity {

	public static final String INTENT_TITLE = "intent_title";
	public static final String INTENT_UID = "intent_uid";
	public static final String INTENT_CHATTYPE = "intent_chattype";

	public static final int CHATTYPE_PERSON = 0;
	public static final int CHATTYPE_GROUP = 1;

	private int chattype = CHATTYPE_PERSON;
	private Long uid;
	private EditText mContentEdit;
	private ChatMsgViewAdapter mChatMsgViewAdapter;
	private ListView mMsgListView;
	private LinearLayout emotionsAppPanel;
	private GridView expressionGriView;
	private EmotionsImageAdapter emotionsImageAdapter;
	private ImageButton picBtn;
	private Button sendBtn;
	private ImageView voiceImg;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onReceiveUserMessage(ChatRoomRichMsg msg) {
		// 启动会话界面，接收消息自动设置已读
		if (msg.getChatType() == ChatRoomRichMsg.CHATTYPE_GROUP) {
			EntboostCache.readMsg(msg.getDepCode());
		} else {
			EntboostCache.readMsg(msg.getSender());
		}
		refreshPage();
	}

	private void refreshPage() {
		if (uid != null) {
			mChatMsgViewAdapter.initChat(EntboostCache.getChatMsgs(uid));
		}
		mChatMsgViewAdapter.notifyDataSetChanged();
		mMsgListView.setSelection(mMsgListView.getBottom());
	}

	@Override
	public void onSendStatusChanged(ChatRoomRichMsg msg) {
		refreshPage();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_chat);
		AbTitleBar titleBar = this.getTitleBar();
		titleBar.setBackgroundResource(R.drawable.title_bar);
		titleBar.setTitleText(this.getIntent().getStringExtra(INTENT_TITLE));
		titleBar.setLogo(R.drawable.entlogo);
		uid = this.getIntent().getLongExtra(INTENT_UID, -1);
		if (uid != null) {
			EntboostCache.readMsg(uid);
		}
		chattype = this.getIntent().getIntExtra(INTENT_CHATTYPE,
				CHATTYPE_PERSON);
		mContentEdit = (EditText) findViewById(R.id.content);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		voiceImg = (ImageView) this.findViewById(R.id.imageViewvoice);
		final Button voiceSendBtn = (Button) this
				.findViewById(R.id.voicesendBtn);
		final ImageButton voiceBtn = (ImageButton) this
				.findViewById(R.id.voiceBtn);
		final ImageButton keyBtn = (ImageButton) this.findViewById(R.id.keyBtn);
		voiceSendBtn.setOnTouchListener(new View.OnTouchListener() {

			private ExtAudioRecorder recorder;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					if (!NetworkUtils.isNetworkConnected(ChatActivity.this)) {
						pageInfo.showError(ChatActivity.this
								.getString(R.string.msg_error_localNoNetwork));
						return false;
					}
					// 按住事件发生后执行代码的区域
					voiceImg.setVisibility(View.VISIBLE);
					recorder = EntboostCM.startRecording();
					voiceSendBtn.setText("松开发送");
					break;
				}
				case MotionEvent.ACTION_MOVE: {
					// 移动事件发生后执行代码的区域
					break;
				}
				case MotionEvent.ACTION_UP: {
					// 松开事件发生后执行代码的区域
					voiceSendBtn.setText("按住说话");
					voiceImg.setVisibility(View.GONE);
					if (recorder != null) {
						recorder.stopRecord();
						if (uid < 0) {
							pageInfo.showError(ChatActivity.this
									.getString(R.string.msg_send_uiderror));
							return false;
						}
						if (uid >= 0) {
							if (chattype == CHATTYPE_PERSON) {
								EntboostCM.sendVoice(uid,
										recorder.getFilePath());
							} else {
								EntboostCM.sendGroupVoice(uid,
										recorder.getFilePath());
							}

						}
						mChatMsgViewAdapter.notifyDataSetChanged();
					}
					break;
				}
				default:
					break;
				}
				return false;
			}
		});
		voiceBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				View btnLayout = findViewById(R.id.voiceBtnLayout);
				btnLayout.setVisibility(View.VISIBLE);
				View contentLayout = findViewById(R.id.contentLayout);
				contentLayout.setVisibility(View.GONE);
				voiceBtn.setVisibility(View.GONE);
				keyBtn.setVisibility(View.VISIBLE);
			}
		});
		keyBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				View btnLayout = findViewById(R.id.voiceBtnLayout);
				btnLayout.setVisibility(View.GONE);
				View contentLayout = findViewById(R.id.contentLayout);
				contentLayout.setVisibility(View.VISIBLE);
				voiceBtn.setVisibility(View.VISIBLE);
				keyBtn.setVisibility(View.GONE);
			}
		});
		mContentEdit.addTextChangedListener(new TextWatcher() {

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
				if (s.length() > 0) {
					sendBtn.setVisibility(View.VISIBLE);
					picBtn.setVisibility(View.GONE);
				} else {
					sendBtn.setVisibility(View.GONE);
					picBtn.setVisibility(View.VISIBLE);
				}
			}
		});

		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = mContentEdit.getText().toString().trim();
				if (StringUtils.isBlank(text)) {
					UIUtils.showToast(ChatActivity.this, "不能发送空消息！");
					return;
				}
				if (!NetworkUtils.isNetworkConnected(ChatActivity.this)) {
					pageInfo.showError(ChatActivity.this
							.getString(R.string.msg_error_localNoNetwork));
					return;
				}
				if (uid < 0) {
					pageInfo.showError(ChatActivity.this
							.getString(R.string.msg_send_uiderror));
					return;
				}
				if (uid >= 0) {
					if (chattype == CHATTYPE_PERSON) {
						EntboostCM.sendText(uid, text);
					} else {
						EntboostCM.sendGroupText(uid, text);
					}
				}
				mChatMsgViewAdapter.notifyDataSetChanged();
				// 清空文本框
				mContentEdit.setText("");
				mMsgListView.setSelection(mMsgListView.getBottom());
				View view = getWindow().peekDecorView();
				if (view != null) {
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
							0);
				}
			}

		});

		mMsgListView = (ListView) this.findViewById(R.id.mListView);
		if (chattype == CHATTYPE_PERSON) {
			EntboostCM.loadPersonChatDiscCache(uid);
		} else {
			EntboostCM.loadGroupChatDiscCache(uid);
		}
		mChatMsgViewAdapter = new ChatMsgViewAdapter(ChatActivity.this,
				EntboostCache.getChatMsgs(uid));
		mMsgListView.setAdapter(mChatMsgViewAdapter);
		mMsgListView.setSelection(mMsgListView.getBottom());

		expressionGriView = (GridView) this
				.findViewById(R.id.expressionGridView);
		emotionsImageAdapter = new EmotionsImageAdapter(this);
		expressionGriView.setAdapter(emotionsImageAdapter);
		expressionGriView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Resource emotions = (Resource) emotionsImageAdapter
						.getItem(position);
				UIUtils.addEmotions(mContentEdit, emotions);
				emotionsAppPanel.setVisibility(View.GONE);
			}
		});

		emotionsAppPanel = (LinearLayout) this
				.findViewById(R.id.expressionAppPanel);
		picBtn = (ImageButton) this.findViewById(R.id.picBtn);
		picBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!NetworkUtils.isNetworkConnected(ChatActivity.this)) {
					pageInfo.showError(ChatActivity.this
							.getString(R.string.msg_error_localNoNetwork));
					return;
				}
				getPicFromContent();
			}
		});
		ImageButton emotionBtn = (ImageButton) this
				.findViewById(R.id.emotionBtn);
		emotionBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (emotionsAppPanel.getVisibility() == View.GONE) {
					emotionsAppPanel.setVisibility(View.VISIBLE);
				} else {
					emotionsAppPanel.setVisibility(View.GONE);
				}
				((BaseAdapter) expressionGriView.getAdapter())
						.notifyDataSetChanged();
			}
		});
	}

	private void getPicFromContent() {
		Intent intent = new Intent();
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		if (data != null) {
			// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
			Uri mImageCaptureUri = data.getData();
			Cursor cursor = ChatActivity.this.getContentResolver().query(
					mImageCaptureUri, null, null, null, null);
			String filePath = null;
			if (cursor.moveToFirst()) {
				filePath = cursor.getString(cursor.getColumnIndex("_data"));//
				cursor.close();
			}
			// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
			if (mImageCaptureUri != null) {
				try {
					// 这个方法是根据Uri获取Bitmap图片的静态方法
					if (filePath == null) {
						// image = MediaStore.Images.Media.getBitmap(
						// this.getContentResolver(), mImageCaptureUri);
					} else {
						if (filePath.endsWith("jpg")
								|| filePath.endsWith("png")) {
							sendPic(filePath);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void sendPic(String picUri) {
		if (uid < 0) {
			pageInfo.showError(ChatActivity.this
					.getString(R.string.msg_send_uiderror));
			return;
		}
		if (uid >= 0) {
			if (chattype == CHATTYPE_PERSON) {
				EntboostCM.sendPic(uid, picUri);
			} else {
				EntboostCM.sendGroupPic(uid, picUri);
			}
		}

		mChatMsgViewAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		super.onStop();
		EntboostCache.saveChatCache();
	}

}
