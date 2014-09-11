package com.entboost.im.base;

import net.yunim.eb.signlistener.EntboostIMListenerInterface;
import net.yunim.service.Entboost;
import net.yunim.service.EntboostCache;
import net.yunim.service.entity.CardInfo;
import net.yunim.service.entity.ChatRoomRichMsg;
import net.yunim.service.entity.MemberInfo;
import net.yunim.utils.UIUtils;

import org.apache.commons.lang.StringUtils;

import android.os.Bundle;

import com.entboost.im.R;
import com.entboost.im.global.MyApplication;
import com.entboost.ui.base.activity.AbActivity;
import com.entboost.ui.base.view.titlebar.AbTitleBar;

public class EbActivity extends AbActivity implements
		EntboostIMListenerInterface {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AbTitleBar titleBar = this.getTitleBar();
		titleBar.setBackgroundResource(R.drawable.title_bar);
		titleBar.setTitleText(this.getTitle().toString());
		titleBar.setLogo(R.drawable.entlogo);
		Entboost.addListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EntboostCache.saveCache();
		// LogUtils.d(this.getClass().getName() + "执行onStop");
		if (!UIUtils.isAppOnForeground(getApplicationContext())) {
			// LogUtils.d("进入后台");
			MyApplication.getInstance().setShowNotificationMsg(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.getInstance().isShowNotificationMsg()) {
			MyApplication.getInstance().setShowNotificationMsg(false);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Entboost.removeListener(this);
	}

	@Override
	public void disConnect() {
		pageInfo.showError(this.getString(R.string.msg_error_disConnect));
		removeProgressDialog();
	}

	@Override
	public void network() {
		String info = pageInfo.getInfo();
		if (StringUtils.equals(info,
				this.getString(R.string.msg_error_serviceNoNetwork))
				|| StringUtils.equals(info,
						this.getString(R.string.msg_error_disConnect))
				|| StringUtils.equals(info,
						this.getString(R.string.msg_error_serviceStop))) {
			pageInfo.hide();
		}
	}

	@Override
	public void localNetwork() {
		String info = pageInfo.getInfo();
		if (StringUtils.equals(info,
				this.getString(R.string.msg_error_localNoNetwork))) {
			pageInfo.hide();
		}
	}

	@Override
	public void localNoNetwork() {
		pageInfo.showError(this.getString(R.string.msg_error_localNoNetwork));
		removeProgressDialog();
	}

	@Override
	public void serviceNoNetwork() {
		pageInfo.showError(this.getString(R.string.msg_error_serviceNoNetwork));
		removeProgressDialog();
	}

	@Override
	public void serviceStop() {
		pageInfo.showError(this.getString(R.string.msg_error_serviceStop));
		removeProgressDialog();
	}

	// @Override
	// public void receiveAppMessage(AppMsg msg) {
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void onUserStateChange(MemberInfo pMemberInfo) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public void onAppMsg(SysMessage msg) {
	// // TODO Auto-generated method stub
	//
	// }

	// @Override
	// public void onLoadAllGroupInfo(List<GroupInfo> groups) {
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void onLoadAllMemberInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveUserMessage(ChatRoomRichMsg msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendStatusChanged(ChatRoomRichMsg msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadAllContactInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCallConnected(Long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCallHangup(Long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCallIncoming(CardInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCallReject(CardInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCallTimeout(CardInfo arg0) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public String onReceiveFile(UserMessage message) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public void onReceiveFileIncoming(FileCache fileCache) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onReceiveFileFinish(FileCache fileCache) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onCallAlerting() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onCallIncoming() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onCallBusy() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onCallHangup() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onCallError(EB_STATE_CODE nState) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onCallConnected() {
	// // TODO Auto-generated method stub
	//
	// }
}
