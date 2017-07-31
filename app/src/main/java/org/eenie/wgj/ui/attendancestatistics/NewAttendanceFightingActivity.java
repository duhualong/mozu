package org.eenie.wgj.ui.attendancestatistics;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.newattendancestatistic.AttendanceStatisticRankAllMonth;
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
 * Created by Eenie on 2017/7/26 at 19:22
 * Email: 472279981@qq.com
 * Des:
 */

public class NewAttendanceFightingActivity extends BaseActivity {

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
        getData(projectId, date);

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
        mSubscription = mRemoteService.getAttendanceStatisticRankRefue(mPrefsHelper.
                getPrefs().getString(Constants.TOKEN, ""),  projectId,date)
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

                        if (apiResponse.getCode()== 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ArrayList<AttendanceStatisticRankAllMonth> data = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<AttendanceStatisticRankAllMonth>>() {
                                    }.getType());
                            if (data != null) {
                                if (mAdapter != null) {
                                    mAdapter.addAll(data);
                                }
                            }
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }





    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<AttendanceStatisticRankAllMonth> projectMonth;

        public ProjectAdapter(Context context, ArrayList<AttendanceStatisticRankAllMonth> projectMonth) {
            this.context = context;
            this.projectMonth = projectMonth;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_new_attendance_fighting, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectMonth != null && !projectMonth.isEmpty()) {

                AttendanceStatisticRankAllMonth data = projectMonth.get(position);
                if (data != null) {
                   int mPosition=projectMonth.size()-position;
                    holder.itemNumber.setText(mPosition+"");
                    if (!TextUtils.isEmpty(data.getId_card_head_image())){
                        Glide.with(context).load(Constant.DOMIN +
                                data.getId_card_head_image())
                                .centerCrop().into(holder.avatarImg);
                    }
                    holder.itemName.setText(data.getName());
                    holder.itemPost.setText(data.getTypename());
                    if (data.getExceptions()<=9){
                        holder.itemCount.setText("异常：0"+data.getExceptions()+"次");

                    }else {
                        holder.itemCount.setText("异常："+data.getExceptions()+"次");

                    }




                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectMonth.size();
        }

        public void addAll(ArrayList<AttendanceStatisticRankAllMonth> projectMonth) {
            this.projectMonth.addAll(projectMonth);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectMonth.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder {


            private TextView itemNumber;
            private ImageView imgSort;
            private CircleImageView avatarImg;
            private TextView itemName;
            private TextView itemPost;
            private TextView itemCount;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemNumber = ButterKnife.findById(itemView, R.id.tv_sort_number);
                imgSort = ButterKnife.findById(itemView, R.id.img_sort);
                avatarImg = ButterKnife.findById(itemView, R.id.img_avatar);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                itemPost = ButterKnife.findById(itemView, R.id.item_post);
                itemCount=ButterKnife.findById(itemView,R.id.tv_item_count);


            }


        }
        }
    }

