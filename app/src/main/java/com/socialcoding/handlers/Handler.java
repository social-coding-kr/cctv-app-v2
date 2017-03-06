package com.socialcoding.handlers;

import com.socialcoding.http.CCTVHttpHandlerDummy;

/**
 * Created by yoon on 2016. 10. 26..
 */
public class Handler {
    public static final PermissionHandler permissionHandler = new PermissionHandler();
    public static final CCTVHttpHandlerDummy cctvHttpHandler = new CCTVHttpHandlerDummy("http://cctvs.nineqs.com/");
}
