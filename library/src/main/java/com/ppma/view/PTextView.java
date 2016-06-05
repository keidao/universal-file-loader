package com.ppma.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ppma.R;
import com.ppma.graphics.Typefaces;

public class PTextView extends TextView {

	private final static int VALUE_NOT_SET = -1;
	private final static String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
	private boolean mHtml;

	public PTextView(Context context) {
		super(context);
	}

	public PTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PTextView);

		String fontName = a.getString(R.styleable.PTextView_typeface);
		if (fontName != null)
			Typefaces.setTypeface(this, fontName);

		mHtml = a.getBoolean(R.styleable.PTextView_html, false);
		if (mHtml) {
			int textResourceId = attrs.getAttributeResourceValue(NAMESPACE_ANDROID, "text", VALUE_NOT_SET);
			if (VALUE_NOT_SET != textResourceId) {
				setText(Html.fromHtml(context.getString(textResourceId)));
			}
		}

		a.recycle();
	}

	public PTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("NewApi")
	public PTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
}
