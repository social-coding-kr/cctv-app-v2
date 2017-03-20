package com.socialcoding.cctv;

import static org.junit.Assert.assertTrue;

import com.socialcoding.http.CCTVHttpHandlerV1;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvClustersResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvDetailResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvLocationsResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvRegisterResponse;
import com.socialcoding.intefaces.IServerResource;
import com.socialcoding.models.CctvCluster;
import com.socialcoding.models.CctvLocation;
import com.socialcoding.models.CctvLocationDetail;
import com.socialcoding.resources.CctvLocationDetailResource;
import com.socialcoding.resources.CctvLocationsResource;
import java.io.File;
import java.util.List;
import java.util.Locale;
import org.junit.Test;

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
    CctvLocationsResource locationInfo = serverResource.getCctvLocations(east, north, south, west);

    assertTrue(locationInfo.getStatus().equals("SUCCESS"));

    for (CctvLocation cctv : locationInfo.getCctvs()) {
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
    serverResource.getCctvLocationsAsync(east, north, south, west, new ICctvLocationsResponse() {
      @Override
      public void onSuccess(List<CctvLocation> cctvLocations) {
        for (CctvLocation cctv : cctvLocations) {
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
  public void testGetClustersAsync() throws Exception {
    double east = 127.003622;
    double north = 37.572985;
    double south = 37.561648;
    double west = 126.949195;

    IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
    serverResource.getCctvClustersAsync(east, north, south, west, new ICctvClustersResponse() {
      @Override
      public void onSuccess(List<CctvCluster> cctvClusters) {
        for (CctvCluster cluster : cctvClusters) {
          System.out.println(cluster);
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
    CctvLocationDetailResource response = serverResource.getCctvDetail(cctvId);
    assertTrue(response.getStatus().equals("SUCCESS"));
    CctvLocationDetail cctv = response.getCctv();
    System.out.println(String.format(Locale.US, "cctv detail cctv id = %d - %s %s", cctv.getCctvId(), cctv.getAddress(), cctv.getSource()));
  }

  @Test
  public void testGetCCTVInformationAsync() throws Exception {
    final long cctvId = 9516;
    IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
    serverResource.getCctvDetailAsync(cctvId, new ICctvDetailResponse() {
      @Override
      public void onSuccess(CctvLocationDetail cctv) {
        System.out.println(String.format(Locale.US, "cctv detail cctv id = %d - %s %s", cctv.getCctvId(), cctv.getAddress(), cctv.getSource()));
      }

      @Override
      public void onError() {
        System.out.println("ERROR] Async getCctvDetail Test");
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
    serverResource.registerCctv(latitude, longitude, cctvImage, noticeImage);
  }

  //@Test
  public void testRegisterCCTVAsync() throws Exception {
    File cctvImage = new File("");
    File noticeImage = new File("");
    Double latitude = 127.003622;
    Double longitude = 127.003622;

    IServerResource serverResource = new CCTVHttpHandlerV1(baseUrl);
    serverResource.registerCctvAsync(latitude, longitude, cctvImage, noticeImage, new ICctvRegisterResponse() {
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