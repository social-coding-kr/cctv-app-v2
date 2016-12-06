package com.socialcoding.inteface;

import com.socialcoding.models.CCTVLocationDetailResource;
import com.socialcoding.models.CCTVLocationResource;
import retrofit2.Call;
import retrofit2.http.Path;

import java.io.IOException;
import java.util.List;

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
                             IRESTServiceHandler.ICCTVLocationResponse callback) throws IOException;

  CCTVLocationDetailResource getCCTVDetail(long cctvId) throws IOException;

  void getCCTVDetailAsync(long cctvId, IRESTServiceHandler.ICCTVDetailResponse callback) throws IOException;
}
