package org.eenie.wgj.ui.exchangeclass;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.exchangework.ExchangeWorkContactResponse;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/13 at 15:50
 * Email: 472279981@qq.com
 * Des:
 */

public class AddManagerExchangeClassPeopleActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.swipe_refresh_list)SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)RecyclerView mRecyclerView;
    @BindView(R.id.tv_apply_ok)TextView tvApply;

    private AddPersonalAdapter mAdapter;
    private ArrayList<ExchangeWorkContactResponse> data=new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_management_people;
    }

    @Override
    protected void updateUI() {
        tvApply.setText("保存");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new AddPersonalAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }





    @OnClick({R.id.img_back,R.id.tv_apply_ok})public  void onClick(View view){
        int number=0;
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.tv_apply_ok:
                if (data!=null){
                    Gson gson=new Gson();
                    Log.d("data", "onClick: "+gson.toJson(data));
                    ArrayList<ExchangeWorkContactResponse> mUser=new ArrayList<>();
                    for (int g=0;g<data.size();g++){
                        if (data.get(g).isChecked()){
                            number++;
                            mUser.add(data.get(g));
                        }
                    }
                    if (number<=0){
                        Toast.makeText(context,"请选择管理人员",Toast.LENGTH_SHORT).show();

                    }else {
                        Intent mIntent = new Intent();
                        mIntent.putParcelableArrayListExtra("mData", mUser);
                        // 设置结果，并进行传送
                        setResult(RESULT_OK, mIntent);
                        finish();


                    }
                }


                break;
        }
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        mSubscription=mRemoteService.
                getExchangeWorkContact(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""))
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
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            data = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<ExchangeWorkContactResponse>>() {
                                    }.getType());
                            if (data!=null){
                                if (mAdapter!=null){
                                    mAdapter.addAll(data);
                                }
                            }
                        }else{
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

    class AddPersonalAdapter extends RecyclerView.Adapter<AddPersonalAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<ExchangeWorkContactResponse> addPersonalClass;

        public AddPersonalAdapter(Context context, ArrayList<ExchangeWorkContactResponse> addPersonalClass) {
            this.context = context;
            this.addPersonalClass = addPersonalClass;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_select_personal, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (addPersonalClass != null && !addPersonalClass.isEmpty()) {
                ExchangeWorkContactResponse data = addPersonalClass.get(position);
                holder.setItem(data,position);
                holder.itemText.setVisibility(View.GONE);
                holder.tvNumber.setVisibility(View.GONE);
                if (data != null) {
                    if (data.isChecked()){
                        holder.checkBoxSelect.setChecked(true);
                    }else {
                        holder.checkBoxSelect.setChecked(false);

                    }
                    if (TextUtils.isEmpty(data.getPosition())){
                        holder.personalName.setText(data.getName());
                    }else {
                        if (!data.getPosition().equals("null")){
                            holder.personalName.setText(data.getName()+" "+data.getPosition());
                        }else {
                            holder.personalName.setText(data.getName());
                        }

                    }

                }

            }

        }

        @Override
        public int getItemCount() {
            return addPersonalClass.size();
        }

        public void addAll(ArrayList<ExchangeWorkContactResponse> projectList) {
            this.addPersonalClass.addAll(projectList);
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.addPersonalClass.clear();
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private CheckBox checkBoxSelect;
            private TextView personalName;
            private TextView tvNumber;
            private ExchangeWorkContactResponse mProjectList;
            private TextView itemText;
            private RelativeLayout rlItem;
            int mPosition;

            public ProjectViewHolder(View itemView) {
                super(itemView);
                checkBoxSelect = ButterKnife.findById(itemView, R.id.checkbox_select);
                personalName = ButterKnife.findById(itemView, R.id.tv_personal_name);
                itemText=ButterKnife.findById(itemView,R.id.item_text);
                tvNumber = ButterKnife.findById(itemView, R.id.tv_number);
                rlItem=ButterKnife.findById(itemView,R.id.rl_item);
                checkBoxSelect.setClickable(false);
                rlItem.setOnClickListener(this);

            }

            public void setItem(ExchangeWorkContactResponse projectList,int position) {
                mProjectList = projectList;
                mPosition=position;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.rl_item:
                        if (checkBoxSelect.isChecked()){

                            mProjectList.setChecked(false);
                            addPersonalClass.get(mPosition).setChecked(false);

                        }else {
                            mProjectList.setChecked(true);
                            addPersonalClass.get(mPosition).setChecked(true);
                        }
                        notifyDataSetChanged();
                        break;

                }


            }
        }
    }
}
