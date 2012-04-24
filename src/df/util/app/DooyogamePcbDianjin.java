package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nd.dianjin.DianJinPlatform;
import com.nd.dianjin.webservice.WebServiceListener;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;
import df.util.android.ApplicationUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
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
public class DooyogamePcbDianjin {
    private static final String TAG = "df.util.app.DooyogamePcbDianjin";
    private final static String IS_HAD_GAVE_HINT_CHANCES = "is_had_gave_hint_chances";

    private final static String USER_HINT_TOTAL = "user_hint_total";
    private final static int FACTOR = 10;

    private static Context context;
    private static Handler updateTextViewHandler = new Handler();

    // 消费动作类型,激活游戏
    public static final int ACTION_ID_1001 = 1001;

    /**
     * 启动的时候初始化 dianjin sdk
     *
     * @param ct
     */
    public static void initDianjinSdk(Context ct) {
        Log.d(TAG, "initDianjinSdk...");
        context = ct;
        DianJinPlatform.initialize(context, 2163, "17acb59b834d3bd63b42426193fdf657");

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
        getUserPoints();
    }

    /**
     * 从服务器获取获取用户的积分
     */
    private static void getUserPoints() {
        Log.d(TAG, "getUserPoints...");
        DianJinPlatform.getBalance(context, new WebServiceListener<Float>() {
            @Override
            public void onResponse(int respCode, Float balance) {
                if (respCode == DianJinPlatform.DIANJIN_SUCCESS) {
                    pointsExchangeToHintTotal(balance);
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
    public static void spendUserPoints(final float spendPoint) {
        // 订单号
        String orderSerial = TimeUtil.toLongOfyyyyMMddHHmmss() + TelephoneUtil.toImei(context);
        // 消费动作类型
        int actionId = ACTION_ID_1001;
        DianJinPlatform.consume(context, orderSerial, spendPoint, actionId, new WebServiceListener<Integer>() {
            @Override
            public void onResponse(int responseCode, Integer balance) {
                switch (responseCode) {
                    case DianJinPlatform.DIANJIN_SUCCESS:
                        addNewHintTotal(spendPoint);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_REQUES_CONSUNE:
                        Log.d(TAG, "支付请求失败");
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:
                        Log.d(TAG, "M币不足");
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ACCOUNT_NO_EXIST:
                        Log.d(TAG, "账号不存在");
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ORDER_SERIAL_REPEAT:
                        Log.d(TAG, "订单号重复");
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT:
                        Log.d(TAG, "一次性交易金额超过最大限定金额");
                        break;
                    case DianJinPlatform.DIANJIN_RETURN_CONSUME_ID_NO_EXIST:
                        Log.d(TAG, "不存在该类型的消费动作ID");
                        break;
                    default:
                        Log.d(TAG, "未知错误,错误码为：" + responseCode);
                }
            }
        });

    }

    /**
     * 新增游戏提示次数
     *
     * @param spendPoint
     */
    private static void addNewHintTotal(float spendPoint) {
        // 当消耗积分成功的时候才会给用户添加相应的游戏提示次数
        int hintTotal = (int) (spendPoint / FACTOR);
        int userHintTotal = getUserhintTotal();
        userHintTotal = userHintTotal + hintTotal;
        saveUserHintTotal(userHintTotal);
        // 再TextView上显示用户最新的提示次数
//        showHintTotal();
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
                            showDianjinOffers();
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
    private static void pointsExchangeToHintTotal(float userPoints) {
        Log.d(TAG, "pointsExchangeToHintTotal,userPoints = " + userPoints);
        // 每10分兑换一次自动完成的机会
        int hintTotal = (int) (userPoints / FACTOR);
        if (hintTotal > 0) {
            // 将兑换了提示次数的积分消耗掉
            int spendPoints = hintTotal * FACTOR;
            spendUserPoints(spendPoints);
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
    public static void showDianjinOffers() {
        DianJinPlatform.showOfferWall(context, DianJinPlatform.Oriention.PORTRAIT);
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

}
