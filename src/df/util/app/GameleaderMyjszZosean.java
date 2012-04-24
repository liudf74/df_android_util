package df.util.app;

import android.content.Context;
import df.util.ad.Colorme;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-16
 * Time: 下午7:49
 * To change this template use File | Settings | File Templates.
 */
public class GameleaderMyjszZosean {
    // 玛雅巨石阵中锐掌讯版本
    public static final String TAG = "df.util.GameleaderMyjszZosean";

    // 不同产品的名称
    public static final String colorme_myjsz_name = "名字：玛雅巨石阵";
    public static final String colorme_myjsz_version = "版本：V2.0";
    public static final String colorme_myjsz_catalog = "应用类型：益智类";
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
    public static void clickMenuAbout(final Context context) {
        String product = colorme_myjsz_name + Colorme.DELIM_LINE
                + colorme_myjsz_version + Colorme.DELIM_LINE
                + colorme_myjsz_catalog;
        String other = zosean_about_company + Colorme.DELIM_LINE
                + zosean_about_tele + Colorme.DELIM_LINE
                + zosean_about_copyright;
        Colorme.clickMenuAbout(context, product, other);
    }

    // 更多菜单
    public static void clickMenuMore(final Context context) {
        Colorme.clickMenuMore(context);
    }

    // 退出菜单
    public static void clickMenuQuit(final Context context) {
        Colorme.clickMenuQuit(context);
    }

}
