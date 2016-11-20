package com.socialcoding.models;

/**
 * Created by yoon on 2016. 11. 20..
 */

public class CCTVLocation {
    int cctvId;
    boolean isPublic;
    Double latitude;
    Double longitude;

    public CCTVLocation(int cctvId, boolean isPublic, Double latitude, Double longitude) {
        this.cctvId = cctvId;
        this.isPublic = isPublic;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
