package org.eenie.wgj.ui.project.workpost;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.PostWorkRequest;
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
 * Created by Eenie on 2017/5/24 at 11:18
 * Email: 472279981@qq.com
 * Des:
 */

public class WorkPostEditActivity extends BaseActivity {
    public static final String INFO = "info";
    public static final String PROJECT_ID = "id";
    private PostWorkList data;
    private String projectId;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.et_input_work_title)
    EditText inputTitle;
    @BindView(R.id.et_input_exchange_work_content)
    EditText inputContent;
    private String title;
    private String content;
    private int mId;

    @Override
    protected int getContentView() {
        return R.layout.activity_post_work_edit;
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(INFO);
        projectId = getIntent().getStringExtra(PROJECT_ID);
        if (data != null) {
            mId=data.getId();
            if (!TextUtils.isEmpty(data.getPost())){
                title=data.getPost();
                inputTitle.setText(title);
            }
            if (!TextUtils.isEmpty(data.getJobassignment())){
                content=data.getJobassignment();
                inputContent.setText(content);
            }


        }


    }

    @OnClick({R.id.img_back, R.id.tv_save})
    public void onClick(View view) {
        title = inputTitle.getText().toString();
        content = inputContent.getText().toString();
        String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(content)&&
                        !TextUtils.isEmpty(projectId)){
                    PostWorkRequest request=new PostWorkRequest(mId,title,content,projectId);
                    editPostWork(token,request);
                }


                break;

        }
    }

    private void editPostWork(String token, PostWorkRequest request) {
        mSubscription=mRemoteService.editPostItem(token,request)
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
                            PostWorkList list = new PostWorkList(request.getId(),request.getPost(),
                                    request.getJobassignment());
                            Intent mIntent = new Intent();
                            mIntent.putExtra("info", list);
                            // 设置结果，并进行传送
                            setResult(4,mIntent);
                            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );
                        }

                    }
                });
    }
}
