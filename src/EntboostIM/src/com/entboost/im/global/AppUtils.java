package com.entboost.im.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.entboost.im.R;

public class AppUtils {
	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return context.getString(R.string.version_name) + version;
		} catch (Exception e) {
			return "";
		}
	}
}
