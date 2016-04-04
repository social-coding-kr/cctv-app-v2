package com.socialcoding.http;

import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.inteface.HttpHandler;
import com.socialcoding.inteface.HttpListener;
import com.socialcoding.inteface.HttpParams;

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
}
