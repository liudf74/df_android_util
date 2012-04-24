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
 * Time: ����7:49
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMonsterfaceMaya {
    // ����Ϳװ ���� �汾
    public static final String TAG = "df.util.DooyogameMonsterfaceMaya";

    // ��ͬ��Ʒ������
    public static final String product_name = "���֣�����Ϳװ";
    public static final String product_version = "�汾��V2.0";
    public static final String product_catalog = "";
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
            "";
    public static final String product_weibo = "��һ�����������Ҷ�ʱ��ʧ���ң����������Ƭ���������ס��" +
            "�Ҵ��������Լ����������ߣ������Ҷ��������Ƭ��е�����Ҳ�н��찡��" +
            "";

    // �Ƿ����ض��˵����������ִ��֮
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

    // ���Ͷ��ź�΢��
    public static final String toSendSmsText() {
        return product_sms;
    }

    public static final String toSendWeiboText() {
        return product_weibo;
    }

    // ���ź�΢���ķ������ݲ�����ͬ������ֻ�ý����ź��������ݷָ����
    public static void showShareChooser(final Context context,
                                        final Intent contentIntent,
                                        final String title,
                                        final Uri imageUrl) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("����ʽ")
                .setMessage("��ֱ��ͨ�����ŷ��������ô��")
                .setPositiveButton("�ǵ�", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // todo: ����ķ���û����ʽ���ĵ�����֪���Ƿ�����android�ֻ���֧��
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
        // ָ�����Ͳ���
        contentIntent.putExtra("sms_body", toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_SUBJECT, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_TEXT, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_TITLE, toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_STREAM, imageUrl);

        contentIntent.setClassName(mmsPackageName, mmsClassName);
        context.startActivity(contentIntent);
    }

    private static void sendImageUsingOtherApp(Intent contentIntent, String title, Context context) {
        // ��ͨ��ѡ��ʽ���Ͷ���ʱ��Ҳ�ܴ�������
        contentIntent.putExtra("sms_body", toSendSmsText());
        contentIntent.putExtra(Intent.EXTRA_SUBJECT, toSendWeiboText());

        contentIntent.putExtra(Intent.EXTRA_TEXT, toSendWeiboText());
        Intent chooseIntent = Intent.createChooser(contentIntent, title);
        context.startActivity(chooseIntent);
    }


}
