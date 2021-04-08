package com.zb.mrseo.utility;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Prefs2 {

    private static SharedPreferences sharedPreferences = null;

    private static void openPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences("pharmaapp", context.MODE_PRIVATE);
    }

    public static void setValue(Context context, String key, String value) {
        Prefs2.openPrefs(context);
        SharedPreferences.Editor prefsEdit = Prefs2.sharedPreferences.edit();
        prefsEdit.putString(key, value);
        prefsEdit.commit();

        prefsEdit = null;
        Prefs2.sharedPreferences = null;
    }

    public static void setValue(Context context, String key, boolean value) {
        Prefs2.openPrefs(context);
        SharedPreferences.Editor prefsEdit = Prefs2.sharedPreferences.edit();
        prefsEdit.putBoolean(key, value);
        prefsEdit.commit();

        prefsEdit = null;
        Prefs2.sharedPreferences = null;
    }



    public static void setValue(Context context, String key, int value) {
        Prefs2.openPrefs(context);
        SharedPreferences.Editor prefsEdit = Prefs2.sharedPreferences.edit();
        prefsEdit.putInt(key, value);
        prefsEdit.commit();

        prefsEdit = null;
        Prefs2.sharedPreferences = null;
    }


    public static void setObject(Context context, String key, Object value) {
        Prefs2.openPrefs(context);
        SharedPreferences.Editor prefsEdit = Prefs2.sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        prefsEdit.putString(key, json);
        prefsEdit.commit();

    }

    public static String getValue(Context context, String key, String value) {
        Prefs2.openPrefs(context);
        String result = Prefs2.sharedPreferences.getString(key, value);
        Prefs2.sharedPreferences = null;
        return result;
    }

    public static int getValue(Context context, String key, int value) {
        Prefs2.openPrefs(context);
        int result = Prefs2.sharedPreferences.getInt(key, value);
        Prefs2.sharedPreferences = null;
        return result;
    }

    public static boolean getValue(Context context, String key, boolean value) {
        Prefs2.openPrefs(context);
        boolean result = Prefs2.sharedPreferences.getBoolean(key, value);
        Prefs2.sharedPreferences = null;
        return result;
    }


    public static Object getObject(Context context, String key, String value, Class va) {
        Gson gson = new Gson();
        Prefs2.openPrefs(context);
        String result = Prefs2.sharedPreferences.getString(key, value);
        return gson.fromJson(result, va);
    }

  /*  public static Object getOrderListObject(Context context, String key, String value) {
        Gson gson = new Gson();
        Prefs.openPrefs(context);
        String result = Prefs.sharedPreferences.getString(key, value);
        Type listType = new TypeToken<List<ProductListResponseModel.Datum>>(){}.getType();
        return gson.fromJson(result, listType);
    }*/

    public static void removeValue(Context context, String key) {
        Prefs2.openPrefs(context);
        SharedPreferences.Editor editor = Prefs2.sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clearAllData(Context context) {
        Prefs2.openPrefs(context);
        SharedPreferences.Editor editor = Prefs2.sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}

