package com.ppma.view;

import java.util.Map;

import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class H {
	
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View convertView, int id) {
		ViewAndItem viewAndItem = getViewAndItem(convertView);

		SparseArray<View> viewHolder = viewAndItem.sparseArray;
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
		}
		
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			viewHolder.put(id, childView);
			childView.setTag(viewAndItem);
		}
		
		return (T) childView;
	}
	
	public static TextView textView(View convertView, int id) {
		return get(convertView, id);
	}

	public static CheckBox checkBox(View convertView, int id) {
		return get(convertView, id);
	}

	public static ImageView imageView(View convertView, int id) {
		return get(convertView, id);
	}	

	public static void setItem(View convertView, Map<String, String> item) {
		ViewAndItem viewAndItem = getViewAndItem(convertView);
		viewAndItem.item = item;
	}
	
	public static Map<String, String> getItem(View view) {
		ViewAndItem viewAndData = (ViewAndItem) view.getTag();
		return viewAndData.item;
	}
	
	private static ViewAndItem getViewAndItem(View convertView) {
		ViewAndItem viewAndItem = (ViewAndItem) convertView.getTag();
		if (viewAndItem == null) {
			viewAndItem = new ViewAndItem();
			convertView.setTag(viewAndItem);
		}
		return viewAndItem;
	}
	
	private static class ViewAndItem {
		public SparseArray<View> sparseArray;
		public Map<String, String> item;
	}
}
