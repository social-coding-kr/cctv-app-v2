package com.socialcoding.adapters;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.socialcoding.cctv.MainActivity;
import com.socialcoding.cctv.R;
import com.socialcoding.fragments.GoogleMapFragment;
import com.socialcoding.models.CctvLocationDetail;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.List;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by yoon on 2017. 1. 15..
 */
@Data
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class CctvInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
  @NonNull
  private MainActivity mainActivity;

  private Marker clicked;
  private CctvLocationDetail cctvDetailInformation;


  @Override
  public View getInfoWindow(Marker marker) {
    return null;
  }

  @Override
  public View getInfoContents(Marker marker) {
    View v = mainActivity.getLayoutInflater().inflate(R.layout.info_window_detail_layout, null);
    TableLayout txtLayout = (TableLayout) v.findViewById(R.id.info_detail_item);

    for (Field field : cctvDetailInformation.getClass().getDeclaredFields()){
      field.setAccessible(true);
      Object value = null;
      try {
        value = field.get(cctvDetailInformation);
        if(value != null) {
          System.out.println(field.getName()+","+value);

          TableRow tr = new TableRow(mainActivity);
          TextView txtKey = new TextView(mainActivity);
          TextView txtValue = new TextView(mainActivity);

          txtKey.setText(field.getName());
          txtValue.setText(value.toString());
          txtKey.setTextSize(11);
          txtKey.setTypeface(Typeface.DEFAULT_BOLD);
          txtValue.setTextSize(10);
          txtValue.setTypeface(Typeface.SANS_SERIF);
          txtValue.setGravity(Gravity.LEFT);
          txtValue.setPadding(40, 0, 0, 0);

          TableLayout.LayoutParams tableRowParams=
                  new TableLayout.LayoutParams
                          (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

          int leftMargin = 0;
          int topMargin = 2;
          int rightMargin = 0;
          int bottomMargin = 2;

          tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

          tr.setLayoutParams(tableRowParams);
          tr.addView(txtKey);
          tr.addView(txtValue);

          txtLayout.addView(tr);
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    return v;
  }
}
