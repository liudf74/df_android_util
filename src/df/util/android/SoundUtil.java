package df.util.android;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;
import java.util.Vector;

/**
 * This class is used for controlling effect
 */

public class SoundUtil {

    private static final String TAG = "df.util.SoundUtil";

    private static float mLeftVolume;
    private static float mRightVolume;

    private static SoundPool mSoundPool;

    // sound id and stream id map
    private static HashMap<Integer, Integer> mSoundIdStreamIdMap;
    // sound path and sound id map
    private static HashMap<String, Integer> mPathSoundIDMap;

    public static Boolean theLock = new Boolean(Boolean.TRUE);

    private static final int MAX_SIMULTANEOUS_STREAMS_DEFAULT = 5;
    private static final float SOUND_RATE = 1.0f;
    private static final int SOUND_PRIORITY = 1;
    private static final int SOUND_LOOP_TIME = 0;
    private static final int SOUND_QUALITY = 5;

    private static final int INVALID_SOUND_ID = -1;
    private static final int INVALID_STREAM_ID = -1;

    static {
        initData();
    }

    public static int preloadEffect(Context context, String path) {
        int soundId = INVALID_SOUND_ID;
        if (mPathSoundIDMap.get(path) != null) {
            soundId = mPathSoundIDMap.get(path).intValue();
        } else {
            soundId = createSoundIdFromAsset(context, path);
            if (soundId != INVALID_SOUND_ID) {
                mSoundIdStreamIdMap.put(soundId, INVALID_STREAM_ID);
                mPathSoundIDMap.put(path, soundId);
            }
        }
        return soundId;
    }

    public static void unloadEffect(String path) {
        Integer soundId = mPathSoundIDMap.remove(path);
        if (soundId != null) {
            mSoundPool.unload(soundId.intValue());
            mSoundIdStreamIdMap.remove(soundId);
        }
    }

    public synchronized static int playEffect(Context context, String path) {
        Log.d(TAG, "playEffect, path = " + path);
//        synchronized (theLock) {
        Integer soundId = mPathSoundIDMap.get(path);
        if (soundId != null) {
            int streamId = mSoundPool.play(
                    soundId.intValue(), mLeftVolume, mRightVolume,
                    SOUND_PRIORITY, SOUND_LOOP_TIME, SOUND_RATE);
            mSoundIdStreamIdMap.put(soundId, streamId);
        } else {
            soundId = preloadEffect(context, path);
            if (soundId == INVALID_SOUND_ID) {
                return INVALID_SOUND_ID;
            }
            playEffect(context, path);
        }
        return soundId.intValue();
//        }
    }

    public static void stopEffect(int soundId) {
        Integer streamId = mSoundIdStreamIdMap.get(soundId);

        if (streamId != null && streamId.intValue() != INVALID_STREAM_ID) {
            mSoundPool.stop(streamId.intValue());
        }
    }

    public static float getEffectsVolume() {
        return (mLeftVolume + mRightVolume) / 2;
    }

    public static void setEffectsVolume(float volume) {
        mLeftVolume = mRightVolume = volume;
    }

    public static void end() {
        mSoundPool.release();
        mPathSoundIDMap.clear();
        mSoundIdStreamIdMap.clear();

        initData();
    }

    public static int createSoundIdFromAsset(Context context, String path) {
        int soundId = INVALID_SOUND_ID;

        try {
            soundId = mSoundPool.load(context.getAssets().openFd(path), 0);
        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }

        return soundId;
    }

    private static void initData() {
        mSoundIdStreamIdMap = new HashMap<Integer, Integer>();
        mSoundPool = new SoundPool(MAX_SIMULTANEOUS_STREAMS_DEFAULT, AudioManager.STREAM_MUSIC, SOUND_QUALITY);
        mPathSoundIDMap = new HashMap<String, Integer>();

        mLeftVolume = 1.0f;
        mRightVolume = 1.0f;
    }
}
