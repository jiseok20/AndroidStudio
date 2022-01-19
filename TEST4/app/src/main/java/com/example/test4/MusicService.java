package com.example.test4;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    //소스 불러오는 부분
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this,R.raw.bornarockstar);
        mediaPlayer.setLooping(false); // 반복재생 하는 부분
    }

    @Override
    //서비스가 끝나는 부분
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    //서비스 시작
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
