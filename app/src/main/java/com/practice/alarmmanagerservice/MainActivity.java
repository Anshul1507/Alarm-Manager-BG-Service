package com.practice.alarmmanagerservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public boolean dailyNotify = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this,MyBroadCastReceiver.class);
        Intent alarmIntent = new Intent(this,AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(dailyNotify){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY,1);
            calendar.set(Calendar.MINUTE,46);
            calendar.set(Calendar.SECOND,40);

            if(calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE,1);
            }

            if(manager != null){
                manager.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,pendingIntent);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                }
            }

            //to enable boot receiver class
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }


    }


}