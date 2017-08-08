package org.eenie.wgj.ui.meeting.launchmeeting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.meeting.MeetingPeopleNew;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/12 at 17:59
 * Email: 472279981@qq.com
 * Des:
 */

public class SelectPeopleRecordAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener {
    private Context context;

    private ArrayList<MeetingPeopleNew> mData;


    public SelectPeopleRecordAdapter(Context context, ArrayList<MeetingPeopleNew> data) {
        this.context = context;
        mData = data;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder gvh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expend_list_view_report, null);
            gvh = new GroupViewHolder();
            gvh.groupText = (TextView) convertView.findViewById(R.id.item_report_department);
            gvh.imgExpend = (ImageView) convertView.findViewById(R.id.item_img_expend);
            gvh.itemCheckAll = (CheckBox) convertView.findViewById(R.id.item_checkbox_select_all);
            convertView.setTag(gvh);

        } else {
            gvh = (GroupViewHolder) convertView.getTag();
        }
        gvh.groupText.setText(mData.get(groupPosition).getName());
        if (mData.get(groupPosition).isChecked()) {
            gvh.itemCheckAll.setChecked(true);

        } else {
            gvh.itemCheckAll.setChecked(false);
        }
        // 點擊 CheckBox 時，將狀態存起來
        gvh.itemCheckAll.setOnClickListener(new GroupCheckBoxClick(groupPosition));

        if (isExpanded) {

            gvh.imgExpend.setImageResource(R.mipmap.ic_expand);
        } else {

            gvh.imgExpend.setImageResource(R.mipmap.ic_collapse);
        }
        return convertView;
    }



    class GroupCheckBoxClick implements View.OnClickListener {
        private int groupPosition;

        GroupCheckBoxClick(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        public void onClick(View v) {
            mData.get(groupPosition).toggle();
            // 將 Children 的 isChecked 全面設成跟 Group 一樣
            int childrenCount = mData.get(groupPosition).getUsers().size();
            boolean groupIsChecked = mData.get(groupPosition).isChecked();
            for (int i = 0; i < childrenCount; i++)
                mData.get(groupPosition).getUsers().get(i).setCheck(groupIsChecked);

            notifyDataSetChanged();
        }
    }

    public class GroupViewHolder {
        TextView groupText;
        ImageView imgExpend;
        CheckBox itemCheckAll;
    }


    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(groupPosition).getUsers().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getUsers().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }




    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ItemViewHolder ivh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expend_item_report, null);
            ivh = new ItemViewHolder();
            ivh.itemText = (TextView) convertView.findViewById(R.id.item_report_name);
            ivh.itemCheckBox = (CheckBox) convertView.findViewById(R.id.item_checkbox_select_item);
            ivh.mRelativeLayout=(RelativeLayout) convertView.findViewById(R.id.rl_item_expend_item);
            convertView.setTag(ivh);
        } else {
            ivh = (ItemViewHolder) convertView.getTag();
        }
        ivh.itemText.setText(mData.get(groupPosition).getUsers().
                get(childPosition).getName());
        if (mData.get(groupPosition).getUsers().get(childPosition).isCheck()) {
            ivh.itemCheckBox.setChecked(true);
        } else {
            ivh.itemCheckBox.setChecked(false);
        }
        ivh.mRelativeLayout.setOnClickListener(new ChildCheckBoxClick(groupPosition, childPosition));

        return convertView;
    }


    /**
     * 勾選 Child CheckBox 時，存 Child CheckBox 的狀態
     */
    class ChildCheckBoxClick implements View.OnClickListener {
        private int groupPosition;
        private int childPosition;

        ChildCheckBoxClick(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        public void onClick(View v) {
            handleClick(childPosition, groupPosition);
        }
    }

    public void handleClick(int childPosition, int groupPosition) {
        mData.get(groupPosition).getUsers().get(childPosition).toggle();

        // 檢查 Child CheckBox 是否有全部勾選，以控制 Group CheckBox
        int childrenCount = mData.get(groupPosition).getUsers().size();
        boolean childrenAllIsChecked = true;
        for (int i = 0; i < childrenCount; i++) {
            if (!mData.get(groupPosition).getUsers().get(i).isCheck())
                childrenAllIsChecked = false;
        }

        mData.get(groupPosition).setChecked(childrenAllIsChecked);
        notifyDataSetChanged();
    }

    public class ItemViewHolder {
        TextView itemText;
        CheckBox itemCheckBox;
        RelativeLayout mRelativeLayout;

    }
    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        handleClick(childPosition, groupPosition);
        return true;
    }

}

