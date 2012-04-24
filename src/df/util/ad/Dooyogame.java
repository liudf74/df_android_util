package df.util.ad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import df.util.android.ApplicationUtil;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12-3-12
 * Time: 下午6:04
 * To change this template use File | Settings | File Templates.
 */
public class Dooyogame {

    public static final String TAG = "df.util.Dooyogame";

    public static final String dooyogame_about_company =
            "开发商：深圳市骨鱼科技有限公司";
    public static final String dooyogame_about_tele =
            "";
    public static final String dooyogame_about_copyright =
            "";
    public static final String dooyogame_about_ok =
            "确定";
    public static final String dooyogame_about_title =
            "关于游戏";
    public static final String dooyogame_help_title =
            "游戏帮助";
    public static final String dooyogame_url_more =
            "";

    public static final String DELIM_LINE = "\n";

    public static final String ID_DOOYOGAME_ABOUT_BUTTON = "dooyogame_about_button";
    public static final String ID_DOOYOGAME_MORE_BUTTON = "dooyogame_more_button";
    public static final String ID_DOOYOGAME_QUIT_BUTTON = "dooyogame_quit_button";
    public static final String ID_DOOYOGAME_HELP_BUTTON = "dooyogame_help_button";


    //////////////////////////////////////////////////////////////
    // 菜单处理
    //////////////////////////////////////////////////////////////

    // “更多游戏”菜单
    public static void clickMenuMore(Context context) {
        Uri cmuri = Uri.parse(dooyogame_url_more);
        Intent returnIt = new Intent(Intent.ACTION_VIEW, cmuri);
        context.startActivity(returnIt);
    }

    // “关于游戏”菜单
    public static void clickMenuAbout(Context context) {
        clickMenuAbout(context, "");
    }

    // “关于游戏”菜单
    public static void clickMenuAbout(Context context, String product) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(dooyogame_about_title)
                .setMessage(product + DELIM_LINE
                        + dooyogame_about_company + DELIM_LINE
                        + dooyogame_about_tele + DELIM_LINE
                        + dooyogame_about_copyright)
                .setNeutralButton(dooyogame_about_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .create();
        dialog.show();
    }

    // “关于游戏”菜单
    public static void clickMenuAbout(Context context, String product, String other) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(dooyogame_about_title)
                .setMessage(product + DELIM_LINE + other)
                .setNeutralButton(dooyogame_about_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .create();
        dialog.show();
    }

    // “游戏帮助”菜单
    public static void clickMenuHelp(Context context, String help) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(dooyogame_help_title)
                .setMessage(help)
                .setNeutralButton(dooyogame_about_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .create();
        dialog.show();
    }

    // “退出游戏”菜单
    public static void clickMenuQuit(Context context) {
        ApplicationUtil.exitApp(context);
    }


}
