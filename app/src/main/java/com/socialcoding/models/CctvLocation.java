package com.socialcoding.models;

import lombok.Data;

/**
 * Created by yoon on 2016. 11. 20..
 */
@Data
public class CctvLocation {
    final transient String PUBLIC_CCTV_TYPE = "PUBLIC";
    final transient String PRIVATE_CCTV_TYPE = "PRIVATE";
    private int cctvId;
    private String source;
    private Double latitude;
    private Double longitude;

    public CctvLocation(int cctvId, boolean isPublic, Double latitude, Double longitude) {
        this.cctvId = cctvId;
        if (isPublic) {
            this.source = PUBLIC_CCTV_TYPE;
        } else {
            this.source = PRIVATE_CCTV_TYPE;
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
