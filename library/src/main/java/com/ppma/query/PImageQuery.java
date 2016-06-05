package com.ppma.query;

import android.view.View;
import android.widget.ImageView;

public class PImageQuery extends PViewQuery {
	private String TAG = PImageQuery.class.getSimpleName();

	public ImageView imageView;

	public PImageQuery(View view) {
		super(view);
		imageView = (ImageView) view;
	}
}