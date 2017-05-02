package org.eenie.wgj.ui.personal.information;

import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/4/25 at 14:36
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalBarcodeActivity extends BaseActivity {

    @BindView(R.id.root_view)View rootView;
    @Override
    protected int getContentView() {
        return R.layout.activity_personal_barcode;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick(R.id.img_back)public void onClick(View view){
        onBackPressed();
    }
}
