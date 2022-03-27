package ca.unb.mobiledev.reflexrevolution.utils;

import android.content.Context;
import android.media.MediaPlayer;

// Adapted from https://stackoverflow.com/a/29883923

public class LoopMediaPlayer {

    private final int mResId;

    private MediaPlayer mCurrentPlayer;
    private MediaPlayer mNextPlayer;
    private float lastSpeed = 1;

    public static LoopMediaPlayer create(Context context, int resId) {
        return new LoopMediaPlayer(context, resId);
    }

    private LoopMediaPlayer(Context context, int resId) {
        mResId = resId;

        mCurrentPlayer = MediaPlayer.create(context, mResId);
        mCurrentPlayer.setOnPreparedListener(mediaPlayer -> mCurrentPlayer.start());
        createNextMediaPlayer(context);
    }

    private void createNextMediaPlayer(Context context) {
        mNextPlayer = MediaPlayer.create(context, mResId);
        mCurrentPlayer.setNextMediaPlayer(mNextPlayer);
        mCurrentPlayer.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.release();
            mCurrentPlayer = mNextPlayer;
            // Reset playback speed when switching to next player
            setPlaybackSpeed(lastSpeed);
            createNextMediaPlayer(context);
        });
    }

    public void pause() {
        if (mCurrentPlayer.isPlaying()) mCurrentPlayer.pause();
    }

    public void restart() {
        if (!mCurrentPlayer.isPlaying()) {
            mCurrentPlayer.seekTo(0);
            mCurrentPlayer.start();
        }
    }

    public void setPlaybackSpeed(float speed) {
        mCurrentPlayer.setPlaybackParams(mCurrentPlayer.getPlaybackParams().setSpeed(speed));
        lastSpeed = speed;
    }

    public void release() {
        mCurrentPlayer.stop();
        mCurrentPlayer.release();
        mNextPlayer.stop();
        mNextPlayer.release();
    }
}