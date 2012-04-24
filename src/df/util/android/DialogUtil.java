package df.util.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-11-2
 * Time: 上午10:46
 * To change this template use File | Settings | File Templates.
 */
public class DialogUtil {

    public static final String TAG = "df.util.DialogUtil";

    // 显示单条消息
//    public static void showMessageDialog(final Context context, final int messageResId) {
//        final AlertDialog dialog = new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setMessage(context.getString(messageResId))
//                .setNeutralButton(df.util.android.SmsUtil.STR_SMS_BUTTON_CONFIRM, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                }).create();
//        dialog.show();
//    }

//    public static void showMessageDialog(final Context context,
//                                         final DialogInterface.OnClickListener yesListener,
//                                         final DialogInterface.OnClickListener noListener
//                                         ) {
//        final AlertDialog dialog = new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setTitle(context.getString(STR_SMS_HINT_TITLE))
//                .setMessage(context.getString(STR_SMS_HINT_MESSAGE))
//                .setNegativeButton(context.getString(R.string.button_yes), yesListener)
//                .setPositiveButton(context.getString(R.string.button_no), noListener)
//                .create();
//        dialog.show();
//    }

//    public static void showMessageDialog(final Context context,
//                                         final DialogInterface.OnClickListener okListener
//                                         ) {
//        final AlertDialog dialog = new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setMessage(context.getString(R.string.pay_prompt))
//                .setNeutralButton(context.getString(R.string.button_yes), okListener)
//                .create();
//        dialog.show();
//    }

}
