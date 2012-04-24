package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.nd.dianjin.DianJinPlatform;
import com.nd.dianjin.OfferBanner;
import com.nd.dianjin.utility.BannerColorConfig;
import com.nd.dianjin.webservice.WebServiceListener;
import df.util.android.*;
import df.util.type.FileUtil;
import df.util.type.NumberUtil;
import df.util.type.StringUtil;
import df.util.type.TimeUtil;

import java.lang.ref.SoftReference;
import java.util.*;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: ����4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMylbhDianjin {
    private static final String TAG = "df.util.app.DooyogameMylbhDianjin";
    private final static String USER_POINTS_KEY = "user_points";
    private final static String IS_HAD_SPEND = "is_had_spend";
    private final static String IS_NEED_SPEND = "is_need_spend";
    private final static String IS_HAD_REQUEST = "is_had_request";
    private static Context context;

    // ���Ѷ�������,������Ϸ
    public static final int ACTION_ID_1001 = 1001;


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
     * ������ʱ���ʼ�� dianjin sdk
     *
     * @param ct
     */
    public static void initDianjinSdk(Context ct) {
        context = ct;
        DianJinPlatform.initialize(context, 2161, "e721bdc60c8de284b8b9d425e57a805f");

        // ��ʼ���û��Ļ���
        getUserPoints();
    }

    /**
     * ��ʾ�û�������Ϸ
     *
     * @param ct
     */
    public static void warnUserSpendPointsDialog(final Context ct) {
        context = ct;

        // �жϼ�����Ϸ�������Ƿ��Ѿ��ύ
        boolean is_had_request = PreferenceUtil.readRecord(context, IS_HAD_REQUEST, false);
        if (is_had_request) {
            Toast.makeText(context, "������Ϸ�������Ѿ��ύ�����Ժ����ԡ�", Toast.LENGTH_SHORT).show();
            return;
        }

        // �������ѱ�ʶ
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final float spendPoints = 100;
        float userPoints = getUserPointsFromLocal();
        Log.d(TAG, "warnUserSpendPointsDialog, userPoints = " + userPoints);
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("��ܰ��ʾ");
        if (userPoints >= spendPoints) {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��M�ң�����ǰӵ��" +
                    userPoints + "��M�ң����Ƿ�Ҫ������";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    spendUserPoints(spendPoints);
                }
            });

        } else {
            alertMsg = "����Ҫ�ȼ������Ϸ�����ü������Ϸ��Ҫ������" + spendPoints + "��M�ң�����ǰӵ��" +
                    userPoints + "��M�ң�M�Ҳ��㣬������ͨ�����ȷ����ȡM�ҡ�";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("ȷ��", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // ��ʾ���ǽ
                    showDianjinOffers();
                }
            });
        }
        builder.setNegativeButton("ȡ��", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    /**
     * ��� dianjin �Ļ������
     *
     * @param activity
     */
    public static void addDianjinAdView(final Activity activity) {
        context = activity;

        // �����������
//        RelativeLayout adArea = new RelativeLayout(context);
//        RelativeLayout.LayoutParams adAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        adAreaParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // ���� dianjin �����������
        LinearLayout adDianjinArea = new LinearLayout(context);
        RelativeLayout.LayoutParams adDianjinAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        adDianjinAreaParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adDianjinArea.setGravity(LinearLayout.VERTICAL);

        BannerColorConfig colorConfig = new BannerColorConfig();
        colorConfig.setBackgroundColor(Color.BLUE);
        colorConfig.setDetailColor(0xFFFF3300);
        colorConfig.setNameColor(0xFFFF3300);
        colorConfig.setRewardColor(0xFFFF3300);
        OfferBanner bannerLayout = new OfferBanner(context, 2160,
                "eaa9cd743713ee4ea52976aaa64473a4", 6000,
                OfferBanner.AnimationType.ANIMATION_PUSHUP, colorConfig);
        adDianjinArea.addView(bannerLayout);

//        adArea.addView(adDianjinArea, adDianjinAreaParams);

        activity.addContentView(adDianjinArea, adDianjinAreaParams);
    }

    /**
     * �ж��û��Ƿ��Ѿ�������Ϸ
     *
     * @param context
     * @return
     */
    public static boolean isHadSpend(Context context) {
        // �Ƿ��Ѿ�����
        boolean is_had_spend = PreferenceUtil.readRecord(context, IS_HAD_SPEND, false);
        if (is_had_spend) {
            PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
        }

        Log.d(TAG, "isHadSpend ,is_had_spend = " + is_had_spend);
        return is_had_spend;
    }

    /**
     * ��Ϸ�Ƿ���Ҫ����
     *
     * @param context
     * @return
     */
    public static boolean isNeedSpend(Context context) {
        boolean is_need_spend = PreferenceUtil.readRecord(context, IS_NEED_SPEND, false);
        Log.d(TAG, "isNeedSpend,is_need_spend = " + is_need_spend);
        return is_need_spend;
    }


    /**
     * ��ʾ���ǽ
     */
    private static void showDianjinOffers() {
        DianJinPlatform.showOfferWall(context, DianJinPlatform.Oriention.PORTRAIT);
    }

    /**
     * �ӷ�������ȡ��ȡ�û��Ļ���
     */
    private static void getUserPoints() {
        DianJinPlatform.getBalance(context, new WebServiceListener<Float>() {
            @Override
            public void onResponse(int respCode, Float balance) {
                if (respCode == DianJinPlatform.DIANJIN_SUCCESS) {
                    saveUserPoints(balance);
                    Log.d(TAG, "getUserPoints, userPoints = " + balance);
                }
            }
        });
    }

    /**
     * �����û��Ļ���
     *
     * @param spendPoint
     */
    public static void spendUserPoints(float spendPoint) {
        // ��ʶ������Ϸ�������Ѿ��ύ
        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, true);

        // ������
        String orderSerial = TimeUtil.toLongOfyyyyMMddHHmmss() + TelephoneUtil.toImei(context);
        // ���Ѷ�������
        int actionId = ACTION_ID_1001;
        DianJinPlatform.consume(context, orderSerial, spendPoint, actionId, new WebServiceListener<Integer>() {
            @Override
            public void onResponse(int responseCode, Integer balance) {
                switch (responseCode) {
                    case DianJinPlatform.DIANJIN_SUCCESS:
                        Toast.makeText(context, "���ѳɹ�,��Ϸ�Ѿ�������������½�����Ϸ��", Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
                        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
                        saveUserPoints(balance);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_REQUES_CONSUNE:
                        Toast.makeText(context, "֧������ʧ��",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:
                        Toast.makeText(context, "M�Ҳ���",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ACCOUNT_NO_EXIST:
                        Toast.makeText(context, "�˺Ų�����",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_ORDER_SERIAL_REPEAT:
                        Toast.makeText(context, "�������ظ�",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_ERROR_BEYOND_LARGEST_AMOUNT:
                        Toast.makeText(context,
                                "һ���Խ��׽�������޶����",
                                Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    case DianJinPlatform.DIANJIN_RETURN_CONSUME_ID_NO_EXIST:
                        Toast.makeText(context,
                                "�����ڸ����͵����Ѷ���ID", Toast.LENGTH_SHORT)
                                .show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                        break;
                    default:
                        Toast.makeText(context, "δ֪����,������Ϊ��" + responseCode, Toast.LENGTH_SHORT).show();
                        PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);
                }
            }
        });

    }


    /**
     * ���ػ�ȡ�û�����
     *
     * @return
     */
    private static float getUserPointsFromLocal() {
        getUserPoints();
        return PreferenceUtil.readRecord(context, USER_POINTS_KEY, 0f);
    }


    private static void saveUserPoints(float points) {
        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, points);
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
