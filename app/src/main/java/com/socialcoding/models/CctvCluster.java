package com.socialcoding.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by clsan on 21/03/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class CctvCluster {
  @AllArgsConstructor(suppressConstructorProperties = true)
  @Data
  private class Location {
    private Double latitude;
    private Double longitude;
  }
  private String clusterId;
  private String clusterName;
  private Location location;
  private Integer count;
}
