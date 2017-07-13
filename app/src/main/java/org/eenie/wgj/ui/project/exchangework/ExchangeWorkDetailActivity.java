package org.eenie.wgj.ui.project.exchangework;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.ExchangeWorkList;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/19 at 12:10
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkDetailActivity extends BaseActivity {
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.exchange_work_name)
    TextView workTitle;
    @BindView(R.id.tv_matter)
    TextView tvMatter;
//    public static final String INFO = "info";
    public static final String PROJECT_ID = "project_id";
    public static final String EXCHANGE_ID="exchange_id";
    private ExchangeWorkList data;
    @BindViews({R.id.img_first, R.id.img_second, R.id.img_third})
    List<ImageView> imgList;
    private String projectId;
    private String exchangeId;
    private int mId;
    private List<ExchangeWorkList.ImageBean> mImageBeanList = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.activity_exchange_work_detail;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        exchangeId= getIntent().getStringExtra(EXCHANGE_ID);
        if (!TextUtils.isEmpty(exchangeId)&&!TextUtils.isEmpty(projectId)){
            getInfo(projectId,exchangeId);
        }


    }

    private void getInfo(String projectId, String exchangeId) {
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getExchangeWorkList(token, projectId)
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

                        if (apiResponse.getResultCode() == 200) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<ExchangeWorkList> exchangeWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<List<ExchangeWorkList>>() {
                                        }.getType());

                                if (exchangeWorkLists != null && !exchangeWorkLists.isEmpty()) {
                                    for (int i=0;i<exchangeWorkLists.size();i++){
                                        if (exchangeId.equals(String.valueOf(
                                                exchangeWorkLists.get(i).getId()))){
                                            data=exchangeWorkLists.get(i);
                                            initUI(data);
                                        }
                                    }


                                }


                            }
                        }
                    }

                });
    }


    private void initUI(ExchangeWorkList data) {
        if (data != null) {
            mId = data.getId();
            if (!TextUtils.isEmpty(data.getMattername())) {
                workTitle.setText(data.getMattername());

            }
            if (!TextUtils.isEmpty(data.getMatter())) {
                tvMatter.setText(data.getMatter());
            } else {
                tvMatter.setText("无");
            }
            if (!data.getImage().isEmpty() && data.getImage() != null) {
                mImageBeanList = data.getImage();
                if (data.getImage().size() == 1) {
                    imgList.get(0).setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                } else if (data.getImage().size() == 2) {
                    imgList.get(0).setVisibility(View.VISIBLE);
                    imgList.get(1).setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage())
                            .centerCrop().into(imgList.get(1));
                } else if (data.getImage().size() >= 3) {
                    imgList.get(0).setVisibility(View.VISIBLE);
                    imgList.get(1).setVisibility(View.VISIBLE);
                    imgList.get(2).setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage())
                            .centerCrop().into(imgList.get(1));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(2).getImage())
                            .centerCrop().into(imgList.get(2));
                }


            }
        }
    }

    @OnClick({R.id.img_back, R.id.button_delete, R.id.button_edit, R.id.img_first, R.id.img_second,
            R.id.img_third})
    public void onClick(View view) {
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");

        switch (view.getId()) {
            case R.id.img_first:

                if (mImageBeanList.size() >= 1) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + mImageBeanList.get(0).getImage()));
                }


                break;
            case R.id.img_second:
                if (mImageBeanList.size() >= 2) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + mImageBeanList.get(1).getImage()));
                }


                break;
            case R.id.img_third:
                if (mImageBeanList.size() >= 3) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + mImageBeanList.get(2).getImage()));
                }

                break;


            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.button_delete:
                if (!TextUtils.isEmpty(token)) {
                    deleteItem(token, mId);
                }

                break;

            case R.id.button_edit:
                Intent intent = new Intent(context, ExchangeWorkEditActivity.class);
                if (data != null) {
                    intent.putExtra(ExchangeWorkEditActivity.INFO, data);
                    if (!TextUtils.isEmpty(projectId)) {
                        intent.putExtra(ExchangeWorkEditActivity.PROJECT_ID, projectId);
                    }
                }
                startActivity(intent);

                break;
        }
    }
    //删除交接班

    private void deleteItem(String token, int id) {
        mSubscription = mRemoteService.deleteExchangeWorkItem(token, id)
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
                        if (apiResponse.getResultCode() == 200) {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_LONG).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );

                        } else {
                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    protected void onResume() {
       super.onResume();
        if (!TextUtils.isEmpty(exchangeId)&&!TextUtils.isEmpty(projectId)){
            getInfo(projectId,exchangeId);
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == 4) {
//            ExchangeWorkListResponse mData = data.getParcelableExtra("exchange_work");
//            initUI(mData);
//        }
//
//    }

}
