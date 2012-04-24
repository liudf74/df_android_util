package df.util.app;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-13
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.uucun.adsdk.UUAppConnect;
import com.uucun.adsdk.UpdatePointListener;
import df.util.android.ApplicationUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.type.NumberUtil;
import df.util.type.StringUtil;

import java.util.Calendar;
import java.util.Random;

//import com.yeahyoo.SpendScoreFeedback;
//import com.yeahyoo.UserActiveAdFeedback;
//import df.util.ad.Yeahyoo;

/**
 * gameleader，神仙斗地主，appjoy
 */
public class DooyogameSxddzAppjoy {

    public static final String TAG = "df.util.app.DooyogameSxddzAppjoy";

    /**
     * //     * 玩家初始积分：100分
     * //     * 输掉一盘或者赢一盘：±20~40分，每局游戏积分进行累积
     * //     * 下载1个广告应用赠送积分：60~120分
     * //     * 系统送积分：当用户积分=0时，系统默认每天送50分，送满了100分封顶，不再送
     * <p/>
     * <p/>
     * todo: wulong:以上的扣费逻辑作废，以下面为准 2012.03.31.16.13。
     * 收场地费逻辑：
     * 1.为0的情况下，每天送20积分
     * 2.每局收场地费15分
     * 3.用户第一次玩送20分
     * endif
     */

//todo: wulong测试appjoy分数
    public static final int SCORE_INIT = 20;
    public static int mServerScore = 0;//服务器返回分数
    public static int mSaveScore = 0;//赠送的积分，存储在本地
    public static int mDebtScore = 0;//欠债，本地分不够扣，记录下欠多少分，当服务器取到分数后，从服务器扣除
    //每天赠送一次分数，当积分为零不能就玩时送出
    public static final String KEY_GAVE_SCORE_EVERYDAY = "game.sxddz.everyday.lastdate";
    public static final int SCORE_EVERYDAY_GIFT = 20;
    public static final int SCORE_RENT = 15;//每局收场地费15分
    //endif


    public static boolean mGettingScoreFlag = false;//正在从服务器获取积分，解决下载广告后服务器无返回积分问题
    // 总积分
    public static final String KEY_GAME_SCORE = "game.sxddz.score";
    //    // 每日赠送的积分（从最近一次赠送开始的累计积分）
//    public static final String KEY_GAME_GIVE_SCORE               = "game.sxddz.give_score";
    // 最后一次赠送积分的日期
    public static final String KEY_GAME_GIVE_DATE = "game.sxddz.give_date";
    // 人物是否解锁标志
    public static final String KEY_GAME_AVATARINDEX_UNLOCKED_SET = "game.sxddz.unlocked_avatarindex_set";

    //
    public static boolean theIsGetScoreDialogShown = false;

    // 角色名称
    public static String[] avatarNameArray = {
            "圣武者", // 0
            "斗士", // 1
            "猎人", // 2
            "圣徒", // 3
            "先知", // 4
            "幽灵武士", // 5
            "元素使", // 6
            "咒术师", // 7
            "食人魔", // 8
            "半灵犬" // 9

//            "如来", // 0
//            "牛头", // 1
//            "马面", // 2
//            "观音", // 3
//            "龟仙", // 4
//            "圣母", // 5
//            "耶稣", // 6
//            "土地", // 7
//            "阎王", // 8
//            "真主", // 9
    };

    // 特效
    public static String[] theSkillNameArray = new String[]{
            "sxddzmaya_skill_shengji_5x3",      // "单攻技能：圣击\n技能说明：以神圣之力来攻击敌方\n"
//            "sxddzmaya_skill_sheyao_4x3",       // "单体技能：蛇咬\n技能说明：矛舞如蛟蛇，对单个敌人进行疯狂攻击，能使目标短时间内陷入出血状态，有机率无视盾牌防御\n"
            "sxddzmaya_skill_lianhuanjian_4x3", // "单体技能：连环箭\n技能说明：瞄准目标三箭连射，威力惊人\n"
            "sxddzmaya_skill_jijiushu_4x2",     // "单体技能：急救术\n技能说明：立即恢复目标大量HP\n"
//            "sxddzmaya_skill_moliranshao_3x2",  // "单体技能：魔力燃烧\n技能说明：燃烧目标的MP值\n"
//            "sxddzmaya_skill_mingwu_4x2",       // "单体技能：冥舞\n技能说明：幽冥之舞，以暗黑之力攻击敌人\n"
//            "sxddzmaya_skill_fenglingdan_4x3",  // "单体技能：风灵弹\n技能说明：凝聚风元素攻击敌人\n"
//            "sxddzmaya_skill_shihuashu_3x2",    // "单体技能：石化术\n技能介绍：短时间内使敌人身体石化，使其不可行动，亦不会受到攻击\n"
//            "sxddzmaya_skill_zhenfen_3x2",      // "群体技能：振奋\n技能说明：短时间内提升所有友方佣兵的攻击力\n"
            "sxddzmaya_skill_dangji_3x2",       // "群体攻击：荡击\n技能说明：抖转长矛，挑起枪花，给敌潇洒一击，装备利器时才可以使用，有机率无视盾牌防御\n"
            "sxddzmaya_skill_paoliejian_4x2",   // "群体技能：爆裂箭\n技能说明：箭头会爆炸的箭术，可攻击到大范围的敌人\n"
//            "sxddzmaya_skill_shenzhijuangu_4x2",// "单体技能：神之眷顾\n技能说明：在此状态下，人物死亡后可自动复活一次，并恢复部分HP与MP\n"
//            "sxddzmaya_skill_shenyou_4x2",      // "群体技能：神佑\n技能说明：短时间內增加目标HP、MP最大值\n"
//            "sxddzmaya_skill_shidu_4x2",        // "群体技能：尸毒\n技能说明：驱逐尸体上的毒疫，在短时间內让尸体周围的敌群陷入中毒狀態\n"
//            "sxddzmaya_skill_bingfeng_4x2",     // "群体技能：冰封\n技能说明：将空气中的所有水元素冻结，使群敌陷入延迟状态无法动弹\n"
//            "sxddzmaya_skill_shufushu_4x2",     // "单体技能：束缚术\n技能介绍：短时间内使敌人无法使用物理攻击技能\n"
    };
    public static BitmapDrawable[][] theSkillDrawableArray = new BitmapDrawable[theSkillNameArray.length][];

    public static int theLastPaiFlag = -1;
    public static int theLastPaiNumber = -1;
    public static int theLastDrawSkillA = -1;
    public static int theLastDrawSkillB = -1;
    public static int theLastDrawSkillC = -1;
    public static int theLastDrawFrame = -1;
    public static boolean theLastDrawFrameFinished = false;
    public static long theLastDrawTimeMillis = 0;
    public static long theStartDrawTimeMillis = 0;
    public static long theDrawSpanMinTimeMillis = 5000;

    public static final int SKILL_TIMEMILLIS_FRAME_SWITCH = 50;   // 每帧切换时间间隔
    public static final int SKILL_TIMEMILLIS_FRAME_SINGLE = 100;   // 每帧显示时间长度
    public static final int SKILL_TIMEMILLIS_FRAME_LAST = 500;   // 最后一帧显示时间长度

    private static final Random theRandom = new Random(System.currentTimeMillis());
    private static Context mContext = null;

    private static final int MSG_EVERYDAY_GIFT = 10;
    private static final int MSG_INIT_SEND_SCORE = 11;
    private static final int MSG_RENT_COST = 12;

    private static final Handler theNotifyHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_EVERYDAY_GIFT: //每日赠送积分
                    everyDayGiftFunc();
                    break;
                case MSG_RENT_COST:
                    spendRentScoreFunc();
                    break;
                case MSG_INIT_SEND_SCORE:
                    firstLoginFunc();
                    break;
                default:
                    break;
            }
        }
    };


    //////////////////////////////////////////////////////////////
    // 公共方法
    //////////////////////////////////////////////////////////////

    private static int toHeightFrom480320(int screenH, int yOf480320) {
        return yOf480320 * screenH / 320;
    }

    private static int toWidthFrom480320(int screenW, int xOf480320) {
        return xOf480320 * screenW / 480;
    }

    ////////////////////////////////////////////////////////////////
    // 人物解锁逻辑
    ////////////////////////////////////////////////////////////////

    // 人物是否解锁
    public static boolean isAvatarUnlocked(final Context context, int avatarIndex) {
        if ((avatarIndex < 0) || (avatarIndex > 9)) {
            Log.d(TAG, "avatarIndex illegal, avatarIndex = " + avatarIndex);
            return true;
        }

        String set = PreferenceUtil.readRecord(context, KEY_GAME_AVATARINDEX_UNLOCKED_SET, "");

        // 初始化部分解锁人物
        if (StringUtil.empty(set)) {
            set = "0,1,2,3,";
            PreferenceUtil.saveRecord(context, KEY_GAME_AVATARINDEX_UNLOCKED_SET, set);
            Log.d(TAG, "init unlocked avatar, unlocked avatarIndex set = " + set);
        }

        final String key = String.valueOf(avatarIndex) + ",";
        return StringUtil.contains(set, key);
    }


    // 解锁人物
    public static void unlockAvatar(final Context context, int avatarIndex) {
        final String key = String.valueOf(avatarIndex) + ",";
        String set = PreferenceUtil.readRecord(context, KEY_GAME_AVATARINDEX_UNLOCKED_SET, "");

        Log.d(TAG, "unlockAvatar, unlocked avatarIndex set = " + set);

        if (!StringUtil.contains(set, key)) {
            set += key;
            PreferenceUtil.saveRecord(context, KEY_GAME_AVATARINDEX_UNLOCKED_SET, set);
            Log.d(TAG, "unlock avatar, unlocked avatarIndex set = " + set);
        }
    }

    // 显示“解锁人物”
    public static void drawHintOfSelectAvatar(final Context context,
                                              Canvas canvas,
                                              int avatarIndex,
                                              int screenW, int screenH) {
        // todo: 坐标位置固定
        final int x = 225 * screenW / 480;
        final int y = 275 * screenH / 320;

        boolean unlocked = isAvatarUnlocked(context, avatarIndex);
        if (unlocked) {
            String hint = "请点击头像选择";
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setARGB(0xff, 0xff, 0xff, 0xff);
            paint.setTextSize(screenH * 18.f / 320.f);
            canvas.drawText(hint, x, y, paint);
        } else {
            String hint = "激活应用解锁人物";
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setARGB(0xff, 0xff, 0xff, 0x00);
            paint.setTextSize(screenH * 18.f / 320.f);
            canvas.drawText(hint, x, y, paint);
        }
    }

    // 点击“解锁人物”
    public static void clickHintOfSelectAvatar(final Context context,
                                               final int avatarIndex,
                                               final int screenW, final int screenH,
                                               final int clickX, final int clickY
    ) {
        // todo: 文本信息坐标位置
        Log.d(TAG, "click hintOfSelectAvatar avatarIndex = " + avatarIndex);

        final int topleftX = 225 * screenW / 480;
        final int topleftY = 250 * screenH / 320;
        final int bottomrightX = 380 * screenW / 480;
        final int bottomrightY = 300 * screenH / 320;
        final Rect rect = new Rect(topleftX, topleftY, bottomrightX, bottomrightY);
        if (!rect.contains(clickX, clickY)) {
            return;
        }

        boolean unlocked = isAvatarUnlocked(context, avatarIndex);
        // 已经解锁，无动作
        // 未解锁的人物，下载广告
        if (!unlocked) {
//todo: wulong弹出广告墙
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("温馨提示");
            builder.setMessage("该人物未解锁,您可以点击这里收看广告后解锁该人物！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    showWall(context);
                    unlockAvatar(context, avatarIndex);
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
//endif
        }
    }

    /**
     * 初始化appjoy接口
     *
     * @param context
     */
    public static void initAppjoySdk(Context context) {
        Log.d(TAG, "init appjoy sdk...");
        UUAppConnect.getInstance(context).initSdk();
    }

    //显示广告墙
    public static void showWall(Context context) {
        Log.d(TAG, "showWall");
        UUAppConnect.getInstance(context).showOffers();
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
                showWall(context);
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

    ////////////////////////////////////////////////////////////////
    // 游戏内获得积分
    ////////////////////////////////////////////////////////////////

    // 初始化积分
    public static void initScore(final Context context) {
        setContext(context);

        long giveDate = PreferenceUtil.readRecord(context, KEY_GAME_GIVE_DATE, 0L);
        if (giveDate <= 0) {
            giveDate = System.currentTimeMillis();
            PreferenceUtil.saveRecord(context, KEY_GAME_GIVE_DATE, giveDate);
            //
            mSaveScore = SCORE_INIT;
            PreferenceUtil.saveRecord(context, KEY_GAME_SCORE, mSaveScore);
            Log.d(TAG, "hainotify init score, score not given yet, mSaveScore = " + mSaveScore);
            Message message = new Message();
            message.what = MSG_INIT_SEND_SCORE;
            theNotifyHandler.sendMessage(message);
        }

        //todo: wulong游戏初始化时需要把服务器积分与本地赠送积分累加
        getScoreFromServer(context);
    }


    private static String getToday() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH);//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        String today = (year + ":" + month + ":" + day);
        return today;
    }

    /**
     * 每日赠送积分，每日赠送一次，积分为零时送
     */
    private static void everyDayGiftFunc() {
        Log.i(TAG, "hainotify: everyDayGiftFunc");
        mSaveScore = PreferenceUtil.readRecord(mContext, KEY_GAME_SCORE, 0);
        mSaveScore += SCORE_EVERYDAY_GIFT;
        PreferenceUtil.saveRecord(mContext, KEY_GAME_SCORE, mSaveScore);
//        Toast.makeText(mContext, "恭喜获得每日赠送积分" + SCORE_EVERYDAY_GIFT + "分", Toast.LENGTH_SHORT);
        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("恭喜")
                .setMessage("恭喜您获得每日赠送积分" + SCORE_EVERYDAY_GIFT + "分，祝您玩得愉快!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }).create();
        dlg.show();
    }


    /**
     * 每局收取场租费
     */
    private static void spendRentScoreFunc() {
        Log.i(TAG, "hainotify: spendRentScoreFunc");
        lostLevelScore(mContext, SCORE_RENT);
//        Toast.makeText(mContext, "收取场地费" + SCORE_RENT + "分", Toast.LENGTH_SHORT);
        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("祝您好运")
                .setMessage("收取场租费" + SCORE_RENT + "分")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }).create();
        dlg.show();
    }


    /**
     * 初次登陆赠送分数
     */
    private static void firstLoginFunc() {
        Log.i(TAG, "hainotify: firstLoginFunc");
        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("恭喜")
                .setMessage("初次登陆赠送" + SCORE_INIT + "分")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }).create();
        dlg.show();
    }

    //每天赠送一次积分，分数为0时送
    private static void checkEverydayGiftScore(final Context context) {

        String lastDay = PreferenceUtil.readRecord(context, KEY_GAVE_SCORE_EVERYDAY, "");
        String today = getToday();
        Log.i(TAG, "hainotify: checkEverydayGiftScore lastDay = " + lastDay + " today = " + today);

        if (!StringUtil.equals(lastDay, today) && getTotalScore() <= 0) {
            Message message = new Message();
            message.what = MSG_EVERYDAY_GIFT;
            theNotifyHandler.sendMessage(message);
            PreferenceUtil.saveRecord(context, KEY_GAVE_SCORE_EVERYDAY, today);
        }
    }

    /**
     * 每场开始都收场地费
     *
     * @param context
     */
    private static void checkRentScore(final Context context) {
        Log.i(TAG, "hainotify: checkRentScore");
        Message message = new Message();
        message.what = MSG_RENT_COST;
        theNotifyHandler.sendMessage(message);
    }

    // 游戏是否能够运行？
    public static boolean canRun(final Context context) {
        // 积分够，可以运行
        if (getTotalScore() > 0) {
            return true;
        }

        // 正在提示用户，不再重复提示
        if (theIsGetScoreDialogShown) {
            Log.d(TAG, "get score dialog is run");
            return false;
        }

        checkNeedAdvertiseDlg(context);
        return false;
    }


    /**
     * 当分数为0时，在游戏界面向服务器请求一次分数。如果仍然为0，则跳出对话框
     * 为了解决下载广告后无返回的问题
     */
    private static void checkNeedAdvertiseDlg(final Context context) {

        if (mGettingScoreFlag) {
            Log.i(TAG, "checkNeedAdvertiseDlg mGettingScoreFlag = " + mGettingScoreFlag);
            return;
        }

        if (getTotalScore() > 0) {
            return;
        }

        //如果没向服务器查询过分数，且分数为0，则置为查询状态，直到查到分数为止，查完分数仍为0，则跳到广告界面
        mGettingScoreFlag = true;

        UUAppConnect.getInstance(context).getPoints(
                new UpdatePointListener() {
                    @Override
                    public void onError(String s) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Log.e(TAG, "checkNeedAdvertiseDlg onError s = " + s);
                        showClickAdDlg(context);
                        mGettingScoreFlag = false;
                        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
                        mServerScore = 0;
                    }

                    @Override
                    public void onSuccess(String s, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
//                       todo: wulong本地存储的只是赠送分数，游戏中产生的分数都在服务器
                        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
                        mServerScore = i;
                        Log.i(TAG, "checkNeedAdvertiseDlg onSuccess get score, old total mSaveScore = " + mSaveScore + "  i = " + i);

                        if (getTotalScore() <= 0) {
                            showClickAdDlg(context);
                        }

                        mGettingScoreFlag = false;
                    }
                }
        );
    }

    /**
     * 显示对话框，按确认键跳转到广告界面
     */
    private static void showClickAdDlg(final Context context) {
        theIsGetScoreDialogShown = true;
        // 积分是否不足时显示广告下载提示信息
        Log.i(TAG, "prompt user to get score");
        final String hintMessage = String.format(
                "亲爱的朋友，您的游戏积分不足，需要联网下载广告后才能继续游戏。"
        );
        final String title = "提示";
        final String btnDownload = "下载广告";

        final AlertDialog scoreDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(hintMessage)
                .setPositiveButton(btnDownload, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        // 显示下载广告
                        clickGetScore(context);
                        //
                        theIsGetScoreDialogShown = false;
                        mGettingScoreFlag = true;
                        // 关闭当前对话框
                        dialogInterface.cancel();
                    }
                })
                .create();
        scoreDialog.show();

    }

    private static void setContext(Context context) {
        if (null != context) {
            mContext = context;
        }
    }

    // 点击“获取积分”
    public static void clickGetScore(final Context context) {
//todo: wulong获得积分
        getScoreFromServer(context);
        showWall(context);
    }

    public static void getScoreFromServer(final Context context) {
        Log.i(TAG, "getScoreFromServer");
        UUAppConnect.getInstance(context).getPoints(
                new UpdatePointListener() {
                    @Override
                    public void onError(String s) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Log.e(TAG, "hainotify: getScoreFromServer onError s = " + s);
                        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
                        mServerScore = 0;
                    }

                    @Override
                    public void onSuccess(String s, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
//                       todo: wulong本地存储的只是赠送分数，游戏中产生的分数都在服务器
                        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
                        mServerScore = i;

                        if (mDebtScore > 0) {
                            //本地不够扣，从服务器取，服务器不同步，什么时候取到什么时候扣
                            mServerScore -= mDebtScore;
                            lostLevelScore(context, mDebtScore);
                        }

                        Log.i(TAG, "hainotify: getScoreFromServer onSuccess mSaveScore = " + mSaveScore + "  i = " + i + " mDebtScore = " + mDebtScore);
                        mGettingScoreFlag = false;
                    }
                }
        );
    }

    // 获得积分数值
    public static int getTotalScore() {
//todo: wulong赠送分数玩了就不管,不实时更新，也许要点两次
        return (mServerScore + mSaveScore);
    }

    // 获得积分数值字符串
    public static String getTotalScoreAsString(final Context context) {
//todo: wulong赠送分数完了就不管本地分
        return String.valueOf(getTotalScore());
    }

    // 获得“积分x”的字符串
    public static String getTotalScoreValueAndString(final Context context) {
        return getTotalScore() + "积分";
    }

    // 根据结果处理一局结束的逻辑
    public static void processLevelResult(final Context context, final int leftPaiCount, final int point) {
        Log.i(TAG, "processLevelResult leftPaiCount = " + leftPaiCount + " point = " + point);
//todo: wulong:appjoy赢了point是整数，输了是负数
        if (point < 0) {
            lostLevelScore(context, (-point));
        } else {
            winLevelScore(context, point);
        }
    }

    // 本局失败
    public static void lostLevelScore(final Context context, final int point) {
//todo: wulong开始赠送100分，本地积分只扣不加，本段重写
//todo: wulong:appjoy减去积分

        if (point <= 0) {
            return;
        }

        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
        Log.i(TAG, "lostLevelScore, mSaveScore = " + mSaveScore + " point = " + point + " mServerScore = " + mServerScore);

        int lostScore = point;

//todo: wulong:appjoy先扣本地分数，不够再扣服务器分数
        if (lostScore > mSaveScore) {
            mDebtScore = lostScore - mSaveScore;//没扣干净，记录一下从服务器获取的时候扣除
            mSaveScore = 0;
            lostScore = mServerScore;//扣的分数超过服务器分数，服务器就不会扣掉
            mServerScore -= mDebtScore;
            if (mServerScore < 0) {
                mServerScore = 0;
            } else {
                lostScore = mDebtScore;
            }
//todo: wulong:appjoy本地不够就扣服务器
            spendScoreCommitToServer(context, lostScore);
        } else {
            mSaveScore -= lostScore;
        }

        PreferenceUtil.saveRecord(context, KEY_GAME_SCORE, mSaveScore);
        Log.i(TAG, "lostLevelScore, new mSaveScore = " + mSaveScore + " mServerScore = " + mServerScore + " lostScore = " + lostScore);
    }

    private static void spendScoreCommitToServer(Context context, final int score) {
        UUAppConnect.getInstance(context).spendPoints(score, new UpdatePointListener() {
            @Override
            public void onError(String error_msg) {
                Log.i(TAG, "hainotify:spendScoreCommitToServer onError error_msg = " + error_msg + " score = " + score);
            }

            @Override
            public void onSuccess(String point_unit, int user_point) {
                //To change body of implemented methods use File | Settings | File Templates.
                Log.i(TAG, "hainotify:spendScoreCommitToServer onSuccess point_unit = " + point_unit + "  user_point = " + user_point  + " score = " + score);
                mDebtScore = 0;
            }
        });
    }

    // 本局胜利
    public static void winLevelScore(final Context context, final int point) {
//todo: wulong获得分数都存储在本地，不用上传服务器
        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
        Log.i(TAG, "winLevelScore, old mSaveScore = " + mSaveScore + "  point = " + point + " mServerScore = " + mServerScore);

        mSaveScore += point;
        PreferenceUtil.saveRecord(context, KEY_GAME_SCORE, mSaveScore);
        Log.i(TAG, "winLevelScore, new total saveScore = " + mSaveScore + " mServerScore = " + mServerScore);
    }

    private static int mGameStage = 0;
    public static void checkNewGame() {

        if (null != mContext) {
            Log.i(TAG, "hainotify: checkNewGame update mGameStage = " + mGameStage);
            checkEverydayGiftScore(mContext);
            checkRentScore(mContext);
        } else {
            Log.i(TAG, "hainotify: checkNewGame mContext == null mGameStage = " + mGameStage);
        }

        mGameStage++;
    }

    ////////////////////////////////////////////////////////////////
    // 动作特效
    ////////////////////////////////////////////////////////////////

    // 新开一局初始化（传送2个角色索引过来）
    public static void newLevel(Context context) {

        setContext(context);

        Log.i(TAG, "hainotify: newLevel");
        // 回收图片
        recycleBitmap();

        // 初始化图片
        for (int skill = 0; skill < theSkillNameArray.length; skill++) {
            BitmapDrawable[] skillSingleDrawableArray = findSkillDrawableArray(
                    context, theSkillNameArray, theSkillDrawableArray, skill);
            assert (skillSingleDrawableArray != null);
        }
    }

    // 游戏界面，绘制技能特效
    public static void drawSkillOfAvatar(Context context, Canvas canvas,
                                         int screenW, int screenH,
                                         int paiNumber, int paiFlag) {
        // 特定牌类型时才显示特效; 牌数有重复时显示多点特效
        // 牌类型定义：1单, 2对, 3三, 4三带1, 5三带2, 6炸弹, 7王炸, 8顺子, 9顺子2, a顺子3, b三顺1, c三顺2, d四带2
        int skillACount = 0, skillBCount = 0, skillCCount = 0;
        if ((9 == paiFlag) || (0xc == paiFlag) || (0xd == paiFlag)) {
            skillACount = 1;
        } else if ((0xa == paiFlag) || (0xb == paiFlag)) {
            skillACount = 2;
        } else if ((6 == paiFlag)) {
            skillACount = 1;
            skillBCount = 2;
        } else if ((7 == paiFlag)) {
            skillACount = 1;
            skillBCount = 2;
            skillCCount = 3;
        } else if ((8 == paiFlag)) {
            // todo: 测试
            skillACount = 1 + (paiNumber - 5) / 2;
//            skillACount = 2;
//            skillBCount = 2;
//            skillCCount = 1;
        } else if ((paiNumber < 6)) {
//            Log.d(TAG, "paiNumber is to small, paiFlag = " + paiFlag + ", paiNumber = " + paiNumber);
            return;
        }


        // 如果切换了牌，则重新绘制
        if ((theLastPaiFlag != paiFlag) || (theLastPaiNumber != paiNumber)) {
            theLastPaiFlag = paiFlag;
            theLastPaiNumber = paiNumber;
            theLastDrawTimeMillis = theStartDrawTimeMillis = 0;
            theLastDrawFrame = -1;
            theLastDrawFrameFinished = false;
            theLastDrawSkillA = theLastDrawSkillB = theLastDrawSkillC = -1;
        }
        // 定时绘制一帧
        long now = System.currentTimeMillis();
        if (theLastDrawTimeMillis == 0) {
            theLastDrawTimeMillis = now;
        }
        if (theStartDrawTimeMillis == 0) {
            theStartDrawTimeMillis = now;
        }
        if (now - theLastDrawTimeMillis > SKILL_TIMEMILLIS_FRAME_SWITCH) {
            theLastDrawTimeMillis = now;

            // 是否结束绘制
            boolean isDraw = true;
            if ((now - theStartDrawTimeMillis > theDrawSpanMinTimeMillis)      // 超出时间
                    && (theLastDrawFrameFinished)/*(theLastDrawFrame == 0)*/                                 // 绘制了最后一帧
                    ) {
                isDraw = false;
            }

            if (isDraw) {
                Log.d(TAG, "paiFlag = " + paiFlag + ", paiNumber = " + paiNumber +
                        ", skillACount = " + skillACount + ", skillBCount = " + skillBCount + ", skillCCount = " + skillCCount);

                // 技能图片
                int maxSkillDrawableArrayCount = 0;
                // 技能A
                if (theLastDrawSkillA == -1) {
                    theLastDrawSkillA = theRandom.nextInt(theSkillNameArray.length);
                }
                BitmapDrawable[] skillADrawableArray = findSkillDrawableArray(
                        context, theSkillNameArray, theSkillDrawableArray, theLastDrawSkillA);
                assert (skillADrawableArray != null);
                if (maxSkillDrawableArrayCount < skillADrawableArray.length) {
                    maxSkillDrawableArrayCount = skillADrawableArray.length;
                }
                // 技能B
                if (theLastDrawSkillB == -1) {
                    theLastDrawSkillB = theRandom.nextInt(theSkillNameArray.length);
                }
                BitmapDrawable[] skillBDrawableArray = findSkillDrawableArray(
                        context, theSkillNameArray, theSkillDrawableArray, theLastDrawSkillB);
                assert (skillBDrawableArray != null);
                if (maxSkillDrawableArrayCount < skillBDrawableArray.length) {
                    maxSkillDrawableArrayCount = skillBDrawableArray.length;
                }
                // 技能C
                if (theLastDrawSkillC == -1) {
                    theLastDrawSkillC = theRandom.nextInt(theSkillNameArray.length);
                }
                BitmapDrawable[] skillCDrawableArray = findSkillDrawableArray(
                        context, theSkillNameArray, theSkillDrawableArray, theLastDrawSkillC);
                assert (skillCDrawableArray != null);
                if (maxSkillDrawableArrayCount < skillCDrawableArray.length) {
                    maxSkillDrawableArrayCount = skillCDrawableArray.length;
                }

                // 反复绘制所有帧
                theLastDrawFrame++;
                if (theLastDrawFrame > maxSkillDrawableArrayCount - 1) {
//                    theLastDrawFrame = 0;
                    theLastDrawFrameFinished = true;
                }
                Log.d(TAG, "theLastDrawFrame = " + theLastDrawFrame);

                // 绘制特效
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillADrawableArray, theLastDrawFrame, skillACount);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillBDrawableArray, theLastDrawFrame, skillBCount);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillCDrawableArray, theLastDrawFrame, skillCCount);

                // 延迟一会，显示本帧特效
                try {
                    int sleep = SKILL_TIMEMILLIS_FRAME_SINGLE;
                    if (theLastDrawFrame == maxSkillDrawableArrayCount - 1) {
                        sleep = SKILL_TIMEMILLIS_FRAME_LAST;
                    }
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                }
            }
        }
    }


    private static void drawSkillOfAvatarOfSkill(Canvas canvas, int screenW, int screenH,
                                                 BitmapDrawable[] skillDrawableArray,
                                                 final int frame,
                                                 int skillCount) {
        for (int i = 0; i < skillCount; i++) {
            // 对于多点显示时，设置不同的落点
            int startLeft = 0;
            int startTop = 0;
            if (i > 0) {
                int offset = theRandom.nextInt(200);
                startLeft += offset;
                startTop += offset;
            }

            // 绘制技能
            if (frame < skillDrawableArray.length) {
                Drawable frameDrawable = skillDrawableArray[frame];
                final int left = (int) (screenW * 0.25) + startLeft;
                final int top = (int) (screenH * 0.25) + startTop;
                final int right = (int) (screenW * 0.75) + startLeft;
                final int bottom = (int) (screenH * 0.75) + startTop;
                frameDrawable.setBounds(left, top, right, bottom);
                frameDrawable.draw(canvas);
            }
        }
    }

    private static BitmapDrawable[] findSkillDrawableArray(Context context,
                                                           final String[] paramNameArray,
                                                           final BitmapDrawable[][] paramDrawArray,
                                                           final int skill) {
        assert ((skill >= 0) && (skill < theSkillNameArray.length));
        BitmapDrawable[] frameDrawableArray = paramDrawArray[skill];
        if (null == frameDrawableArray) {
            String skillName = paramNameArray[skill];
            if (StringUtil.empty(skillName)) {
                Log.d(TAG, "name is empty, avatarIndex = " + skill);
            } else {
                frameDrawableArray = initDrawArray(context, skillName);
                paramDrawArray[skill] = frameDrawableArray;
            }
        }
        return frameDrawableArray;
    }

    private static BitmapDrawable[] initDrawArray(Context context, String totalName) {

        // 获得图片
        int totalId = ResourceUtil.getDrawableResourceIdFromName(context, totalName);
        BitmapDrawable totalDrawable = (BitmapDrawable) context.getResources().getDrawable(totalId);
        final Bitmap totalBitmap = totalDrawable.getBitmap();

        // todo: 如果图片在drawable目录下，minVersion=3的话，会缩放，因此我们获得实际图片大小
        int totalWidth = totalBitmap.getWidth();
        int totalHeight = totalBitmap.getHeight();

        // 名称格式举例：sxddzmaya_skill_shengji_4x3
        String[] nameSet = StringUtil.split2(totalName, "_");
        assert (nameSet.length > 0);
        String cellDefine = nameSet[nameSet.length - 1];
        String[] cellDefineSet = StringUtil.split2(cellDefine, "x");
        assert (cellDefineSet.length == 2);
        final int rowCount = NumberUtil.toInt(cellDefineSet[1]);
        final int colCount = NumberUtil.toInt(cellDefineSet[0]);
        Log.d(TAG, "name = " + totalName + ", id = " + totalId +
                ", row = " + rowCount + ", col = " + colCount +
                ", totalWidth = " + totalWidth + ", totalHeight = " + totalHeight);
        int width = totalWidth / colCount;
        int height = totalHeight / rowCount;

        // 创建每帧图片
        BitmapDrawable[] frameDrawableArray = new BitmapDrawable[rowCount * colCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                initDrawArray(totalBitmap, colCount, frameDrawableArray, width, height, row, col);
            }
        }

        return frameDrawableArray;
    }

    private static void initDrawArray(Bitmap totalBitmap, int colCount,
                                      BitmapDrawable[] frameDrawableArray,
                                      int width, int height,
                                      int row, int col) {
        int left = col * width;
        int top = row * height;
        Log.d(TAG, "left = " + left + ", top = " + top + ", width = " + width + ", height = " + height);
        Bitmap frameBitmap = Bitmap.createBitmap(totalBitmap, left, top, width, height);
        BitmapDrawable frameDrawable = new BitmapDrawable(frameBitmap);
        final int index = row * colCount + col;
        frameDrawableArray[index] = frameDrawable;
        Log.d(TAG, "init frameDrawableArray, index = " + index);
    }

    // 回收图片内存
    private static void recycleBitmap() {
        Log.d(TAG, "recycle bitmap");
        for (int i = 0; i < theSkillDrawableArray.length; i++) {
            BitmapDrawable[] drawableArray = theSkillDrawableArray[i];
            if (drawableArray != null) {
                for (int j = 0; j < drawableArray.length; j++) {
                    BitmapDrawable drawable = drawableArray[j];
                    if (drawable != null) {
                        drawable.getBitmap().recycle();
                        drawableArray[j] = null;
                    }
                }
                theSkillDrawableArray[i] = null;
            }
        }
    }
}
