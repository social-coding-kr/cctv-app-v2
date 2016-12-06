package com.socialcoding.cctv;

import com.socialcoding.http.CCTVHttpHandlerV1;
import com.socialcoding.inteface.IRESTServiceHandler;
import com.socialcoding.inteface.IServerResource;
import com.socialcoding.models.CCTVLocationData;
import com.socialcoding.models.CCTVLocationResource;
import okhttp3.Request;
import org.junit.Test;
import retrofit2.Call;

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
        serverResource.getCCTVLocationsAsync(east, north, south, west, new IRESTServiceHandler.ICCTVLocationResponse() {
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


}