package df.util.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12-3-1
 * Time: 下午12:39
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationUtil {

    public static final String TAG = "df.util.ApplicationUtil";

    // 退出应用程序
    public static void exitApp(Context context) {
        //手机版本
        Log.i(TAG, "exitApp: phone version=" + Build.VERSION.RELEASE);

        //杀死后台服务
//        Intent i = new Intent();
//        i.setClass(this, QuweiService.class);
//        this.stopService(i);


        if (context instanceof Activity) {
            ((Activity) context).finish();
        }

        //杀死Application
        String packName = context.getPackageName();
        ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //todo: 需要权限<uses-permission android:name="android.permission.RESTART_PACKAGES" />
        activityMgr.restartPackage(packName);
        //todo: 需要权限 <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
        activityMgr.killBackgroundProcesses(packName);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
