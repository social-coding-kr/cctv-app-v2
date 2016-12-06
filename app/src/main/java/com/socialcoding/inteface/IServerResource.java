package com.socialcoding.inteface;

import com.socialcoding.models.CCTVLocationDetailResource;
import com.socialcoding.models.CCTVLocationResource;

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
   * @return CCTVLocationResource
   */
  CCTVLocationResource getCCTVLocations(Double east,
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
  void getCCTVLocationsAsync(Double east,
                             Double north,
                             Double south,
                             Double west,
                             IRESTAsyncServiceHandler.ICCTVLocationResponse callback) throws IOException;

  CCTVLocationDetailResource getCCTVDetail(long cctvId) throws IOException;

  void getCCTVDetailAsync(long cctvId, IRESTAsyncServiceHandler.ICCTVDetailResponse callback) throws IOException;
}
