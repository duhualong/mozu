package org.eenie.wgj.ui.message;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.requset.NoticeMessage;
import org.eenie.wgj.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/4 at 18:20
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeMessageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.root_view)View rootView;
    @BindView(R.id.notice_swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private NoticeAdapter meetingListAdapter;
    @BindView(R.id.recycler_to_do)RecyclerView mRecyclerView;
    private List<NoticeMessage> result;
    @BindView(R.id.title)TextView title;
    @Override
    protected int getContentView() {
        return R.layout.activity_to_do_notice;
    }

    @Override
    protected void updateUI() {
        title.setText("通知");
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        meetingListAdapter=new NoticeAdapter(context,new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(meetingListAdapter);

    }
    @OnClick({R.id.img_back})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onRefresh() {
        meetingListAdapter.clear();
        //  String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");
        mSubscription=mRemoteService.getNotice(Constant.TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(listApiResponse -> {
                    result = new ArrayList<>();
                    if (listApiResponse.getResultCode() == 200) {
                        List<NoticeMessage> orderList = listApiResponse.getData();
                        if (orderList != null && !orderList.isEmpty()) {
                            for (NoticeMessage order : orderList) {
                                result.add(order);
                            }
                        }
                    }
                    return Single.just(result);
                })
                .subscribe(meetingList -> {
                    cancelRefresh();
                    if (meetingList != null && !meetingList.isEmpty()) {
                        if (meetingListAdapter != null) {
                            meetingListAdapter.addAll(meetingList);
                        }
                    }
                });

    }


    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
    @Override public void onResume() {
        super.onResume();
        onRefresh();
    }

    class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
        private Context context;
        private List<NoticeMessage> meetingList;

        public NoticeAdapter(Context context, List<NoticeMessage> meetingList) {
            this.context = context;
            this.meetingList = meetingList;
        }

        @Override
        public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_notice, parent, false);
            return new NoticeViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NoticeViewHolder holder, int position) {
            if (meetingList != null && !meetingList.isEmpty()) {
                NoticeMessage noticeMessage = meetingList.get(position);
                holder.setItem(noticeMessage);
                String meetingName=noticeMessage.getAlert();
                if (!TextUtils.isEmpty(meetingName)){
                    holder.meetingContent.setText(meetingName);
                }
                String applyDate=noticeMessage.getCreated_at();
                if (!TextUtils.isEmpty(applyDate)){
                    holder.applyDate.setText(applyDate);
                }


            }

        }

        @Override
        public int getItemCount() {
            return meetingList.size();
        }

        public void addAll(List<NoticeMessage> meetingList) {
            this.meetingList.addAll(meetingList);
            NoticeAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.meetingList.clear();
            NoticeAdapter.this.notifyDataSetChanged();
        }
        class NoticeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView applyDate;
            private TextView meetingContent;
            private TextView meetingDetail;
            private NoticeMessage mMeetingNotice;


            public NoticeViewHolder(View itemView) {

                super(itemView);
                applyDate= ButterKnife.findById(itemView,R.id.item_apply_date);
                meetingContent=ButterKnife.findById(itemView,R.id.item_meeting_name);
                meetingDetail=ButterKnife.findById(itemView,R.id.item_look_detail);
                meetingDetail.setOnClickListener(this);

            }
            public void setItem(NoticeMessage meetingNotice) {
                mMeetingNotice = meetingNotice;
            }

            @Override
            public void onClick(View v) {


            }
        }
    }
}
