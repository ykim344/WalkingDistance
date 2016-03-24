package com.example.yeonjun.walkingdistance;

import java.io.Serializable;

/**
 * Created by klay2 on 3/24/16.
 */
public class PromObject implements Serializable {
    //int for expiration day of month and hour of day. kept as a primitve and not a date type
    //so that it can be easily serializable and saved to disk and received as JSON data
    //some task will have to check every hour and on startup for expired promo objects and delete them.
    private int expirationDay;//day of month starts at 1
    private int expirationHour;//hour of day military time
    private byte[] businessPhoto;//byte arrays are used here  for photos because you have to use byte arrays
    private byte[] promoPhoto;//to get/save images from disk and to get from JSON, they can be converted to bitmaps in the activity
    private String businessName;  //might not need this
    private long id; //unique id for promo item may not be necessary

    //null empty constructor
    public PromObject(){
        expirationDay = 0;//maybe set to -1 for no expiration
        expirationHour = 0;//maybe set to -1 for no expiration
        businessPhoto = null;
        promoPhoto = null;
        businessName = null;
        id = -1;

    }

    public PromObject(long id, String businessName, int expirationDay, int expirationHour, byte[] businessPhoto,byte[] promoPhoto){
       this.id = id;
        this.businessName = businessName;
        this.expirationDay = expirationDay;
        this.expirationHour = expirationHour;
        this.businessPhoto = businessPhoto;
        this.promoPhoto = promoPhoto;

    }

    public long getId(){
        return id;
    }

    public String getBusinessName(){
        return businessName;
    }

    public int getExpirationDay(){
        return expirationDay;
    }

    public int getExpirationHour(){
        return expirationHour;
    }

    public byte[] getbusinessPhoto(){
        return businessPhoto;
    }

    public byte[] getPromoPhoto(){
        return promoPhoto;
    }

}
