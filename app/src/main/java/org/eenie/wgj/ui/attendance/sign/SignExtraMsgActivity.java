package org.eenie.wgj.ui.attendance.sign;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static org.eenie.wgj.ui.attendance.sign.AttendanceTokePhotoActivity.RESULT_CODE;

/**
 * Created by Eenie on 2017/6/19 at 9:43
 * Email: 472279981@qq.com
 * Des:
 */

public class SignExtraMsgActivity extends BaseActivity {
    @BindView(R.id.edit_cause)EditText mEditText;

    @Override
    protected int getContentView() {
        return R.layout.activity_sign_extra_message;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_back,R.id.btn_save})public void onClick(View view){
       String inputContent= mEditText.getText().toString().trim();
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.btn_save:
                if (!TextUtils.isEmpty(inputContent)){
                    setResult(RESULT_CODE, getIntent().putExtra("extra_msg", inputContent));
                    finish();
                }else {
                    Toast.makeText(context,"请输入外出原因",Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
