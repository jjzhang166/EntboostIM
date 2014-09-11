package com.entboost.im.setting;

import net.yunim.service.EntboostLC;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.entboost.global.AbConstant;
import com.entboost.im.R;
import com.entboost.im.base.EbFragment;
import com.entboost.im.user.LoginActivity;
import com.entboost.im.user.UserInfoActivity;

public class SettingFragment extends EbFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = onCreateEbView(R.layout.fragment_setting, inflater,
				container);
		view.findViewById(R.id.info_person_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity,
								UserInfoActivity.class);
						startActivity(intent);
					}
				});
		view.findViewById(R.id.info_changepwd_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity,
								ChangePWDActivity.class);
						startActivity(intent);
					}
				});
		view.findViewById(R.id.btn_logout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						View view = mInflater.inflate(R.layout.dialog_exit,
								null);
						activity.showDialog(AbConstant.DIALOGBOTTOM, view);
						view.findViewById(R.id.logoutBtn).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										EntboostLC.logout();
										activity.getBottomDialog().cancel();
										activity.finish();
										Intent intent = new Intent(activity,
												LoginActivity.class);
										startActivity(intent);
									}
								});
						view.findViewById(R.id.exitBtn).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										EntboostLC.exit();
										activity.getBottomDialog().cancel();
										activity.finish();
									}
								});
						view.findViewById(R.id.cancelBtn).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										activity.getBottomDialog().cancel();
									}
								});
					}
				});
		return view;
	}
}
