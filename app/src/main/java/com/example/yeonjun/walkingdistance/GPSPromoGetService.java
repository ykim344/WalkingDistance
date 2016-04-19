package com.example.yeonjun.walkingdistance;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * Created by klay2 on 4/19/16.
 */
public class GPSPromoGetService extends Service {

    Handler timerMessage;
    Message getPromo;


    //todo:  need to start a thread that does the handler etc stuff



    public IBinder onBind(Intent intent){
        return null;


    }


    public void onCreate(){
        System.out.println("!!!!!!IN GPS PROMO SERVICE!!!!!!");
        timerMessage = new Handler();

        //getPromo = new Message();
        //timerMessage.sendMessageDelayed(getPromo, 6000);

         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                task();
                timerMessage.postDelayed(this, 6000);
            }
        };
        timerMessage.postDelayed(runnable,6000);


    }
    public void task(){
        System.out.println("@@@@@@@@IN HANDLE MESSAGE IN SERVICE@@@@@@");
        //TODO: get coordinates gps

        //TODO: send http message to get object

        //TODO: save promo to file
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("reloadNewPromos");
        sendBroadcast(broadcastIntent);



        return;

    }




}
