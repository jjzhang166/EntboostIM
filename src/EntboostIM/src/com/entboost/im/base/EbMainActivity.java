package com.entboost.im.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

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
		titleBar.setBackgroundResource(R.drawable.title_bar);
		titleBar.setTitleText("恩布通信IM");
		titleBar.setLogo(R.drawable.entlogo);
		mBottomTabView = (AbBottomTabView) findViewById(R.id.mBottomTabView);

		// 如果里面的页面列表不能下载原因：
		// Fragment里面用的AbTaskQueue,由于有多个tab，顺序下载有延迟，还没下载好就被缓存了。改成用AbTaskPool，就ok了。
		// 或者setOffscreenPageLimit(0)

		// 缓存数量
		mBottomTabView.getViewPager().setOffscreenPageLimit(0);

		// 禁止滑动
		/*
		 * mBottomTabView.getViewPager().setOnTouchListener(new
		 * OnTouchListener(){
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { return
		 * true; }
		 * 
		 * });
		 */

		mBottomTabView.setTabPadding(2, 10, 2, 2);
		mBottomTabView.setTabBackgroundResource(R.drawable.tab_bg);
		mBottomTabView.setTabLayoutBackgroundResource(R.drawable.bottom_bar);
		mBottomTabView.setTabTextColor(Color.GRAY);

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
