package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: ����4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameWmmtWaps {
    private static final String TAG = "df.util.app.DooyogameWmmtWaps";
    private final static String USER_POINTS_KEY = "user_points";
    private final static String USER_POINT_UNIT_KEY = "point_unit";
    private final static String IS_HAD_SPEND = "is_had_spend";
    private final static String IS_NEED_SPEND = "is_need_spend";
    private final static String IS_HAD_REQUEST = "is_had_request";
    private final static String IS_HAD_SPEND_CLOSE_AD = "is_had_spend_close_ad";

    private static LinearLayout adWapsArea;
    private static ImageButton imageButton;

    private static GetUserPoints getUserPoints = new GetUserPoints();
    private static SpendUserPoints spendUserPoints = new SpendUserPoints();
    private static Context context;
    private static Handler updatePointsHandler = new Handler();
    private static Handler closeAdHandler;
    private final static Timer timer = new Timer();

    final static Handler buttomHandler = new Handler() {
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
     * ������ʱ���ʼ�� waps sdk
     *
     * @param ct
     */
    public static void initWapsSdk(Context ct) {
        context = ct;
        AppConnect.getInstance(context);

        // ��ʼ���û�����
        getUserPoints.getUserPoints();
    }

    /**
     * ��ʾ�û�������Ϸ
     *
     * @param ct
     */
    public static void warnUserSpendPointsDialog(final Context ct) {
        context = ct;

        // �жϼ�����Ϸ�������Ƿ��Ѿ��ύ
        boolean is_had_request = PreferenceUtil.readRecord(context, IS_HAD_REQUEST, false);
        if (is_had_request) {
            Toast.makeText(context, "������Ϸ�������Ѿ��ύ�����Ժ����ԡ�", Toast.LENGTH_SHORT).show();
            return;
        }

        // ������ѱ�ʶ
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final int spendPoints = 100;
        int userPonts = getUserPointsFromLocal();
        String pointUnit = getUserPointUnit();
        Log.d(TAG, "warnUserSpendPointsDialog, userPoints = " + userPonts);
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("��ܰ��ʾ");
        if (userPonts >= spendPoints) {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��" + pointUnit + "������ǰӵ��" +
                    userPonts + "��" + pointUnit + "�����Ƿ�Ҫ������";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    spendUserPoints.spendUserPoints(spendPoints);
                }
            });

        } else {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��" + pointUnit + "������ǰӵ��" +
                    userPonts + "��" + pointUnit + "��" + pointUnit + "���㣬������ͨ�����ȷ����ȡ" + pointUnit + "��";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // ��ʾ���ǽ
                    showWapsOffers();
                }
            });
        }
        builder.setNegativeButton("ȡ��", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    /**
     * ��� waps �Ļ������
     *
     * @param activity
     */
    public static void addWapsAdView(final Activity activity) {
        context = (Context) activity;

        // �ж��û��Ƿ��Ѿ����ѹرջ������
        boolean is_had_spend_close_ad = PreferenceUtil.readRecord(context, IS_HAD_SPEND_CLOSE_AD, false);
        if (is_had_spend_close_ad) {
            return;
        }

        // �����������
        RelativeLayout adArea = new RelativeLayout(context);
        RelativeLayout.LayoutParams adAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        adAreaParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // ���� waps �����������
        adWapsArea = new LinearLayout(context);
        RelativeLayout.LayoutParams adWapsAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        adWapsArea.setGravity(LinearLayout.VERTICAL);
        adArea.addView(adWapsArea, adWapsAreaParams);

        // �������رհ�ť
        int rmAdid = ResourceUtil.getDrawableResourceIdFromName(context, "rm_ad");
        imageButton = new ImageButton(context);
        RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imageButton.setBackgroundResource(rmAdid);
        imageButton.setVisibility(View.GONE);
        imageButton.setLayoutParams(imageButtonParams);

        adArea.addView(imageButton);

        activity.addContentView(adArea, adAreaParams);
        new AdView(activity, adWapsArea).DisplayAd();

        // �Ƿ���ʾ���رհ�ť
        isShowAdCloseButtom();
        // ָ���û�ȥ���ѹرջ��������
        warnUserSpendForCloseAd();
    }

    /**
     * ��ʾ�û��رջ��������Ҫ����һ���Ļ���
     */
    private static void warnUserSpendForCloseAd() {

        // ��ӹرհ�ť�ĵ���¼��ļ���
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userPoints = getUserPointsFromLocal();
                final int spendPoints = 80;

                // ��ʾ�û��رջ������
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("��ܰ��ʾ");
                if (userPoints > spendPoints) {
                    builder.setMessage("�رչ������Ҫ������" + spendPoints + "��ң�" +
                            "����ǰӵ��" + userPoints + "����ң����Ƿ������");
                    builder.setPositiveButton("ȷ��", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // �����û��Ļ���
                            spendUserPoints.spendUserPoints(spendPoints);
                            closeAdHandler = new Handler();
                            Toast.makeText(context, "�����Ѿ��ύ!���۷ѳɹ�֮�����������Զ���ʧ��", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    builder.setMessage("�رչ������Ҫ������" + spendPoints + "��ң�" +
                            "����ǰӵ��" + userPoints + "����ң���Ҳ��㣬����Ե��ȷ����ѻ�ȡ���");
                    builder.setPositiveButton("ȷ��", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // ��ʾ���ǽ
                            showWapsOffers();
                        }
                    });
                }
                builder.setNegativeButton("ȡ��", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }

    private static void isShowAdCloseButtom() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int adAreaHeight = adWapsArea.getHeight();
                Log.d(TAG, "adAreaHeight = " + adAreaHeight);
                if (adAreaHeight > 40 && imageButton.getVisibility() == View.GONE) {
                    Message message = new Message();
                    message.what = 1;
                    buttomHandler.sendMessage(message);
                }

                if (adAreaHeight < 40 && imageButton.getVisibility() == View.VISIBLE) {
                    Message message = new Message();
                    message.what = 0;
                    buttomHandler.sendMessage(message);
                }

            }
        };

        timer.schedule(timerTask, 1500, 2000);
    }

    /**
     * �ж��û��Ƿ��Ѿ�������Ϸ
     *
     * @param context
     * @return
     */
    public static boolean isHadSpend(Context context) {
        boolean is_had_spend = PreferenceUtil.readRecord(context, IS_HAD_SPEND, false);
        if (is_had_spend) {
            PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
        }
        return is_had_spend;
    }

    /**
     * �ж���Ϸ�Ƿ���Ҫ����
     *
     * @param context
     * @return
     */
    public static boolean isNeedSpend(Context context) {
        return PreferenceUtil.readRecord(context, IS_NEED_SPEND, false);
    }

    /**
     * ��ʾ���ǽ
     */
    private static void showWapsOffers() {
        AppConnect.getInstance(context).showOffers(context);
    }

    /**
     * ���ػ�ȡ�û�����
     *
     * @return
     */
    private static int getUserPointsFromLocal() {
        getUserPoints.getUserPoints();
        return PreferenceUtil.readRecord(context, USER_POINTS_KEY, 0);

    }

    /**
     * ��ȡ������ҵĵ�λ
     *
     * @return
     */
    private static String getUserPointUnit() {
        return PreferenceUtil.readRecord(context, USER_POINT_UNIT_KEY, "���");
    }

    private static void saveUserPoints(int points) {
        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, points);
    }

    private static void saveUserPointUnit(String pointUnit) {
        PreferenceUtil.saveRecord(context, USER_POINT_UNIT_KEY, pointUnit);
    }


    /**
     * �����ȡ�û����ֵ�״̬���ڲ���
     */
    private static class GetUserPoints implements UpdatePointsNotifier {

        public void getUserPoints() {
            AppConnect.getInstance(context).getPoints(GetUserPoints.this);
        }


        @Override
        public void getUpdatePoints(String pointUnit, int pointTotal) {
            Log.d(TAG, "GetUserPoints, getUpdatePoints, pointTotal = " + pointTotal);
            saveUserPoints(pointTotal);
            saveUserPointUnit(pointUnit);
        }

        @Override
        public void getUpdatePointsFailed(String s) {
            // nothing to do...
            Log.d(TAG, "GetUserPoints, getUpdatePointsFailed, errorMsg = " + s);
        }
    }

    /**
     * ��������û����ֵ�״̬���ڲ���
     */
    private static class SpendUserPoints implements UpdatePointsNotifier {

        public void spendUserPoints(int spendPoints) {
            AppConnect.getInstance(context).spendPoints(spendPoints, SpendUserPoints.this);
            PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, true);

        }

        @Override
        public void getUpdatePoints(String pointUnit, int pointTotal) {
            Log.d(TAG, "SpendUserPoints, getUpdatePoints, pointTotal = " + pointTotal);
            saveUserPoints(pointTotal);
            saveUserPointUnit(pointUnit);
            PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
            PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
            PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);

            // �ж��Ƿ񴥷��˹رջ������Ľӿ�
            if (closeAdHandler != null) {
                PreferenceUtil.saveRecord(context, IS_HAD_SPEND_CLOSE_AD, true);

                closeAdHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setVisibility(View.GONE);
                        adWapsArea.setVisibility(View.GONE);
                        timer.cancel();
                        closeAdHandler = null;
                    }
                });
            }
            // ���м�����Ϸ�ɹ������ʾ��Ϣ
            else {
                updatePointsHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "���ѳɹ������������½�����Ϸ��", Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

        @Override
        public void getUpdatePointsFailed(String s) {
            final String errorMsg = s;
            Log.d(TAG, "SpendUserPoints, getUpdatePointsFailed, errorMsg = " + errorMsg);
            updatePointsHandler.post(new Runnable() {
                @Override
                public void run() {
                    //To change body of implemented methods use File | Settings | File Templates.
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
