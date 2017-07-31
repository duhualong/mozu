package org.eenie.wgj.ui.noticemessage;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/31 at 15:08
 * Email: 472279981@qq.com
 * Des:
 */

public class ApplyDepartmentActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_department_select;
    }

    @Override
    protected void updateUI() {

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }
}
