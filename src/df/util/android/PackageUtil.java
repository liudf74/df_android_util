package df.util.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import df.util.type.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12-2-29
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public class PackageUtil {

    public static final String TAG = "df.util.PackageUtil";

    // todo: 在没有安装的情况下，总是返回null：pm.getLaunchIntentForPackage(pi.packageName);
//    // 获得apk文件中启动activity的组合名称，通过该名称可以获得包名以及类名
//    public static ComponentName getLaunchActivityInfoFromApkFile(final Context context, String apkFileName) {
//        PackageManager pm = context.getPackageManager();
//        PackageInfo pi = pm.getPackageArchiveInfo(apkFileName, PackageManager.GET_ACTIVITIES);
//        Intent launchIntent = pm.getLaunchIntentForPackage(pi.packageName);
//        return launchIntent.getComponent();
//    }
//

    // 获得安装的包中启动activity的组合名称，通过该名称可以获得包名以及类名
    public static ComponentName getLaunchActivityInfoFromPackageName(final Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
        return launchIntent.getComponent();
    }

    // 从apk文件获得包名
    public static String getPackageNameFromApkFile(final Context context, String apkFileName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkFileName, PackageManager.GET_ACTIVITIES);
            return pi.packageName;
        } catch (Exception e) {
            Log.e(TAG, "getPackageNameFromApkFile failure", e);
            return "";
        }
    }

    // 根据包名判断包是否已经安装
    public static boolean isPackageInstalled(final Context context, String packageName) {
        boolean isInstalled = false;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            Log.d(TAG, "package installed" +
                    ", packageName = " + pi.packageName +
                    ", versionName = " + pi.versionName +
                    ", versionCode = " + pi.versionCode);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
            Log.d(TAG, "package no installed yet, packageName = " + packageName, e);
        } catch (Exception e) {
            isInstalled = false;
            Log.d(TAG, "package query failure, packageName = " + packageName, e);
        }
        return isInstalled;
    }

    // 根据apk文件中判断包是否已经安装
    public static boolean isPackageInstalledFromApkFile(final Context context, String apkFileName) {
        boolean isInstalled = false;
        String packageName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkFileName, PackageManager.GET_ACTIVITIES);
            packageName = pi.packageName;
            pi = pm.getPackageInfo(packageName, 0);
            Log.d(TAG, "package installed" +
                    ", packageName = " + pi.packageName +
                    ", versionName = " + pi.versionName +
                    ", versionCode = " + pi.versionCode);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
            Log.d(TAG, "package no installed yet, packageName = " + packageName, e);
        } catch (Exception e) {
            isInstalled = false;
            Log.d(TAG, "package query failure, packageName = " + packageName, e);
        }
        return isInstalled;
    }

    // apk文件是否合法
    public static boolean isApkValid(final Context context, String apkFileName) {
        try {
            // 验证是否为null
            String packageName = getPackageNameFromApkFile(context, apkFileName);
            if (!StringUtil.empty(packageName)) {
                Log.d(TAG, "apk is valid, apkFileName = " + apkFileName + ", packageName = " + packageName);
                return true;
            }
        } catch (Exception e) {
            Log.d(TAG, "apk is valid, apkFileName = " + apkFileName, e);
        }
        return false;
    }

    // apk文件是否合法（如果参数包名非空，则还需要判断是否具有指定的包名前缀）
    public static boolean isApkValidWithPackageNamePrefix(final Context context, String apkFileName, String prefixPackageName) {
        try {
            String packageName = getPackageNameFromApkFile(context, apkFileName);
            if (!StringUtil.empty(packageName)) {
                if (StringUtil.empty(prefixPackageName)) {
                    Log.d(TAG, "apk is valid without judge prefixPackageName" +
                            ", apkFileName = " + apkFileName + ", packageName = " + packageName + ", prefixPackageName = " + prefixPackageName);
                    return true;
                } else if (StringUtil.contains(packageName, prefixPackageName)) {
                    Log.d(TAG, "apk is valid with assigned prefixPackageName" +
                            ", apkFileName = " + apkFileName + ", packageName = " + packageName + ", prefixPackageName = " + prefixPackageName);
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "apk is invalid, apkFileName = " + apkFileName, e);
        }
        return false;
    }

}
