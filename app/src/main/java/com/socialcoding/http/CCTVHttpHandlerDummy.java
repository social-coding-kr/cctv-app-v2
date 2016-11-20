package com.socialcoding.http;

import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.inteface.HttpHandler;
import com.socialcoding.inteface.HttpListener;
import com.socialcoding.inteface.HttpParams;
import com.socialcoding.models.CCTVDetail;
import com.socialcoding.models.CCTVLocation;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by darkg on 2016-03-26.
 */
public class CCTVHttpHandlerDummy implements HttpHandler {
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

    public ArrayList<CCTVLocation> getCctvLocations(Double lat, Double lon) {
        CCTVLocation l1 = new CCTVLocation(1, true, 37.5667151, 126.9781312);
        CCTVLocation l2 = new CCTVLocation(2, false, 37.5677151, 126.9781312);
        CCTVLocation l3 = new CCTVLocation(3, true, 37.5687151, 126.9781312);
        CCTVLocation l4 = new CCTVLocation(4, false, 37.5697151, 126.9781312);
        ArrayList<CCTVLocation> cctvLocations = new ArrayList<>();
        cctvLocations.add(l1);
        cctvLocations.add(l2);
        cctvLocations.add(l3);
        cctvLocations.add(l4);
        return cctvLocations;
    }

    public CCTVDetail getCctvDetails(int cctvId) {
        return new CCTVDetail(true, null, null, 37.5667151, 126.9781312);
    }

    public boolean uploadCctv(File cctvImage, File infoImage, boolean isPublic, Double lat, Double lon) {
        return true;
    }
}
