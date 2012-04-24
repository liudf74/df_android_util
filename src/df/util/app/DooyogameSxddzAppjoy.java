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
 * gameleader�����ɶ�������appjoy
 */
public class DooyogameSxddzAppjoy {

    public static final String TAG = "df.util.app.DooyogameSxddzAppjoy";

    /**
     * //     * ��ҳ�ʼ���֣�100��
     * //     * ���һ�̻���Ӯһ�̣���20~40�֣�ÿ����Ϸ���ֽ����ۻ�
     * //     * ����1�����Ӧ�����ͻ��֣�60~120��
     * //     * ϵͳ�ͻ��֣����û�����=0ʱ��ϵͳĬ��ÿ����50�֣�������100�ַⶥ��������
     * <p/>
     * <p/>
     * todo: wulong:���ϵĿ۷��߼����ϣ�������Ϊ׼ 2012.03.31.16.13��
     * �ճ��ط��߼���
     * 1.Ϊ0������£�ÿ����20����
     * 2.ÿ���ճ��ط�15��
     * 3.�û���һ������20��
     * endif
     */

//todo: wulong����appjoy����
    public static final int SCORE_INIT = 20;
    public static int mServerScore = 0;//���������ط���
    public static int mSaveScore = 0;//���͵Ļ��֣��洢�ڱ���
    public static int mDebtScore = 0;//Ƿծ�����طֲ����ۣ���¼��Ƿ���ٷ֣���������ȡ�������󣬴ӷ������۳�
    //ÿ������һ�η�����������Ϊ�㲻�ܾ���ʱ�ͳ�
    public static final String KEY_GAVE_SCORE_EVERYDAY = "game.sxddz.everyday.lastdate";
    public static final int SCORE_EVERYDAY_GIFT = 20;
    public static final int SCORE_RENT = 15;//ÿ���ճ��ط�15��
    //endif


    public static boolean mGettingScoreFlag = false;//���ڴӷ�������ȡ���֣�������ع���������޷��ػ�������
    // �ܻ���
    public static final String KEY_GAME_SCORE = "game.sxddz.score";
    //    // ÿ�����͵Ļ��֣������һ�����Ϳ�ʼ���ۼƻ��֣�
//    public static final String KEY_GAME_GIVE_SCORE               = "game.sxddz.give_score";
    // ���һ�����ͻ��ֵ�����
    public static final String KEY_GAME_GIVE_DATE = "game.sxddz.give_date";
    // �����Ƿ������־
    public static final String KEY_GAME_AVATARINDEX_UNLOCKED_SET = "game.sxddz.unlocked_avatarindex_set";

    //
    public static boolean theIsGetScoreDialogShown = false;

    // ��ɫ����
    public static String[] avatarNameArray = {
            "ʥ����", // 0
            "��ʿ", // 1
            "����", // 2
            "ʥͽ", // 3
            "��֪", // 4
            "������ʿ", // 5
            "Ԫ��ʹ", // 6
            "����ʦ", // 7
            "ʳ��ħ", // 8
            "����Ȯ" // 9

//            "����", // 0
//            "ţͷ", // 1
//            "����", // 2
//            "����", // 3
//            "����", // 4
//            "ʥĸ", // 5
//            "Ү��", // 6
//            "����", // 7
//            "����", // 8
//            "����", // 9
    };

    // ��Ч
    public static String[] theSkillNameArray = new String[]{
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

    public static final int SKILL_TIMEMILLIS_FRAME_SWITCH = 50;   // ÿ֡�л�ʱ����
    public static final int SKILL_TIMEMILLIS_FRAME_SINGLE = 100;   // ÿ֡��ʾʱ�䳤��
    public static final int SKILL_TIMEMILLIS_FRAME_LAST = 500;   // ���һ֡��ʾʱ�䳤��

    private static final Random theRandom = new Random(System.currentTimeMillis());
    private static Context mContext = null;

    private static final int MSG_EVERYDAY_GIFT = 10;
    private static final int MSG_INIT_SEND_SCORE = 11;
    private static final int MSG_RENT_COST = 12;

    private static final Handler theNotifyHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_EVERYDAY_GIFT: //ÿ�����ͻ���
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

        Log.d(TAG, "unlockAvatar, unlocked avatarIndex set = " + set);

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
        // �Ѿ��������޶���
        // δ������������ع��
        if (!unlocked) {
//todo: wulong�������ǽ
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("��ܰ��ʾ");
            builder.setMessage("������δ����,�����Ե�������տ�������������");
            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    showWall(context);
                    unlockAvatar(context, avatarIndex);
                }
            });

            builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
     * ��ʼ��appjoy�ӿ�
     *
     * @param context
     */
    public static void initAppjoySdk(Context context) {
        Log.d(TAG, "init appjoy sdk...");
        UUAppConnect.getInstance(context).initSdk();
    }

    //��ʾ���ǽ
    public static void showWall(Context context) {
        Log.d(TAG, "showWall");
        UUAppConnect.getInstance(context).showOffers();
    }

    /**
     * �Ͽ�appjoy�ӿ�
     *
     * @param context
     */
    public static void exitAppjoySdk(Context context) {
        UUAppConnect.getInstance(context).exitSdk();
    }


    /**
     * ��ʾ����ǽ����ʾ�û�ȥ���������ȡ����
     *
     * @param context
     */
    public static void showOfferWall(final Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("��ܰ��ʾ");
        builder.setMessage(msg + ",�����Ե�������ȡ���֣�");
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
                showWall(context);
            }
        });

        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
    // ��Ϸ�ڻ�û���
    ////////////////////////////////////////////////////////////////

    // ��ʼ������
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

        //todo: wulong��Ϸ��ʼ��ʱ��Ҫ�ѷ����������뱾�����ͻ����ۼ�
        getScoreFromServer(context);
    }


    private static String getToday() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//��ȡ���
        int month = ca.get(Calendar.MONTH);//��ȡ�·�
        int day = ca.get(Calendar.DATE);//��ȡ��
        String today = (year + ":" + month + ":" + day);
        return today;
    }

    /**
     * ÿ�����ͻ��֣�ÿ������һ�Σ�����Ϊ��ʱ��
     */
    private static void everyDayGiftFunc() {
        Log.i(TAG, "hainotify: everyDayGiftFunc");
        mSaveScore = PreferenceUtil.readRecord(mContext, KEY_GAME_SCORE, 0);
        mSaveScore += SCORE_EVERYDAY_GIFT;
        PreferenceUtil.saveRecord(mContext, KEY_GAME_SCORE, mSaveScore);
//        Toast.makeText(mContext, "��ϲ���ÿ�����ͻ���" + SCORE_EVERYDAY_GIFT + "��", Toast.LENGTH_SHORT);
        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("��ϲ")
                .setMessage("��ϲ�����ÿ�����ͻ���" + SCORE_EVERYDAY_GIFT + "�֣�ף��������!")
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }).create();
        dlg.show();
    }


    /**
     * ÿ����ȡ�����
     */
    private static void spendRentScoreFunc() {
        Log.i(TAG, "hainotify: spendRentScoreFunc");
        lostLevelScore(mContext, SCORE_RENT);
//        Toast.makeText(mContext, "��ȡ���ط�" + SCORE_RENT + "��", Toast.LENGTH_SHORT);
        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("ף������")
                .setMessage("��ȡ�����" + SCORE_RENT + "��")
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }).create();
        dlg.show();
    }


    /**
     * ���ε�½���ͷ���
     */
    private static void firstLoginFunc() {
        Log.i(TAG, "hainotify: firstLoginFunc");
        final AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("��ϲ")
                .setMessage("���ε�½����" + SCORE_INIT + "��")
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }).create();
        dlg.show();
    }

    //ÿ������һ�λ��֣�����Ϊ0ʱ��
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
     * ÿ����ʼ���ճ��ط�
     *
     * @param context
     */
    private static void checkRentScore(final Context context) {
        Log.i(TAG, "hainotify: checkRentScore");
        Message message = new Message();
        message.what = MSG_RENT_COST;
        theNotifyHandler.sendMessage(message);
    }

    // ��Ϸ�Ƿ��ܹ����У�
    public static boolean canRun(final Context context) {
        // ���ֹ�����������
        if (getTotalScore() > 0) {
            return true;
        }

        // ������ʾ�û��������ظ���ʾ
        if (theIsGetScoreDialogShown) {
            Log.d(TAG, "get score dialog is run");
            return false;
        }

        checkNeedAdvertiseDlg(context);
        return false;
    }


    /**
     * ������Ϊ0ʱ������Ϸ���������������һ�η����������ȻΪ0���������Ի���
     * Ϊ�˽�����ع����޷��ص�����
     */
    private static void checkNeedAdvertiseDlg(final Context context) {

        if (mGettingScoreFlag) {
            Log.i(TAG, "checkNeedAdvertiseDlg mGettingScoreFlag = " + mGettingScoreFlag);
            return;
        }

        if (getTotalScore() > 0) {
            return;
        }

        //���û���������ѯ���������ҷ���Ϊ0������Ϊ��ѯ״̬��ֱ���鵽����Ϊֹ�����������Ϊ0��������������
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
//                       todo: wulong���ش洢��ֻ�����ͷ�������Ϸ�в����ķ������ڷ�����
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
     * ��ʾ�Ի��򣬰�ȷ�ϼ���ת��������
     */
    private static void showClickAdDlg(final Context context) {
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
                        mGettingScoreFlag = true;
                        // �رյ�ǰ�Ի���
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

    // �������ȡ���֡�
    public static void clickGetScore(final Context context) {
//todo: wulong��û���
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
//                       todo: wulong���ش洢��ֻ�����ͷ�������Ϸ�в����ķ������ڷ�����
                        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
                        mServerScore = i;

                        if (mDebtScore > 0) {
                            //���ز����ۣ��ӷ�����ȡ����������ͬ����ʲôʱ��ȡ��ʲôʱ���
                            mServerScore -= mDebtScore;
                            lostLevelScore(context, mDebtScore);
                        }

                        Log.i(TAG, "hainotify: getScoreFromServer onSuccess mSaveScore = " + mSaveScore + "  i = " + i + " mDebtScore = " + mDebtScore);
                        mGettingScoreFlag = false;
                    }
                }
        );
    }

    // ��û�����ֵ
    public static int getTotalScore() {
//todo: wulong���ͷ������˾Ͳ���,��ʵʱ���£�Ҳ��Ҫ������
        return (mServerScore + mSaveScore);
    }

    // ��û�����ֵ�ַ���
    public static String getTotalScoreAsString(final Context context) {
//todo: wulong���ͷ������˾Ͳ��ܱ��ط�
        return String.valueOf(getTotalScore());
    }

    // ��á�����x�����ַ���
    public static String getTotalScoreValueAndString(final Context context) {
        return getTotalScore() + "����";
    }

    // ���ݽ������һ�ֽ������߼�
    public static void processLevelResult(final Context context, final int leftPaiCount, final int point) {
        Log.i(TAG, "processLevelResult leftPaiCount = " + leftPaiCount + " point = " + point);
//todo: wulong:appjoyӮ��point�������������Ǹ���
        if (point < 0) {
            lostLevelScore(context, (-point));
        } else {
            winLevelScore(context, point);
        }
    }

    // ����ʧ��
    public static void lostLevelScore(final Context context, final int point) {
//todo: wulong��ʼ����100�֣����ػ���ֻ�۲��ӣ�������д
//todo: wulong:appjoy��ȥ����

        if (point <= 0) {
            return;
        }

        mSaveScore = PreferenceUtil.readRecord(context, KEY_GAME_SCORE, 0);
        Log.i(TAG, "lostLevelScore, mSaveScore = " + mSaveScore + " point = " + point + " mServerScore = " + mServerScore);

        int lostScore = point;

//todo: wulong:appjoy�ȿ۱��ط����������ٿ۷���������
        if (lostScore > mSaveScore) {
            mDebtScore = lostScore - mSaveScore;//û�۸ɾ�����¼һ�´ӷ�������ȡ��ʱ��۳�
            mSaveScore = 0;
            lostScore = mServerScore;//�۵ķ��������������������������Ͳ���۵�
            mServerScore -= mDebtScore;
            if (mServerScore < 0) {
                mServerScore = 0;
            } else {
                lostScore = mDebtScore;
            }
//todo: wulong:appjoy���ز����Ϳ۷�����
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

    // ����ʤ��
    public static void winLevelScore(final Context context, final int point) {
//todo: wulong��÷������洢�ڱ��أ������ϴ�������
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
    // ������Ч
    ////////////////////////////////////////////////////////////////

    // �¿�һ�ֳ�ʼ��������2����ɫ����������
    public static void newLevel(Context context) {

        setContext(context);

        Log.i(TAG, "hainotify: newLevel");
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
                        ", skillACount = " + skillACount + ", skillBCount = " + skillBCount + ", skillCCount = " + skillCCount);

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
