package com.example.yeonjun.walkingdistance;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * Created by Grisha on 4/24/2016.
 */
public class toPromoImage extends Activity {
    private  GoogleMap googleMap ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Double Longitude = bundle.getDouble("Long");
        Double Latitude = bundle.getDouble("Lat");
        String BName = bundle.getString("Bname");
        String BusImage = bundle.getString("ImageURL");
        String exper = bundle.getString("Exp");

        LatLng Cord = new LatLng(Latitude , Longitude);

        TextView businessName = (TextView) findViewById(R.id.storeName);
        TextView experation = (TextView) findViewById(R.id.expDate);

        businessName.setText(BName);
        experation.setText(exper);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(Cord).title(BName));
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}
