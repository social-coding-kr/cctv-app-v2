package com.socialcoding.inteface;

import com.socialcoding.models.CCTVLocationDetailResource;
import com.socialcoding.models.CCTVLocationResource;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
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
