package com.example.yeonjun.walkingdistance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PromObject> promotions;
    private GridView promoListview ;
    private CustomPromoAdapter mPromoAdapter ;
    private BroadcastReceiver gpsUpdateReceiver;
    private IntentFilter iFilter;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        promotions = new ArrayList<PromObject>();
        promoListview = (GridView)findViewById(R.id.gridView);
        mPromoAdapter = new CustomPromoAdapter(this, R.layout.list_promobject_layout,promotions);
        iFilter = new IntentFilter();
        iFilter.addAction("gotcoords");
        serviceIntent = new Intent(this,GPSPromoGetService.class);












        //Stufff done for receiving broadcast from the background intent service
        gpsUpdateReceiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent) {
               DownloadPromo downTask = new DownloadPromo();
                downTask.execute(Double.toString(intent.getDoubleExtra("Longitude",0)),Double.toString(intent.getDoubleExtra("Latitude",0)));
               // downTask.execute("-89.406318","43.071309");


                System.out.println("!GOT BORADCAST IN MAIN Longitude = "+intent.getDoubleExtra("Longitude",0)+"!!$$$$$$$$$$$$$$$$$$");

            }
        };




        if(promoListview != null){
            promoListview.setAdapter(mPromoAdapter);
        }







    }

    protected void onResume(){
        super.onResume();
        registerReceiver(gpsUpdateReceiver,iFilter);
        startService(serviceIntent);


    }


    protected void onPause(){
        super.onPause();
        stopService(serviceIntent);
        unregisterReceiver(gpsUpdateReceiver);
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
             final OkHttpClient client = new OkHttpClient();
            String longitude = params[0];
            String latitude = params[1];



            Request request = new Request.Builder()
                    .url("http://hidden-fortress-95984.heroku.com/hello")
                    .header("longitude", longitude)
                    .addHeader("latitude", latitude)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String responseJson = response.body().string();

                    if(responseJson.equals("[]")){
                        //empty response do nothing.
                    }
                    else {
                        Gson gson = new GsonBuilder().create();
                        PromObject[] inProms = gson.fromJson(responseJson, PromObject[].class);
                        for(PromObject promo :inProms){
                            promotions.add(promo);
                            System.out.println("ADDED PROMO&&&&&&&&&&&");
                            System.out.println("Business: "+promo.getBusinessName());
                        }

                    }

                }

        });//end client.newCall.enqueue


                return null;
        }


        public void onPostExecute(String result){
            mPromoAdapter.notifyDataSetChanged();

        }


    }








}
