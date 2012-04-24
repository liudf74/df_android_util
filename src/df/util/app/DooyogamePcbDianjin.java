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
 * Time: ����4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogamePcbDianjin {
    private static final String TAG = "df.util.app.DooyogamePcbDianjin";
    private final static String IS_HAD_GAVE_HINT_CHANCES = "is_had_gave_hint_chances";

    private final static String USER_HINT_TOTAL = "user_hint_total";
    private final static int FACTOR = 10;

    private static Context context;
    private static Handler updateTextViewHandler = new Handler();

    // ���Ѷ�������,������Ϸ
    public static final int ACTION_ID_1001 = 1001;

    /**
     * ������ʱ���ʼ�� dianjin sdk
     *
     * @param ct
     */
    public static void initDianjinSdk(Context ct) {
        Log.d(TAG, "initDianjinSdk...");
        context = ct;
        DianJinPlatform.initialize(context, 2163, "17acb59b834d3bd63b42426193fdf657");

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
     * �ӷ�������ȡ��ȡ�û��Ļ���
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
     * �����û��Ļ���
     *
     * @param spendPoint
     */
    public static void spendUserPoints(final float spendPoint) {
        // ������
        String orderSerial = TimeUtil.toLongOfyyyyMMddHHmmss() + TelephoneUtil.toImei(context);
        // ���Ѷ�������
        int actionId = ACTION_ID_1001;
        DianJinPlatform.consume(context, orderSerial, spendPoint, actionId, new WebServiceListener<Integer>() {
            @Override
            public void onResponse(int responseCode, Integer balance) {
                switch (responseCode) {
                    case DianJinPlatform.DIANJIN_SUCCESS:
                        addNewHintTotal(spendPoint);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_REQUES_CONSUNE:
                        Log.d(TAG, "֧������ʧ��");
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:
                        Log.d(TAG, "M�Ҳ���");
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ACCOUNT_NO_EXIST:
                        Log.d(TAG, "�˺Ų�����");
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ORDER_SERIAL_REPEAT:
                        Log.d(TAG, "�������ظ�");
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT:
                        Log.d(TAG, "һ���Խ��׽�������޶����");
                        break;
                    case DianJinPlatform.DIANJIN_RETURN_CONSUME_ID_NO_EXIST:
                        Log.d(TAG, "�����ڸ����͵����Ѷ���ID");
                        break;
                    default:
                        Log.d(TAG, "δ֪����,������Ϊ��" + responseCode);
                }
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
                            showDianjinOffers();
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
    private static void pointsExchangeToHintTotal(float userPoints) {
        Log.d(TAG, "pointsExchangeToHintTotal,userPoints = " + userPoints);
        // ÿ10�ֶһ�һ���Զ���ɵĻ���
        int hintTotal = (int) (userPoints / FACTOR);
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
