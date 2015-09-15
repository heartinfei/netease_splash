package com.wangqiang.netease_sample.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;


public class SoundService extends Service {
    public static final int START = 0;
    public static final int PAUSE = 1;
    private MediaPlayer mediaPlayer;
    private String currentMediaPath = "new_version.mp3";

    public SoundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd(currentMediaPath);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
            switch (intent.getIntExtra("COMMAND", START)) {
                case START:
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                    break;
                case PAUSE:
                    mediaPlayer.pause();
                    break;
            }
        return super.onStartCommand(intent, flags, startId);
    }

    public static final void pauseMedia(Context context) {
        Intent mediaIntent = new Intent(context, SoundService.class);
        mediaIntent.putExtra("COMMAND", SoundService.PAUSE);
        context.startService(mediaIntent);
    }

    public static final void startMedia(Context context) {
        Intent mediaIntent = new Intent(context, SoundService.class);
        mediaIntent.putExtra("COMMAND", SoundService.START);
        context.startService(mediaIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
