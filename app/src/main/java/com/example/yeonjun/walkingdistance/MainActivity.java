package com.example.yeonjun.walkingdistance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PromObject> promotions;
    private GridView promoListview ;
    private CustomPromoAdapter mPromoAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        promotions = new ArrayList<PromObject>();
        // 2promotions.add();
        promoListview = (GridView)findViewById(R.id.gridView);
        mPromoAdapter = new CustomPromoAdapter(this, R.layout.list_promobject_layout,promotions);


        if(promoListview != null){
            promoListview.setAdapter(mPromoAdapter);
        }



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
}
