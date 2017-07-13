package org.eenie.wgj.ui.noticemessage;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/13 at 17:38
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeMessageActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_notice_module;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick(R.id.img_back)public  void onClick(){
        onBackPressed();
    }
}

