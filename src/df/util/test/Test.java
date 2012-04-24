package df.util.test;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import df.util.app.GameleaderSxddzYeahyoo;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-11-4
 * Time: ÏÂÎç4:28
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String agrs[]) {
        int x = 0;
        switch (x) {
            case 0:
                System.out.println("p....1");
                System.out.println("p....2");
                break;
        }
    }


    private long m = 0;
    private int i = 0;

    private void test() {

        View v = null;
        v.invalidate();
        v.getWidth();

        long x = SystemClock.uptimeMillis();
        if (m == 0) {
            m = x;
        }
        if (x - m >= 300000) {
            Log.d("1", "");
            i = 1;
            m = x;
        }

    }

    private void test2() {
        H h = new H();
        h.sendEmptyMessage(0);
    }

    class H extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);    //To change body of overridden methods use File | Settings | File Templates.

            int x = msg.what;
        }
    }

    public static void testYeahyoo(int x, int y) {
        GameleaderSxddzYeahyoo.clickHintOfSelectAvatar(null, 0, 0, 0, x, y);
    }

    public static final void testAndroid() {
        ProgressDialog.show(null, "", "", false);

//        BitmapFactory.decodeResource(context, R.drawable.colorme_aiyouxi);
//        Drawable d = new BitmapDrawable(R.drawable.colorme_aiyouxi);
    }
}
