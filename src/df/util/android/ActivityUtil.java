package df.util.android;

import android.content.Context;
import android.content.Intent;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-6
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class ActivityUtil {

    // 退出游戏
    public static void quitActivity(final Context context) {

        ApplicationUtil.exitApp(context);

//        // 主桌面
//        Intent intent = new Intent("android.intent.action.MAIN");
//        intent.addCategory("android.intent.category.HOME");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//
//        // 退出
//        System.exit(0);
    }
}
