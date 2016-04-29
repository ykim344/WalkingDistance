package com.example.yeonjun.walkingdistance;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * Created by klay2 on 3/24/16.
 */
public class PromObject implements Serializable {
    //int for expiration day of month and hour of day. kept as a primitve and not a date type
    //so that it can be easily serializable and saved to disk and received as JSON data
    //some task will have to check every hour and on startup for expired promo objects and delete them.
    private int expirationDay;//day of month starts at 1
    private int expirationHour;//hour of day military time
    private String promoPhoto;//to get/save images from disk and to get from JSON, they can be converted to bitmaps in the activity
    private String businessPhoto;
    private String businessName;  //might not need this
    private double longitude;
    private double latitude;
    private long id; //unique id for promo item may not be necessary

    //null empty constructor
    public PromObject(){
        expirationDay = 0;//maybe set to -1 for no expiration
        expirationHour = 0;//maybe set to -1 for no expiration
        promoPhoto = null;
        promoPhoto = null;
        businessName = null;
        id = -1;

    }

    public PromObject(long id, String businessName, int expirationDay, int expirationHour, String businessPhoto, String promoPhoto,double longitude,double latitude){
        this.id = id;
        this.businessName = businessName;
        this.expirationDay = expirationDay;
        this.expirationHour = expirationHour;
        this.promoPhoto = promoPhoto;
        this.businessPhoto = businessPhoto;
        this.longitude = longitude;
        this.latitude = latitude;

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

    public String getbusinessPhoto(){
        //String urlBphoto = "https://storage.cloud.google.com/" + "walkingdistance/Bimage/" + promoPhoto  ;
        return businessPhoto;
    }

    public String getPromoPhoto(){
        //String urlBpromo = "https://storage.cloud.google.com/" + "walkingdistance/Bpromo/" + promoPhoto  ;

        return promoPhoto;
    }

    public double getlatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String promoDateTime() {
        String ending ;
        if(expirationDay == 1 ||expirationDay == 21 ||expirationDay == 31){
           ending = "st" ;
        }
        if(expirationDay == 2 ||expirationDay == 22 ){
            ending = "nd" ;
        }
        if(expirationDay == 3 ||expirationDay == 23 ){
            ending = "rd" ;
        }
        else{
            ending = "th";
        }
        String amOrPm;

        if(expirationDay > 31){
            return null ;
        }

        if (expirationHour < 12 ) {
            amOrPm = "am" ;
            return "Expires: " + expirationHour+ " at " + expirationDay;
        }
        if (expirationHour >= 12 && expirationHour < 25) {
           expirationHour = expirationHour % 12 ;

           amOrPm = "pm" ;

           if(expirationHour == 12) {
               amOrPm = "Noon";

               return "Expires: " + expirationHour + amOrPm + " on the " + expirationDay + ending;
           }
           if (expirationHour == 24){
               amOrPm = "Midnight";
               return "Expires: " + expirationHour + amOrPm + " on the " + expirationDay + ending;
           }
            else {
               return "Expires: " + expirationHour + amOrPm + " on the " + expirationDay + ending;
           }
        }

        return null ;
    }
}
