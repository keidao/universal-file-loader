package com.ppma.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class PSwipeRefreshLayout extends SwipeRefreshLayout {

	private boolean mEnabledSwiping = false;
	
	public PSwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setEnabledSwiping(boolean enabledSwiping) {
		mEnabledSwiping = enabledSwiping;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mEnabledSwiping) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return false;
		}
	}
}
