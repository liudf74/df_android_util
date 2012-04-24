package df.util.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-11-2
 * Time: ÉÏÎç10:44
 * To change this template use File | Settings | File Templates.
 */
public class PreferenceUtil {

    public static final String TAG = "df.util.PreferenceUtil";

    public static HashMap<String, Boolean> theLogShownMap = new HashMap<String, Boolean>();


    public static void logOnce(String msg) {
        if (theLogShownMap.get(msg) != null) {
            theLogShownMap.put(msg, true);
            Log.d(TAG, msg);
        }
    }

    public static void saveRecord(Context activity, final String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        prefs.edit().putString(key, value).commit();
        logOnce("saveRecord, " + key + " = " + value);
    }

    public static void saveRecord(Context activity, final String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        prefs.edit().putInt(key, value).commit();
        logOnce("saveRecord, " + key + " = " + value);
    }

    public static void saveRecord(Context activity, final String key, float value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        prefs.edit().putFloat(key, value).commit();
        logOnce("saveRecord, " + key + " = " + value);
    }

    public static void saveRecord(Context activity, final String key, long value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        prefs.edit().putLong(key, value).commit();
        logOnce("saveRecord, " + key + " = " + value);
    }

    public static void saveRecord(Context activity, final String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        prefs.edit().putBoolean(key, value).commit();
        logOnce("saveRecord, " + key + " = " + value);
    }

    public static String readRecord(Context activity, final String key, final String defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String value = prefs.getString(key, defaultValue);
        logOnce("readRecord, " + key + " = " + value);
        return value;
    }

    public static int readRecord(Context activity, final String key, final int defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        int value = prefs.getInt(key, defaultValue);
        logOnce("readRecord, " + key + " = " + value);
        return value;
    }

    public static float readRecord(Context activity, final String key, final float defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        float value = prefs.getFloat(key, defaultValue);
        logOnce("readRecord, " + key + " = " + value);
        return value;
    }

    public static long readRecord(Context activity, final String key, final long defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        long value = prefs.getLong(key, defaultValue);
        logOnce("readRecord, " + key + " = " + value);
        return value;
    }

    public static boolean readRecord(Context activity, final String key, final boolean defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean value = prefs.getBoolean(key, defaultValue);
        logOnce("readRecord, " + key + " = " + value);
        return value;
    }

}
