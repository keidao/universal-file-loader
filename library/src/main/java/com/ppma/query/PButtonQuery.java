package com.ppma.query;

import android.view.View;
import android.widget.Button;

public class PButtonQuery extends PViewQuery {
	private String TAG = PButtonQuery.class.getSimpleName();

	public Button button;

	public PButtonQuery(View view) {
		super(view);
		button = (Button) view;
	}
	
	public void setText(String text) {
		button.setText(text);
	}

	public void setText(int resid) {
		button.setText(resid);
	}

	public String getText() {
		return button.getText().toString();
	}
	
	public void textColor(int resid) {
		button.setTextColor(button.getResources().getColor(resid));
	}
}	