package com.ppma.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

public class DeviceUtils {

	public static String getCurrentVersion(Context context) {
		String currentVersion = "";
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			currentVersion = info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return currentVersion;
	}

	public static int getCurrentVersionCode(Context context) {
		int currentVersionCode = -1;
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			currentVersionCode = info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return currentVersionCode;
	}
	public static String getDevcieOs() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	public static int getTimeZone() {
		return Calendar.getInstance().getTimeZone().getRawOffset() / 1000;
	}
	
	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}
	
	// Do not call this function from the main thread. Otherwise, 
	// an IllegalStateException will be thrown.
	public String getAdvertisingId(Context context) {
		try {
			Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
			final String id = adInfo.getId();
			final boolean isLAT = adInfo.isLimitAdTrackingEnabled();
			if (isLAT)
				return "OptedOut"; // Google restricts usage of the id to "build profiles" if the user checks opt out so we can't collect.
			else
				return id;
		}
		catch (IOException e) { Log.d("EXCEPTION", "IOException"); } // Unrecoverable error connecting to Google Play services (e.g., the old version of the service doesn't support getting AdvertisingId).
		catch (GooglePlayServicesNotAvailableException e) { Log.d("EXCEPTION", "GooglePlayServicesNotAvailableException"); } // Google Play services is not available entirely.
		//catch (IllegalStateException e) { Log.d("EXCEPTION", "IllegalStateException"); } // Unknown error
		catch (GooglePlayServicesRepairableException e) { Log.d("EXCEPTION", "GooglePlayServicesRepairableException"); } // Google Play Services is not installed, up-to-date, or enabled

		return null;
	}

	public static String getAltId(Context context) {
		String id;

		id = getPhoneId(context);
		if (id != null)
			return id;

		// Wifi is more consistent than the Android ID but Corona's deviceID falls back to this
		id = getAndroidId(context);
		if (id != null)
			return id;
		
		id = getWifiMac(context);
		if (id != null)
			return id;

		return null;
	}
	
	// See links on alternative unique IDs for players
	// http://technet.weblineindia.com/mobile/getting-unique-device-id-of-an-android-smartphone/
	// http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id

	// Requires android.permission.READ_PHONE_STATE permission
	public static String getPhoneId(Context context)
	{
		try {
			final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			return deviceId;
		}
		catch (RuntimeException e) {
			return null;
		}
	}
	
	public static String getAndroidId(Context context) {
		try {
			final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			// see http://code.google.com/p/android/issues/detail?id=10603 for info on this 'dup' id.
			if (androidId != "9774d56d682e549c")
				return androidId;
		}
		catch (RuntimeException e) {
			return null;
		}

		return null;
	}
	
	// Requires android.permission.ACCESS_WIFI_STATE permission
	public static String getWifiMac(Context context) {
		try {
			final String m_wlanMacAdd = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
			return m_wlanMacAdd;
		}
		catch (RuntimeException e) {
			return null;
		}
	}

	public static boolean isNetworkUsable(Context context) {
		boolean flag = false;

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo wimax = cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

		if ( (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()) || (wimax != null && wimax.isConnectedOrConnecting())) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}
}
