package org.eenie.wgj.ui.train;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.training.TrainingContentResponse;
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
    @BindView(R.id.progress_bar_key_personal)
    ProgressBar progressBarKeyPersonal;
    @BindView(R.id.progress_bar_post)
    ProgressBar progressBarPost;
    @BindView(R.id.tv_progress_key_number)TextView tvKeyPersonal;
    @BindView(R.id.tv_progress_post)TextView tvPost;
    private TrainingContentResponse mTrainingPost;
    private TrainingContentResponse mTrainingKeyPersonal;
    @Override
    protected int getContentView() {
        return R.layout.activity_traing_new_pager;
    }

    @Override
    protected void updateUI() {
        getData();


    }


    private void getData() {
        mSubscription = mRemoteService.getTrainInfoList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getCode()==0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<TrainingContentResponse> mData = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<TrainingContentResponse>>() {
                                        }.getType());

                                if (mData != null) {
                                    if (mData.size() >= 1) {
                                        mTrainingKeyPersonal = mData.get(0);
                                        if (mData.get(0).getSchedule() > 0 && mData.get(0).getSchedule() < 1) {
                                            progressBarKeyPersonal.setProgress(1);

                                        } else {
                                            if (mData.get(0).getSchedule() > 100) {
                                                progressBarKeyPersonal.setProgress(100);
                                            } else {
                                                progressBarKeyPersonal.setProgress((int) mData.get(0).getSchedule());
                                            }


                                        }
                                        if (mData.get(0).getSchedule() > 100) {
                                            tvKeyPersonal.setText("100%");
                                        } else {
                                            if (String.valueOf(mData.get(0).getSchedule()).length()>6){
                                                tvKeyPersonal.setText(String.valueOf(mData.get(0).getSchedule()).substring(0,5) + "%");

                                            }else {
                                                tvKeyPersonal.setText(mData.get(0).getSchedule() + "%");

                                            }
                                        }

                                    }
                                    if (mData.size() >= 2) {
                                        mTrainingPost = mData.get(1);
                                        if (mData.get(1).getSchedule() > 0 && mData.get(1).getSchedule() < 1) {
                                            progressBarPost.setProgress(1);

                                        } else {
                                            if (mData.get(1).getSchedule() > 100) {
                                                progressBarPost.setProgress(100);
                                            } else {
                                                progressBarPost.setProgress((int) mData.get(1).getSchedule());
                                            }

                                        }
                                        if (mData.get(1).getSchedule() > 100) {
                                            tvPost.setText("100%");
                                        } else {
                                            if (String.valueOf(mData.get(1).getSchedule()).length()>6){
                                                tvPost.setText(String.valueOf(mData.get(1).getSchedule()).substring(0,5) + "%");
                                            }else {
                                                tvPost.setText(mData.get(1).getSchedule() + "%");
                                            }

                                        }

                                    }
                                }

                            }
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    @OnClick({R.id.img_back, R.id.rl_training_key_personal, R.id.rl_training_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.rl_training_key_personal:
                if (mTrainingKeyPersonal != null) {

                    if (mTrainingKeyPersonal.getTotal_pages()!=null) {

                        startActivity(new Intent(context, TrainingKeyPersonalActivity.class)
                                .putExtra("info",mTrainingKeyPersonal));
//                                    .putExtra("curPage", mTrainingKeyPersonal.getCurrentpage())
//                                    .putExtra("maxPage", mTrainingKeyPersonal.getTotal_pages().size()));

                    }

                } else {
                    Toast.makeText(context, "当前没有关键人物可以学习", Toast.LENGTH_SHORT).show();

                }



                break;
            case R.id.rl_training_post:
                if (mTrainingPost != null) {
                    if (mTrainingPost.getTotal_pages()!=null) {
                        startActivity(new Intent(context, TrainingPostActivity.class)
                                .putExtra("info",mTrainingPost));
//                                .putExtra("curPage", mTrainingPost.getCurrentpage())
//                                .putExtra("maxPage", mTrainingPost.getTotal_pages().size()));
                    } else {
                        Toast.makeText(context, "当前没有岗位可以学习", Toast.LENGTH_SHORT).show();

                    }

                }

                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
//    @BindView(R.id.pro_rate)
//    CircularProgressBar mProRate;
//    @BindView(R.id.tv_rate)
//    TextView mTvRate;
//    @BindView(R.id.tv_newly_num)
//    TextView mTvNewlyNum;
//    @BindView(R.id.pro_rate2)
//    CircularProgressBar mProRate2;
//    @BindView(R.id.tv_rate2)
//    TextView mTvRate2;
//    private TrainingContentResponse mTrainingPost;
//    private TrainingContentResponse mTrainingKeyPersonal;
//
//    @Override
//    protected int getContentView() {
//        return R.layout.activity_train_study_setting;
//    }
//
//    @Override
//    protected void updateUI() {
//        mProRate.setProgress(0);
//        mProRate2.setProgress(0);
//        getData();
//
//    }
//
//    private void getData() {
//        mSubscription = mRemoteService.getTrainInfoList(mPrefsHelper.getPrefs().
//                getString(Constants.TOKEN, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
//                    @Override
//                    public void onNext(ApiResponse apiResponse) {
//                        if (apiResponse.getCode()==0) {
//                            if (apiResponse.getData() != null) {
//                                Gson gson = new Gson();
//                                String jsonArray = gson.toJson(apiResponse.getData());
//                                ArrayList<TrainingContentResponse> mData = gson.fromJson(jsonArray,
//                                        new TypeToken<ArrayList<TrainingContentResponse>>() {
//                                        }.getType());
//
//                                if (mData != null) {
//                                    if (mData.size() >= 1) {
//                                        mTrainingKeyPersonal = mData.get(0);
//                                        if (mData.get(0).getSchedule() > 0 && mData.get(0).getSchedule() < 1) {
//                                            mProRate.setProgress(1);
//
//                                        } else {
//                                            if (mData.get(0).getSchedule() > 100) {
//                                                mProRate.setProgress(100);
//                                            } else {
//                                                mProRate.setProgress((int) mData.get(0).getSchedule());
//                                            }
//
//
//                                        }
//                                        if (mData.get(0).getSchedule() > 100) {
//                                            mTvRate.setText("100.0%");
//                                        } else {
//                                            mTvRate.setText(mData.get(0).getSchedule() + "%");
//                                        }
//
//                                    }
//                                    if (mData.size() >= 2) {
//                                        mTrainingPost = mData.get(1);
//                                        if (mData.get(1).getSchedule() > 0 && mData.get(1).getSchedule() < 1) {
//                                            mProRate2.setProgress(1);
//
//                                        } else {
//                                            if (mData.get(1).getSchedule() > 100) {
//                                                mProRate2.setProgress(100);
//                                            } else {
//                                                mProRate2.setProgress((int) mData.get(1).getSchedule());
//                                            }
//
//                                        }
//                                        if (mData.get(1).getSchedule() > 100) {
//                                            mTvRate2.setText("100.0%");
//                                        } else {
//                                            mTvRate2.setText(mData.get(1).getSchedule() + "%");
//                                        }
//
//                                    }
//                                }
//
//                            }
//                        } else {
//                            Toast.makeText(context, apiResponse.getResultMessage(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//    }
//
//    @OnClick({R.id.img_back, R.id.rl_key_personal, R.id.rl_post_training})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.img_back:
//                onBackPressed();
//
//                break;
//            case R.id.rl_key_personal:
//                if (mTrainingKeyPersonal != null) {
//
//                        if (mTrainingKeyPersonal.getTotal_pages()!=null) {
//
//                            startActivity(new Intent(context, TrainingKeyPersonalActivity.class)
//                                    .putExtra("info",mTrainingKeyPersonal));
////                                    .putExtra("curPage", mTrainingKeyPersonal.getCurrentpage())
////                                    .putExtra("maxPage", mTrainingKeyPersonal.getTotal_pages().size()));
//
//                        }
//
//                    } else {
//                        Toast.makeText(context, "当前没有关键人物可以学习", Toast.LENGTH_SHORT).show();
//
//                    }
//
//
//
//                break;
//            case R.id.rl_post_training:
//                if (mTrainingPost != null) {
//                    if (mTrainingPost.getTotal_pages()!=null) {
//                        startActivity(new Intent(context, TrainingPostActivity.class)
//                                .putExtra("info",mTrainingPost));
////                                .putExtra("curPage", mTrainingPost.getCurrentpage())
////                                .putExtra("maxPage", mTrainingPost.getTotal_pages().size()));
//                    } else {
//                        Toast.makeText(context, "当前没有岗位可以学习", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }
//
//                break;
//
//
//        }
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getData();
//    }
}
