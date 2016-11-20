package com.socialcoding.models;

import java.io.File;

/**
 * Created by yoon on 2016. 11. 20..
 */

public class CCTVDetail {
    boolean isPublic;
    File cctvImage;
    File infoImage;
    Double latitude;
    Double longitude;

    public CCTVDetail(boolean isPublic, File cctvImage, File infoImage, Double latitude, Double longitude) {
        this.isPublic = isPublic;
        this.cctvImage = cctvImage;
        this.infoImage = infoImage;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
