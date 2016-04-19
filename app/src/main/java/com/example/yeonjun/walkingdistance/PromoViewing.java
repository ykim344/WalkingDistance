package com.example.yeonjun.walkingdistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PromoViewing extends AppCompatActivity {

    Button deleteBtn;
    Button blockBtn;
    Button useBtn;
    Button likeBtn;
    ImageView imageView;
    TextView businessNameTV;
    TextView timeTV;
    PromObject workingObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_viewing);

        deleteBtn = (Button)findViewById(R.id.delBtn);
        blockBtn = (Button)findViewById(R.id.blockBtn);
        useBtn=(Button)findViewById(R.id.useBtn);
        likeBtn=(Button)findViewById(R.id.likeBtn);
        imageView = (ImageView)findViewById(R.id.imageView);
        workingObject = MainActivity.promotions.get(MainActivity.workingIndex);
        businessNameTV = (TextView)findViewById(R.id.buzName);
        timeTV = (TextView)findViewById(R.id.expiration);

        businessNameTV.setText(workingObject.getBusinessName());
        timeTV.setText("Expires in "+workingObject.getExpirationDay()+" days");
        buttonHandler();



    }


    public void buttonHandler(){
        deleteBtn.setOnClickListener(

                new View.OnClickListener() {
                    public void onClick(View v) {
                        MainActivity.promotions.remove(MainActivity.workingIndex);

                        Intent i = new Intent(PromoViewing.this,MainActivity.class);
                        startActivity(i);
                    }
                }

        );
        blockBtn.setOnClickListener(

                new View.OnClickListener() {
                    public void onClick(View v) {
                    /////////////////////block function needs to be finished
                    }
                }

        );
        useBtn.setOnClickListener(

                new View.OnClickListener() {
                    public void onClick(View v) {

                        MainActivity.promotions.remove(MainActivity.workingIndex);
                        //pop up the use page
                        Intent i = new Intent(PromoViewing.this,MainActivity.class);
                        startActivity(i);
                    }
                }

        );
        likeBtn.setOnClickListener(

                new View.OnClickListener() {
                    public void onClick(View v) {
                        PromObject workingPromo = MainActivity.promotions.get(MainActivity.workingIndex);
                        if(workingPromo.getLiked()==false){
                            workingPromo.setLiked(true);
                            MainActivity.likes.add(workingPromo);
                            likeBtn.setText("LIKE");
                        }
                        else{
                            workingPromo.setLiked(true);
                            MainActivity.likes.remove(workingPromo);
                            likeBtn.setText("UNLIKE");
                        }
                    }
                }

        );
    }
}
