package com.hithamsoft.test.messageapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.hithamsoft.test.messageapp.InitApplication;
import com.hithamsoft.test.messageapp.activites.MainActivity;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class MyMusicService extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener
,MediaPlayer.OnErrorListener,MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener  {

    private static final String TAG = "MyMusicService";
    private MediaPlayer mediaPlayer;
    private String musicLink;
    private MusicStopListiner musicStopListiner;


    Handler mSeekbarUpdateHandler = new Handler();

    public MyMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicStopListiner= (MusicStopListiner) InitApplication.context;

        musicLink=intent.getStringExtra("MUSIC_FILE");
        mediaPlayer.reset();
        if (mediaPlayer!=null){
            try {
                mediaPlayer.setDataSource(musicLink);
                mediaPlayer.prepareAsync();
                //

            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (mp.isPlaying()){
            mp.stop();

        }
        musicStopListiner.onMusicStop();
        stopSelf();//stop service
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        switch (what){
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(this, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT).show();
                break;

            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
        }
    return false;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        if (!mp.isPlaying()){
            mp.start();




//            int currentPosition = mediaPlayer.getCurrentPosition();
//            int total = mediaPlayer.getDuration();
//
//            while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
//                try {
////                    Thread.sleep(1000);
//                    currentPosition = mediaPlayer.getCurrentPosition();
//                }catch (Exception e){
//
//                }
//
//                musicStopListiner.mMusicSeek(currentPosition);
//
//            }


        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

//        musicStopListiner.mMusicSeek(mp.getDuration());
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        musicStopListiner.mMusicSeek(percent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying())
                musicStopListiner.mMusicSeek(mediaPlayer.getCurrentPosition()/1000);
            }
        },1000);

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }






//    @Override
//    public void run() {
//        int currentPosition = mediaPlayer.getCurrentPosition();
//        int total = mediaPlayer.getDuration();
//        Log.d(TAG, "run: total "+total);
//
//
//        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
//            try {
//                Thread.sleep(1000);
//                currentPosition = mediaPlayer.getCurrentPosition();
//                Log.d(TAG, "run: cureentPosition "+currentPosition);
//
//            } catch (InterruptedException e) {
//                return;
//            } catch (Exception e) {
//                return;
//            }
//            musicStopListiner.mMusicSeek((float) currentPosition);
//
//
//
//        }
//    }
}
