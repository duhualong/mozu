package org.eenie.wgj.ui.meeting.launchmeeting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import org.eenie.wgj.model.response.meeting.MeetingPeople;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/10 at 16:57
 * Email: 472279981@qq.com
 * Des:
 */

public class AddPeopleSelectActivity extends BaseActivity {

    @BindView(R.id.recycler_view_meeting)
    RecyclerView mRecyclerView;
    private AddPersonalAdapter mAdapter;
    private ArrayList<MeetingPeople> data = new ArrayList<>();
    @BindView(R.id.checkbox_select_all)CheckBox mCheckBoxAll;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_meeting_people;
    }

    @Override
    protected void updateUI() {

        mAdapter = new AddPersonalAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();

    }

    @OnClick({R.id.img_back, R.id.submit_tv,R.id.checkbox_select_all})
    public void onClick(View view) {
        int number = 0;
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.submit_tv:
                if (data != null) {
                    Gson gson = new Gson();
                    Log.d("data", "onClick: " + gson.toJson(data));
                    ArrayList<MeetingPeople> mUser = new ArrayList<>();
                    for (int g = 0; g < data.size(); g++) {
                        if (data.get(g).isChecked()) {
                            number++;
                            mUser.add(data.get(g));
                        }
                    }
                    Log.d("user", "onClick: " + gson.toJson(mUser));
                    if (number <= 0) {
                        Toast.makeText(context, "请选择参会人员", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent mIntent = new Intent();
                        mIntent.putParcelableArrayListExtra("mData", mUser);
                        // 设置结果，并进行传送
                        setResult(RESULT_OK, mIntent);
                        finish();

                    }
                }


                break;
            case R.id.checkbox_select_all:
                if (mCheckBoxAll.isChecked()){
                    if (data!=null&&!data.isEmpty()){
                        for (int i=0;i<data.size();i++){
                            data.get(i).setChecked(true);

                        }
                        if (mAdapter!=null){
                            mAdapter.clear();
                            mAdapter.addAll(data);
                        }


                    }
                }else {
                    if (data!=null&&!data.isEmpty()){
                        for (int i=0;i<data.size();i++){
                            data.get(i).setChecked(false);
                        }
                        if (mAdapter!=null){
                            mAdapter.clear();
                            mAdapter.addAll(data);
                        }


                    }

                }


                break;

        }
    }


    public void onRefresh() {
        mAdapter.clear();
        mSubscription = mRemoteService.
                getMeetingPeopleList(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                   @Override
                   public void onNext(ApiResponse apiResponse) {
                       if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                           Gson gson = new Gson();
                           String jsonArray = gson.toJson(apiResponse.getData());
                           data = gson.fromJson(jsonArray,
                                   new TypeToken<ArrayList<MeetingPeople>>() {
                                   }.getType());
                           if (data != null) {
                               if (mAdapter != null) {
                                   mAdapter.clear();
                                   mAdapter.addAll(data);
                               }
                           }
                       } else {
                           Toast.makeText(context, apiResponse.getResultMessage(),
                                   Toast.LENGTH_SHORT).show();
                       }
                   }
               });


    }



    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    class AddPersonalAdapter extends RecyclerView.Adapter<AddPersonalAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<MeetingPeople> addPersonalClass;

        public AddPersonalAdapter(Context context, ArrayList<MeetingPeople> addPersonalClass) {
            this.context = context;
            this.addPersonalClass = addPersonalClass;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_checkbox_select_meeting, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (addPersonalClass != null && !addPersonalClass.isEmpty()) {
                MeetingPeople data = addPersonalClass.get(position);
                if (data != null) {
                    holder.setItem(data,position);
                    if (data.getName() != null) {
                        holder.tvName.setText(data.getName());
                        if (data.isChecked()){
                            holder.checkBoxSelect.setChecked(true);
                        }else {
                            holder.checkBoxSelect.setChecked(false);

                        }


                    }
                }

            }

        }

        @Override
        public int getItemCount() {
            return addPersonalClass.size();
        }

        public void addAll(ArrayList<MeetingPeople> projectList) {
            this.addPersonalClass.addAll(projectList);
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.addPersonalClass.clear();
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private CheckBox checkBoxSelect;
            private MeetingPeople mMeetingPeople;
            private RelativeLayout mRelativeLayout;
            private TextView tvName;
            private  int mPosition;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                checkBoxSelect = ButterKnife.findById(itemView, R.id.item_checkbox_select_item);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_meeting_item);
                tvName=ButterKnife.findById(itemView,R.id.item_name);

                checkBoxSelect.setOnClickListener(this);


                checkBoxSelect.setOnClickListener(this);
                mRelativeLayout.setOnClickListener(this);

            }

            public void setItem(MeetingPeople projectList,int position) {
                mMeetingPeople = projectList;
                mPosition=position;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_meeting_item:
                        System.out.println("ssss");
                        if (checkBoxSelect.isChecked()) {
                            mMeetingPeople.setChecked(false);
                            addPersonalClass.get(mPosition).setChecked(false);
                        } else {
                            mMeetingPeople.setChecked(true);
                            addPersonalClass.get(mPosition).setChecked(true);
                        }
                        notifyDataSetChanged();
                        System.out.println("dddd");
                        break;
                }


            }
        }
    }
}