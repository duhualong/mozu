package org.eenie.wgj.ui.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.message.MessageRequestData;
import org.eenie.wgj.util.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.eenie.wgj.R.id.main;
import static org.eenie.wgj.R.mipmap.ic_gift;

/**
 * Created by Eenie on 2017/8/7 at 11:18
 * Email: 472279981@qq.com
 * Des:
 */

public class NewDeleteMessageFragment extends Fragment {
//    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    RecyclerView mRecyclerView;
    private int page = 1;
    private int totalPager = 1;
    private List<MessageRequestData.DataBean> dataBean = new ArrayList<>();
    private MessageAdapter adapter;

//    @Override
//    protected int getContentView() {
//        return R.layout.fragment_message_recyclerview_new;
//    }
//
//    @Override
//    protected void updateUI() {
//        getData(page);
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_message_recyclerview_new,container,false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        refreshLayout= (SmartRefreshLayout) root.findViewById(R.id.refreshLayout);
        getData(page);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置 Header 为 Material风格


        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
//设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
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

                    Toast.makeText(getActivity(), "数据加载完毕", Toast.LENGTH_SHORT).show();
//
                } else {
                    getData(page);
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
    }

//    @Override
//    public void getBundle(Bundle bundle) {
//
//    }

    private void getData(int currentPage) {
        if (currentPage <= totalPager) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.DOMIN_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FileUploadService userBiz = retrofit.create(FileUploadService.class);

            Call<ApiResponse> call = userBiz.queryNewMessageNotice("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.e" +
                    "yJpYXQiOjE1MDE2NzMxNDYsIm5iZiI6MTUwMTY3MzE0NywiZXhwIjoxNTMyNzc3MTQ3LCJkYXRh" +
                    "Ijp7ImlkIjoxOX19.mfgNbjAXGc7Ore-Jbrn7kNWqQF_ZMVAZ78dtFXxnic0", currentPage,1);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> apiResponse) {

                    if (apiResponse.body().getCode() == 0) {
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.body().getData());
                        MessageRequestData data = gson.fromJson(jsonArray,
                                new TypeToken<MessageRequestData>() {
                                }.getType());
                        totalPager = data.getLast_page();
                        page++;
                        if (data.getData() != null) {
                            for (int i = 0; i < data.getData().size(); i++) {
                                dataBean.add(data.getData().get(i));
                            }
                            adapter = new MessageAdapter(getActivity(), dataBean);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setAdapter(adapter);

                        }
                    }

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
//            mSubscription = mRemoteService.queryNewMessageNotice(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
//                    currentPage, 1)
//
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<ApiResponse>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(ApiResponse apiResponse) {
//                            if (apiResponse.getCode() == 0) {
//                                Gson gson = new Gson();
//                                String jsonArray = gson.toJson(apiResponse.getData());
//                                MessageRequestData data = gson.fromJson(jsonArray,
//                                        new TypeToken<MessageRequestData>() {
//                                        }.getType());
//                                totalPager = data.getLast_page();
//                                page++;
//                                if (data.getData() != null) {
//                                    for (int i = 0; i < data.getData().size(); i++) {
//                                        dataBean.add(data.getData().get(i));
//                                    }
//                                    adapter = new MessageAdapter(context, dataBean);
//                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//                                    mRecyclerView.setLayoutManager(layoutManager);
//                                    mRecyclerView.setAdapter(adapter);
//
//                                }
//                            }
//                        }
//                    });
//
        }
    }
    private void getDatas(int currentPage) {

        for (int j = 1; j <= currentPage; j++) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.DOMIN_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FileUploadService userBiz = retrofit.create(FileUploadService.class);

            Call<ApiResponse> call = userBiz.queryNewMessageNotice("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.e" +
                    "yJpYXQiOjE1MDE2NzMxNDYsIm5iZiI6MTUwMTY3MzE0NywiZXhwIjoxNTMyNzc3MTQ3LCJkYXRh" +
                    "Ijp7ImlkIjoxOX19.mfgNbjAXGc7Ore-Jbrn7kNWqQF_ZMVAZ78dtFXxnic0", j,1);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> apiResponse) {

                    if (apiResponse.body().getCode() == 0) {
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.body().getData());
                        MessageRequestData data = gson.fromJson(jsonArray,
                                new TypeToken<MessageRequestData>() {
                                }.getType());

                        if (data.getData() != null) {
                            for (int i = 0; i < data.getData().size(); i++) {
                                dataBean.add(data.getData().get(i));
                            }

                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter = new MessageAdapter(getActivity(), dataBean);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
//            mSubscription = mRemoteService.queryNewMessageNotice(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
//                    j, 1)
//
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<ApiResponse>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(ApiResponse apiResponse) {
//                            if (apiResponse.getCode() == 0) {
//                                Gson gson = new Gson();
//                                String jsonArray = gson.toJson(apiResponse.getData());
//                                MessageRequestData data = gson.fromJson(jsonArray,
//                                        new TypeToken<MessageRequestData>() {
//                                        }.getType());
//
//                                if (data.getData() != null) {
//                                    for (int i = 0; i < data.getData().size(); i++) {
//                                        dataBean.add(data.getData().get(i));
//                                    }
//
//                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//                                    adapter = new MessageAdapter(context, dataBean);
//                                    mRecyclerView.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//
//                        }
//                    });
        }





    }

    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Holder> {
        private Context mContext;
        private List<MessageRequestData.DataBean> mDataBeanList;

        public MessageAdapter(Context context, List<MessageRequestData.DataBean> dataBeanList) {
            mContext = context;
            mDataBeanList = dataBeanList;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(mContext).inflate(R.layout.new_message_delete, parent, false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (mDataBeanList.get(position) != null) {
                MessageRequestData.DataBean data = mDataBeanList.get(position);
                holder.setItem(data);
                holder.tvDate.setText(data.getCreated_at());
                holder.tvTitle.setText(data.getTitle());
                holder.tvContent.setText(data.getAlert());
                switch (data.getKey()) {

                    case 1101:
                        Glide.with(mContext)
                                .load(R.mipmap.ic_remind)
                                .into(holder.imgSetting);
                        break;
                    case 2101:
                        //考勤

                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);

                        // ((MyHolder) holder).imgIcon.setImageResource(R.mipmap.ic_notice);

                        break;
                    case 1002:
                        //报岗
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);

                        break;
                    case 1003:
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);

                        //巡检
                        break;
                    case 2201:
                        //会议室审核后
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);

                        break;
                    case 2202:
                        //会议
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);

                        break;
                    case 2203:
                        //异常
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);

                        break;
                    case 2301:
                        //新进员工
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);

                        break;

                    case 2302:
                        //六天没考勤
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);
                        break;
                    case 2303:
                        //提醒签合同录指纹
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);

                        break;
                    case 2401:
                        //交接班注意事项
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);
                        break;
                    case 3101:
                        //生日提醒
                        Glide.with(mContext)
                                .load(ic_gift)
                                .into(holder.imgSetting);


                        break;
                    case 4101:
                        //随手拍
                        Glide.with(mContext)
                                .load(R.mipmap.ic_notice)
                                .into(holder.imgSetting);
                        break;


                }


            }
//            holder.mBmp.setImageResource(mResIds.get(position));
        }

        @Override
        public int getItemCount() {
            return mDataBeanList.size();
        }

        class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView imgSetting;
            private TextView tvContent;
            private TextView tvTitle;
            private TextView tvDate;
            private MessageRequestData.DataBean mDataBean;

            Holder(View itemView) {
                super(itemView);

                imgSetting = (ImageView) itemView.findViewById(R.id.img_setting);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                tvTitle = (TextView) itemView.findViewById(R.id.item_to_do_title);
                tvDate = (TextView) itemView.findViewById(R.id.item_apply_date);

                View main = itemView.findViewById(R.id.main);
                main.setOnClickListener(this);
                View delete = itemView.findViewById(R.id.delete);
                delete.setOnClickListener(this);
            }

            public void setItem(MessageRequestData.DataBean dataBean) {
                mDataBean = dataBean;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case main:

                        if (mDataBean.getKey()==1101||mDataBean.getKey()==2201){
                            Intent intent = new Intent(getActivity(), ApplyFeedBackActivity.class);
                            intent.putExtra(ApplyFeedBackActivity.APPLY_INFO, mDataBean);
                            startActivity(intent);
                        }else {
                            startActivity(new Intent(getActivity(), NoticeDetailActivity.class)
                                    .putExtra(NoticeDetailActivity.INFO, mDataBean));
                        }
                        break;

                    case R.id.delete:
                        int pos = getAdapterPosition();
                        mDataBeanList.remove(pos);
                        notifyItemRemoved(pos);
                        break;
                }
            }


        }
    }


}
