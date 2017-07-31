package org.eenie.wgj.ui.noticemessage;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/31 at 14:33
 * Email: 472279981@qq.com
 * Des:
 */

public class AddNoticeMessageActivity extends BaseActivity {
    @BindView(R.id.et_input_work_title)
    EditText etTitle;
    @BindView(R.id.et_input_exchange_work_content)
    EditText etContent;


    @Override
    protected int getContentView() {
        return R.layout.activity_add_notice_content;
    }

    @Override
    protected void updateUI() {
        
    }

    @OnClick({R.id.img_back, R.id.tv_apply_ok})
    public void onClick(View view) {
        String mTitle = etTitle.getText().toString();
        String mContent = etContent.getText().toString();
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_apply_ok:
                if (TextUtils.isEmpty(mTitle)) {
                    Toast.makeText(getApplicationContext(), "请输入通知标题", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(mContent)) {
                        Toast.makeText(getApplicationContext(), "请输入通知内容", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(getApplicationContext(), ApplyDepartmentActivity.class));

                    }
                }


                break;
        }
    }
}
