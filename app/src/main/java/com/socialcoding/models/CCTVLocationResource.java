package com.socialcoding.models;

import java.util.List;

/**
 * Created by cloverhearts on 2016-12-06.
 */
public class CCTVLocationResource {
    private String status;
    private List<CCTVLocationData> cctvs;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<CCTVLocationData> getCctvs() {
    return cctvs;
  }

  public void setCctvs(List<CCTVLocationData> cctvs) {
    this.cctvs = cctvs;
  }
}
