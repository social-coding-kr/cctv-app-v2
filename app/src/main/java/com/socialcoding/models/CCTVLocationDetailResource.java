package com.socialcoding.models;

import java.util.List;

/**
 * Created by cloverhearts on 2016-12-06.
 */
public class CCTVLocationDetailResource {
    private String status;
    private List<CCTVLocationDetailData> cctv;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<CCTVLocationDetailData> getCctv() {
    return cctv;
  }

  public void setCctv(List<CCTVLocationDetailData> cctv) {
    this.cctv = cctv;
  }
}
