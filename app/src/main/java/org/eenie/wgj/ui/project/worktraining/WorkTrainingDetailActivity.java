package org.eenie.wgj.ui.project.worktraining;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.WorkTrainingList;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

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
 * Created by Eenie on 2017/5/21 at 14:58
 * Email: 472279981@qq.com
 * Des:
 */

public class WorkTrainingDetailActivity extends BaseActivity {
    @BindView(R.id.root_view)View rootView;
    @BindView(R.id.exchange_work_name)TextView workTitle;
    @BindView(R.id.tv_matter)TextView tvMatter;
    public static final String INFO="info";
    public static final String PROJECT_ID="project_id";
    private WorkTrainingList data;
    @BindViews({R.id.img_first,R.id.img_second,R.id.img_third})List<ImageView> imgList;
    private String projectId;
    private  int mId;



    @Override
    protected int getContentView() {
        return R.layout.activity_training_work_detail;
    }

    @Override
    protected void updateUI() {
        data= getIntent().getParcelableExtra(INFO);
        projectId=getIntent().getStringExtra(PROJECT_ID);
        initUI(data);


    }

    private void initUI(WorkTrainingList data) {
        if (data!=null){
            mId= data.getId();
            if (!TextUtils.isEmpty(data.getTrainingname())){
                workTitle.setText(data.getTrainingname());

            }
            if (!TextUtils.isEmpty(data.getTrainingcontent())){
                tvMatter.setText(data.getTrainingcontent());
            }else {
                tvMatter.setText("无");
            }
            if (!data.getImage().isEmpty()&&data.getImage()!=null){
                if (data.getImage().size()>3) {
                    for (int i = 0; i < 3; i++) {
                        imgList.get(i).setVisibility(View.VISIBLE);
                        Glide.with(context).load(Constant.DOMIN + data.getImage().get(i).getImage())
                                .centerCrop().into(imgList.get(i));


                    }
                }else {
                    for (int i = 0; i < data.getImage().size(); i++) {
                        imgList.get(i).setVisibility(View.VISIBLE);
                        Glide.with(context).load(Constant.DOMIN + data.getImage().get(i).getImage())
                                .centerCrop().into(imgList.get(i));


                    }
                }
            }

        }
    }

    @OnClick({R.id.img_back,R.id.button_delete,R.id.button_edit,R.id.img_first,R.id.img_second,
            R.id.img_third})public void onClick(View view){
        String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");

        switch (view.getId()){
            case R.id.img_first:
                if (data.getImage().size()>=1){
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + data.getImage().get(0).getImage()));
                }


                break;
            case R.id.img_second:

                if (data.getImage().size()>=2){
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + data.getImage().get(1).getImage()));
                }

                break;
            case R.id.img_third:
                if (data.getImage().size()>=3){
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + data.getImage().get(2).getImage()));
                }


                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.button_delete:
                if (!TextUtils.isEmpty(token)){
                    deleteItem(token,mId);
                }

                break;

            case R.id.button_edit:
                Intent intent=new Intent(context,EditTrainingWorkActivity.class);
                if (data!=null){
                    intent.putExtra(EditTrainingWorkActivity.INFO,data);
                    if (!TextUtils.isEmpty(projectId)){
                        intent.putExtra(EditTrainingWorkActivity.PROJECT_ID,projectId);
                    }
                }
                startActivityForResult(intent, 1);
                break;
        }
    }
    //删除交接班

    private void deleteItem(String token, int id) {
        mSubscription=mRemoteService.deleteTrainingWorkItem(token,id)
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
                        if (apiResponse.getResultCode()==200){
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_LONG).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );

                        }else {
                            Snackbar.make(rootView,apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
//            ExchangeWorkListResponse mData = data.getParcelableExtra("exchange_work");
//            initUI(mData);
        }

    }
}
