package org.eenie.wgj.ui.project.attendance;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.LocationAddress;
import org.eenie.wgj.search.ClearEditText;
import org.eenie.wgj.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static butterknife.ButterKnife.findById;

/**
 * Created by Eenie on 2017/5/23 at 16:02
 * Email: 472279981@qq.com
 * Des:
 */

public class SearchAddressDetailActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_project_contacts)
    RecyclerView mRecyclerView;
    private LocationAdapter adapter;
    @BindView(R.id.filter_edit)ClearEditText mSearchView;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_address_detail;
    }

    @Override
    protected void updateUI() {

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        adapter = new LocationAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s.toString())) {
                    searchAddress(s.toString());
                }else {
                    searchAddress("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s.toString())) {
                    adapter.clear();
                    searchAddress(s.toString());
                }else {
                    searchAddress("");
                }
            }
        });

    }


    private void searchAddress(String address) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BAIDU_SEARCH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);
        Call<ApiResponse> call = userBiz.getAddressDetail("上海", address, "json");
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                cancelRefresh();
                if (response.body().getStatus() == 0) {
                    Gson gson = new Gson();
                    String jsonArray = gson.toJson(response.body().getResult());
                    List<LocationAddress> data = gson.fromJson(jsonArray,
                            new TypeToken<List<LocationAddress>>() {
                            }.getType());

                    if (data != null&&!data.isEmpty()) {
                            if (adapter != null) {
                                adapter.addAll(data);
                            }

                    }


                    Log.d("test", "onResponse: " + response.body().getResult().toString());
                }else {
                    adapter.clear();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


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
        adapter.clear();

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s.toString())) {
                    searchAddress(s.toString());
                }else {
                    searchAddress("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s.toString())) {
                    adapter.clear();
                    searchAddress(s.toString());
                }else {
                    searchAddress("");
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
    class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ProjectViewHolder> {
        private Context context;
        private List<LocationAddress> projectList;

        public LocationAdapter(Context context, List<LocationAddress> projectList) {
            this.context = context;
            this.projectList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_choose_address, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                LocationAddress data = projectList.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.address.setText(data.getName());





                    // holder.imgProject.setImageURI(data.get);
                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public void addAll(List<LocationAddress> projectList) {
            this.projectList.addAll(projectList);
            LocationAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectList.clear();
            LocationAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView address;
            private LocationAddress mLocationAddress;
            private RelativeLayout selectRl;



            public ProjectViewHolder(View itemView) {

                super(itemView);

                address= findById(itemView,R.id.detailAddress);
               selectRl= ButterKnife.findById(itemView,R.id.rl_select_detail);
                selectRl.setOnClickListener(this);

            }

            public void setItem(LocationAddress projectList) {
                mLocationAddress = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){


                }


            }
        }
    }
}
