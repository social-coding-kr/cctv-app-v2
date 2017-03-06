package com.socialcoding.http;

import com.google.android.gms.maps.model.LatLng;
import com.socialcoding.cctv.MainActivity;
import com.socialcoding.cctv.R;
import com.socialcoding.fragment.GoogleMapFragment;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

/**
 * Created by yoon on 2017. 3. 7..
 */
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class GooglePlaceTextsearchHttpHandler implements Runnable {
    @NonNull
    private MainActivity mainActivity;

    @NonNull
    private String searchText;


    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/textsearch/json" +
                        "?query=" + searchText +
                        "&key=" + mainActivity.getResources().getString(R.string.webservice_key))
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            Double lat = (Double) jsonObject.getJSONArray("results").getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location").get("lat");
            Double lng = (Double) jsonObject.getJSONArray("results").getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location").get("lng");
            final LatLng latLng = new LatLng(lat, lng);

            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    ((GoogleMapFragment) mainActivity.googleMapFragment).moveCamera(latLng);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}