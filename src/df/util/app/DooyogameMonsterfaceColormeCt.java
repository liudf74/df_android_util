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
 * Time: ����7:49
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMonsterfaceColormeCt {
    // ����Ϳװ ������ ���� �汾
    public static final String TAG = "df.util.DooyogameMonsterfaceColormeCt";

    // ��ͬ��Ʒ������
    public static final String product_name = "���֣�����Ϳװ";
    public static final String product_version = "�汾��V2.0";
    public static final String product_catalog = "Ӧ�����ͣ�����";
    public static final String product_help = "���ơ���ЦΪ�����Ϳѻ��Ϸ����Ϊ2��ģʽ��\n" +
            "���ļ���ģʽ��\n" +
            "��Ϸ��������������������һ����Ƭ������羰�ȣ���ͨ����Ϸ�ڵĵ��߽��и��Ի���Ϳѻ��" +
            "������YY������\n" +
            "����ͼƬͿѻģʽ��\n" +
            "��Ϸ��������ʹ���ֻ�ͼ�⣬��ѡһ������Ҫʹ�õ�ͼƬ��ͨ����Ϸ�ڵĵ��߽��и��Ի���Ϳѻ��" +
            "��Ϊϲ����ͼƬ�����¼Ǻ�Ŷ��\n" +
            " \n" +
            "Ϳѻ�����Ƭ�����Ա��ֵ������ղز���ͨ�����š��ʼ���΢���ȷ�ʽ���������ˡ����ˡ����ѡ�������һ�����";
    public static final String product_sms = "��һ�����������Ҷ�ʱ��ʧ���ң����������Ƭ���������ס��" +
            "�Ҵ��������Լ����������ߣ������Ҷ��������Ƭ��е�����Ҳ�н��찡�� " +
            "http://wapgame.189.cn/hd/qs";
    public static final String product_weibo = "��һ�����������Ҷ�ʱ��ʧ���ң����������Ƭ���������ס��" +
            "�Ҵ��������Լ����������ߣ������Ҷ��������Ƭ��е�����Ҳ�н��찡��" +
            "http://game.189.cn/hd/2012qs";

    // ����ͷ�����3���Ժ�ʼ�Ʒѣ�
    // �ʷ�3Ԫ��ע������е���ֵҪ���Ž���ߣ���
    // ���룺0311C0905611022100548511022100528201MC099146000000000000000000000000
    public static final String theCountPaySmsAddress = "10659811003";
    public static final String theCountPaySmsContent = "0311C0905611022100548511022100528201MC099146000000000000000000000000";

    // ��ϷĬ�ϴ�ʮ��Ϳѻ���ߣ������ĵ��߲���ʹ�ã��ͻ����δ���ŵ�Ϳѻ���ߺ󵯳��Ʒѣ���Ҫ����
    // �ʷ�1Ԫ��ע������е���ֵҪ���Ž���ߣ��������ȫ������ʹ�á�
    // ���룺0111C0905611022100548511022100528301MC099146000000000000000000000000
    public static final String theItemPaySmsAddress = "10659811001";
    public static final String theItemPaySmsContent = "0111C0905611022100548511022100528301MC099146000000000000000000000000";

    // ��ѵ�������
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
    // ������ͼ�꣬��ʾ�û�����
    public static final String theLockItem = "pay_icon";
    // �����ڴ�ʹ��
//    public static Drawable                theLockDrawable;
    public static TypedArray theLockTypedArray;
    public static SoftReference<Drawable> theLockReference;


    // �Ƿ���colorme���ض��˵����������ִ��֮
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
    // �Ʒ���أ���Ѵ�����
    // ����ͷ�����3���Ժ�ʼ�Ʒѣ�
    // �ʷ�3Ԫ��ע������е���ֵҪ���Ž���ߣ���

    public static void initPayDataOfFreeCount(Context context) {
        Colorme.initPayDataOfFreeCount(context, theCountPaySmsAddress, theCountPaySmsContent, 3, 3);
    }

    public static boolean canRunOfFreeCount(final Context context) {
        // ���ĵ�ǰ�Ѿ�ʹ�õĴ��������ڱ���ͷ�������˵��
        int currentCount = PreferenceUtil.readRecord(context, Colorme.KEY_COLORME_FREE_COUNT_CURRENT_COUNT, -1);
        currentCount++;
        PreferenceUtil.saveRecord(context, Colorme.KEY_COLORME_FREE_COUNT_CURRENT_COUNT, currentCount);

        return Colorme.canRunOfFreeCount(context, currentCount);
    }

    ////////////////////////////////////////////////////////////
    // �Ʒ���أ���ѵ��ߣ�
    // ��ϷĬ�ϴ�ʮ��Ϳѻ���ߣ������ĵ��߲���ʹ�ã��ͻ����δ���ŵ�Ϳѻ���ߺ󵯳��Ʒѣ���Ҫ����
    // �ʷ�1Ԫ��ע������е���ֵҪ���Ž���ߣ��������ȫ������ʹ�á�

    public static void initPayDataOfFreeItem(Context context) {
        // ��ʼ����ѵ��ߵ���ԴID��������ͼ��Сͼ��
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

    // ����Ҫ����ź�΢���ķ������ݲ�����ͬ������ֻ�ý����ź��������ݷָ����
    public static void showShareChooser(final Context context,
                                        final Intent contentIntent,
                                        final String title,
                                        final Uri imageUrl) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("����ʽ")
                .setMessage("��ֱ��ͨ�����ŷ��������ô��\n���Ͳ���������ȡͨ�ŷѣ�����ȡ�������á�")
                .setPositiveButton("�ǵ�", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // todo: ����ķ���û����ʽ���ĵ�����֪���Ƿ�����android�ֻ���֧��

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
                .setNegativeButton("ѡ��������ʽ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendImageUsingOtherApp(contentIntent, title, context);
                    }
                })
                .create();
        dialog.show();
    }

    private static void sendImageUsingSmsApp(String mmsPackageName, String mmsClassName, Intent contentIntent, Context context, Uri imageUrl) {
        // todo����֤�ڸ����汾��android�ϣ���֧�ֲ��ŷ���
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
