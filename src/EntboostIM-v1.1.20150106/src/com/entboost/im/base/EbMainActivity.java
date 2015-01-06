package com.entboost.im.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.entboost.im.R;
import com.entboost.ui.base.view.sliding.AbBottomTabView;
import com.entboost.ui.base.view.titlebar.AbTitleBar;

public class EbMainActivity extends EbActivity {
	public AbBottomTabView mBottomTabView;
	private ViewPager.OnPageChangeListener mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_main);
		AbTitleBar titleBar = this.getTitleBar();
		titleBar.setTitleText("恩布企业IM");
		titleBar.hideLogo();
		mBottomTabView = (AbBottomTabView) findViewById(R.id.mBottomTabView);

		// 缓存数量
		mBottomTabView.getViewPager().setOffscreenPageLimit(0);

		mBottomTabView.setTabLayoutBackgroundResource(R.drawable.bottombar);
		mBottomTabView.setTabPadding(2, 10, 2, 2);
		mBottomTabView.setTabTextColor(getResources().getColor(
				R.color.bottomTabViewText));
		mBottomTabView.setTabSelectColor(getResources().getColor(
				R.color.bottomTabViewTextSelect));
		mBottomTabView.setTabTextSize(12);

		mListener = new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int index) {
				mBottomTabView.setCurrentItem(index);
				EbFragment fragment = (EbFragment) mBottomTabView
						.getItemView(index);
				fragment.refreshPage();
			}

		};
		mBottomTabView.setOnPageChangeListener(mListener);
	}

	public void setTitle(String text) {
		this.getTitleBar().setTitleText(text);
	}

	public void addView(String tabText, Fragment fragment,
			Drawable drawableNormal, Drawable drawablePressed) {
		mBottomTabView.addItemView(tabText, fragment, drawableNormal,
				drawablePressed);
	}

}
