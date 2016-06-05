package com.ppma.view;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.ppma.query.PTextQuery;

public class PViewHolder {
	
	private final static int KEY_VIEW = 0;
	
	@SuppressWarnings("unchecked")
	public static <T extends View> T v(View convertView, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag(KEY_VIEW);
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
		}
		
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			viewHolder.put(id, childView);
		}
		
		return (T) childView;
	}
	
	public static PTextQuery text(View convertView, int id) {
		return new PTextQuery(v(convertView, id));
	}	

	public static ImageView image(View convertView, int id) {
		return v(convertView, id);
	}	
}
