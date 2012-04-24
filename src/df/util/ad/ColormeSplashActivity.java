package df.util.ad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import df.util.android.ResourceUtil;

/**
 * 快乐米界面
 */
public class ColormeSplashActivity extends Activity {

    public static final String TAG = "df.util.ColormeSplashActivity";

    //
    public static final String KEY_TYPE = "type";
    public static final String VALUE_JYZA = "jyza";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Context context = this.getApplicationContext();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SplashHandler mHandler = new SplashHandler();
        final int layoutId = ResourceUtil.getLayoutResourceIdFromName(context, Colorme.LAYOUT_COLORME_AIYOUXI_SPLASH);
        setContentView(layoutId);

        final int viewId = ResourceUtil.getIdResourceIdFromName(context, Colorme.ID_COLORME_AIYOUXI_DRAWABLE);
        View view = findViewById(viewId);

        int what = 0;
        Intent intent = this.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String type = bundle.getString(KEY_TYPE);
                if (VALUE_JYZA.equals(type)) {
                    final int bgId = ResourceUtil.getDrawableResourceIdFromName(context, Colorme.DRAWABLE_COLORME_JYZA);
                    view.setBackgroundResource(bgId);
                    what = 1;
                }
            }
        }
        if (what == 0) {
            final int bgId = ResourceUtil.getDrawableResourceIdFromName(context, Colorme.DRAWABLE_COLORME_AIYOUXI);
            view.setBackgroundResource(bgId);
        }

        Message msg = new Message();
        msg.what = what;
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

                    intent.setClass(ColormeSplashActivity.this,
                            ColormeSplashActivity.class);
                    intent.putExtra(KEY_TYPE, VALUE_JYZA);
                    startActivity(intent);

                    ColormeSplashActivity.this.finish();
                    break;

                case 1:
                    super.handleMessage(msg);

                    intent.setClass(ColormeSplashActivity.this,
                            Colorme.getMainActivityClass(ColormeSplashActivity.this.getApplicationContext()));
                    startActivity(intent);

                    ColormeSplashActivity.this.finish();
                    break;
            }
        }
    }


}