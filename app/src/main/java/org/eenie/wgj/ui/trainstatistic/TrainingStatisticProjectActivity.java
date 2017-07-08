package org.eenie.wgj.ui.trainstatistic;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.training.TrainingStatisticListResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/7 at 12:30
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingStatisticProjectActivity extends BaseActivity{
    public static final String PROJECT_ID = "id";
    public static final String PROJECT_NAME = "name";
    private String projectName;
    private String projectId;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private TrainStatisticAdapter mAdapter;
    List<TrainingStatisticListResponse> mTrainStatistEntities = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.activity_training_statistic_project;
    }

    @Override
    protected void updateUI() {

        projectId = getIntent().getStringExtra(PROJECT_ID);
        projectName = getIntent().getStringExtra(PROJECT_NAME);

        mAdapter = new TrainStatisticAdapter(mTrainStatistEntities);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        if (!TextUtils.isEmpty(projectName)) {
            tvTitle.setText(projectName);
        } else {
            tvTitle.setText("无");
        }
        if (!TextUtils.isEmpty(projectId)){

            onRefresh(projectId);
        }

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }

    public void onRefresh(String projectId) {

        mSubscription=mRemoteService.getTrainingStatisticList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            if (apiResponse.getData()!=null){
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<TrainingStatisticListResponse> mData = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<TrainingStatisticListResponse>>() {
                                        }.getType());
                                if (mData!=null&&!mData.isEmpty()){
                                    mTrainStatistEntities.clear();
                                    mTrainStatistEntities.addAll(mData);
                                    mAdapter.notifyDataSetChanged();
                                }

                            }

                        }else{
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(projectId)){
            onRefresh(projectId);
        }


    }


}
