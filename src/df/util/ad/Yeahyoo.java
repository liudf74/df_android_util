package df.util.ad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import com.yeahyoo.*;
import df.util.android.ActivityUtil;
import df.util.android.DialogUtil;
import df.util.android.ManifestUtil;
import df.util.android.PreferenceUtil;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-9-23
 * Time: ����12:21
 * To change this template use File | Settings | File Templates.
 */
public class Yeahyoo {

    public static final String TAG = "df.util.Yeahyoo";

    public static final String DF_YEAHYOO_MINSCORE = "DF_YEAHYOO_MINSCORE";
    public static final String DF_YEAHYOO_LOGDEBUG = "DF_YEAHYOO_LOGDEBUG";
    public static final String DF_YEAHYOO_SHOWADMILLIS = "DF_YEAHYOO_SHOWADMILLIS";
    public static final String DF_YEAHYOO_HIDEADMILLIS = "DF_YEAHYOO_HIDEADMILLIS";

    public static final String KEY_GAME_ACTIVED = "game.actived";
    public static final String KEY_GAME_SHOW_BANNER_AD = "game.show_bannerad";

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILURE = 2;
    public static final int RESULT_NOSCORE = 3;

    public static boolean mBannerAdLastShow = false;
    public static long mBannerAdLastMillis = 0L;
    public static long mBannerAdShowMillis = 0L;
    public static long mBannerAdHideMillis = 0L;

    // Ӧ��������Ҫ����С����
    public static int getMinScoreNeededToRun(Context context) {
        int minScore = -1;
        try {
            String packageName = context.getPackageName();
            PackageManager packagemanager = context.getPackageManager();
            ApplicationInfo ai = packagemanager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null) {
                Bundle bundle = ai.metaData;
                if (bundle != null) {
                    minScore = bundle.getInt(DF_YEAHYOO_MINSCORE, minScore);
                    Log.i(TAG, "min score needed to run = " + minScore);
                }
            } else {
                Log.e(TAG, "metaData[ " + DF_YEAHYOO_MINSCORE + " ] is null, appInfo = " + ai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minScore;
    }

    // �Ƿ���ʾ��־������Ϣ
    public static boolean isLogDebug(Context context) {
        boolean isDebug = false;
        try {
            String packageName = context.getPackageName();
            PackageManager packagemanager = context.getPackageManager();
            ApplicationInfo ai = packagemanager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null) {
                Bundle bundle = ai.metaData;
                if (bundle != null) {
                    isDebug = bundle.getBoolean(DF_YEAHYOO_LOGDEBUG, isDebug);
                    Log.i(TAG, "logOnce debug = " + isDebug);
                }
            } else {
                Log.e(TAG, "metaData[ " + DF_YEAHYOO_LOGDEBUG + " ] is null, appInfo = " + ai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDebug;
    }

    // ��Ϸ�Ƿ��ܹ���ʼ���У�
    public static boolean canRun(final Context context) {

        final boolean isActived = PreferenceUtil.readRecord(context, KEY_GAME_ACTIVED, false);
        final int localScore = getScoreFromLocal();
        final int minScore = getMinScoreNeededToRun(context);
        if (minScore < 0) {
            Log.e(TAG, "min score is illegal, please check AndroidManifest.xml");
            return false;
        }
        if ((isActived)) {
            Log.d(TAG, "game actived, can run");
            return true;
        }

        Log.i(TAG, "need to active, prompt user to get score");
        final String hintMessage = String.format(
                "�װ������ѣ����ã���Ϸ��Ҫ��������ʹ" +
                        "�á�������%d���֣�ֻ�輤��һ�Σ���" +
                        "��\"��ȡ%d����\"��ť���뾫Ʒ�Ƽ�������Ӧ" +
                        "�ú�ʹ��1�μ��ɻ�ö�Ӧ�Ļ��ֽ���������" +
                        "��һ��Ǯ���������ص��������Ӧ����" +
                        "����������ȡ���ְɣ�\n" +
                        "��������ӵ�л��֣�%d���������������ʹ" +
                        "�ã������������ʱ���������ʾ����" +
                        "ȷ������\"�������\"���Ժ����Լ��ɣ���",
                minScore, minScore, localScore);
        final String title = "����ϵͳ";
        final String btnActive = "�������";
        final String btnScore = String.format("��ȡ%d����", minScore);

        final AlertDialog scoreDialog = new AlertDialog.Builder(context)
//                .setCancelable(true)
                .setTitle(title)
                .setMessage(hintMessage)
                .setPositiveButton(btnActive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
//                        // �ӷ�������ȡ���֣���ˢ�±��ػ���
//                        getScoreFromServer();

                        // �ӷ�������ȥ���֣���ֹ�û�����װ��������
                        spendScoreFromServer(context, minScore);

                        // ����Ϊ�ǵ�һ������
                        PreferenceUtil.saveRecord(context, KEY_GAME_ACTIVED, false);

                        // �رյ�ǰ�Ի���
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(btnScore, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        // ��ʾ����������б���ʾ�û�����
                        DownListAd.show(context, new UserActiveAdFeedback() {
                            @Override
                            public void doAfterActive(int result, int score) {
                                Log.d(TAG, "DownListAd.show(), doAfterActive()"
                                        + ", result = " + result
                                        + ", score = " + score);
                                // �ӷ�������ȡ���֣���ˢ�±��ػ���
                                getScoreFromServer();

                                // ��ʾ�û��ɹ�
                                final String scoreMessage = "��ϲ�����" + score + "����";
                                hintScoreMessage(context, scoreMessage);
                            }
                        });
                        // �رյ�ǰ�Ի���
                        dialogInterface.cancel();
                    }
                })
                .create();
        scoreDialog.show();
        return false;

    }

    private static void hintScoreMessage(Context context, String scoreMessage) {
        // todo: �ֻ��ϲ���ʾ�����ܱ�������������Ӧ�ø��ǵ���
        Toast.makeText(context, scoreMessage, Toast.LENGTH_LONG).show();

//        AlertDialog successDialog = new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setMessage(scoreMessage)
//                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(final DialogInterface dialogInterface, int i) {
//                    }
//                }).create();
//        successDialog.show();
    }

    public static int getScoreFromLocal() {
        final int score = App.getScore();
        Log.d(TAG, "local score = " + score);
        return score;
    }

    public static void getScoreFromServer() {
        App.getScore(new GetScoreFeedback() {
            @Override
            public void doAfterGetScore(int result, int score) {
                /**
                 * resultNo : 1 �ɹ��� 2 ʧ�ܣ�
                 * score : ��ǰ���û����µĻ���
                 */
                if (result == RESULT_SUCCESS) {
                    Log.d(TAG, "get score from server, server score = " + score);
                } else {
                    Log.d(TAG, "get score from server failure, resultCode = " + result);
                }
            }
        });
    }

    // ���ѻ���
    public static void spendScoreFromServer(final Context context, int score) {
        App.spendScore(new SpendScoreFeedback() {
            @Override
            public void doAfterSpendScore(int result, int score) {
                /**
                 * resultNo : 1 �ɹ��� 2 ʧ�ܣ� 3 ���ֲ������ѣ�
                 * score ����ǰ���û����µĻ���
                 */
                if (result == RESULT_SUCCESS) {
                    // ����ɹ�
                    PreferenceUtil.saveRecord(context, KEY_GAME_ACTIVED, true);
                    Log.i(TAG, "spend score from server success, game actived");
                } else {
                    Log.e(TAG, "spend score from server failure, resultCode = " + result + ", server score = " + score);
                }
            }
        }, score);
    }

    // ���ѻ���
    public static void spendScoreFromServer(final Context context, int score,
                                            final SpendScoreFeedback doAfterSpendScore) {
        App.spendScore(new SpendScoreFeedback() {
            @Override
            public void doAfterSpendScore(int result, int score) {
                /**
                 * resultNo : 1 �ɹ��� 2 ʧ�ܣ� 3 ���ֲ������ѣ�
                 * score ����ǰ���û����µĻ���
                 */
                Log.d(TAG, "App.spendScore(), doAfterSpendScore()" +
                        ", result = " + result + ", score = " + score);
                // ��ȡ���µĻ���
                getScoreFromLocal();

                doAfterSpendScore.doAfterSpendScore(result, score);
            }
        }, score);
    }

    // ��ʼ��
    public static void init(Context context) {
        App.init(context);
        LogUtils.enableLog(isLogDebug(context));

        // �ӷ�������ȡ���֣���ǰ��ȡ
        getScoreFromServer();
        getScoreFromLocal();
    }

    // ������
    public static void fini() {
        App.destroy();
    }

    // ��ʼ�������
    public static void initBannerAd(final Context context, final BannerAd bannerAd) {
        mBannerAdShowMillis = ManifestUtil.getMetadataAsInt(context, DF_YEAHYOO_SHOWADMILLIS, 5000);
        mBannerAdHideMillis = ManifestUtil.getMetadataAsInt(context, DF_YEAHYOO_HIDEADMILLIS, 5000);
    }

    // �����������
    public static void finiBannerAd(final Context context, final BannerAd bannerAd) {
        if (bannerAd != null) {
            Log.d(TAG, "yeahyoo bannerAd destroy");
            bannerAd.destroy();
        }
    }

    // ���ò���ʾ�����
    public static void saveRecordOfShowBannerAdFalse(Context context) {
        PreferenceUtil.saveRecord(context, KEY_GAME_SHOW_BANNER_AD, false);
    }

    // ����Ҫ��ʾ�����
    public static void saveRecordOfShowBannerAdTrue(final Context context) {
        PreferenceUtil.saveRecord(context, KEY_GAME_SHOW_BANNER_AD, true);
    }

    // �Ƿ��ܹ���ʾ�����
    public static boolean readRecordOfShowBannerAd(final Context context) {
        return PreferenceUtil.readRecord(context, KEY_GAME_SHOW_BANNER_AD, true);
    }

    // ���ع����
    public static void hideBannerAd(final Context context, final BannerAd bannerAd) {
        if (bannerAd != null) {
            bannerAd.hide();
            Log.d(TAG, "yeahyoo bannerAd hide");
        }
    }

    // ��ʾ�����
    public static void showBannerAd(final Context context, final BannerAd bannerAd) {
        if (bannerAd != null) {
            boolean canShow = readRecordOfShowBannerAd(context);
            if (canShow) {
                Log.d(TAG, "yeahyoo bannerAd show");
                bannerAd.show(new UserActiveAdFeedback() {
                    @Override
                    public void doAfterActive(int resultCode, int score) {
                        Log.d(TAG, "yeahyoo bannerAd.show(), doAfterActive()" +
                                ", result = " + resultCode + ", score = " + score);
                        // �û���������󣬲�����ʾ
                        saveRecordOfShowBannerAdFalse(context);
                        hideBannerAd(context, bannerAd);
                    }
                });
            }
        }
    }

    // ��ʾ�������ʼ����ʾ��
    public static void showBannerAdAlways(final Context context, final BannerAd bannerAd) {
        if (bannerAd != null) {
            boolean canShow = readRecordOfShowBannerAd(context);
            if (canShow) {
                Log.d(TAG, "yeahyoo bannerAd show");
                bannerAd.show(new UserActiveAdFeedback() {
                    @Override
                    public void doAfterActive(int resultCode, int score) {
                        Log.d(TAG, "yeahyoo bannerAd.show(), doAfterActive()" +
                                ", result = " + resultCode + ", score = " + score);
//                        // �û���������󣬲�����ʾ
//                        saveRecordOfShowBannerAdFalse(context);
//                        hideBannerAd(context, bannerAd);
                    }
                });
            }
        }
    }

    // �л����������ʾ������
    public static void toggleBannerAd(final Context context, final BannerAd bannerAd) {

        boolean canShow = readRecordOfShowBannerAd(context);
        if (!canShow) {
            return;
        }

        long upTimeMillis = SystemClock.uptimeMillis();
        if (mBannerAdLastMillis == 0L) {
            mBannerAdLastMillis = upTimeMillis;
            initBannerAd(context, bannerAd);
            // ��һ�ν�������ʾ���
            showBannerAd(context, bannerAd);
        }

        if (mBannerAdLastShow) {
            if (upTimeMillis - mBannerAdLastMillis > mBannerAdShowMillis) {
                Log.d(TAG, "show time reached, hide");
                hideBannerAd(context, bannerAd);
                mBannerAdLastShow = false;
                mBannerAdLastMillis = upTimeMillis;
            }
        } else {
            if (upTimeMillis - mBannerAdLastMillis > mBannerAdHideMillis) {
                Log.d(TAG, "hide time reached, show");
                showBannerAd(context, bannerAd);
                mBannerAdLastShow = true;
                mBannerAdLastMillis = upTimeMillis;
            }
        }
    }

    // ��ʾ����б�
    public static void showDownList(final Context context) {
        DownListAd.show(context, new UserActiveAdFeedback() {
            @Override
            public void doAfterActive(int resultCode, int score) {
                Log.d(TAG, "DownListAd.show(), doAfterActive()" +
                        ", result = " + resultCode + ", score = " + score);
                // ��ȡ���µĻ���
                getScoreFromLocal();
            }
        });

    }

    // ��ʾ����б�
    public static void showDownList(final Context context,
                                    final UserActiveAdFeedback doAfterActive) {
        DownListAd.show(context, new UserActiveAdFeedback() {
            @Override
            public void doAfterActive(int resultCode, int score) {
                Log.d(TAG, "DownListAd.show(), doAfterActive()" +
                        ", result = " + resultCode + ", score = " + score);
                // ��ȡ���µĻ���
                getScoreFromLocal();

                // �ٴε����û��Ļص�����
                doAfterActive.doAfterActive(resultCode, score);
            }
        });

    }


    // ��ʾ�Ի����˳���Ϸ��װ���ر���
    public static void showDialogOfQuitAndMust(final Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage("�˳���Ϸ")
                .setPositiveButton("�˳���Ϸ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityUtil.quitActivity(context);
                    }
                })
                .setNegativeButton("װ���ر�", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showDownList(context);
                    }
                })
                .create();
        dialog.show();
    }
}
