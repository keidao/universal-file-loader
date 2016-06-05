package com.ppma.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;

public class SystemUtils {
	public static void powerLock(Context context) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "");

		if (!pm.isScreenOn()) {
			wakeLock.acquire();

			new Handler().postDelayed(new Runnable() {
				@Override 
				public void run() {
					wakeLock.release();
				}
			}, 3000);
		}
	}

	public static void ringtone(Context context) {
		Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
		Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
		ringtone.play();
	}

	public static void vibrate(Context context, int duration) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(duration);
	}

	public static void alarm(Context context) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		switch (audioManager.getRingerMode()) {
		case AudioManager.RINGER_MODE_SILENT:
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			SystemUtils.vibrate(context, 500);
			break;
		default:
			SystemUtils.ringtone(context);
			break;
		}
	}
}
