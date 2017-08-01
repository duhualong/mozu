package org.eenie.wgj.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.message.MessageRequestData;
import org.eenie.wgj.ui.message.ApplyFeedBackActivity;
import org.eenie.wgj.ui.message.NoticeDetailActivity;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/21 at 18:02
 * Email: 472279981@qq.com
 * Des:
 */

public class RedMessageFragment extends BaseSupportFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private int page = 1;
    private MyAdapter adapter;
    private int totalPager = 1;
    private List<MessageRequestData.DataBean> dataBean = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.fragment_unread_recyclerview;
    }

    @Override
    protected void updateUI() {
        adapter = new MyAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        getData(page);

    }

    private void getData(int currentPage) {
        if (currentPage <= totalPager) {


            mSubscription = mRemoteService.queryNewMessageNotice(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                    currentPage, 1)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                        @Override
                        public void onNext(ApiResponse apiResponse) {
                            if (apiResponse.getCode() == 0) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                MessageRequestData data = gson.fromJson(jsonArray,
                                        new TypeToken<MessageRequestData>() {
                                        }.getType());
                                totalPager = data.getLast_page();
                                page++;
                                if (data.getData() != null) {
                                    for (int i = 0; i < data.getData().size(); i++) {
                                        dataBean.add(data.getData().get(i));
                                    }

                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    adapter = new MyAdapter(context, dataBean);
                                    mRecyclerView.setAdapter(adapter);

                                    adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener(){
                                        @Override
                                        public void onItemClick(View view , int position){
                                            if (dataBean.get(position).getKey()==1101||dataBean.get(position).getKey()==2201){
                                                Intent intent = new Intent(context, ApplyFeedBackActivity.class);
                                                intent.putExtra(ApplyFeedBackActivity.APPLY_INFO, dataBean.get(position));
                                                startActivity(intent);
                                            }else {
                                                startActivity(new Intent(context, NoticeDetailActivity.class)
                                                        .putExtra(NoticeDetailActivity.INFO, dataBean.get(position)));
                                            }


                                        }
                                    });

                                }


                            }

                        }
                    });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置 Header 为 Material风格


        refreshLayout.setRefreshHeader(new MaterialHeader(context).setShowBezierWave(true));
//设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                getDatas(page);
                refreshlayout.finishRefresh(2000);

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (page > totalPager) {
                    refreshLayout.finishLoadmore();

                    Toast.makeText(context, "数据加载完毕", Toast.LENGTH_SHORT).show();
//
                } else {
                    getData(page);
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    private void getDatas(int currentPage) {
//        dataBean=new ArrayList<>();
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        adapter = new MyAdapter(context, dataBean);
//        mRecyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        for (int j=1;j<=currentPage;j++) {

            mSubscription = mRemoteService.queryNewMessageNotice(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                    j, 1)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                        @Override
                        public void onNext(ApiResponse apiResponse) {
                            if (apiResponse.getCode() == 0) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                MessageRequestData data = gson.fromJson(jsonArray,
                                        new TypeToken<MessageRequestData>() {
                                        }.getType());

                                if (data.getData() != null) {
                                    for (int i = 0; i < data.getData().size(); i++) {
                                        dataBean.add(data.getData().get(i));
                                    }

                                }


                            }

                        }
                    });
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyAdapter(context, dataBean);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){

                if (dataBean.get(position).getKey()==1101||dataBean.get(position).getKey()==2201){
                    Intent intent = new Intent(context, ApplyFeedBackActivity.class);
                    intent.putExtra(ApplyFeedBackActivity.APPLY_INFO, dataBean.get(position));
                    startActivity(intent);
                }else {
                    startActivity(new Intent(context, NoticeDetailActivity.class)
                            .putExtra(NoticeDetailActivity.INFO, dataBean.get(position)));
                }


            }
        });
    }

}
