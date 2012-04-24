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
 * Time: ����7:16
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMntsAppjoy {
    public static final String TAG = "df.util.app.DooyogameMntsAppjoy";
    public static LinearLayout adLinearLayout;
    // �û��Ļ���
    public static int USER_POINT = 0;
    // ���ֵ�λ
    public static String POINT_UNIT = "";
    // ���Ѵ�����ʾ
    public static String SPEND_ERROR_MSG = "";
    // ��ȡ���ִ�����ʾ
    public static String GET_POINT_ERROR_MSG = "";
    // �ж��ܷ������һ��
    public static boolean canRun = false;

    public static String KEY_NAME = "level_numbers";

    /**
     * ��ʼ��appjoy�ӿ�
     *
     * @param context
     */
    public static void initAppjoySdk(Context context) {
        Log.d(TAG, "init appjoy sdk...");
        UUAppConnect.getInstance(context).initSdk();
    }

    /**
     * �Ͽ�appjoy�ӿ�
     *
     * @param context
     */
    public static void exitAppjoySdk(Context context) {
        UUAppConnect.getInstance(context).exitSdk();
    }

    /**
     * ��ȡ�û��Ļ���
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
     * �����û��Ļ���
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
     * ��ʾ����ǽ����ʾ�û�ȥ���������ȡ����
     *
     * @param context
     */
    public static void showOfferWall(final Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("��ܰ��ʾ");
        builder.setMessage(msg + ",�����Ե�������ȡ���֣�");
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
                UUAppConnect.getInstance(context).showOffers();
            }
        });

        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
     * ��ʾ����
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
     * bug �Ѽ��ӿ�
     *
     * @param context
     */
    public static void catchBugs(Context context) {
        UUAppConnect.getInstance(context).startupCatchBugs();
    }

    /**
     * �ж��û��Ƿ��ܹ�������һ�ز���
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

        // ��ȡ�ؿ��ַ���
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

        // ��ȡ�û��Ѿ�ͬͨ���Ĺؿ�
        levelCount = getUserLevelCount(newLevelValues);
        Log.d(TAG, "The levelCount = " + levelCount);

        // �ﵽ��5�ؿ�ʼ��������5�أ�ÿ5������100����
        if (levelCount >= 5 && levelCount < 30) {
            if (levelCount % 5 == 0) {
                PreferenceUtil.saveRecord(context, "is_need_spend", true);
                // ���Ļ���
                spendPoint = 100;
                // ��ʾ�û�������Ҫ���Ļ���
                wornSpendPointDialog(context, levelCount, spendPoint);
            }
        }
        // �ﵽ��30�ؿ�ʼ��������30�أ�ÿ10������280����
        else if (levelCount >= 30 && levelCount < 60) {
            if (levelCount % 10 == 0) {
                PreferenceUtil.saveRecord(context, "is_need_spend", true);
                spendPoint = 280;
                // ��ʾ�û�������Ҫ���Ļ���
                wornSpendPointDialog(context, levelCount, spendPoint);
            }
        }
        // �ﵽ60�أ���Ҫ�۳�500����
        else if (levelCount == 60) {
            PreferenceUtil.saveRecord(context, "is_need_spend", true);
            spendPoint = 500;
            // ��ʾ�û�������Ҫ���Ļ���
            wornSpendPointDialog(context, levelCount, spendPoint);
        }

    }

    public static void canNotStarGame(Context context) {
        int levelCount = 0;
        int spendPoint = 0;

        // ��ȡ�ؿ��ַ���
        String oldLevelValues = getLevelValuesByKey(context);
        // ��ȡ�û��Ѿ�ͬͨ���Ĺؿ�
        levelCount = getUserLevelCount(oldLevelValues);
        Log.d(TAG, "The levelCount = " + levelCount);

        // �ﵽ��5�ؿ�ʼ��������5�أ�ÿ5������100����
        if (levelCount >= 5 && levelCount < 30) {
            if (levelCount % 5 == 0) {
                PreferenceUtil.saveRecord(context, "is_need_spend", true);
                // ���Ļ���
                spendPoint = 100;
            }
        }
        // �ﵽ��30�ؿ�ʼ��������30�أ�ÿ10������280����
        else if (levelCount >= 30 && levelCount < 60) {
            PreferenceUtil.saveRecord(context, "is_need_spend", true);
            spendPoint = 280;
        }
        // �ﵽ60�أ���Ҫ�۳�500����
        else if (levelCount == 60) {
            PreferenceUtil.saveRecord(context, "is_need_spend", true);
            spendPoint = 500;
        }
        // ��ʾ�û�������Ҫ���Ļ���
        wornSpendPointDialog(context, levelCount, spendPoint);
    }


    /**
     * ��ȡ�ؿ���
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
     * ��ʾ�û�������һ�ؽ��������û��Ļ���
     *
     * @param context
     * @param levelCount
     */
    public static void wornSpendPointDialog(final Context context, int levelCount, final int spenPoint) {
        Log.d(TAG, "Worning user spend point....");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("��ܰ��ʾ");
        // ��ȡ�û��Ľ��
        getUserPoints(context);
        if (GET_POINT_ERROR_MSG.equals("") && USER_POINT >= spenPoint) {
            builder.setMessage("��ϲ���������" + levelCount + "�أ���Ҫ������" + spenPoint + "����ң�����ǰӵ��" + USER_POINT + "����ң����Ƿ�Ҫ����?");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // �����û��Ľ��
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
            String msg = "���������" + levelCount + "�أ���Ҫ������" + spenPoint + "����ң�����ǰӵ��" + USER_POINT + "����ң����Ľ�Ҳ���";
            showOfferWall(context, msg);
        }
    }

    /**
     * ��ȡ�ؿ�����ַ���
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
     * �ж��û��ܷ������һ�صĲ���
     * ˵����1. �ж��û�������һ���Ƿ���Ҫ����
     * 2. �ж��û��Ƿ��Ѿ�����
     *
     * @param context
     * @return
     */
    public static boolean getCanRun(Context context) {
        boolean flag = false;
        // �Ƿ��Ѿ�����
        boolean is_had_spend = PreferenceUtil.readRecord(context, "is_had_spend", false);
        // ֻ�����û�����֮��ſ���ȡ����Ҫ����
        if (is_had_spend) {
            PreferenceUtil.saveRecord(context, "is_need_spend", false);
            PreferenceUtil.saveRecord(context, "is_had_spend", false);
        }

        // �Ƿ�Ҫ����
        boolean is_need_spend = PreferenceUtil.readRecord(context, "is_need_spend", false);
        Log.d(TAG, "is_need_spend = " + is_need_spend + ", is_had_spend = " + is_had_spend);

        if (!is_need_spend) {
            flag = true;
        }

        Log.d(TAG, "if can run ,flag = " + flag);
        return flag;
    }

}
