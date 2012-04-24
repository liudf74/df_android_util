package df.util.android;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-8
 * Time: 下午11:47
 * To change this template use File | Settings | File Templates.
 */
public class ResourceUtil {
    public static final String TAG = "df.util.ResourceUtil";

    // 根据名称获得资源ID
    public static int getStringResourceIdFromName(Context context, final String key) {
        Log.d(TAG, "get string identifier from resource, key = " + key);
        return context.getResources().getIdentifier(key, "string", ManifestUtil.getPackage(context));
    }

    public static String getStringResource(Context context, final String key) {
        int id = getStringResourceIdFromName(context, key);
        if (id > 0) {
            return context.getString(id);
        }
        return "";
    }

    // 根据名称获得资源ID
    public static int getLayoutResourceIdFromName(Context context, final String key) {
        Log.d(TAG, "get layout identifier from resource, key = " + key);
        return context.getResources().getIdentifier(key, "layout", ManifestUtil.getPackage(context));
    }

    // 根据名称获得资源ID
    public static int getDrawableResourceIdFromName(Context context, final String key) {
        Log.d(TAG, "get drawable identifier from resource, key = " + key);
        return context.getResources().getIdentifier(key, "drawable", ManifestUtil.getPackage(context));
    }

    // 根据名称获得资源ID
    public static int getIdResourceIdFromName(Context context, final String key) {
        Log.d(TAG, "get id identifier from resource, key = " + key);
        return context.getResources().getIdentifier(key, "id", ManifestUtil.getPackage(context));
    }

    // 根据名称获得资源ID
    public static int getArrayResourceIdFromName(Context context, final String key) {
        Log.d(TAG, "get array identifier from resource, key = " + key);
        return context.getResources().getIdentifier(key, "array", ManifestUtil.getPackage(context));
    }
}
