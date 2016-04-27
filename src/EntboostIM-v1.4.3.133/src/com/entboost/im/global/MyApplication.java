package com.entboost.im.global;

import java.util.ArrayList;
import java.util.List;

import net.yunim.service.Entboost;
import android.app.Application;
import android.graphics.Bitmap;

import com.entboost.im.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {

	private static MyApplication myInstance;

	public static long appid = 278573612908l;

	public static String appkey = "ec1b9c69094db40d9ada80d657e08cc6";

	private boolean showNotificationMsg = false;

	private List<Object> selectedUserList = new ArrayList<Object>();

	private DisplayImageOptions imgOptions;

	public DisplayImageOptions getImgOptions() {
		return imgOptions;
	}

	public List<Object> getSelectedUserList() {
		return selectedUserList;
	}

	public void setSelectedUserList(List<Object> selectedUserList) {
		this.selectedUserList = selectedUserList;
	}

	public boolean isShowNotificationMsg() {
		return showNotificationMsg;
	}

	public void setShowNotificationMsg(boolean showNotificationMsg) {
		this.showNotificationMsg = showNotificationMsg;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		myInstance = this;
		initEbConfig();
	}

	public void initEbConfig() {
		Entboost.init(getApplicationContext());
		Entboost.showSotpLog(true);
		Builder imgConfig = new ImageLoaderConfiguration.Builder(this);
		imgConfig.threadPriority(Thread.NORM_PRIORITY - 2);// 设置线程的优先级
		imgConfig.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 设置缓存文件的名字
		imgConfig.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		imgConfig.denyCacheImageMultipleSizesInMemory();// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
		imgConfig.tasksProcessingOrder(QueueProcessingType.LIFO);// 设置图片下载和显示的工作队列排序
		ImageLoader.getInstance().init(imgConfig.build());
		imgOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.entboost_logo)
				.showImageForEmptyUri(R.drawable.entboost_logo)
				.showImageOnFail(R.drawable.entboost_logo).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	public static MyApplication getInstance() {
		return myInstance;
	}

}
