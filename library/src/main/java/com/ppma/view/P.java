package com.ppma.view;

import java.util.Map.Entry;

import android.graphics.Paint;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class P {

	@SuppressWarnings("unchecked")
	public static <T extends View> T v(View v, int resid) {
		View view = v.findViewById(resid);
		return (T) view;
	}

	@SuppressWarnings("unchecked")
	public static <T extends View> T v(Window window, int resid) {
		View view = window.findViewById(resid);
		return (T) view;
	}
	
	public static TextView findTextView(View v, int resid) {
		View view = v.findViewById(resid);
		return (TextView) view;
	}

	public static EditText findEditText(View v, int resid) {
		View view = v.findViewById(resid);
		return (EditText) view;
	}
	
	public static Button findButton(View v, int resid) {
		View view = v.findViewById(resid);
		return (Button) view;
	}

	public static ImageView imageView(View v, int resid) {
		return (ImageView) v.findViewById(resid);
	}

	public static Spinner findSpinner(View v, int resid) {
		View view = v.findViewById(resid);
		return (Spinner) view;
	}
	
	public static CheckBox fCheckBox(View v, int resid) {
		View view = v.findViewById(resid);
		return (CheckBox) view;
	}
	
	public static void setCheckBox(View v, int resid, boolean checked) {
		((CheckBox) v.findViewById(resid)).setChecked(checked);
	}

	public static void setText(View v, int resid, String text) {
		setText(v, resid, text, 0, 0);
	}
	
	public static void setText(View v, int resid, String text, int colorResid) {
		setText(v, resid, text, colorResid, 0);
	}

	public static void setText(View v, int resid, String text, int colorResid, int gravity) {
		TextView textView = (TextView) v.findViewById(resid);
		textView.setText(text);
		if (colorResid != 0) {
			textView.setTextColor(colorResid);
		}
		if (gravity != 0) {
			textView.setGravity(gravity);
		}
	}
	
	public static void setTagValue(View v, int resid, String value) {
		v.findViewById(resid).setTag(value);
	}
	
	public static String getText(View v, int resid) {
		TextView textView = (TextView) v.findViewById(resid);
		return textView.getText().toString();
	}

	@SuppressWarnings("unchecked")
	public static String getSpinnerText(View v, int resid) {
		String text = ((Entry<String, String>)P.findSpinner(v, resid).getSelectedItem()).getValue();
		return text;
	}
	
	public static String getTagValue(View v, int resid) {
		Object value = v.findViewById(resid).getTag();
		return value == null ? "" : (String) value;
	}
	
	public static void requestFocus(View v, int resid) {
		v.findViewById(resid).requestFocus();
	}
	
	public static void show(View v, int resid) {
		v.findViewById(resid).setVisibility(View.VISIBLE);
	}

	public static void hide(View v, int resid) {
		v.findViewById(resid).setVisibility(View.GONE);
	}

	public static void invisible(View v, int resid) {
		v.findViewById(resid).setVisibility(View.INVISIBLE);
	}
	
	public static void toggle(View v, int resid) {
		View view = v.findViewById(resid);
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}

	public static void setBackgroundResource(View v, int resid, int colorResid) {
		v.findViewById(resid).setBackgroundResource(colorResid);
	}
	
	/**
	 * PJY
	 */
	public static void setBackgroundColor(View v, int resid, int color, int alpha) {
		View view = v.findViewById(resid);
		
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setAlpha(alpha);
		view.setBackgroundColor(paint.getColor());
	}
	
	
}
