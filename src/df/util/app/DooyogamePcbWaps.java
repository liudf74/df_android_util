package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.*;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;
import df.util.android.ApplicationUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogamePcbWaps {
    private static final String TAG = "df.util.app.DooyogamePcbWaps";
    private final static String USER_POINTS_KEY = "user_points";
    private final static String USER_POINT_UNIT_KEY = "point_unit";
    private final static String IS_HAD_GAVE_HINT_CHANCES = "is_had_gave_hint_chances";

    private final static String USER_HINT_TOTAL = "user_hint_total";
    private final static int FACTOR = 10;

    private static GetUserPoints getUserPoints = new GetUserPoints();
    private static Context context;
    private static Handler updateTextViewHandler = new Handler();

    /**
     * 启动的时候初始化 waps sdk
     *
     * @param ct
     */
    public static void initWapsSdk(Context ct) {
        Log.d(TAG, "initWapsSdk...");
        context = ct;
        AppConnect.getInstance(context);

        // 初始化用户积分
        initUserPoint();

        // 游戏第一次使用的时候，赠送用户积分
        boolean isHadGetChance = PreferenceUtil.readRecord(context, IS_HAD_GAVE_HINT_CHANCES, false);
        if (!isHadGetChance) {
            // 初始化用户自动完成游戏的机会，新开始游戏赠送5次机会
            int hintTotal = 5;
            saveUserHintTotal(hintTotal);
            PreferenceUtil.saveRecord(context, IS_HAD_GAVE_HINT_CHANCES, true);
        }
    }

    public static void initUserPoint() {
        Log.d(TAG, "initUserPoint...");
        getUserPoints.getUserPoints();
    }
    /**
     * 消耗用户拥有的自动完成游戏的机会一次
     */
    public static void useHintChance() {
        Log.d(TAG, "useHintChance...");
        int userHintTotal = getUserhintTotal();
        if (userHintTotal > 0) {
            userHintTotal = userHintTotal - 1;
            saveUserHintTotal(userHintTotal);
            showHintTotal();
        } else {
            new AlertDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMessage("您的游戏提示次数已经为0,您可以点击确定获取金币，每10个金币可以兑换1次提示的机会。")
                    .setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //To change body of implemented methods use File | Settings | File Templates.
                            showWapsOffers();
                        }
                    })
                    .setNegativeButton("取消", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }
                    })
                    .create()
                    .show();
        }
    }

    /**
     * 初始化用户自动完成游戏的次数
     * 显示在指定的TextView上
     *
     * @param ct
     */
    public static void initHintCount(Context ct) {
        Log.d(TAG, "initHintCount...");
        context = ct;
        showHintTotal();
    }


    /**
     * 显示用户剩余的自动完成的机会
     */
    private static void showHintTotal() {
        Log.d(TAG, "showHintTotal...");

        int hintTotalViewId = ResourceUtil.getIdResourceIdFromName(context, "texthintcount");
        Activity activity = (Activity) context;
        final TextView hintTotalTextView = (TextView) activity.findViewById(hintTotalViewId);

        final int hintTotal = getUserhintTotal();
        updateTextViewHandler.post(new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
                hintTotalTextView.setText(String.valueOf(hintTotal));
            }
        });
    }


    /**
     * 10点积分可以兑换一次游戏自动完成的机会
     *
     * @return
     */
    private static void pointsExchangeToHintTotal(int userPoints) {
        Log.d(TAG, "pointsExchangeToHintTotal,userPoints = " + userPoints);
        // 每10分兑换一次自动完成的机会
        int hintTotal = userPoints / FACTOR;
        if (hintTotal > 0) {
            // 将兑换了提示次数的积分消耗掉
            int spendPoints = hintTotal * FACTOR;
            SpendUserPoints spendUserPoints = new SpendUserPoints();
            spendUserPoints.spendUserPoints(spendPoints);
        }

    }

    /**
     * 判断是否还有游戏提示的次数
     *
     * @return
     */
    public static boolean isCanHint() {
        boolean isCanHint = false;
        int userHintTotal = getUserhintTotal();
        if (userHintTotal > 0) {
            isCanHint = true;
        }
        return isCanHint;
    }

    /**
     * 显示广告墙
     */
    public static void showWapsOffers() {
        AppConnect.getInstance(context).showOffers(context);
    }

    /**
     * 本地获取用户积分
     *
     * @return
     */
    private static int getUserPointsFromLocal() {
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

    private static void saveUserPointUnit(String pointUnit) {
        PreferenceUtil.saveRecord(context, USER_POINT_UNIT_KEY, pointUnit);
    }

    private static void saveUserHintTotal(int hintTotal) {
        Log.d(TAG, "saveUserHintTotal ,hintTotal = " + hintTotal);
        PreferenceUtil.saveRecord(context, USER_HINT_TOTAL, hintTotal);
    }

    public static int getUserhintTotal() {
        int userHintTotal = PreferenceUtil.readRecord(context, USER_HINT_TOTAL, 0);
        Log.d(TAG, "getUserhintTotal , userHintTotal = " + userHintTotal);
        return userHintTotal;
    }

    public static void showAbout() {

        Dialog d = new Dialog(context) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                dismiss();
                return true;
            }
        };
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(ResourceUtil.getLayoutResourceIdFromName(context, "information"));
        d.show();
    }

    public static void showGameHelp() {
        Dialog d = new Dialog(context) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                dismiss();
                return true;
            }
        };
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(ResourceUtil.getLayoutResourceIdFromName(context, "game_help"));
        d.show();
    }

    public static void exitGame(){
        ApplicationUtil.exitApp(context);
    }


    /**
     * 处理 获取用户积分 的状态的内部类
     */
    private static class GetUserPoints implements UpdatePointsNotifier {

        public void getUserPoints() {
            AppConnect.getInstance(context).getPoints(GetUserPoints.this);
        }

        @Override
        public void getUpdatePoints(String pointUnit, int pointTotal) {
            Log.d(TAG, "GetUserPoints, getUpdatePoints, pointTotal = " + pointTotal);

            // 将获取的积分兑换成用户的游戏提示次数
            pointsExchangeToHintTotal(pointTotal);

            saveUserPointUnit(pointUnit);
        }

        @Override
        public void getUpdatePointsFailed(String s) {
            // nothing to do...
            Log.d(TAG, "GetUserPoints, getUpdatePointsFailed, errorMsg = " + s);
        }
    }

    /**
     * 处理 消耗用户积分 的状态的内部类
     */
    private static class SpendUserPoints implements UpdatePointsNotifier {
        private static int points;

        public void spendUserPoints(int spendPoints) {
            points = spendPoints;
            AppConnect.getInstance(context).spendPoints(spendPoints, SpendUserPoints.this);
        }

        @Override
        public void getUpdatePoints(String pointUnit, int pointTotal) {
            Log.d(TAG, "SpendUserPoints, getUpdatePoints, pointTotal = " + pointTotal);

            // 当消耗积分成功的时候才会给用户添加相应的游戏提示次数
            int hintTotal = points / FACTOR;
            int userHintTotal = getUserhintTotal();
            userHintTotal = userHintTotal + hintTotal;
            saveUserHintTotal(userHintTotal);
//            // 再TextView上显示用户最新的提示次数
//            showHintTotal();
        }

        @Override
        public void getUpdatePointsFailed(String s) {
            final String errorMsg = s;
            Log.d(TAG, "SpendUserPoints, getUpdatePointsFailed, errorMsg = " + errorMsg);
        }
    }
}
