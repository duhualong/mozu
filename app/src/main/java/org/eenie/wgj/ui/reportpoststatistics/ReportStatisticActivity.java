package org.eenie.wgj.ui.reportpoststatistics;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/4 at 20:51
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportStatisticActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String PROJECT_ID="id";
    private String projectId;
    @BindView(R.id.rv_report_statistic)
    RecyclerView mRvReportStatistic;
    @BindView(R.id.swipe_refresh_list)SwipeRefreshLayout mSwipeRefreshLayout;
    ReportStatisticAdapter mReportStatisticAdapter;

    List<ReportStatisticEntity> mReportStatisticEntities = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_routing_statistic;
    }


    @Override
    protected void updateUI() {

        projectId=getIntent().getStringExtra(PROJECT_ID);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mReportStatisticAdapter = new ReportStatisticAdapter(mReportStatisticEntities, projectId);
        mRvReportStatistic.setAdapter(mReportStatisticAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRvReportStatistic.setLayoutManager(layoutManager);
        mRvReportStatistic.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRvReportStatistic.setItemAnimator(new DefaultItemAnimator());
        mRvReportStatistic.setAdapter(mReportStatisticAdapter);

    }
    @OnClick(R.id.img_back)public  void onClick(){
        onBackPressed();
    }

    @Override
    public void onRefresh() {


        mSubscription=mRemoteService.getReportStatisticsList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onCompleted() {
                        cancelRefresh();

                    }

                    @Override
                    public void onError(Throwable e) {
                        cancelRefresh();
                        Toast.makeText(context,"网络加载失败",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            if (apiResponse.getData()!=null){
                                Gson gson=new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<ReportStatisticEntity> mData = gson.fromJson(jsonArray,
                                        new TypeToken<List<ReportStatisticEntity>>() {
                                        }.getType());
                                if (mData!=null&&!mData.isEmpty()){
                                    mReportStatisticEntities.clear();
                                    mReportStatisticEntities.addAll(mData);
                                    mReportStatisticAdapter.notifyDataSetChanged();
                                }

                            }

                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
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
}
