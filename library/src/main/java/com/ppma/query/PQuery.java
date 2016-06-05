package com.ppma.query;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

public class PQuery {

	protected View mContentView;
	
	public PQuery(Activity activity) {
		mContentView = activity.getWindow().getDecorView();
	}

	public PQuery(Fragment fragment) {
		mContentView = fragment.getView();
	}

	public PQuery(View view) {
		mContentView = view;
	}
	
	public PViewQuery id(int resid) {
		return new PViewQuery(mContentView.findViewById(resid));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends View> T v(int resid) {
		return (T) mContentView.findViewById(resid);
	}

	public PTextQuery text(int resid) {
		return new PTextQuery(mContentView.findViewById(resid));
	}

	public ImageView image(int resid) {
		return (ImageView) mContentView.findViewById(resid);
	}

	public PButtonQuery button(int resid) {
		id(resid);
		return new PButtonQuery(mContentView.findViewById(resid));
	}
	
	public PScrollQuery scroll(int resid) {
		id(resid);
		return new PScrollQuery(mContentView.findViewById(resid));
	}
}
