package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.LinearLayout;
import com.uucun.adsdk.UUAppConnect;
import com.uucun.adsdk.UpdatePointListener;
import df.util.android.ApplicationUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.type.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-3-19
 * Time: 下午7:16
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMntsAppjoy {
    public static final String TAG = "df.util.app.DooyogameMntsAppjoy";
    public static LinearLayout adLinearLayout;
    // 用户的积分
    public static int USER_POINT = 0;
    // 积分单位
    public static String POINT_UNIT = "";
    // 消费错误提示
    public static String SPEND_ERROR_MSG = "";
    // 获取积分错误提示
    public static String GET_POINT_ERROR_MSG = "";
    // 判断能否继续下一关
    public static boolean canRun = false;

    public static String KEY_NAME = "level_numbers";

    /**
     * 初始化appjoy接口
     *
     * @param context
     */
    public static void initAppjoySdk(Context context) {
        Log.d(TAG, "init appjoy sdk...");
        UUAppConnect.getInstance(context).initSdk();
    }

    /**
     * 断开appjoy接口
     *
     * @param context
     */
    public static void exitAppjoySdk(Context context) {
        UUAppConnect.getInstance(context).exitSdk();
    }

    /**
     * 获取用户的积分
     *
     * @param context
     * @return
     */
    public static void getUserPoints(Context context) {
        UUAppConnect.getInstance(context).getPoints(
                new UpdatePointListener() {
                    @Override
                    public void onError(String s) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        GET_POINT_ERROR_MSG = s;
                    }

                    @Override
                    public void onSuccess(String s, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        POINT_UNIT = s;
                        USER_POINT = i;
                    }
                }
        );
    }

    /**
     * 消耗用户的积分
     *
     * @return
     */
    public static void spendUserPoints(final Context context, int amount) {
        UUAppConnect.getInstance(context).spendPoints(amount, new UpdatePointListener() {
            @Override
            public void onError(String error_msg) {
                //To change body of implemented methods use File | Settings | File Templates.
                canRun = false;
                SPEND_ERROR_MSG = error_msg;
                showOfferWall(context, SPEND_ERROR_MSG);
                PreferenceUtil.saveRecord(context, "is_had_spend", false);
            }

            @Override
            public void onSuccess(String point_unit, int user_point) {
                //To change body of implemented methods use File | Settings | File Templates.
                POINT_UNIT = point_unit;
                USER_POINT = user_point;
                canRun = true;
                PreferenceUtil.saveRecord(context, "is_had_spend", true);
            }
        });
    }

    /**
     * 显示积分墙，提示用户去下载软件获取积分
     *
     * @param context
     */
    public static void showOfferWall(final Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        builder.setMessage(msg + ",您可以点击这里获取积分！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
                UUAppConnect.getInstance(context).showOffers();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
                exitAppjoySdk(context);
                ApplicationUtil.exitApp(context);
            }
        });
        builder.show();
    }

    /**
     * 显示活动广告
     */
    public static void showInteractiveAdvertisement(Activity context) {
        Log.d(TAG, "show interactive advertisement...");
        int id = ResourceUtil.getIdResourceIdFromName(context, "adLayout");
        Log.d(TAG, "The return id = " + id);
        adLinearLayout = (LinearLayout) context.findViewById(id);
        UUAppConnect.getInstance(context).showBanner(
                adLinearLayout, 30);
        Log.d(TAG, "Set adLinearlayout to null...");
        adLinearLayout = null;
    }


    /**
     * bug 搜集接口
     *
     * @param context
     */
    public static void catchBugs(Context context) {
        UUAppConnect.getInstance(context).startupCatchBugs();
    }

    /**
     * 判断用户是否能够进行下一关操作
     *
     * @param context
     * @param level
     * @return
     */
    public static void canRunNexLevel(Context context, int level) {
        Log.d(TAG, "the current level = " + level);
        int spendPoint = 0;
        int levelCount;
        String newLevelValues;
        boolean canRun = getCanRun(context);

        // 获取关卡字符串
        String oldLevelValues = getLevelValuesByKey(context);
        if (canRun) {
            if (oldLevelValues.equals("")) {
                newLevelValues = String.valueOf(level);
                PreferenceUtil.saveRecord(context, KEY_NAME, newLevelValues);
            } else {
                newLevelValues = oldLevelValues + "," + level;
                PreferenceUtil.saveRecord(context, KEY_NAME, newLevelValues);
            }
        } else {
            newLevelValues = oldLevelValues;
        }
        Log.d(TAG, "The newLevelValues = " + newLevelValues);

        // 获取用户已经同通过的关卡
        levelCount = getUserLevelCount(newLevelValues);
        Log.d(TAG, "The levelCount = " + levelCount);

        // 达到第5关开始，包括第5关，每5关消耗100积分
        if (levelCount >= 5 && levelCount < 30) {
            if (levelCount % 5 == 0) {
                PreferenceUtil.saveRecord(context, "is_need_spend", true);
                // 消耗积分
                spendPoint = 100;
                // 提示用户即将需要消耗积分
                wornSpendPointDialog(context, levelCount, spendPoint);
            }
        }
        // 达到第30关开始，包括第30关，每10关消耗280积分
        else if (levelCount >= 30 && levelCount < 60) {
            if (levelCount % 10 == 0) {
                PreferenceUtil.saveRecord(context, "is_need_spend", true);
                spendPoint = 280;
                // 提示用户即将需要消耗积分
                wornSpendPointDialog(context, levelCount, spendPoint);
            }
        }
        // 达到60关，需要扣除500积分
        else if (levelCount == 60) {
            PreferenceUtil.saveRecord(context, "is_need_spend", true);
            spendPoint = 500;
            // 提示用户即将需要消耗积分
            wornSpendPointDialog(context, levelCount, spendPoint);
        }

    }

    public static void canNotStarGame(Context context) {
        int levelCount = 0;
        int spendPoint = 0;

        // 获取关卡字符串
        String oldLevelValues = getLevelValuesByKey(context);
        // 获取用户已经同通过的关卡
        levelCount = getUserLevelCount(oldLevelValues);
        Log.d(TAG, "The levelCount = " + levelCount);

        // 达到第5关开始，包括第5关，每5关消耗100积分
        if (levelCount >= 5 && levelCount < 30) {
            if (levelCount % 5 == 0) {
                PreferenceUtil.saveRecord(context, "is_need_spend", true);
                // 消耗积分
                spendPoint = 100;
            }
        }
        // 达到第30关开始，包括第30关，每10关消耗280积分
        else if (levelCount >= 30 && levelCount < 60) {
            PreferenceUtil.saveRecord(context, "is_need_spend", true);
            spendPoint = 280;
        }
        // 达到60关，需要扣除500积分
        else if (levelCount == 60) {
            PreferenceUtil.saveRecord(context, "is_need_spend", true);
            spendPoint = 500;
        }
        // 提示用户即将需要消耗积分
        wornSpendPointDialog(context, levelCount, spendPoint);
    }


    /**
     * 获取关卡数
     *
     * @param levelValues
     * @return
     */
    public static int getUserLevelCount(String levelValues) {
        int levelCount = 0;
        String[] valuesSet = StringUtil.split(levelValues, ",", true);
        levelCount = valuesSet.length;

        return levelCount;
    }


    /**
     * 提示用户进行下一关将会消耗用户的积分
     *
     * @param context
     * @param levelCount
     */
    public static void wornSpendPointDialog(final Context context, int levelCount, final int spenPoint) {
        Log.d(TAG, "Worning user spend point....");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        // 获取用户的金币
        getUserPoints(context);
        if (GET_POINT_ERROR_MSG.equals("") && USER_POINT >= spenPoint) {
            builder.setMessage("恭喜您将进入第" + levelCount + "关，需要消耗您" + spenPoint + "个金币，您当前拥有" + USER_POINT + "个金币，您是否要继续?");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 消耗用户的金币
                    spendUserPoints(context, spenPoint);
                }
            });
            builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ApplicationUtil.exitApp(context);
                    exitAppjoySdk(context);
                }
            });
            builder.show();
        } else {
            String msg = "您将进入第" + levelCount + "关，需要消耗您" + spenPoint + "个金币，您当前拥有" + USER_POINT + "个金币，您的金币不足";
            showOfferWall(context, msg);
        }
    }

    /**
     * 获取关卡编号字符串
     *
     * @param context
     * @return
     */
    public static String getLevelValuesByKey(Context context) {
        String levelValues;
        levelValues = PreferenceUtil.readRecord(context, KEY_NAME, "");
        return levelValues;
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

}
