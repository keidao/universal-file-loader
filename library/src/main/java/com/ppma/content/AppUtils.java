package com.ppma.content;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppUtils {
	
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;
	    try {
	        pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	    } catch (NameNotFoundException e) {
	    }
	    return pi;
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
}
