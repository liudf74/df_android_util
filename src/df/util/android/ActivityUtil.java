package df.util.android;

import android.content.Context;
import android.content.Intent;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-6
 * Time: ����2:50
 * To change this template use File | Settings | File Templates.
 */
public class ActivityUtil {

    // �˳���Ϸ
    public static void quitActivity(final Context context) {

        ApplicationUtil.exitApp(context);

//        // ������
//        Intent intent = new Intent("android.intent.action.MAIN");
//        intent.addCategory("android.intent.category.HOME");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//
//        // �˳�
//        System.exit(0);
    }
}
