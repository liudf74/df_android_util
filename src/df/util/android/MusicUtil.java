package df.util.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * This class is used for controlling background music
 */
public class MusicUtil {

    private static final String TAG = "df.util.MusicUtil";

    private static float mLeftVolume;
    private static float mRightVolume;
    private static boolean mIsPaused;

    private static MediaPlayer mBackgroundMediaPlayer;

    static {
        initData();
    }

    public static void playBackgroundMusic(Context context, String path, boolean isLoop) {
        if (mBackgroundMediaPlayer == null) {
            mBackgroundMediaPlayer = createMediaplayerFromAssets(context, path);
        }

        if (mBackgroundMediaPlayer == null) {
            Log.e(TAG, "playBackgroundMusic: background media player is null");
        } else {
            // if the music is playing or paused, stop it
            mBackgroundMediaPlayer.stop();

            mBackgroundMediaPlayer.setLooping(isLoop);

            try {
                mBackgroundMediaPlayer.prepare();
                mBackgroundMediaPlayer.start();
            } catch (Exception e) {
                Log.e(TAG, "playBackgroundMusic: error state");
            }
        }
    }

    public static void stopBackgroundMusic() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.stop();
        }
    }

    public static void pauseBackgroundMusic() {
        if (mBackgroundMediaPlayer != null && mBackgroundMediaPlayer.isPlaying()) {
            mBackgroundMediaPlayer.pause();
            mIsPaused = true;
        }
    }

    public static void resumeBackgroundMusic() {
        if (mBackgroundMediaPlayer != null && mIsPaused) {
            mBackgroundMediaPlayer.start();
        }
    }

    public static void rewindBackgroundMusic() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.stop();

            try {
                mBackgroundMediaPlayer.prepare();
                mBackgroundMediaPlayer.start();
            } catch (Exception e) {
                Log.e(TAG, "rewindBackgroundMusic: error state");
            }
        }
    }

    public static boolean isBackgroundMusicPlaying() {
        boolean ret = false;

        if (mBackgroundMediaPlayer == null) {
            ret = false;
        } else {
            ret = mBackgroundMediaPlayer.isPlaying();
        }

        return ret;
    }

    public static void end() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.release();
        }

        initData();
    }


    public static float getBackgroundVolume() {
        if (mBackgroundMediaPlayer != null) {
            return (mLeftVolume + mRightVolume) / 2;
        } else {
            return 0.0f;
        }
    }

    public static void setBackgroundVolume(float volume) {
        if (mBackgroundMediaPlayer != null) {
            mLeftVolume = mRightVolume = volume;
            mBackgroundMediaPlayer.setVolume(mLeftVolume, mRightVolume);
        }
    }

    private static void initData() {
        mLeftVolume = 0.5f;
        mRightVolume = 0.5f;
        mBackgroundMediaPlayer = null;
        mIsPaused = false;
    }

    /**
     * create mediaplayer for music
     *
     * @param path the path relative to assets
     * @return
     */
    private static MediaPlayer createMediaplayerFromAssets(Context context, String path) {
        MediaPlayer mediaPlayer = null;

        try {
            AssetFileDescriptor assetFileDescritor = context.getAssets().openFd(path);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(assetFileDescritor.getFileDescriptor(),
                    assetFileDescritor.getStartOffset(), assetFileDescritor.getLength());
            mediaPlayer.prepare();

            mediaPlayer.setVolume(mLeftVolume, mRightVolume);
        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }

        return mediaPlayer;
    }
}
