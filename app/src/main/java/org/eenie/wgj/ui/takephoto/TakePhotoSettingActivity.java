package org.eenie.wgj.ui.takephoto;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.TakePhotoApiResponse;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/11 at 14:43
 * Email: 472279981@qq.com
 * Des:
 */

public class TakePhotoSettingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_take_photo)
    RecyclerView mRecyclerView;
    private TakePhotoAdapter mAdapter;
    @BindView(R.id.root_view)View rootView;
    @Override
    protected int getContentView() {
        return R.layout.activity_take_photo_setting;
    }

    @Override
    protected void updateUI() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new TakePhotoAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

    }
    @OnClick({R.id.img_back,R.id.img_add_photo})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();

                break;

            case R.id.img_add_photo:
                startActivity(new Intent(context,AddTakePhotoActivity.class));


                break;

        }
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getTakePhoto(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {
                        cancelRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        cancelRefresh();
                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getResultCode() == 200) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ArrayList<TakePhotoApiResponse> data = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<TakePhotoApiResponse>>() {
                                    }.getType());

                            if (data != null && !data.isEmpty()) {
                                if (mAdapter != null) {
                                    mAdapter.addAll(data);
                                }
                            }
                        } else {

                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }

                });

    }


    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    class TakePhotoAdapter extends RecyclerView.Adapter<TakePhotoAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<TakePhotoApiResponse> mTakePhotoApiResponseArrayList;

        public TakePhotoAdapter(Context context,
                                ArrayList<TakePhotoApiResponse> mTakePhotoApiResponseArrayList) {
            this.context = context;
            this.mTakePhotoApiResponseArrayList = mTakePhotoApiResponseArrayList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_take_photo, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mTakePhotoApiResponseArrayList != null && !mTakePhotoApiResponseArrayList.isEmpty()) {
                TakePhotoApiResponse data = mTakePhotoApiResponseArrayList.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getCreated_at())){
                        holder.itemDate.setText(data.getCreated_at());
                    }else {
                        holder.itemDate.setText("无");
                    }
                    if (!TextUtils.isEmpty(data.getTitle())){
                        holder.itemTitle.setText(data.getTitle());
                    }else {
                        holder.itemTitle.setText("无");

                    }


                }

            }

        }

        @Override
        public int getItemCount() {
            return mTakePhotoApiResponseArrayList.size();
        }

        public void addAll(ArrayList<TakePhotoApiResponse> takePhotoApiResponses) {
            this.mTakePhotoApiResponseArrayList.addAll(takePhotoApiResponses);
            TakePhotoAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mTakePhotoApiResponseArrayList.clear();
            TakePhotoAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView itemTitle;
            private TextView itemDate;
            private ImageView imgRight;

            private TakePhotoApiResponse mTakePhotoApiResponse;
            private RelativeLayout rlTakePhoto;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemTitle = ButterKnife.findById(itemView, R.id.item_title);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                imgRight=ButterKnife.findById(itemView,R.id.img_right);
                rlTakePhoto=ButterKnife.findById(itemView,R.id.rl_take_photo);

                rlTakePhoto.setOnClickListener(this);
                imgRight.setOnClickListener(this);

            }

            public void setItem(TakePhotoApiResponse takePhotoApiResponse) {
                mTakePhotoApiResponse = takePhotoApiResponse;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.img_right:
                    case R.id.rl_take_photo:
                        Intent intent=new Intent(TakePhotoSettingActivity.this,
                                TakePhotoDetailActivity.class);
                        intent.putExtra(TakePhotoDetailActivity.INFO,mTakePhotoApiResponse);
                        startActivity(intent);


                        break;

                }


            }
        }
    }

}
