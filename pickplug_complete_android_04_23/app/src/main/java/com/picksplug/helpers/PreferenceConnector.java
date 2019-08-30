package com.picksplug.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

public class PreferenceConnector {
	public static final String 	PREF_NAME 					= 	"com.pickplug";
	public static final int 	MODE 						= 	Context.MODE_PRIVATE;

	// new added-

	public static final String TAG_IS_LOGIN        			=   "TAG_IS_LOGIN";
	public static final String TAG_IS_VERIFIED      		=   "TAG_IS_VERIFIED";
	public static final String TAG_USER_ID        			=   "TAG_USER_ID";
	public static final String TAG_USER_NAME      			=   "TAG_USER_NAME";
	public static final String TAG_USER_PROFILE      	 	=   "TAG_USER_PROFILE";
	public static final String TAG_USER_EMAIL         		=   "TAG_USER_EMAIL";
	public static final String KEY_PUSH_TOKEN         		=   "KEY_PUSH_TOKEN";
	public static final String KEY_TIME 					= 	"KEY_TIME";
	public static final String KEY_REQUEST_LOAD 			= 	"KEY_REQUEST_LOAD";
	public static final String RELOAD_SUBSCRIPTION 			= 	"RELOAD_SUBSCRIPTION";



	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}


	public static void writeString(Context context, String key, String string) {
		getEditor(context).putString(key, string).commit();
	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static void cleanPrefrences(Context context){
		getPreferences(context).edit().clear().commit();
	}

	public static Map<String, ?> getAllKeys(Context context) {
		Map<String, ?> map = getPreferences(context).getAll();
		return map;
	}
}
