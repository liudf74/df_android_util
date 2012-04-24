package df.util.ad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import df.util.android.ManifestUtil;
import df.util.android.ResourceUtil;

/**
 * 北京中锐掌讯科技有限公司
 */
public class DooyogameSplashActivity extends Activity {

    public static final String TAG = "df.util.DooyogameSplashActivity";

    //
    public static final String KEY_TYPE = "type";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Context context = this.getApplicationContext();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SplashHandler mHandler = new SplashHandler();
        final int layoutId = ResourceUtil.getLayoutResourceIdFromName(context, "dooyogame_splash");
        setContentView(layoutId);

        final int viewId = ResourceUtil.getIdResourceIdFromName(context, "dooyogame_drawable");
        View view = findViewById(viewId);

        final int bgId = ResourceUtil.getDrawableResourceIdFromName(context, "dooyogame_splash");
        view.setBackgroundResource(bgId);

        Message msg = new Message();
        msg.what = 0;
        mHandler.sendMessageDelayed(msg, 3000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        Log.d(TAG, "onDestroy()");
        System.gc();
    }

    // Handler class implementation to handle the message
    private class SplashHandler extends Handler {
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            switch (msg.what) {
                default:
                case 0:
                    super.handleMessage(msg);

                    intent.setClass(DooyogameSplashActivity.this,
                            getMainActivityClass(DooyogameSplashActivity.this.getApplicationContext()));
                    startActivity(intent);

                    DooyogameSplashActivity.this.finish();
                    break;
            }
        }
    }


    // 获取真正执行的主应用的类
    public static Class getMainActivityClass(Context context) {
        try {
            return Class.forName(ManifestUtil.getMetadata(context, "DF_DOOYOGAME_MAINACTIVITY", ""));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "init main activity class failure, check " + "DF_DOOYOGAME_MAINACTIVITY");
            System.exit(-1);
        }
        return null;
    }


}