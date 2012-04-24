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
import android.widget.TextView;
import com.uucun.adsdk.UUAppConnect;
import com.uucun.adsdk.UpdatePointListener;
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
 * Time: ����4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogamePcbAppjoy {
    private static final String TAG = "df.util.app.DooyogamePcbAppjoy";
    private final static String IS_HAD_GAVE_HINT_CHANCES = "is_had_gave_hint_chances";

    private final static String USER_HINT_TOTAL = "user_hint_total";
    private final static int FACTOR = 5;

    private static Context context;
    private static Handler updateTextViewHandler = new Handler();

    // ���Ѷ�������,������Ϸ
    public static final int ACTION_ID_1001 = 1001;

    /**
     * ������ʱ���ʼ�� appjoy sdk
     *
     * @param ct
     */
    public static void initAppjoySdk(Context ct) {
        Log.d(TAG, "initAppjoySdk...");
        context = ct;
        UUAppConnect.getInstance(context).initSdk();

        // ��ʼ���û�����
        initUserPoint();

        // ��Ϸ��һ��ʹ�õ�ʱ�������û�����
        boolean isHadGetChance = PreferenceUtil.readRecord(context, IS_HAD_GAVE_HINT_CHANCES, false);
        if (!isHadGetChance) {
            // ��ʼ���û��Զ������Ϸ�Ļ��ᣬ�¿�ʼ��Ϸ����5�λ���
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
     * �ӷ�������ȡ�û��Ļ���
     */
    private static void getUserPoints() {
        Log.d(TAG, "getUserPoints,userPointTotal...");
        UUAppConnect.getInstance(context).getPoints(
                new UpdatePointListener() {
                    @Override
                    public void onError(String s) {
                        // nothing to do ...
                    }

                    @Override
                    public void onSuccess(String pointUnit, int userPointTotal) {
                        Log.d(TAG, "getUserPoints,userPointTotal = " + userPointTotal);
                        pointsExchangeToHintTotal(userPointTotal);
                    }
                }
        );
    }

    /**
     * ʹ�û���
     *
     * @param spendPoints
     */
    private static void spendUserPoints(final int spendPoints) {
        Log.d(TAG, "spendUserPoints...");
        // ֱ�����Ľ��
        UUAppConnect.getInstance(context).spendPoints(spendPoints,
                new UpdatePointListener() {
                    public void onSuccess(String unit, int total) {
                        Log.d(TAG, "spendUserPoints, userPointsTotal = " + total);
                        addNewHintTotal(spendPoints);
                    }

                    public void onError(String msg) {
                    }
                });
    }


    /**
     * ������Ϸ��ʾ����
     *
     * @param spendPoint
     */
    private static void addNewHintTotal(float spendPoint) {
        // �����Ļ��ֳɹ���ʱ��Ż���û������Ӧ����Ϸ��ʾ����
        int hintTotal = (int) (spendPoint / FACTOR);
        int userHintTotal = getUserhintTotal();
        userHintTotal = userHintTotal + hintTotal;
        saveUserHintTotal(userHintTotal);
        // ��TextView����ʾ�û����µ���ʾ����
//        showHintTotal();
    }


    /**
     * �����û�ӵ�е��Զ������Ϸ�Ļ���һ��
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
                    .setTitle("��ܰ��ʾ")
                    .setMessage("������Ϸ��ʾ�����Ѿ�Ϊ0,�����Ե��ȷ����ȡ��ң�ÿ10����ҿ��Զһ�1����ʾ�Ļ��ᡣ")
                    .setPositiveButton("ȷ��", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //To change body of implemented methods use File | Settings | File Templates.
                            showAppjoyOffers();
                        }
                    })
                    .setNegativeButton("ȡ��", new OnClickListener() {
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
     * ��ʼ���û��Զ������Ϸ�Ĵ���
     * ��ʾ��ָ����TextView��
     *
     * @param ct
     */
    public static void initHintCount(Context ct) {
        Log.d(TAG, "initHintCount...");
        context = ct;
        showHintTotal();
    }


    /**
     * ��ʾ�û�ʣ����Զ���ɵĻ���
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
     * 10����ֿ��Զһ�һ����Ϸ�Զ���ɵĻ���
     *
     * @return
     */
    private static void pointsExchangeToHintTotal(int userPoints) {
        Log.d(TAG, "pointsExchangeToHintTotal,userPoints = " + userPoints);
        // ÿ10�ֶһ�һ���Զ���ɵĻ���
        int hintTotal = userPoints / FACTOR;
        if (hintTotal > 0) {
            // ���һ�����ʾ�����Ļ������ĵ�
            int spendPoints = hintTotal * FACTOR;
            spendUserPoints(spendPoints);
        }

    }

    /**
     * �ж��Ƿ�����Ϸ��ʾ�Ĵ���
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
     * ��ʾ���ǽ
     */
    public static void showAppjoyOffers() {
        UUAppConnect.getInstance(context).showOffers();
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
