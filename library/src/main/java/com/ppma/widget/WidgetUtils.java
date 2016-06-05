package com.ppma.widget;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Toast;

import com.ppma.R;

public class WidgetUtils {
	
	public static final String TAG = "CommonUtil";

	public static void toast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void toastByLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public static void alertDialog(Context context, String title, String msg, OnClickListener positiveButtonOnClickListener) {
		alertDialog(context, title, msg, false, positiveButtonOnClickListener);
	}
	
	public static void alertDialog(Context context, String title, String msg, OnClickListener positiveButtonOnClickListener, OnClickListener negativeButtonOnClickListener) {
		alertDialog(context, title, msg, false, positiveButtonOnClickListener, negativeButtonOnClickListener);
	}

	public static void alertDialog(Context context, String title, String msg, boolean cancelable, OnClickListener positiveButtonOnClickListener) {
		alertDialog(context, title, msg, cancelable, positiveButtonOnClickListener, null);
	}
	
	public static void alertDialog(Context context, String title, String msg, boolean cancelable, OnClickListener positiveButtonOnClickListener, OnClickListener negativeButtonOnClickListener) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(msg)
		.setCancelable(cancelable)
		.setPositiveButton(R.string.ok, positiveButtonOnClickListener);
		
		if (negativeButtonOnClickListener != null) {
			dialogBuilder.setNegativeButton(R.string.cancel, negativeButtonOnClickListener);
		}
		
		dialogBuilder.create().show();
	}

	public static void alertDialog(Context context, String title, int itemsId, OnClickListener listener) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
				.setTitle(title)
				.setItems(itemsId, listener)
				.setCancelable(false);

		dialogBuilder.create().show();
	}

	public static void toggle(View view) {
		if (view.getVisibility() == View.GONE) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}
	
	public static void toggleSelect(View view) {
		if (view.isSelected()) {
			view.setSelected(false);
		} else {
			view.setSelected(true);
		}
	}

	public static void showKeyboard(View v, boolean flag) {
		InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
		if (flag) {
			imm.showSoftInput(v, 0);   
		} else {
			imm.hideSoftInputFromWindow(v.getWindowToken(),0);		
		}
	}
	
	public static View blankViewOnListViewById(Context context, int dimensionId) {
    	return blankViewOnListViewByHeight(context, (int) context.getResources().getDimension(dimensionId));
	}

	public static View blankViewOnListViewByHeight(Context context, int heightPixels) {
    	View blankView = new View(context);
    	blankView.setLayoutParams(new AbsListView.LayoutParams(
    			context.getResources().getDisplayMetrics().widthPixels,
    			heightPixels));
    	return blankView;
	}
	
	public static View blankView(Context context) {
    	View blankView = new View(context);
    	blankView.setLayoutParams(new AbsListView.LayoutParams(
    			context.getResources().getDisplayMetrics().widthPixels,
    			AbsListView.LayoutParams.WRAP_CONTENT));
    	return blankView;
	}	
}
