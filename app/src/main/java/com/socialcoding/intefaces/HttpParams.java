package com.socialcoding.intefaces;

import com.socialcoding.exception.SocialCodeException;

/**
 * Created by darkg on 2016-03-26.
 */
public interface HttpParams {
    public void setParam(String key, String value, boolean overwrite) throws SocialCodeException;
    public String getParam(String key) throws SocialCodeException;
}
