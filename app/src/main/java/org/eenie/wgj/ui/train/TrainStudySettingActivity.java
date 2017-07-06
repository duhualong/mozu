package org.eenie.wgj.ui.train;

import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.training.TrainingContentResponse;
import org.eenie.wgj.ui.reportpoststatistics.CircularProgressBar;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/6 at 17:20
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainStudySettingActivity extends BaseActivity {
    @BindView(R.id.pro_rate)
    CircularProgressBar mProRate;
    @BindView(R.id.tv_rate)
    TextView mTvRate;
    @BindView(R.id.tv_newly_num)
    TextView mTvNewlyNum;
    @BindView(R.id.pro_rate2)
    CircularProgressBar mProRate2;
    @BindView(R.id.tv_rate2)
    TextView mTvRate2;

    @Override
    protected int getContentView() {
        return R.layout.activity_train_study_setting;
    }

    @Override
    protected void updateUI() {
        mProRate.setProgress(0);
        mProRate2.setProgress(0);
        getData();

    }

    private void getData() {

        mSubscription=mRemoteService.getTrainInfoList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN,""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            if (apiResponse.getData()!=null){
                                Gson gson=new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<TrainingContentResponse> mData = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<TrainingContentResponse>>() {
                                        }.getType());

                                if (mData!=null){
                                    if (mData.size()>=1){
                                        if (mData.get(0).getSchedule()>0&&mData.get(0).getSchedule()<1){
                                            mProRate.setProgress(1);

                                        }else {
                                            mProRate.setProgress((int) mData.get(0).getSchedule());

                                        }
                                        mTvRate.setText(mData.get(0).getSchedule()+"%");
                                    }
                                    if (mData.size()>=2){
                                        if (mData.get(1).getSchedule()>0&&mData.get(1).getSchedule()<1){
                                            mProRate2.setProgress(1);

                                        }else {
                                            mProRate2.setProgress((int) mData.get(1).getSchedule());

                                        }

                                        mTvRate2.setText(mData.get(1).getSchedule()+"%");
                                    }
                                }

                            }
                        }

                    }
                });
    }

    @OnClick({R.id.img_back, R.id.rl_key_personal, R.id.rl_post_training})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.rl_key_personal:


                break;
            case R.id.rl_post_training:


                break;


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
