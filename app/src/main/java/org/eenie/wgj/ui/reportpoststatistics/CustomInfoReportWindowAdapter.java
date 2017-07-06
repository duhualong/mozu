package org.eenie.wgj.ui.reportpoststatistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

import org.eenie.wgj.R;
import org.eenie.wgj.model.MapMarkerResponse;

/**
 * Created by Eenie on 2017/7/1 at 13:27
 * Email: 472279981@qq.com
 * Des:
 */

public class CustomInfoReportWindowAdapter implements AMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoReportWindowAdapter(Context context) {
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_marker_report, null);
        setViewContent(marker,view);
        return view;
    }
    //这个方法根据自己的实体信息来进行相应控件的赋值
    private void setViewContent(Marker marker,View view) {
        //实例：
        MapMarkerResponse storeInfo = (MapMarkerResponse) marker.getObject();
        TextView tvPersonal= (TextView) view.findViewById(R.id.marker_personal);
        tvPersonal.setText("报岗人员："+storeInfo.getUsername());
        TextView tvTime= (TextView) view.findViewById(R.id.marker_time);
        tvTime.setText("报岗时间："+storeInfo.getTime());
        TextView tvStatus=(TextView) view.findViewById(R.id.marker_status);
        tvStatus.setText("报岗情况："+storeInfo.getHappening());
        TextView tvLocation=(TextView) view.findViewById(R.id.marker_location);
        tvLocation.setText("报岗岗位："+storeInfo.getInspectionname());
        TextView tvAddress = (TextView) view.findViewById(R.id.marker_address);
        tvAddress.setText(storeInfo.getLocation_name());
    }

    //提供了一个给默认信息窗口定制内容的方法。如果用自定义的布局，不用管这个方法。
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
