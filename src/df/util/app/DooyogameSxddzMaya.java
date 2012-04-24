package df.util.app;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-13
 * Time: ����4:55
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
 * dooyogame�����ɶ�������maya�汾
 */
public class DooyogameSxddzMaya {

    public static final String TAG = "df.util.DooyogameSxddzMaya";

//    /**
//     * ��ҳ�ʼ���֣�100��
//     * ���һ�̻���Ӯһ�̣���20~40�֣�ÿ����Ϸ���ֽ����ۻ�
//     * ����1�����Ӧ�����ͻ��֣�60~120��
//     * ϵͳ�ͻ��֣����û�����=0ʱ��ϵͳĬ��ÿ����50�֣�������100�ַⶥ��������
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
    // �ܻ���
    public static final String KEY_GAME_SCORE                    = "game.sxddz.score";
//    // ÿ�����͵Ļ��֣������һ�����Ϳ�ʼ���ۼƻ��֣�
//    public static final String KEY_GAME_GIVE_SCORE               = "game.sxddz.give_score";
//    // ���һ�����ͻ��ֵ�����
//    public static final String KEY_GAME_GIVE_DATE                = "game.sxddz.give_date";
//    // �����Ƿ������־
//    public static final String KEY_GAME_AVATARINDEX_UNLOCKED_SET = "game.sxddz.unlocked_avatarindex_set";
//
//    //
//    public static boolean theIsGetScoreDialogShown = false;

    //
    public static       String[]           theAvatarNameArray                = {
            "ʥ����", // 0
            "��ʿ", // 1
            "����", // 2
            "ʥͽ", // 3
            "��֪", // 4
            "������ʿ", // 5
            "Ԫ��ʹ", // 6
            "����ʦ", // 7
            "ʳ��ħ", // 8
            "����Ȯ", // 9
    };
    public static       String[]           theAvatarInfoArray                = {
            "ʥ����\n" +
                    "��Щ����ս�����Ѳ���ս�������������սʿ�ǣ����Ƿ����˽����Ķ�־����ʼ�������ּ�⣬" +
                    "��Ҫ�����еĽ�������ػ��Լ��ļ�԰���ػ��Լ��������������������\n" +
                    "��������������\n" +
                    "ʥ����ר����������\n" +
                    "���ֶ��ƣ�����\n" +
                    "�������ܣ�ʥ��\n" +
                    "����˵��������ʥ֮���������з�\n" +
                    "Ⱥ�弼�ܣ����\n" +
                    "����˵������ʱ�������������ѷ�Ӷ���Ĺ�����\n",
            "��ʿ\n" +
                    "��Щʹ�ó�ì����ʿ���Ѿ���ȫ�����˳���������������ì������һ����ì��ó����뻯��" +
                    "��Ȼֻ��Ƭ�����壬������ս����ìɨһ��Ƭ�������ܵ���һ����\n" +
                    "˫��װ�����ϻ�\n" +
                    "��ʿר��ì��װ��\n" +
                    "���弼�ܣ���ҧ\n" +
                    "����˵����ì�������ߣ��Ե������˽��з�񹥻�����ʹĿ���ʱ���������Ѫ״̬���л������Ӷ��Ʒ���\n" +
                    "Ⱥ�弼�ܣ�����\n" +
                    "����˵������ת��ì������ǹ������������һ����װ������ʱ�ſ���ʹ�ã��л������Ӷ��Ʒ���\n",
            "����\n" +
                    "��ȫ�����˹���֮������ʿ�����ǳ�û�ڴ���֮�䣬�乭����������һ������������˺���\n" +
                    "˫��������׷��\n" +
                    "����ר�ù�������\n" +
                    "���弼�ܣ�������\n" +
                    "����˵������׼Ŀ���������䣬��������\n" +
                    "Ⱥ�弼�ܣ����Ѽ�\n" +
                    "����˵������ͷ�ᱬը�ļ������ɹ�������Χ�ĵ���\n",
            "ʥͽ\n" +
                    "�õ������������ʦ�����������˹��������������˸��Ӿ�ͨ������֮���⣬������Ч�ظ��������������������\n" +
                    "������������\n" +
                    "ʥͽ����������\n" +
                    "���ַ��������Ǿ���\n" +
                    "���弼�ܣ�������\n" +
                    "����˵���������ָ�Ŀ�����HP\n" +
                    "���弼�ܣ���֮���\n" +
                    "����˵�����ڴ�״̬�£�������������Զ�����һ�Σ����ָ�����HP��MP\n",
            "��֪\n" +
                    "�������񴦻��������������ʦ�ǣ��ܹ�Ԥ�����£��������׷��������ʥף�� ��\n" +
                    "����������ʥ��\n" +
                    "����������\n" +
                    "���ַ��������Ǿ���\n" +
                    "���弼�ܣ�ħ��ȼ��\n" +
                    "����˵����ȼ��Ŀ���MPֵ\n" +
                    "Ⱥ�弼�ܣ�����\n" +
                    "����˵������ʱ�������Ŀ��HP��MP���ֵ\n",
            "������ʿ\n" +
                    "��Щ����ս��ϴ����ʧ���е�սʿ�ǣ������Ϊ�����ǵ������������Զ��������Լ���" +
                    "�����ô���������˵�ͷ�ǹǣ���������ǵ���Ȥ���ڡ�\n" +
                    "����������ڤ��\n" +
                    "������ʿר����������\n" +
                    "���ֶ��ƣ��ʺ�.��֮��\n" +
                    "���弼�ܣ�ڤ��\n" +
                    "����˵������ڤ֮�裬�԰���֮����������\n" +
                    "Ⱥ�弼�ܣ�ʬ��\n" +
                    "����˵��������ʬ���ϵĶ��ߣ��ڶ�ʱ�����ʬ����Χ�ĵ�Ⱥ�����ж���B\n",
            "Ԫ��ʹ\n" +
                    "��ħ���������г������������ʦ�ǣ��ƿ��ŷ硢�ء�ˮ���𡢰�������ʥһ��Ԫ�أ�" +
                    "�����صķ�����������Ԫ�صľ�������������Ե��ƻ���\n" +
                    "����������ĩ��\n" +
                    "Ԫ��ʹר����������\n" +
                    "���ַ������ӻ�.��֮��\n" +
                    "���弼�ܣ����鵯\n" +
                    "����˵�������۷�Ԫ�ع�������\n" +
                    "Ⱥ�弼�ܣ�����\n" +
                    "����˵�����������е�����ˮԪ�ض��ᣬʹȺ�������ӳ�״̬�޷�����\n",
            "����ʦ\n" +
                    "��ˮ�����п��ӵ�������������ʦ�ǣ���������а��������ܴ�ʬ������ȡ������" +
                    "�ܴӰ������ٻ����ֹ��ܵľ�����Э���Լ���������ɶ��ָ���״̬��\n" +
                    "��������������\n" +
                    "����ʦר��ˮ����������\n" +
                    "���ַ������ʺ�.��֮��\n" +
                    "���弼�ܣ�ʯ����\n" +
                    "���ܽ��ܣ���ʱ����ʹ��������ʯ����ʹ�䲻���ж����಻���ܵ�����\n" +
                    "���弼�ܣ�������\n" +
                    "���ܽ��ܣ���ʱ����ʹ�����޷�ʹ������������\n",
            "ʳ��ħ\n" +
                    "����λ�������ħ֮�أ���ɱ¾������ǿ������ʳ��ħ���������Ų����У���������ʿ��һ�𲢼���ս\n",
            "����Ȯ\n" +
                    "�����˵���ʵ���ѣ��Ը��꣬���г־õ�ս����\n",
    };
    public static       TextUtil[]         theAvatarInfoTextUtilArray        = new TextUtil[theAvatarInfoArray.length];
    // ���弼��
    public static       String[]           theAvatarSkillSingleNameArray     = new String[]{
            "sxddzmaya_skill_shengji_5x3",      // "�������ܣ�ʥ��\n����˵��������ʥ֮���������з�\n"
            "sxddzmaya_skill_sheyao_4x3",       // "���弼�ܣ���ҧ\n����˵����ì�������ߣ��Ե������˽��з�񹥻�����ʹĿ���ʱ���������Ѫ״̬���л������Ӷ��Ʒ���\n"
            "sxddzmaya_skill_lianhuanjian_4x3", // "���弼�ܣ�������\n����˵������׼Ŀ���������䣬��������\n"
            "sxddzmaya_skill_jijiushu_4x2",     // "���弼�ܣ�������\n����˵���������ָ�Ŀ�����HP\n"
            "sxddzmaya_skill_moliranshao_3x2",  // "���弼�ܣ�ħ��ȼ��\n����˵����ȼ��Ŀ���MPֵ\n"
            "sxddzmaya_skill_mingwu_4x2",       // "���弼�ܣ�ڤ��\n����˵������ڤ֮�裬�԰���֮����������\n"
            "sxddzmaya_skill_fenglingdan_4x3",  // "���弼�ܣ����鵯\n����˵�������۷�Ԫ�ع�������\n"
            "sxddzmaya_skill_shihuashu_3x2",    // "���弼�ܣ�ʯ����\n���ܽ��ܣ���ʱ����ʹ��������ʯ����ʹ�䲻���ж����಻���ܵ�����\n"
            "",
            "",
    };
    public static       String[]           theAvatarSkillSingleInfoArray     = new String[]{
            "ʥ���ߵ��弼�ܣ�����ʥ֮���������з�",
            "��ʿ���弼�ܣ�ì�������ߣ�������������ʹ���ʱ���ڳ�Ѫ���л������Ӷ��Ʒ���",
            "���˵��弼�ܣ���׼Ŀ���������䣬��������",
            "ʥͽ���弼�ܣ������ָ�Ŀ�����HP",
            "��֪���弼�ܣ�ȼ��Ŀ���MPֵ",
            "������ʿ���弼�ܣ���ڤ֮�裬�԰���֮����������",
            "Ԫ��ʹ���弼�ܣ����۷�Ԫ�ع�������",
            "����ʦ���弼�ܣ���ʱ����ʹ��������ʯ����ʹ�䲻���ж����಻���ܵ�����",
            "",
            "",
    };
    public static       BitmapDrawable[][] theAvatarSkillSingleDrawableArray = new BitmapDrawable[theAvatarSkillSingleNameArray.length][];
    // Ⱥ�弼��
    public static       String[]           theAvatarSkillMultiNameArray      = new String[]{
            "sxddzmaya_skill_zhenfen_3x2",      // "Ⱥ�弼�ܣ����\n����˵������ʱ�������������ѷ�Ӷ���Ĺ�����\n"
            "sxddzmaya_skill_dangji_3x2",       // "Ⱥ�幥��������\n����˵������ת��ì������ǹ������������һ����װ������ʱ�ſ���ʹ�ã��л������Ӷ��Ʒ���\n"
            "sxddzmaya_skill_paoliejian_4x2",   // "Ⱥ�弼�ܣ����Ѽ�\n����˵������ͷ�ᱬը�ļ������ɹ�������Χ�ĵ���\n"
            "sxddzmaya_skill_shenzhijuangu_4x2",// "���弼�ܣ���֮���\n����˵�����ڴ�״̬�£�������������Զ�����һ�Σ����ָ�����HP��MP\n"
            "sxddzmaya_skill_shenyou_4x2",      // "Ⱥ�弼�ܣ�����\n����˵������ʱ�������Ŀ��HP��MP���ֵ\n"
            "sxddzmaya_skill_shidu_4x2",        // "Ⱥ�弼�ܣ�ʬ��\n����˵��������ʬ���ϵĶ��ߣ��ڶ�ʱ�����ʬ����Χ�ĵ�Ⱥ�����ж���B\n"
            "sxddzmaya_skill_bingfeng_4x2",     // "Ⱥ�弼�ܣ�����\n����˵�����������е�����ˮԪ�ض��ᣬʹȺ�������ӳ�״̬�޷�����\n"
            "sxddzmaya_skill_shufushu_4x2",     // "���弼�ܣ�������\n���ܽ��ܣ���ʱ����ʹ�����޷�ʹ������������\n"
            "",
            "",
    };
    public static       String[]           theAvatarSkillMultiInfoArray      = new String[]{
            "ʥ����Ⱥ�弼�ܣ���ʱ�������������ѷ�Ӷ���Ĺ�����",
            "��ʿȺ�弼�ܣ���ת��ì������ǹ������������һ�����л������Ӷ��Ʒ���",
            "����Ⱥ�弼�ܣ���ͷ�ᱬը�ļ������ɹ�������Χ�ĵ���",
            "ʥͽ���弼�ܣ���״̬�£�������������Զ�����һ�Σ����ָ�����HP��MP",
            "��֪Ⱥ�弼�ܣ���ʱ�������Ŀ��HP��MP���ֵ",
            "������ʿȺ�弼�ܣ�����ʬ���ϵĶ��ߣ���ʱ�����ʬ����Χ�ĵ�Ⱥ�ж�",
            "Ԫ��ʹȺ�弼�ܣ���������е�����ˮԪ�أ�ʹȺ�������ӳ�״̬�޷�����",
            "����ʦȺ�弼�ܣ���ʱ����ʹ�����޷�ʹ������������",
            "",
            "",
    };
    public static       BitmapDrawable[][] theAvatarSkillMultiDrawableArray  = new BitmapDrawable[theAvatarSkillMultiNameArray.length][];
    // ����
    public static final String             theAvatarWeaponName               = "sxddzmaya_weapon_10x1";
    public static       BitmapDrawable[][] theAvatarWeaponDrawableArray      = new BitmapDrawable[theAvatarNameArray.length][];
    // ����
    public static final String             theAvatarVoiceSingleName          = "sxddzmaya_voice_skillsingle_4x10";
    public static       BitmapDrawable[][] theAvatarVoiceSingleDrawableArray = new BitmapDrawable[theAvatarNameArray.length][];
    public static final String             theAvatarVoiceMultiName           = "sxddzmaya_voice_skillmulti_4x10";
    public static       BitmapDrawable[][] theAvatarVoiceMultiDrawableArray  = new BitmapDrawable[theAvatarNameArray.length][];

    // ��ǰһ�ֵĶ�ս��ɫ����
    public static int theAvatarIndexLast = -1;
    public static int theAvatarIndexNext = -1;

    enum InitDrawType {
        ALL,    // ���е�Ԫ��
        COL_OF_THE_AVATAR_INDEX,    // ֻ��ʼ���У���ȡֵ�μ�theAvatarIndexLast��theAvatarIndexNext
        ROW_OF_THE_AVATAR_INDEX,    // ֻ��ʼ���У���ȡֵ�μ�theAvatarIndexLast��theAvatarIndexNext
    }

    // �Ƿ�����Ϸ�����е���˽�ɫ��ͷ��
    public static boolean theIsClickGameAvatarLast = false;
    public static boolean theIsClickGameAvatarNext = false;

    public static final String[] theTipOfSelectUpPlayerArray   = new String[]{
            "��ѡ�������ϼ�",
            "������ʿ�����κ���ս",
            "ΰ�������������ͬ�ڣ�",
    };
    public static final String[] theTipOfSelectDownPlayerArray = new String[]{
            "��ѡ�������¼�",
            "������ʿ��ս������",
            "��ҫ����ΰ���������",
    };

    public static final Paint thePaint = new Paint();

    public static int theLastSelectAvatarIndex = -1;  // ���һ��ѡ��Ľ�ɫ����

    public static int  theLastGameBackgroundId     = 0;
    public static long theLastGameBackgroundMillis = 0;

    public static int  theLastDrawAvatarIndex   = -1;     // ���һ�λ��ƵĽ�ɫ����
    public static int  theLastDrawFrame         = -1;
    public static long theLastDrawTimeMillis    = 0;
    public static long theStartDrawTimeMillis   = 0;
    public static long theDrawSpanMinTimeMillis = 3000;
    public static int  theLastDrawVoiceFrame    = -1;
    public static int  theLastDrawVoiceLeft     = -1;
    public static int  theLastDrawVoiceTop      = -1;

    public static final int SKILL_TIMEMILLIS_FRAME_SWITCH = 50;   // ÿ֡�л�ʱ����
    public static final int SKILL_TIMEMILLIS_FRAME_SINGLE = 150;   // ÿ֡��ʾʱ�䳤��
    public static final int SKILL_TIMEMILLIS_FRAME_LAST   = 500;   // ���һ֡��ʾʱ�䳤��

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

    // ����һ������ͼƬ
    public static Drawable toGameBackgroundDrawable(Context context) {
        long currMillis = System.currentTimeMillis();
        if (theLastGameBackgroundMillis == 0) {
            theLastGameBackgroundMillis = currMillis;
        }
        // ��һ��
        if (theLastGameBackgroundId == 0) {
            theLastGameBackgroundId = getRandomGameBackgroundId(context);
        }
        // ��30�뻻һ��
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

    // ���������
    public static String toNameOfAvatar(int avatarIndex) {
        if ((avatarIndex < 0) || (avatarIndex > 9)) {
            Log.d(TAG, "toNameOfAvatar, avatarIndex illegal, avatarIndex = " + avatarIndex);
            return "δ֪";
        }
        return theAvatarNameArray[avatarIndex];
    }

    // ����ѡ����棬���Ͻǵ���ʾ��Ϣ
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

    // ѡ������棬�·�������������Ϣ
    public static void drawHintOfAvatarChangePage(Canvas canvas, int screenW, int screenH, int avatarIndex,
                                                  boolean isChangePage) {
        if ((avatarIndex < 0) || (avatarIndex > 9)) {
            Log.d(TAG, "drawHintOfAvatarChangePage, avatarIndex illegal, avatarIndex = " + avatarIndex);
            return;
        }

        // ��ʼ��
        TextUtil textUtil = theAvatarInfoTextUtilArray[avatarIndex];
        if (null == textUtil) {
            for (int i = 0; i < theAvatarInfoTextUtilArray.length; i++) {
                String info = theAvatarInfoArray[i];
                TextUtil tu = new TextUtil(info,
                        toWidthFrom480320(screenW, COORD_AVATARINFO_X),
                        toHeightFrom480320(screenH, COORD_AVATARINFO_Y),
                        toWidthFrom480320(screenW, COORD_AVATARINFO_W),
                        toHeightFrom480320(screenH, COORD_AVATARINFO_H),
                        0x00B4FF, 255,      // ����ɫ
                        toHeightFrom480320(screenH, TEXT_SIZE_AVATARINFO)
                );
                tu.initText();
                theAvatarInfoTextUtilArray[i] = tu;
            }
            textUtil = theAvatarInfoTextUtilArray[avatarIndex];
        }

        // ����л���avatar�������¿�ʼ��ʾ
        if (theLastSelectAvatarIndex != avatarIndex) {
            textUtil.gotoFirstPage();
            theLastSelectAvatarIndex = avatarIndex;
        } else {
            // ��ҳ����ʾ
            if (isChangePage) {
                textUtil.loopPageForward();
            }
        }
        textUtil.drawText(canvas);
    }

    public static void drawHintOfAvatar(Canvas canvas, int screenW, int screenH, int avatarIndex) {
        drawHintOfAvatarChangePage(canvas, screenW, screenH, avatarIndex, true);
    }


    // �¿�һ�ֳ�ʼ��������2����ɫ����������
    public static void newLevel(Context context, int avatarIndexLast, int avatarIndexNext) {

        Log.d(TAG, "newLevel, avatarIndexLast = " + avatarIndexLast + ", avatarIndexNext = " + avatarIndexNext);
        theAvatarIndexLast = avatarIndexLast;
        theAvatarIndexNext = avatarIndexNext;

        // ����ͼƬ
        recycleBitmap();

        // ��ʼ��ͼƬ
        int[] avatarIndexArray = new int[]{avatarIndexLast, avatarIndexNext};
        for (int i = 0; i < avatarIndexArray.length; i++) {
            int avatarIndex = avatarIndexArray[i];

            // ���弼��ͼƬ
            BitmapDrawable[] skillSingleDrawableArray = findSkillDrawableArray(
                    context, avatarIndex, theAvatarSkillSingleNameArray, theAvatarSkillSingleDrawableArray);
            assert (skillSingleDrawableArray != null);
            // Ⱥ�弼��ͼƬ
            BitmapDrawable[] skillMultiDrawableArray = findSkillDrawableArray(
                    context, avatarIndex, theAvatarSkillMultiNameArray, theAvatarSkillMultiDrawableArray);
            assert (skillMultiDrawableArray != null);
            // ����ͼƬ
            BitmapDrawable[] weaponDrawableArray = findWeaponDrawableArray(context, avatarIndex);
            assert (weaponDrawableArray != null);
            // ��������ͼƬ
            BitmapDrawable[] voiceSingleDrawableArray = findVoiceDrawableArray(
                    context, avatarIndex, theAvatarVoiceSingleName, theAvatarVoiceSingleDrawableArray);
            assert (voiceSingleDrawableArray != null);
            // Ⱥ������ͼƬ
            BitmapDrawable[] voiceMultiDrawableArray = findVoiceDrawableArray(
                    context, avatarIndex, theAvatarVoiceMultiName, theAvatarVoiceMultiDrawableArray);
            assert (voiceMultiDrawableArray != null);
        }
    }

    // ��Ϸ���棬���Ƽ�����Ч
    public static void drawSkillOfAvatar(Context context, Canvas canvas, int screenW, int screenH, int avatarIndex,
                                         int paiNumber, int paiFlag) {
        // �ض�������ʱ����ʾ��Ч��1��, 2��, 3��, 4����1, 5����2, 6ը��, 7��ը, 8˳��, 9˳��2, a˳��3, b��˳1, c��˳2, d�Ĵ�2
        // ����������5ʱ��ʾ��Ч
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
        // ��ֹ������ʾ��һ����ɫ����Ч
        if ((StringUtil.empty(theAvatarSkillSingleNameArray[avatarIndex]))
                || (StringUtil.empty(theAvatarSkillMultiNameArray[avatarIndex]))) {
            theLastDrawAvatarIndex = avatarIndex;
            Log.d(TAG, "drawSkillOfAvatar, skill name is empty, avatarIndex = " + avatarIndex);
            return;
        }

        // ����л��˽�ɫ��������ɽ�ɫ�����������ڴ�ռ��
        recycleBitmap(avatarIndex);

        // ���弼��ͼƬ
        BitmapDrawable[] skillSingleDrawableArray = findSkillDrawableArray(
                context, avatarIndex, theAvatarSkillSingleNameArray, theAvatarSkillSingleDrawableArray);
        assert (skillSingleDrawableArray != null);
        // ���弼������
        String skillSingleInfo = theAvatarSkillSingleInfoArray[avatarIndex];
        assert (!StringUtil.empty(skillSingleInfo));
        // Ⱥ�弼��ͼƬ
        BitmapDrawable[] skillMultiDrawableArray = findSkillDrawableArray(
                context, avatarIndex, theAvatarSkillMultiNameArray, theAvatarSkillMultiDrawableArray);
        assert (skillMultiDrawableArray != null);
        // Ⱥ�弼������
        String skillMultiInfo = theAvatarSkillMultiInfoArray[avatarIndex];
        assert (!StringUtil.empty(skillMultiInfo));
        // ����ͼƬ
        BitmapDrawable[] weaponDrawableArray = findWeaponDrawableArray(context, avatarIndex);
        assert (weaponDrawableArray != null);
        // ��������ͼƬ
        BitmapDrawable[] voiceSingleDrawableArray = findVoiceDrawableArray(
                context, avatarIndex, theAvatarVoiceSingleName, theAvatarVoiceSingleDrawableArray);
        assert (voiceSingleDrawableArray != null);
        // Ⱥ������ͼƬ
        BitmapDrawable[] voiceMultiDrawableArray = findVoiceDrawableArray(
                context, avatarIndex, theAvatarVoiceMultiName, theAvatarVoiceMultiDrawableArray);
        assert (voiceMultiDrawableArray != null);

        // ����л��˽�ɫ�������»���
        if (theLastDrawAvatarIndex != avatarIndex) {
            theLastDrawAvatarIndex = avatarIndex;
            theLastDrawTimeMillis = 0;
            theStartDrawTimeMillis = 0;
            theLastDrawFrame = -1;
            theLastDrawVoiceFrame = -1;
            theLastDrawVoiceLeft = -1;
            theLastDrawVoiceTop = -1;
        }
        // ��ʱ����һ֡
        long now = System.currentTimeMillis();
        if (theLastDrawTimeMillis == 0) {
            theLastDrawTimeMillis = now;
        }
        if (theStartDrawTimeMillis == 0) {
            theStartDrawTimeMillis = now;
        }
        if (now - theLastDrawTimeMillis > SKILL_TIMEMILLIS_FRAME_SWITCH) {
            theLastDrawTimeMillis = now;

            // �Ƿ��������
            boolean isDraw = true;
            if ((now - theStartDrawTimeMillis > theDrawSpanMinTimeMillis)      // ����ʱ��
                    && (theLastDrawFrame == 0)                                // ���������һ֡
                    ) {
                isDraw = false;
            }

            if (isDraw) {
                // ����������ʾȺ�弼��
                boolean isSkillSingle = (paiNumber < 6);
                String skillInfo = (isSkillSingle ? skillSingleInfo : skillMultiInfo);
                BitmapDrawable[] skillDrawableArray = (isSkillSingle ? skillSingleDrawableArray : skillMultiDrawableArray);
                BitmapDrawable[] voiceDrawableArray = (isSkillSingle ? voiceSingleDrawableArray : voiceMultiDrawableArray);

//                // �������ظ��£���ʾ�����Ч: 1��, 2��, 3��, 4����1, 5����2, 6ը��, 7��ը, 8˳��, 9˳��2, a˳��3, b��˳1, c��˳2, d�Ĵ�2
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

                // ������������֡
                theLastDrawFrame++;
                if (theLastDrawFrame >= skillDrawableArray.length) {
                    theLastDrawFrame = 0;
                }
                Log.d(TAG, "theLastDrawFrame = " + theLastDrawFrame);

                // �жϵ�ǰ��ɫ����߻����ұߵĽ�ɫ
                boolean isLeftSide = (avatarIndex == theAvatarIndexLast);

                // ������Ч
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
        // ���Ʊ���
        thePaint.setARGB(0xdd, 0, 0, 0);
        canvas.drawRect(
                toWidthFrom480320(screenW, 0),
                toHeightFrom480320(screenH, 240),
                toWidthFrom480320(screenW, 480),
                toHeightFrom480320(screenH, 320),
                thePaint
        );
        // ��������
        int left = (isLeftSide) ? 120 : 60;
        TextUtil textUtil = new TextUtil(skillInfo,
                toWidthFrom480320(screenW, left),
                toHeightFrom480320(screenH, 270),
                toWidthFrom480320(screenW, 300),        // w
                toHeightFrom480320(screenH, 30),        // h
                0x00A4FF, 255,      // ����ɫ
                toHeightFrom480320(screenH, 16)
        );
        textUtil.initText();
        textUtil.drawText(canvas);
    }

    private static void drawSkillOfAvatarOfWeapon(Canvas canvas, int screenW, int screenH,
                                                  BitmapDrawable bitmapDrawable,
                                                  boolean isLeftSide) {
        // ��������
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
            // ���ڶ����ʾʱ�����ò�ͬ�����
            int startLeft = 0;
            int startTop = 0;
            if (i > 0) {
                int offset = theRandom.nextInt(120);
                startLeft += offset;
                startTop += offset;
            }

            // ���Ƽ���
            Drawable frameDrawable = skillSingleDrawableArray[frame];
            final int left = (int) (screenW * 0.25) + startLeft;
            final int top = (int) (screenH * 0.25) + startTop;
            final int right = (int) (screenW * 0.75) + startLeft;
            final int bottom = (int) (screenH * 0.75) + startTop;
            frameDrawable.setBounds(left, top, right, bottom);
            frameDrawable.draw(canvas);
        }

        // �ӳ�һ�ᣬ��ʾ��֡��Ч
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
        // ÿ֡ͼƬ֮����ù̶�ʱ����
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
        // ��ת��ʾ��������Ȼ
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

        // ���ͼƬ
        int totalId = ResourceUtil.getDrawableResourceIdFromName(context, totalName);
        BitmapDrawable totalDrawable = (BitmapDrawable) context.getResources().getDrawable(totalId);
        final Bitmap totalBitmap = totalDrawable.getBitmap();

        // todo: ���ͼƬ��drawableĿ¼�£�minVersion=3�Ļ��������ţ�������ǻ��ʵ��ͼƬ��С
        int totalWidth = totalBitmap.getWidth();
        int totalHeight = totalBitmap.getHeight();

        // ���Ƹ�ʽ������sxddzmaya_skill_shengji_4x3
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

        // ����ÿ֡ͼƬ
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

    // ����ͼƬ�ڴ�
    private static void recycleBitmap(int avatarIndex) {
        // ÿ����2����ɫ��������Ǳ���2����ɫͼƬ������������ͻ���
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

    // �Ƿ�����Ϸ�����У�����˽�ɫͷ��
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
        // ���Ͻ�
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
        // ���Ͻ�
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

    // ����Ϸ����ʾ��ɫ��ϸ��Ϣ
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
        // ���Ʊ���
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
        // ��������
        TextUtil textUtil = new TextUtil(info,
                toWidthFrom480320(screenW, 40),
                toHeightFrom480320(screenH, 50),
                toWidthFrom480320(screenW, 400),        // w
                toHeightFrom480320(screenH, 240),        // h
                0x00A4FF, 255,      // ����ɫ
                toHeightFrom480320(screenH, 15)
        );
        textUtil.initText();
        textUtil.drawText(canvas);
    }

    // ����ѡ����棬�Ƿ���������������
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

    // ����ѡ����棬�����Ƿ����
    public static boolean isAvatarUnlocked(final Context context, int avatarIndex) {
        return true;
    }

    // ����ѡ����棬��������
    public static void unlockAvatar(final Context context, int avatarIndex) {

    }

    // ��Ϸ�Ƿ��ܹ����У�
    public static boolean canRun(final Context context) {
        return true;
    }

    // ��û���
    public static int getTotalScore(final Context context) {
        return PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
    }

    // ��û���
    public static String getTotalScoreAsString(final Context context) {
        return String.valueOf(PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0));
    }

    // ��û����ַ���
    public static String getTotalScoreValueAndString(final Context context) {
        return getTotalScore(context) + "����";
    }

    // ���ݽ������һ�ֽ������߼�
    public static void processLevelResult(final Context context, final int leftPaiCount, final int point) {
        if (leftPaiCount > 0) {
            lostLevelScore(context, point);
        } else {
            winLevelScore(context, point);
        }
    }

    // ����ʧ��
    public static void lostLevelScore(final Context context, final int point) {
        //
        int totalScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
        Log.i(TAG, "level lost, old total score = " + totalScore);
        //
//        Random r = new Random();
//        int lostScore = r.nextInt(SCORE_LEVEL_MAX - SCORE_LEVEL_MIN);
//        lostScore += SCORE_LEVEL_MIN;
        // todo: �����Ǹ���
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

    // ����ʤ��
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

    // ÿ�����ͻ���
    public static void initScore(final Context context) {

    }
}
