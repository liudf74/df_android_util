package df.util.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;
import df.util.android.MusicUtil;
import df.util.android.PreferenceUtil;
import df.util.android.ResourceUtil;
import df.util.android.SoundUtil;
import df.util.type.FileUtil;
import df.util.type.NumberUtil;
import df.util.type.StringUtil;

import java.lang.ref.SoftReference;
import java.util.*;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-4-5
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class DooyogameMylbhWaps {
    private static final String TAG = "df.util.app.DooyogameWmmtWaps";
    private final static String USER_POINTS_KEY = "user_points";
    private final static String USER_POINT_UNIT_KEY = "point_unit";
    private final static String IS_HAD_SPEND = "is_had_spend";
    private final static String IS_NEED_SPEND = "is_need_spend";
    private final static String IS_HAD_REQUEST = "is_had_request";
    private final static String IS_HAD_SPEND_CLOSE_AD = "is_had_spend_close_ad";
    private static LinearLayout adWapsArea;
    private static ImageButton imageButton;
    private static RelativeLayout adArea;
    private static GetUserPoints getUserPoints = new GetUserPoints();
    private static SpendUserPoints spendUserPoints = new SpendUserPoints();
    private static Context context;
    private static Handler updatePointsHandler = new Handler();
    private static Handler closeAdHandler;
    private final static Timer timer = new Timer();


    // 特效
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
    public static final int SKILL_TIMEMILLIS_FRAME_SWITCH = 50;   // 每帧切换时间间隔
    public static final int SKILL_TIMEMILLIS_FRAME_SINGLE = 100;   // 每帧显示时间长度
    public static final int SKILL_TIMEMILLIS_FRAME_LAST = 500;   // 最后一帧显示时间长度
    private static final Random theRandom = new Random(System.currentTimeMillis());
    // level文件中，以300*400作为分辨率
    // public static int ADJUST_MIN_X = 270;
    // public static int ADJUST_MIN_Y = 370;
    public static int ADJUST_MIN_X;
    public static int ADJUST_MIN_Y;
    public static final HashMap<Integer, Integer> thePositionOriginalToAdjustMap = new HashMap<Integer, Integer>();

    // 控制广告条关闭按钮的 Handler
    final static Handler buttomHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);    //To change body of overridden methods use File | Settings | File Templates.
            int w = msg.what;
            switch (w) {
                case 0:
                    imageButton.setVisibility(View.GONE);
                    break;
                case 1:
                    imageButton.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };


    /**
     * 启动的时候初始化 waps sdk
     *
     * @param ct
     */
    public static void initWapsSdk(Context ct) {
        context = ct;
        AppConnect.getInstance(context);

        // 初始化用户积分
        getUserPoints.getUserPoints();
    }

    /**
     * 提示用户激活游戏
     *
     * @param ct
     */
    public static void warnUserSpendPointsDialog(final Context ct) {
        context = ct;

        // 判断激活游戏的请求是否已经提交
        boolean is_had_request = PreferenceUtil.readRecord(context, IS_HAD_REQUEST, false);
        if (is_had_request) {
            Toast.makeText(context, "激活游戏的请求已经提交，请稍后再试。", Toast.LENGTH_SHORT).show();
            return;
        }

        // 添加消费标识
        PreferenceUtil.saveRecord(context, IS_NEED_SPEND, true);

        final int spendPoints = 100;
        int userPonts = getUserPointsFromLocal();
        String pointUnit = getUserPointUnit();
        Log.d(TAG, "warnUserSpendPointsDialog, userPoints = " + userPonts);
        String alertMsg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        if (userPonts >= spendPoints) {
            alertMsg = "您需要先激活该游戏，永久激活该游戏需要消耗您" + spendPoints + "个" + pointUnit + "，您当前拥有" +
                    userPonts + "个" + pointUnit + "，您是否要继续？";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    spendUserPoints.spendUserPoints(spendPoints);
                }
            });

        } else {
            alertMsg = "您需要先激活该游戏，永久激活该游戏需要消耗您" + spendPoints + "个" + pointUnit + "，您当前拥有" +
                    userPonts + "个" + pointUnit + "，" + pointUnit + "不足，您可以通过点击确定获取" + pointUnit + "。";
            builder.setMessage(alertMsg);
            builder.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 显示广告墙
                    showWapsOffers();
                }
            });
        }
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    /**
     * 添加 waps 的互动广告
     *
     * @param activity
     */
    public static void addWapsAdView(final Activity activity) {
        context = (Context) activity;

        // 判断用户是否已经消费关闭互动广告
        boolean is_had_spend_close_ad = PreferenceUtil.readRecord(context, IS_HAD_SPEND_CLOSE_AD, false);
        if (is_had_spend_close_ad) {
            return;
        }

        // 新增广告区域
        adArea = new RelativeLayout(context);
        RelativeLayout.LayoutParams adAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        adAreaParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // 新增 waps 互动广告区域
        adWapsArea = new LinearLayout(context);
        RelativeLayout.LayoutParams adWapsAreaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        adWapsArea.setGravity(LinearLayout.VERTICAL);
        adArea.addView(adWapsArea, adWapsAreaParams);

        // 新增广告关闭按钮
        int rmAdid = ResourceUtil.getDrawableResourceIdFromName(context, "rm_ad");
        imageButton = new ImageButton(context);
        RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imageButton.setBackgroundResource(rmAdid);
        imageButton.setVisibility(View.GONE);
        imageButton.setLayoutParams(imageButtonParams);

        adArea.addView(imageButton);

        activity.addContentView(adArea, adAreaParams);
        new AdView(activity, adWapsArea).DisplayAd();

        // 是否显示广告关闭按钮
        isShowAdCloseButtom();
        // 指导用户去消费关闭互动广告条
        warnUserSpendForCloseAd();
    }

    /**
     * 提示用户关闭互动广告需要消耗一定的积分
     */
    private static void warnUserSpendForCloseAd() {

        // 添加关闭按钮的点击事件的监听
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userPoints = getUserPointsFromLocal();
                final int spendPoints = 80;

                // 提示用户关闭互动广告
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("温馨提示");
                if (userPoints > spendPoints) {
                    builder.setMessage("关闭广告条需要花费您" + spendPoints + "金币，" +
                            "您当前拥有" + userPoints + "个金币，您是否继续？");
                    builder.setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 消耗用户的积分
                            spendUserPoints.spendUserPoints(spendPoints);
                            closeAdHandler = new Handler();
                            Toast.makeText(context, "请求已经提交!当扣费成功之后广告条将会自动消失。", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    builder.setMessage("关闭广告条需要花费您" + spendPoints + "金币，" +
                            "您当前拥有" + userPoints + "个金币，金币不足，你可以点击确定免费获取金币");
                    builder.setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 显示广告墙
                            showWapsOffers();
                        }
                    });
                }
                builder.setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }

    private static void isShowAdCloseButtom() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int adAreaHeight = adArea.getHeight();
                Log.d(TAG, "adAreaHeight = " + adAreaHeight);
                if (adAreaHeight > 40 && imageButton.getVisibility() == View.GONE) {
                    Message message = new Message();
                    message.what = 1;
                    buttomHandler.sendMessage(message);
                }

                if (adAreaHeight < 40 && imageButton.getVisibility() == View.VISIBLE) {
                    Message message = new Message();
                    message.what = 0;
                    buttomHandler.sendMessage(message);
                }

            }
        };

        timer.schedule(timerTask, 1500, 2000);
    }

    /**
     * 判断用户是否已经激活游戏
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
     * 判断游戏是否需要消费
     *
     * @param context
     * @return
     */
    public static boolean isNeedSpend(Context context) {
        return PreferenceUtil.readRecord(context, IS_NEED_SPEND, false);
    }

    /**
     * 显示广告墙
     */
    private static void showWapsOffers() {
        AppConnect.getInstance(context).showOffers(context);
    }

    /**
     * 本地获取用户积分
     *
     * @return
     */
    private static int getUserPointsFromLocal() {
        getUserPoints.getUserPoints();
        return PreferenceUtil.readRecord(context, USER_POINTS_KEY, 0);

    }

    /**
     * 获取虚拟货币的单位
     *
     * @return
     */
    private static String getUserPointUnit() {
        return PreferenceUtil.readRecord(context, USER_POINT_UNIT_KEY, "金币");
    }

    private static void saveUserPoints(int points) {
        PreferenceUtil.saveRecord(context, USER_POINTS_KEY, points);
    }

    private static void saveUserPointUnit(String pointUnit) {
        PreferenceUtil.saveRecord(context, USER_POINT_UNIT_KEY, pointUnit);
    }


    /**
     * 处理获取用户积分的状态的内部类
     */
    private static class GetUserPoints implements UpdatePointsNotifier {

        public void getUserPoints() {
            AppConnect.getInstance(context).getPoints(GetUserPoints.this);
        }


        @Override
        public void getUpdatePoints(String pointUnit, int pointTotal) {
            Log.d(TAG, "GetUserPoints, getUpdatePoints, pointTotal = " + pointTotal);
            saveUserPoints(pointTotal);
            saveUserPointUnit(pointUnit);
        }

        @Override
        public void getUpdatePointsFailed(String s) {
            // nothing to do...
            Log.d(TAG, "GetUserPoints, getUpdatePointsFailed, errorMsg = " + s);
        }
    }

    /**
     * 处理更新用户积分的状态的内部类
     */
    private static class SpendUserPoints implements UpdatePointsNotifier {

        public void spendUserPoints(int spendPoints) {
            AppConnect.getInstance(context).spendPoints(spendPoints, SpendUserPoints.this);
            PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, true);

        }

        @Override
        public void getUpdatePoints(String pointUnit, int pointTotal) {
            Log.d(TAG, "SpendUserPoints, getUpdatePoints, pointTotal = " + pointTotal);
            saveUserPoints(pointTotal);
            saveUserPointUnit(pointUnit);
            PreferenceUtil.saveRecord(context, IS_HAD_SPEND, true);
            PreferenceUtil.saveRecord(context, IS_NEED_SPEND, false);
            PreferenceUtil.saveRecord(context, IS_HAD_REQUEST, false);

            // 判断是否触发了关闭互动广告的接口
            if (closeAdHandler != null) {
                PreferenceUtil.saveRecord(context, IS_HAD_SPEND_CLOSE_AD, true);

                closeAdHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setVisibility(View.GONE);
                        adWapsArea.setVisibility(View.GONE);
                        timer.cancel();
                        closeAdHandler = null;
                    }
                });
            }
            // 进行激活游戏成功后的提示信息
            else {
                updatePointsHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "消费成功，您可以重新进入游戏。", Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

        @Override
        public void getUpdatePointsFailed(String s) {
            final String errorMsg = s;
            Log.d(TAG, "SpendUserPoints, getUpdatePointsFailed, errorMsg = " + errorMsg);
            updatePointsHandler.post(new Runnable() {
                @Override
                public void run() {
                    //To change body of implemented methods use File | Settings | File Templates.
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    ////////////////////////////////////////////////////////////////
    // 动作特效
    ////////////////////////////////////////////////////////////////

    public static void exitGame(Context context) {
        MusicUtil.stopBackgroundMusic();
    }

    // 新开游戏初始化
    public static void newGame(Context context) {
        // 回收图片
        recycleBitmap(theFailDrawableArray);
        recycleBitmap(theSuccessDrawableArray);

        // 初始化图片
        initSkillDrawableArray(context, theFailNameArray, theFailDrawableArray);
        initSkillDrawableArray(context, theSuccessNameArray, theSuccessDrawableArray);

        // 初始化数据，去掉已经显示过的人物
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

        // 背景音乐
        playBackgroundMusic(context);

        // 开始新局
        newLevel(context, 0);
    }

    // 新开一局初始化
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
         * level文件格式举例
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
            // 获得当前位置
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

            // 调整位置，根据手机的分辨率调整
            // 调整 X 坐标的位置
            setPositionForX(maxX);
            // 调整 Y 坐标的位置
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

    // 播放背景音乐
    public static void playBackgroundMusic(Context context) {
        MusicUtil.playBackgroundMusic(context, theBackgroundMusicName, true);
    }

    // 返回人物图片
    public static int toAvatarDrawableId(Context context, int currLevel) {

        // 返回关卡对应的人物
        String name = PreferenceUtil.readRecord(context, toPreferenceKeyOfLevelAvatarName(currLevel), "");
        if (!StringUtil.empty(name)) {
            return ResourceUtil.getDrawableResourceIdFromName(context, name);
        }

        // 所有人物都显示完毕时，显示缺省人物
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

    // 测试
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

    // 游戏界面，绘制技能特效
    public static void drawSkillForFail(Context context, View view, Canvas canvas) {
        drawSkill(context, view, canvas, theFailNameArray, theFailDrawableArray, false);
    }

    public static void drawSkillForSuccess(Context context, View view, Canvas canvas, int currLevel) {

        // 特效过关
        if (currLevel % 5 == 0) {
            Log.d(TAG, "show avatar, currLevel = " + currLevel);
            // 绘制人物 同一关始终显示相同人物
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

            // 绘制特效
            drawSkill(context, view, canvas, theSuccessNameArray, theSuccessDrawableArray, true);

            // 音乐
            SoundUtil.playEffect(context, theEffectSuccessMusicName);
        } else {

            // 音乐
            SoundUtil.playEffect(context, theLevelSuccessMusicName);
        }

    }

    // 游戏界面，绘制技能特效
    public static void drawSkill(Context context, View view, Canvas canvas,
                                 final String[] paramNameArray, final BitmapDrawable[][] paramDrawArray,
                                 boolean isDrawOnScreenLeftTop) {
        Log.d(TAG, "drawSkill");

        int screenW = view.getWidth();
        int screenH = view.getHeight();
        Log.d(TAG, "drawSkill,screenW = " + screenW + ",screenH = " + screenH);

        int skillACount = 1, skillBCount = 0, skillCCount = 0;

        // 如果切换了牌，则重新绘制
        if (theRedrawSkill) {
            theRedrawSkill = false;
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
                Log.d(TAG, "skillACount = " + skillACount + ", skillBCount = " + skillBCount + ", skillCCount = " + skillCCount);

                // 技能图片
                int maxSkillDrawableArrayCount = 0;
                // 技能A
                if (theLastDrawSkillA == -1) {
                    theLastDrawSkillA = theRandom.nextInt(paramNameArray.length);
                }
                BitmapDrawable[] skillADrawableArray = findSkillDrawableArray(
                        context, paramNameArray, paramDrawArray, theLastDrawSkillA);
                assert (skillADrawableArray != null);
                if (maxSkillDrawableArrayCount < skillADrawableArray.length) {
                    maxSkillDrawableArrayCount = skillADrawableArray.length;
                }
                // 技能B
                if (theLastDrawSkillB == -1) {
                    theLastDrawSkillB = theRandom.nextInt(paramNameArray.length);
                }
                BitmapDrawable[] skillBDrawableArray = findSkillDrawableArray(
                        context, paramNameArray, paramDrawArray, theLastDrawSkillB);
                assert (skillBDrawableArray != null);
                if (maxSkillDrawableArrayCount < skillBDrawableArray.length) {
                    maxSkillDrawableArrayCount = skillBDrawableArray.length;
                }
                // 技能C
                if (theLastDrawSkillC == -1) {
                    theLastDrawSkillC = theRandom.nextInt(paramNameArray.length);
                }
                BitmapDrawable[] skillCDrawableArray = findSkillDrawableArray(
                        context, paramNameArray, paramDrawArray, theLastDrawSkillC);
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
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillADrawableArray, theLastDrawFrame, skillACount, isDrawOnScreenLeftTop);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillBDrawableArray, theLastDrawFrame, skillBCount, isDrawOnScreenLeftTop);
                drawSkillOfAvatarOfSkill(canvas, screenW, screenH, skillCDrawableArray, theLastDrawFrame, skillCCount, isDrawOnScreenLeftTop);

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

        // 持续绘制，动画
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
