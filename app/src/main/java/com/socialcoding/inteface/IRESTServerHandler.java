package com.socialcoding.inteface;

import com.socialcoding.models.CCTVLocationDetailResource;
import com.socialcoding.models.CCTVLocationResource;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

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

  @Multipart
  @POST("cctv")
  Call<ResponseBody> registerCCTV(@PartMap() Map<String, RequestBody> fields,
                                  @Part MultipartBody.Part cctvImage,
                                  @Part MultipartBody.Part noticeImage);

}
