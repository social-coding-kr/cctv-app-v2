package com.socialcoding.http;

import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.inteface.HttpHandler;
import com.socialcoding.inteface.HttpListener;
import com.socialcoding.inteface.HttpParams;
import com.socialcoding.inteface.IRESTServerHandler;
import com.socialcoding.inteface.IRESTServiceHandler;
import com.socialcoding.inteface.IServerResource;
import com.socialcoding.models.CCTVDetail;
import com.socialcoding.models.CCTVLocationResource;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public void getCCTVLocationsAsync(Double east, Double north, Double south, Double west, final IRESTServiceHandler.ICCTVLocationResponse callback) throws IOException {
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
}

