package org.eenie.wgj.ui.project;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.KeyContactList;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/17 at 15:59
 * Email: 472279981@qq.com
 * Des:
 */

public class KeyPersonalDetailActivity extends BaseActivity {
    public static final String INFO = "info";
    @BindView(R.id.img_avatar)
    CircleImageView avatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_post)
    TextView tvPost;
    @BindView(R.id.tv_wok_time)
    TextView tvWorkTime;
    @BindView(R.id.tv_car_number)
    TextView tvCarNumber;
    @BindView(R.id.tv_phone_number)
    TextView tvPhone;
    @BindView(R.id.tv_other)
    TextView tvOther;
    private  KeyContactList data;


    @Override
    protected int getContentView() {
        return R.layout.activity_key_personal_detail_item;
    }

    @Override
    protected void updateUI() {
         data = getIntent().getParcelableExtra(INFO);
        if (data != null) {
            if (!TextUtils.isEmpty(data.getName())) {
                tvName.setText(data.getName());
            } else {
                tvName.setText("无");
            }
            if (!TextUtils.isEmpty(data.getImages())) {

                Glide.with(context).load(
                        Constant.DOMIN + data.getImages()).
                        centerCrop().into(avatar);
            }
            switch (data.getInfo().getSex()) {
                case 1:
                    tvGender.setText("男");

                    break;
                case 2:
                    tvGender.setText("女");

                    break;
            }
            tvAge.setText(data.getInfo().getAge() + "岁");
            tvHeight.setText(data.getInfo().getHeight() + "cm");
            if (!TextUtils.isEmpty(data.getInfo().getJob())) {
                tvPost.setText(data.getInfo().getJob());
            } else {
                tvPost.setText("无");
            }
            if (!TextUtils.isEmpty(data.getInfo().getWorkinghours())) {
                tvWorkTime.setText(data.getInfo().getWorkinghours());
            } else {
                tvWorkTime.setText("无");
            }
            if (!TextUtils.isEmpty(data.getInfo().getNumberplates())) {
                tvCarNumber.setText(data.getInfo().getNumberplates());
            } else {
                tvCarNumber.setText("无");
            }
            if (!TextUtils.isEmpty(data.getInfo().getPhone())) {
                tvPhone.setText(data.getInfo().getPhone());
            } else {
                tvPhone.setText("无");
            }
            if (!TextUtils.isEmpty(data.getInfo().getRemarks())){
                tvOther.setText(data.getInfo().getRemarks());
            }else {
                tvOther.setText("无");
            }


        }


    }

    @OnClick({R.id.img_back, R.id.button_delete, R.id.button_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.button_delete:
                if (data!=null){
                    deleteKeyContacts(data.getId());
                }


                break;
            case R.id.button_edit:


                break;
        }
    }

    private void deleteKeyContacts(int id) {
        String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");
        if (!TextUtils.isEmpty(token)){
            mSubscription=mRemoteService.deleteKeyPersonal(token,id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ApiResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ApiResponse apiResponse) {
                            if (apiResponse.getResultCode()==200){
                                Toast.makeText(context,apiResponse.getResultMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Single.just("").delay(1, TimeUnit.SECONDS).
                                        compose(RxUtils.applySchedulers()).
                                        subscribe(s -> finish()
                                        );

                            }

                        }
                    });
        }
    }
}
