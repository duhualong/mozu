package org.eenie.wgj.ui.routinginspection.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.model.MapMarkerResponse;
import org.eenie.wgj.util.Constant;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eenie on 2017/7/1 at 13:27
 * Email: 472279981@qq.com
 * Des:
 */

public class CustomInfoWindowAdapter implements AMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_marker, null);
        setViewContent(marker,view);
        return view;
    }
    //这个方法根据自己的实体信息来进行相应控件的赋值
    private void setViewContent(Marker marker,View view) {
        //实例：
        MapMarkerResponse storeInfo = (MapMarkerResponse) marker.getObject();
        CircleImageView ivPic = (CircleImageView) view.findViewById(R.id.img_avatar);
        Glide.with(context).load(Constant.DOMIN+storeInfo.getAvatarUrl()).centerCrop().into(ivPic);
        TextView tvPoint = (TextView) view.findViewById(R.id.marker_point);
        int mPosition=storeInfo.getPosition()+1;
        if (mPosition<=9){
            tvPoint.setText("巡检点位：0"+mPosition);
        }else {
            tvPoint.setText("巡检点位："+mPosition);
        }
        TextView tvPersonal= (TextView) view.findViewById(R.id.marker_personal);
        tvPersonal.setText("巡检人员："+storeInfo.getUsername());
        TextView tvTime= (TextView) view.findViewById(R.id.marker_time);
        tvTime.setText("巡检时间："+storeInfo.getTime());
        TextView tvStatus=(TextView) view.findViewById(R.id.marker_status);
        tvStatus.setText("巡检状况："+storeInfo.getHappening());
        TextView tvLocation=(TextView) view.findViewById(R.id.marker_location);
        tvLocation.setText("巡检地点："+storeInfo.getInspectionname());
        TextView tvAddress = (TextView) view.findViewById(R.id.marker_address);
        tvAddress.setText(storeInfo.getLocation_name());

    }

    //提供了一个给默认信息窗口定制内容的方法。如果用自定义的布局，不用管这个方法。
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
