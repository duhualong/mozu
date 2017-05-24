package org.eenie.wgj.ui.project.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.LocationAddress;

import java.util.List;

/**
 * Created by Eenie on 2017/5/23 at 15:30
 * Email: 472279981@qq.com
 * Des:
 */

public class SearchAddressAdapter extends BaseAdapter {
    private Context context;
    private List<LocationAddress> searchAddressList;

    public SearchAddressAdapter(Context context, List<LocationAddress> searchAddressList) {
        this.context = context;
        this.searchAddressList = searchAddressList;
    }

    public void clear() {
        searchAddressList.clear();
        this.notifyDataSetChanged();
    }

    @Override public int getCount() {
        return searchAddressList != null ? searchAddressList.size() : 0;
    }
    @Override public Object getItem(int position) {
        return position;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView detailAddress;
        TextView regionAddress;
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.item_choose_address, parent, false);
        }
        detailAddress = (TextView) convertView.findViewById(R.id.detailAddress);
        regionAddress = (TextView) convertView.findViewById(R.id.regionAddress);
        String serviceName = searchAddressList.get(position).getName();
        String serviceLogo =
                searchAddressList.get(position).getCity() + "-" + searchAddressList.get(position)
                        .getDistrict();
        detailAddress.setText(serviceName);
        regionAddress.setText(serviceLogo);
        return convertView;
    }

}
