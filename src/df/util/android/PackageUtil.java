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
 * Time: ����4:36
 * To change this template use File | Settings | File Templates.
 */
public class PackageUtil {

    public static final String TAG = "df.util.PackageUtil";

    // todo: ��û�а�װ������£����Ƿ���null��pm.getLaunchIntentForPackage(pi.packageName);
//    // ���apk�ļ�������activity��������ƣ�ͨ�������ƿ��Ի�ð����Լ�����
//    public static ComponentName getLaunchActivityInfoFromApkFile(final Context context, String apkFileName) {
//        PackageManager pm = context.getPackageManager();
//        PackageInfo pi = pm.getPackageArchiveInfo(apkFileName, PackageManager.GET_ACTIVITIES);
//        Intent launchIntent = pm.getLaunchIntentForPackage(pi.packageName);
//        return launchIntent.getComponent();
//    }
//

    // ��ð�װ�İ�������activity��������ƣ�ͨ�������ƿ��Ի�ð����Լ�����
    public static ComponentName getLaunchActivityInfoFromPackageName(final Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
        return launchIntent.getComponent();
    }

    // ��apk�ļ���ð���
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

    // ���ݰ����жϰ��Ƿ��Ѿ���װ
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

    // ����apk�ļ����жϰ��Ƿ��Ѿ���װ
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

    // apk�ļ��Ƿ�Ϸ�
    public static boolean isApkValid(final Context context, String apkFileName) {
        try {
            // ��֤�Ƿ�Ϊnull
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

    // apk�ļ��Ƿ�Ϸ���������������ǿգ�����Ҫ�ж��Ƿ����ָ���İ���ǰ׺��
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
