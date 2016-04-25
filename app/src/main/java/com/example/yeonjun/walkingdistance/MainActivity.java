package com.example.yeonjun.walkingdistance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.games.GamesMetadata;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PromObject> promotions;
    private ArrayList<PromObject> likedPromotions;
    private GridView promoListview ;
    private CustomPromoAdapter mPromoAdapter ;
    private BroadcastReceiver reloadPromoReceiver;
    private IntentFilter iFilter;
    Button Liked ;
    Button allPromos ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Liked = (Button) findViewById(R.id.likedbutn);
        allPromos = (Button) findViewById(R.id.promobutn);

        setContentView(R.layout.activity_main);
        promotions = new ArrayList<PromObject>();
        likedPromotions = new ArrayList<PromObject>();


        // 2promotions.add();
        promoListview = (GridView)findViewById(R.id.gridView);
        mPromoAdapter = new CustomPromoAdapter(this, R.layout.list_promobject_layout,promotions);


        iFilter = new IntentFilter();
        iFilter.addAction("reloadNewPromos");
        Intent serviceIntent = new Intent(this,GPSPromoGetService.class);
        startService(serviceIntent);
        Toast.makeText(getApplicationContext(), "getting this far",Toast.LENGTH_SHORT).show();

        //Stufff done for receiving broadcast from the background intent service
        reloadPromoReceiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent) {
                //loadFromDisk();//TODO:this should be done async
                //mPromoAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "got Latitude: " + intent.getDoubleExtra("Latitude",0),Toast.LENGTH_SHORT).show();

            }
        };

        registerReceiver(reloadPromoReceiver,iFilter);


        if(promoListview != null){
            promoListview.setAdapter(mPromoAdapter);
        }

        Liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPromoAdapter = new CustomPromoAdapter(MainActivity.this, R.layout.list_promobject_layout,likedPromotions);
            }
        });
        allPromos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPromoAdapter = new CustomPromoAdapter(MainActivity.this, R.layout.list_promobject_layout,promotions);
            }
        });

        promoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PromObject Obj = (PromObject) parent.getAdapter().getItem(position);
                    Intent mIntent = new Intent(MainActivity.this , toPromoImage.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putDouble("Long", Obj.getLongitude());
                    mBundle.putDouble("Lat", Obj.getLatitude());
                    mBundle.putString("ImageUrl", Obj.getbusinessPhoto());
                    mBundle.putString("Bname", Obj.getBusinessName());
                    mBundle.putString("Exp", Obj.promoDateTime());
                    mIntent.putExtras(mBundle);


            }

        });

        promoListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PromObject likedObj = (PromObject) parent.getAdapter().getItem(position);
                if (likedPromotions.isEmpty()){
                    likedPromotions.add(likedObj);
                }
                else {
                    int i = 0 ;
                    boolean contains = false;
                    while (i < likedPromotions.size() && contains ){
                        if (likedObj.getBusinessName().equals( likedPromotions.get(i).getBusinessName())){
                        contains = true ;
                        }
                        i++ ;
                    }
                    if(contains == false){
                        likedPromotions.add(likedObj);
                        Toast.makeText(MainActivity.this, "Added To Liked",Toast.LENGTH_SHORT);

                    }
                }



                return true ;
            }
        });



        boolean loadResult;
        promotions = null;
        loadResult = loadFromDisk();
        if(loadResult  && promotions != null){
            //load successful, TODO:check for and delete expired promos finish the method via getting an iterator etc
            deleteExpired(promotions);
        }



    }

    //method for combing over promotions in the array and deleting
    //them if  they are currently expired
    //TODO: finish this thingy
    private void deleteExpired(ArrayList<PromObject> promotionsIn){

        return;
    }

    //method for saving promos to a file on disk
    //returns a boolean for whether it failed or not
    //call this whenever the activity is about to end
    //TODO: sloppily done, needs some more advanced checking or whatever maybe
    private boolean saveToDisk(){
        File fileOut = new File(this.getFilesDir(), "promotions.sv");
        FileOutputStream fileOutStream;
        ObjectOutputStream objectsOut;
        try {
            fileOutStream = openFileOutput("promotions.sv", this.MODE_PRIVATE);
            objectsOut = new ObjectOutputStream(fileOutStream);
            objectsOut.writeObject(promotions);
            objectsOut.close();
            fileOutStream.close();


        }catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        catch(IOException f){
            f.printStackTrace();
            return false;
        }
        return true;

    }

    //method for loading promotions from disk
    //returns a boolean for failure etc
    //TODO:  sloppily done needs some more advanced file checking or whatever maybe
    private boolean loadFromDisk(){
        FileInputStream fileInStream;
        ObjectInputStream objectsIn;
        try{
            fileInStream = openFileInput("promotions.sv");
            objectsIn = new ObjectInputStream(fileInStream);
            promotions = (ArrayList<PromObject>)objectsIn.readObject();
            fileInStream.close();
            objectsIn.close();
            if(promotions == null){
                return false;
            }
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        catch(ClassNotFoundException f){
            f.printStackTrace();
            return false;
        }
        return true;

    }

    private class DownloadPromo extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            //TODO:  USE COORDINATES TO GET NEW PROMO and add it to the arraylist and update adapter




            return null;
        }
    }

    private class OrmStartup extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            //TODO:  USE COORDINATES TO GET NEW PROMO and add it to the arraylist and update adapter
            ObjectifyService.register(PromObject.class);



            return null;
        }
    }

}
