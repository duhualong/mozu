package org.eenie.wgj.ui.message;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.BirthdayDetail;
import org.eenie.wgj.util.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/5 at 10:07
 * Email: 472279981@qq.com
 * Des:
 */

public class BirthdayDetailPersonalActivity extends BaseActivity {
    public static final String ID = "id";
    @BindView(R.id.img_avatar)CircleImageView avatar;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.tv_no_birthday)
    TextView noBirthday;
    @BindView(R.id.rl_birthday_tip)
    RelativeLayout rlBirthdayTip;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.birthday_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_name)TextView name;
    @BindView(R.id.no_blessing)TextView noBlessing;


    @Override
    protected int getContentView() {
        return R.layout.activity_birthday_alert;
    }

    @Override
    protected void updateUI() {
        String id = getIntent().getStringExtra(ID);
        getBirthdayDetail(id);


    }

    //得到生日详情
    private void getBirthdayDetail(String id) {
        mSubscription=mRemoteService.getBirthdayById(Constant.TOKEN,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse<BirthdayDetail>>() {
                    @Override
                    public void onSuccess(ApiResponse<BirthdayDetail> value) {
                        if (value.getResultCode()==200){
                            BirthdayDetail data=value.getData();
                            String url=Constant.DOMIN+data.getAvatar();
                            Glide.with(context)
                                    .load(url)
                                    .centerCrop()
                                    .into(avatar);
                            name.setText(data.getName());
                            if (data.getBlessing()==0){
                                noBlessing.setVisibility(View.VISIBLE);
                            }
                        }else {

                        }

                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
    }

    @OnClick({R.id.img_back, R.id.btn_give_gift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_give_gift:
                startActivity(new Intent(context,BirthdayGiftActivity.class));


                break;
        }
    }
}
