package com.socialcoding.http;

import com.socialcoding.inteface.IRESTServerHandler;
import com.socialcoding.inteface.IRESTAsyncServiceHandler;
import com.socialcoding.inteface.IServerResource;
import com.socialcoding.models.CCTVLocationDetailResource;
import com.socialcoding.models.CCTVLocationResource;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;

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
    public CCTVLocationResource getCCTVLocations(Double east, Double north, Double south, Double west) throws IOException {
        String sEast = String.valueOf(east);
        String sNorth = String.valueOf(north);
        String sSouth = String.valueOf(south);
        String sWest = String.valueOf(west);

        return server.getCCTVLocations(sEast, sNorth, sSouth, sWest).execute().body();
    }

    @Override
    public void getCCTVLocationsAsync(Double east, Double north, Double south, Double west, final IRESTAsyncServiceHandler.ICCTVLocationResponse callback) throws IOException {
        String sEast = String.valueOf(east);
        String sNorth = String.valueOf(north);
        String sSouth = String.valueOf(south);
        String sWest = String.valueOf(west);

        Call<CCTVLocationResource> call = server.getCCTVLocations(sEast, sNorth, sSouth, sWest);
        call.enqueue(new Callback<CCTVLocationResource>() {
            @Override
            public void onResponse(Call<CCTVLocationResource> call, Response<CCTVLocationResource> response) {
                callback.onSuccess(response.body().getCctvs());
            }

            @Override
            public void onFailure(Call<CCTVLocationResource> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public CCTVLocationDetailResource getCCTVDetail(long cctvId) throws IOException {
        return server.getCCTVDetail(cctvId).execute().body();
    }

    @Override
    public void getCCTVDetailAsync(long cctvId, final IRESTAsyncServiceHandler.ICCTVDetailResponse callback) throws IOException {
        server.getCCTVDetail(cctvId).enqueue(new Callback<CCTVLocationDetailResource>() {
            @Override
            public void onResponse(Call<CCTVLocationDetailResource> call, Response<CCTVLocationDetailResource> response) {
                callback.onSuccess(response.body().getCctv());
            }

            @Override
            public void onFailure(Call<CCTVLocationDetailResource> call, Throwable t) {
                callback.onError();
            }
        });
    }
}

