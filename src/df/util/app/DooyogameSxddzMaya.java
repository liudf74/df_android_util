package df.util.app;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-13
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.android.TextUtil;
import df.util.type.NumberUtil;
import df.util.type.StringUtil;

import java.util.Random;

/**
 * dooyogame，神仙斗地主，maya版本
 */
public class DooyogameSxddzMaya {

    public static final String TAG = "df.util.DooyogameSxddzMaya";

//    /**
//     * 玩家初始积分：100分
//     * 输掉一盘或者赢一盘：±20~40分，每局游戏积分进行累积
//     * 下载1个广告应用赠送积分：60~120分
//     * 系统送积分：当用户积分=0时，系统默认每天送50分，送满了100分封顶，不再送
//     */
//    public static final String DF_SXDDZ_INIT_SCORE      = "DF_SXDDZ_INIT_SCORE";
//    public static final int    SCORE_INIT               = 100;
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
//
    // 总积分
    public static final String KEY_GAME_SCORE                    = "game.sxddz.score";
//    // 每日赠送的积分（从最近一次赠送开始的累计积分）
//    public static final String KEY_GAME_GIVE_SCORE               = "game.sxddz.give_score";
//    // 最后一次赠送积分的日期
//    public static final String KEY_GAME_GIVE_DATE                = "game.sxddz.give_date";
//    // 人物是否解锁标志
//    public static final String KEY_GAME_AVATARINDEX_UNLOCKED_SET = "game.sxddz.unlocked_avatarindex_set";
//
//    //
//    public static boolean theIsGetScoreDialogShown = false;

    //
    public static       String[]           theAvatarNameArray                = {
            "圣武者", // 0
            "斗士", // 1
            "猎人", // 2
            "圣徒", // 3
            "先知", // 4
            "幽灵武士", // 5
            "元素使", // 6
            "咒术师", // 7
            "食人魔", // 8
            "半灵犬", // 9
    };
    public static       String[]           theAvatarInfoArray                = {
            "圣武者\n" +
                    "那些饱经战争苦难并从战争中醒悟过来的战士们，他们放弃了进攻的斗志，开始奉行神的旨意，" +
                    "誓要以手中的剑与盾来守护自己的家园、守护自己的所爱与最初的信仰。\n" +
                    "单手武器：七闪\n" +
                    "圣武者专用武器剑类\n" +
                    "单手盾牌：龙魂\n" +
                    "单攻技能：圣击\n" +
                    "技能说明：以神圣之力来攻击敌方\n" +
                    "群体技能：振奋\n" +
                    "技能说明：短时间内提升所有友方佣兵的攻击力\n",
            "斗士\n" +
                    "那些使用长矛的勇士，已经完全抛弃了长弓，他们醉心于矛技，将一根长矛舞得出神入化，" +
                    "虽然只以片布遮体，但其在战场上矛扫一大片，无人能敌其一击。\n" +
                    "双手装备：断魂\n" +
                    "斗士专用矛类装备\n" +
                    "单体技能：蛇咬\n" +
                    "技能说明：矛舞如蛟蛇，对单个敌人进行疯狂攻击，能使目标短时间内陷入出血状态，有机率无视盾牌防御\n" +
                    "群体技能：荡击\n" +
                    "技能说明：抖转长矛，挑起枪花，给敌潇洒一击，装备利器时才可以使用，有机率无视盾牌防御\n",
            "猎人\n" +
                    "完全掌握了弓箭之道的勇士，他们出没于丛林之间，弯弓搭箭，如鬼魅一般给敌以致命伤害。\n" +
                    "双手武器：追魂\n" +
                    "猎人专用弓类武器\n" +
                    "单体技能：连环箭\n" +
                    "技能说明：瞄准目标三箭连射，威力惊人\n" +
                    "群体技能：爆裂箭\n" +
                    "技能说明：箭头会爆炸的箭术，可攻击到大范围的敌人\n",
            "圣徒\n" +
                    "得到造物神垂青的牧师，他们掌握了光明的力量，除了更加精通于治愈之术外，还能有效地给暗黑生物以致命打击。\n" +
                    "单手武器：神罚\n" +
                    "圣徒用仗类武器\n" +
                    "单手法宝：冰魄晶心\n" +
                    "单体技能：急救术\n" +
                    "技能说明：立即恢复目标大量HP\n" +
                    "单体技能：神之眷顾\n" +
                    "技能说明：在此状态下，人物死亡后可自动复活一次，并恢复部分HP与MP\n",
            "先知\n" +
                    "从造物神处获得神秘力量的牧师们，能够预言世事，并跟虔心追随者以神圣祝福 。\n" +
                    "单手武器：圣言\n" +
                    "法杖类武器\n" +
                    "单手法宝：冰魄晶心\n" +
                    "单体技能：魔力燃烧\n" +
                    "技能说明：燃烧目标的MP值\n" +
                    "群体技能：神佑\n" +
                    "技能说明：短时间內增加目标HP、MP最大值\n",
            "幽灵武士\n" +
                    "那些饱经战争洗礼并迷失其中的战士们，死神成为了他们的信仰，左手以盾来护卫自己，" +
                    "右手用锤来敲碎敌人的头盖骨，这便是他们的乐趣所在。\n" +
                    "单手武器：冥王\n" +
                    "幽灵武士专用武器锤类\n" +
                    "单手盾牌：彩虹.域之盾\n" +
                    "单体技能：冥舞\n" +
                    "技能说明：幽冥之舞，以暗黑之力攻击敌人\n" +
                    "群体技能：尸毒\n" +
                    "技能说明：驱逐尸体上的毒疫，在短时间內让尸体周围的敌群陷入中毒狀態\n",
            "元素使\n" +
                    "从魔法书中钻研出世间真理的巫师们，掌控着风、地、水、火、暗黑与神圣一切元素，" +
                    "以神秘的法术呼唤各种元素的精灵来制造毁灭性的破坏。\n" +
                    "单手武器：末日\n" +
                    "元素使专用书类武器\n" +
                    "单手法宝：庇护.神之晶\n" +
                    "单体技能：风灵弹\n" +
                    "技能说明：凝聚风元素攻击敌人\n" +
                    "群体技能：冰封\n" +
                    "技能说明：将空气中的所有水元素冻结，使群敌陷入延迟状态无法动弹\n",
            "咒术师\n" +
                    "从水晶球中窥视到死亡力量的巫师们，利用这种邪恶的力量能从尸体中吸取能量，" +
                    "能从暗黑中召唤各种功能的晶体来协助自己，给敌造成多种负面状态。\n" +
                    "单手武器：囚神\n" +
                    "咒术师专用水晶球类武器\n" +
                    "单手法宝：彩虹.神之心\n" +
                    "单体技能：石化术\n" +
                    "技能介绍：短时间内使敌人身体石化，使其不可行动，亦不会受到攻击\n" +
                    "单体技能：束缚术\n" +
                    "技能介绍：短时间内使敌人无法使用物理攻击技能\n",
            "食人魔\n" +
                    "部族位于南面巨魔之地，好杀戮，力量强大。少数食人魔臣服于玛雅部落中，与人类勇士们一起并肩作战\n",
            "半灵犬\n" +
                    "玛雅人的忠实好友，性格暴躁，具有持久的战斗力\n",
    };
    public static       TextUtil[]         theAvatarInfoTextUtilArray        = new TextUtil[theAvatarInfoArray.length];
    // 单体技能
    public static       String[]           theAvatarSkillSingleNameArray     = new String[]{
            "sxddzmaya_skill_shengji_5x3",      // "单攻技能：圣击\n技能说明：以神圣之力来攻击敌方\n"
            "sxddzmaya_skill_sheyao_4x3",       // "单体技能：蛇咬\n技能说明：矛舞如蛟蛇，对单个敌人进行疯狂攻击，能使目标短时间内陷入出血状态，有机率无视盾牌防御\n"
            "sxddzmaya_skill_lianhuanjian_4x3", // "单体技能：连环箭\n技能说明：瞄准目标三箭连射，威力惊人\n"
            "sxddzmaya_skill_jijiushu_4x2",     // "单体技能：急救术\n技能说明：立即恢复目标大量HP\n"
            "sxddzmaya_skill_moliranshao_3x2",  // "单体技能：魔力燃烧\n技能说明：燃烧目标的MP值\n"
            "sxddzmaya_skill_mingwu_4x2",       // "单体技能：冥舞\n技能说明：幽冥之舞，以暗黑之力攻击敌人\n"
            "sxddzmaya_skill_fenglingdan_4x3",  // "单体技能：风灵弹\n技能说明：凝聚风元素攻击敌人\n"
            "sxddzmaya_skill_shihuashu_3x2",    // "单体技能：石化术\n技能介绍：短时间内使敌人身体石化，使其不可行动，亦不会受到攻击\n"
            "",
            "",
    };
    public static       String[]           theAvatarSkillSingleInfoArray     = new String[]{
            "圣武者单体技能：以神圣之力来攻击敌方",
            "斗士单体技能：矛舞如蛟蛇，攻击单个敌人使其短时间内出血，有机率无视盾牌防御",
            "猎人单体技能：瞄准目标三箭连射，威力惊人",
            "圣徒单体技能：立即恢复目标大量HP",
            "先知单体技能：燃烧目标的MP值",
            "幽灵武士单体技能：幽冥之舞，以暗黑之力攻击敌人",
            "元素使单体技能：凝聚风元素攻击敌人",
            "咒术师单体技能：短时间内使敌人身体石化，使其不可行动，亦不会受到攻击",
            "",
            "",
    };
    public static       BitmapDrawable[][] theAvatarSkillSingleDrawableArray = new BitmapDrawable[theAvatarSkillSingleNameArray.length][];
    // 群体技能
    public static       String[]           theAvatarSkillMultiNameArray      = new String[]{
            "sxddzmaya_skill_zhenfen_3x2",      // "群体技能：振奋\n技能说明：短时间内提升所有友方佣兵的攻击力\n"
            "sxddzmaya_skill_dangji_3x2",       // "群体攻击：荡击\n技能说明：抖转长矛，挑起枪花，给敌潇洒一击，装备利器时才可以使用，有机率无视盾牌防御\n"
            "sxddzmaya_skill_paoliejian_4x2",   // "群体技能：爆裂箭\n技能说明：箭头会爆炸的箭术，可攻击到大范围的敌人\n"
            "sxddzmaya_skill_shenzhijuangu_4x2",// "单体技能：神之眷顾\n技能说明：在此状态下，人物死亡后可自动复活一次，并恢复部分HP与MP\n"
            "sxddzmaya_skill_shenyou_4x2",      // "群体技能：神佑\n技能说明：短时间內增加目标HP、MP最大值\n"
            "sxddzmaya_skill_shidu_4x2",        // "群体技能：尸毒\n技能说明：驱逐尸体上的毒疫，在短时间內让尸体周围的敌群陷入中毒狀態\n"
            "sxddzmaya_skill_bingfeng_4x2",     // "群体技能：冰封\n技能说明：将空气中的所有水元素冻结，使群敌陷入延迟状态无法动弹\n"
            "sxddzmaya_skill_shufushu_4x2",     // "单体技能：束缚术\n技能介绍：短时间内使敌人无法使用物理攻击技能\n"
            "",
            "",
    };
    public static       String[]           theAvatarSkillMultiInfoArray      = new String[]{
            "圣武者群体技能：短时间内提升所有友方佣兵的攻击力",
            "斗士群体技能：抖转长矛，挑起枪花，给敌潇洒一击，有机率无视盾牌防御",
            "猎人群体技能：箭头会爆炸的箭术，可攻击到大范围的敌人",
            "圣徒单体技能：此状态下，人物死亡后可自动复活一次，并恢复部分HP与MP",
            "先知群体技能：短时间內增加目标HP、MP最大值",
            "幽灵武士群体技能：驱逐尸体上的毒疫，短时间內让尸体周围的敌群中毒",
            "元素使群体技能：冻结空气中的所有水元素，使群敌陷入延迟状态无法动弹",
            "咒术师群体技能：短时间内使敌人无法使用物理攻击技能",
            "",
            "",
    };
    public static       BitmapDrawable[][] theAvatarSkillMultiDrawableArray  = new BitmapDrawable[theAvatarSkillMultiNameArray.length][];
    // 武器
    public static final String             theAvatarWeaponName               = "sxddzmaya_weapon_10x1";
    public static       BitmapDrawable[][] theAvatarWeaponDrawableArray      = new BitmapDrawable[theAvatarNameArray.length][];
    // 声音
    public static final String             theAvatarVoiceSingleName          = "sxddzmaya_voice_skillsingle_4x10";
    public static       BitmapDrawable[][] theAvatarVoiceSingleDrawableArray = new BitmapDrawable[theAvatarNameArray.length][];
    public static final String             theAvatarVoiceMultiName           = "sxddzmaya_voice_skillmulti_4x10";
    public static       BitmapDrawable[][] theAvatarVoiceMultiDrawableArray  = new BitmapDrawable[theAvatarNameArray.length][];

    // 当前一局的对战角色索引
    public static int theAvatarIndexLast = -1;
    public static int theAvatarIndexNext = -1;

    enum InitDrawType {
        ALL,    // 所有单元格
        COL_OF_THE_AVATAR_INDEX,    // 只初始化列，列取值参见theAvatarIndexLast和theAvatarIndexNext
        ROW_OF_THE_AVATAR_INDEX,    // 只初始化行，行取值参见theAvatarIndexLast和theAvatarIndexNext
    }

    // 是否在游戏界面中点击了角色的头像
    public static boolean theIsClickGameAvatarLast = false;
    public static boolean theIsClickGameAvatarNext = false;

    public static final String[] theTipOfSelectUpPlayerArray   = new String[]{
            "请选择您的上家",
            "玛雅勇士接受任何挑战",
            "伟大的羽蛇神与吾同在！",
    };
    public static final String[] theTipOfSelectDownPlayerArray = new String[]{
            "请选择您的下家",
            "玛雅勇士将战斗到底",
            "荣耀属于伟大的羽蛇神！",
    };

    public static final Paint thePaint = new Paint();

    public static int theLastSelectAvatarIndex = -1;  // 最近一次选择的角色索引

    public static int  theLastGameBackgroundId     = 0;
    public static long theLastGameBackgroundMillis = 0;

    public static int  theLastDrawAvatarIndex   = -1;     // 最近一次绘制的角色索引
    public static int  theLastDrawFrame         = -1;
    public static long theLastDrawTimeMillis    = 0;
    public static long theStartDrawTimeMillis   = 0;
    public static long theDrawSpanMinTimeMillis = 3000;
    public static int  theLastDrawVoiceFrame    = -1;
    public static int  theLastDrawVoiceLeft     = -1;
    public static int  theLastDrawVoiceTop      = -1;

    public static final int SKILL_TIMEMILLIS_FRAME_SWITCH = 50;   // 每帧切换时间间隔
    public static final int SKILL_TIMEMILLIS_FRAME_SINGLE = 150;   // 每帧显示时间长度
    public static final int SKILL_TIMEMILLIS_FRAME_LAST   = 500;   // 最后一帧显示时间长度

    public static final int COORD_AVATARINFO_X   = 20;
    public static final int COORD_AVATARINFO_Y   = 250;
    public static final int COORD_AVATARINFO_W   = 430;
    public static final int COORD_AVATARINFO_H   = 60;
    public static final int TEXT_SIZE_AVATARINFO = 16;
    public static final int COORD_TIP_X          = 120;
    public static final int COORD_TIP_Y          = 26;
    public static final int TEXT_SIZE_TIP        = 14;
    public static final int TEXT_DIST_TIP        = 20;

    private static final Random theRandom = new Random(System.currentTimeMillis());

    private static int toHeightFrom480320(int screenH, int yOf480320) {
        return yOf480320 * screenH / 320;
    }

    private static int toWidthFrom480320(int screenW, int xOf480320) {
        return xOf480320 * screenW / 480;
    }

    // 返回一个背景图片
    public static Drawable toGameBackgroundDrawable(Context context) {
        long currMillis = System.currentTimeMillis();
        if (theLastGameBackgroundMillis == 0) {
            theLastGameBackgroundMillis = currMillis;
        }
        // 第一次
        if (theLastGameBackgroundId == 0) {
            theLastGameBackgroundId = getRandomGameBackgroundId(context);
        }
        // 隔30秒换一次
        else if (currMillis - theLastGameBackgroundMillis > 30 * 1000) {
            theLastGameBackgroundId = getRandomGameBackgroundId(context);
            theLastGameBackgroundMillis = currMillis;
        }
        return context.getResources().getDrawable(theLastGameBackgroundId);
    }

    private static int getRandomGameBackgroundId(Context context) {
        int x = theRandom.nextInt(4);
        int y = theRandom.nextInt(2);
        String key = "gbg_" + x + y;
        return ResourceUtil.getDrawableResourceIdFromName(context, key);
    }

    // 人物的名称
    public static String toNameOfAvatar(int avatarIndex) {
        if ((avatarIndex < 0) || (avatarIndex > 9)) {
            Log.d(TAG, "toNameOfAvatar, avatarIndex illegal, avatarIndex = " + avatarIndex);
            return "未知";
        }
        return theAvatarNameArray[avatarIndex];
    }

    // 人物选择界面，左上角的提示信息
    public static void drawHintOfSelectUpPlayer(Canvas canvas, int screenW, int screenH) {
        drawHintOfSelect(canvas, screenW, screenH, theTipOfSelectUpPlayerArray);
    }

    public static void drawHintOfSelectDownPlayer(Canvas canvas, int screenW, int screenH) {
        drawHintOfSelect(canvas, screenW, screenH, theTipOfSelectDownPlayerArray);
    }

    private static void drawHintOfSelect(Canvas canvas, int screenW, int screenH, String[] hintArray) {
        int x = toWidthFrom480320(screenW, COORD_TIP_X);
        int y = toHeightFrom480320(screenH, COORD_TIP_Y);
        int s = toHeightFrom480320(screenH, TEXT_SIZE_TIP);
        int d = toHeightFrom480320(screenH, TEXT_DIST_TIP);

        thePaint.setTextSize(s);
        thePaint.setARGB(255, 255, 255, 255);

        for (int i = 0; i < hintArray.length; i++) {
            String hint = hintArray[i];
            int tipX = x;
            int tipY = y + i * d;
            canvas.drawText(hint, tipX, tipY, thePaint);
        }
    }

    // 选人物界面，下方的人物描述信息
    public static void drawHintOfAvatarChangePage(Canvas canvas, int screenW, int screenH, int avatarIndex,
                                                  boolean isChangePage) {
        if ((avatarIndex < 0) || (avatarIndex > 9)) {
            Log.d(TAG, "drawHintOfAvatarChangePage, avatarIndex illegal, avatarIndex = " + avatarIndex);
            return;
        }

        // 初始化
        TextUtil textUtil = theAvatarInfoTextUtilArray[avatarIndex];
        if (null == textUtil) {
            for (int i = 0; i < theAvatarInfoTextUtilArray.length; i++) {
                String info = theAvatarInfoArray[i];
                TextUtil tu = new TextUtil(info,
                        toWidthFrom480320(screenW, COORD_AVATARINFO_X),
                        toHeightFrom480320(screenH, COORD_AVATARINFO_Y),
                        toWidthFrom480320(screenW, COORD_AVATARINFO_W),
                        toHeightFrom480320(screenH, COORD_AVATARINFO_H),
                        0x00B4FF, 255,      // 淡蓝色
                        toHeightFrom480320(screenH, TEXT_SIZE_AVATARINFO)
                );
                tu.initText();
                theAvatarInfoTextUtilArray[i] = tu;
            }
            textUtil = theAvatarInfoTextUtilArray[avatarIndex];
        }

        // 如果切换了avatar，则重新开始显示
        if (theLastSelectAvatarIndex != avatarIndex) {
            textUtil.gotoFirstPage();
            theLastSelectAvatarIndex = avatarIndex;
        } else {
            // 翻页并显示
            if (isChangePage) {
                textUtil.loopPageForward();
            }
        }
        textUtil.drawText(canvas);
    }

    public static void drawHintOfAvatar(Canvas canvas, int screenW, int screenH, int avatarIndex) {
        drawHintOfAvatarChangePage(canvas, screenW, screenH, avatarIndex, true);
    }


    // 新开一局初始化（传送2个角色索引过来）
    public static void newLevel(Context context, int avatarIndexLast, int avatarIndexNext) {

        Log.d(TAG, "newLevel, avatarIndexLast = " + avatarIndexLast + ", avatarIndexNext = " + avatarIndexNext);
        theAvatarIndexLast = avatarIndexLast;
        theAvatarIndexNext = avatarIndexNext;

        // 回收图片
        recycleBitmap();

        // 初始化图片
        int[] avatarIndexArray = new int[]{avatarIndexLast, avatarIndexNext};
        for (int i = 0; i < avatarIndexArray.length; i++) {
            int avatarIndex = avatarIndexArray[i];

            // 单体技能图片
            BitmapDrawable[] skillSingleDrawableArray = findSkillDrawableArray(
                    context, avatarIndex, theAvatarSkillSingleNameArray, theAvatarSkillSingleDrawableArray);
            assert (skillSingleDrawableArray != null);
            // 群体技能图片
            BitmapDrawable[] skillMultiDrawableArray = findSkillDrawableArray(
                    context, avatarIndex, theAvatarSkillMultiNameArray, theAvatarSkillMultiDrawableArray);
            assert (skillMultiDrawableArray != null);
            // 武器图片
            BitmapDrawable[] weaponDrawableArray = findWeaponDrawableArray(context, avatarIndex);
            assert (weaponDrawableArray != null);
            // 单体声音图片
            BitmapDrawable[] voiceSingleDrawableArray = findVoiceDrawableArray(
                    context, avatarIndex, theAvatarVoiceSingleName, theAvatarVoiceSingleDrawableArray);
            assert (voiceSingleDrawableArray != null);
            // 群体声音图片
            BitmapDrawable[] voiceMultiDrawableArray = findVoiceDrawableArray(
                    context, avatarIndex, theAvatarVoiceMultiName, theAvatarVoiceMultiDrawableArray);
            assert (voiceMultiDrawableArray != null);
        }
    }

    // 游戏界面，绘制技能特效
    public static void drawSkillOfAvatar(Context context, Canvas canvas, int screenW, int screenH, int avatarIndex,
                                         int paiNumber, int paiFlag) {
        // 特定牌类型时才显示特效：1单, 2对, 3三, 4三带1, 5三带2, 6炸弹, 7王炸, 8顺子, 9顺子2, a顺子3, b三顺1, c三顺2, d四带2
        // 出牌数大于5时显示特效
//        if (((paiFlag == 1) || (paiFlag == 2)) || (paiNumber < 6)) {
//            return;
//        }
        int skillCount = 1;
        if ((9 == paiFlag) || (0xc == paiFlag) || (0xd == paiFlag)) {
            skillCount = 1;
        } else if ((0xa == paiFlag) || (0xb == paiFlag)) {
            skillCount = 2;
        } else if ((6 == paiFlag)) {
            skillCount = 3;
        } else if ((7 == paiFlag)) {
            skillCount = 5;
        } else if ((8 == paiFlag)) {
            skillCount = 1 + (paiNumber - 5) / 2;
        } else if ((paiNumber < 6)) {
//            Log.d(TAG, "paiNumber is to small, paiFlag = " + paiFlag + ", paiNumber = " + paiNumber);
            return;
        }

        if ((avatarIndex < 0) || (avatarIndex > 9)) {
            Log.d(TAG, "drawSkillOfAvatar, avatarIndex illegal, avatarIndex = " + avatarIndex);
            return;
        }
        // 防止不再显示另一方角色的特效
        if ((StringUtil.empty(theAvatarSkillSingleNameArray[avatarIndex]))
                || (StringUtil.empty(theAvatarSkillMultiNameArray[avatarIndex]))) {
            theLastDrawAvatarIndex = avatarIndex;
            Log.d(TAG, "drawSkillOfAvatar, skill name is empty, avatarIndex = " + avatarIndex);
            return;
        }

        // 如果切换了角色，则清除旧角色动画，减少内存占用
        recycleBitmap(avatarIndex);

        // 单体技能图片
        BitmapDrawable[] skillSingleDrawableArray = findSkillDrawableArray(
                context, avatarIndex, theAvatarSkillSingleNameArray, theAvatarSkillSingleDrawableArray);
        assert (skillSingleDrawableArray != null);
        // 单体技能描述
        String skillSingleInfo = theAvatarSkillSingleInfoArray[avatarIndex];
        assert (!StringUtil.empty(skillSingleInfo));
        // 群体技能图片
        BitmapDrawable[] skillMultiDrawableArray = findSkillDrawableArray(
                context, avatarIndex, theAvatarSkillMultiNameArray, theAvatarSkillMultiDrawableArray);
        assert (skillMultiDrawableArray != null);
        // 群体技能描述
        String skillMultiInfo = theAvatarSkillMultiInfoArray[avatarIndex];
        assert (!StringUtil.empty(skillMultiInfo));
        // 武器图片
        BitmapDrawable[] weaponDrawableArray = findWeaponDrawableArray(context, avatarIndex);
        assert (weaponDrawableArray != null);
        // 单体声音图片
        BitmapDrawable[] voiceSingleDrawableArray = findVoiceDrawableArray(
                context, avatarIndex, theAvatarVoiceSingleName, theAvatarVoiceSingleDrawableArray);
        assert (voiceSingleDrawableArray != null);
        // 群体声音图片
        BitmapDrawable[] voiceMultiDrawableArray = findVoiceDrawableArray(
                context, avatarIndex, theAvatarVoiceMultiName, theAvatarVoiceMultiDrawableArray);
        assert (voiceMultiDrawableArray != null);

        // 如果切换了角色，则重新绘制
        if (theLastDrawAvatarIndex != avatarIndex) {
            theLastDrawAvatarIndex = avatarIndex;
            theLastDrawTimeMillis = 0;
            theStartDrawTimeMillis = 0;
            theLastDrawFrame = -1;
            theLastDrawVoiceFrame = -1;
            theLastDrawVoiceLeft = -1;
            theLastDrawVoiceTop = -1;
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
                    && (theLastDrawFrame == 0)                                // 绘制了最后一帧
                    ) {
                isDraw = false;
            }

            if (isDraw) {
                // 牌数多则显示群体技能
                boolean isSkillSingle = (paiNumber < 6);
                String skillInfo = (isSkillSingle ? skillSingleInfo : skillMultiInfo);
                BitmapDrawable[] skillDrawableArray = (isSkillSingle ? skillSingleDrawableArray : skillMultiDrawableArray);
                BitmapDrawable[] voiceDrawableArray = (isSkillSingle ? voiceSingleDrawableArray : voiceMultiDrawableArray);

//                // 牌数有重复事，显示多点特效: 1单, 2对, 3三, 4三带1, 5三带2, 6炸弹, 7王炸, 8顺子, 9顺子2, a顺子3, b三顺1, c三顺2, d四带2
//                int skillCount = 1;
//                if ((5 == paiFlag) || (9 == paiFlag) || (0xc == paiFlag) || (0xd == paiFlag)) {
//                    skillCount = 2;
//                    Log.d(TAG, "paiFlag = " + paiFlag + ", skillCount = " + skillCount);
//                } else if ((0xa == paiFlag) || (0xb == paiFlag)) {
//                    skillCount = 3;
//                    Log.d(TAG, "paiFlag = " + paiFlag + ", skillCount = " + skillCount);
//                } else if ((6 == paiFlag)) {
//                    skillCount = 4;
//                    Log.d(TAG, "paiFlag = " + paiFlag + ", skillCount = " + skillCount);
//                } else if ((7 == paiFlag)) {
//                    skillCount = 5;
//                    Log.d(TAG, "paiFlag = " + paiFlag + ", skillCount = " + skillCount);
//                } else if ((8 == paiFlag)) {
//                    skillCount = 1 + (paiNumber - 5) / 2;
//                    Log.d(TAG, "paiFlag = " + paiFlag + ", skillCount = " + skillCount);
//                }

                // 反复绘制所有帧
                theLastDrawFrame++;
                if (theLastDrawFrame >= skillDrawableArray.length) {
                    theLastDrawFrame = 0;
                }
                Log.d(TAG, "theLastDrawFrame = " + theLastDrawFrame);

                // 判断当前角色是左边还是右边的角色
                boolean isLeftSide = (avatarIndex == theAvatarIndexLast);

                // 绘制特效
                drawSkillOfAvatarOfInfo(canvas, screenW, screenH, skillInfo, isLeftSide);
                drawSkillOfAvatarOfWeapon(canvas, screenW, screenH, weaponDrawableArray[0], isLeftSide);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillDrawableArray, theLastDrawFrame, skillCount);
                drawSkillOfAvatarOfVoice(canvas, screenW, screenH, voiceDrawableArray, now, isLeftSide);
            }
        }
    }

    private static void drawSkillOfAvatarOfInfo(Canvas canvas, int screenW, int screenH,
                                                String skillInfo,
                                                boolean isLeftSide) {
        // 绘制背景
        thePaint.setARGB(0xdd, 0, 0, 0);
        canvas.drawRect(
                toWidthFrom480320(screenW, 0),
                toHeightFrom480320(screenH, 240),
                toWidthFrom480320(screenW, 480),
                toHeightFrom480320(screenH, 320),
                thePaint
        );
        // 绘制描述
        int left = (isLeftSide) ? 120 : 60;
        TextUtil textUtil = new TextUtil(skillInfo,
                toWidthFrom480320(screenW, left),
                toHeightFrom480320(screenH, 270),
                toWidthFrom480320(screenW, 300),        // w
                toHeightFrom480320(screenH, 30),        // h
                0x00A4FF, 255,      // 淡蓝色
                toHeightFrom480320(screenH, 16)
        );
        textUtil.initText();
        textUtil.drawText(canvas);
    }

    private static void drawSkillOfAvatarOfWeapon(Canvas canvas, int screenW, int screenH,
                                                  BitmapDrawable bitmapDrawable,
                                                  boolean isLeftSide) {
        // 绘制武器
        Drawable weaponDrawable = bitmapDrawable;
        int left = (isLeftSide) ? 20 : 360;
        int right = left + 100;
        weaponDrawable.setBounds(
                toWidthFrom480320(screenW, left),
                toHeightFrom480320(screenH, 200),
                toWidthFrom480320(screenW, right),
                toHeightFrom480320(screenH, 320)
        );
        weaponDrawable.draw(canvas);
    }

    private static void drawSkillOfAvatarOfSkill(Canvas canvas, int screenW, int screenH,
                                                 BitmapDrawable[] skillSingleDrawableArray,
                                                 final int frame,
                                                 int skillCount) {
        for (int i = 0; i < skillCount; i++) {
            // 对于多点显示时，设置不同的落点
            int startLeft = 0;
            int startTop = 0;
            if (i > 0) {
                int offset = theRandom.nextInt(120);
                startLeft += offset;
                startTop += offset;
            }

            // 绘制技能
            Drawable frameDrawable = skillSingleDrawableArray[frame];
            final int left = (int) (screenW * 0.25) + startLeft;
            final int top = (int) (screenH * 0.25) + startTop;
            final int right = (int) (screenW * 0.75) + startLeft;
            final int bottom = (int) (screenH * 0.75) + startTop;
            frameDrawable.setBounds(left, top, right, bottom);
            frameDrawable.draw(canvas);
        }

        // 延迟一会，显示本帧特效
        try {
            int sleep = (frame == skillSingleDrawableArray.length - 1) ? SKILL_TIMEMILLIS_FRAME_LAST : SKILL_TIMEMILLIS_FRAME_SINGLE;
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void drawSkillOfAvatarOfVoice(Canvas canvas, int screenW, int screenH,
                                                 BitmapDrawable[] voiceDrawableArray, long now,
                                                 boolean isLeftSide) {
        // 每帧图片之间采用固定时间间隔
        long voiceFrameTimeMillis = theDrawSpanMinTimeMillis / 4;
        int voiceFrame = (int) ((now - theStartDrawTimeMillis) / voiceFrameTimeMillis);
        final int frameCount = voiceDrawableArray.length;
        if (voiceFrame >= frameCount) {
            voiceFrame = frameCount - 1;
        }
        Drawable voiceDrawable = voiceDrawableArray[voiceFrame];
        //
        final int width = 64 * (voiceFrame + 1);
        final int height = 32 * (voiceFrame + 1);
        //
        int left, top;
        if (voiceFrame == theLastDrawVoiceFrame) {
            left = theLastDrawVoiceLeft;
            top = theLastDrawVoiceTop;
        } else {
            theLastDrawVoiceFrame = voiceFrame;
            //
            if (isLeftSide) {
                left = theRandom.nextInt(160);
            } else {
                left = 480 - (theRandom.nextInt(160) + width);
            }
            theLastDrawVoiceLeft = left;
            //
            top = theRandom.nextInt(100);
            theLastDrawVoiceTop = top;
        }
        int right = left + width;
        int bottom = top + height;
        int centerX = toWidthFrom480320(screenW, screenW/2); // (right - left) / 2);
        int centerY = toHeightFrom480320(screenH, screenH/2); // (bottom - top) / 2);
        // 旋转显示，更加自然
        canvas.save();
        float angle = (isLeftSide) ? -30.f : 30.f;
        canvas.rotate(angle, centerX, centerY);
        //
        voiceDrawable.setBounds(
                toWidthFrom480320(screenW, left),
                toHeightFrom480320(screenH, top),
                toWidthFrom480320(screenW, right),
                toHeightFrom480320(screenH, bottom)
        );
        voiceDrawable.draw(canvas);
        //
        canvas.restore();
    }

    private static BitmapDrawable[] findVoiceDrawableArray(Context context, int avatarIndex,
                                                           final String paramVoiceName,
                                                           final BitmapDrawable[][] paramVoiceDrawableArray) {
        if (null == paramVoiceDrawableArray[avatarIndex]) {
            BitmapDrawable[] allVoiceDrawableArray = initDrawArray(
                    context, paramVoiceName, InitDrawType.ROW_OF_THE_AVATAR_INDEX);
            int frameCount = allVoiceDrawableArray.length / paramVoiceDrawableArray.length;
            for (int i = 0; i < paramVoiceDrawableArray.length; i++) {
                BitmapDrawable[] frameDrawableArray = new BitmapDrawable[frameCount];
                for (int j = 0; j < frameDrawableArray.length; j++) {
                    int k = i * frameCount + j;
                    frameDrawableArray[j] = allVoiceDrawableArray[k];
                }
                paramVoiceDrawableArray[i] = frameDrawableArray;
            }
        }
        return paramVoiceDrawableArray[avatarIndex];
    }

    private static BitmapDrawable[] findWeaponDrawableArray(Context context, int avatarIndex) {
        if (null == theAvatarWeaponDrawableArray[avatarIndex]) {
            BitmapDrawable[] allWeaponDrawableArray = initDrawArray(
                    context, theAvatarWeaponName, InitDrawType.COL_OF_THE_AVATAR_INDEX);
            for (int i = 0; i < theAvatarWeaponDrawableArray.length; i++) {
                theAvatarWeaponDrawableArray[i] = new BitmapDrawable[1];
                theAvatarWeaponDrawableArray[i][0] = allWeaponDrawableArray[i];
            }
        }
        return theAvatarWeaponDrawableArray[avatarIndex];
    }

    private static BitmapDrawable[] findSkillDrawableArray(
            Context context,
            int avatarIndex,
            final String[] paramNameArray,
            final BitmapDrawable[][] paramDrawArray) {
        BitmapDrawable[] frameDrawableArray = paramDrawArray[avatarIndex];
        if (null == frameDrawableArray) {
            String totalName = paramNameArray[avatarIndex];
            if (StringUtil.empty(totalName)) {
                Log.d(TAG, "name is empty, avatarIndex = " + avatarIndex);
            } else {
                frameDrawableArray = initDrawArray(context, totalName, InitDrawType.ALL);
                paramDrawArray[avatarIndex] = frameDrawableArray;
            }
        }
        return frameDrawableArray;
    }

    private static BitmapDrawable[] initDrawArray(Context context, String totalName, InitDrawType initType) {

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
        if (InitDrawType.ALL == initType) {
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    initDrawArray(totalBitmap, colCount, frameDrawableArray, width, height, row, col);
                }
            }
        } else if (InitDrawType.COL_OF_THE_AVATAR_INDEX == initType) {
            for (int row = 0; row < rowCount; row++) {
                initDrawArray(totalBitmap, colCount, frameDrawableArray, width, height, row, theAvatarIndexLast);
                initDrawArray(totalBitmap, colCount, frameDrawableArray, width, height, row, theAvatarIndexNext);
            }
        } else if (InitDrawType.ROW_OF_THE_AVATAR_INDEX == initType) {
            for (int col = 0; col < colCount; col++) {
                initDrawArray(totalBitmap, colCount, frameDrawableArray, width, height, theAvatarIndexLast, col);
            }
            for (int col = 0; col < colCount; col++) {
                initDrawArray(totalBitmap, colCount, frameDrawableArray, width, height, theAvatarIndexNext, col);
            }
        }

        return frameDrawableArray;
    }

    private static void initDrawArray(Bitmap totalBitmap, int colCount, BitmapDrawable[] frameDrawableArray,
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
    private static void recycleBitmap(int avatarIndex) {
        // 每局有2个角色，因此我们保留2个角色图片，如果超过，就回收
        int count = 0;
        for (int i = 0; i < theAvatarSkillSingleDrawableArray.length; i++) {
            if (null != theAvatarSkillSingleDrawableArray[i]) {
                count++;
            }
        }
        if (count > 2) {
            recycleBitmap();
        }
    }

    private static void recycleBitmap() {
        Log.d(TAG, "recycle bitmap");
        recycleBitmap(theAvatarSkillSingleDrawableArray);
        recycleBitmap(theAvatarSkillMultiDrawableArray);
        recycleBitmap(theAvatarWeaponDrawableArray);
        recycleBitmap(theAvatarVoiceSingleDrawableArray);
        recycleBitmap(theAvatarVoiceMultiDrawableArray);
    }

    private static void recycleBitmap(final BitmapDrawable[][] paramArray) {
        for (int i = 0; i < paramArray.length; i++) {
            BitmapDrawable[] drawableArray = paramArray[i];
            if (drawableArray != null) {
                for (int j = 0; j < drawableArray.length; j++) {
                    BitmapDrawable drawable = drawableArray[j];
                    if (drawable != null) {
                        drawable.getBitmap().recycle();
                        drawableArray[j] = null;
                    }
                }
                paramArray[i] = null;
            }
        }
    }

    // 是否在游戏画面中，点击了角色头像
    public static boolean isClickGameAvatarIcon(int screenW, int screenH,
                                                int clickX, int clickY) {
        if (theIsClickGameAvatarLast) {
            theIsClickGameAvatarLast = false;
            Log.d(TAG, "isClickGameAvatarIcon, stop show avatar last info");
            return true;
        }
        if (theIsClickGameAvatarNext) {
            theIsClickGameAvatarNext = false;
            Log.d(TAG, "isClickGameAvatarIcon, stop show avatar next info");
            return true;
        }

        Rect rect;
        // 左上角
        rect = new Rect(
                toWidthFrom480320(screenW, 0),
                toHeightFrom480320(screenH, 0),
                toWidthFrom480320(screenW, 0 + 80),
                toHeightFrom480320(screenH, 0 + 60));
        if (rect.contains(clickX, clickY)) {
            theIsClickGameAvatarLast = true;
            Log.d(TAG, "isClickGameAvatarIcon, start show avatar last info");
            return true;
        }
        // 右上角
        rect = new Rect(
                toWidthFrom480320(screenW, 480 - 80),
                toHeightFrom480320(screenH, 0),
                toWidthFrom480320(screenW, 480 - 80 + 80),
                toHeightFrom480320(screenH, 0 + 60));
        if (rect.contains(clickX, clickY)) {
            theIsClickGameAvatarNext = true;
            Log.d(TAG, "isClickGameAvatarIcon, start show avatar next info");
            return true;
        }
        //
        return false;
    }

    // 在游戏中显示角色详细信息
    public static boolean isDrawGameAvatarInfo(Context context, Canvas canvas,
                                               int screenW, int screenH) {
        if (theIsClickGameAvatarLast) {
            Log.d(TAG, "drawGameAvatarInfo, show avatar last info");
            final String info = theAvatarInfoArray[theAvatarIndexLast];
            drawGameAvatarInfo(canvas, screenW, screenH, info);
            return true;
        }
        if (theIsClickGameAvatarNext) {
            Log.d(TAG, "drawGameAvatarInfo, show avatar next info");
            final String info = theAvatarInfoArray[theAvatarIndexNext];
            drawGameAvatarInfo(canvas, screenW, screenH, info);
            return true;
        }
        return false;
    }

    private static void drawGameAvatarInfo(Canvas canvas, int screenW, int screenH, String info) {
        // 绘制背景
        thePaint.setARGB(0x88, 0xff, 0xff, 0xff);
        canvas.drawRect(
                toWidthFrom480320(screenW, 18),
                toHeightFrom480320(screenH, 18),
                toWidthFrom480320(screenW, 462),
                toHeightFrom480320(screenH, 302),
                thePaint
        );
        thePaint.setARGB(0xdd, 0, 0, 0);
        canvas.drawRect(
                toWidthFrom480320(screenW, 20),
                toHeightFrom480320(screenH, 20),
                toWidthFrom480320(screenW, 460),
                toHeightFrom480320(screenH, 300),
                thePaint
        );
        // 绘制描述
        TextUtil textUtil = new TextUtil(info,
                toWidthFrom480320(screenW, 40),
                toHeightFrom480320(screenH, 50),
                toWidthFrom480320(screenW, 400),        // w
                toHeightFrom480320(screenH, 240),        // h
                0x00A4FF, 255,      // 淡蓝色
                toHeightFrom480320(screenH, 15)
        );
        textUtil.initText();
        textUtil.drawText(canvas);
    }

    // 人物选择界面，是否点击了人物描述框
    public static boolean isClickHintOfAvatar(
            final int screenW, final int screenH,
            final int clickX, final int clickY) {
        final int topleftX = toWidthFrom480320(screenW, COORD_AVATARINFO_X);
        final int topleftY = toHeightFrom480320(screenH, COORD_AVATARINFO_Y);
        final int bottomrightX = toWidthFrom480320(screenW, COORD_AVATARINFO_X + COORD_AVATARINFO_W);
        final int bottomrightY = toHeightFrom480320(screenH, COORD_AVATARINFO_Y + COORD_AVATARINFO_H);
        final Rect rect = new Rect(topleftX, topleftY, bottomrightX, bottomrightY);
        if (!rect.contains(clickX, clickY)) {
            return false;
        }
        return true;
    }

    // 人物选择界面，人物是否解锁
    public static boolean isAvatarUnlocked(final Context context, int avatarIndex) {
        return true;
    }

    // 人物选择界面，解锁人物
    public static void unlockAvatar(final Context context, int avatarIndex) {

    }

    // 游戏是否能够运行？
    public static boolean canRun(final Context context) {
        return true;
    }

    // 获得积分
    public static int getTotalScore(final Context context) {
        return PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
    }

    // 获得积分
    public static String getTotalScoreAsString(final Context context) {
        return String.valueOf(PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0));
    }

    // 获得积分字符串
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

    // 每日赠送积分
    public static void initScore(final Context context) {

    }
}
