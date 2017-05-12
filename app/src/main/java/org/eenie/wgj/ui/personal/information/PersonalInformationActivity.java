package org.eenie.wgj.ui.personal.information;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/2 at 12:04
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalInformationActivity extends BaseActivity {
    @BindView(R.id.root_view)View rootView;
    @BindView(R.id.tv_height)TextView tvHeight;
    @BindView(R.id.tv_qualifications)TextView tvEducation;
    @BindView(R.id.tv_marry_state)TextView tvMarry;
    @BindView(R.id.tv_contacts)TextView tvContacts;
    @BindView(R.id.tv_industry)TextView tvIndustry;
    @BindView(R.id.tv_skill)TextView tvSkill;
    @BindView(R.id.tv_employment)TextView tvChannel;
  //  @BindView(R.id.tv_experience)TextView


    @Override
    protected int getContentView() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void updateUI() {
        String userId = mPrefsHelper.getPrefs().getString(Constants.UID, "");
        if (!TextUtils.isEmpty(userId)) {
            getUerInformationById(userId);


        }


    }

    private void getUerInformationById(String userId) {
        UserId mUser = new UserId(userId);
        mSubscription = mRemoteService.getUserInfoById(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), mUser)
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
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            UserInforById mData = gson.fromJson(jsonArray,
                                    new TypeToken<UserInforById>() {
                                    }.getType());
                            if (mData != null) {



                            }
                        }else {

                        }


                    }
                });

    }

    @OnClick({R.id.img_back,R.id.rl_height,R.id.rl_qualifications,R.id.rl_marry_state,
            R.id.rl_now_address ,R.id.rl_contacts,R.id.rl_industry,R.id.rl_skill,
    R.id.rl_work_channel,R.id.rl_work_experience,R.id.rl_work_content})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_height:


                break;
            case R.id.rl_qualifications:


                break;
            case R.id.rl_marry_state:

                break;
            case R.id.rl_now_address:


                break;
            case R.id.rl_contacts:


                break;
            case R.id.rl_industry:


                break;
            case R.id.rl_skill:

                break;

            case R.id.rl_work_channel:


                break;
            case R.id.rl_work_experience:

                break;
            case R.id.rl_work_content:


                break;

        }
    }
}
