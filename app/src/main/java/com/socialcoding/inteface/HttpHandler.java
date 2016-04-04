package com.socialcoding.inteface;

import com.socialcoding.exception.SocialCodeException;

/**
 * Created by darkg on 2016-03-26.
 */
public interface HttpHandler {

    enum HTTP_TYPE {
        GET,
        POST
    }

    public void connect(String url, int port) throws SocialCodeException;
    public void disconnect() throws SocialCodeException;
    public void setListener(HttpListener listener) throws SocialCodeException;
    public void request(HTTP_TYPE type, HttpParams params) throws SocialCodeException;
}
