package df.util.app;

import android.content.Context;
import df.util.ad.Colorme;
import df.util.android.ResourceUtil;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-16
 * Time: 下午7:49
 * To change this template use File | Settings | File Templates.
 */
public class GameleaderSxddzColorme {
    // 神仙斗地主快乐米版本
    public static final String TAG = "df.util.GameleaderSxddzColorme";

    // 不同产品的名称
    public static final String colorme_sxddz_name = "名字：神仙斗地主";
    public static final String colorme_sxddz_version = "版本：V2.0";
    public static final String colorme_sxddz_catalog = "应用类型：棋牌类";
    // 厂家信息
    public static final String zosean_about_company =
            "开发商：北京中锐掌讯科技有限公司";
    public static final String zosean_about_tele =
            "客服电话：18618185892";
    public static final String zosean_about_copyright =
            "免责声明：" +
                    "本游戏的版权归“北京中锐掌讯科技有限公司”所有，" +
                    "游戏中的文字、图片等内容均为游戏版权所有者的个人" +
                    "态度或立场，中国电信对此不承担任何法律责任。";


    // 关于菜单
    public static void clickColormeMenuSxddzAbout(final Context context) {
        String product = colorme_sxddz_name + Colorme.DELIM_LINE
                + colorme_sxddz_version + Colorme.DELIM_LINE
                + colorme_sxddz_catalog;
        String other = zosean_about_company + Colorme.DELIM_LINE
                + zosean_about_tele + Colorme.DELIM_LINE
                + zosean_about_copyright;
        Colorme.clickMenuAbout(context, product, other);
    }

    // 更多菜单
    public static void clickColormeMenuSxddzMore(final Context context) {
        Colorme.clickMenuMore(context);
    }

    // 退出菜单
    public static void clickColormeMenuSxddzQuit(final Context context) {
        Colorme.clickMenuQuit(context);
    }

}
