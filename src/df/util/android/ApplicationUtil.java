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
 * Time: ����12:39
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationUtil {

    public static final String TAG = "df.util.ApplicationUtil";

    // �˳�Ӧ�ó���
    public static void exitApp(Context context) {
        //�ֻ��汾
        Log.i(TAG, "exitApp: phone version=" + Build.VERSION.RELEASE);

        //ɱ����̨����
//        Intent i = new Intent();
//        i.setClass(this, QuweiService.class);
//        this.stopService(i);


        if (context instanceof Activity) {
            ((Activity) context).finish();
        }

        //ɱ��Application
        String packName = context.getPackageName();
        ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //todo: ��ҪȨ��<uses-permission android:name="android.permission.RESTART_PACKAGES" />
        activityMgr.restartPackage(packName);
        //todo: ��ҪȨ�� <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
        activityMgr.killBackgroundProcesses(packName);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
