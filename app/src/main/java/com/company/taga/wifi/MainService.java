package com.company.taga.wifi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.id;

/**
 * Created by Gurnain on 2017-11-22.
 */

public class MainService extends Service {
    private WifiManager wifiManager;
    private WifiInfo connection;
    private AudioManager audioManager;
    private String id;
    private Thread thread;
    private boolean running;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        thread = new Thread(new Runnable() {
            @Override
            public void run(){
                while(running){
                    try {
                        //audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        connection = wifiManager.getConnectionInfo();
                        id = connection.getSSID();
                        if(id=="BELL626"){
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            break;
                        }
                        Thread.sleep(5000);
                        //audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        //Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running = false;
        thread.interrupt();
        Log.d("STATE 2",Boolean.toString(thread.isInterrupted()));
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
