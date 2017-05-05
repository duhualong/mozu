package org.eenie.wgj.ui.message;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eenie on 2017/5/5 at 10:55
 * Email: 472279981@qq.com
 * Des:
 */

public class BirthdayGiftActivity extends BaseActivity {
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.rv_gift_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.img_avatar)
    CircleImageView avatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.edit_gift)
    EditText editGift;


    @Override
    protected int getContentView() {
        return R.layout.activity_gift_send;
    }

    @Override
    protected void updateUI() {

    }

    @OnClick({R.id.img_back, R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.btn_ok:

                break;

        }
    }
}
