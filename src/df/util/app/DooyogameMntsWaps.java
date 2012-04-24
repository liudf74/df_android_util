package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.type.StringUtil;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-3-26
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMntsWaps implements UpdatePointsNotifier {

    public static final String TAG = "df.util.app.DooyogameMntsDianjin";

    // 记录用户已经通过的字符串
    public static String KEY_NAME = "level_numbers";
    public static Context context = null;
    public static DialogInterface.OnClickListener gotoNextLevelAction;
    // 用户的积分相对应的字符串KEY
    private static final String USER_POINT_KEY = "user_points";
    // 虚拟货币名称
    private static String USER_POINT_UNIT = "point_unit";
    private static final DooyogameMntsWaps dooyogameMntsWaps = new DooyogameMntsWaps();
    private static LinearLayout adLinearLayout;
    private static ImageButton imageButton;
    final static Handler mHandler = new Handler();
    // 互动广告的状态
    private final static String ADLINEARLAYOUT_STATE = "ad_state";

    final static Handler handler = new Handler() {
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
     * 初始化WAPS数据统计接口
     */
    private void initWapsAppConnect() {
        Log.d(TAG, "Initialize waps AppConnect...");
        AppConnect.getInstance(context);
        getUserPointsFromWaps();
    }

    private void getUserPointsFromWaps() {
        // 初始化用户积分
        AppConnect.getInstance(context).getPoints(this);
    }

    /**
     * 显示 waps 的互动广告
     *
     * @param activity
     */
    public static void showWapsAdView(Activity activity) {
        context = activity;
        // 初始化数据统计接口
        dooyogameMntsWaps.initWapsAppConnect();

        boolean isShowAd = PreferenceUtil.readRecord(activity, ADLINEARLAYOUT_STATE, true);
        Log.d(TAG, "showWapsAdView ,isShowAd = " + isShowAd);
        if (isShowAd) {
            int adLinearLayoutId = ResourceUtil.getIdResourceIdFromName(activity, "AdLinearLayout");
            int adImageButtomId = ResourceUtil.getIdResourceIdFromName(activity, "ad_close");

            Log.d(TAG, "viewId = " + adLinearLayoutId);
            adLinearLayout = (LinearLayout) activity.findViewById(adLinearLayoutId);
            imageButton = (ImageButton) activity.findViewById(adImageButtomId);
            new AdView(activity, adLinearLayout).DisplayAd(10);

            // 互动广告开关
            isShowAdCloseImageButton();

            // 给ImageButton添加点击事件监听
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isCloseAdLinearLayoutDialog();
                }
            });
        }
    }

    /**
     * 消耗用户的积分
     *
     * @param amount
     */
    public void spendUserPoints(int amount) {
        AppConnect.getInstance(context).spendPoints(amount, DooyogameMntsWaps.this);
    }

    /**
     * 显示waps广告墙
     */
    public static void showWpasOfferWall() {
        AppConnect.getInstance(context).showOffers(context);
    }

    /**
     * 进入下一关操作
     *
     * @param c
     * @param level
     */
    public static void runNextLevel(Context c, int level, DialogInterface.OnClickListener gotoNextLevelListener) {
        context = c;
        gotoNextLevelAction = gotoNextLevelListener;
        String newLevelValues;
        int levelCount;
        int spendPoint;
        boolean is_spend_next_level;

        // 获取以存储的关卡编号的字符串
        String levelValues = PreferenceUtil.readRecord(context, KEY_NAME, "");
        Log.d(TAG, "The levels are activated, levelValues = " + levelValues);

        // 判断用户当前的关卡是是否已经激活
        boolean is_pass_level = isPass(levelValues, level);

        // 关卡已经激活
        if (is_pass_level) {
            Log.d(TAG, "The level is activated, level = " + level);
            newLevelValues = levelValues;
            is_spend_next_level = isSpendPointNextLevel(levelValues, level);
            boolean canRun = getCanRun(context);
            Log.d(TAG, "is_spend_next_level = " + is_spend_next_level);
            if (!is_spend_next_level || (is_spend_next_level && canRun)) {
                PreferenceUtil.saveRecord(context, "is_need_spend", false);
                return;
            }
        }
        // 关卡未被激活
        else {
            boolean canRun = getCanRun(context);
            if (canRun) {
                if (levelValues.equals("")) {
                    newLevelValues = String.valueOf(level);
                } else {
                    newLevelValues = levelValues + "," + level;
                }
                // 存储关卡编号
                PreferenceUtil.saveRecord(context, KEY_NAME, newLevelValues);
            } else {
                newLevelValues = levelValues;
            }
        }

        Log.d(TAG, "The newLevelValues = " + newLevelValues);
        levelCount = getLevelCount(newLevelValues);
        Log.d(TAG, "The levelCount = " + levelCount);

        // 获取进入下一关所需要消耗的积分
        spendPoint = getUserSpendPoint((levelCount + 1));
        Log.d(TAG, "levelCount = " + levelCount + " ,need to spend " + spendPoint + " point...");

        // 如果需要消耗积分，提示用户
        if (spendPoint > 0) {
            // 转换成消费状态
            PreferenceUtil.saveRecord(context, "is_need_spend", true);
            warnSpendPointDialog(levelCount, spendPoint);
        }
    }

    /**
     * 当前玩的关卡是否已经激活
     *
     * @return
     */
    private static boolean isPass(String levelValues, int level) {
        boolean flag = false;
        String levelString = String.valueOf(level);
        String[] levelValuesSet = StringUtil.split(levelValues, ",", true);

        for (int i = 0; i < levelValuesSet.length; i++) {
            if (levelString.equals(levelValuesSet[i])) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断当前激活的关卡去下一关是否需要消费
     *
     * @return
     */
    private static boolean isSpendPointNextLevel(String levelValues, int level) {
        boolean flag = false;
        int levelCount = getLevelCount(levelValues);
        int nextLevelCount = levelCount + 1;

        int spendPoint = getUserSpendPoint(nextLevelCount);
        if (spendPoint != 0) {
            flag = levelValues.endsWith(String.valueOf(level));
        }
        return flag;
    }

    /**
     * 提示用户进行下一关将会消耗用户的积分
     *
     * @param levelCount
     */
    private static void warnSpendPointDialog(final int levelCount, final int spenPoint) {
        Log.d(TAG, "warning user spend point....");

        final int userPoints = getUserPoints();
        final String pointUnit = PreferenceUtil.readRecord(context, USER_POINT_UNIT, "");
        if (userPoints >= spenPoint) {
            String warnMsg = "您将进入" + (levelCount + 1) + "关，需要消耗您" + spenPoint + "个" + pointUnit + ",您当前拥有" + userPoints + "个" + pointUnit + ",您是否继续？";
            new AlertDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMessage(warnMsg)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dooyogameMntsWaps.spendUserPoints(spenPoint);
                            saveUserPoints((userPoints - spenPoint));
                            // 标识为以消费状态
                            PreferenceUtil.saveRecord(context, "is_had_spend", true);
                            if (gotoNextLevelAction != null) {
                                gotoNextLevelAction.onClick(null, 9);
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }
                    })
                    .create()
                    .show();
        } else {
            String warnMsg = "您将进入" + (levelCount + 1) + "关，需要消耗您" + spenPoint + "个" + pointUnit + ",您的" + pointUnit + "不足，您可以点击确定获取。";
            new AlertDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMessage(warnMsg)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showWpasOfferWall();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
     * 获取进入下一关所需要消耗的积分，如果不需要则返回0
     *
     * @param levelCount
     * @return
     */
    private static int getUserSpendPoint(int levelCount) {
        int spendPoint = 0;
        // 达到第5关开始，包括第5关，每5关消耗100积分
        if (levelCount >= 5 && levelCount < 30) {
            if (levelCount % 5 == 0) {
                spendPoint = 100;
            }
        }
        // 达到第30关开始，包括第30关，每10关消耗280积分
        else if (levelCount >= 30 && levelCount < 60) {
            if (levelCount % 10 == 0) {
                spendPoint = 280;
            }
        }
        // 达到60关，需要扣除500积分
        else if (levelCount == 60) {
            spendPoint = 500;
        }
        return spendPoint;
    }

    /**
     * 获取关卡数
     *
     * @param levelValues
     * @return
     */
    private static int getLevelCount(String levelValues) {
        int levelCount;
        String[] levelValuesSet = StringUtil.split(levelValues, ",", true);
        levelCount = levelValuesSet.length;
        return levelCount;
    }

    /**
     * 判断用户能否进行下一关的操作
     * 说明：1. 判断用户进行下一关是否需要消费
     * 2. 判断用户是否已经消费
     *
     * @param context
     * @return
     */
    public static boolean getCanRun(Context context) {
        boolean flag = false;
        // 是否已经消费
        boolean is_had_spend = PreferenceUtil.readRecord(context, "is_had_spend", false);
        // 只有在用户消费之后才可以取消需要消费
        if (is_had_spend) {
            PreferenceUtil.saveRecord(context, "is_need_spend", false);
            PreferenceUtil.saveRecord(context, "is_had_spend", false);
        }

        // 是否要消费
        boolean is_need_spend = PreferenceUtil.readRecord(context, "is_need_spend", false);
        Log.d(TAG, "is_need_spend = " + is_need_spend + ", is_had_spend = " + is_had_spend);

        if (!is_need_spend) {
            flag = true;
        }

        Log.d(TAG, "if can run ,flag = " + flag);
        return flag;
    }


    /**
     * AppConnect.getPoints()方法的实现，必须实现
     *
     * @param currencyName 虚拟货币名称.
     * @param pointTotal   虚拟货币余额.
     */
    public void getUpdatePoints(String currencyName, int pointTotal) {
        Log.d(TAG, "currencyName = " + currencyName + ",pointTotal = " + pointTotal);
        // 本地保存用户的积分
        saveUserPoints(pointTotal);
        PreferenceUtil.saveRecord(context, USER_POINT_UNIT, currencyName);
    }

    /**
     * 获取失败，提示用户
     *
     * @param error
     */
    public void getUpdatePointsFailed(String error) {
        Log.d(TAG, "getUpdatePointsFailed , error = " + error);
        final String msg = error;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 本地保存用户的积分
     *
     * @param points
     */
    private static void saveUserPoints(int points) {
        PreferenceUtil.saveRecord(context, USER_POINT_KEY, points);
    }

    /**
     * 从本地获取用户的积分
     *
     * @return
     */
    private static int getUserPoints() {
        dooyogameMntsWaps.getUserPointsFromWaps();

        int userPoints = 0;
        try {
            Thread.currentThread().sleep(500);
            userPoints = PreferenceUtil.readRecord(context, USER_POINT_KEY, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return userPoints;
    }


    /**
     * 动态广告开关
     */
    private static void isShowAdCloseImageButton() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int linearLayoutHeigh = adLinearLayout.getHeight();
//                Log.d(TAG, "isShowAdCloseImageButton, linearLayoutHeigh = " + linearLayoutHeigh);
                Message message = new Message();
                if (linearLayoutHeigh > 8) {
                    message.what = 1;
                } else {
                    message.what = 0;
                }
                handler.sendMessage(message);
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 10000, 2000);
    }


    /**
     * 提示用户是否关闭互动广告
     */
    private static void isCloseAdLinearLayoutDialog() {
        // 获取用的积分
        final int userPoints = getUserPoints();
        Log.d(TAG, "isCloseAdLinearLayoutDialogv, userPoints = " + userPoints);
        final int closeAdSpend = 80;
        String pointUnit = PreferenceUtil.readRecord(context, USER_POINT_UNIT, "金币");
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("温馨提示");
        if (userPoints >= closeAdSpend) {
            alertMsg = "关闭广告需要消耗您" + closeAdSpend + "个" + pointUnit + ",您当前拥有" + userPoints + "个" + pointUnit + ",您是否要继续？";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dooyogameMntsWaps.spendUserPoints(closeAdSpend);
                    saveUserPoints(userPoints - closeAdSpend);
                    PreferenceUtil.saveRecord(context, ADLINEARLAYOUT_STATE, false);
                    imageButton.setVisibility(View.GONE);
                    adLinearLayout.setVisibility(View.GONE);
                }
            });
        } else {
            alertMsg = "关闭广告需要消耗您" + closeAdSpend + "个" + pointUnit + ",您当前拥有" + userPoints + "个" + pointUnit + "," + pointUnit + "不足,您可以点击确定免费获取" + pointUnit;
            builder.setMessage(alertMsg);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showWpasOfferWall();
                }
            });
        }
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        builder.show();
    }
}
