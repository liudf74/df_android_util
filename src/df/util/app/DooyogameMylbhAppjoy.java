package df.util.app;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-13
 * Time: ����4:55
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.uucun.adsdk.UUAppConnect;
import com.uucun.adsdk.UpdatePointListener;
import df.util.android.MusicUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.android.SoundUtil;
import df.util.type.FileUtil;
import df.util.type.NumberUtil;
import df.util.type.StringUtil;

import java.lang.ref.SoftReference;
import java.util.*;

/**
 * dooyogame���������ʻ���appjoy�汾��ӡ�ӹŵ���
 */
public class DooyogameMylbhAppjoy {

    public static final String TAG = "df.util.DooyogameMylbhAppjoy";

    private final static String USER_POINTS_KEY = "user_points";
    private final static String USER_POINT_UNIT_KEY = "point_unit";
    private final static String IS_HAD_SPEND = "is_had_spend";
    private final static String IS_NEED_SPEND = "is_need_spend";
    private final static String IS_HAD_REQUESET = "is_had_request";

    // ��Ч
    public static String[] theFailNameArray = new String[]{
            "effect_fail_4x1",
    };
    public static BitmapDrawable[][] theFailDrawableArray = new BitmapDrawable[theFailNameArray.length][];
    //
    public static String[] theSuccessNameArray = new String[]{
            "effect_success_4x2",
    };
    public static BitmapDrawable[][] theSuccessDrawableArray = new BitmapDrawable[theSuccessNameArray.length][];
    //
    public static String[] theAvatarNameArray = new String[]{
            "text_ghost_1",
            "text_ghost_2",
            "text_ghost_3",
            "text_ghost_4",
            "text_ghost_51",
            "text_ghost_6",
            "text_ghost_7",
            "text_ghost_8",
            "text_ghost_9",
            "text_ghost_10",
            "text_ghost_11",
            "text_man_1",
            "text_man_2",
            "text_man_3",
            "text_man_4",
            "text_man_5",
            "text_man_6",
    };
    public static int theLastAvatarResourceId = -1;
    public static int theLastLevel = -1;
    public static SoftReference<Bitmap> theAvatarLockReference;
    //    public static HashMap<String,Boolean> theAvatarNameAvaliableMap = new HashMap<String, Boolean>();
    public static ArrayList<String> theAvatarNameAvaliableList = new ArrayList<String>();

    public static String theDefaultAvatarName = "text_success";

    public static String theBackgroundMusicName = "music/bg.mp3";
    public static String theEffectSuccessMusicName = "music/effect_success.mp3";
    public static String theLevelSuccessMusicName = "music/level_success.mp3";


    public static boolean theRedrawSkill = false;

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

    // level�ļ��У���300*400��Ϊ�ֱ���
//    public static int ADJUST_MIN_X = 270;
//    public static int ADJUST_MIN_Y = 370;
    public static int ADJUST_MIN_X;
    public static int ADJUST_MIN_Y;
    public static final HashMap<Integer, Integer> thePositionOriginalToAdjustMap = new HashMap<Integer, Integer>();

    /**
     * ������ʱ���ʼ��appjoy sdk
     *
     * @param context
     */
    public static void initAppjoySdk(final Context context) {
        UUAppConnect.getInstance(context).initSdk();
        // ��ʼ���û�����
        getUserPionts(context);
    }

    /**
     * ��ʾ�û�������Ϸ
     *
     * @param context
     */
    public static void warnUserSpendPointsDialog(final Context context) {
        // �ж������Ƿ��Ѿ��ύ
        Boolean isHadRequest = PreferenceUtil.readRecord(context, IS_HAD_REQUESET, false);
        if (isHadRequest) {
            Toast.makeText(context, "������Ϸ�������Ѿ��ύ�����Ժ����ԡ�", Toast.LENGTH_LONG).show();
            return;
        }

        // �������ѱ�ʶ
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final int spendPoints = 100;
        int userPonts = getUserPointsFromLocal(context);
        String pointUnit = getUserPointUnit(context);
        Log.d(TAG, "warnUserSpendPointsDialog, userPoints = " + userPonts);
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("��ܰ��ʾ");
        if (userPonts >= spendPoints) {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��" + pointUnit + "������ǰӵ��" +
                    userPonts + "��" + pointUnit + "�����Ƿ�Ҫ������";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    spendUserPoints(context, spendPoints);
                }
            });

        } else {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��" + pointUnit + "������ǰӵ��" +
                    userPonts + "��" + pointUnit + "��" + pointUnit + "���㣬������ͨ�����ȷ����ȡ" + pointUnit + "��";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // ��ʾ���ǽ
                    showAppjoyOffers(context);
                }
            });
        }
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    /**
     * ʹ�û���
     *
     * @param context
     * @param spendPoints
     */
    private static void spendUserPoints(final Context context, int spendPoints) {
        // ���������ύ��ʶ
        PreferenceUtil.saveRecord(context, IS_HAD_REQUESET, true);

        // ֱ�����Ľ��
        UUAppConnect.getInstance(context).spendPoints(spendPoints,
                new UpdatePointListener() {
                    public void onSuccess(String unit, int total) {
                        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, total);
                        PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
                        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
                        Toast.makeText(context, "���ѳɹ�,���������½�����Ϸ��ף����Ϸ��졣", Toast.LENGTH_LONG).show();
                    }

                    public void onError(String msg) {
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUESET, false);
                    }
                });
    }

    /**
     * �ж��û��Ƿ��Ѿ�����
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
     * �ж��Ƿ���Ҫ����
     */
    public static boolean isNeedSpend(Context context) {
        return PreferenceUtil.readRecord(context, IS_NEED_SPEND, false);
    }


    /**
     * ���ػ�ȡ�û�����
     *
     * @param context
     * @return
     */
    private static int getUserPointsFromLocal(Context context) {
        // �ӷ����������û��Ļ���
        getUserPionts(context);
        int userPoints;
        userPoints = PreferenceUtil.readRecord(context, USER_POINTS_KEY, 0);
        return userPoints;
    }

    /**
     * �ӷ�������ȡ�û��Ļ���
     *
     * @param context
     */
    private static void getUserPionts(final Context context) {
        UUAppConnect.getInstance(context).getPoints(
                new UpdatePointListener() {
                    @Override
                    public void onError(String s) {
                        // nothing to do ...
                    }

                    @Override
                    public void onSuccess(String pointUnit, int userPointTotal) {
                        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, userPointTotal);
                        PreferenceUtil.saveRecord(context, USER_POINT_UNIT_KEY, pointUnit);
                    }
                }
        );
    }

    /**
     * ��ʾ���ǽ
     *
     * @param context
     */
    private static void showAppjoyOffers(Context context) {
        UUAppConnect.getInstance(context).showOffers();
    }


    /**
     * ��ȡ������ҵĵ�λ
     *
     * @param context
     * @return
     */
    private static String getUserPointUnit(Context context) {
        String pointUnit;
        pointUnit = PreferenceUtil.readRecord(context, USER_POINT_UNIT_KEY, "���");
        return pointUnit;
    }


    ////////////////////////////////////////////////////////////////
    // ������Ч
    ////////////////////////////////////////////////////////////////

    public static void exitGame(Context context) {
        MusicUtil.stopBackgroundMusic();
    }


    // �¿���Ϸ��ʼ��
    public static void newGame(Context context) {
        // ����ͼƬ
        recycleBitmap(theFailDrawableArray);
        recycleBitmap(theSuccessDrawableArray);

        // ��ʼ��ͼƬ
        initSkillDrawableArray(context, theFailNameArray, theFailDrawableArray);
        initSkillDrawableArray(context, theSuccessNameArray, theSuccessDrawableArray);

        // ��ʼ�����ݣ�ȥ���Ѿ���ʾ��������
        for (int i = 0; i < theAvatarNameArray.length; i++) {
            String name = theAvatarNameArray[i];
            theAvatarNameAvaliableList.add(name);
        }
        ArrayList<String> list = new ArrayList<String>();
        for (int level = 0; level < 200; level++) {
            String name = PreferenceUtil.readRecord(context, toPreferenceKeyOfLevelAvatarName(level), "");
            if (!StringUtil.empty(name)) {
                list.add(name);
            }
        }
        theAvatarNameAvaliableList.removeAll(list);

        // ��������
        playBackgroundMusic(context);

        // ��ʼ�¾�
        newLevel(context, 0);
    }

    // �¿�һ�ֳ�ʼ��
    public static void newLevel(Context context, int currLevel) {
        theRedrawSkill = true;
//        adjustLevel(context, currLevel);
    }

    public static void adjustLevel(Context context, int currLevel) {

        Activity a = (Activity) context;
        int screenWidth = a.getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = a.getWindowManager().getDefaultDisplay().getHeight();
        Log.d(TAG, "adjustLevel,screenWidth = " + screenWidth + " screenHeight = " + screenHeight);

        ADJUST_MIN_X = (int) (screenWidth * 0.8); // 400; // screenWidth;
        ADJUST_MIN_Y = (int) (screenHeight * 0.6); // 600; // screenHeight;
        Log.d(TAG, "adjustLevel,ADJUST_MIN_X = " + ADJUST_MIN_X + " ADJUST_MIN_Y = " + ADJUST_MIN_Y);


        thePositionOriginalToAdjustMap.clear();

        //
        /**
         * level�ļ���ʽ����
         300 400
         1 80 160
         2 160 240
         3 160 160
         4 160 80
         5 240 240
         6 240 160
         2 5
         5 6
         6 3
         3 2
         2 6
         6 4
         4 1
         1 2
         2 5 6 3 2 6 4 1 2
         */
        try {
            // ��õ�ǰλ��
            int maxX = 0, maxY = 0;
            String levelFilePathName = "level" + currLevel + ".txt";
            byte[] byteArray = FileUtil.readAssetFile(levelFilePathName, context.getAssets());
            String content = new String(byteArray);
            String[] lineArray = StringUtil.split2(content, "\n");
            for (int i = 0; i < lineArray.length; i++) {
                String line = lineArray[i];
                String[] posArray = StringUtil.split2(line, " ");
                if (posArray.length == 3) {
                    int x = NumberUtil.toInt(posArray[1]);
                    int y = NumberUtil.toInt(posArray[2]);
                    Log.d(TAG, "adjustLevel,x = " + x + " y = " + y);
                    thePositionOriginalToAdjustMap.put(toAdjustKey(x, y), toAdjustKey(x, y));
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                    Log.d(TAG, "adjustLevel,maxX = " + maxX + " maxY = " + maxY);
                }
            }
            Log.d(TAG, "origna map = " + thePositionOriginalToAdjustMap);

            // ����λ�ã������ֻ��ķֱ��ʵ���
            // ���� X �����λ��
            setPositionForX(maxX);
            // ���� Y �����λ��
            setPositionForY(maxY);
            Log.d(TAG, "adjust map = " + thePositionOriginalToAdjustMap);

        } catch (Exception e) {
            Log.e(TAG, "open level file failure", e);
        }
    }

    private static void setPositionForY(int maxY) {
        float aspect = ADJUST_MIN_Y * 1.0f / maxY;
        Log.d(TAG, "y aspect = " + aspect);
        for (Iterator<Map.Entry<Integer, Integer>> it = thePositionOriginalToAdjustMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Integer> entry = it.next();
            int key = entry.getKey();
            int val = entry.getValue();
            int oldX = toAdjustX(val);
            int oldY = toAdjustY(val);
            int newY = (int) (oldY * aspect);
            int value = toAdjustKey(oldX, newY);
            thePositionOriginalToAdjustMap.put(key, value);
            Log.d(TAG, "y, key = " + key + ", value = " + value);
        }
    }

    private static void setPositionForX(int maxX) {
        float aspect = ADJUST_MIN_X * 1.0f / maxX;
        Log.d(TAG, "x aspect = " + aspect);
        for (Iterator<Map.Entry<Integer, Integer>> it = thePositionOriginalToAdjustMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Integer> entry = it.next();
            int key = entry.getKey();
            int val = entry.getValue();
            int oldX = toAdjustX(val);
            int oldY = toAdjustY(val);
            int newX = (int) (oldX * aspect);
            int value = toAdjustKey(newX, oldY);
            thePositionOriginalToAdjustMap.put(key, value);
            Log.d(TAG, "x, key = " + key + ", value = " + value);
        }
    }

    public static float adjustX(float x, float y) {
        final int key = toAdjustKey(x, y);
        final Integer value = thePositionOriginalToAdjustMap.get(key);
        final int result = (int) ((value != null) ? toAdjustX(value) : x);
        Log.d(TAG, "adjustX, key = " + key + ", value = " + value + ", result = " + result);
        return result;
    }

    public static float adjustY(float x, float y) {
        final int key = toAdjustKey(x, y);
        final Integer value = thePositionOriginalToAdjustMap.get(key);
        final int result = (int) ((value != null) ? toAdjustY(value) : y);
        Log.d(TAG, "adjustY, key = " + key + ", value = " + value + ", result = " + result);
        return result;
    }

    private static int toAdjustKey(float x, float y) {
        return (int) (x * 1000 + y);
    }

    private static int toAdjustX(int key) {
        return key / 1000;
    }

    private static int toAdjustY(Integer value) {
        return value % 1000;
    }

    // ���ű�������
    public static void playBackgroundMusic(Context context) {
        MusicUtil.playBackgroundMusic(context, theBackgroundMusicName, true);
    }

    // ��������ͼƬ
    public static int toAvatarDrawableId(Context context, int currLevel) {

        // ���عؿ���Ӧ������
        String name = PreferenceUtil.readRecord(context, toPreferenceKeyOfLevelAvatarName(currLevel), "");
        if (!StringUtil.empty(name)) {
            return ResourceUtil.getDrawableResourceIdFromName(context, name);
        }

        // �������ﶼ��ʾ���ʱ����ʾȱʡ����
        if (theAvatarNameAvaliableList.size() <= 0) {
            return ResourceUtil.getDrawableResourceIdFromName(context, theDefaultAvatarName);
        }

        int index = theRandom.nextInt(theAvatarNameAvaliableList.size());
        name = theAvatarNameAvaliableList.get(index);
        theAvatarNameAvaliableList.remove(index);
        PreferenceUtil.saveRecord(context, toPreferenceKeyOfLevelAvatarName(currLevel), name);
        Log.d(TAG, "avatar name  = " + name);

        return ResourceUtil.getDrawableResourceIdFromName(context, name);
    }

    private static String toPreferenceKeyOfLevelAvatarName(int currLevel) {
        return "mylbh.avatar.name.level." + currLevel;
    }

    // ����
    public static void debugPrintLevel(int[][] matrix, int nodeCount, List nodes) {
        Log.d(TAG, "nodeCount = " + nodeCount);
        Log.d(TAG, "nodes = " + nodes);
        StringBuffer sb = new StringBuffer("\\");
        for (int i = 0; i < matrix.length; i++) {
            sb.append("[");
            int[] inner = matrix[i];
            for (int j = 0; j < inner.length; j++) {
                int v = inner[j];
                sb.append(v).append(",");
            }
            sb.append("]").append("\\");
        }
        Log.d(TAG, "matrix = " + sb.toString());
    }

    // ��Ϸ���棬���Ƽ�����Ч
    public static void drawSkillForFail(Context context, View view, Canvas canvas) {
        drawSkill(context, view, canvas, theFailNameArray, theFailDrawableArray, false);
    }

    public static void drawSkillForSuccess(Context context, View view, Canvas canvas, int currLevel) {

        // ��Ч����
        if (currLevel % 5 == 0) {
            Log.d(TAG, "show avatar, currLevel = " + currLevel);
            // �������� ͬһ��ʼ����ʾ��ͬ����
            if (theRedrawSkill && (theLastLevel != currLevel)) {
                Log.d(TAG, "create a new avatar");
                theLastAvatarResourceId = -1;
                theLastLevel = currLevel;
            }
            if (theLastAvatarResourceId < 0) {
                theLastAvatarResourceId = toAvatarDrawableId(context, currLevel);
                theAvatarLockReference = null;
            }
            Bitmap bm = null;
            if (theAvatarLockReference == null) {
                bm = BitmapFactory.decodeResource(context.getResources(), theLastAvatarResourceId);
                theAvatarLockReference = new SoftReference<Bitmap>(bm);
            }
            bm = theAvatarLockReference.get();
            if (bm != null) {
                Rect dstRect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
                canvas.drawBitmap(bm, null, dstRect, new Paint());
            }

            // ������Ч
            drawSkill(context, view, canvas, theSuccessNameArray, theSuccessDrawableArray, true);

            // ����
            SoundUtil.playEffect(context, theEffectSuccessMusicName);
        } else {

            // ����
            SoundUtil.playEffect(context, theLevelSuccessMusicName);
        }

    }

    // ��Ϸ���棬���Ƽ�����Ч
    public static void drawSkill(Context context, View view, Canvas canvas,
                                 final String[] paramNameArray, final BitmapDrawable[][] paramDrawArray,
                                 boolean isDrawOnScreenLeftTop) {
        Log.d(TAG, "drawSkill");

        int screenW = view.getWidth();
        int screenH = view.getHeight();
        Log.d(TAG, "drawSkill,screenW = " + screenW + ",screenH = " + screenH);

        int skillACount = 1, skillBCount = 0, skillCCount = 0;

        // ����л����ƣ������»���
        if (theRedrawSkill) {
            theRedrawSkill = false;
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
                Log.d(TAG, "skillACount = " + skillACount + ", skillBCount = " + skillBCount + ", skillCCount = " + skillCCount);

                // ����ͼƬ
                int maxSkillDrawableArrayCount = 0;
                // ����A
                if (theLastDrawSkillA == -1) {
                    theLastDrawSkillA = theRandom.nextInt(paramNameArray.length);
                }
                BitmapDrawable[] skillADrawableArray = findSkillDrawableArray(
                        context, paramNameArray, paramDrawArray, theLastDrawSkillA);
                assert (skillADrawableArray != null);
                if (maxSkillDrawableArrayCount < skillADrawableArray.length) {
                    maxSkillDrawableArrayCount = skillADrawableArray.length;
                }
                // ����B
                if (theLastDrawSkillB == -1) {
                    theLastDrawSkillB = theRandom.nextInt(paramNameArray.length);
                }
                BitmapDrawable[] skillBDrawableArray = findSkillDrawableArray(
                        context, paramNameArray, paramDrawArray, theLastDrawSkillB);
                assert (skillBDrawableArray != null);
                if (maxSkillDrawableArrayCount < skillBDrawableArray.length) {
                    maxSkillDrawableArrayCount = skillBDrawableArray.length;
                }
                // ����C
                if (theLastDrawSkillC == -1) {
                    theLastDrawSkillC = theRandom.nextInt(paramNameArray.length);
                }
                BitmapDrawable[] skillCDrawableArray = findSkillDrawableArray(
                        context, paramNameArray, paramDrawArray, theLastDrawSkillC);
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
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillADrawableArray, theLastDrawFrame, skillACount, isDrawOnScreenLeftTop);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillBDrawableArray, theLastDrawFrame, skillBCount, isDrawOnScreenLeftTop);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillCDrawableArray, theLastDrawFrame, skillCCount, isDrawOnScreenLeftTop);

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

        // �������ƣ�����
        if (!theLastDrawFrameFinished) {
            view.invalidate();
        }

    }


    private static void drawSkillOfAvatarOfSkill(Canvas canvas, int screenW, int screenH,
                                                 BitmapDrawable[] skillDrawableArray,
                                                 final int frame,
                                                 int skillCount,
                                                 boolean isDrawOnScreenLeftTop) {
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
                Rect bound;
                if (isDrawOnScreenLeftTop) {
                    bound = new Rect(0, 0, frameDrawable.getIntrinsicWidth(), frameDrawable.getIntrinsicHeight());
                } else {
                    bound = toBoundOfScreenCenter(screenW, screenH, startLeft, startTop);
                }
                frameDrawable.setBounds(bound);
                frameDrawable.draw(canvas);
            }
        }
    }

    private static Rect toBoundOfScreenCenter(int screenW, int screenH, int startLeft, int startTop) {
        final int left = (int) (screenW * 0.25) + startLeft;
        final int top = (int) (screenH * 0.25) + startTop;
        final int right = (int) (screenW * 0.75) + startLeft;
        final int bottom = (int) (screenH * 0.75) + startTop;
        return new Rect(left, top, right, bottom);
    }

    private static void initSkillDrawableArray(Context context, final String[] paramNameArray, final BitmapDrawable[][] paramDrawArray) {
        for (int skill = 0; skill < paramNameArray.length; skill++) {
            BitmapDrawable[] skillSingleDrawableArray = findSkillDrawableArray(
                    context, paramNameArray, paramDrawArray, skill);
            assert (skillSingleDrawableArray != null);
        }
    }

    private static BitmapDrawable[] findSkillDrawableArray(Context context,
                                                           final String[] paramNameArray,
                                                           final BitmapDrawable[][] paramDrawArray,
                                                           final int skill) {
        assert ((skill >= 0) && (skill < paramNameArray.length));
        BitmapDrawable[] frameDrawableArray = paramDrawArray[skill];
        if (null == frameDrawableArray) {
            String skillName = paramNameArray[skill];
            if (StringUtil.empty(skillName)) {
                Log.d(TAG, "name is empty, index = " + skill);
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
    private static void recycleBitmap(final BitmapDrawable[][] paramDrawableArray) {
        Log.d(TAG, "recycle bitmap");
        for (int i = 0; i < paramDrawableArray.length; i++) {
            BitmapDrawable[] drawableArray = paramDrawableArray[i];
            if (drawableArray != null) {
                for (int j = 0; j < drawableArray.length; j++) {
                    BitmapDrawable drawable = drawableArray[j];
                    if (drawable != null) {
                        drawable.getBitmap().recycle();
                        drawableArray[j] = null;
                    }
                }
                paramDrawableArray[i] = null;
            }
        }
    }


}