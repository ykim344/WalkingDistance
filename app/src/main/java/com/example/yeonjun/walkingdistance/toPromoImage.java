package com.example.yeonjun.walkingdistance;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

/**
 * Created by Grisha on 4/24/2016.
 */
public class toPromoImage extends Activity {
    private  GoogleMap googleMap ;

    Button deleteButton;
    Button likeButton;
    Button useButton;
    String BName;
    boolean marked;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promo_viewing);
        Bundle bundle = getIntent().getExtras();
        double Longitude = bundle.getDouble("Long");
        double Latitude = bundle.getDouble("Lat");
        BName = bundle.getString("Bname");
        String BusImage = bundle.getString("ImageUrl");
        String exper = bundle.getString("Exp");

        useButton = (Button)findViewById(R.id.useBtn);
        deleteButton = (Button)findViewById(R.id.deleteBtn);
        likeButton = (Button)findViewById(R.id.likeBtn);

        marked = false;
        for(PromObject p:MainActivity.likedPromotions){
            if(p.equals(MainActivity.workingObj))
                marked = true;
        }

        if(marked){
            likeButton.setText("Unlike");
        }

        buttonHandler();

        LatLng Cord = new LatLng(Latitude, Longitude);

        final TextView businessName = (TextView) findViewById(R.id.buzName);
        final TextView experation = (TextView) findViewById(R.id.expiration);
        ImageView bPic = (ImageView) findViewById(R.id.imageView) ;



        Picasso.with(toPromoImage.this)
              .load(BusImage)
              .into(bPic);

        businessName.setText(BName);
        experation.setText(exper);







        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(new LatLng(Latitude,Longitude) ).title(BName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Cord, 17));
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void buttonHandler() {

        useButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(toPromoImage.this, "show this message to the staff",
                        Toast.LENGTH_LONG).show();
                MainActivity.promotions.remove(MainActivity.workingObj);
                if (marked)
                    MainActivity.likedPromotions.remove(MainActivity.workingObj);

            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //like=>unlike
                if (marked) {
                    Toast.makeText(toPromoImage.this,"Remove from Liked", Toast.LENGTH_SHORT).show();
                    MainActivity.likedPromotions.remove(MainActivity.workingObj);
                    likeButton.setText("Like");
                    marked = false;
                }
                //unlike->like
                else {
                    Toast.makeText(toPromoImage.this,"Added to Liked", Toast.LENGTH_SHORT).show();
                    MainActivity.likedPromotions.add(MainActivity.workingObj);
                    likeButton.setText("Unlike");
                    marked = true;
                }

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(toPromoImage.this,"Deleting the promo", Toast.LENGTH_SHORT).show();
                MainActivity.promotions.remove(MainActivity.workingObj);
                if (marked)
                    MainActivity.likedPromotions.remove(MainActivity.workingObj);
            }
        });
    }





}
