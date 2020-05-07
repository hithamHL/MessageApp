package com.hithamsoft.test.messageapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hithamsoft.test.messageapp.InitApplication;
import com.hithamsoft.test.messageapp.R;
import com.hithamsoft.test.messageapp.databinding.ActivityMainScreenBinding;

public class MainScreen extends AppCompatActivity {

    ActivityMainScreenBinding mainScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        mainScreenBinding= DataBindingUtil.setContentView(this, R.layout.activity_main_screen);

        mainScreenBinding.messageAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,MainActivity.class));
            }
        });

        mainScreenBinding.musicAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,MusicScreen.class));
            }
        });

//        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000, interpolator);
//        mainScreenBinding.kenBurnsView.setTransitionGenerator(generator);

    }
}
