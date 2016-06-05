package com.ppma.query;

import android.view.View;
import android.widget.TextView;

public class PTextQuery extends PViewQuery {
	private String TAG = PTextQuery.class.getSimpleName();

	public TextView textView;

	public PTextQuery(View view) {
		super(view);
		textView = (TextView) view;
	}
	
	public PTextQuery text(String text) {
		textView.setText(text);
		return this;
	}

	public String text() {
		return textView.toString();
	}
	
	public PTextQuery textColor(int resid) {
		textView.setTextColor(textView.getResources().getColor(resid));
		return this;
	}
}	