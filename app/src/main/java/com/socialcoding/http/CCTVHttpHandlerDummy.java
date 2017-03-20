package com.socialcoding.http;

import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.intefaces.HttpHandler;
import com.socialcoding.intefaces.HttpListener;
import com.socialcoding.intefaces.HttpParams;
import com.socialcoding.intefaces.IRESTServerHandler;
import com.socialcoding.models.CctvDetail;
import com.socialcoding.models.CctvLocation;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by darkg on 2016-03-26.
 */
public class CCTVHttpHandlerDummy implements HttpHandler {

    protected IRESTServerHandler server;

    public CCTVHttpHandlerDummy(String baseUrl) {

    }

    @Override
    public void connect(String url, int port) throws SocialCodeException {

    }

    @Override
    public void disconnect() throws SocialCodeException {

    }

    @Override
    public void setListener(HttpListener listener) throws SocialCodeException {

    }

    @Override
    public void request(HTTP_TYPE type, HttpParams params) throws SocialCodeException {

    }


    public ArrayList<CctvLocation> getCctvLocations(Double lat, Double lon) {
        CctvLocation l1 = new CctvLocation(1, true, 37.5667151, 126.9781312);
        CctvLocation l2 = new CctvLocation(2, false, 37.5677151, 126.9781312);
        CctvLocation l3 = new CctvLocation(3, true, 37.5687151, 126.9781312);
        CctvLocation l4 = new CctvLocation(4, false, 37.5697151, 126.9781312);
        ArrayList<CctvLocation> cctvLocations = new ArrayList<>();
        cctvLocations.add(l1);
        cctvLocations.add(l2);
        cctvLocations.add(l3);
        cctvLocations.add(l4);
        return cctvLocations;
    }

    public CctvDetail getCctvDetails(int cctvId) {
        return new CctvDetail(true, null, null, 37.5667151, 126.9781312);
    }

    public boolean uploadCctv(File cctvImage, File infoImage, boolean isPublic, Double lat, Double lon) {
        return true;
    }
}
