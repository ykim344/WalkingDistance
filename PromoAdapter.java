package com.example.yeonjun.walkingdistance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Grisha on 3/23/2016.
 */
public class PromoAdapter extends ArrayAdapter<PromObject>{

        Context context;
        int layoutResourceId ;
        PromObject data[] = null ;

    public PromoAdapter(Context context, int resource, PromObject[] data) {
        super(context, resource, data);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = data;

    }
    public PromObject getItem(int position){
        return super.getItem(position);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View promoIcon = convertView ; // inflate the layout for single promoObject
        LayoutInflater inflater = LayoutInflater.from(context);
        promoIcon = inflater.inflate(layoutResourceId,parent,false); // get a refrence to different view element we want to update

        // gets a reference to the different view elements we want to update
        TextView bName = (TextView) promoIcon.findViewById(R.id.storeName);
        TextView bExp = (TextView ) promoIcon.findViewById(R.id.expDate);
        ImageView bImage = (ImageView) promoIcon.findViewById(R.id.businessImage);

        // get the data from the data array
        PromObject promo = data[position];

        // setting the view to reflect the data we need to display

        bName.setText(promo.getBusinessName());
        bExp.setText(promo.promoDateTime());

        int resId = context.getResources().getIdentifier(promo.getbusinessPhoto(),"drawable", context.getPackageName());
        bImage.setImageResource(resId);





            return  promoIcon ;
    }
}
