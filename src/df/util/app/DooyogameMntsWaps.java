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
 * Time: ����10:43
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMntsWaps implements UpdatePointsNotifier {

    public static final String TAG = "df.util.app.DooyogameMntsDianjin";

    // ��¼�û��Ѿ�ͨ�����ַ���
    public static String KEY_NAME = "level_numbers";
    public static Context context = null;
    public static DialogInterface.OnClickListener gotoNextLevelAction;
    // �û��Ļ������Ӧ���ַ���KEY
    private static final String USER_POINT_KEY = "user_points";
    // �����������
    private static String USER_POINT_UNIT = "point_unit";
    private static final DooyogameMntsWaps dooyogameMntsWaps = new DooyogameMntsWaps();
    private static LinearLayout adLinearLayout;
    private static ImageButton imageButton;
    final static Handler mHandler = new Handler();
    // ��������״̬
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
     * ��ʼ��WAPS����ͳ�ƽӿ�
     */
    private void initWapsAppConnect() {
        Log.d(TAG, "Initialize waps AppConnect...");
        AppConnect.getInstance(context);
        getUserPointsFromWaps();
    }

    private void getUserPointsFromWaps() {
        // ��ʼ���û�����
        AppConnect.getInstance(context).getPoints(this);
    }

    /**
     * ��ʾ waps �Ļ������
     *
     * @param activity
     */
    public static void showWapsAdView(Activity activity) {
        context = activity;
        // ��ʼ������ͳ�ƽӿ�
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

            // ������濪��
            isShowAdCloseImageButton();

            // ��ImageButton��ӵ���¼�����
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isCloseAdLinearLayoutDialog();
                }
            });
        }
    }

    /**
     * �����û��Ļ���
     *
     * @param amount
     */
    public void spendUserPoints(int amount) {
        AppConnect.getInstance(context).spendPoints(amount, DooyogameMntsWaps.this);
    }

    /**
     * ��ʾwaps���ǽ
     */
    public static void showWpasOfferWall() {
        AppConnect.getInstance(context).showOffers(context);
    }

    /**
     * ������һ�ز���
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
            warnSpendPointDialog(levelCount, spendPoint);
        }
    }

    /**
     * ��ǰ��Ĺؿ��Ƿ��Ѿ�����
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
     * �жϵ�ǰ����Ĺؿ�ȥ��һ���Ƿ���Ҫ����
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
     * ��ʾ�û�������һ�ؽ��������û��Ļ���
     *
     * @param levelCount
     */
    private static void warnSpendPointDialog(final int levelCount, final int spenPoint) {
        Log.d(TAG, "warning user spend point....");

        final int userPoints = getUserPoints();
        final String pointUnit = PreferenceUtil.readRecord(context, USER_POINT_UNIT, "");
        if (userPoints >= spenPoint) {
            String warnMsg = "��������" + (levelCount + 1) + "�أ���Ҫ������" + spenPoint + "��" + pointUnit + ",����ǰӵ��" + userPoints + "��" + pointUnit + ",���Ƿ������";
            new AlertDialog.Builder(context)
                    .setTitle("��ܰ��ʾ")
                    .setMessage(warnMsg)
                    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dooyogameMntsWaps.spendUserPoints(spenPoint);
                            saveUserPoints((userPoints - spenPoint));
                            // ��ʶΪ������״̬
                            PreferenceUtil.saveRecord(context, "is_had_spend", true);
                            if (gotoNextLevelAction != null) {
                                gotoNextLevelAction.onClick(null, 9);
                            }
                        }
                    })
                    .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }
                    })
                    .create()
                    .show();
        } else {
            String warnMsg = "��������" + (levelCount + 1) + "�أ���Ҫ������" + spenPoint + "��" + pointUnit + ",����" + pointUnit + "���㣬�����Ե��ȷ����ȡ��";
            new AlertDialog.Builder(context)
                    .setTitle("��ܰ��ʾ")
                    .setMessage(warnMsg)
                    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showWpasOfferWall();
                        }
                    })
                    .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
     * ��ȡ������һ������Ҫ���ĵĻ��֣��������Ҫ�򷵻�0
     *
     * @param levelCount
     * @return
     */
    private static int getUserSpendPoint(int levelCount) {
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
    private static int getLevelCount(String levelValues) {
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


    /**
     * AppConnect.getPoints()������ʵ�֣�����ʵ��
     *
     * @param currencyName �����������.
     * @param pointTotal   ����������.
     */
    public void getUpdatePoints(String currencyName, int pointTotal) {
        Log.d(TAG, "currencyName = " + currencyName + ",pointTotal = " + pointTotal);
        // ���ر����û��Ļ���
        saveUserPoints(pointTotal);
        PreferenceUtil.saveRecord(context, USER_POINT_UNIT, currencyName);
    }

    /**
     * ��ȡʧ�ܣ���ʾ�û�
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
     * ���ر����û��Ļ���
     *
     * @param points
     */
    private static void saveUserPoints(int points) {
        PreferenceUtil.saveRecord(context, USER_POINT_KEY, points);
    }

    /**
     * �ӱ��ػ�ȡ�û��Ļ���
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
     * ��̬��濪��
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
     * ��ʾ�û��Ƿ�رջ������
     */
    private static void isCloseAdLinearLayoutDialog() {
        // ��ȡ�õĻ���
        final int userPoints = getUserPoints();
        Log.d(TAG, "isCloseAdLinearLayoutDialogv, userPoints = " + userPoints);
        final int closeAdSpend = 80;
        String pointUnit = PreferenceUtil.readRecord(context, USER_POINT_UNIT, "���");
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("��ܰ��ʾ");
        if (userPoints >= closeAdSpend) {
            alertMsg = "�رչ����Ҫ������" + closeAdSpend + "��" + pointUnit + ",����ǰӵ��" + userPoints + "��" + pointUnit + ",���Ƿ�Ҫ������";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
            alertMsg = "�رչ����Ҫ������" + closeAdSpend + "��" + pointUnit + ",����ǰӵ��" + userPoints + "��" + pointUnit + "," + pointUnit + "����,�����Ե��ȷ����ѻ�ȡ" + pointUnit;
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showWpasOfferWall();
                }
            });
        }
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        builder.show();
    }
}
