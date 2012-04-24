package df.util.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import cn.casee.adsdk.CaseeAdView;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameWmmtCasee {

    public static void addCaseeViewAD(Activity activity) {
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);

        CaseeAdView cav = new CaseeAdView(activity, "C20A6A1E090E0215F5BE55F5FAE91A62",
                false, 15 * 1000,//正式发布为false
                Color.BLACK, Color.WHITE, false);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(cav, p);
        activity.addContentView(relativeLayout, params);
    }
}
