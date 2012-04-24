package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.nd.dianjin.DianJinPlatform;
import com.nd.dianjin.OfferBanner;
import com.nd.dianjin.utility.BannerColorConfig;
import com.nd.dianjin.webservice.WebServiceListener;
import df.util.android.PreferenceUtil;
import df.util.android.TelephoneUtil;
import df.util.type.TimeUtil;


import static android.content.DialogInterface.OnClickListener;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: ����4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameWmmtDianjin {
    private static final String TAG = "df.util.app.DooyogameWmmtDianjin";
    private final static String USER_POINTS_KEY = "user_points";
    private final static String IS_HAD_SPEND = "is_had_spend";
    private final static String IS_NEED_SPEND = "is_need_spend";
    private final static String IS_HAD_REQUEST = "is_had_request";
    private static Context context;

    // ���Ѷ�������,������Ϸ
    public static final int ACTION_ID_1001 = 1001;


    /**
     * ������ʱ���ʼ�� dianjin sdk
     *
     * @param ct
     */
    public static void initDianjinSdk(Context ct) {
        context = ct;
        DianJinPlatform.initialize(context, 2160, "eaa9cd743713ee4ea52976aaa64473a4");

        // ��ʼ���û��Ļ���
        getUserPoints();
    }

    /**
     * ��ʾ�û�������Ϸ
     *
     * @param ct
     */
    public static void warnUserSpendPointsDialog(final Context ct) {
        context = ct;

        // �жϼ�����Ϸ�������Ƿ��Ѿ��ύ
        boolean is_had_request = PreferenceUtil.readRecord(context, IS_HAD_REQUEST, false);
        if (is_had_request) {
            Toast.makeText(context, "������Ϸ�������Ѿ��ύ�����Ժ����ԡ�", Toast.LENGTH_SHORT).show();
            return;
        }

        // �������ѱ�ʶ
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final float spendPoints = 100;
        float userPoints = getUserPointsFromLocal();
        Log.d(TAG, "warnUserSpendPointsDialog, userPoints = " + userPoints);
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("��ܰ��ʾ");
        if (userPoints >= spendPoints) {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��M�ң�����ǰӵ��" +
                    userPoints + "��M�ң����Ƿ�Ҫ������";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    spendUserPoints(spendPoints);
                }
            });

        } else {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��M�ң�����ǰӵ��" +
                    userPoints + "��M�ң�M�Ҳ��㣬������ͨ�����ȷ����ȡM�ҡ�";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // ��ʾ���ǽ
                    showDianjinOffers();
                }
            });
        }
        builder.setNegativeButton("ȡ��", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    /**
     * ��� dianjin �Ļ������
     *
     * @param activity
     */
    public static void addDianjinAdView(final Activity activity) {
        context = activity;

        // �����������
//        RelativeLayout adArea = new RelativeLayout(context);
//        RelativeLayout.LayoutParams adAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        adAreaParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // ���� dianjin �����������
        LinearLayout adDianjinArea = new LinearLayout(context);
        RelativeLayout.LayoutParams adDianjinAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        adDianjinAreaParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adDianjinArea.setGravity(LinearLayout.VERTICAL);

        BannerColorConfig colorConfig = new BannerColorConfig();
        colorConfig.setBackgroundColor(Color.BLUE);
        colorConfig.setDetailColor(0xFFFF3300);
        colorConfig.setNameColor(0xFFFF3300);
        colorConfig.setRewardColor(0xFFFF3300);
        OfferBanner bannerLayout = new OfferBanner(context, 2160,
                "eaa9cd743713ee4ea52976aaa64473a4", 6000,
                OfferBanner.AnimationType.ANIMATION_PUSHUP, colorConfig);
        adDianjinArea.addView(bannerLayout);

//        adArea.addView(adDianjinArea, adDianjinAreaParams);

        activity.addContentView(adDianjinArea, adDianjinAreaParams);
    }

    /**
     * �ж��û��Ƿ��Ѿ�������Ϸ
     *
     * @param context
     * @return
     */
    public static boolean isHadSpend(Context context) {
        // �Ƿ��Ѿ�����
        boolean is_had_spend = PreferenceUtil.readRecord(context, IS_HAD_SPEND, false);
        if (is_had_spend) {
            PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
        }

        Log.d(TAG, "isHadSpend ,is_had_spend = " + is_had_spend);
        return is_had_spend;
    }

    /**
     * ��Ϸ�Ƿ���Ҫ����
     *
     * @param context
     * @return
     */
    public static boolean isNeedSpend(Context context) {
        boolean is_need_spend = PreferenceUtil.readRecord(context, IS_NEED_SPEND, false);
        Log.d(TAG, "isNeedSpend,is_need_spend = " + is_need_spend);
        return is_need_spend;
    }


    /**
     * ��ʾ���ǽ
     */
    private static void showDianjinOffers() {
        DianJinPlatform.showOfferWall(context, DianJinPlatform.Oriention.PORTRAIT);
    }

    /**
     * �ӷ�������ȡ��ȡ�û��Ļ���
     */
    private static void getUserPoints() {
        DianJinPlatform.getBalance(context, new WebServiceListener<Float>() {
            @Override
            public void onResponse(int respCode, Float balance) {
                if (respCode == DianJinPlatform.DIANJIN_SUCCESS) {
                    saveUserPoints(balance);
                    Log.d(TAG, "getUserPoints, userPoints = " + balance);
                }
            }
        });
    }

    /**
     * �����û��Ļ���
     *
     * @param spendPoint
     */
    public static void spendUserPoints(float spendPoint) {
        // ��ʶ������Ϸ�������Ѿ��ύ
        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, true);

        // ������
        String orderSerial = TimeUtil.toLongOfyyyyMMddHHmmss() + TelephoneUtil.toImei(context);
        // ���Ѷ�������
        int actionId = ACTION_ID_1001;
        DianJinPlatform.consume(context, orderSerial, spendPoint, actionId, new WebServiceListener<Integer>() {
            @Override
            public void onResponse(int responseCode, Integer balance) {
                switch (responseCode) {
                    case DianJinPlatform.DIANJIN_SUCCESS:
                        Toast.makeText(context, "���ѳɹ�,��Ϸ�Ѿ�������������½�����Ϸ��", Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
                        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
                        saveUserPoints(balance);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_REQUES_CONSUNE:
                        Toast.makeText(context, "֧������ʧ��",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:
                        Toast.makeText(context, "M�Ҳ���",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ACCOUNT_NO_EXIST:
                        Toast.makeText(context, "�˺Ų�����",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ORDER_SERIAL_REPEAT:
                        Toast.makeText(context, "�������ظ�",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT:
                        Toast.makeText(context,
                                "һ���Խ��׽�������޶����",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_RETURN_CONSUME_ID_NO_EXIST:
                        Toast.makeText(context,
                                "�����ڸ����͵����Ѷ���ID", Toast.LENGTH_SHORT)
                                .show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    default:
                        Toast.makeText(context, "δ֪����,������Ϊ��" + responseCode, Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                }
            }
        });

    }


    /**
     * ���ػ�ȡ�û�����
     *
     * @return
     */
    private static float getUserPointsFromLocal() {
        getUserPoints();
        return PreferenceUtil.readRecord(context, USER_POINTS_KEY, 0f);
    }


    private static void saveUserPoints(float points) {
        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, points);
    }


}
