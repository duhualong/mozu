//package org.eenie.wgj.ui.meeting.launchmeeting;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ExpandableListView;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import org.eenie.wgj.R;
//import org.eenie.wgj.model.response.meeting.MeetingPeopleNew;
//
//import java.util.ArrayList;
//
///**
// * Created by Eenie on 2017/8/8 at 17:40
// * Email: 472279981@qq.com
// * Des:
// */
//
//public class SelectHostAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener {
//    private Context context;
//
//    private ArrayList<MeetingPeopleNew> mData;
//
//
//    public SelectHostAdapter(Context context, ArrayList<MeetingPeopleNew> data) {
//        this.context = context;
//        mData = data;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        GroupViewHolder gvh;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_expend_list_view_host, null);
//            gvh = new GroupViewHolder();
//            gvh.groupText = (TextView) convertView.findViewById(R.id.item_department);
//            gvh.imgExpend = (ImageView) convertView.findViewById(R.id.item_img_expend);
//            convertView.setTag(gvh);
//
//        } else {
//            gvh = (GroupViewHolder) convertView.getTag();
//        }
//        gvh.groupText.setText(mData.get(groupPosition).getName());
//
//
//        if (isExpanded) {
//
//            gvh.imgExpend.setImageResource(R.mipmap.ic_expand);
//        } else {
//
//            gvh.imgExpend.setImageResource(R.mipmap.ic_collapse);
//        }
//        return convertView;
//    }
//
//
//
//
//
//    public class GroupViewHolder {
//        TextView groupText;
//        ImageView imgExpend;
//
//    }
//
//
//    @Override
//    public int getGroupCount() {
//        return mData.size();
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return mData.get(groupPosition).getUsers().size();
//    }
//
//    @Override
//    public Object getGroup(int groupPosition) {
//        return mData.get(groupPosition);
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return mData.get(groupPosition).getUsers().get(childPosition);
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return true;
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return true;
//    }
//
//
//
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
//                             View convertView, ViewGroup parent) {
//        ItemViewHolder ivh;
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_expend_item_host, null);
//            ivh = new ItemViewHolder();
//            ivh.itemText = (TextView) convertView.findViewById(R.id.item_host_name);
//            ivh.rlItemHost = (RelativeLayout) convertView.findViewById(R.id.rl_item_host);
//            convertView.setTag(ivh);
//        } else {
//            ivh = (ItemViewHolder) convertView.getTag();
//        }
//        ivh.itemText.setText(mData.get(groupPosition).getUsers().
//                get(childPosition).getName());
//
//        ivh.rlItemHost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        return convertView;
//    }
//
//
//
//
//    public class ItemViewHolder {
//        TextView itemText;
//        RelativeLayout rlItemHost;
//
//    }
//    @Override
//    public boolean onChildClick(ExpandableListView parent, View v,
//                                int groupPosition, int childPosition, long id) {
//        return true;
//    }
//
//}
