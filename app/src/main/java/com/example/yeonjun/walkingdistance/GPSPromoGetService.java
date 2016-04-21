package com.example.yeonjun.walkingdistance;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;

/**
 * Created by klay2 on 4/19/16.
 */
public class GPSPromoGetService extends Service {

    private Handler timerMessage;
    private Message getPromo;
    private Criteria gpsCriteria;
    private LocationListener gpsListener;
    private LocationManager gpsManager;
    private String gpsProvider;


    //todo:  need to start a thread that does the handler etc stuff


    public IBinder onBind(Intent intent) {
        return null;


    }


    public void onCreate() {
       /* System.out.println("!!!!!!IN GPS PROMO SERVICE!!!!!!");
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
        */
        try {
            gpsManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            gpsProvider = gpsManager.NETWORK_PROVIDER;
            gpsListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    useCoords(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

            //checkSelfPermission(android.permission.ACCESS_COARSE_LOCATION);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            gpsManager.requestLocationUpdates(gpsProvider, 60000, 60, gpsListener);//this puppy throws errors!
        }catch(Exception e){e.printStackTrace();}


    }

    private void useCoords(Location in){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("gotcoords");
        broadcastIntent.putExtra("Latitude", in.getLatitude());
        broadcastIntent.putExtra("Longitude", in.getLongitude());
        sendBroadcast(broadcastIntent);
        System.out.println("Got longitude: "+ in.getLongitude());


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
