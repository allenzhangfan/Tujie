package com.google.android.cameraview;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class PlayVoice {

    private static MediaPlayer mediaPlayer;

    public static void play(Context context) {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.camera_click);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //停止播放声音
    public static void stop() {
        if (null != mediaPlayer) {
            mediaPlayer.stop();
        }
    }

    public static int getAudioManagerType(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int mode = audioManager.getRingerMode();
        return mode;
    }
}
