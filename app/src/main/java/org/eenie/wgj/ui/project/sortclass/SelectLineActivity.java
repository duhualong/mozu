package org.eenie.wgj.ui.project.sortclass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.RoundWayList;
import org.eenie.wgj.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/8 at 11:11
 * Email: 472279981@qq.com
 * Des:
 */

public class SelectLineActivity extends BaseActivity {
    public static final String PROJECT_ID = "id";
    public static final String GROUND = "ground";
    public static final String POSITION = "position";
    @BindView(R.id.recycler_select_line)
    RecyclerView mRecyclerView;
    private String projectId;
    private RoundWayAdapter adapter;
    private String ground;
    private String position;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_line;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        ground = getIntent().getStringExtra(GROUND);
        position = getIntent().getStringExtra(POSITION);
        getData();

    }

    private void getData() {


        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getLineList(token, projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<RoundWayList> postWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<List<RoundWayList>>() {
                                        }.getType());
                                if (postWorkLists != null) {
                                    adapter = new RoundWayAdapter(context, postWorkLists);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                    mRecyclerView.setLayoutManager(layoutManager);
                                    mRecyclerView.setAdapter(adapter);
                                }


                            }
                        }
                    }

                });
    }

    @OnClick({R.id.img_back, R.id.tv_no_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_no_select:

                RoundWayList data = new RoundWayList();
                Intent mIntent = new Intent();
                mIntent.putExtra("info", data);
                mIntent.putExtra("ground",ground);
                mIntent.putExtra("position",position);
                // 设置结果，并进行传送
                setResult(2, mIntent);
                finish();

                break;
        }
    }


    class RoundWayAdapter extends RecyclerView.Adapter<RoundWayAdapter.RoundWayViewHolder> {
        private Context context;
        private List<RoundWayList> contactsList;

        public RoundWayAdapter(Context context, List<RoundWayList> contactsList) {
            this.context = context;
            this.contactsList = contactsList;
        }

        @Override
        public RoundWayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_select_line, parent, false);
            return new RoundWayViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RoundWayViewHolder holder, int position) {
            if (contactsList != null && !contactsList.isEmpty()) {
                RoundWayList data = contactsList.get(position);
                holder.setItem(data);
                if (data != null) {
                    position = position + 1;
                    holder.itemPosition.setText(String.valueOf(position));
                    holder.itemLine.setText(data.getName());

                }

            }

        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public void addAll(List<RoundWayList> contactsList) {
            this.contactsList.addAll(contactsList);
            RoundWayAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.contactsList.clear();
            RoundWayAdapter.this.notifyDataSetChanged();
        }

        class RoundWayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView itemPosition;
            private TextView itemLine;
            private RelativeLayout rlLine;

            private RoundWayList mReportPostList;

            public RoundWayViewHolder(View itemView) {

                super(itemView);

                itemPosition = ButterKnife.findById(itemView, R.id.item_number);
                itemLine = ButterKnife.findById(itemView, R.id.item_line);
                rlLine = ButterKnife.findById(itemView, R.id.rl_select_line);
                rlLine.setOnClickListener(this);


            }

            public void setItem(RoundWayList data) {
                mReportPostList = data;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_select_line:
                        RoundWayList data = new RoundWayList(mReportPostList.getId(),
                                mReportPostList.getName());
                        Intent mIntent = new Intent();
                        mIntent.putExtra("info", data);
                        mIntent.putExtra("ground",ground);
                        mIntent.putExtra("position",position);
                        // 设置结果，并进行传送
                        setResult(2, mIntent);
                        SelectLineActivity.this.finish();

                        break;


                }


            }
        }
    }


}
