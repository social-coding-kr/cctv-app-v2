package com.socialcoding.inteface;

import com.socialcoding.models.CCTVLocationDetailResource;
import com.socialcoding.models.CCTVLocationResource;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

/**
 * Created by cloverhearts on 2016-11-26.
 */
public interface IRESTServerHandler {
  @GET("map/cctvs?")
  Call<CCTVLocationResource> getCCTVLocations(@Query("east") String east,
                                              @Query("north") String north,
                                              @Query("south") String south,
                                              @Query("west") String west);

  @GET("cctv/{cctvId}")
  Call<CCTVLocationDetailResource> getCCTVDetail(@Path("cctvId") long cctvId);

}
