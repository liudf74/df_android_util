package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameWmmtWaps {
    private static final String TAG = "df.util.app.DooyogameWmmtWaps";
    private final static String USER_POINTS_KEY = "user_points";
    private final static String USER_POINT_UNIT_KEY = "point_unit";
    private final static String IS_HAD_SPEND = "is_had_spend";
    private final static String IS_NEED_SPEND = "is_need_spend";
    private final static String IS_HAD_REQUEST = "is_had_request";
    private final static String IS_HAD_SPEND_CLOSE_AD = "is_had_spend_close_ad";

    private static LinearLayout adWapsArea;
    private static ImageButton imageButton;

    private static GetUserPoints getUserPoints = new GetUserPoints();
    private static SpendUserPoints spendUserPoints = new SpendUserPoints();
    private static Context context;
    private static Handler updatePointsHandler = new Handler();
    private static Handler closeAdHandler;
    private final static Timer timer = new Timer();

    final static Handler buttomHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);    //To change body of overridden methods use File | Settings | File Templates.
            int w = msg.what;
            switch (w) {
                case 0:
                    imageButton.setVisibility(View.GONE);
                    break;
                case 1:
                    imageButton.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };


    /**
     * 启动的时候初始化 waps sdk
     *
     * @param ct
     */
    public static void initWapsSdk(Context ct) {
        context = ct;
        AppConnect.getInstance(context);

        // 初始化用户积分
        getUserPoints.getUserPoints();
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

        // 添加消费标识
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final int spendPoints = 100;
        int userPonts = getUserPointsFromLocal();
        String pointUnit = getUserPointUnit();
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
                    spendUserPoints.spendUserPoints(spendPoints);
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
                    showWapsOffers();
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
     * 添加 waps 的互动广告
     *
     * @param activity
     */
    public static void addWapsAdView(final Activity activity) {
        context = (Context) activity;

        // 判断用户是否已经消费关闭互动广告
        boolean is_had_spend_close_ad = PreferenceUtil.readRecord(context, IS_HAD_SPEND_CLOSE_AD, false);
        if (is_had_spend_close_ad) {
            return;
        }

        // 新增广告区域
        RelativeLayout adArea = new RelativeLayout(context);
        RelativeLayout.LayoutParams adAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        adAreaParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // 新增 waps 互动广告区域
        adWapsArea = new LinearLayout(context);
        RelativeLayout.LayoutParams adWapsAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        adWapsArea.setGravity(LinearLayout.VERTICAL);
        adArea.addView(adWapsArea, adWapsAreaParams);

        // 新增广告关闭按钮
        int rmAdid = ResourceUtil.getDrawableResourceIdFromName(context, "rm_ad");
        imageButton = new ImageButton(context);
        RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imageButton.setBackgroundResource(rmAdid);
        imageButton.setVisibility(View.GONE);
        imageButton.setLayoutParams(imageButtonParams);

        adArea.addView(imageButton);

        activity.addContentView(adArea, adAreaParams);
        new AdView(activity, adWapsArea).DisplayAd();

        // 是否显示广告关闭按钮
        isShowAdCloseButtom();
        // 指导用户去消费关闭互动广告条
        warnUserSpendForCloseAd();
    }

    /**
     * 提示用户关闭互动广告需要消耗一定的积分
     */
    private static void warnUserSpendForCloseAd() {

        // 添加关闭按钮的点击事件的监听
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userPoints = getUserPointsFromLocal();
                final int spendPoints = 80;

                // 提示用户关闭互动广告
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("温馨提示");
                if (userPoints > spendPoints) {
                    builder.setMessage("关闭广告条需要花费您" + spendPoints + "金币，" +
                            "您当前拥有" + userPoints + "个金币，您是否继续？");
                    builder.setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 消耗用户的积分
                            spendUserPoints.spendUserPoints(spendPoints);
                            closeAdHandler = new Handler();
                            Toast.makeText(context, "请求已经提交!当扣费成功之后广告条将会自动消失。", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    builder.setMessage("关闭广告条需要花费您" + spendPoints + "金币，" +
                            "您当前拥有" + userPoints + "个金币，金币不足，你可以点击确定免费获取金币");
                    builder.setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 显示广告墙
                            showWapsOffers();
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
        });
    }

    private static void isShowAdCloseButtom() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int adAreaHeight = adWapsArea.getHeight();
                Log.d(TAG, "adAreaHeight = " + adAreaHeight);
                if (adAreaHeight > 40 && imageButton.getVisibility() == View.GONE) {
                    Message message = new Message();
                    message.what = 1;
                    buttomHandler.sendMessage(message);
                }

                if (adAreaHeight < 40 && imageButton.getVisibility() == View.VISIBLE) {
                    Message message = new Message();
                    message.what = 0;
                    buttomHandler.sendMessage(message);
                }

            }
        };

        timer.schedule(timerTask, 1500, 2000);
    }

    /**
     * 判断用户是否已经激活游戏
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
     * 显示广告墙
     */
    private static void showWapsOffers() {
        AppConnect.getInstance(context).showOffers(context);
    }

    /**
     * 本地获取用户积分
     *
     * @return
     */
    private static int getUserPointsFromLocal() {
        getUserPoints.getUserPoints();
        return PreferenceUtil.readRecord(context, USER_POINTS_KEY, 0);

    }

    /**
     * 获取虚拟货币的单位
     *
     * @return
     */
    private static String getUserPointUnit() {
        return PreferenceUtil.readRecord(context, USER_POINT_UNIT_KEY, "金币");
    }

    private static void saveUserPoints(int points) {
        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, points);
    }

    private static void saveUserPointUnit(String pointUnit) {
        PreferenceUtil.saveRecord(context, USER_POINT_UNIT_KEY, pointUnit);
    }


    /**
     * 处理获取用户积分的状态的内部类
     */
    private static class GetUserPoints implements UpdatePointsNotifier {

        public void getUserPoints() {
            AppConnect.getInstance(context).getPoints(GetUserPoints.this);
        }


        @Override
        public void getUpdatePoints(String pointUnit, int pointTotal) {
            Log.d(TAG, "GetUserPoints, getUpdatePoints, pointTotal = " + pointTotal);
            saveUserPoints(pointTotal);
            saveUserPointUnit(pointUnit);
        }

        @Override
        public void getUpdatePointsFailed(String s) {
            // nothing to do...
            Log.d(TAG, "GetUserPoints, getUpdatePointsFailed, errorMsg = " + s);
        }
    }

    /**
     * 处理更新用户积分的状态的内部类
     */
    private static class SpendUserPoints implements UpdatePointsNotifier {

        public void spendUserPoints(int spendPoints) {
            AppConnect.getInstance(context).spendPoints(spendPoints, SpendUserPoints.this);
            PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, true);

        }

        @Override
        public void getUpdatePoints(String pointUnit, int pointTotal) {
            Log.d(TAG, "SpendUserPoints, getUpdatePoints, pointTotal = " + pointTotal);
            saveUserPoints(pointTotal);
            saveUserPointUnit(pointUnit);
            PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
            PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
            PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);

            // 判断是否触发了关闭互动广告的接口
            if (closeAdHandler != null) {
                PreferenceUtil.saveRecord(context, IS_HAD_SPEND_CLOSE_AD, true);

                closeAdHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setVisibility(View.GONE);
                        adWapsArea.setVisibility(View.GONE);
                        timer.cancel();
                        closeAdHandler = null;
                    }
                });
            }
            // 进行激活游戏成功后的提示信息
            else {
                updatePointsHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "消费成功，您可以重新进入游戏。", Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

        @Override
        public void getUpdatePointsFailed(String s) {
            final String errorMsg = s;
            Log.d(TAG, "SpendUserPoints, getUpdatePointsFailed, errorMsg = " + errorMsg);
            updatePointsHandler.post(new Runnable() {
                @Override
                public void run() {
                    //To change body of implemented methods use File | Settings | File Templates.
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
