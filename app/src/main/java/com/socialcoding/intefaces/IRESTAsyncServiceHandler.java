package com.socialcoding.intefaces;

import com.socialcoding.models.CctvCluster;
import com.socialcoding.models.CctvLocation;
import com.socialcoding.models.CctvLocationDetail;
import java.util.List;

/**
 * Created by cloverhearts on 2016-12-06.
 */
public interface IRESTAsyncServiceHandler {
  public interface ICctvLocationsResponse {
    public void onSuccess(List<CctvLocation> cctvLocations);
    public void onError();
  }
  public interface ICctvClustersResponse {
    public void onSuccess(List<CctvCluster> cctvClusters);
    public void onError();
  }
  public interface ICctvDetailResponse {
    public void onSuccess(CctvLocationDetail cctvDetailInformation);
    public void onError();
  }
  public interface ICctvRegisterResponse {
    public void onSuccess();
    public void onError();
  }
}
