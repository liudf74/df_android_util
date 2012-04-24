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
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameWmmtDianjin {
    private static final String TAG = "df.util.app.DooyogameWmmtDianjin";
    private final static String USER_POINTS_KEY = "user_points";
    private final static String IS_HAD_SPEND = "is_had_spend";
    private final static String IS_NEED_SPEND = "is_need_spend";
    private final static String IS_HAD_REQUEST = "is_had_request";
    private static Context context;

    // 消费动作类型,激活游戏
    public static final int ACTION_ID_1001 = 1001;


    /**
     * 启动的时候初始化 dianjin sdk
     *
     * @param ct
     */
    public static void initDianjinSdk(Context ct) {
        context = ct;
        DianJinPlatform.initialize(context, 2160, "eaa9cd743713ee4ea52976aaa64473a4");

        // 初始化用户的积分
        getUserPoints();
    }

    /**
     * 提示用户激活游戏
     *
     * @param ct
     */
    public static void warnUserSpendPointsDialog(final Context ct) {
        context = ct;

        // 判断激活游戏的请求是否已经提交
        boolean is_had_request = PreferenceUtil.readRecord(context, IS_HAD_REQUEST, false);
        if (is_had_request) {
            Toast.makeText(context, "激活游戏的请求已经提交，请稍后再试。", Toast.LENGTH_SHORT).show();
            return;
        }

        // 设置消费标识
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final float spendPoints = 100;
        float userPoints = getUserPointsFromLocal();
        Log.d(TAG, "warnUserSpendPointsDialog, userPoints = " + userPoints);
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        if (userPoints >= spendPoints) {
            alertMsg = "您需要先激活该游戏，永久激活该游戏需要消耗您" + spendPoints + "个M币，您当前拥有" +
                    userPoints + "个M币，您是否要继续？";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    spendUserPoints(spendPoints);
                }
            });

        } else {
            alertMsg = "您需要先激活该游戏，永久激活该游戏需要消耗您" + spendPoints + "个M币，您当前拥有" +
                    userPoints + "个M币，M币不足，您可以通过点击确定获取M币。";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 显示广告墙
                    showDianjinOffers();
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
     * 添加 dianjin 的互动广告
     *
     * @param activity
     */
    public static void addDianjinAdView(final Activity activity) {
        context = activity;

        // 新增广告区域
//        RelativeLayout adArea = new RelativeLayout(context);
//        RelativeLayout.LayoutParams adAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        adAreaParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // 新增 dianjin 互动广告区域
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
     * 判断用户是否已经激活游戏
     *
     * @param context
     * @return
     */
    public static boolean isHadSpend(Context context) {
        // 是否已经消费
        boolean is_had_spend = PreferenceUtil.readRecord(context, IS_HAD_SPEND, false);
        if (is_had_spend) {
            PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
        }

        Log.d(TAG, "isHadSpend ,is_had_spend = " + is_had_spend);
        return is_had_spend;
    }

    /**
     * 游戏是否需要消费
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
     * 显示广告墙
     */
    private static void showDianjinOffers() {
        DianJinPlatform.showOfferWall(context, DianJinPlatform.Oriention.PORTRAIT);
    }

    /**
     * 从服务器获取获取用户的积分
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
     * 消耗用户的积分
     *
     * @param spendPoint
     */
    public static void spendUserPoints(float spendPoint) {
        // 标识激活游戏的请求已经提交
        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, true);

        // 订单号
        String orderSerial = TimeUtil.toLongOfyyyyMMddHHmmss() + TelephoneUtil.toImei(context);
        // 消费动作类型
        int actionId = ACTION_ID_1001;
        DianJinPlatform.consume(context, orderSerial, spendPoint, actionId, new WebServiceListener<Integer>() {
            @Override
            public void onResponse(int responseCode, Integer balance) {
                switch (responseCode) {
                    case DianJinPlatform.DIANJIN_SUCCESS:
                        Toast.makeText(context, "消费成功,游戏已经激活，您可以重新进入游戏。", Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
                        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
                        saveUserPoints(balance);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_REQUES_CONSUNE:
                        Toast.makeText(context, "支付请求失败",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:
                        Toast.makeText(context, "M币不足",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ACCOUNT_NO_EXIST:
                        Toast.makeText(context, "账号不存在",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ORDER_SERIAL_REPEAT:
                        Toast.makeText(context, "订单号重复",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT:
                        Toast.makeText(context,
                                "一次性交易金额超过最大限定金额",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_RETURN_CONSUME_ID_NO_EXIST:
                        Toast.makeText(context,
                                "不存在该类型的消费动作ID", Toast.LENGTH_SHORT)
                                .show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    default:
                        Toast.makeText(context, "未知错误,错误码为：" + responseCode, Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                }
            }
        });

    }


    /**
     * 本地获取用户积分
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
