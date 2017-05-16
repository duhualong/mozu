package org.eenie.wgj.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.util.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eenie on 2017/5/15 at 14:06
 * Email: 472279981@qq.com
 * Des:
 */

public class ContactsDetailActivity extends BaseActivity {
    public static final String AVATAR="avatar";
    public static final String PHONE="phone";

    public static final String DUTY="duty";
    public static final String NAME="name";
    private String phone;
    @BindView(R.id.img_avatar)CircleImageView imgAvatar;
    @BindView(R.id.linkman_name)TextView mName;
    @BindView(R.id.linkman_duty)TextView mDuty;
    @BindView(R.id.linkman_phone)TextView mPhone;

    @Override
    protected int getContentView() {
        return R.layout.activity_contact_detail;
    }

    @Override
    protected void updateUI() {
      String avatarUrl=  getIntent().getStringExtra(AVATAR);
        if (!TextUtils.isEmpty(avatarUrl)){
            if (avatarUrl.startsWith(Constant.DOMIN)){
                Glide.with(context).load(avatarUrl).centerCrop().into(imgAvatar);
            }else {
                Glide.with(context).load(Constant.DOMIN+avatarUrl).centerCrop().into(imgAvatar);
            }


        }
        String name=getIntent().getStringExtra(NAME);
        if (!TextUtils.isEmpty(name)){
            mName.setText(name);

        }
        String duty=getIntent().getStringExtra(DUTY);
        if (!TextUtils.isEmpty(duty)){
            mDuty.setText(duty);

        }
         phone=getIntent().getStringExtra(PHONE);
        if (!TextUtils.isEmpty(phone)){
            mPhone.setText(phone);

        }

    }

    @OnClick({R.id.img_back, R.id.btn_send_msg, R.id.btn_send_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_send_msg:

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                startActivity(intent);
                break;
            case R.id.btn_send_call:
                Intent intents = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +phone));
                startActivity(intents);

                break;

        }
    }
}
