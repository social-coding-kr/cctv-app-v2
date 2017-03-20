package com.socialcoding.resources;

import com.socialcoding.models.CctvLocation;
import java.util.List;
import lombok.Data;

/**
 * Created by cloverhearts on 2016-12-06.
 */
@Data
public class CctvLocationsResource {
  private String status;
  private List<CctvLocation> cctvs;
}
