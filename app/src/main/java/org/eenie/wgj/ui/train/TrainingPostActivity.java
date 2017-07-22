package org.eenie.wgj.ui.train;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.training.TrainingContentResponse;
import org.eenie.wgj.model.response.training.TrainingPostResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/6 at 19:35
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingPostActivity extends BaseActivity {

    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.banner)
    BGABanner mBanner;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_pos)
    TextView mTvPos;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.item_action)
    RelativeLayout mItemAction;


    @BindView(R.id.tv_pager)
    TextView tvPager;
    private TrainingContentResponse mData;

    int curPage = 0;
    int maxPage;
    boolean canLoad = true;

    @Override
    protected int getContentView() {
        return R.layout.activity_training_detail;
    }

    @Override
    protected void updateUI() {
       mData= getIntent().getParcelableExtra("info");
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

        mBanner.setAutoPlayAble(false);

        mBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvPos.setText(String.format("%s/%s", position + 1, mBanner.getItemCount()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void fetchMasterData(int curPage) {
        mSubscription = mRemoteService.lookTrainingInfoList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), curPage, 2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.getData());
                        TrainingPostResponse mData = gson.fromJson(jsonArray,
                                new TypeToken<TrainingPostResponse>() {
                                }.getType());
                        if (mData != null) {
                            fillData(mData);

                        }

                    }
                });
    }

    private void fillData(TrainingPostResponse mData) {
        initBanner(mData.getImage());
        mTvPos.setText(String.format("%s/%s", 1, mData.getImage().size()));
        mTvTitle.setText(mData.getTrainingname());
        mTvContent.setText(mData.getTrainingcontent());
        if (!mData.isWhether()) {
            startCount();

        }

    }

    private void initBanner(ArrayList<TrainingPostResponse.ImageBean> image) {


        ArrayList<String> modules = new ArrayList<>();
        ArrayList<String> tips = new ArrayList<>();

        for (TrainingPostResponse.ImageBean imageBean : image) {
            modules.add(Constant.DOMIN + imageBean.getImage());
            tips.add("Banner 06");
        }


        mBanner.setAdapter((banner, view, model, position) ->
                Glide.with(context)
                .load(model.toString())
                .into((ImageView) view));
        mBanner.setData(modules, tips);
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
                    mTvCount.setVisibility(View.VISIBLE);
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        canLoad = true;
                        mTvCount.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mTvCount.setText(integer + "s");
                    }
                });
    }


    @OnClick({R.id.img_back, R.id.tv_proi, R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_proi:
                if (canLoad) {

                    if (curPage - 1 > 0) {
                        curPage--;
                        tvPager.setText(curPage + "/" + maxPage);
                        fetchMasterData(mData.getTotal_pages().get(curPage-1));
                    } else {
                        Toast.makeText(context, "当前为首页", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, mTvCount.getText() + "后才能加载",
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
                    Toast.makeText(context, mTvCount.getText() + "后才能加载",
                            Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
