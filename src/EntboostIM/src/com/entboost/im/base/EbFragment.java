package com.entboost.im.base;

import net.yunim.eb.signlistener.EntboostIMListenerInterface;
import net.yunim.service.Entboost;
import net.yunim.service.entity.CardInfo;
import net.yunim.service.entity.ChatRoomRichMsg;
import net.yunim.service.entity.MemberInfo;

import org.apache.commons.lang.StringUtils;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entboost.im.R;
import com.entboost.ui.base.view.PageInfo;

public class EbFragment extends Fragment implements EntboostIMListenerInterface {

	protected LayoutInflater mInflater;

	private String infoText;

	private int infoType;

	protected EbActivity activity;

	public void showPageInfo() {
		if (StringUtils.isNotBlank(infoText)) {
			activity.pageInfo.show(infoText, infoType);
		}
	}

	public void showProgress(String text) {
		infoText = text;
		infoType = PageInfo.TYPE_PROGRESS;
		activity.pageInfo.showProgress(text);
	}

	public void showError(String text) {
		infoText = text;
		infoType = PageInfo.TYPE_ERROR;
		activity.pageInfo.showError(text);
	}

	public void showInfo(String text) {
		infoText = text;
		infoType = PageInfo.TYPE_INFO;
		activity.pageInfo.showInfo(text);
	}

	public void hide() {
		activity.pageInfo.hide();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Entboost.removeListener(this);
	}

	public void refreshPage() {
		hide();
	};

	protected View onCreateEbView(int layoutId, LayoutInflater inflater,
			ViewGroup container) {
		mInflater = inflater;
		activity = (EbActivity) this.getActivity();
		Entboost.addListener(this);
		return inflater.inflate(layoutId, container, false);
	}

	@Override
	public void disConnect() {
		showError(this.getString(R.string.msg_error_disConnect));
	}

	@Override
	public void network() {
		if (StringUtils.equals(infoText,
				this.getString(R.string.msg_error_serviceNoNetwork))
				|| StringUtils.equals(infoText,
						this.getString(R.string.msg_error_disConnect))
				|| StringUtils.equals(infoText,
						this.getString(R.string.msg_error_serviceStop))) {
			activity.pageInfo.hide();
		}
	}

	@Override
	public void localNetwork() {
		if (StringUtils.equals(infoText,
				this.getString(R.string.msg_error_localNoNetwork))) {
			activity.pageInfo.hide();
		}
	}

	@Override
	public void localNoNetwork() {
		showError(this.getString(R.string.msg_error_localNoNetwork));
	}

	@Override
	public void serviceNoNetwork() {
		showError(this.getString(R.string.msg_error_serviceNoNetwork));
	}

	@Override
	public void serviceStop() {
		showError(this.getString(R.string.msg_error_serviceStop));
	}

	@Override
	public void onUserStateChange(MemberInfo pMemberInfo) {
		// TODO Auto-generated method stub

	}

//	@Override
//	public void onLoadAllGroupInfo(List<GroupInfo> groups) {
//		// TODO Auto-generated method stub
//
//	}

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
}
