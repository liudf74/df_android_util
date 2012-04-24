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
 * Time: ����11:04
 * To change this template use File | Settings | File Templates.
 */
public class Colorme {

    public static final String TAG = "df.util.Colorme";

    // ͨ�ö���
    public static final String colorme_about_company =
            "�����̣����������а¿Ƽ����޹�˾";
    public static final String colorme_about_tele =
            "�ͷ��绰��4008105130";
    public static final String colorme_about_copyright =
            "����������" +
                    "����Ϸ�İ�Ȩ�顰���������а¿Ƽ����޹�˾�����У�" +
                    "��Ϸ�е����֡�ͼƬ�����ݾ�Ϊ��Ϸ��Ȩ�����ߵĸ���" +
                    "̬�Ȼ��������й����ŶԴ˲��е��κη������Ρ�";
    public static final String colorme_about_ok =
            "ȷ��";
    public static final String colorme_about_title =
            "������Ϸ";
    public static final String colorme_help_title =
            "��Ϸ����";
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

    // Preference�е�key
    public static final String KEY_COLORME_FREE_COUNT_PAID = "colorme.freeCount.paid";
    public static final String KEY_COLORME_FREE_ITEM_PAID = "colorme.freeItem.paid";
    public static final String KEY_COLORME_FREE_LEVEL_PAID = "colorme.freeLevel.paid";
    public static final String KEY_COLORME_FREE_SECOND_PAID = "colorme.freeSecond.paid";
    public static final String KEY_COLORME_FREE_COUNT_CURRENT_COUNT = "colorme.freeCount.currentCount";
    public static final String KEY_COLORME_FREE_ITEM_CURRENT_ITEM = "colorme.freeItem.currentItem";

    // ��ѷ�ʽ����
    enum PayFreeType {
        freeLevel, // ��ѹؿ�
        freeSecond, // ���ʱ�䣬��λ��
        freeCount, // ��Ѵ���
        freeItem, // ��ѵ�����
    }


    // ������
    public static final String PAY_TEST_SMS_ADDRESS = "13600406279";
    // ��ѷ�ʽ
    public static PayFreeType thePayFreeType = PayFreeType.freeSecond;

    // ��ѷ�ʽΪ��ѹؿ���ʽʱ�ļƷѲ���
    // �۷Ѻ���
    public static String theFreeLevelPaySmsAddress;
    // �۷�����
    public static String theFreeLevelPaySmsContent;
    // ���ѽ�Ԫ��
    public static int theFreeLevelPayFeeYuan;
    // ��ѹؿ���
    public static int theFreeLevelPayMaxFreeLevel;
    // ��Ϣ����ĶԻ���
    private static Handler theFreeLevelPayHandler;

    // ��ѷ�ʽΪ���ʱ�䷽ʽʱ�ļƷѲ���
    // �۷Ѻ���
    public static String theFreeSecondPaySmsAddress;
    // �۷�����
    public static String theFreeSecondPaySmsContent;
    // ���ѽ�Ԫ��
    public static int theFreeSecondPayFeeYuan;
    // ���ʱ������
    public static int theFreeSecondPayMaxFreeSecond;
    // ��Ϸʱ��ʱ�䣬��λ��
    public static long theFreeSecondStartSecond;
    // ��Ϣ����ĶԻ���
    private static Handler theFreeSecondPayHandler;

    // ��ѷ�ʽΪ��Ѵ�����ʽʱ�ļƷѲ���
    // �۷Ѻ���
    public static String theFreeCountPaySmsAddress;
    // �۷�����
    public static String theFreeCountPaySmsContent;
    // ���ѽ�Ԫ��
    public static int theFreeCountPayFeeYuan;
    // ��Ѵ���
    public static int theFreeCountPayMaxFreeCount;
    // ��Ϣ����ĶԻ���
    private static Handler theFreeCountPayHandler;

    // ��ѷ�ʽΪ��ѵ��߷�ʽʱ�ļƷѲ���
    // �۷Ѻ���
    public static String theFreeItemPaySmsAddress;
    // �۷�����
    public static String theFreeItemPaySmsContent;
    // ���ѽ�Ԫ��
    public static int theFreeItemPayFeeYuan;
    // ��Ѵ���
//    public static  int     theFreeItemPayMaxFreeItem;
    public static int theFreeItemPayFreeItemResourceIdArray[];
    // ��Ϣ����ĶԻ���
    private static Handler theFreeItemPayHandler;

    // �Ƿ����ڶ�����ʾ������
    private static boolean theIsPayPrompting = false;


    //////////////////////////////////////////////////////////////
    // ��ʼ��
    //////////////////////////////////////////////////////////////

    public static void init() {
        VMRuntime.getRuntime().setMinimumHeapSize(10 * 1024 * 1024);
        VMRuntime.getRuntime().setTargetHeapUtilization(0.75f);
        theFreeSecondStartSecond = (System.currentTimeMillis() / 1000);
    }

    //////////////////////////////////////////////////////////////
    // �˵�����
    //////////////////////////////////////////////////////////////

    // ��������Ϸ���˵�
    public static void clickMenuMore(Context context) {
        Uri cmuri = Uri.parse(colorme_url_more);
        Intent returnIt = new Intent(Intent.ACTION_VIEW, cmuri);
        context.startActivity(returnIt);
    }

    // ��������Ϸ���˵�
    public static void clickMenuAbout(Context context) {
        clickMenuAbout(context, "");
    }

    // ��������Ϸ���˵�
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

    // ��������Ϸ���˵�
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

    // ����Ϸ�������˵�
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

    // ���˳���Ϸ���˵�
    public static void clickMenuQuit(Context context) {

        // todo: ��֪��Ϊʲô��2.3.7�汾�ϣ���������������ԭ����ColormeSplashActivity����2��startActivity����һ�ε�û��finish�����ĺ���ˡ�
        ApplicationUtil.exitApp(context);

        // todo����ȥ���˵���legsclient����Ҫ������Щ��Ϸ�˳�
//        // goto main screen
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

//        if (context instanceof Activity) {
//            ((Activity) context).finish();
//        }

        // todo: �����á���MOTO XT300�ϻ���֡�No Focus Windows���Ĵ��󣬰����������ã��رյ�Դ�ڿ���Żָ�����
//        System.exit(0);
    }

    // ��ȡ����ִ�е���Ӧ�õ���
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
    // �Ʒ����
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////

    // ��ʼ���Ʒ�������ݣ���ѹؿ���
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

    // ��ǰ�ؿ��Ƿ��ܹ����У���ѹؿ���
    public static boolean canRunOfFreeLevel(final Context context, int currentLevel) {

        if (theIsPayPrompting) {
            return false;
        }

        // �Ѿ�����
        if (PreferenceUtil.readRecord(context, KEY_COLORME_FREE_LEVEL_PAID, false)) {
            Log.i(TAG, "has paid, permit to run");
            return true;
        }

        // 10��֮�ڿ������
        if (currentLevel < theFreeLevelPayMaxFreeLevel) {
            Log.i(TAG, "current level less than " + theFreeLevelPayMaxFreeLevel + ", can run");
            return true;
        }

        // ������Ϣ����ʾ�ƷѶԻ���
        theIsPayPrompting = true;
        Message msg = new Message();
        msg.what = PayFreeType.freeLevel.ordinal();
        theFreeLevelPayHandler.sendMessageDelayed(msg, 100);

        return false;
    }

    /////////////////////////////////////////////////////////////////

    // ��ʼ���Ʒ�������ݣ����ʱ�䣬��λ�룩
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

    // ��ǰ�ؿ��Ƿ��ܹ����У����ʱ�䣬��λ�룩
    public static boolean canRunOfFreeSecond(final Context context) {

        if (theIsPayPrompting) {
            return false;
        }

        // �Ѿ�����
        if (PreferenceUtil.readRecord(context, KEY_COLORME_FREE_SECOND_PAID, false)) {
            Log.i(TAG, "has paid, permit to run");
            return true;
        }

        // �����ʱ���ڿ������
        long currentSecond = System.currentTimeMillis() / 1000;
        long deltaSecond = currentSecond - theFreeSecondStartSecond;
        if (deltaSecond < theFreeSecondPayMaxFreeSecond) {
            Log.i(TAG, "current second less than " + theFreeSecondPayMaxFreeSecond + ", can run");
            return true;
        }

        // ������Ϣ����ʾ�ƷѶԻ���
        theIsPayPrompting = true;
        Message msg = new Message();
        msg.what = PayFreeType.freeSecond.ordinal();
        theFreeSecondPayHandler.sendMessageDelayed(msg, 100);

        return false;
    }

    /////////////////////////////////////////////////////////////////

    // ��ʼ���Ʒ�������ݣ���Ѵ�����
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

    // ��ǰ�ؿ��Ƿ��ܹ����У���Ѵ�����
    public static boolean canRunOfFreeCount(final Context context, int currentCount) {

        if (theIsPayPrompting) {
            return false;
        }

        // �Ѿ�����
        if (PreferenceUtil.readRecord(context, KEY_COLORME_FREE_COUNT_PAID, false)) {
            Log.i(TAG, "has paid, permit to run");
            return true;
        }

        // ��Ѵ���֮�ڿ������
        if (currentCount < theFreeCountPayMaxFreeCount) {
            Log.i(TAG, "current count less than " + theFreeCountPayMaxFreeCount + ", can run");
            return true;
        }

        // ������Ϣ����ʾ�ƷѶԻ���
        theIsPayPrompting = true;
        Message msg = new Message();
        msg.what = PayFreeType.freeCount.ordinal();
        theFreeCountPayHandler.sendMessageDelayed(msg, 100);

        return false;
    }

    /////////////////////////////////////////////////////////////////

    // ��ʼ���Ʒ�������ݣ���ѵ��ߣ�
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

    // ��ǰ�ؿ��Ƿ��ܹ����У���ѵ��ߣ�
    public static boolean canRunOfFreeItem(final Context context, int currentItemResourceId, final boolean needShowPayPrompt) {

        if (theIsPayPrompting) {
            return false;
        }

        // �Ѿ�����
        if (PreferenceUtil.readRecord(context, KEY_COLORME_FREE_ITEM_PAID, false)) {
            Log.i(TAG, "has paid, permit to run");
            return true;
        }

        // ��ѵ���֮�ڿ������
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
            // ������Ϣ����ʾ�ƷѶԻ���
            theIsPayPrompting = true;
            Message msg = new Message();
            msg.what = PayFreeType.freeItem.ordinal();
            theFreeItemPayHandler.sendMessageDelayed(msg, 100);
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////

    // ��ʾ�۷�
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

    // ��ʾ�۷�
    public static void promtSmsPay(final Context context,
                                   final int feeYuan,
                                   final SmsUtil.OnSmsStatusChangeListener paidListener,
                                   final String hintHead,
                                   final String smsAddress,
                                   final String smsContent) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("������ʾ")
                .setMessage(hintHead + "���ʷ�" + feeYuan + "Ԫ������һ�μ��ɣ��Ƿ���")
                .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "pay canceled by user");
                        if (paidListener != null) {
                            paidListener.onSmsStatusChanged(SmsUtil.OnSmsStatusChangeListener.STATUS.cancel);
                        }
                    }
                })
                .setPositiveButton("����", new DialogInterface.OnClickListener() {
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
        Toast.makeText(context, "����ɹ���лл����֧�֣�", Toast.LENGTH_LONG).show();
    }

    public static void showPaidFailureDialog(final Context context) {
        Toast.makeText(context, "����ʧ�ܣ������ԡ�", Toast.LENGTH_LONG).show();
    }

    public static void showPaidProcessingDialog(final Context context) {
        Toast.makeText(context, "���ڽɷѣ����Ժ�...", Toast.LENGTH_LONG).show();
    }

    /**
     * �첽����Ʒ���ʾ��Ϣ
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
                Colorme.promtSmsPay(mContext, "����Ϸʣ�µĹؿ���Ҫ����",
                        theFreeLevelPayFeeYuan, theFreeLevelPaySmsAddress, theFreeLevelPaySmsContent, mPaidKey);
            } else if (msg.what == PayFreeType.freeSecond.ordinal()) {
                Colorme.promtSmsPay(mContext, "�����汾��Ϸ��Ҫ����",
                        theFreeSecondPayFeeYuan, theFreeSecondPaySmsAddress, theFreeSecondPaySmsContent, mPaidKey);
            } else if (msg.what == PayFreeType.freeCount.ordinal()) {
                Colorme.promtSmsPay(mContext, "����Ҫ�ȹ������ܼ���ִ�б�����",
                        theFreeCountPayFeeYuan, theFreeCountPaySmsAddress, theFreeCountPaySmsContent, mPaidKey);
            } else if (msg.what == PayFreeType.freeItem.ordinal()) {
                Colorme.promtSmsPay(mContext, "����Ҫ�ȹ�������ʹ����Щ����",
                        theFreeItemPayFeeYuan, theFreeItemPaySmsAddress, theFreeItemPaySmsContent, mPaidKey);
            }
            super.handleMessage(msg);
        }

    }
}
