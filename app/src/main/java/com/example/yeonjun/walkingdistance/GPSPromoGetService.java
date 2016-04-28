package com.example.yeonjun.walkingdistance;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;

/**
 * Created by klay2 on 4/19/16.
 */
public class GPSPromoGetService extends Service {



    private LocationListener gpsListener;
    private LocationManager gpsManager;
    private String gpsProvider;
    private HandlerThread sammich;


    //todo:  need to start a thread that does the handler etc stuff


    public IBinder onBind(Intent intent) {
        return null;


    }


    public void onCreate() {
        System.out.println("!!!!!!IN GPS PROMO SERVICE!!!!!!");



        try {
           // gpsManager = (LocationManager) getSystemService(LOCATION_SERVICE);
           // gpsProvider = gpsManager.NETWORK_PROVIDER;

            gpsListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    useCoords(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                    System.out.println("@@@@@@@@PROVIDER enabled!@@@@@@");

                }

                public void onProviderDisabled(String provider) {
                    System.out.println("@@@@@@@@PROVIDER DISABLED!@@@@@@");
                }
            };

            /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }*/


            sammich = new HandlerThread("gpsCallback");
            sammich.start();
            gpsManager.requestLocationUpdates(gpsProvider, 30, 5, gpsListener,sammich.getLooper());//this puppy throws errors!







            System.out.println("@@@@@@@@GPS SETUP IN SERVICE@@@@@@");
        }catch(Exception e){e.printStackTrace();}


    }

    public void onDestroy(){
        sammich.quit();


    }

    private void useCoords(Location in){
        System.out.println("@@@@@@@@IN USE COORDS IN SERVICE@@@@@@");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("gotcoords");
        broadcastIntent.putExtra("Latitude", in.getLatitude());
        broadcastIntent.putExtra("Longitude", in.getLongitude());
        sendBroadcast(broadcastIntent);
        System.out.println("Got longitude: " + in.getLongitude());


    }




}
