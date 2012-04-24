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
import android.util.Log;
import com.yeahyoo.SpendScoreFeedback;
import com.yeahyoo.UserActiveAdFeedback;
import df.util.ad.Yeahyoo;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.type.NumberUtil;
import df.util.type.StringUtil;

import java.util.Random;

/**
 * gameleader，神仙斗地主，yeahyoo版本 易蛙
 */
public class GameleaderSxddzYeahyoo {

    public static final String TAG = "df.util.GameleaderSxddzYeahyoo";

    /**
     * 玩家初始积分：100分
     * 输掉一盘或者赢一盘：±20~40分，每局游戏积分进行累积
     * 下载1个广告应用赠送积分：60~120分
     * 系统送积分：当用户积分=0时，系统默认每天送50分，送满了100分封顶，不再送
     */
    public static final int SCORE_INIT = 100;

//    public static final String DF_SXDDZ_INIT_SCORE      = "DF_SXDDZ_INIT_SCORE";
//    public static final String DF_SXDDZ_LEVEL_SCORE_MIN = "DF_SXDDZ_LEVEL_SCORE_MIN";
//    public static final String DF_SXDDZ_LEVEL_SCORE_MAX = "DF_SXDDZ_LEVEL_SCORE_MAX";
//    public static final int    SCORE_LEVEL_MIN          = 20;
//    public static final int    SCORE_LEVEL_MAX          = 40;
//    public static final String DF_SXDDZ_DOWN_SCORE_MIN  = "DF_SXDDZ_DOWN_SCORE_MIN";
//    public static final String DF_SXDDZ_DOWN_SCORE_MAX  = "DF_SXDDZ_DOWN_SCORE_MAX";
//    public static final int    SCORE_DOWN_MIN           = 60;
//    public static final int    SCORE_DOWN_MAX           = 120;
//    public static final String DF_SXDDZ_DAY_SCORE       = "DF_SXDDZ_DAY_SCORE";
//    public static final String DF_SXDDZ_MAX_SCORE       = "DF_SXDDZ_MAX_SCORE";
//    public static final int    SCORE_DAY                = 50;
//    public static final int    SCORE_MAX                = 100;

    // 总积分
    public static final String KEY_GAME_SCORE                    = "game.sxddz.score";
    //    // 每日赠送的积分（从最近一次赠送开始的累计积分）
//    public static final String KEY_GAME_GIVE_SCORE               = "game.sxddz.give_score";
    // 最后一次赠送积分的日期
    public static final String KEY_GAME_GIVE_DATE                = "game.sxddz.give_date";
    // 人物是否解锁标志
    public static final String KEY_GAME_AVATARINDEX_UNLOCKED_SET = "game.sxddz.unlocked_avatarindex_set";

    //
    public static boolean theIsGetScoreDialogShown = false;

    // 角色名称
    public static String[] avatarNameArray = {
            "如来", // 0
            "牛头", // 1
            "马面", // 2
            "观音", // 3
            "龟仙", // 4
            "圣母", // 5
            "耶稣", // 6
            "土地", // 7
            "阎王", // 8
            "真主", // 9
    };

    // 特效
    public static String[]           theSkillNameArray     = new String[]{
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

    public static int  theLastPaiFlag           = -1;
    public static int  theLastPaiNumber         = -1;
    public static int  theLastDrawSkillA        = -1;
    public static int  theLastDrawSkillB        = -1;
    public static int  theLastDrawSkillC        = -1;
    public static int  theLastDrawFrame         = -1;
    public static boolean theLastDrawFrameFinished = false;
    public static long theLastDrawTimeMillis    = 0;
    public static long theStartDrawTimeMillis   = 0;
    public static long theDrawSpanMinTimeMillis = 5000;

    public static final int SKILL_TIMEMILLIS_FRAME_SWITCH = 50;   // 每帧切换时间间隔
    public static final int SKILL_TIMEMILLIS_FRAME_SINGLE = 100;   // 每帧显示时间长度
    public static final int SKILL_TIMEMILLIS_FRAME_LAST   = 500;   // 最后一帧显示时间长度

    private static final Random theRandom = new Random(System.currentTimeMillis());

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
        Log.d(TAG, "click hintOfSelectAvatar");

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
            Yeahyoo.showDownList(context, new UserActiveAdFeedback() {
                @Override
                public void doAfterActive(int resultCode, int score) {
                    if (resultCode == Yeahyoo.RESULT_SUCCESS) {
                        Log.i(TAG, "unlock avatar after download ad, avatarIndex = " + avatarIndex);
                        unlockAvatar(context, avatarIndex);
                    }
                }
            });
        }
    }


    ////////////////////////////////////////////////////////////////
    // 游戏内获得积分
    ////////////////////////////////////////////////////////////////

    // 初始化积分
    public static void initScore(final Context context) {
        long giveDate = PreferenceUtil.readRecord(context, KEY_GAME_GIVE_DATE, 0L);
        if (giveDate <= 0) {
            giveDate = System.currentTimeMillis();
            PreferenceUtil.saveRecord(context, KEY_GAME_GIVE_DATE, giveDate);

            //
            int scoreInit = SCORE_INIT;
            PreferenceUtil.saveRecord(context, KEY_GAME_SCORE, scoreInit);
            Log.d(TAG, "haitest init score, score not given yet, given score = " + scoreInit);
        }
    }

    // 游戏是否能够运行？
    public static boolean canRun(final Context context) {
        // 积分够，可以运行
        if (getTotalScore(context) > 0) {
            return true;
        }

        // 正在提示用户，不再重复提示
        if (theIsGetScoreDialogShown) {
            Log.d(TAG, "get score dialog is run");
            return false;
        }

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
                        // 关闭当前对话框
                        dialogInterface.cancel();
                    }
                })
                .create();
        scoreDialog.show();
        return false;
    }

    // 点击“获取积分”
    public static void clickGetScore(final Context context) {
        Yeahyoo.showDownList(context, new UserActiveAdFeedback() {
            @Override
            public void doAfterActive(int resultCode, int score) {
                //
                int totalScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
                Log.i(TAG, "get score, old total score = " + totalScore);
//                //
//                Random r = new Random();
//                int givenScore = r.nextInt(SCORE_DOWN_MAX - SCORE_DOWN_MIN);
//                givenScore += SCORE_DOWN_MIN;
//                Log.i(TAG, "get score, give score = " + givenScore);
//                //
//                totalScore += givenScore;
//                PreferenceUtil.saveRecord(context, KEY_GAME_LOCAL_SCORE, totalScore);
//                Log.i(TAG, "get score, new total score = " + totalScore);

                // todo：由于积分为0时才会下载广告，因此将服务器的积分，直接设置为本地积分
                totalScore += score;
                PreferenceUtil.saveRecord(context, KEY_GAME_SCORE, totalScore);
                Log.i(TAG, "get score, new total score = " + totalScore);
            }
        });
    }

    // 获得积分数值
    public static int getTotalScore(final Context context) {
        return PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
    }

    // 获得积分数值字符串
    public static String getTotalScoreAsString(final Context context) {
        return String.valueOf(PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0));
    }

    // 获得“积分x”的字符串
    public static String getTotalScoreValueAndString(final Context context) {
        return getTotalScore(context) + "积分";
    }

    // 根据结果处理一局结束的逻辑
    public static void processLevelResult(final Context context, final int leftPaiCount, final int point) {
        if (leftPaiCount > 0) {
            lostLevelScore(context, point);
        } else {
            winLevelScore(context, point);
        }
    }

    // 本局失败
    public static void lostLevelScore(final Context context, final int point) {
        //
        int totalScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
        Log.i(TAG, "level lost, old total score = " + totalScore);
        //
//        Random r = new Random();
//        int lostScore = r.nextInt(SCORE_LEVEL_MAX - SCORE_LEVEL_MIN);
//        lostScore += SCORE_LEVEL_MIN;
        // todo: 点数是负的
        int lostScore = -point;
        Log.i(TAG, "level lost, lost score = " + lostScore);
        //
        totalScore -= lostScore;
        if (totalScore < 0) {
            totalScore = 0;
        }
        PreferenceUtil.saveRecord(context, KEY_GAME_SCORE, totalScore);
        Log.i(TAG, "level lost, new total score = " + totalScore);

        // todo: 调用广告积分消费接口，减去积分
        Yeahyoo.spendScoreFromServer(context, lostScore, new SpendScoreFeedback() {
            @Override
            public void doAfterSpendScore(int i, int i1) {
                /**
                 * resultNo : 1 成功； 2 失败； 3 积分不够消费；
                 * score ：当前该用户最新的积分
                 */
                Log.d(TAG, "level lost, spend score from server");
            }
        });

    }

    // 本局胜利
    public static void winLevelScore(final Context context, final int point) {
        //
        int totalScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
        Log.i(TAG, "level win, old total score = " + totalScore);
        //
//        Random r = new Random();
//        int winScore = r.nextInt(SCORE_LEVEL_MAX - SCORE_LEVEL_MIN);
//        winScore += SCORE_LEVEL_MIN;
        int winScore = point;
        Log.i(TAG, "level win, win score = " + winScore);
        //
        totalScore += winScore;
        PreferenceUtil.saveRecord(context, KEY_GAME_SCORE, totalScore);
        Log.i(TAG, "level win, new total score = " + totalScore);
    }

    // todo: 20120206: xiaona: 要求去掉
//    // 每日赠送积分
//    public static void initScore(final Context context) {
//
//        final int dayScore = SCORE_DAY;
//        final int oldScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
//        long giveDate = PreferenceUtil.readRecord(context, KEY_GAME_GIVE_DATE, 0L);
//        long now = TimeUtil.toLongOfyyyyMMdd();
//
//        // 如果积分为0，则开始赠送积分
//        if (oldScore == 0) {
//            int giveScore = dayScore;
//            int newScore = oldScore + giveScore;
//            giveDate = now;
//            PreferenceUtil.saveRecord(context, KEY_GAME_GIVE_DATE, giveDate);
//            PreferenceUtil.saveRecord(context, KEY_GAME_GIVE_SCORE, giveScore);
//            PreferenceUtil.saveRecord(context, KEY_GAME_LOCAL_SCORE, newScore);
//            Log.i(TAG, "old total score is 0, start day give score"
//                    + ", day give score = " + dayScore
//                    + ", total give score = " + giveScore
//                    + ", old total score = " + oldScore
//                    + ", new total score = " + newScore
//                    + ", give date = " + giveDate);
//        }
//        // 如果积分不为0，则判断是否赠送完毕，没有赠送完毕时，继续赠送
//        else {
//            int giveScore = PreferenceUtil.readRecord(context, KEY_GAME_GIVE_SCORE, SCORE_MAX);
//            if ((giveScore < SCORE_MAX) && (giveDate != now)) {
//                giveScore += dayScore;
//                int newScore = oldScore + dayScore;
//                giveDate = now;
//                PreferenceUtil.saveRecord(context, KEY_GAME_GIVE_DATE, giveDate);
//                PreferenceUtil.saveRecord(context, KEY_GAME_GIVE_SCORE, giveScore);
//                PreferenceUtil.saveRecord(context, KEY_GAME_LOCAL_SCORE, newScore);
//                Log.i(TAG, "give score not reach max, continue day give score"
//                        + ", day give score = " + dayScore
//                        + ", total give score = " + giveScore
//                        + ", old total score = " + oldScore
//                        + ", new total score = " + newScore
//                        + ", give date = " + giveDate);
//            } else {
//                Log.i(TAG, "give score reach max, stop day give score"
//                        + ", total give score = " + giveScore
//                        + ", give date = " + giveDate);
//            }
//        }
//
//    }


    ////////////////////////////////////////////////////////////////
    // 动作特效
    ////////////////////////////////////////////////////////////////

    // 新开一局初始化（传送2个角色索引过来）
    public static void newLevel(Context context) {
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
                        ", skillACount = " + skillACount+ ", skillBCount = " + skillBCount+ ", skillCCount = " + skillCCount);

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
