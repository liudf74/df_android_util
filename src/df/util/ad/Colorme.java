package df.util.ad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import dalvik.system.VMRuntime;
import df.util.android.ApplicationUtil;
import df.util.android.ManifestUtil;
import df.util.android.PreferenceUtil;
import df.util.android.SmsUtil;


/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-8
 * Time: 下午11:04
 * To change this template use File | Settings | File Templates.
 */
public class Colorme {

    public static final String TAG = "df.util.Colorme";

    // 通用定义
    public static final String colorme_about_company =
            "开发商：北京聚亚中奥科技有限公司";
    public static final String colorme_about_tele =
            "客服电话：4008105130";
    public static final String colorme_about_copyright =
            "免责声明：" +
                    "本游戏的版权归“北京聚亚中奥科技有限公司”所有，" +
                    "游戏中的文字、图片等内容均为游戏版权所有者的个人" +
                    "态度或立场，中国电信对此不承担任何法律责任。";
    public static final String colorme_about_ok =
            "确定";
    public static final String colorme_about_title =
            "关于游戏";
    public static final String colorme_help_title =
            "游戏帮助";
    public static final String colorme_url_more =
            "http://wapgame.189.cn";

    //
    public static final String LAYOUT_COLORME_AIYOUXI_SPLASH = "colorme_aiyouxi_splash";
    //
    public static final String ID_COLORME_AIYOUXI_DRAWABLE = "colorme_aiyouxi_drawable";
    public static final String ID_COLORME_ABOUT_BUTTON = "colorme_about_button";
    public static final String ID_COLORME_MORE_BUTTON = "colorme_more_button";
    public static final String ID_COLORME_QUIT_BUTTON = "colorme_quit_button";
    public static final String ID_COLORME_HELP_BUTTON = "colorme_help_button";
    //
    public static final String DRAWABLE_COLORME_AIYOUXI = "colorme_aiyouxi";
    public static final String DRAWABLE_COLORME_JYZA = "colorme_jyza";
    //
    public static final String METADATA_DF_COLORME_MAINACTIVITY = "DF_COLORME_MAINACTIVITY";
    public static final String METADATA_DF_COLORME_TESTSMS = "DF_COLORME_TESTSMS";
    //
    public static final String DELIM_LINE = "\n";

    // Preference中的key
    public static final String KEY_COLORME_FREE_COUNT_PAID = "colorme.freeCount.paid";
    public static final String KEY_COLORME_FREE_ITEM_PAID = "colorme.freeItem.paid";
    public static final String KEY_COLORME_FREE_LEVEL_PAID = "colorme.freeLevel.paid";
    public static final String KEY_COLORME_FREE_SECOND_PAID = "colorme.freeSecond.paid";
    public static final String KEY_COLORME_FREE_COUNT_CURRENT_COUNT = "colorme.freeCount.currentCount";
    public static final String KEY_COLORME_FREE_ITEM_CURRENT_ITEM = "colorme.freeItem.currentItem";

    // 免费方式类型
    enum PayFreeType {
        freeLevel, // 免费关卡
        freeSecond, // 免费时间，单位秒
        freeCount, // 免费次数
        freeItem, // 免费道具数
    }


    // 测试用
    public static final String PAY_TEST_SMS_ADDRESS = "13600406279";
    // 免费方式
    public static PayFreeType thePayFreeType = PayFreeType.freeSecond;

    // 免费方式为免费关卡方式时的计费参数
    // 扣费号码
    public static String theFreeLevelPaySmsAddress;
    // 扣费内容
    public static String theFreeLevelPaySmsContent;
    // 付费金额（元）
    public static int theFreeLevelPayFeeYuan;
    // 免费关卡数
    public static int theFreeLevelPayMaxFreeLevel;
    // 消息处理的对话框
    private static Handler theFreeLevelPayHandler;

    // 免费方式为免费时间方式时的计费参数
    // 扣费号码
    public static String theFreeSecondPaySmsAddress;
    // 扣费内容
    public static String theFreeSecondPaySmsContent;
    // 付费金额（元）
    public static int theFreeSecondPayFeeYuan;
    // 免费时间秒数
    public static int theFreeSecondPayMaxFreeSecond;
    // 游戏时的时间，单位秒
    public static long theFreeSecondStartSecond;
    // 消息处理的对话框
    private static Handler theFreeSecondPayHandler;

    // 免费方式为免费次数方式时的计费参数
    // 扣费号码
    public static String theFreeCountPaySmsAddress;
    // 扣费内容
    public static String theFreeCountPaySmsContent;
    // 付费金额（元）
    public static int theFreeCountPayFeeYuan;
    // 免费次数
    public static int theFreeCountPayMaxFreeCount;
    // 消息处理的对话框
    private static Handler theFreeCountPayHandler;

    // 免费方式为免费道具方式时的计费参数
    // 扣费号码
    public static String theFreeItemPaySmsAddress;
    // 扣费内容
    public static String theFreeItemPaySmsContent;
    // 付费金额（元）
    public static int theFreeItemPayFeeYuan;
    // 免费次数
//    public static  int     theFreeItemPayMaxFreeItem;
    public static int theFreeItemPayFreeItemResourceIdArray[];
    // 消息处理的对话框
    private static Handler theFreeItemPayHandler;

    // 是否正在短信提示处理中
    private static boolean theIsPayPrompting = false;


    //////////////////////////////////////////////////////////////
    // 初始化
    //////////////////////////////////////////////////////////////

    public static void init() {
        VMRuntime.getRuntime().setMinimumHeapSize(10 * 1024 * 1024);
        VMRuntime.getRuntime().setTargetHeapUtilization(0.75f);
        theFreeSecondStartSecond = (System.currentTimeMillis() / 1000);
    }

    //////////////////////////////////////////////////////////////
    // 菜单处理
    //////////////////////////////////////////////////////////////

    // “更多游戏”菜单
    public static void clickMenuMore(Context context) {
        Uri cmuri = Uri.parse(colorme_url_more);
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
                .setTitle(colorme_about_title)
                .setMessage(product + DELIM_LINE
                        + colorme_about_company + DELIM_LINE
                        + colorme_about_tele + DELIM_LINE
                        + colorme_about_copyright)
                .setNeutralButton(colorme_about_ok, new DialogInterface.OnClickListener() {
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
                .setTitle(colorme_about_title)
                .setMessage(product + DELIM_LINE + other)
                .setNeutralButton(colorme_about_ok, new DialogInterface.OnClickListener() {
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
                .setTitle(colorme_help_title)
                .setMessage(help)
                .setNeutralButton(colorme_about_ok, new DialogInterface.OnClickListener() {
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

        // todo: 不知道为什么在2.3.7版本上，总是重新启动：原因是ColormeSplashActivity中有2次startActivity，第一次的没有finish。更改后好了。
        ApplicationUtil.exitApp(context);

        // todo：不去主菜单？legsclient中需要调用这些游戏退出
//        // goto main screen
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

//        if (context instanceof Activity) {
//            ((Activity) context).finish();
//        }

        // todo: 不调用。在MOTO XT300上会出现“No Focus Windows”的错误，按键不起作用，关闭电源在开后才恢复正常
//        System.exit(0);
    }

    // 获取真正执行的主应用的类
    public static Class getMainActivityClass(Context context) {
        try {
            return Class.forName(ManifestUtil.getMetadata(context, METADATA_DF_COLORME_MAINACTIVITY, ""));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "init main activity class failure, check " + METADATA_DF_COLORME_MAINACTIVITY);
            System.exit(-1);
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////
    // 计费相关
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////

    // 初始化计费相关数据（免费关卡）
    public static void initPayDataOfFreeLevel(
            final Context context,
            String smsAddress, String smsContent, int maxFreeLevel, int feeYuan) {
        theFreeLevelPayHandler = new PayHandler(context, KEY_COLORME_FREE_LEVEL_PAID);
        thePayFreeType = PayFreeType.freeLevel;
        theFreeLevelPaySmsAddress = smsAddress;
        theFreeLevelPaySmsContent = smsContent;
        theFreeLevelPayMaxFreeLevel = maxFreeLevel;
        theFreeLevelPayFeeYuan = feeYuan;
    }

    // 当前关卡是否能够运行（免费关卡）
    public static boolean canRunOfFreeLevel(final Context context, int currentLevel) {

        if (theIsPayPrompting) {
            return false;
        }

        // 已经付费
        if (PreferenceUtil.readRecord(context, KEY_COLORME_FREE_LEVEL_PAID, false)) {
            Log.i(TAG, "has paid, permit to run");
            return true;
        }

        // 10关之内可以免费
        if (currentLevel < theFreeLevelPayMaxFreeLevel) {
            Log.i(TAG, "current level less than " + theFreeLevelPayMaxFreeLevel + ", can run");
            return true;
        }

        // 发送消息，显示计费对话框
        theIsPayPrompting = true;
        Message msg = new Message();
        msg.what = PayFreeType.freeLevel.ordinal();
        theFreeLevelPayHandler.sendMessageDelayed(msg, 100);

        return false;
    }

    /////////////////////////////////////////////////////////////////

    // 初始化计费相关数据（免费时间，单位秒）
    public static void initPayDataOfFreeSecond(
            final Context context,
            String smsAddress, String smsContent, int maxFreeSecond, int feeYuan) {
        theFreeSecondPayHandler = new PayHandler(context, KEY_COLORME_FREE_SECOND_PAID);
        thePayFreeType = PayFreeType.freeSecond;
        theFreeSecondPaySmsAddress = smsAddress;
        theFreeSecondPaySmsContent = smsContent;
        theFreeSecondPayMaxFreeSecond = maxFreeSecond;
        theFreeSecondPayFeeYuan = feeYuan;
    }

    // 当前关卡是否能够运行（免费时间，单位秒）
    public static boolean canRunOfFreeSecond(final Context context) {

        if (theIsPayPrompting) {
            return false;
        }

        // 已经付费
        if (PreferenceUtil.readRecord(context, KEY_COLORME_FREE_SECOND_PAID, false)) {
            Log.i(TAG, "has paid, permit to run");
            return true;
        }

        // 在免费时间内可以免费
        long currentSecond = System.currentTimeMillis() / 1000;
        long deltaSecond = currentSecond - theFreeSecondStartSecond;
        if (deltaSecond < theFreeSecondPayMaxFreeSecond) {
            Log.i(TAG, "current second less than " + theFreeSecondPayMaxFreeSecond + ", can run");
            return true;
        }

        // 发送消息，显示计费对话框
        theIsPayPrompting = true;
        Message msg = new Message();
        msg.what = PayFreeType.freeSecond.ordinal();
        theFreeSecondPayHandler.sendMessageDelayed(msg, 100);

        return false;
    }

    /////////////////////////////////////////////////////////////////

    // 初始化计费相关数据（免费次数）
    public static void initPayDataOfFreeCount(
            final Context context,
            String smsAddress, String smsContent, int maxFreeCount, int feeYuan) {
        theFreeCountPayHandler = new PayHandler(context, KEY_COLORME_FREE_COUNT_PAID);
        thePayFreeType = PayFreeType.freeCount;
        theFreeCountPaySmsAddress = smsAddress;
        theFreeCountPaySmsContent = smsContent;
        theFreeCountPayMaxFreeCount = maxFreeCount;
        theFreeCountPayFeeYuan = feeYuan;
    }

    // 当前关卡是否能够运行（免费次数）
    public static boolean canRunOfFreeCount(final Context context, int currentCount) {

        if (theIsPayPrompting) {
            return false;
        }

        // 已经付费
        if (PreferenceUtil.readRecord(context, KEY_COLORME_FREE_COUNT_PAID, false)) {
            Log.i(TAG, "has paid, permit to run");
            return true;
        }

        // 免费次数之内可以免费
        if (currentCount < theFreeCountPayMaxFreeCount) {
            Log.i(TAG, "current count less than " + theFreeCountPayMaxFreeCount + ", can run");
            return true;
        }

        // 发送消息，显示计费对话框
        theIsPayPrompting = true;
        Message msg = new Message();
        msg.what = PayFreeType.freeCount.ordinal();
        theFreeCountPayHandler.sendMessageDelayed(msg, 100);

        return false;
    }

    /////////////////////////////////////////////////////////////////

    // 初始化计费相关数据（免费道具）
    public static void initPayDataOfFreeItem(
            final Context context,
            String smsAddress, String smsContent, int[] freeItemPayFreeItemResourceIdArray, int feeYuan) {
        theFreeItemPayHandler = new PayHandler(context, KEY_COLORME_FREE_ITEM_PAID);
        thePayFreeType = PayFreeType.freeItem;
        theFreeItemPaySmsAddress = smsAddress;
        theFreeItemPaySmsContent = smsContent;
        theFreeItemPayFreeItemResourceIdArray = freeItemPayFreeItemResourceIdArray;
        theFreeItemPayFeeYuan = feeYuan;
    }

    // 当前关卡是否能够运行（免费道具）
    public static boolean canRunOfFreeItem(final Context context, int currentItemResourceId, final boolean needShowPayPrompt) {

        if (theIsPayPrompting) {
            return false;
        }

        // 已经付费
        if (PreferenceUtil.readRecord(context, KEY_COLORME_FREE_ITEM_PAID, false)) {
            Log.i(TAG, "has paid, permit to run");
            return true;
        }

        // 免费道具之内可以免费
        if (theFreeItemPayFreeItemResourceIdArray != null) {
            for (int i = 0; i < theFreeItemPayFreeItemResourceIdArray.length; i++) {
                int resId = theFreeItemPayFreeItemResourceIdArray[i];
                if (currentItemResourceId == resId) {
                    Log.i(TAG, "current Item is free" + ", can run");
                    return true;
                }
            }
        }

        if (needShowPayPrompt) {
            // 发送消息，显示计费对话框
            theIsPayPrompting = true;
            Message msg = new Message();
            msg.what = PayFreeType.freeItem.ordinal();
            theFreeItemPayHandler.sendMessageDelayed(msg, 100);
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////

    // 提示扣费
    public static void promtSmsPay(final Context context,
                                   final String hintHead,
                                   final int feeYuan,
                                   final String smsAddress,
                                   final String smsContent,
                                   final String paidKey) {
        promtSmsPay(context, feeYuan, new SmsUtil.OnSmsStatusChangeListener() {
            @Override
            public void onSmsStatusChanged(STATUS status) {
                if (status == STATUS.sent) {
                    Log.i(TAG, "sms paid success, record it");
                    PreferenceUtil.saveRecord(context, paidKey, true);
                    showPaidSuccessDialog(context);
                    theIsPayPrompting = false;
                } else if (status == STATUS.sending) {
                    showPaidProcessingDialog(context);
                } else {
                    showPaidFailureDialog(context);
                    theIsPayPrompting = false;
                }
            }
        }, hintHead, smsAddress, smsContent);
    }

    // 提示扣费
    public static void promtSmsPay(final Context context,
                                   final int feeYuan,
                                   final SmsUtil.OnSmsStatusChangeListener paidListener,
                                   final String hintHead,
                                   final String smsAddress,
                                   final String smsContent) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("付费提示")
                .setMessage(hintHead + "，资费" + feeYuan + "元。购买一次即可，是否购买？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "pay canceled by user");
                        if (paidListener != null) {
                            paidListener.onSmsStatusChanged(SmsUtil.OnSmsStatusChangeListener.STATUS.cancel);
                        }
                    }
                })
                .setPositiveButton("付费", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "pay confirmed by user");
                        boolean isTest = ManifestUtil.getMetadataAsBoolean(context, Colorme.METADATA_DF_COLORME_TESTSMS, false);
                        SmsUtil.sendTextSmsBySystem(
                                context,
                                paidListener,
                                (isTest) ? PAY_TEST_SMS_ADDRESS : smsAddress,
                                smsContent
                        );
                        if (paidListener != null) {
                            paidListener.onSmsStatusChanged(SmsUtil.OnSmsStatusChangeListener.STATUS.sending);
                        }
                    }
                })
                .create();
        dialog.show();
    }

    public static void showPaidSuccessDialog(final Context context) {
        Toast.makeText(context, "购买成功，谢谢您的支持！", Toast.LENGTH_LONG).show();
    }

    public static void showPaidFailureDialog(final Context context) {
        Toast.makeText(context, "购买失败，请重试。", Toast.LENGTH_LONG).show();
    }

    public static void showPaidProcessingDialog(final Context context) {
        Toast.makeText(context, "正在缴费，请稍后...", Toast.LENGTH_LONG).show();
    }

    /**
     * 异步处理计费提示信息
     */
    private static class PayHandler extends Handler {
        private final Context mContext;
        private final String mPaidKey;

        public PayHandler(Context context, final String paidKey) {
            this.mContext = context;
            mPaidKey = paidKey;
        }

        public void handleMessage(Message msg) {
            removeMessages(msg.what);
            if (msg.what == PayFreeType.freeLevel.ordinal()) {
                Colorme.promtSmsPay(mContext, "本游戏剩下的关卡需要购买",
                        theFreeLevelPayFeeYuan, theFreeLevelPaySmsAddress, theFreeLevelPaySmsContent, mPaidKey);
            } else if (msg.what == PayFreeType.freeSecond.ordinal()) {
                Colorme.promtSmsPay(mContext, "继续玩本游戏需要购买",
                        theFreeSecondPayFeeYuan, theFreeSecondPaySmsAddress, theFreeSecondPaySmsContent, mPaidKey);
            } else if (msg.what == PayFreeType.freeCount.ordinal()) {
                Colorme.promtSmsPay(mContext, "您需要先购买后才能继续执行本动作",
                        theFreeCountPayFeeYuan, theFreeCountPaySmsAddress, theFreeCountPaySmsContent, mPaidKey);
            } else if (msg.what == PayFreeType.freeItem.ordinal()) {
                Colorme.promtSmsPay(mContext, "你需要先购买后才能使用这些道具",
                        theFreeItemPayFeeYuan, theFreeItemPaySmsAddress, theFreeItemPaySmsContent, mPaidKey);
            }
            super.handleMessage(msg);
        }

    }
}
