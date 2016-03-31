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

import java.util.ArrayList;

/**
 * Created by klay2 on 3/30/16.
 * plug and play, set image to some drawable or something at first.
 * mess with formatting and layout.
 */
public class CustomPromoAdapter extends BaseAdapter {
    private ArrayList<PromObject> promotions;
    private Context context;

    private static class ViewHolder{
        TextView expiration;
        ImageView image1;//the business image not the promotion itself
        //maybe button for delete or somthing
    }

    public CustomPromoAdapter(Context context, ArrayList<PromObject> promosIn){
        promotions = promosIn;
        this.context = context;
    }

    public int getCount(){return promotions.size();}

    public PromObject getItem(int position){return promotions.get(position);}

    public long getItemId(int position){return position;}//need this for some reason dunno why

    public View getView(int position,View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_promobject_layout,parent,false);

            viewHolder.expiration = (TextView) convertView.findViewById(R.id.expirationText);
            viewHolder.image1 = (ImageView) convertView.findViewById(R.id.businessImageView);

            //TODO:if we add any buttons initialize them here
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final PromObject promo = getItem(position);

        viewHolder.expiration.setText("Expires: " + promo.getExpirationDay()+":"+promo.getExpirationHour());//TODO: better expiration formatting

        //found how to do this here: http://stackoverflow.com/questions/13854742/byte-array-of-image-into-imageview
        byte[] businessPhoto = promo.getBusinessPhoto();
        Bitmap Businessbmp = BitmapFactory.decodeByteArray(businessPhoto, 0, businessPhoto.length);
        viewHolder.image1.setImageBitmap(Businessbmp);

        //TODO:  implement on click whatever stuff listener compactly right here!

        return convertView;
    }

}
