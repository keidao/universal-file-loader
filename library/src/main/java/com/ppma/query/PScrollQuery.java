package com.ppma.query;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.widget.ScrollView;

public class PScrollQuery extends PViewQuery {

	public ScrollView scrollView;

	public PScrollQuery(View view) {
		super(view);
		scrollView = (ScrollView) view;
	}
	
	
	public void pageDown() {
		int scrollY;
		int scrollContnetHeight = scrollView.getChildAt(0).getHeight();
		if (scrollView.getScrollY() + scrollView.getHeight() < scrollContnetHeight) {
			scrollY = scrollView.getScrollY() + scrollView.getHeight();
		} else {
			scrollY = scrollContnetHeight - scrollView.getHeight();
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && false) {
			ObjectAnimator anim = ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getScrollY(), (int)scrollY);
			anim.setDuration(600);
			anim.start();			
		} else {
			scrollView.scrollTo(0, (int) scrollY);
		}
	}
	
	public void pageUp() {
		int scrollY;
		if (scrollView.getScrollY() - scrollView.getHeight() > 0) {
			scrollY =  scrollView.getScrollY() - scrollView.getHeight(); 
		} else {
			scrollY =  0; 
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && false) {
			ObjectAnimator anim = ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getScrollY(), (int)scrollY);
			anim.setDuration(400);
			anim.start();			
		} else {
			scrollView.scrollTo(0, (int)scrollY);
		}
	}		
}
