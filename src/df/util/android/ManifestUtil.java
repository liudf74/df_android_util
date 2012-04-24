package df.util.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-11-2
 * Time: 上午10:21
 * To change this template use File | Settings | File Templates.
 */
public class ManifestUtil {

    public static final String TAG = "df.util.ManifestUtil";

    // 为了提高效率，初始化经常访问的变量
    private static boolean isInitialized = false;
    private static boolean isDebug;

    // 获得包名称
    public static String getPackage(Context context) {
        try {
            String packageName = context.getPackageName();
            return packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // 获得meta-data的数据
    public static String getMetadata(Context context, final String metadataName, final String defaultValue) {
        try {
            String packageName = context.getPackageName();
            PackageManager packagemanager = context.getPackageManager();
            ApplicationInfo ai = packagemanager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null) {
                Bundle bundle = ai.metaData;
                if (bundle != null) {
                    String value = bundle.getString(metadataName);
                    Log.i(TAG, "meta-data: " + metadataName + " = " + value);
                    return value;
                }
            } else {
                Log.e(TAG, "meta-data [ " + metadataName + " ] is null, appInfo = " + ai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static int getMetadataAsInt(Context context, final String metadataName, final int defaultValue) {
        try {
            String packageName = context.getPackageName();
            PackageManager packagemanager = context.getPackageManager();
            ApplicationInfo ai = packagemanager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null) {
                Bundle bundle = ai.metaData;
                if (bundle != null) {
                    int value = bundle.getInt(metadataName, defaultValue);
                    Log.i(TAG, "meta-data: " + metadataName + " = " + value);
                    return value;
                }
            } else {
                Log.e(TAG, "meta-data [ " + metadataName + " ] is null, appInfo = " + ai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static boolean getMetadataAsBoolean(Context context, final String metadataName, final boolean defaultValue) {
        try {
            String packageName = context.getPackageName();
            PackageManager packagemanager = context.getPackageManager();
            ApplicationInfo ai = packagemanager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null) {
                Bundle bundle = ai.metaData;
                if (bundle != null) {
                    boolean value = bundle.getBoolean(metadataName, defaultValue);
                    Log.i(TAG, "meta-data: " + metadataName + " = " + value);
                    return value;
                }
            } else {
                Log.e(TAG, "meta-data [ " + metadataName + " ] is null, appInfo = " + ai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static boolean isDebug(Context context) {
        if (isInitialized) {
            return isDebug;
        }
        isDebug = getMetadataAsBoolean(context, "debug", false);
        isInitialized = true;
        return isDebug;
    }
}
