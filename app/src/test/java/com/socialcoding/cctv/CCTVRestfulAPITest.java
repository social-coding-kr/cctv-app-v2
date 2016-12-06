package com.socialcoding.cctv;

import com.socialcoding.http.CCTVHttpHandlerV1;
import com.socialcoding.inteface.IRESTAsyncServiceHandler;
import com.socialcoding.inteface.IServerResource;
import com.socialcoding.models.CCTVLocationData;
import com.socialcoding.models.CCTVLocationDetailData;
import com.socialcoding.models.CCTVLocationDetailResource;
import com.socialcoding.models.CCTVLocationResource;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CCTVRestfulAPITest {
    final String baseUrl = "http://cctvs.nineqs.com";

    @Test
    public void testGetLocations() throws Exception {
        double east = 127.003622;
        double north = 37.572985;
        double south = 37.561648;
        double west = 126.949195;

        IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
        CCTVLocationResource locationInfo = serverResource.getCCTVLocations(east, north, south, west);

        assertTrue(locationInfo.getStatus().equals("SUCCESS"));

        for (CCTVLocationData cctv : locationInfo.getCctvs()) {
            System.out.println(
                String.format(Locale.US, "cctv id %d, latitude %f, longitube %f, source %s", cctv.getCctvId(), cctv.getLatitude(), cctv.getLongitude(), cctv.getSource())
            );
        }
    }

    @Test
    public void testGetLocationsAsync() throws Exception {
        double east = 127.003622;
        double north = 37.572985;
        double south = 37.561648;
        double west = 126.949195;

        IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
        serverResource.getCCTVLocationsAsync(east, north, south, west, new IRESTAsyncServiceHandler.ICCTVLocationResponse() {
            @Override
            public void onSuccess(List<CCTVLocationData> cctvLocationDatas) {
                for (CCTVLocationData cctv : cctvLocationDatas) {
                    System.out.println(
                        String.format(Locale.US, "cctv id %d, latitude %f, longitube %f, source %s", cctv.getCctvId(), cctv.getLatitude(), cctv.getLongitude(), cctv.getSource())
                    );
                }
            }

            @Override
            public void onError() {
                System.out.println("ERROR] Async getCCTVLocation Test");
                assertTrue(false);
            }
        });

        Thread.sleep(10000);
    }

    @Test
    public void testGetCCTVInformation() throws Exception {
        final long cctvId = 9516;
        IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
        CCTVLocationDetailResource response = serverResource.getCCTVDetail(cctvId);
        assertTrue(response.getStatus().equals("SUCCESS"));
        CCTVLocationDetailData cctv = response.getCctv();
        System.out.println(String.format(Locale.US, "cctv detail cctv id = %d - %s %s", cctv.getCctvId(), cctv.getAddress(), cctv.getSource()));
    }

    @Test
    public void testGetCCTVInformationAsync() throws Exception {
        final long cctvId = 9516;
        IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
        serverResource.getCCTVDetailAsync(cctvId, new IRESTAsyncServiceHandler.ICCTVDetailResponse() {
            @Override
            public void onSuccess(CCTVLocationDetailData cctv) {
                System.out.println(String.format(Locale.US, "cctv detail cctv id = %d - %s %s", cctv.getCctvId(), cctv.getAddress(), cctv.getSource()));
            }

            @Override
            public void onError() {
                System.out.println("ERROR] Async getCCTVDetail Test");
                assertTrue(false);
            }
        });

        Thread.sleep(10000);
    }

    //@Test
    public void testRegisterCCTV() throws Exception {
        File cctvImage = new File("");
        File noticeImage = new File("");
        Double latitude = 127.003622;
        Double longitude = 127.003622;

        IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
        serverResource.registerCCTV(latitude, longitude, cctvImage, noticeImage);
    }

    //@Test
    public void testRegisterCCTVAsync() throws Exception {
        File cctvImage = new File("");
        File noticeImage = new File("");
        Double latitude = 127.003622;
        Double longitude = 127.003622;

        IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
        serverResource.registerCCTVAsync(latitude, longitude, cctvImage, noticeImage, new IRESTAsyncServiceHandler.ICCTVRegisterResponse() {
            @Override
            public void onSuccess() {
                assertTrue(true);
            }

            @Override
            public void onError() {
                assertTrue(false);
            }
        });

        Thread.sleep(100000);
    }

}