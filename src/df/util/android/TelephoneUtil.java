package df.util.android;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12-2-28
 * Time: 下午1:18
 * To change this template use File | Settings | File Templates.
 */
// 需要权限：<uses-permission android:name="android.permission.READ_PHONE_STATE" />
public class TelephoneUtil {

    public static final String TAG = "df.util.TelephoneUtil";

    public static String toImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            Log.e(TAG, "get imei failure", e);
            return "";
        }
    }

    public static String toImsi(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimSerialNumber();
        } catch (Exception e) {
            Log.e(TAG, "get imsi failure", e);
            return "";
        }
    }

    // 获取客户id，在gsm中是imsi号
    public static String toSubId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        } catch (Exception e) {
            Log.e(TAG, "get subid failure", e);
            return "";
        }
    }

    // 不一定能得到
    public static String toMsisdn(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        } catch (Exception e) {
            Log.e(TAG, "get msisdn failure", e);
            return "";
        }
    }

}
