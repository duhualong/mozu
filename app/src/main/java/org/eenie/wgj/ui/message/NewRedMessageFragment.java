package org.eenie.wgj.ui.message;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gxz.library.SwapRecyclerView;
import com.gxz.library.SwipeMenuBuilder;
import com.gxz.library.bean.SwipeMenu;
import com.gxz.library.bean.SwipeMenuItem;
import com.gxz.library.view.SwipeMenuView;
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
import org.eenie.wgj.model.response.message.DeleteMessage;
import org.eenie.wgj.model.response.message.MessageRequestData;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/8/4 at 13:47
 * Email: 472279981@qq.com
 * Des:
 */

public class NewRedMessageFragment extends BaseSupportFragment implements SwipeMenuBuilder {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.id_rv)
    SwapRecyclerView mRecyclerView;
    private int page = 1;
    private RecyclerAdapter adapter;
    private int totalPager = 1;
    private List<MessageRequestData.DataBean> dataBean = new ArrayList<>();

    private int pos;

    @Override
    protected int getContentView() {
        return R.layout.fragment_message_recyclerview_new;
    }

    @Override
    protected void updateUI() {


        getData(page);

    }

    private void getData(int currentPage) {
        if (currentPage <= totalPager) {


            mSubscription = mRemoteService.queryNewMessageNotice(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                    currentPage, 1)

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
                                    adapter = new RecyclerAdapter( dataBean,context);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                    mRecyclerView.setLayoutManager(layoutManager);
                                    mRecyclerView.setAdapter(adapter);

                                    mRecyclerView.setOnSwipeListener(new SwapRecyclerView.OnSwipeListener() {
                                        @Override
                                        public void onSwipeStart(int position) {
//                Toast.makeText(MainActivity.this,"onSwipeStart-"+position,Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onSwipeEnd(int position) {
//                Toast.makeText(MainActivity.this, "onSwipeEnd-" + position, Toast.LENGTH_SHORT).show();
                                            pos = position;
                                        }
                                    });
                                    adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                                            if (dataBean.get(position).getKey() == 1101 || dataBean.get(position).getKey() == 2201) {
                                                Intent intent = new Intent(context, ApplyFeedBackActivity.class);
                                                intent.putExtra(ApplyFeedBackActivity.APPLY_INFO, dataBean.get(position));
                                                startActivity(intent);
                                            } else {
                                                startActivity(new Intent(context, NoticeDetailActivity.class)
                                                        .putExtra(NoticeDetailActivity.INFO, dataBean.get(position)));
                                            }
                                        }

                                        @Override
                                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
//                Toast.makeText(MainActivity.this, "onItemLongClick-->>>"+list.get(position), Toast.LENGTH_LONG).show();
                                            return true;//!!!!!!!!!!!!
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
        for (int j = 1; j <= currentPage; j++) {

            mSubscription = mRemoteService.queryNewMessageNotice(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                    j, 1)

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
                                    adapter.addAll(dataBean);
                                    mRecyclerView.setOnSwipeListener(new SwapRecyclerView.OnSwipeListener() {
                                        @Override
                                        public void onSwipeStart(int position) {
//                Toast.makeText(MainActivity.this,"onSwipeStart-"+position,Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onSwipeEnd(int position) {
//                Toast.makeText(MainActivity.this, "onSwipeEnd-" + position, Toast.LENGTH_SHORT).show();
                                            pos = position;
                                        }
                                    });

                                    adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                                            if (dataBean.get(position).getKey() == 1101 || dataBean.get(position).getKey() == 2201) {
                                                Intent intent = new Intent(context, ApplyFeedBackActivity.class);
                                                intent.putExtra(ApplyFeedBackActivity.APPLY_INFO, dataBean.get(position));
                                                startActivity(intent);
                                            } else {
                                                startActivity(new Intent(context, NoticeDetailActivity.class)
                                                        .putExtra(NoticeDetailActivity.INFO, dataBean.get(position)));
                                            }
                                        }

                                        @Override
                                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
//                Toast.makeText(MainActivity.this, "onItemLongClick-->>>"+list.get(position), Toast.LENGTH_LONG).show();
                                            return true;//!!!!!!!!!!!!
                                        }

                                    });
                                }


                            }

                        }
                    });
        }





    }

    @Override
    public SwipeMenuView create() {
        SwipeMenu menu = new SwipeMenu(context);

        SwipeMenuItem item = new SwipeMenuItem(context);
//        item.setTitle("取消")
//                .setTitleColor(Color.WHITE)
//                .setBackground(new ColorDrawable(Color.BLUE))
//                .setTitleSize(14)
//        ;

        menu.addMenuItem(item);
        item = new SwipeMenuItem(context);
        item.setTitle("删除")
                .setTitleColor(Color.WHITE)
                .setTitleSize(14)
                .setBackground(new ColorDrawable(Color.RED));
        menu.addMenuItem(item);

        SwipeMenuView menuView = new SwipeMenuView(menu);

        menuView.setOnMenuItemClickListener(mOnSwipeItemClickListener);

        return menuView;
    }

    private SwipeMenuView.OnMenuItemClickListener mOnSwipeItemClickListener = new SwipeMenuView.OnMenuItemClickListener() {

        @Override
        public void onMenuItemClick(int pos, SwipeMenu menu, int index) {
            Toast.makeText(context, menu.getMenuItem(index).getTitle(), Toast.LENGTH_LONG).show();
            if (index == 0) {
                DeleteMessage request = new DeleteMessage(dataBean.get(pos).getId());

                mRecyclerView.smoothCloseMenu(pos);
                dataBean.remove(pos);
                adapter.remove(pos);
                mSubscription = mRemoteService.deleteMessageNotice(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                        request)
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
                                Toast.makeText(context, apiResponse.getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        });



            }
        }
    };
}
