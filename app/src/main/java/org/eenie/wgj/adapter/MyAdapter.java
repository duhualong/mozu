package org.eenie.wgj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.Contacts;
import org.eenie.wgj.util.Sidebar;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Eenie on 2017/4/12 at 9:54
 * Email: 472279981@qq.com
 * Des:
 */

public class MyAdapter extends BaseAdapter implements SectionIndexer {
    private Context mContext;
    private List<Contacts> mContacts;
    public MyAdapter(Context context,List<Contacts> contacts){
        mContext = context;
        mContacts = contacts;
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_right, null);
            viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.contacts= (RelativeLayout) convertView.findViewById(R.id.item_contacts);
            viewHolder.surname= (TextView) convertView.findViewById(R.id.tv_surname);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0){
            viewHolder.tvIndex.setVisibility(View.VISIBLE);
        }else if (mContacts.get(position).getFirstAlphabet().charAt(0) != mContacts.get(position - 1).getFirstAlphabet().charAt(0)){
            viewHolder.tvIndex.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tvIndex.setVisibility(View.GONE);
        }

        viewHolder.tvIndex.setText(mContacts.get(position).getFirstAlphabet());
        viewHolder.name.setText(mContacts.get(position).getName());
        viewHolder.surname.setText(mContacts.get(position).getName().substring(0,1));

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return Arrays.copyOf(Sidebar.alphabets, Sidebar.alphabets.length);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i=0; i<getCount(); i++){
            if (((String)getSections()[sectionIndex]).charAt(0) == mContacts.get(i).getFirstAlphabet().charAt(0)){
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    private class ViewHolder{
         TextView tvIndex;
         RelativeLayout contacts;
         TextView surname;
         TextView name;
    }
}