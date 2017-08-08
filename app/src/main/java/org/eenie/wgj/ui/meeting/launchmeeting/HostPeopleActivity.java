package org.eenie.wgj.ui.meeting.launchmeeting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.MeetingPeopleNew;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/10 at 16:09
 * Email: 472279981@qq.com
 * Des:
 */

public class HostPeopleActivity extends BaseActivity {

    public static final String TYPE = "type";
    private SelectHostAdapter adapter;
    @BindView(R.id.expand_list)
    ExpandableListView mExpandableListView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    String mTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_host;
    }

    @Override
    protected void updateUI() {
        mExpandableListView.setOnChildClickListener(adapter);
        mTitle = getIntent().getStringExtra(TYPE);
        switch (mTitle) {
            case "host":
                tvTitle.setText("主持人");


                break;
            case "record":
                tvTitle.setText("记录人");
                break;
        }
        initData();
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();

    }


    @Override
    public void onResume() {
        super.onResume();


    }


    private void initData() {

        mSubscription = mRemoteService.getMeetingPeopleList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<MeetingPeopleNew> mData =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<MeetingPeopleNew>>() {
                                                }.getType());
                                if (mData != null) {
                                    adapter = new SelectHostAdapter(context, mData);
                                    mExpandableListView.setAdapter(adapter);

                                }


                            }
                        }
                    }
                });

    }

//    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
//        private Context context;
//        private List<MeetingPeople> projectList;
//
//        public ProjectAdapter(Context context, List<MeetingPeople> projectList) {
//            this.context = context;
//            this.projectList = projectList;
//        }
//
//        @Override
//        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View itemView = inflater.inflate(R.layout.item_behavior, parent, false);
//            return new ProjectViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(ProjectViewHolder holder, int position) {
//            if (projectList != null && !projectList.isEmpty()) {
//                MeetingPeople data = projectList.get(position);
//                holder.setItem(data);
//                if (data != null) {
//                    holder.itemText.setText(data.getName());
//
//                }
////设置显示内容
//
//            }
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return projectList.size();
//        }
//
//        public void addAll(List<MeetingPeople> projectList) {
//            this.projectList.addAll(projectList);
//            ProjectAdapter.this.notifyDataSetChanged();
//        }
//
//        public void clear() {
//            this.projectList.clear();
//            ProjectAdapter.this.notifyDataSetChanged();
//        }
//
//        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            private TextView itemText;
//            private MeetingPeople mMeetingPeople;
//            private LinearLayout mLinearLayout;
//
//
//            public ProjectViewHolder(View itemView) {
//
//                super(itemView);
//                itemText = ButterKnife.findById(itemView, R.id.text_item);
//                mLinearLayout = ButterKnife.findById(itemView, R.id.line_item);
//                mLinearLayout.setOnClickListener(this);
//
//            }
//
//            public void setItem(MeetingPeople projectList) {
//                mMeetingPeople = projectList;
//            }
//
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.line_item:
//                        Intent mIntent = new Intent();
//                        mIntent.putExtra("mData", mMeetingPeople);
//                        // 设置结果，并进行传送
//                        setResult(RESULT_OK, mIntent);
//                        finish();
//                        break;
//
//                }
//
//
//            }
//        }
//    }

    class SelectHostAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener {
        private Context context;

        private ArrayList<MeetingPeopleNew> mData;


        public SelectHostAdapter(Context context, ArrayList<MeetingPeopleNew> data) {
            this.context = context;
            mData = data;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder gvh;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_expend_list_view_host, null);
                gvh = new GroupViewHolder();
                gvh.groupText = (TextView) convertView.findViewById(R.id.item_department);
                gvh.imgExpend = (ImageView) convertView.findViewById(R.id.item_img_expend);
                convertView.setTag(gvh);

            } else {
                gvh = (GroupViewHolder) convertView.getTag();
            }
            gvh.groupText.setText(mData.get(groupPosition).getName());


            if (isExpanded) {

                gvh.imgExpend.setImageResource(R.mipmap.ic_expand);
            } else {

                gvh.imgExpend.setImageResource(R.mipmap.ic_collapse);
            }
            return convertView;
        }


        public class GroupViewHolder {
            TextView groupText;
            ImageView imgExpend;

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
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.item_expend_item_host, null);
                ivh = new ItemViewHolder();
                ivh.itemText = (TextView) convertView.findViewById(R.id.item_host_name);
                ivh.rlItemHost = (RelativeLayout) convertView.findViewById(R.id.rl_item_host);
                convertView.setTag(ivh);
            } else {
                ivh = (ItemViewHolder) convertView.getTag();
            }
            if (mData.get(groupPosition).getUsers()!=null)
            ivh.itemText.setText(mData.get(groupPosition).getUsers().
                    get(childPosition).getName());
            ivh.rlItemHost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent();
                    if (mData.get(groupPosition).getUsers().get(childPosition)!=null){
                        mIntent.putExtra("mData", mData.get(groupPosition).getUsers().get(childPosition));
                    }
                    // 设置结果，并进行传送
                    setResult(RESULT_OK, mIntent);
                    finish();
                }
            });
            return convertView;
        }


        public class ItemViewHolder {
            TextView itemText;
            RelativeLayout rlItemHost;

        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            return true;
        }

    }


}