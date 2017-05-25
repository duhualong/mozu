package org.eenie.wgj.ui.project.workpost;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.PostWorkRequest;
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
 * Created by Eenie on 2017/5/24 at 12:16
 * Email: 472279981@qq.com
 * Des:
 */

public class WorkPostAddActivity extends BaseActivity {
    public static final String PROJECT_ID="id";
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.et_input_work_title)
    EditText inputTitle;
    @BindView(R.id.et_input_exchange_work_content)
    EditText inputContent;
    private String title;
    private String content;
    private String projectId;
    @Override
    protected int getContentView() {
        return R.layout.activity_post_work_edit;
    }

    @Override
    protected void updateUI() {
        projectId=getIntent().getStringExtra(PROJECT_ID);

    }
    @OnClick({R.id.img_back,R.id.tv_save})public void onClick(View view){
       title= inputTitle.getText().toString();
        content=inputContent.getText().toString();
        String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(content)&&
                        !TextUtils.isEmpty(projectId)){
                    PostWorkRequest request=new PostWorkRequest(title,content,projectId);
                    addPostItem(token,request);

                }

                break;
        }

    }

    private void addPostItem(String token, PostWorkRequest request) {
        mSubscription=mRemoteService.addPostItem(token,request)
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
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );
                        }

                    }
                });


    }
}
