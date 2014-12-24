package com.entboost.im.base;

import net.yunim.eb.signlistener.EntboostIMListenerInterface;
import net.yunim.service.Entboost;
import net.yunim.service.entity.CardInfo;
import net.yunim.service.entity.ChatRoomRichMsg;
import net.yunim.service.entity.SysMessage;
import net.yunim.utils.UIUtils;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entboost.im.R;
import com.entboost.im.global.MyApplication;
import com.entboost.ui.base.activity.AbActivity;
import com.entboost.ui.base.view.titlebar.AbTitleBar;

public class EbActivity extends AbActivity implements
		EntboostIMListenerInterface {

	private Dialog customProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AbTitleBar titleBar = this.getTitleBar();
		titleBar.setTitleBarGravity(Gravity.CENTER, Gravity.RIGHT);
		titleBar.setTitleText(this.getTitle().toString());
		titleBar.setLogo(R.drawable.back);
		titleBar.setLogoOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleBar.setTitleBarBackground(R.drawable.titlebar);
		titleBar.setTitleTextMargin(10, 0, 0, 0);
		titleBar.setLogoLine(R.drawable.line);
		Entboost.addListener(this);
	}

	public void showProgressDialog(String message) {
		if (customProgressDialog == null) {
			customProgressDialog = createLoadingDialog(this, message);
			// 设置点击屏幕Dialog不消失
			customProgressDialog.setCanceledOnTouchOutside(false);
			customProgressDialog.show();
		}
	}

	/**
	 * 描述：移除进度框.
	 */
	public void removeProgressDialog() {
		if (customProgressDialog != null) {
			try {
				customProgressDialog.dismiss();
			} catch (Exception e) {
			}
			customProgressDialog = null;
		}
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (!UIUtils.isAppOnForeground(getApplicationContext())) {
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
	public void disConnect(String err) {
		pageInfo.showError(err);
		removeProgressDialog();
	}

	@Override
	public void network() {
		pageInfo.hide();
	}

	@Override
	public void onUserStateChange(Long uid) {
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

	@Override
	public void onReceiveMyMessage(SysMessage message) {
		// TODO Auto-generated method stub

	}

}
