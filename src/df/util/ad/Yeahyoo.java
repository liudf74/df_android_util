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
 * Time: 上午12:21
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

    // 应用运行需要的最小积分
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

    // 是否显示日志调试信息
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

    // 游戏是否能够开始运行？
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
                "亲爱的朋友，您好，游戏需要激活后才能使" +
                        "用。激活需%d积分，只需激活一次，请" +
                        "按\"获取%d积分\"按钮进入精品推荐，下载应" +
                        "用后使用1次即可获得对应的积分奖励，不用" +
                        "您一分钱，还能下载到最新最潮的应用软" +
                        "件，快来获取积分吧！\n" +
                        "（您现在拥有积分：%d，激活后可永久免费使" +
                        "用，如果因网络延时引起积分显示不正" +
                        "确，请点击\"激活软件\"后，稍候再试即可！）",
                minScore, minScore, localScore);
        final String title = "积分系统";
        final String btnActive = "激活软件";
        final String btnScore = String.format("获取%d积分", minScore);

        final AlertDialog scoreDialog = new AlertDialog.Builder(context)
//                .setCancelable(true)
                .setTitle(title)
                .setMessage(hintMessage)
                .setPositiveButton(btnActive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
//                        // 从服务器获取积分，并刷新本地积分
//                        getScoreFromServer();

                        // 从服务器减去积分，防止用户不安装广告就下载
                        spendScoreFromServer(context, minScore);

                        // 设置为非第一次运行
                        PreferenceUtil.saveRecord(context, KEY_GAME_ACTIVED, false);

                        // 关闭当前对话框
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(btnScore, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        // 显示可下载软件列表，提示用户下载
                        DownListAd.show(context, new UserActiveAdFeedback() {
                            @Override
                            public void doAfterActive(int result, int score) {
                                Log.d(TAG, "DownListAd.show(), doAfterActive()"
                                        + ", result = " + result
                                        + ", score = " + score);
                                // 从服务器获取积分，并刷新本地积分
                                getScoreFromServer();

                                // 提示用户成功
                                final String scoreMessage = "恭喜您获得" + score + "积分";
                                hintScoreMessage(context, scoreMessage);
                            }
                        });
                        // 关闭当前对话框
                        dialogInterface.cancel();
                    }
                })
                .create();
        scoreDialog.show();
        return false;

    }

    private static void hintScoreMessage(Context context, String scoreMessage) {
        // todo: 手机上不显示，可能被紧接着启动的应用覆盖掉了
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
                 * resultNo : 1 成功； 2 失败；
                 * score : 当前该用户最新的积分
                 */
                if (result == RESULT_SUCCESS) {
                    Log.d(TAG, "get score from server, server score = " + score);
                } else {
                    Log.d(TAG, "get score from server failure, resultCode = " + result);
                }
            }
        });
    }

    // 消费积分
    public static void spendScoreFromServer(final Context context, int score) {
        App.spendScore(new SpendScoreFeedback() {
            @Override
            public void doAfterSpendScore(int result, int score) {
                /**
                 * resultNo : 1 成功； 2 失败； 3 积分不够消费；
                 * score ：当前该用户最新的积分
                 */
                if (result == RESULT_SUCCESS) {
                    // 激活成功
                    PreferenceUtil.saveRecord(context, KEY_GAME_ACTIVED, true);
                    Log.i(TAG, "spend score from server success, game actived");
                } else {
                    Log.e(TAG, "spend score from server failure, resultCode = " + result + ", server score = " + score);
                }
            }
        }, score);
    }

    // 消费积分
    public static void spendScoreFromServer(final Context context, int score,
                                            final SpendScoreFeedback doAfterSpendScore) {
        App.spendScore(new SpendScoreFeedback() {
            @Override
            public void doAfterSpendScore(int result, int score) {
                /**
                 * resultNo : 1 成功； 2 失败； 3 积分不够消费；
                 * score ：当前该用户最新的积分
                 */
                Log.d(TAG, "App.spendScore(), doAfterSpendScore()" +
                        ", result = " + result + ", score = " + score);
                // 获取最新的积分
                getScoreFromLocal();

                doAfterSpendScore.doAfterSpendScore(result, score);
            }
        }, score);
    }

    // 初始化
    public static void init(Context context) {
        App.init(context);
        LogUtils.enableLog(isLogDebug(context));

        // 从服务器获取积分，提前获取
        getScoreFromServer();
        getScoreFromLocal();
    }

    // 结束化
    public static void fini() {
        App.destroy();
    }

    // 初始化广告条
    public static void initBannerAd(final Context context, final BannerAd bannerAd) {
        mBannerAdShowMillis = ManifestUtil.getMetadataAsInt(context, DF_YEAHYOO_SHOWADMILLIS, 5000);
        mBannerAdHideMillis = ManifestUtil.getMetadataAsInt(context, DF_YEAHYOO_HIDEADMILLIS, 5000);
    }

    // 结束化广告条
    public static void finiBannerAd(final Context context, final BannerAd bannerAd) {
        if (bannerAd != null) {
            Log.d(TAG, "yeahyoo bannerAd destroy");
            bannerAd.destroy();
        }
    }

    // 设置不显示广告条
    public static void saveRecordOfShowBannerAdFalse(Context context) {
        PreferenceUtil.saveRecord(context, KEY_GAME_SHOW_BANNER_AD, false);
    }

    // 设置要显示广告条
    public static void saveRecordOfShowBannerAdTrue(final Context context) {
        PreferenceUtil.saveRecord(context, KEY_GAME_SHOW_BANNER_AD, true);
    }

    // 是否能够显示广告条
    public static boolean readRecordOfShowBannerAd(final Context context) {
        return PreferenceUtil.readRecord(context, KEY_GAME_SHOW_BANNER_AD, true);
    }

    // 隐藏广告条
    public static void hideBannerAd(final Context context, final BannerAd bannerAd) {
        if (bannerAd != null) {
            bannerAd.hide();
            Log.d(TAG, "yeahyoo bannerAd hide");
        }
    }

    // 显示广告条
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
                        // 用户点击过广告后，不再显示
                        saveRecordOfShowBannerAdFalse(context);
                        hideBannerAd(context, bannerAd);
                    }
                });
            }
        }
    }

    // 显示广告条（始终显示）
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
//                        // 用户点击过广告后，不再显示
//                        saveRecordOfShowBannerAdFalse(context);
//                        hideBannerAd(context, bannerAd);
                    }
                });
            }
        }
    }

    // 切换广告条的显示和隐藏
    public static void toggleBannerAd(final Context context, final BannerAd bannerAd) {

        boolean canShow = readRecordOfShowBannerAd(context);
        if (!canShow) {
            return;
        }

        long upTimeMillis = SystemClock.uptimeMillis();
        if (mBannerAdLastMillis == 0L) {
            mBannerAdLastMillis = upTimeMillis;
            initBannerAd(context, bannerAd);
            // 第一次进入先显示广告
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

    // 显示广告列表
    public static void showDownList(final Context context) {
        DownListAd.show(context, new UserActiveAdFeedback() {
            @Override
            public void doAfterActive(int resultCode, int score) {
                Log.d(TAG, "DownListAd.show(), doAfterActive()" +
                        ", result = " + resultCode + ", score = " + score);
                // 获取最新的积分
                getScoreFromLocal();
            }
        });

    }

    // 显示广告列表
    public static void showDownList(final Context context,
                                    final UserActiveAdFeedback doAfterActive) {
        DownListAd.show(context, new UserActiveAdFeedback() {
            @Override
            public void doAfterActive(int resultCode, int score) {
                Log.d(TAG, "DownListAd.show(), doAfterActive()" +
                        ", result = " + resultCode + ", score = " + score);
                // 获取最新的积分
                getScoreFromLocal();

                // 再次调用用户的回调函数
                doAfterActive.doAfterActive(resultCode, score);
            }
        });

    }


    // 显示对话框（退出游戏、装机必备）
    public static void showDialogOfQuitAndMust(final Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage("退出游戏")
                .setPositiveButton("退出游戏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityUtil.quitActivity(context);
                    }
                })
                .setNegativeButton("装机必备", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showDownList(context);
                    }
                })
                .create();
        dialog.show();
    }
}
