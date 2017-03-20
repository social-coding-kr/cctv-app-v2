package com.socialcoding.models;

import java.io.File;
import lombok.AllArgsConstructor;

/**
 * Created by yoon on 2016. 11. 20..
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class CctvDetail {
  private boolean isPublic;
  private File cctvImage;
  private File infoImage;
  private Double latitude;
  private Double longitude;
}
