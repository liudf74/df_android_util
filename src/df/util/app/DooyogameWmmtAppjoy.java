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
 * Time: 下午4:09
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
     * 启动的时候初始化appjoy sdk
     *
     * @param context
     */
    public static void initAppjoySdk(final Context context) {
        UUAppConnect.getInstance(context).initSdk();

        // 初始化用户积分
        getUserPionts(context);
    }

    /**
     * 提示用户激活游戏
     *
     * @param context
     */
    public static void warnUserSpendPointsDialog(final Context context) {
        // 判断请求是否提交
        boolean isHadRequest = PreferenceUtil.readRecord(context, IS_HAD_REQUEST, false);
        if (isHadRequest) {
            Toast.makeText(context, "激活游戏的请求已经提交，请稍后再试", Toast.LENGTH_LONG).show();
            return;
        }
        // 添加消费标识
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final int spendPoints = 100;
        int userPonts = getUserPointsFromLocal(context);
        String pointUnit = getUserPointUnit(context);
        Log.d(TAG, "warnUserSpendPointsDialog, userPoints = " + userPonts);
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        if (userPonts >= spendPoints) {
            alertMsg = "您需要先激活该游戏，永久激活该游戏需要消耗您" + spendPoints + "个" + pointUnit + "，您当前拥有" +
                    userPonts + "个" + pointUnit + "，您是否要继续？";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    spendUserPoints(context, spendPoints);
                }
            });

        } else {
            alertMsg = "您需要先激活该游戏，永久激活该游戏需要消耗您" + spendPoints + "个" + pointUnit + "，您当前拥有" +
                    userPonts + "个" + pointUnit + "，" + pointUnit + "不足，您可以通过点击确定获取" + pointUnit + "。";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 显示广告墙
                    showAppjoyOffers(context);
                }
            });
        }
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    /**
     * 使用积分
     *
     * @param context
     * @param spendPoints
     */
    private static void spendUserPoints(final Context context, int spendPoints) {
        // 添加请求提交标识
        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, true);

        // 直接消耗金币
        UUAppConnect.getInstance(context).spendPoints(spendPoints,
                new UpdatePointListener() {
                    public void onSuccess(String unit, int total) {
                        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, total);
                        PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
                        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
                        Toast.makeText(context, "消费成功,您可以重新进入游戏，祝您游戏愉快。", Toast.LENGTH_LONG).show();
                    }

                    public void onError(String msg) {
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * 判断用户是否已经付费
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
     * 判断游戏是否需要消费
     *
     * @param context
     * @return
     */
    public static boolean isNeedSpend(Context context) {
        return PreferenceUtil.readRecord(context, IS_NEED_SPEND, false);
    }


    /**
     * 本地获取用户积分
     *
     * @param context
     * @return
     */
    private static int getUserPointsFromLocal(Context context) {
        // 从服务器更新用户的积分
        getUserPionts(context);
        int userPoints;
        userPoints = PreferenceUtil.readRecord(context, USER_POINTS_KEY, 0);
        return userPoints;
    }

    /**
     * 从服务器获取用户的积分
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
     * 显示广告墙
     *
     * @param context
     */
    private static void showAppjoyOffers(Context context) {
        UUAppConnect.getInstance(context).showOffers();
    }


    /**
     * 获取虚拟货币的单位
     *
     * @param context
     * @return
     */
    private static String getUserPointUnit(Context context) {
        String pointUnit;
        pointUnit = PreferenceUtil.readRecord(context, USER_POINT_UNIT_KEY, "金币");
        return pointUnit;
    }


}
