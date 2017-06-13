package org.eenie.wgj.ui.attendance;

import android.view.View;
import android.widget.TextView;

import org.eenie.wgj.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/13 at 9:16
 * Email: 472279981@qq.com
 * Des:
 */

public class SignInAttendanceActivity extends BaseAttendanceActivity {
    @BindView(R.id.tv_location)TextView tvLocation;
    @Override
    public int initMapView() {
        return R.id.map_view;
    }

    @Override
    public void onTimeChange(long date) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sign_in_attandance;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_back})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
