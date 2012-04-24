package df.util.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import df.util.ad.Colorme;
import df.util.android.PackageUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.type.StringUtil;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-16
 * Time: 下午7:49
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMonsterfaceColormeCt {
    // 怪脸涂装 快乐米 电信 版本
    public static final String TAG = "df.util.DooyogameMonsterfaceColormeCt";

    // 不同产品的名称
    public static final String product_name = "名字：怪脸涂装";
    public static final String product_version = "版本：V2.0";
    public static final String product_catalog = "应用类型：其他";
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
            "http://wapgame.189.cn/hd/qs";
    public static final String product_weibo = "你一闪而过，令我顿时迷失自我，望着你的照片真想把你留住，" +
            "我大声告诉自己不能让你走，于是我对着你的照片大叫到：你也有今天啊！" +
            "http://game.189.cn/hd/2012qs";

    // 保存和分享共计3次以后开始计费，
    // 资费3元（注意代码中的数值要跟着金额走）。
    // 代码：0311C0905611022100548511022100528201MC099146000000000000000000000000
    public static final String theCountPaySmsAddress = "10659811003";
    public static final String theCountPaySmsContent = "0311C0905611022100548511022100528201MC099146000000000000000000000000";

    // 游戏默认打开十个涂鸦道具，其它的道具不能使用，客户点击未开放的涂鸦工具后弹出计费，需要购买，
    // 资费1元（注意代码中的数值要跟着金额走），购买后全部可以使用。
    // 代码：0111C0905611022100548511022100528301MC099146000000000000000000000000
    public static final String theItemPaySmsAddress = "10659811001";
    public static final String theItemPaySmsContent = "0111C0905611022100548511022100528301MC099146000000000000000000000000";

    // 免费道具名称
    public static final String[] theFreeItemArray = {
            "spcie_body_03",
            "spcie_body_03_icon",
            "spcie_body_10",
            "spcie_body_10_icon",
            "spcie_body_14",
            "spcie_body_14_icon",
            "spcie_body_05",
            "spcie_body_05_icon",
            "spcie_mask_09",
            "spcie_mask_09_icon",
            "spcie_text_01",
            "spcie_text_01_icon",
            "spcie_token_18",
            "spcie_token_18_icon",
            "spcie_token_16",
            "spcie_token_16_icon",
            "spcie_weapon_05",
            "spcie_weapon_05_icon",
            "spcie_text_07",
    };
    public static int[] theFreeItemResourceIdArray;
    // 加锁的图标，提示用户购买
    public static final String theLockItem = "pay_icon";
    // 减少内存使用
//    public static Drawable                theLockDrawable;
    public static TypedArray theLockTypedArray;
    public static SoftReference<Drawable> theLockReference;


    // 是否是colorme的特定菜单？如果是则执行之
    public static boolean isProductMenu(final Context context, final int viewId) {

        final int aboutId = ResourceUtil.getIdResourceIdFromName(context, Colorme.ID_COLORME_ABOUT_BUTTON);
        if (viewId == aboutId) {
            String product = product_name + Colorme.DELIM_LINE
                    + product_version + Colorme.DELIM_LINE
                    + product_catalog;
            Colorme.clickMenuAbout(context, product);
            return true;
        }

        final int moreId = ResourceUtil.getIdResourceIdFromName(context, Colorme.ID_COLORME_MORE_BUTTON);
        if (viewId == moreId) {
            Colorme.clickMenuMore(context);
            return true;
        }

        final int quitId = ResourceUtil.getIdResourceIdFromName(context, Colorme.ID_COLORME_QUIT_BUTTON);
        if (viewId == quitId) {
            Colorme.clickMenuQuit(context);
            return true;
        }

        final int helpId = ResourceUtil.getIdResourceIdFromName(context, Colorme.ID_COLORME_HELP_BUTTON);
        if (viewId == helpId) {
            Colorme.clickMenuHelp(context, product_help);
            return true;
        }

        return false;
    }

    ////////////////////////////////////////////////////////////
    // 计费相关（免费次数）
    // 保存和分享共计3次以后开始计费，
    // 资费3元（注意代码中的数值要跟着金额走）。

    public static void initPayDataOfFreeCount(Context context) {
        Colorme.initPayDataOfFreeCount(context, theCountPaySmsAddress, theCountPaySmsContent, 3, 3);
    }

    public static boolean canRunOfFreeCount(final Context context) {
        // 更改当前已经使用的次数（对于保存和分享动作来说）
        int currentCount = PreferenceUtil.readRecord(context, Colorme.KEY_COLORME_FREE_COUNT_CURRENT_COUNT, -1);
        currentCount++;
        PreferenceUtil.saveRecord(context, Colorme.KEY_COLORME_FREE_COUNT_CURRENT_COUNT, currentCount);

        return Colorme.canRunOfFreeCount(context, currentCount);
    }

    ////////////////////////////////////////////////////////////
    // 计费相关（免费道具）
    // 游戏默认打开十个涂鸦道具，其它的道具不能使用，客户点击未开放的涂鸦工具后弹出计费，需要购买，
    // 资费1元（注意代码中的数值要跟着金额走），购买后全部可以使用。

    public static void initPayDataOfFreeItem(Context context) {
        // 初始化免费道具的资源ID（包括大图和小图）
        theFreeItemResourceIdArray = new int[theFreeItemArray.length];
        for (int i = 0; i < theFreeItemArray.length; i++) {
            String name = theFreeItemArray[i];
            int spiceResourceId = ResourceUtil.getDrawableResourceIdFromName(context, name);
            theFreeItemResourceIdArray[i] = spiceResourceId;
        }

        Colorme.initPayDataOfFreeItem(context, theItemPaySmsAddress, theItemPaySmsContent, theFreeItemResourceIdArray, 1);
    }

    public static boolean canUseItem(final Context context, int currentItemResourceId) {
        return Colorme.canRunOfFreeItem(context, currentItemResourceId, true);
    }

    public static boolean isItemFree(final Context context, int currentItemResourceId) {
        return Colorme.canRunOfFreeItem(context, currentItemResourceId, false);
    }

    public static synchronized Drawable toLockItemDrawable(Context context) {
        if (theLockReference == null) {
            int res = ResourceUtil.getArrayResourceIdFromName(context, "spice_lock");
            theLockTypedArray = context.getResources().obtainTypedArray(res);
            Drawable d = theLockTypedArray.getDrawable(0);
            theLockReference = new SoftReference<Drawable>(d);
        }
        return theLockReference.get();
    }


    public static final String toSendSmsText() {
        return product_sms;
    }

    public static final String toSendWeiboText() {
        return product_weibo;
    }

    // 电信要求短信和微博的分享内容不能相同，我们只好将短信和其他内容分割出来
    public static void showShareChooser(final Context context,
                                        final Intent contentIntent,
                                        final String title,
                                        final Uri imageUrl) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("分享方式")
                .setMessage("想直接通过彩信分享给好友么？\n发送彩信正常收取通信费，不收取其他费用。")
                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // todo: 下面的方法没有正式的文档，不知道是否所有android手机都支持

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
        // todo：保证在各个版本的android上，都支持彩信发送
        contentIntent.putExtra("sms_body", toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_SUBJECT, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_TEXT, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_TITLE, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_STREAM, imageUrl);

        contentIntent.setClassName(mmsPackageName, mmsClassName);
        context.startActivity(contentIntent);
    }

    private static void sendImageUsingOtherApp(Intent contentIntent, String title, Context context) {

        contentIntent.putExtra("sms_body", toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_SUBJECT, toSendWeiboText());

        contentIntent.putExtra(Intent.EXTRA_TEXT, toSendWeiboText());
        Intent chooseIntent = Intent.createChooser(contentIntent, title);
        context.startActivity(chooseIntent);
    }


}
