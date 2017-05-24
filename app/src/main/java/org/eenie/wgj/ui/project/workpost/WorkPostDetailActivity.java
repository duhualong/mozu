package org.eenie.wgj.ui.project.workpost;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.PostWorkList;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/24 at 10:50
 * Email: 472279981@qq.com
 * Des:
 */

public class WorkPostDetailActivity extends BaseActivity {
    public static final String INFO="info";
    public static final String PROJECT_ID="id";
    private PostWorkList data;
    private String projectId;
    private int mId;
    @BindView(R.id.exchange_work_name)TextView postName;
    @BindView(R.id.tv_matter)TextView postContent;
    @Override
    protected int getContentView() {
        return R.layout.activity_post_work_detail;
    }

    @Override
    protected void updateUI() {
        data=getIntent().getParcelableExtra(INFO);
        projectId=getIntent().getStringExtra(PROJECT_ID);
        if (data!=null){
            mId=data.getId();
            if (!TextUtils.isEmpty(data.getPost())){
                postName.setText(data.getPost());
            }else {
                postName.setText("无");
            }
            if (!TextUtils.isEmpty(data.getJobassignment())){
                postContent.setText(data.getJobassignment());
            }else {
                postContent.setText("无");
            }



        }



    }
    @OnClick({R.id.img_back,R.id.button_delete,R.id.button_edit})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.button_delete:
                //删除岗位设置
                deletePost(mId);


                break;
            case R.id.button_edit:
                Intent intent=new Intent(context,WorkPostEditActivity.class);
                if (data!=null){
                    intent.putExtra(WorkPostEditActivity.INFO,data);
                    if (!TextUtils.isEmpty(projectId)){
                        intent.putExtra(WorkPostEditActivity.PROJECT_ID,projectId);
                    }
                }
                startActivityForResult(intent, 1);



                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            PostWorkList mData = data.getParcelableExtra("post_work");
           // initUI(mData);
        }

    }
    private void deletePost(int id) {
        String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");
        mSubscription=mRemoteService.deletePostItem(token,id)
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

                        }

                    }
                });
    }
}
