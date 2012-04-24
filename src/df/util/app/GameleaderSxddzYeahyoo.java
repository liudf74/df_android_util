package df.util.app;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-13
 * Time: ����4:55
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
 * gameleader�����ɶ�������yeahyoo�汾 ����
 */
public class GameleaderSxddzYeahyoo {

    public static final String TAG = "df.util.GameleaderSxddzYeahyoo";

    /**
     * ��ҳ�ʼ���֣�100��
     * ���һ�̻���Ӯһ�̣���20~40�֣�ÿ����Ϸ���ֽ����ۻ�
     * ����1�����Ӧ�����ͻ��֣�60~120��
     * ϵͳ�ͻ��֣����û�����=0ʱ��ϵͳĬ��ÿ����50�֣�������100�ַⶥ��������
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

    // �ܻ���
    public static final String KEY_GAME_SCORE                    = "game.sxddz.score";
    //    // ÿ�����͵Ļ��֣������һ�����Ϳ�ʼ���ۼƻ��֣�
//    public static final String KEY_GAME_GIVE_SCORE               = "game.sxddz.give_score";
    // ���һ�����ͻ��ֵ�����
    public static final String KEY_GAME_GIVE_DATE                = "game.sxddz.give_date";
    // �����Ƿ������־
    public static final String KEY_GAME_AVATARINDEX_UNLOCKED_SET = "game.sxddz.unlocked_avatarindex_set";

    //
    public static boolean theIsGetScoreDialogShown = false;

    // ��ɫ����
    public static String[] avatarNameArray = {
            "����", // 0
            "ţͷ", // 1
            "����", // 2
            "����", // 3
            "����", // 4
            "ʥĸ", // 5
            "Ү��", // 6
            "����", // 7
            "����", // 8
            "����", // 9
    };

    // ��Ч
    public static String[]           theSkillNameArray     = new String[]{
            "sxddzmaya_skill_shengji_5x3",      // "�������ܣ�ʥ��\n����˵��������ʥ֮���������з�\n"
//            "sxddzmaya_skill_sheyao_4x3",       // "���弼�ܣ���ҧ\n����˵����ì�������ߣ��Ե������˽��з�񹥻�����ʹĿ���ʱ���������Ѫ״̬���л������Ӷ��Ʒ���\n"
            "sxddzmaya_skill_lianhuanjian_4x3", // "���弼�ܣ�������\n����˵������׼Ŀ���������䣬��������\n"
            "sxddzmaya_skill_jijiushu_4x2",     // "���弼�ܣ�������\n����˵���������ָ�Ŀ�����HP\n"
//            "sxddzmaya_skill_moliranshao_3x2",  // "���弼�ܣ�ħ��ȼ��\n����˵����ȼ��Ŀ���MPֵ\n"
//            "sxddzmaya_skill_mingwu_4x2",       // "���弼�ܣ�ڤ��\n����˵������ڤ֮�裬�԰���֮����������\n"
//            "sxddzmaya_skill_fenglingdan_4x3",  // "���弼�ܣ����鵯\n����˵�������۷�Ԫ�ع�������\n"
//            "sxddzmaya_skill_shihuashu_3x2",    // "���弼�ܣ�ʯ����\n���ܽ��ܣ���ʱ����ʹ��������ʯ����ʹ�䲻���ж����಻���ܵ�����\n"
//            "sxddzmaya_skill_zhenfen_3x2",      // "Ⱥ�弼�ܣ����\n����˵������ʱ�������������ѷ�Ӷ���Ĺ�����\n"
            "sxddzmaya_skill_dangji_3x2",       // "Ⱥ�幥��������\n����˵������ת��ì������ǹ������������һ����װ������ʱ�ſ���ʹ�ã��л������Ӷ��Ʒ���\n"
            "sxddzmaya_skill_paoliejian_4x2",   // "Ⱥ�弼�ܣ����Ѽ�\n����˵������ͷ�ᱬը�ļ������ɹ�������Χ�ĵ���\n"
//            "sxddzmaya_skill_shenzhijuangu_4x2",// "���弼�ܣ���֮���\n����˵�����ڴ�״̬�£�������������Զ�����һ�Σ����ָ�����HP��MP\n"
//            "sxddzmaya_skill_shenyou_4x2",      // "Ⱥ�弼�ܣ�����\n����˵������ʱ�������Ŀ��HP��MP���ֵ\n"
//            "sxddzmaya_skill_shidu_4x2",        // "Ⱥ�弼�ܣ�ʬ��\n����˵��������ʬ���ϵĶ��ߣ��ڶ�ʱ�����ʬ����Χ�ĵ�Ⱥ�����ж���B\n"
//            "sxddzmaya_skill_bingfeng_4x2",     // "Ⱥ�弼�ܣ�����\n����˵�����������е�����ˮԪ�ض��ᣬʹȺ�������ӳ�״̬�޷�����\n"
//            "sxddzmaya_skill_shufushu_4x2",     // "���弼�ܣ�������\n���ܽ��ܣ���ʱ����ʹ�����޷�ʹ������������\n"
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

    public static final int SKILL_TIMEMILLIS_FRAME_SWITCH = 50;   // ÿ֡�л�ʱ����
    public static final int SKILL_TIMEMILLIS_FRAME_SINGLE = 100;   // ÿ֡��ʾʱ�䳤��
    public static final int SKILL_TIMEMILLIS_FRAME_LAST   = 500;   // ���һ֡��ʾʱ�䳤��

    private static final Random theRandom = new Random(System.currentTimeMillis());

    //////////////////////////////////////////////////////////////
    // ��������
    //////////////////////////////////////////////////////////////

    private static int toHeightFrom480320(int screenH, int yOf480320) {
        return yOf480320 * screenH / 320;
    }

    private static int toWidthFrom480320(int screenW, int xOf480320) {
        return xOf480320 * screenW / 480;
    }

    ////////////////////////////////////////////////////////////////
    // ��������߼�
    ////////////////////////////////////////////////////////////////

    // �����Ƿ����
    public static boolean isAvatarUnlocked(final Context context, int avatarIndex) {
        if ((avatarIndex < 0) || (avatarIndex > 9)) {
            Log.d(TAG, "avatarIndex illegal, avatarIndex = " + avatarIndex);
            return true;
        }

        String set = PreferenceUtil.readRecord(context, KEY_GAME_AVATARINDEX_UNLOCKED_SET, "");

        // ��ʼ�����ֽ�������
        if (StringUtil.empty(set)) {
            set = "0,1,2,3,";
            PreferenceUtil.saveRecord(context, KEY_GAME_AVATARINDEX_UNLOCKED_SET, set);
            Log.d(TAG, "init unlocked avatar, unlocked avatarIndex set = " + set);
        }

        final String key = String.valueOf(avatarIndex) + ",";
        return StringUtil.contains(set, key);
    }

    // ��������
    public static void unlockAvatar(final Context context, int avatarIndex) {
        final String key = String.valueOf(avatarIndex) + ",";
        String set = PreferenceUtil.readRecord(context, KEY_GAME_AVATARINDEX_UNLOCKED_SET, "");
        if (!StringUtil.contains(set, key)) {
            set += key;
            PreferenceUtil.saveRecord(context, KEY_GAME_AVATARINDEX_UNLOCKED_SET, set);
            Log.d(TAG, "unlock avatar, unlocked avatarIndex set = " + set);
        }
    }

    // ��ʾ���������
    public static void drawHintOfSelectAvatar(final Context context,
                                              Canvas canvas,
                                              int avatarIndex,
                                              int screenW, int screenH) {
        // todo: ����λ�ù̶�
        final int x = 225 * screenW / 480;
        final int y = 275 * screenH / 320;

        boolean unlocked = isAvatarUnlocked(context, avatarIndex);
        if (unlocked) {
            String hint = "����ͷ��ѡ��";
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setARGB(0xff, 0xff, 0xff, 0xff);
            paint.setTextSize(screenH * 18.f / 320.f);
            canvas.drawText(hint, x, y, paint);
        } else {
            String hint = "����Ӧ�ý�������";
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setARGB(0xff, 0xff, 0xff, 0x00);
            paint.setTextSize(screenH * 18.f / 320.f);
            canvas.drawText(hint, x, y, paint);
        }
    }

    // ������������
    public static void clickHintOfSelectAvatar(final Context context,
                                               final int avatarIndex,
                                               final int screenW, final int screenH,
                                               final int clickX, final int clickY
    ) {
        // todo: �ı���Ϣ����λ��
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
        // �Ѿ��������޶���
        // δ������������ع��
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
    // ��Ϸ�ڻ�û���
    ////////////////////////////////////////////////////////////////

    // ��ʼ������
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

    // ��Ϸ�Ƿ��ܹ����У�
    public static boolean canRun(final Context context) {
        // ���ֹ�����������
        if (getTotalScore(context) > 0) {
            return true;
        }

        // ������ʾ�û��������ظ���ʾ
        if (theIsGetScoreDialogShown) {
            Log.d(TAG, "get score dialog is run");
            return false;
        }

        theIsGetScoreDialogShown = true;
        // �����Ƿ���ʱ��ʾ���������ʾ��Ϣ
        Log.i(TAG, "prompt user to get score");
        final String hintMessage = String.format(
                "�װ������ѣ�������Ϸ���ֲ��㣬��Ҫ�������ع�����ܼ�����Ϸ��"
        );
        final String title = "��ʾ";
        final String btnDownload = "���ع��";

        final AlertDialog scoreDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(hintMessage)
                .setPositiveButton(btnDownload, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        // ��ʾ���ع��
                        clickGetScore(context);
                        //
                        theIsGetScoreDialogShown = false;
                        // �رյ�ǰ�Ի���
                        dialogInterface.cancel();
                    }
                })
                .create();
        scoreDialog.show();
        return false;
    }

    // �������ȡ���֡�
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

                // todo�����ڻ���Ϊ0ʱ�Ż����ع�棬��˽��������Ļ��֣�ֱ������Ϊ���ػ���
                totalScore += score;
                PreferenceUtil.saveRecord(context, KEY_GAME_SCORE, totalScore);
                Log.i(TAG, "get score, new total score = " + totalScore);
            }
        });
    }

    // ��û�����ֵ
    public static int getTotalScore(final Context context) {
        return PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
    }

    // ��û�����ֵ�ַ���
    public static String getTotalScoreAsString(final Context context) {
        return String.valueOf(PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0));
    }

    // ��á�����x�����ַ���
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

        // todo: ���ù��������ѽӿڣ���ȥ����
        Yeahyoo.spendScoreFromServer(context, lostScore, new SpendScoreFeedback() {
            @Override
            public void doAfterSpendScore(int i, int i1) {
                /**
                 * resultNo : 1 �ɹ��� 2 ʧ�ܣ� 3 ���ֲ������ѣ�
                 * score ����ǰ���û����µĻ���
                 */
                Log.d(TAG, "level lost, spend score from server");
            }
        });

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

    // todo: 20120206: xiaona: Ҫ��ȥ��
//    // ÿ�����ͻ���
//    public static void initScore(final Context context) {
//
//        final int dayScore = SCORE_DAY;
//        final int oldScore = PreferenceUtil.readRecord(context, KEY_GAME_LOCAL_SCORE, 0);
//        long giveDate = PreferenceUtil.readRecord(context, KEY_GAME_GIVE_DATE, 0L);
//        long now = TimeUtil.toLongOfyyyyMMdd();
//
//        // �������Ϊ0����ʼ���ͻ���
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
//        // ������ֲ�Ϊ0�����ж��Ƿ�������ϣ�û���������ʱ����������
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
    // ������Ч
    ////////////////////////////////////////////////////////////////

    // �¿�һ�ֳ�ʼ��������2����ɫ����������
    public static void newLevel(Context context) {
        // ����ͼƬ
        recycleBitmap();

        // ��ʼ��ͼƬ
        for (int skill = 0; skill < theSkillNameArray.length; skill++) {
            BitmapDrawable[] skillSingleDrawableArray = findSkillDrawableArray(
                    context, theSkillNameArray, theSkillDrawableArray, skill);
            assert (skillSingleDrawableArray != null);
        }
    }

    // ��Ϸ���棬���Ƽ�����Ч
    public static void drawSkillOfAvatar(Context context, Canvas canvas,
                                         int screenW, int screenH,
                                         int paiNumber, int paiFlag) {
        // �ض�������ʱ����ʾ��Ч; �������ظ�ʱ��ʾ�����Ч
        // �����Ͷ��壺1��, 2��, 3��, 4����1, 5����2, 6ը��, 7��ը, 8˳��, 9˳��2, a˳��3, b��˳1, c��˳2, d�Ĵ�2
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
            // todo: ����
            skillACount = 1 + (paiNumber - 5) / 2;
//            skillACount = 2;
//            skillBCount = 2;
//            skillCCount = 1;
        } else if ((paiNumber < 6)) {
//            Log.d(TAG, "paiNumber is to small, paiFlag = " + paiFlag + ", paiNumber = " + paiNumber);
            return;
        }


        // ����л����ƣ������»���
        if ((theLastPaiFlag != paiFlag) || (theLastPaiNumber != paiNumber)) {
            theLastPaiFlag = paiFlag;
            theLastPaiNumber = paiNumber;
            theLastDrawTimeMillis = theStartDrawTimeMillis = 0;
            theLastDrawFrame = -1;
            theLastDrawFrameFinished = false;
            theLastDrawSkillA = theLastDrawSkillB = theLastDrawSkillC = -1;
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
                    && (theLastDrawFrameFinished)/*(theLastDrawFrame == 0)*/                                 // ���������һ֡
                    ) {
                isDraw = false;
            }

            if (isDraw) {
                Log.d(TAG, "paiFlag = " + paiFlag + ", paiNumber = " + paiNumber +
                        ", skillACount = " + skillACount+ ", skillBCount = " + skillBCount+ ", skillCCount = " + skillCCount);

                // ����ͼƬ
                int maxSkillDrawableArrayCount = 0;
                // ����A
                if (theLastDrawSkillA == -1) {
                    theLastDrawSkillA = theRandom.nextInt(theSkillNameArray.length);
                }
                BitmapDrawable[] skillADrawableArray = findSkillDrawableArray(
                        context, theSkillNameArray, theSkillDrawableArray, theLastDrawSkillA);
                assert (skillADrawableArray != null);
                if (maxSkillDrawableArrayCount < skillADrawableArray.length) {
                    maxSkillDrawableArrayCount = skillADrawableArray.length;
                }
                // ����B
                if (theLastDrawSkillB == -1) {
                    theLastDrawSkillB = theRandom.nextInt(theSkillNameArray.length);
                }
                BitmapDrawable[] skillBDrawableArray = findSkillDrawableArray(
                        context, theSkillNameArray, theSkillDrawableArray, theLastDrawSkillB);
                assert (skillBDrawableArray != null);
                if (maxSkillDrawableArrayCount < skillBDrawableArray.length) {
                    maxSkillDrawableArrayCount = skillBDrawableArray.length;
                }
                // ����C
                if (theLastDrawSkillC == -1) {
                    theLastDrawSkillC = theRandom.nextInt(theSkillNameArray.length);
                }
                BitmapDrawable[] skillCDrawableArray = findSkillDrawableArray(
                        context, theSkillNameArray, theSkillDrawableArray, theLastDrawSkillC);
                assert (skillCDrawableArray != null);
                if (maxSkillDrawableArrayCount < skillCDrawableArray.length) {
                    maxSkillDrawableArrayCount = skillCDrawableArray.length;
                }

                // ������������֡
                theLastDrawFrame++;
                if (theLastDrawFrame > maxSkillDrawableArrayCount - 1) {
//                    theLastDrawFrame = 0;
                    theLastDrawFrameFinished = true;
                }
                Log.d(TAG, "theLastDrawFrame = " + theLastDrawFrame);

                // ������Ч
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillADrawableArray, theLastDrawFrame, skillACount);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillBDrawableArray, theLastDrawFrame, skillBCount);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillCDrawableArray, theLastDrawFrame, skillCCount);

                // �ӳ�һ�ᣬ��ʾ��֡��Ч
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
            // ���ڶ����ʾʱ�����ò�ͬ�����
            int startLeft = 0;
            int startTop = 0;
            if (i > 0) {
                int offset = theRandom.nextInt(200);
                startLeft += offset;
                startTop += offset;
            }

            // ���Ƽ���
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

    // ����ͼƬ�ڴ�
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
