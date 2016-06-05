package com.ppma.content;

import android.content.Context;

public class ResourceUtils {

	public static boolean getBoolean(Context context, String name) {
		int id = getIdentifier(context, name, "bool", context.getPackageName());
		if (id == 0) {
            throw new NullPointerException(name + " not found");
		}
		return context.getResources().getBoolean(id);
	}
	
	public static int getIdentifier(Context context, String name, String defType, String defPackage) {
		int id = context.getResources().getIdentifier(name, defType, context.getPackageName());
		return id;
	}
}
