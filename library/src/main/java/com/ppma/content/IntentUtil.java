package com.ppma.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;

public class IntentUtil {
	
	public static void launchApp(Fragment fragment, String packageName) {
		/*
		 * 참고 페이지
		 * 어플 실행 : 
		 * http://stackoverflow.com/questions/3872063/android-launch-an-application-from-another-application 
		 * http://blog.daum.net/seed/2629
		 */
		Intent i;
		i = fragment.getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
		if (i != null) {
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			fragment.startActivity(i);
		} else {
			goPlaystore(fragment, packageName);
		}
	}
	
	public static boolean existApp(Fragment fragment, String packageName) {
		Intent i = fragment.getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
		if (i != null) {
			return true;
		}
		return false;
	}	
	
	public static void viewAppDetail(Context context, String packageName) {
        /*
		 * 참조 페이지
		 * 어플리케이션 정보 : http://stackoverflow.com/questions/6238946/android-launch-applications-detail-page
		 */
		// Toast.makeText(view.getContext(), packageName, Toast.LENGTH_SHORT).show();
		Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		i.setData(Uri.parse("package:" + packageName));
		context.startActivity(i);		
	}
	
	public static void uninstallApp(Context context, String packageName) {
    	Intent i = new Intent(Intent.ACTION_DELETE);
		i.setData(Uri.parse("package:" + packageName));
		context.startActivity(i);
	}

	public static void uninstallApp(Fragment fragment, String packageName) {
    	Intent i = new Intent(Intent.ACTION_DELETE);
		i.setData(Uri.parse("package:" + packageName));
		fragment.startActivityForResult(i, 1000);
	}
	
	public static void shoutcut(Context context, String packageName) {
		/*
		 * http://stackoverflow.com/questions/6337431/android-create-shortcuts-on-the-home-screen
		 * http://blog.naver.com/PostView.nhn?blogId=space5084&logNo=60135449764&categoryNo=15&viewDate=&currentPage=1&listtype=0&from=postList
		 */
		Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		PackageManager pm = context.getPackageManager();

		PackageInfo packageInfo;
		try {
			packageInfo = pm.getPackageInfo(packageName, PackageManager.PERMISSION_GRANTED);
			Bitmap bitmap = ((BitmapDrawable)packageInfo.applicationInfo.loadIcon(pm)).getBitmap();
//		    ShortcutIconResource icon =  Intent.ShortcutIconResource.fromContext(context, packageInfo.applicationInfo.i);
	
			Intent intent = new Intent();
//			Intent launchIntent = new Intent(context, Class.forName(packageInfo.applicationInfo.className));
	
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, packageInfo.applicationInfo.loadLabel(pm).toString());
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
			intent.putExtra("duplicate", false);
	
			intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			context.sendBroadcast(intent);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
//		setResult(RESULT_OK, intent);
	}
	
	public static void goPlaystore(Fragment fragment, String packageName) {
    	/*
    	 * How to open the google play store directly from my android application
    	 * http://stackoverflow.com/questions/11753000/how-to-open-the-google-play-store-directly-from-my-android-application
    	 */
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
		    i.setData(Uri.parse("market://details?id=" + packageName));
		    fragment.startActivityForResult(i, 1001);
		} catch (android.content.ActivityNotFoundException anfe) {
		    i.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
		    fragment.startActivityForResult(i, 1001);
		}
	}
	
	public static void goWeb(Context context, String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.setData(Uri.parse(url));
	    context.startActivity(i);
	}
	
	// 전화걸기
	public static void callPhone(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		context.startActivity(intent);
	}
	
	// 이메일
	public static void sendEmail(Context context, String to, String title, String content) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {to});
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		context.startActivity(Intent.createChooser(intent, "이메일 전송"));
	}
}
