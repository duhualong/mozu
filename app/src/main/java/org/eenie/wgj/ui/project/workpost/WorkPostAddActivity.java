package org.eenie.wgj.ui.project.workpost;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.requset.PostWorkRequest;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;

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
                if (!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(content)){
                    PostWorkRequest request=new PostWorkRequest(title,content,projectId);
                    addPostItem(token,request);

                }

                break;
        }

    }

    private void addPostItem(String token, PostWorkRequest request) {

    }
}
