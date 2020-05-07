package com.hithamsoft.test.messageapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hithamsoft.test.messageapp.InitApplication;
import com.hithamsoft.test.messageapp.R;
import com.hithamsoft.test.messageapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_SMS=1002;
    String SENT="sms_sent";
    String DELIVERED ="SMS_DELVERED";
    PendingIntent sendPI,deliveredPI;
    BroadcastReceiver sendBR,deliveredBR;

    ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        activityMainBinding= DataBindingUtil.setContentView(this, R.layout.activity_main);
        sendPI=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPI=PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

        //enable/disable night mode
        checkNightMode();

       activityMainBinding.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String messageIs=activityMainBinding.inputMessageTxt.getText().toString();
               String phoneNum=activityMainBinding.phoneNumberTxt.getText().toString();

               //check permission
               if (ContextCompat.checkSelfPermission(MainActivity.this,
                       Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){

                   ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},REQUEST_SMS);

               }else {
                   SmsManager smsManager=SmsManager.getDefault();
                   smsManager.sendTextMessage(phoneNum,null,messageIs,sendPI,deliveredPI);
               }
           }
       });
    }


    @Override
    protected void onResume() {
        super.onResume();
        sendBR=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK :
                        Toast.makeText(context, "Message Send", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic Failure", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No Service", Toast.LENGTH_SHORT).show();
                        break;

                        case SmsManager.RESULT_ERROR_NULL_PDU: // result null
                            Toast.makeText(context, "NULL PDU!", Toast.LENGTH_SHORT).show();
                            break;

                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(context, "Radio Off", Toast.LENGTH_SHORT).show();
                                break;
                }

            }
        };
        deliveredBR=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Message Delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "Message Not Delivered", Toast.LENGTH_SHORT).show();
                        break;


                }

            }
        };

        registerReceiver(sendBR,new IntentFilter(SENT));
        registerReceiver(deliveredBR,new IntentFilter(DELIVERED));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(sendBR);
        unregisterReceiver(deliveredBR);
    }

    private void checkNightMode(){
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            activityMainBinding.darkModeSwitch.setChecked(true);
        activityMainBinding.darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    InitApplication.getInstance().setIsNightModeEnabled(true);


                    recreate();
//                    Intent intent = getIntent();
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    finish();
//                    startActivity(intent);

                } else {
                    InitApplication.getInstance().setIsNightModeEnabled(false);

                    recreate();
//                    Intent intent = getIntent();
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    finish();
//                    startActivity(intent);
                }
            }
        });

    }
}
