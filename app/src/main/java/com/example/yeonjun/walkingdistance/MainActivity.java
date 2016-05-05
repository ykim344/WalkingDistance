package com.example.yeonjun.walkingdistance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


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
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    static ArrayList<PromObject> promotions;
    static ArrayList<PromObject> likedPromotions;
    private HashSet<Long> promoIds;
    private GridView promoListview ;
    private CustomPromoAdapter mPromoAdapter ;
    private BroadcastReceiver gpsUpdateReceiver;
    private IntentFilter iFilter;
    private Intent serviceIntent;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find out drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        promotions = new ArrayList<PromObject>();
        promoIds = new HashSet<Long>();
        // 2promotions.add();
        promoListview = (GridView)findViewById(R.id.gridView);
        mPromoAdapter = new CustomPromoAdapter(this, R.layout.list_promobject_layout,promotions);

        likedPromotions = new ArrayList<PromObject>();

        iFilter = new IntentFilter();
        iFilter.addAction("gotcoords");

        mPromoAdapter.notifyDataSetChanged();
        serviceIntent = new Intent(this,GPSPromoGetService.class);
        startService(serviceIntent);
        PromObject obj ;
        obj = new PromObject(1,"Walking Distance",15,16,"https://www.djc.com/stories/images/20130206/POM_2_Mushroom_Farm_KS_big.jpg","http://d1v471jzilqnl0.cloudfront.net/wp-content/uploads/2012/12/BBBS-WEEKDAY-PROMO.jpg",43.071617,-89.407197);
        promotions.add(obj);

        // startService(serviceIntent);
        //Toast.makeText(getApplicationContext(), "getting this far",Toast.LENGTH_SHORT).show();

       // DownloadPromo downTask = new DownloadPromo();
        //downTask.execute("-89.406318", "43.071309");



        promoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PromObject Obj = (PromObject) parent.getAdapter().getItem(position);
                Intent mIntent = new Intent(MainActivity.this, toPromoImage.class);
                Bundle mBundle = new Bundle();
                mBundle.putDouble("Long", Obj.getLongitude());
                mBundle.putDouble("Lat", Obj.getlatitude());
                mBundle.putString("ImageUrl", Obj.getPromoPhoto());
                mBundle.putString("Bname", Obj.getBusinessName());
                mBundle.putString("Exp", Obj.promoDateTime());
                mIntent.putExtras(mBundle);
                startActivity(mIntent);


            }

        });

        promoListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                PromObject likedObj = (PromObject) parent.getAdapter().getItem(position);
                if (likedPromotions.isEmpty()) {
                    likedPromotions.add(likedObj);
                } else {
                    int i = 0;
                    boolean contains = false;
                    while (i < likedPromotions.size() && contains) {
                        if (likedObj.getBusinessName().equals(likedPromotions.get(i).getBusinessName())) {
                            contains = true;
                        }
                        i++;
                    }
                    if (contains == false) {
                        likedPromotions.add(likedObj);
                        Toast.makeText(MainActivity.this, "Added To Liked", Toast.LENGTH_SHORT);

                    }
                }


                return true;
            }
        });








        //Stufff done for receiving broadcast from the background intent service


//        registerReceiver(gpsUpdateReceiver,iFilter);


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



       // boolean loadResult;
       // promotions = null;
       // loadResult = loadFromDisk();
       // if(loadResult  && promotions != null){
            //load successful, TODO:check for and delete expired promos finish the method via getting an iterator etc
            //deleteExpired(promotions);
       // }



    }


    protected void onResume(){
        super.onResume();
        registerReceiver(gpsUpdateReceiver,iFilter);



    }


    protected void onPause(){
        super.onPause();
        //stopService(serviceIntent);
        unregisterReceiver(gpsUpdateReceiver);
    }

    protected void onDestroy(){
        super.onDestroy();
        stopService(serviceIntent);

    }

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

            //SYNCHRONOUGET
            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                String responseJson = response.body().string();

                if(responseJson.equals("[]")){
                    //empty response do nothing.
                }
                else {
                    Gson gson = new GsonBuilder().create();
                    PromObject[] inProms = gson.fromJson(responseJson, PromObject[].class);
                    for (PromObject promo : inProms) {
                        if(!promoIds.contains(promo.getId())) {
                            promotions.add(promo);
                            promoIds.add(promo.getId());
                            System.out.println("ADDED PROMO&&&&&&&&&&&");
                            System.out.println("Business: " + promo.getBusinessName());
                        }
                    }
                }


            }catch(Exception e){e.printStackTrace();}





            /*
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

            });//end client.newCall.enqueue*/


            return "go";
        }


        public void onPostExecute(String result){
            promoListview = (GridView)findViewById(R.id.gridView);
            mPromoAdapter = new CustomPromoAdapter(MainActivity.this, R.layout.list_promobject_layout,promotions);
            promoListview.setAdapter(mPromoAdapter);
            mPromoAdapter.notifyDataSetChanged();

        }
        public void update(){

        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch(menuItem.getItemId()) {
            case R.id.promos2:
                onPromoSelected();
                break;
            case R.id.liked2:
                onLikeMenuSelected();
                break;
            default:
                onPromoSelected();
        }


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    private void onLikeMenuSelected() {
        promoListview = (GridView)findViewById(R.id.gridView);
        mPromoAdapter = new CustomPromoAdapter(MainActivity.this, R.layout.list_promobject_layout,likedPromotions);
        promoListview.setAdapter(mPromoAdapter);
        mPromoAdapter.notifyDataSetChanged();
    }

    private void onPromoSelected() {
        promoListview = (GridView)findViewById(R.id.gridView);
        mPromoAdapter = new CustomPromoAdapter(MainActivity.this, R.layout.list_promobject_layout,promotions);
        promoListview.setAdapter(mPromoAdapter);
        mPromoAdapter.notifyDataSetChanged();
    }



}
