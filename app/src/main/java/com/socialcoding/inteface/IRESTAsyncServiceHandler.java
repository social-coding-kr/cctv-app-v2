package com.socialcoding.inteface;

import com.socialcoding.models.CCTVLocationData;
import com.socialcoding.models.CCTVLocationDetailData;
import com.socialcoding.models.CCTVLocationResource;

import java.util.List;

/**
 * Created by cloverhearts on 2016-12-06.
 */
public interface IRESTAsyncServiceHandler {
  public interface ICCTVLocationResponse {
    public void onSuccess(List<CCTVLocationData> cctvLocationDatas);
    public void onError();
  }
  public interface ICCTVDetailResponse {
    public void onSuccess(CCTVLocationDetailData cctvDetailInformation);
    public void onError();
  }
  public interface ICCTVRegisterResponse {
    public void onSuccess();
    public void onError();
  }
}
