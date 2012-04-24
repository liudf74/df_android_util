package df.util.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import com.nd.dianjin.DianJinPlatform;
import com.nd.dianjin.webservice.WebServiceListener;
import df.util.android.PreferenceUtil;
import df.util.android.TelephoneUtil;
import df.util.type.StringUtil;
import df.util.type.TimeUtil;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-3-26
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMntsDianjin {

    public static final String TAG = "df.util.app.DooyogameMntsDianjin";

    // 记录用户已经通过的字符串
    public static String KEY_NAME = "level_numbers";

    // 消费动作类型扣100积分
    public static final int ACTION_ID_100 = 1001;
    // 消费动作类型扣100积分
    public static final int ACTION_ID_280 = 1002;
    // 消费动作类型扣100积分
    public static final int ACTION_ID_500 = 1003;

    public static Context context = null;
    public static DialogInterface.OnClickListener gotoNextLevelAction;

    /**
     * 初始化点金SDK
     *
     * @param context
     */
    public static void initDianjinSdk(Context context) {
        Log.d(TAG, "Initialize Dianjin SDK...");
        DianJinPlatform.initialize(context, 1824, "bd1f1ed37d21980f22570e6140e7177f");
    }

    /**
     * 显示点金推广墙
     *
     * @param context
     */
    public static void showDianjinOfferWall(final Context context) {
        DianJinPlatform.showOfferWall(context, DianJinPlatform.Oriention.PORTRAIT);
    }

    /**
     * 消耗用户的积分
     *
     * @param spendPoint
     */
    public static void spendUserPoints(int spendPoint) {
        // 订单号
        String orderSerial = TimeUtil.toLongOfyyyyMMddHHmmss() + TelephoneUtil.toImei(context);
        // 消费的积分
        float amount = spendPoint;

        // 消费动作类型
        int actionId = getActionId(spendPoint);

        DianJinPlatform.consume(context, orderSerial, amount, actionId, new WebServiceListener<Integer>() {
            @Override
            public void onResponse(int responseCode, Integer t) {
                switch (responseCode) {
                    case DianJinPlatform.DIANJIN_SUCCESS:
                        Toast.makeText(context, "消费成功", Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, "is_had_spend", true);
                        if (gotoNextLevelAction != null) gotoNextLevelAction.onClick(null, 9);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_REQUES_CONSUNE:
                        Toast.makeText(context, "支付请求失败",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:
                        Toast.makeText(context, "M币不足",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ACCOUNT_NO_EXIST:
                        Toast.makeText(context, "账号不存在",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ORDER_SERIAL_REPEAT:
                        Toast.makeText(context, "订单号重复",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT:
                        Toast.makeText(context,
                                "一次性交易金额超过最大限定金额",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_RETURN_CONSUME_ID_NO_EXIST:
                        Toast.makeText(context,
                                "不存在该类型的消费动作ID", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    default:
                        Toast.makeText(
                                context,
                                "未知错误,错误码为："
                                        + responseCode,
                                Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private static int getActionId(int spendPoint) {
        int actionId = 0;
        switch (spendPoint) {
            case 100:
                actionId = ACTION_ID_100;
                break;
            case 280:
                actionId = ACTION_ID_280;
                break;
            case 500:
                actionId = ACTION_ID_500;
                break;
        }
        return actionId;
    }


    /**
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
            wornSpendPointDialog(levelCount, spendPoint);
//            testWornDialog(context, levelCount, spendPoint);
        }
    }

    /**
     * 当前玩的关卡是否已经激活
     *
     * @return
     */
    public static boolean isPass(String levelValues, int level) {
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
    public static boolean isSpendPointNextLevel(String levelValues, int level) {
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
    public static void wornSpendPointDialog(final int levelCount, final int spenPoint) {
        Log.d(TAG, "Worning user spend point....");

        String wornMsg = "您将进入" + (levelCount + 1) + "关，需要消耗您" + spenPoint + "个M币,您是否继续？";
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("温馨提示")
                .setMessage(wornMsg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "wornSpendPointDialog, onClick");
                        // 获取用户的积分
                        DianJinPlatform.getBalance(context, new WebServiceListener<Float>() {
                            @Override
                            public void onResponse(int responseCode, Float balance) {
                                Log.d(TAG, "Get userPoints, responseCode = " + responseCode);
                                // 返回成功
                                if (responseCode == DianJinPlatform.DIANJIN_SUCCESS) {
                                    Log.d(TAG, "Get userPoints success ,userPoints = " + balance);
                                    isSpendUserPointDialog(balance, spenPoint, levelCount);
                                }
                                // 返回错误
                                else {
                                    Toast.makeText(context, "对不起，获取您的M币失败,请检查您的网络。", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        builder.show();
    }

    public static void isSpendUserPointDialog(Float balance, final int spenPoint, int levelCount) {
        if (spenPoint <= balance) {
            // 消耗积分
            spendUserPoints(spenPoint);
        } else {
            String wornMsg;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("温馨提示");
            wornMsg = "您将进入" + (levelCount + 1) + "关，需要消耗您" + spenPoint + "个M币,您当前拥有" + balance + "个M币，M币不足，您可以点击这里获取M币。";
            builder.setMessage(wornMsg);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 显示广告墙
                    showDianjinOfferWall(context);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            builder.show();
        }
    }


    /**
     * 获取进入下一关所需要消耗的积分，如果不需要则返回0
     *
     * @param levelCount
     * @return
     */
    public static int getUserSpendPoint(int levelCount) {
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
    public static int getLevelCount(String levelValues) {
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

}
