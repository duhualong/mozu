package org.eenie.wgj.ui.fragment;

import android.content.Intent;
import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.ui.message.AbnormalHandleNotifyActivity;
import org.eenie.wgj.ui.message.BirthdayAlertActivity;
import org.eenie.wgj.ui.message.NoticeMessageActivity;
import org.eenie.wgj.ui.message.ToDoNoticeActivity;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/4/10 at 11:15
 * Des:
 */

public class MessagePagerFragment  extends BaseSupportFragment{

    @Override
    protected int getContentView() {
        return R.layout.fragment_message_pager;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.rl_alert_to_do,R.id.rl_alert_notice,R.id.rl_alert_birthday,
            R.id.rl_alert_abnormal})public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_alert_to_do:
                startActivity(new Intent(context, ToDoNoticeActivity.class));


                break;
            case R.id.rl_alert_notice:
                startActivity(new Intent(context, NoticeMessageActivity.class));


                break;
            case R.id.rl_alert_birthday:
                startActivity(new Intent(context, BirthdayAlertActivity.class));


                break;

            case R.id.rl_alert_abnormal:
                startActivity(new Intent(context, AbnormalHandleNotifyActivity.class));


                break;

        }
    }
}
