package org.eenie.wgj.ui.meeting.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.UnStartMeeting;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/11 at 19:25
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingProgressFragment extends BaseSupportFragment {
    @BindView(R.id.rv_meeting_unstart)RecyclerView mRecyclerView;
    private NormalRoutingAdapter mAdapter;
    @Override
    protected int getContentView() {
        return R.layout.fargment_arranege_unstart;
    }

    @Override
    protected void updateUI() {

        mAdapter = new NormalRoutingAdapter(context, new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);

        initData();

    }

    private void initData() {
        mAdapter.clear();
        mSubscription=mRemoteService.getMeetingProgressList(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                           List<UnStartMeeting>  mData =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<List<UnStartMeeting>>() {
                                            }.getType());
                            if (mData!=null){
                                if (mAdapter!=null){
                                    mAdapter.clear();
                                }
                                mAdapter.addAll(mData);

                            }
                        }else {
                        }

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }



    class NormalRoutingAdapter extends RecyclerView.Adapter<NormalRoutingAdapter.ProjectViewHolder> {
        private Context context;
        private List<UnStartMeeting> mMeetings;


        public NormalRoutingAdapter(Context context, List<UnStartMeeting>
                mMeetings) {
            this.context = context;
            this.mMeetings = mMeetings;

        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_meeting_arrange, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mMeetings != null && !mMeetings.isEmpty()) {


                UnStartMeeting data = mMeetings.get(position);
                holder.setItem(data);
                if (data!=null){
                    if (!TextUtils.isEmpty(data.getId_card_head_image())){
                        Glide.with(context).load(Constant.DOMIN+data.getId_card_head_image()).
                                centerCrop().into(holder.mCircleImageView);
                    }
                    if (!TextUtils.isEmpty(data.getName())){
                        holder.itemTitle.setText(data.getName());
                    }
                    if (!TextUtils.isEmpty(data.getUsername())){
                        holder.itemUserName.setText(data.getUsername());
                    }
                    if (!TextUtils.isEmpty(data.getState())){
                        holder.itemStatus.setText(data.getState());

                    }
                    if (!TextUtils.isEmpty(data.getStart())){
                        holder.itemTime.setText(data.getStart());

                    }
                }



            }

        }

        @Override
        public int getItemCount() {
            return mMeetings.size();
        }

        public void addAll(List<UnStartMeeting> projectList) {
            this.mMeetings.addAll(projectList);
            NormalRoutingAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mMeetings.clear();
            NormalRoutingAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private UnStartMeeting mStartMeeting;
            private RelativeLayout mRelativeLayout;
            private CircleImageView mCircleImageView;

            private TextView itemTitle;
            private TextView itemUserName;
            private TextView itemStatus;
            private TextView itemTime;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                mCircleImageView = ButterKnife.findById(itemView, R.id.img_avatar);
                itemTitle = ButterKnife.findById(itemView, R.id.item_name);
                itemUserName = ButterKnife.findById(itemView, R.id.item_username);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_detail);
                itemStatus = ButterKnife.findById(itemView, R.id.item_status);
                itemTime = ButterKnife.findById(itemView, R.id.item_start_time);
                mRelativeLayout.setOnClickListener(this);

            }

            public void setItem(UnStartMeeting meeting) {
                mStartMeeting = meeting;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_detail:
                        startActivity(new Intent(context,MeetingProgressDetailActivity.class)
                                .putExtra(MeetingProgressDetailActivity.APPLY_ID,
                                        String.valueOf(mStartMeeting.getId())));
                        break;


                }

            }


        }
    }


}
