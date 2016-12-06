package com.socialcoding.models;

import java.util.List;

/**
 * Created by cloverhearts on 2016-12-06.
 */
public class CCTVLocationDetailResource {
    private String status;
    private CCTVLocationDetailData cctv;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public CCTVLocationDetailData getCctv() {
    return cctv;
  }

  public void setCctv(CCTVLocationDetailData cctv) {
    this.cctv = cctv;
  }
}
