package com.example.yeonjun.walkingdistance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grisha on 3/23/2016.
 */
public class CustomPromoAdapter extends BaseAdapter{

    private  ArrayList<PromObject> promotions ;
    Context context;
    int layoutResourceId ;

    ImageView bImage ;
    TextView bName ;
    TextView bExp;

    private static  class ViewHolder{
        TextView bName ;
        TextView bExp ;
        ImageView bImage ;
    }

    public CustomPromoAdapter(Context context,int layoutResourceId, ArrayList<PromObject> promosIn) {
        this.layoutResourceId = layoutResourceId ;
        this.context = context;
        promotions = promosIn ;

    }


    public PromObject getItem(int position){
        return promotions.get(position);
    }
    public int getCount(){
        return  promotions.size();
    }

    public long getItemId(int position){
        return  position ;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder ;
        View promoIcon = convertView ; // inflate the layout for single promoObject
        if( promoIcon == null){
            viewHolder = new ViewHolder() ;
            LayoutInflater inflater = LayoutInflater.from(context);
            promoIcon = inflater.inflate(layoutResourceId,parent,false);

            viewHolder.bName = (TextView) promoIcon.findViewById(R.id.storeName);
            viewHolder.bExp = (TextView) promoIcon.findViewById(R.id.expDate);
            viewHolder.bImage = (ImageView) promoIcon.findViewById(R.id.businessImage);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag() ;
        }
        final  PromObject promo = getItem(position);
        // get a refrence to different view element we want to update

        // gets a reference to the different view elements we want to update
        bName = (TextView) promoIcon.findViewById(R.id.storeName);
        bExp = (TextView ) promoIcon.findViewById(R.id.expDate);
        bImage = (ImageView) promoIcon.findViewById(R.id.businessImage);

        // get the data from the data array


        // setting the view to reflect the data we need to display

        viewHolder.bName.setText(promo.getBusinessName());
        viewHolder.bExp.setText(promo.promoDateTime());
        Picasso.with(context)
                .load(promo.getbusinessPhoto())
                .into(viewHolder.bImage);
        //byte[] businessPhoto = promo.getbusinessPhoto();
        //Bitmap Businessbmp = BitmapFactory.decodeByteArray(businessPhoto, 0,businessPhoto.length);
        //viewHolder.bImage.setImageBitmap(Businessbmp);






        return  promoIcon ;
    }
}
