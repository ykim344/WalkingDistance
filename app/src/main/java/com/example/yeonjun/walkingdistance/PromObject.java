package com.example.yeonjun.walkingdistance;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Created by klay2 on 3/24/16.
 */
@Entity
public class PromObject implements Serializable {
    //int for expiration day of month and hour of day. kept as a primitve and not a date type
    //so that it can be easily serializable and saved to disk and received as JSON data
    //some task will have to check every hour and on startup for expired promo objects and delete them.
    private String expirationDay;//day of month starts at 1
    private String expirationHour;//hour of day military time
    @Index private String latitude;
    @Index private String longitude;
    private String bpImages;//to get/save images from disk and to get from JSON, they can be converted to bitmaps in the activity
    private String businessName;  //might not need this
    @Id private long id; //unique id for promo item may not be necessary

    //null empty constructor
    public PromObject(){
        expirationDay = null;
        expirationHour = null;//maybe set to -1 for no expiration
        latitude = null;
        longitude = null;
        bpImages = null;
        bpImages = null;
        businessName = null;
        id = -1;

    }

    public PromObject( String businessName, String expirationDay, String expirationHour, String urlPhotos , String Long, String Lat){

        this.businessName = businessName;
        this.expirationDay = expirationDay;
        this.expirationHour = expirationHour;
        this.bpImages = urlPhotos;
        this.longitude = Long;
        this.latitude = Lat ;

    }

    public long getId(){
        return id;
    }

    public String getBusinessName(){
        return businessName;
    }

    public String getExpirationDay(){
        return expirationDay;
    }

    public String getExpirationHour(){
        return expirationHour;
    }

    public String getbusinessPhoto(){
        String urlBphoto =  bpImages  ;
        return urlBphoto;
    }

    public String getPromoPhoto(){
        String urlBpromo =  bpImages  ;

        return urlBpromo;
    }

    public String getLongitude(){
        return longitude ;
    }

    public String getLatitude() {
        return latitude;
    }

    public String promoDateTime(){

        return expirationDay + " at " + expirationHour ;
    }

}
