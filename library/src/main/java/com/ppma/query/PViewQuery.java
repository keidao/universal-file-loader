package com.ppma.query;

import android.view.View;

public class PViewQuery {

	protected View mV;
	
	public PViewQuery(View view) {
		mV = view;
	}
	
	public void show() {
		mV.setVisibility(View.VISIBLE);
	}

	public void hide() {
		mV.setVisibility(View.GONE);
	}
	
	public static void invisible(View v, int resid) {
		v.findViewById(resid).setVisibility(View.INVISIBLE);
	}
	
	public boolean isVisible() {
		if (mV.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}
	
	public void toggle() {
		if (mV.getVisibility() == View.VISIBLE) {
			mV.setVisibility(View.GONE);
		} else {
			mV.setVisibility(View.VISIBLE);
		}
	}
}
