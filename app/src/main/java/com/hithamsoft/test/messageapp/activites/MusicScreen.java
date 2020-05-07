package com.hithamsoft.test.messageapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hithamsoft.test.messageapp.InitApplication;
import com.hithamsoft.test.messageapp.R;
import com.hithamsoft.test.messageapp.databinding.ActivityMusicScreenBinding;
import com.hithamsoft.test.messageapp.service.MusicStopListiner;
import com.hithamsoft.test.messageapp.service.MyMusicService;

import java.util.HashMap;
import java.util.logging.Logger;

import info.abdolahi.CircularMusicProgressBar;
import info.abdolahi.OnCircularSeekBarChangeListener;

public class MusicScreen extends AppCompatActivity implements MusicStopListiner {

    private static final String TAG ="MusicScreen" ;
    ActivityMusicScreenBinding musicScreenBinding;
    //string file
    int musicFile=R.raw.sia_song;
    String musicLink="https://mp3storage.alarab.com/data/static/MMS_Files/MP3/mp3_files//A/Asy%20El%20Helany/Alarab_Assi-ElHallani_Ramadan-Rahma.mp3";
    boolean musicPlaying=false;
    Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        musicScreenBinding= DataBindingUtil.setContentView(this,R.layout.activity_music_screen);

        InitApplication.context=MusicScreen.this;
        musicScreenBinding.musicBtn.setImageResource(R.drawable.ic_start);
        serviceIntent=new Intent(this, MyMusicService.class);

        musicScreenBinding.musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!musicPlaying){
                    playMusic();
                    musicScreenBinding.musicBtn.setImageResource(R.drawable.ic_stop);
                    musicPlaying=true;
//                    new Thread(this).start();


                }else {
                    stopMusic();
                    musicScreenBinding.musicBtn.setImageResource(R.drawable.ic_start);
                    musicPlaying=false;
                    musicScreenBinding.albumArt.setValue(0);
                }
            }
        });
        setImage();

        musicScreenBinding.albumArt.setOnCircularBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser) {
                //circularBar.setValue(progress);
            }

            @Override
            public void onClick(CircularMusicProgressBar circularBar) {

            }

            @Override
            public void onLongPress(CircularMusicProgressBar circularBar) {

            }
        });


    }
    private Bitmap downloadBitmap(final String url) {
        final MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(url, new HashMap<String, String>());
        try {
            final byte[] art = metaRetriever.getEmbeddedPicture();
            return BitmapFactory.decodeByteArray(art, 0, art.length);
        } catch (Exception e) {

            return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        }
    }
    private void setImage(){



        //coverart is an Imageview object

        // convert the byte array to a bitmap
       musicScreenBinding.albumArt.setImageBitmap(downloadBitmap(musicLink));
    }

    private void stopMusic() {
        stopService(serviceIntent);
    }

    private void playMusic() {
        serviceIntent.putExtra("MUSIC_FILE",musicLink);
        startService(serviceIntent);

    }

    @Override
    public void onMusicStop() {
        musicScreenBinding.musicBtn.setImageResource(R.drawable.ic_start);
        musicPlaying=false;
    }

    @Override
    public void mMusicSeek(float seek) {

        Log.d(TAG, "mMusicSeek: "+seek);
        musicScreenBinding.albumArt.setValue(seek);
    }
}
