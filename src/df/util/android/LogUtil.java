package df.util.android;

import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12-3-20
 * Time: ÏÂÎç12:31
 * To change this template use File | Settings | File Templates.
 */
public class LogUtil {

    public static final String TAG = "df.util.LogUtil";

    public static final void log(int i0) {
        Log.d(TAG, "i0 = " + i0);
    }

    public static final void log(int i0, int i1) {
        Log.d(TAG, "i0 = " + i0 + ", i1 = " + i1);
    }

    public static final void log(String s0) {
        Log.d(TAG, "s0 = " + s0);
    }

    public static final void log(String s0, String s1) {
        Log.d(TAG, "s0 = " + s0 + ", s1 = " + s1);
    }


}
