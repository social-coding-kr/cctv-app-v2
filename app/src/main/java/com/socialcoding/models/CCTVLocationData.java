package com.socialcoding.models;

/**
 * Created by yoon on 2016. 11. 20..
 */

public class CCTVLocationData {
    final transient String PUBLIC_CCTV_TYPE = "PUBLIC";
    final transient String PRIVATE_CCTV_TYPE = "PRIVATE";
    private int cctvId;
    private String source;
    private Double latitude;
    private Double longitude;

    public CCTVLocationData(int cctvId, boolean isPublic, Double latitude, Double longitude) {
        this.cctvId = cctvId;
        if (isPublic == true) {
            this.source = PUBLIC_CCTV_TYPE;
        } else {
            this.source = PRIVATE_CCTV_TYPE;
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getCctvId() {
        return cctvId;
    }

    public void setCctvId(int cctvId) {
        this.cctvId = cctvId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setPublicCCTV() {
        this.source = PUBLIC_CCTV_TYPE;
    }

    public void setPrivateCCTV() {
        this.source = PRIVATE_CCTV_TYPE;
    }

    public boolean isPublicCCTV() {
        return this.getSource().equals(PUBLIC_CCTV_TYPE);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
