package df.util.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import com.uucun.adsdk.UUAppConnect;
import com.uucun.adsdk.UpdatePointListener;
import df.util.android.PreferenceUtil;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: ����4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameWmmtAppjoy {
    private static final String TAG = "df.util.app.DooyogameWmmtAppjoy";
    private final static String USER_POINTS_KEY = "user_points";
    private final static String USER_POINT_UNIT_KEY = "point_unit";
    private final static String IS_HAD_SPEND = "is_had_spend";
    private final static String IS_NEED_SPEND = "is_need_spend";
    private final static String IS_HAD_REQUEST = "is_had_request";

    /**
     * ������ʱ���ʼ��appjoy sdk
     *
     * @param context
     */
    public static void initAppjoySdk(final Context context) {
        UUAppConnect.getInstance(context).initSdk();

        // ��ʼ���û�����
        getUserPionts(context);
    }

    /**
     * ��ʾ�û�������Ϸ
     *
     * @param context
     */
    public static void warnUserSpendPointsDialog(final Context context) {
        // �ж������Ƿ��ύ
        boolean isHadRequest = PreferenceUtil.readRecord(context, IS_HAD_REQUEST, false);
        if (isHadRequest) {
            Toast.makeText(context, "������Ϸ�������Ѿ��ύ�����Ժ�����", Toast.LENGTH_LONG).show();
            return;
        }
        // ������ѱ�ʶ
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final int spendPoints = 100;
        int userPonts = getUserPointsFromLocal(context);
        String pointUnit = getUserPointUnit(context);
        Log.d(TAG, "warnUserSpendPointsDialog, userPoints = " + userPonts);
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("��ܰ��ʾ");
        if (userPonts >= spendPoints) {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��" + pointUnit + "������ǰӵ��" +
                    userPonts + "��" + pointUnit + "�����Ƿ�Ҫ������";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    spendUserPoints(context, spendPoints);
                }
            });

        } else {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��" + pointUnit + "������ǰӵ��" +
                    userPonts + "��" + pointUnit + "��" + pointUnit + "���㣬������ͨ�����ȷ����ȡ" + pointUnit + "��";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // ��ʾ���ǽ
                    showAppjoyOffers(context);
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
     * ʹ�û���
     *
     * @param context
     * @param spendPoints
     */
    private static void spendUserPoints(final Context context, int spendPoints) {
        // ��������ύ��ʶ
        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, true);

        // ֱ�����Ľ��
        UUAppConnect.getInstance(context).spendPoints(spendPoints,
                new UpdatePointListener() {
                    public void onSuccess(String unit, int total) {
                        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, total);
                        PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
                        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
                        Toast.makeText(context, "���ѳɹ�,���������½�����Ϸ��ף����Ϸ��졣", Toast.LENGTH_LONG).show();
                    }

                    public void onError(String msg) {
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * �ж��û��Ƿ��Ѿ�����
     *
     * @param context
     * @return
     */
    public static boolean isHadSpend(Context context) {
        boolean is_had_spend = PreferenceUtil.readRecord(context, IS_HAD_SPEND, false);
        if (is_had_spend) {
            PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
        }
        return is_had_spend;
    }

    /**
     * �ж���Ϸ�Ƿ���Ҫ����
     *
     * @param context
     * @return
     */
    public static boolean isNeedSpend(Context context) {
        return PreferenceUtil.readRecord(context, IS_NEED_SPEND, false);
    }


    /**
     * ���ػ�ȡ�û�����
     *
     * @param context
     * @return
     */
    private static int getUserPointsFromLocal(Context context) {
        // �ӷ����������û��Ļ���
        getUserPionts(context);
        int userPoints;
        userPoints = PreferenceUtil.readRecord(context, USER_POINTS_KEY, 0);
        return userPoints;
    }

    /**
     * �ӷ�������ȡ�û��Ļ���
     *
     * @param context
     */
    private static void getUserPionts(final Context context) {
        UUAppConnect.getInstance(context).getPoints(
                new UpdatePointListener() {
                    @Override
                    public void onError(String s) {
                        // nothing to do ...
                    }

                    @Override
                    public void onSuccess(String pointUnit, int userPointTotal) {
                        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, userPointTotal);
                        PreferenceUtil.saveRecord(context, USER_POINT_UNIT_KEY, pointUnit);
                    }
                }
        );
    }

    /**
     * ��ʾ���ǽ
     *
     * @param context
     */
    private static void showAppjoyOffers(Context context) {
        UUAppConnect.getInstance(context).showOffers();
    }


    /**
     * ��ȡ������ҵĵ�λ
     *
     * @param context
     * @return
     */
    private static String getUserPointUnit(Context context) {
        String pointUnit;
        pointUnit = PreferenceUtil.readRecord(context, USER_POINT_UNIT_KEY, "���");
        return pointUnit;
    }


}
