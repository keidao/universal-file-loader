package com.ppma.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class AssetUtils {
	
	private static HashMap<String, String> sCache = new HashMap<String, String>();

	public static synchronized String loadFile(Context context, String path) {
		if (sCache.containsKey(path)) {
			return sCache.get(path);
		}
		
		try {
			InputStream is = context.getAssets().open(path);
			Writer writer = new StringWriter();

			char[] buffer = new char[2048];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			
			String decodedBase64 = writer.toString().replace("!", "A").replace("*", "B").replace("+", "1");
	    	decodedBase64 = new String(Base64.decode(decodedBase64, Base64.URL_SAFE));

			return decodedBase64;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static Bitmap getBitmapFromAssets(Context context, String fileName) {

		try {
			AssetManager assetManager = context.getAssets();
			InputStream istr = assetManager.open(fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(istr);
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Drawable getDrawableFromAssets(Context context, String fileName) {
		fileName = fileName.replace("assets://", "");

		try {
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open(fileName);
			return Drawable.createFromStream(is, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
