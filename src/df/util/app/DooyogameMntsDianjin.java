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
 * Time: ����10:43
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMntsDianjin {

    public static final String TAG = "df.util.app.DooyogameMntsDianjin";

    // ��¼�û��Ѿ�ͨ�����ַ���
    public static String KEY_NAME = "level_numbers";

    // ���Ѷ������Ϳ�100����
    public static final int ACTION_ID_100 = 1001;
    // ���Ѷ������Ϳ�100����
    public static final int ACTION_ID_280 = 1002;
    // ���Ѷ������Ϳ�100����
    public static final int ACTION_ID_500 = 1003;

    public static Context context = null;
    public static DialogInterface.OnClickListener gotoNextLevelAction;

    /**
     * ��ʼ�����SDK
     *
     * @param context
     */
    public static void initDianjinSdk(Context context) {
        Log.d(TAG, "Initialize Dianjin SDK...");
        DianJinPlatform.initialize(context, 1824, "bd1f1ed37d21980f22570e6140e7177f");
    }

    /**
     * ��ʾ����ƹ�ǽ
     *
     * @param context
     */
    public static void showDianjinOfferWall(final Context context) {
        DianJinPlatform.showOfferWall(context, DianJinPlatform.Oriention.PORTRAIT);
    }

    /**
     * �����û��Ļ���
     *
     * @param spendPoint
     */
    public static void spendUserPoints(int spendPoint) {
        // ������
        String orderSerial = TimeUtil.toLongOfyyyyMMddHHmmss() + TelephoneUtil.toImei(context);
        // ���ѵĻ���
        float amount = spendPoint;

        // ���Ѷ�������
        int actionId = getActionId(spendPoint);

        DianJinPlatform.consume(context, orderSerial, amount, actionId, new WebServiceListener<Integer>() {
            @Override
            public void onResponse(int responseCode, Integer t) {
                switch (responseCode) {
                    case DianJinPlatform.DIANJIN_SUCCESS:
                        Toast.makeText(context, "���ѳɹ�", Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, "is_had_spend", true);
                        if (gotoNextLevelAction != null) gotoNextLevelAction.onClick(null, 9);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_REQUES_CONSUNE:
                        Toast.makeText(context, "֧������ʧ��",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:
                        Toast.makeText(context, "M�Ҳ���",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ACCOUNT_NO_EXIST:
                        Toast.makeText(context, "�˺Ų�����",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ORDER_SERIAL_REPEAT:
                        Toast.makeText(context, "�������ظ�",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT:
                        Toast.makeText(context,
                                "һ���Խ��׽�������޶����",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case DianJinPlatform.DIANJIN_RETURN_CONSUME_ID_NO_EXIST:
                        Toast.makeText(context,
                                "�����ڸ����͵����Ѷ���ID", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    default:
                        Toast.makeText(
                                context,
                                "δ֪����,������Ϊ��"
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

        // ��ȡ�Դ洢�Ĺؿ���ŵ��ַ���
        String levelValues = PreferenceUtil.readRecord(context, KEY_NAME, "");
        Log.d(TAG, "The levels are activated, levelValues = " + levelValues);

        // �ж��û���ǰ�Ĺؿ����Ƿ��Ѿ�����
        boolean is_pass_level = isPass(levelValues, level);

        // �ؿ��Ѿ�����
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
        // �ؿ�δ������
        else {
            boolean canRun = getCanRun(context);
            if (canRun) {
                if (levelValues.equals("")) {
                    newLevelValues = String.valueOf(level);
                } else {
                    newLevelValues = levelValues + "," + level;
                }
                // �洢�ؿ����
                PreferenceUtil.saveRecord(context, KEY_NAME, newLevelValues);
            } else {
                newLevelValues = levelValues;
            }
        }

        Log.d(TAG, "The newLevelValues = " + newLevelValues);
        levelCount = getLevelCount(newLevelValues);
        Log.d(TAG, "The levelCount = " + levelCount);

        // ��ȡ������һ������Ҫ���ĵĻ���
        spendPoint = getUserSpendPoint((levelCount + 1));
        Log.d(TAG, "levelCount = " + levelCount + " ,need to spend " + spendPoint + " point...");

        // �����Ҫ���Ļ��֣���ʾ�û�
        if (spendPoint > 0) {
            // ת��������״̬
            PreferenceUtil.saveRecord(context, "is_need_spend", true);
            wornSpendPointDialog(levelCount, spendPoint);
//            testWornDialog(context, levelCount, spendPoint);
        }
    }

    /**
     * ��ǰ��Ĺؿ��Ƿ��Ѿ�����
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
     * �жϵ�ǰ����Ĺؿ�ȥ��һ���Ƿ���Ҫ����
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
     * ��ʾ�û�������һ�ؽ��������û��Ļ���
     *
     * @param levelCount
     */
    public static void wornSpendPointDialog(final int levelCount, final int spenPoint) {
        Log.d(TAG, "Worning user spend point....");

        String wornMsg = "��������" + (levelCount + 1) + "�أ���Ҫ������" + spenPoint + "��M��,���Ƿ������";
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("��ܰ��ʾ")
                .setMessage(wornMsg)
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "wornSpendPointDialog, onClick");
                        // ��ȡ�û��Ļ���
                        DianJinPlatform.getBalance(context, new WebServiceListener<Float>() {
                            @Override
                            public void onResponse(int responseCode, Float balance) {
                                Log.d(TAG, "Get userPoints, responseCode = " + responseCode);
                                // ���سɹ�
                                if (responseCode == DianJinPlatform.DIANJIN_SUCCESS) {
                                    Log.d(TAG, "Get userPoints success ,userPoints = " + balance);
                                    isSpendUserPointDialog(balance, spenPoint, levelCount);
                                }
                                // ���ش���
                                else {
                                    Toast.makeText(context, "�Բ��𣬻�ȡ����M��ʧ��,�����������硣", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        builder.show();
    }

    public static void isSpendUserPointDialog(Float balance, final int spenPoint, int levelCount) {
        if (spenPoint <= balance) {
            // ���Ļ���
            spendUserPoints(spenPoint);
        } else {
            String wornMsg;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("��ܰ��ʾ");
            wornMsg = "��������" + (levelCount + 1) + "�أ���Ҫ������" + spenPoint + "��M��,����ǰӵ��" + balance + "��M�ң�M�Ҳ��㣬�����Ե�������ȡM�ҡ�";
            builder.setMessage(wornMsg);
            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // ��ʾ���ǽ
                    showDianjinOfferWall(context);
                }
            });
            builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            builder.show();
        }
    }


    /**
     * ��ȡ������һ������Ҫ���ĵĻ��֣��������Ҫ�򷵻�0
     *
     * @param levelCount
     * @return
     */
    public static int getUserSpendPoint(int levelCount) {
        int spendPoint = 0;
        // �ﵽ��5�ؿ�ʼ��������5�أ�ÿ5������100����
        if (levelCount >= 5 && levelCount < 30) {
            if (levelCount % 5 == 0) {
                spendPoint = 100;
            }
        }
        // �ﵽ��30�ؿ�ʼ��������30�أ�ÿ10������280����
        else if (levelCount >= 30 && levelCount < 60) {
            if (levelCount % 10 == 0) {
                spendPoint = 280;
            }
        }
        // �ﵽ60�أ���Ҫ�۳�500����
        else if (levelCount == 60) {
            spendPoint = 500;
        }
        return spendPoint;
    }

    /**
     * ��ȡ�ؿ���
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
