package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;
import com.uucun.adsdk.UUAppConnect;
import com.uucun.adsdk.UpdatePointListener;
import df.util.android.ApplicationUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: ÏÂÎç4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogamePcbCasee {
    private static final String TAG = "df.util.app.DooyogamePcbAppjoy";

    public static void showAbout(Context context) {

        Dialog d = new Dialog(context) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                dismiss();
                return true;
            }
        };
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(ResourceUtil.getLayoutResourceIdFromName(context, "information"));
        d.show();
    }

    public static void showGameHelp(Context context) {
        Dialog d = new Dialog(context) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                dismiss();
                return true;
            }
        };
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(ResourceUtil.getLayoutResourceIdFromName(context, "game_help"));
        d.show();
    }

    public static void exitGame(Context context) {
        ApplicationUtil.exitApp(context);
    }
}
