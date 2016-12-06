package com.socialcoding.models;

/**
 * Created by cloverhearts on 2016-12-06.
 */
public class CCTVLocationDetailData {
  final transient String PUBLIC_CCTV_TYPE = "PUBLIC";
  final transient String PRIVATE_CCTV_TYPE = "PRIVATE";

  private long cctvId;
  private String purpose;
  private String source;
  private String address;
  private String borough;
  private String dong;
  private String range;
  private String department;
  private String pixel;
  private String form;
  private String installedAt;

  public CCTVLocationDetailData(long cctvId,
                                String purpose,
                                boolean isPublic,
                                String address,
                                String borough,
                                String dong,
                                String range,
                                String department,
                                String pixel,
                                String form,
                                String installedAt) {
    this.cctvId = cctvId;
    this.purpose = purpose;
    this.address = address;
    this.borough = borough;
    this.dong = dong;
    this.range = range;
    this.department = department;
    this.pixel = pixel;
    this.form = form;
    this.installedAt = installedAt;

    if (isPublic == true) {
      this.source = PUBLIC_CCTV_TYPE;
    } else {
      this.source = PRIVATE_CCTV_TYPE;
    }

  }

  public boolean isPublic() {
    return this.source.equals(PUBLIC_CCTV_TYPE);
  }

  public String getPUBLIC_CCTV_TYPE() {
    return PUBLIC_CCTV_TYPE;
  }

  public String getPRIVATE_CCTV_TYPE() {
    return PRIVATE_CCTV_TYPE;
  }

  public long getCctvId() {
    return cctvId;
  }

  public void setCctvId(long cctvId) {
    this.cctvId = cctvId;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getBorough() {
    return borough;
  }

  public void setBorough(String borough) {
    this.borough = borough;
  }

  public String getDong() {
    return dong;
  }

  public void setDong(String dong) {
    this.dong = dong;
  }

  public String getRange() {
    return range;
  }

  public void setRange(String range) {
    this.range = range;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPixel() {
    return pixel;
  }

  public void setPixel(String pixel) {
    this.pixel = pixel;
  }

  public String getForm() {
    return form;
  }

  public void setForm(String form) {
    this.form = form;
  }

  public String getInstalledAt() {
    return installedAt;
  }

  public void setInstalledAt(String installedAt) {
    this.installedAt = installedAt;
  }
}
