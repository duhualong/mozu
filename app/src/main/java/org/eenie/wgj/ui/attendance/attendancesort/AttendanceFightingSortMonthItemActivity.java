package org.eenie.wgj.ui.attendance.attendancesort;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceFightingResponse;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/5 at 20:21
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceFightingSortMonthItemActivity extends BaseActivity  {

    public static final String DATE = "date";
    public static final String PROJECT_ID = "id";

    @BindView(R.id.recycler_sort)
    RecyclerView mRecyclerView;
    private String date;
    private String projectId;
    private ProjectAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_sort_team_detail;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        date = getIntent().getStringExtra(DATE);

        mAdapter = new ProjectAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        getData(projectId,date);

    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }


    private void getData(String projectId, String date) {
        mSubscription = mRemoteService.getMonthFightingSort(mPrefsHelper.
                getPrefs().getString(Constants.TOKEN, ""), date, projectId)
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
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ArrayList<AttendanceFightingResponse> data = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<AttendanceFightingResponse>>() {
                                    }.getType());
                            if (data != null) {
                                if (mAdapter != null) {
                                    mAdapter.addAll(data);
                                }
                            }
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<AttendanceFightingResponse> projectMonth;

        public ProjectAdapter(Context context, ArrayList<AttendanceFightingResponse> projectMonth) {
            this.context = context;
            this.projectMonth = projectMonth;
        }
        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_sort_fighting_month_item, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectMonth != null && !projectMonth.isEmpty()) {

                AttendanceFightingResponse data = projectMonth.get(position);
                if (data != null) {

                    int mPosition = projectMonth.size() - position;
                    if (mPosition<10){
                        holder.itemNumber.setText("0"+String.valueOf((mPosition)));
                    }else {
                        holder.itemNumber.setText(String.valueOf((mPosition)));

                    }

                    holder.itemName.setText(data.getName());
                    holder.itemPost.setText(data.getPermissions());

                    holder.attendanceFirst.setText("异常" + data.getRefue() + "次");

                    if (!TextUtils.isEmpty(data.getId_card_head_image())) {
                        Glide.with(context).load(Constant.DOMIN + data.getId_card_head_image()).
                                centerCrop().into(holder.imgAvatar);
                    }

//设置显示内容
                }
            }

        }

        @Override
        public int getItemCount() {
            return projectMonth.size();
        }

        public void addAll(ArrayList<AttendanceFightingResponse> projectMonth) {
            this.projectMonth.addAll(projectMonth);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectMonth.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder  {

            private TextView itemNumber;

            private TextView itemName;
            private TextView itemPost;

            private TextView itemDate;
            private TextView attendanceFirst;
            private CircleImageView imgAvatar;




            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemNumber = ButterKnife.findById(itemView, R.id.tv_gold_number);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                itemPost = ButterKnife.findById(itemView, R.id.item_post);
                attendanceFirst = ButterKnife.findById(itemView, R.id.attendance_first);
                imgAvatar=ButterKnife.findById(itemView,R.id.img_avatar);


            }



        }
    }

}
