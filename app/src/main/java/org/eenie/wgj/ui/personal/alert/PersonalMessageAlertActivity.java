package org.eenie.wgj.ui.personal.alert;

import android.content.Intent;
import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/2 at 13:41
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalMessageAlertActivity extends BaseActivity{
    @Override
    protected int getContentView() {
        return R.layout.activity_personal_message_alert;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_back,R.id.rl_attendance_work,R.id.rl_report_job,R.id.rl_routing_job})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
            onBackPressed();
                break;
            case R.id.rl_attendance_work:

                startActivity(new Intent(context,AttendanceWorkActivity.class));
                break;
            case R.id.rl_report_job:


                break;
            case R.id.rl_routing_job:


                break;

        }
    }
}
