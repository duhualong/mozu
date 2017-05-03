package org.eenie.wgj.ui.fragment;

import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;

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


                break;
            case R.id.rl_alert_notice:


                break;
            case R.id.rl_alert_birthday:


                break;

            case R.id.rl_alert_abnormal:


                break;

        }
    }
}
