package com.ppma.graphics;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

public class Typefaces {
	private static final String TAG = "Typefaces";
	public static final String AWESOME = "awesome";

	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface get(Context c, String assetPath) {
		synchronized (cache) {
			if (!cache.containsKey(assetPath)) {
				try {
					Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
					cache.put(assetPath, t);
				} catch (Exception e) {
					Log.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage());
					return null;
				}
			}
			return cache.get(assetPath);
		}
	}
	
	public static void setTypeface(TextView textView, String fontName) {
		
		String assetPath = null;
		if (fontName.equals(AWESOME)) {
			assetPath = "fontawesome-webfont.ttf";
		}
		
		Typeface typeface = textView.getTypeface();
		if (typeface == null || !typeface.equals(Typefaces.get(textView.getContext(), assetPath))) {
	       textView.setTypeface(get(textView.getContext(), assetPath));					
		}
	}
	
	public static void setAwesomeTypeface(TextView textView) {
		setTypeface(textView, AWESOME);
	}
}
	//find: ^\s?.?\s([\w-]*)\s\[(.*) 
	//replace with : 	<string name="$1">$2</string>
	//find: ^\s?.?\s([\w-]*).*\[(.*) 
	//replace with : 	<string name="$1">$2</string>
