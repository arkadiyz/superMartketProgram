package com.arkadiy.enter.smp3.activities;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.arkadiy.enter.smp3.dataObjects.Store;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }
    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        App.mContext = mContext;
    }
    private void createNotificationChannels(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,"Channel1", NotificationManager.IMPORTANCE_MAX);
            channel1.setDescription("This is Channel1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,"Channel2", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("This is Channel2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

        }

    }


}
