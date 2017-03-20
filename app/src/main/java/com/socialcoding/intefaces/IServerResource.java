package com.socialcoding.intefaces;

import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvClustersResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvDetailResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvLocationsResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvRegisterResponse;
import com.socialcoding.resources.CctvLocationDetailResource;
import com.socialcoding.resources.CctvLocationsResource;
import java.io.File;
import java.io.IOException;

/**
 * Created by cloverhearts on 2016-12-06.
 */
public interface IServerResource {
  /**
   * get cctv locations from server.
   * @param east
   * @param north
   * @param south
   * @param west
   * @return CctvLocationsResource
   */
  CctvLocationsResource getCctvLocations(
      Double east,
      Double north,
      Double south,
      Double west) throws IOException;

  /**
   * (async) get cctv locations from server.
   * @param east
   * @param north
   * @param south
   * @param west
   * @param callback
   * @throws IOException
   */
  void getCctvLocationsAsync(
      Double east,
      Double north,
      Double south,
      Double west,
      ICctvLocationsResponse callback) throws IOException;

  void getCctvClustersAsync(
      Double east,
      Double north,
      Double south,
      Double west,
      ICctvClustersResponse callback) throws IOException;

  CctvLocationDetailResource getCctvDetail(long cctvId) throws IOException;

  void getCctvDetailAsync(long cctvId, ICctvDetailResponse callback) throws IOException;

  void registerCctv(
      Double latitudeData,
      Double longitudeData,
      File cctvImageData,
      File noticeImageData) throws IOException;

  void registerCctvAsync(
      Double latitudeData,
      Double longitudeData,
      File cctvImageData,
      File noticeImageData,
      ICctvRegisterResponse callback) throws IOException;
}
