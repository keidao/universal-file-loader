package com.ppma.content;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonPreferences {

	public static final String DEFAULT_NAME = "DEFAULT";

	private Context mContext;
	private SharedPreferences mSharedPreferences;

	private GsonPreferences(Context context) {
		mContext = context;
		mSharedPreferences = mContext.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
	}

	private GsonPreferences(Context context, String name) {
		mContext = context;
		mSharedPreferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static GsonPreferences getInstance(Context context) {
		return new GsonPreferences(context);
	}

	public static GsonPreferences getInstance(Context context, String name) {
		return new GsonPreferences(context, name);
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public String getString(String key) {
		String value = mSharedPreferences.getString(key, "");
		return value;
	}

	public void putLong(String key, long value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putLong(key, value);
		editor.apply();
	}

	public long getLong(String key) {
		long value = mSharedPreferences.getLong(key, 0);
		return value;
	}

	public void removeString(String key) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.remove(key);
		editor.apply();
	}

	public SharedPreferences getSharedPreferences() {
		return mSharedPreferences;
	}

	private void changePreferenceName(Context context, String newName, String key) {
		GsonPreferences gsonPreferences = GsonPreferences.getInstance(context);
		String oldValue = gsonPreferences.getString(key);
		if (!oldValue.equals("")) {
			GsonPreferences.getInstance(context).removeAll(key);
			GsonPreferences.getInstance(context, newName).putString(key, oldValue);
		}
	}

	public void putItem(String key, String id, String json) {
		LinkedHashMap<String, String> map = getItemList(key);

		if (map.containsKey(id)) {
//			map.remove(id);
		}
		map.put(id, json);

		putString(key, new Gson().toJson(map, getType()));
	}

	public boolean containsKey(String key, String id) {
		LinkedHashMap<String, String> map = getItemList(key);
		return map.containsKey(id);
	}

	public void removeItem(String key, String id) {
		LinkedHashMap<String, String> map = getItemList(key);

		if (map.containsKey(id)) {
			map.remove(id);
		}

		putString(key, new Gson().toJson(map, getType()));
	}

	public void removeAll(String key) {
		removeString(key);
	}

	public LinkedHashMap<String, String> getItemList(String key) {
		String mapJson = getString(key);

		LinkedHashMap<String, String> map = null;
		if (!mapJson.equals("")) {
			map = new Gson().fromJson(mapJson, getType());

		} else {
			map = new LinkedHashMap<String, String>();
		}
		return map;
	}

	private Type getType() {
		Type type = new TypeToken<LinkedHashMap<String, String>>(){}.getType();
		return type;
	}
}
