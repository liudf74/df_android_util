package df.util.type;


import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: liudongfeng
 * Date: 2004-4-27
 * Time: 1:27:20
 * To change this template use File | Settings | File Templates.
 */
public class BooleanUtil {
    public static final String TAG = "df.util.BooleanUtil";

    ///////////////////////////////////////////////////////////////////

    public static boolean toBoolean(String str) {
        return toBoolean(str, false);
    }

    public static boolean toBoolean(String str, boolean defaultValue) {
        if (StringUtil.empty(str))
            return defaultValue;
        try {
            return toRawBoolean(str);
        } catch (Exception e) {
            Log.e(TAG, "toBoolean, failure, str = " + str, e);
            return defaultValue;
        }
    }

//    public static boolean toBooleanFromResource( String key, boolean defaultValue )
//    {
//        String str = ResourceUtil.getResourceString(key);
//        return toBoolean(str, defaultValue);
//    }

    public static boolean toRawBoolean(String str) throws Exception {
        return "true".equalsIgnoreCase(str.trim());
    }

}
