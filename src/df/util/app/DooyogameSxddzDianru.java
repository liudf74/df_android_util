package df.util.app;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-13
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.dianru.sdk.AdLoader;
import com.dianru.sdk.AdSpace;
import df.util.android.ApplicationUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.android.TelephoneUtil;
import df.util.type.NumberUtil;
import df.util.type.StringUtil;
import df.util.type.TimeUtil;

import java.util.Calendar;
import java.util.Random;

//import com.yeahyoo.SpendScoreFeedback;
//import com.yeahyoo.UserActiveAdFeedback;
//import df.util.ad.Yeahyoo;

/**
 * gameleader，神仙斗地主，点入20120406
 */
public class DooyogameSxddzDianru {

    public static final String TAG = "df.util.app.DooyogameSxddzDianru";

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

//todo: wulong测试dianjin分数
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
    public static final String KEY_GAME_LOCAL_SCORE = "game.sxddz.local.score";
    public static final String KEY_GAME_SERVER_SCORE = "game.sxddz.server.score";
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
//            "圣武者", // 0
//            "斗士", // 1
//            "猎人", // 2
//            "圣徒", // 3
//            "先知", // 4
//            "幽灵武士", // 5
//            "元素使", // 6
//            "咒术师", // 7
//            "食人魔", // 8
//            "半灵犬" // 9

            "如来", // 0
            "牛头", // 1
            "马面", // 2
            "观音", // 3
            "龟仙", // 4
            "圣母", // 5
            "耶稣", // 6
            "土地", // 7
            "阎王", // 8
            "真主"// 9
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


    ///////////////////////////////////////广告条添加关闭按钮////////////
    private static float mTouchX = 0.0f;
    private static float mTouchY = 0.0f;

    private static boolean mCloseAdGettingScore = false;
    private static boolean mCloseAdRequset = false;//从关闭按钮进入广告墙，则置为true
    private static int mBeforeCloseScore = 0;//用于验证是否有下载，用户点击关闭广告条按钮，下载获得积分后，关闭动作才有效
    private static final String KEY_CLOSE_AD_OK = "game.close.ad.ok";
/////////////////////////////////////


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
//todo: wulong 全部解锁可以玩点入无广告墙
        unlocked = true;

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
                    exitAdSdk(context);
                    ApplicationUtil.exitApp(context);
                }
            });
            builder.show();
//endif
        }
    }

    /**
     * 初始化广告接口
     *
     * @param context
     */
    public static void initAdSdk(Context context) {
        Log.d(TAG, "init initAdSdk sdk...");

    }


    //关闭广告条
    public static void closeAdvertise(final Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("关闭广告条提示");
//        builder.setMessage("下载并安装一份推荐软件就可以关闭广告条");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                Log.i(TAG, "hainotify: closeAdvertise button onClick");
//                mCloseAdRequset = true;
//                mBeforeCloseScore = getTotalScore();
//                showWall(context);
//            }
//        });
//        builder.show();
    }

    /**
     * 点击关闭广告按钮
     */
    public static void clickCloseAdvertise(final Context context) {
//        Log.i(TAG, "hainotify: clickCloseAdvertise");
//        int id = ResourceUtil.getIdResourceIdFromName(context, "sxddz_close_button");
//        View view = ((Activity) context).findViewById(id);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                closeAdvertise(context);
//            }
//        });
//
//
//        id = ResourceUtil.getIdResourceIdFromName(context, "sxddz_ad_bar");
//        view = ((Activity) context).findViewById(id);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    mTouchX = motionEvent.getX();
//                    mTouchY = motionEvent.getY();
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    float x = motionEvent.getX();
//                    float y = motionEvent.getY();
//                    float w = (x > mTouchX) ? (x - mTouchX) : (mTouchX - x);
//                    float h = (y > mTouchY) ? (y - mTouchY) : (mTouchY - y);
//
//                    if (w < 10 && h < 10) {
//                        showWall(context);
//                        Log.i(TAG, "hainotify: clickCloseAdvertise lay onTouch");
//                    }
//
//                }
//                return true;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
    }


    public static void retFromAdverAct() {
        Log.i(TAG, "hainotify: retFromAdverAct");
    }

    //标记以后都不需要显示广告条
    private static void saveCloseAdBannerFunc(Context context) {
        Log.i(TAG, "hainotify: saveCloseAdBannerFunc");
        PreferenceUtil.saveRecord(context, KEY_CLOSE_AD_OK, true);
        mCloseAdRequset = false;
    }

    //是否需要关闭广告条
    private static void needCloseAdBanner(Context context) {

        boolean flag = PreferenceUtil.readRecord(context, KEY_CLOSE_AD_OK, false);
        Log.i(TAG, "hainotify: needCloseAdBanner flag = " + flag);

        if (flag) {
            int id = ResourceUtil.getIdResourceIdFromName(context, "sxddz_ad_layout");
            View v = (View) ((Activity) context).findViewById(id);
            v.setVisibility(View.GONE);
        }
    }


    //显示广告墙
    public static void showWall(Context context) {
//        Log.d(TAG, "showWall");
//        final int id = ResourceUtil.getIdResourceIdFromName(context, "sxddz_ad_bar");
//        final AdSpace space = (AdSpace) ((Activity) context).findViewById(id);
//        space.setType(2);
    }

    /**
     * 断开广告接口
     *
     * @param context
     */
    public static void exitAdSdk(Context context) {
        Log.i(TAG, "exitAdSdk");
        AdLoader.destroy();
    }


    /**
     * 显示积分墙，提示用户去下载软件获取积分
     *
     * @param context
     */
    public static void showOfferWall(final Context context, String msg) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("温馨提示");
//        builder.setMessage(msg + ",您可以点击这里获取积分！");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                showWall(context);
//            }
//        });
//
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                exitAdSdk(context);
//                ApplicationUtil.exitApp(context);
//            }
//        });
//        builder.show();
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
            PreferenceUtil.saveRecord(context, KEY_GAME_LOCAL_SCORE, mSaveScore);
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
//        Log.i(TAG, "hainotify: everyDayGiftFunc");
//        mSaveScore = PreferenceUtil.readRecord(mContext, KEY_GAME_LOCAL_SCORE, 0);
//        mSaveScore += SCORE_EVERYDAY_GIFT;
//        PreferenceUtil.saveRecord(mContext, KEY_GAME_LOCAL_SCORE, mSaveScore);
////        Toast.makeText(mContext, "恭喜获得每日赠送积分" + SCORE_EVERYDAY_GIFT + "分", Toast.LENGTH_SHORT);
//        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("恭喜")
//                .setMessage("恭喜您获得每日赠送积分" + SCORE_EVERYDAY_GIFT + "分，祝您玩得愉快!")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //To change body of implemented methods use File | Settings | File Templates.
//                    }
//                }).create();
//        dlg.show();
    }


    /**
     * 每局收取场租费
     */
    private static void spendRentScoreFunc() {
//        Log.i(TAG, "hainotify: spendRentScoreFunc");
//        lostLevelScore(mContext, SCORE_RENT);
////        Toast.makeText(mContext, "收取场地费" + SCORE_RENT + "分", Toast.LENGTH_SHORT);
//        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("祝您好运")
//                .setMessage("收取场租费" + SCORE_RENT + "分")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //To change body of implemented methods use File | Settings | File Templates.
//                    }
//                }).create();
//        dlg.show();
    }


    /**
     * 初次登陆赠送分数
     */
    private static void firstLoginFunc() {
//        Log.i(TAG, "hainotify: firstLoginFunc");
//        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("恭喜")
//                .setMessage("初次登陆赠送" + SCORE_INIT + "分")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //To change body of implemented methods use File | Settings | File Templates.
//                    }
//                }).create();
//        dlg.show();
    }

    //每天赠送一次积分，分数为0时送
    private static void checkEverydayGiftScore(final Context context) {
//
//        String lastDay = PreferenceUtil.readRecord(context, KEY_GAVE_SCORE_EVERYDAY, "");
//        String today = getToday();
//        Log.i(TAG, "hainotify: checkEverydayGiftScore lastDay = " + lastDay + " today = " + today);
//
//        if (!StringUtil.equals(lastDay, today) && getTotalScore() <= 0) {
//            Message message = new Message();
//            message.what = MSG_EVERYDAY_GIFT;
//            theNotifyHandler.sendMessage(message);
//            PreferenceUtil.saveRecord(context, KEY_GAVE_SCORE_EVERYDAY, today);
//        }
    }

    /**
     * 每场开始都收场地费
     *
     * @param context
     */
    private static void checkRentScore(final Context context) {
//        Log.i(TAG, "hainotify: checkRentScore");
//        Message message = new Message();
//        message.what = MSG_RENT_COST;
//        theNotifyHandler.sendMessage(message);
    }

    // 游戏是否能够运行？
    public static boolean canRun(final Context context) {
//
//        checkCloseAdFromAdAct(context);
//
        // 积分够，可以运行
        if (getTotalScore() > 0) {
            return true;
        }
//
//        // 正在提示用户，不再重复提示
//        if (theIsGetScoreDialogShown) {
//            Log.d(TAG, "get score dialog is run");
//            return false;
//        }
//
//        checkNeedAdvertiseDlg(context);
        return false;
    }


    /**
     * 点击关闭按钮进入广告墙，返回后查询是否下载了东西，分数变化较多代表下载过，执行关闭广告条操作
     */
    private static void checkCloseAdFromAdAct(final Context context) {
//        //如果没向服务器查询过分数，且分数为0，则置为查询状态，直到查到分数为止，查完分数仍为0，则跳到广告界面
//        if (!mCloseAdRequset) {
//            Log.i(TAG, "checkCloseAdFromAdAct mCloseAdRequset = " + mCloseAdRequset);
//            return;
//        }
//
//        if (mCloseAdGettingScore) {
//            Log.i(TAG, "checkCloseAdFromAdAct mGettingScoreFlag = " + mGettingScoreFlag);
//            return;
//        }
//
//        Log.i(TAG, "hainotify:checkCloseAdFromAdAct mCloseAdGettingScore = " + mCloseAdGettingScore + " " +
//                "mCloseAdRequset = " + mCloseAdRequset + " mBeforeCloseScore = " + mBeforeCloseScore + " allscore = "
//                + getTotalScore());
//
//        mCloseAdGettingScore = true;
////
////        DianJinPlatform.getBalance(context, new WebServiceListener<Float>() {
////            @Override
////            public void onResponse(int responseCode, Float balance) {
////                Log.d(TAG, "hainotify:checkCloseAdFromAdAct, responseCode = " + responseCode);
////                // 返回成功
////                if (responseCode == DianJinPlatform.DIANJIN_SUCCESS) {
////                    //                       todo: wulong本地存储的只是赠送分数，游戏中产生的分数都在服务器
////                    int val = balance.intValue();
////                    mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
////                    mServerScore = val;
////
////                    PreferenceUtil.saveRecord(context, KEY_GAME_SERVER_SCORE, mServerScore);
////
////                    Log.i(TAG, "hainotify:checkCloseAdFromAdAct onSuccess get score, old total mSaveScore = " + mSaveScore + "  balance = " + balance);
////
////                    //关闭广告条按钮请求后，如果从广告墙返回时分数增加30分以上，则当做关闭广告条成功
////                    if (mCloseAdRequset) {
////                        if (mBeforeCloseScore + 30 <= getTotalScore()) {
////                            saveCloseAdBannerFunc(context);
////                            needCloseAdBanner(context);
////                        }
////                    }
////                } else {
//////                    todo: wulong:dianjin获取分数失败
////                    Log.e(TAG, "hainotify:checkCloseAdFromAdAct onError responseCode = " + responseCode);
////                    showClickAdDlg(context);
////                    mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
////                    mServerScore = PreferenceUtil.readRecord(context, KEY_GAME_SERVER_SCORE, 0);
////                }
////                mCloseAdRequset = false;
////                mCloseAdGettingScore = false;
////            }
////        });
    }

    /**
     * 从广告墙回来积分是否增加
     * 为了解决下载广告后无返回的问题
     */
    private static void checkNeedAdvertiseDlg(final Context context) {
//
//        if (mGettingScoreFlag) {
//            Log.i(TAG, "checkNeedAdvertiseDlg mGettingScoreFlag = " + mGettingScoreFlag);
//            return;
//        }
//
//        if (getTotalScore() > 0) {
//            return;
//        }
//
//        //如果没向服务器查询过分数，且分数为0，则置为查询状态，直到查到分数为止，查完分数仍为0，则跳到广告界面
//        mGettingScoreFlag = true;
////
////        DianJinPlatform.getBalance(context, new WebServiceListener<Float>() {
////            @Override
////            public void onResponse(int responseCode, Float balance) {
////                Log.d(TAG, "checkNeedAdvertiseDlg, responseCode = " + responseCode);
////                // 返回成功
////                if (responseCode == DianJinPlatform.DIANJIN_SUCCESS) {
////                    Log.d(TAG, "checkNeedAdvertiseDlg success ,balance = " + balance);
////                    //                       todo: wulong本地存储的只是赠送分数，游戏中产生的分数都在服务器
////                    int val = balance.intValue();
////                    mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
////                    mServerScore = val;
////                    PreferenceUtil.saveRecord(context, KEY_GAME_SERVER_SCORE, mServerScore);
////
////                    Log.i(TAG, "checkNeedAdvertiseDlg onSuccess get score, old total mSaveScore = " + mSaveScore + "  balance = " + balance);
////
////                    if (getTotalScore() <= 0) {
////                        showClickAdDlg(context);
////                    }
////
////                    mGettingScoreFlag = false;
////
////                    //关闭广告条按钮请求后，如果从广告墙返回时分数增加30分以上，则当做关闭广告条成功
//////                    Log.i(TAG,"checkNeedAdvertiseDlg ");
//////                    if (mCloseAdRequset) {
//////                        if (mBeforeCloseScore + 30 <= getTotalScore()) {
//////                            saveCloseAdBannerFunc(context);
//////                            needCloseAdBanner(context);
//////                        }
//////                        mCloseAdRequset = false;
//////                    }
////                } else {
//////                    todo: wulong:dianjin获取分数失败
////                    Log.e(TAG, "checkNeedAdvertiseDlg onError responseCode = " + responseCode);
////                    showClickAdDlg(context);
////                    mGettingScoreFlag = false;
////                    mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
////                    mServerScore = PreferenceUtil.readRecord(context, KEY_GAME_SERVER_SCORE, 0);
////                }
////            }
////        });
    }

    /**
     * 显示对话框，按确认键跳转到广告界面
     */
    private static void showClickAdDlg(final Context context) {
//        theIsGetScoreDialogShown = true;
//        // 积分是否不足时显示广告下载提示信息
//        Log.i(TAG, "prompt user to get score");
//        final String hintMessage = String.format(
//                "亲爱的朋友，您的游戏积分不足，需要联网下载广告后才能继续游戏。"
//        );
//        final String title = "提示";
//        final String btnDownload = "下载广告";
//
//        final AlertDialog scoreDialog = new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setTitle(title)
//                .setMessage(hintMessage)
//                .setPositiveButton(btnDownload, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(final DialogInterface dialogInterface, int i) {
//                        // 显示下载广告
//                        clickGetScore(context);
//                        //
//                        theIsGetScoreDialogShown = false;
//                        mGettingScoreFlag = true;
//                        // 关闭当前对话框
//                        dialogInterface.cancel();
//                    }
//                })
//                .create();
//        scoreDialog.show();

    }

    private static void setContext(Context context) {
        Log.i(TAG, "hainotify:setContext context = " + context);
        if (null != context) {
            mContext = context;
        }
    }

    // 点击“获取积分”
    public static void clickGetScore(final Context context) {
////todo: wulong获得积分
//        getScoreFromServer(context);
//        showWall(context);
    }

    public static void getScoreFromServer(final Context context) {
        Log.i(TAG, "getScoreFromServer");
//
//        DianJinPlatform.getBalance(context, new WebServiceListener<Float>() {
//            @Override
//            public void onResponse(int responseCode, Float balance) {
//                Log.d(TAG, "Get userPoints, responseCode = " + responseCode);
//                // 返回成功
//                if (responseCode == DianJinPlatform.DIANJIN_SUCCESS) {
//                    mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
//                    mServerScore = balance.intValue();
//                    PreferenceUtil.saveRecord(context, KEY_GAME_SERVER_SCORE, mServerScore);
//
//                    if (mDebtScore > 0) {
//                        //本地不够扣，从服务器取，服务器不同步，什么时候取到什么时候扣
//                        mServerScore -= mDebtScore;
//                        lostLevelScore(context, mDebtScore);
//                    }
//
//                    PreferenceUtil.saveRecord(context, KEY_GAME_SERVER_SCORE, mServerScore);
//
//                    Log.i(TAG, "hainotify: getScoreFromServer onSuccess mSaveScore = " + mSaveScore + "  balance = " + balance + " mDebtScore = " + mDebtScore);
//                    mGettingScoreFlag = false;
//                } else {
////                    todo: wulong:dianjin获取分数失败
//                    Log.e(TAG, "hainotify: getScoreFromServer onError responseCode = " + responseCode);
//                    mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
//                    mServerScore = PreferenceUtil.readRecord(context, KEY_GAME_SERVER_SCORE, 0);
//                }
//            }
//        });
    }

    // 获得积分数值
    public static int getTotalScore() {
//todo: wulong赠送分数玩了就不管,不实时更新，也许要点两次
        int allScore = mServerScore + mSaveScore;
        if (allScore < 1) {
            allScore = 1;
        }
        return allScore;
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
//todo: wulong:赢了point是整数，输了是负数
        if (point < 0) {
            lostLevelScore(context, (-point));
        } else {
            winLevelScore(context, point);
        }
    }

    // 本局失败
    public static void lostLevelScore(final Context context, final int point) {
//todo: wulong开始赠送100分，本地积分只扣不加，本段重写
//todo: wulong:减去积分

        if (point <= 0) {
            return;
        }

        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
        mServerScore = PreferenceUtil.readRecord(context, KEY_GAME_SERVER_SCORE, 0);

        Log.i(TAG, "lostLevelScore, mSaveScore = " + mSaveScore + " point = " + point + " mServerScore = " + mServerScore);

        int lostScore = point;

//todo: wulong:先扣本地分数，不够再扣服务器分数
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
//todo: wulong:本地不够就扣服务器
            spendScoreCommitToServer(context, lostScore);
        } else {
            mSaveScore -= lostScore;
        }

        PreferenceUtil.saveRecord(context, KEY_GAME_LOCAL_SCORE, mSaveScore);
        PreferenceUtil.saveRecord(context, KEY_GAME_SERVER_SCORE, mServerScore);
        Log.i(TAG, "lostLevelScore, new mSaveScore = " + mSaveScore + " mServerScore = " + mServerScore + " lostScore = " + lostScore);
    }

    private static void spendScoreCommitToServer(final Context context, final int score) {
        // 订单号
//        String orderSerial = TelephoneUtil.toImei(context) + TimeUtil.toLongOfyyyyMMddHHmmss();
//        DianJinPlatform.consume(context, orderSerial, score, 100, new WebServiceListener<Integer>() {
//            @Override
//            public void onResponse(int responseCode, Integer t) {
//                switch (responseCode) {
//                    case DianJinPlatform.DIANJIN_SUCCESS:
//                        Log.i(TAG, "haimoney:spendUserPoints DIANJIN_SUCCESS");
//                        Toast.makeText(context, "消费成功", Toast.LENGTH_SHORT).show();
//                        mDebtScore = 0;
//                        break;
//                    case DianJinPlatform.DIANJIN_ERROR_REQUES_CONSUNE:
//                        Log.i(TAG, "haimoney:spendUserPoints DIANJIN_ERROR_REQUES_CONSUNE");
//                        Toast.makeText(context, "支付请求失败",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:
//                        Log.i(TAG, "haimoney:spendUserPoints DIANJIN_ERROR_BALANCE_NO_ENOUGH");
//                        Toast.makeText(context, "余额不足",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case DianJinPlatform.DIANJIN_ERROR_ACCOUNT_NO_EXIST:
//                        Log.i(TAG, "haimoney:spendUserPoints DIANJIN_ERROR_ACCOUNT_NO_EXIST");
//                        Toast.makeText(context, "账号不存在",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case DianJinPlatform.DIANJIN_ERROR_ORDER_SERIAL_REPEAT:
//                        Log.i(TAG, "haimoney:spendUserPoints DIANJIN_ERROR_ORDER_SERIAL_REPEAT");
//                        Toast.makeText(context, "订单号重复",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case DianJinPlatform.DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT:
//                        Log.i(TAG, "haimoney:spendUserPoints DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT");
//                        Toast.makeText(context,
//                                "一次性交易金额超过最大限定金额",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case DianJinPlatform.DIANJIN_RETURN_CONSUME_ID_NO_EXIST:
//                        Log.i(TAG, "haimoney:spendUserPoints DIANJIN_RETURN_CONSUME_ID_NO_EXIST");
//                        Toast.makeText(context,
//                                "不存在该类型的消费动作ID", Toast.LENGTH_SHORT)
//                                .show();
//                        break;
//                    default:
//                        Log.i(TAG, "haimoney:spendUserPoints unkonwn error");
//                        Toast.makeText(
//                                context,
//                                "未知错误,错误码为："
//                                        + responseCode,
//                                Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    // 本局胜利
    public static void winLevelScore(final Context context, final int point) {
//todo: wulong获得分数都存储在本地，不用上传服务器
        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
        Log.i(TAG, "winLevelScore, old mSaveScore = " + mSaveScore + "  point = " + point + " mServerScore = " + mServerScore);

        mSaveScore += point;

        PreferenceUtil.saveRecord(context, KEY_GAME_LOCAL_SCORE, mSaveScore);
        PreferenceUtil.saveRecord(context, KEY_GAME_SERVER_SCORE, mServerScore);
        Log.i(TAG, "winLevelScore, new total saveScore = " + mSaveScore + " mServerScore = " + mServerScore);
    }

    private static int mGameStage = 0;

    public static void checkNewGame() {

//        if (null != mContext) {
//            Log.i(TAG, "hainotify: checkNewGame update mGameStage = " + mGameStage);
//            checkEverydayGiftScore(mContext);
//            checkRentScore(mContext);
//            needCloseAdBanner(mContext);
//        } else {
//            Log.i(TAG, "hainotify: checkNewGame mContext == null mGameStage = " + mGameStage);
//        }
//
//        mGameStage++;
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
