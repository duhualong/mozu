package org.eenie.wgj.ui.project.projecttime;

import android.content.Intent;
import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/31 at 11:59
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectTotalTimeSettingActivity extends BaseActivity {
    public static final String PROJECT_ID="project_id";
    private String projectId;

    @Override
    protected int getContentView() {
        return R.layout.activity_project_time_setting;
    }

    @Override
    protected void updateUI() {
        projectId=getIntent().getStringExtra(PROJECT_ID);


    }
    @OnClick({R.id.rl_project_personal_time,R.id.rl_project_total_time,R.id.img_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_project_personal_time:
//                Intent intents=new Intent(context,ProjectTimePersonalSettingActivity.class);
//                intents.putExtra(ProjectTimePersonalSettingActivity.PROJECT_ID,projectId);
//                startActivity(intents);
                Intent intents=new Intent(context,ProjectSettingPersonalTimeNewActivity.class);
                intents.putExtra(ProjectSettingPersonalTimeNewActivity.PROJECT_ID,projectId);
                startActivity(intents);

                break;

            case R.id.rl_project_total_time:
                Intent intent=new Intent(context,ProjectTimeSettingActivity.class);
                intent.putExtra(ProjectTimeSettingActivity.PROJECT_ID,projectId);
                startActivity(intent);

                break;
            case R.id.img_back:
                onBackPressed();
                break;

        }
    }
}
