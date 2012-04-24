package df.util.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import df.util.ad.Dooyogame;
import df.util.android.ResourceUtil;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-16
 * Time: 下午7:49
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMonsterfaceMaya {
    // 怪脸涂装 莱科 版本
    public static final String TAG = "df.util.DooyogameMonsterfaceMaya";

    // 不同产品的名称
    public static final String product_name = "名字：怪脸涂装";
    public static final String product_version = "版本：V2.0";
    public static final String product_catalog = "";
    public static final String product_help = "整蛊、搞笑为主题的涂鸦游戏，分为2种模式。\n" +
            "即拍即用模式：\n" +
            "游戏内您可以随心所欲拍摄一张照片（人物、风景等），通过游戏内的道具进行个性化的涂鸦。" +
            "满足您YY的需求。\n" +
            "已有图片涂鸦模式：\n" +
            "游戏内您可以使用手机图库，挑选一张您需要使用的图片，通过游戏内的道具进行个性化的涂鸦。" +
            "可为喜欢的图片都留下记号哦！\n" +
            " \n" +
            "涂鸦后的照片您可以保持到本地收藏并可通过彩信、邮件、微博等方式与您的亲人、敌人、好友、老婆们一起分享。";
    public static final String product_sms = "你一闪而过，令我顿时迷失自我，望着你的照片真想把你留住，" +
            "我大声告诉自己不能让你走，于是我对着你的照片大叫到：你也有今天啊！ " +
            "";
    public static final String product_weibo = "你一闪而过，令我顿时迷失自我，望着你的照片真想把你留住，" +
            "我大声告诉自己不能让你走，于是我对着你的照片大叫到：你也有今天啊！" +
            "";

    // 是否是特定菜单？如果是则执行之
    public static boolean isProductMenu(final Context context, final int viewId) {

        final int aboutId = ResourceUtil.getIdResourceIdFromName(context, Dooyogame.ID_DOOYOGAME_ABOUT_BUTTON);
        if (viewId == aboutId) {
            String product = product_name + Dooyogame.DELIM_LINE
                    + product_version + Dooyogame.DELIM_LINE
                    + product_catalog;
            Dooyogame.clickMenuAbout(context, product);
            return true;
        }

        final int moreId = ResourceUtil.getIdResourceIdFromName(context, Dooyogame.ID_DOOYOGAME_MORE_BUTTON);
        if (viewId == moreId) {
            Dooyogame.clickMenuMore(context);
            return true;
        }

        final int quitId = ResourceUtil.getIdResourceIdFromName(context, Dooyogame.ID_DOOYOGAME_QUIT_BUTTON);
        if (viewId == quitId) {
            Dooyogame.clickMenuQuit(context);
            return true;
        }

        final int helpId = ResourceUtil.getIdResourceIdFromName(context, Dooyogame.ID_DOOYOGAME_HELP_BUTTON);
        if (viewId == helpId) {
            Dooyogame.clickMenuHelp(context, product_help);
            return true;
        }

        return false;
    }

    // 发送短信和微博
    public static final String toSendSmsText() {
        return product_sms;
    }

    public static final String toSendWeiboText() {
        return product_weibo;
    }

    // 短信和微博的分享内容不能相同，我们只好将短信和其他内容分割出来
    public static void showShareChooser(final Context context,
                                        final Intent contentIntent,
                                        final String title,
                                        final Uri imageUrl) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("分享方式")
                .setMessage("想直接通过彩信分享给好友么？")
                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // todo: 下面的方法没有正式的文档，不知道是否所有android手机都支持
//                        contentIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
//                        contentIntent.setData(null);
//                        contentIntent.putExtra("sms_body", toSendSmsText());
//                        contentIntent.putExtra(Intent.EXTRA_STREAM, imageUrl);
//                        contentIntent.setType("image/jpeg");

                        boolean findMms = false;
                        String mmsPackageName = "";
                        String mmsClassName = "";
                        contentIntent.putExtra("sms_body", toSendSmsText());
                        contentIntent.putExtra(Intent.EXTRA_TEXT, toSendSmsText());
                        PackageManager pm = context.getPackageManager();
                        List<ResolveInfo> riList = pm.queryIntentActivities(contentIntent, 0);
                        for (int j = 0; j < riList.size(); j++) {
                            ResolveInfo ri = riList.get(j);
                            ActivityInfo ai = ri.activityInfo;
                            if (ai != null) {
                                Log.d(TAG, "queryIntentActivities, packageName = " + ai.packageName + ", className = " + ai.name);
                                mmsPackageName = ai.packageName;
                                mmsClassName = ai.name;
                                if (mmsPackageName.startsWith("com.android.mms")
                                        ) { // && mmsClassName.contains("Compose")) {
                                    findMms = true;
                                    break;
                                }
                            }
                        }

                        if (findMms) {
                            Log.d(TAG, "find the mms compose activity, packageName = " + mmsPackageName + ", mmsClassName = " + mmsClassName);
                            sendImageUsingSmsApp(mmsPackageName, mmsClassName, contentIntent, context, imageUrl);
                        } else {
                            Log.e(TAG, "cannot find the mms compose activity, let user choose");
                            sendImageUsingOtherApp(contentIntent, title, context);
                        }
                    }
                })
                .setNegativeButton("选择其他方式", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendImageUsingOtherApp(contentIntent, title, context);
                    }
                })
                .create();
        dialog.show();
    }

    private static void sendImageUsingSmsApp(String mmsPackageName, String mmsClassName, Intent contentIntent, Context context, Uri imageUrl) {
        // 指定发送彩信
        contentIntent.putExtra("sms_body", toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_SUBJECT, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_TEXT, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_TITLE, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_STREAM, imageUrl);

        contentIntent.setClassName(mmsPackageName, mmsClassName);
        context.startActivity(contentIntent);
    }

    private static void sendImageUsingOtherApp(Intent contentIntent, String title, Context context) {
        // 在通过选择方式发送短信时，也能带上内容
        contentIntent.putExtra("sms_body", toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_SUBJECT, toSendWeiboText());

        contentIntent.putExtra(Intent.EXTRA_TEXT, toSendWeiboText());
        Intent chooseIntent = Intent.createChooser(contentIntent, title);
        context.startActivity(chooseIntent);
    }


}
