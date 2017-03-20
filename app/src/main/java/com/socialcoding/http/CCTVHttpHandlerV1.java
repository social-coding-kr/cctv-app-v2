package com.socialcoding.http;

import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvClustersResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvDetailResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvLocationsResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvRegisterResponse;
import com.socialcoding.intefaces.IRESTServerHandler;
import com.socialcoding.intefaces.IServerResource;
import com.socialcoding.models.CctvCluster;
import com.socialcoding.resources.CctvLocationDetailResource;
import com.socialcoding.resources.CctvLocationsResource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by darkg on 2016-03-26.
 */
public class CCTVHttpHandlerV1 implements IServerResource {

  protected IRESTServerHandler server;

  public CCTVHttpHandlerV1(String baseUrl) {
    Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    server = retrofit.create(IRESTServerHandler.class);
  }

  @Override
  public CctvLocationsResource getCctvLocations(Double east, Double north, Double south, Double west) throws IOException {
    String sEast = String.valueOf(east);
    String sNorth = String.valueOf(north);
    String sSouth = String.valueOf(south);
    String sWest = String.valueOf(west);

    return server.getCctvLocations(sEast, sNorth, sSouth, sWest).execute().body();
  }

  @Override
  public void getCctvLocationsAsync(Double east, Double north, Double south, Double west, final ICctvLocationsResponse callback) throws IOException {
    String sEast = String.valueOf(east);
    String sNorth = String.valueOf(north);
    String sSouth = String.valueOf(south);
    String sWest = String.valueOf(west);

    Call<CctvLocationsResource> call = server.getCctvLocations(sEast, sNorth, sSouth, sWest);
    call.enqueue(new Callback<CctvLocationsResource>() {
      @Override
      public void onResponse(Call<CctvLocationsResource> call, Response<CctvLocationsResource> response) {
        callback.onSuccess(response.body().getCctvs());
      }

      @Override
      public void onFailure(Call<CctvLocationsResource> call, Throwable t) {
        callback.onError();
      }
    });
  }

  @Override
  public void getCctvClustersAsync(
      Double east,
      Double north,
      Double south,
      Double west,
      final ICctvClustersResponse callback) throws IOException {
    String sEast = String.valueOf(east);
    String sNorth = String.valueOf(north);
    String sSouth = String.valueOf(south);
    String sWest = String.valueOf(west);

    Call<List<CctvCluster>> call = server.getCctvClusters(sEast, sNorth, sSouth, sWest);
    call.enqueue(new Callback<List<CctvCluster>>() {
      @Override
      public void onResponse(Call<List<CctvCluster>> call, Response<List<CctvCluster>> response) {
        callback.onSuccess(response.body());
      }

      @Override
      public void onFailure(Call<List<CctvCluster>> call, Throwable t) {
        callback.onError();
      }
    });
  }

  @Override
  public CctvLocationDetailResource getCctvDetail(long cctvId) throws IOException {
    return server.getCctvDetail(cctvId).execute().body();
  }

  @Override
  public void getCctvDetailAsync(long cctvId, final ICctvDetailResponse callback) throws IOException {
    server.getCctvDetail(cctvId).enqueue(new Callback<CctvLocationDetailResource>() {
      @Override
      public void onResponse(Call<CctvLocationDetailResource> call, Response<CctvLocationDetailResource> response) {
        callback.onSuccess(response.body().getCctv());
      }

      @Override
      public void onFailure(Call<CctvLocationDetailResource> call, Throwable t) {
        callback.onError();
      }
    });
  }

  @Override
  public void registerCctv(Double latitudeData, Double longitudeData, File cctvImageData, File noticeImageData) throws IOException {
    RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(latitudeData.doubleValue()));
    RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(longitudeData.doubleValue()));

    HashMap<String, RequestBody> map = new HashMap<>();
    map.put("latitude", latitude);
    map.put("longitude", longitude);

    MultipartBody.Part cctvImage = prepareFilePart("cctvImage", cctvImageData);
    MultipartBody.Part noticeImage = null;
    if (noticeImageData != null) {
      noticeImage = prepareFilePart("noticeImage", noticeImageData);
    }

    Call<ResponseBody> call = server.registerCctv(map, cctvImage, noticeImage);
    call.execute().body();
  }

  @Override
  public void registerCctvAsync(Double latitudeData, Double longitudeData, File cctvImageData, File noticeImageData,
      final ICctvRegisterResponse callback) throws IOException {
    RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(latitudeData.doubleValue()));
    RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(longitudeData.doubleValue()));

    HashMap<String, RequestBody> map = new HashMap<>();
    map.put("latitude", latitude);
    map.put("longitude", longitude);

    MultipartBody.Part cctvImage = prepareFilePart("cctvImage", cctvImageData);
    MultipartBody.Part noticeImage = null;
    if (noticeImageData != null) {
      noticeImage = prepareFilePart("noticeImage", noticeImageData);
    }

    Call<ResponseBody> call = server.registerCctv(map, cctvImage, noticeImage);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        callback.onSuccess();
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        callback.onError();
      }
    });
  }

  private MultipartBody.Part prepareFilePart(String partName, File imageFile) {
    RequestBody requestFile =
        RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
    return MultipartBody.Part.createFormData(partName, imageFile.getName(), requestFile);
  }

}

