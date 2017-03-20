package com.socialcoding.intefaces;

import com.socialcoding.models.CctvCluster;
import com.socialcoding.resources.CctvLocationDetailResource;
import com.socialcoding.resources.CctvLocationsResource;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by cloverhearts on 2016-11-26.
 */
public interface IRESTServerHandler {
  @GET("map/cctvs?")
  Call<CctvLocationsResource> getCctvLocations(
      @Query("east") String east,
      @Query("north") String north,
      @Query("south") String south,
      @Query("west") String west);

  @GET("api/v2/maps/clusters?")
  Call<List<CctvCluster>> getCctvClusters(
      @Query("east") String east,
      @Query("north") String north,
      @Query("south") String south,
      @Query("west") String west);

  @GET("cctv/{cctvId}")
  Call<CctvLocationDetailResource> getCctvDetail(@Path("cctvId") long cctvId);

  @Multipart
  @POST("cctv")
  Call<ResponseBody> registerCctv(
      @PartMap() Map<String, RequestBody> fields,
      @Part MultipartBody.Part cctvImage,
      @Part MultipartBody.Part noticeImage);

}
