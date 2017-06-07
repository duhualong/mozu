package org.eenie.wgj.ui.project.roundpoint;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.RoundPoint;
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
 * Created by Eenie on 2017/5/25 at 16:51
 * Email: 472279981@qq.com
 * Des:
 */

public class RoundPointDetailActivity extends BaseActivity {
    public static final String INFO="info";
    public static final String PROJECT_ID="id";
    @BindView(R.id.exchange_work_name)TextView inspectionName;
    @BindView(R.id.tv_matter)TextView inspectionContent;
    @BindViews({R.id.img_first,R.id.img_second,R.id.img_third})List<ImageView>imgList;
    private RoundPoint mRoundPoint;
    private String projectId;
    private int id;


    @Override
    protected int getContentView() {
        return R.layout.activity_round_point_detail;
    }

    @Override
    protected void updateUI() {
       mRoundPoint= getIntent().getParcelableExtra(INFO);
        projectId=getIntent().getStringExtra(PROJECT_ID);
        initUI(mRoundPoint);


    }

    private void initUI(RoundPoint mRoundPoint) {
        if (mRoundPoint!=null){
            id=mRoundPoint.getId();
            inspectionName.setText(mRoundPoint.getInspectionname());
            inspectionContent.setText(mRoundPoint.getInspectioncontent());
            if (!mRoundPoint.getImage().isEmpty()&&mRoundPoint.getImage()!=null){
                if (mRoundPoint.getImage().size()>3) {
                    for (int i = 0; i < 3; i++) {
                        imgList.get(i).setVisibility(View.VISIBLE);
                        Glide.with(context).load(Constant.DOMIN + mRoundPoint.getImage().get(i).getImage())
                                .centerCrop().into(imgList.get(i));


                    }
                }else {
                    for (int i = 0; i < mRoundPoint.getImage().size(); i++) {
                        imgList.get(i).setVisibility(View.VISIBLE);
                        Glide.with(context).load(Constant.DOMIN + mRoundPoint.getImage().get(i).getImage())
                                .centerCrop().into(imgList.get(i));
                    }
                }
            }
        }
    }

    @OnClick({R.id.img_back,R.id.button_delete,R.id.button_edit})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.button_delete:
                deleteRoundPointItem();
                break;
            case R.id.button_edit:
                Intent intent=new Intent(context,RoundPointEditActivity.class);
                intent.putExtra(RoundPointEditActivity.INFO,mRoundPoint);
                intent.putExtra(RoundPointEditActivity.PROJECT_ID,projectId);
                startActivityForResult(intent, 1);


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            RoundPoint mData = data.getParcelableExtra("info");
            if (mData!=null){
                initUI(mData);

            }
        }
    }

    private void deleteRoundPointItem() {
        mSubscription=mRemoteService.deleteRoundPointItem(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),id)
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
                        if (apiResponse.getResultCode()==0||apiResponse.getResultCode()==200){
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_LONG).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );
                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
