package com.ppma.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;

/**
 * Created by keidao on 1/10/16.
 */
public class DisplayUtils {
    public static int dpToPx(Context context, int dps) {
        return Math.round(context.getResources().getDisplayMetrics().density * dps);
    }

    public static void setFullScreen(Activity activity, boolean enabled) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int visibility;
            if (enabled) {
                visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            } else {
                visibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
    }

    public static void setStatusBarColorResId(Activity activity, int colorId) {
        setStatusBarColor(activity, activity.getResources().getColor(colorId));
    }

    public static void setStatusBarColorResId(Activity activity, String colorString) {
        setStatusBarColor(activity, Color.parseColor(colorString));
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    public static int getColor(int colorPrimary, float alpha) {
        int colorPrimaryDark = Color.rgb((int) (Color.red(colorPrimary) * alpha),
                (int) (Color.green(colorPrimary) * alpha),
                (int) (Color.blue(colorPrimary) * alpha));
        return colorPrimaryDark;
    }

    public static Display getWindowDisplay(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();

        return display;
    }

    public static float getDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.density;
    }

    public static int getDPI(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.densityDpi;
    }

    public static int getDispWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.widthPixels;
    }

    public static int getDispHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getApplyDimenssion(Context context, float dpSize) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, context.getResources().getDisplayMetrics());
    }
}