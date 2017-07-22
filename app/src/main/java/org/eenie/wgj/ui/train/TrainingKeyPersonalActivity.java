package org.eenie.wgj.ui.train;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.training.TrainingContentResponse;
import org.eenie.wgj.model.response.training.TrainingKeyPersonalResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/7 at 10:34
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingKeyPersonalActivity extends BaseActivity {

    @BindView(R.id.tv_count_time)
    TextView mTvCountTime;
    @BindView(R.id.img_master_header)
    CircleImageView mImgMasterHeader;
    @BindView(R.id.tv_master_name)
    TextView mTvMasterName;
    @BindView(R.id.tv_master_age)
    TextView mTvMasterAge;
    @BindView(R.id.tv_master_height)
    TextView mTvMasterHeight;
    @BindView(R.id.tv_master_work)
    TextView mTvMasterWork;
    @BindView(R.id.tv_master_worktime)
    TextView mTvMasterWorktime;
    @BindView(R.id.tv_master_chepai)
    TextView mTvMasterChepai;
    @BindView(R.id.tv_master_phone)
    TextView mTvMasterPhone;
    @BindView(R.id.tv_matser_other)
    TextView mTvMatserOther;


    boolean canLoad = true;
    int curPage = 0;
    int maxPage;


    @BindView(R.id.tv_pager)
    TextView tvPager;
    private TrainingContentResponse mData;
    @Override
    protected int getContentView() {
        return R.layout.activity_training_key_personal;
    }

    @Override
    protected void updateUI() {
        mData = getIntent().getParcelableExtra("info");
        if (mData.getCurrent_page()!=-1){
            if (mData.getTotal_pages()!=null){
                if (mData.getTotal_pages().size()>0){
                    curPage=mData.getCurrent_index();
                    maxPage=mData.getTotal_pages().size();
                }

            }
        }else {
            if (mData.getTotal_pages()!=null){
                if (mData.getTotal_pages().size()>0){
                    curPage=1;
                    maxPage=mData.getTotal_pages().size();
                }

            }
        }

        if (curPage>maxPage){
            tvPager.setText(maxPage + "/" + maxPage);
            fetchMasterData(mData.getTotal_pages().get(maxPage-1));
        }else {
            tvPager.setText(curPage + "/" + maxPage);
            fetchMasterData(mData.getTotal_pages().get(curPage-1));

        }

    }

    private void fetchMasterData(int curPage) {

        mSubscription = mRemoteService.lookTrainingInfoList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), curPage, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            TrainingKeyPersonalResponse mData = gson.fromJson(jsonArray,
                                    new TypeToken<TrainingKeyPersonalResponse>() {
                                    }.getType());
                            if (mData != null) {
                                fillData(mData);

                            }
                        }else {
                            Toast.makeText(context,apiResponse.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void fillData(TrainingKeyPersonalResponse entity) {

        mTvMasterAge.setText(entity.getAge() + "岁");
        mTvMasterHeight.setText(String.valueOf(entity.getHeight()) + "cm");
        if (!entity.getJob().isEmpty() && entity.getJob() != null) {
            mTvMasterWork.setText(entity.getJob());
        } else {
            mTvMasterWork.setText("无");
        }
        if (!entity.getWorkinghours().isEmpty() && entity.getWorkinghours() != null) {
            mTvMasterWorktime.setText(entity.getWorkinghours());
        } else {
            mTvMasterWorktime.setText("无");
        }
        if (entity.getNumberplates() != null && !entity.getNumberplates().isEmpty()) {
            mTvMasterChepai.setText(entity.getNumberplates());
        } else {
            mTvMasterChepai.setText("无");

        }
        if (entity.getPhone() != null && !entity.getPhone().isEmpty()) {
            mTvMasterPhone.setText(entity.getPhone());
        } else {
            mTvMasterPhone.setText("无");
        }
        if (entity.getRemarks()!=null&&!entity.getRemarks().isEmpty()){
            mTvMatserOther.setText(entity.getRemarks());
        }else {
            mTvMatserOther.setText("无");
        }

        mTvMasterName.setText(String.format("%s/%s", entity.getName(), entity.getSex() == 1 ? "男" : "女"));
        if (!entity.getImages().isEmpty() && entity.getImages() != null) {
            Glide.with(context).load(Constant.DOMIN + entity.getImages()).centerCrop().into(mImgMasterHeader);
        }

        if (!entity.isWhether()) {
            startCount();
        }

    }
    private Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(increaseTime -> countTime - increaseTime.intValue())
                .take(countTime + 1);
    }
    private void startCount() {
        countdown(20)
                .doOnSubscribe(() -> {
                    canLoad = false;
                    mTvCountTime.setVisibility(View.VISIBLE);
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        canLoad = true;
                        mTvCountTime.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mTvCountTime.setText(integer + "s");
                    }
                });
    }

    @OnClick({R.id.tv_prio, R.id.tv_next, R.id.img_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_prio:
                if (canLoad) {
                    if (curPage - 1 > 0) {
                        curPage--;
                        fetchMasterData(mData.getTotal_pages().get(curPage-1));
                        tvPager.setText(curPage + "/" + maxPage);

                    } else {
                        Toast.makeText(context, "当前为首页", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, mTvCountTime.getText() + "后才能加载",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_next:
                if (canLoad) {
                    if (curPage + 1 <= maxPage) {
                        curPage++;
                        tvPager.setText(curPage + "/" + maxPage);
                        fetchMasterData(mData.getTotal_pages().get(curPage-1));

                    } else {
                        Toast.makeText(context, "当前为最后一页", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(context, mTvCountTime.getText() + "后才能加载",
                            Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


}
