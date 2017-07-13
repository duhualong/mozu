package org.eenie.wgj;


import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.ui.contacts.FragmentContact;
import org.eenie.wgj.ui.fragment.ApplyPagerFragment;
import org.eenie.wgj.ui.fragment.HomePagerFragment;
import org.eenie.wgj.ui.fragment.MessagePagerFragment;
import org.eenie.wgj.ui.fragment.PersonalCenterFragment;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private static final int NAVIGATOR_COUNT = 5;
    private static final String TAG = "MainActivity";
    int[] navigatorMipmapNormal = {R.mipmap.ic_home_bottom_tab_main,
            R.mipmap.ic_home_bottom_tab_contact, R.mipmap.ic_home_bottom_tab_unselected_msg,
            R.mipmap.ic_home_bottom_tab_app, R.mipmap.ic_home_bottom_tab_me};
    int[] navigatorMipmapSelect = {R.mipmap.ic_home_bottom_tab_main_selected,
            R.mipmap.ic_home_bottom_tab_contact_selected, R.mipmap.ic_home_bottom_tab_msg,
            R.mipmap.ic_home_bottom_tab_app_selected, R.mipmap.ic_home_bottom_tab_me_selected
    };

    @BindView(R.id.img_message)
    ImageView imgMessage;

    //    @BindView(R.id.main_container)FrameLayout fragment;
    @BindViews({R.id.tv_home_pager, R.id.tv_contact_pager, R.id.tv_message_pager, R.id.tv_apply_pager,
            R.id.tv_mine_pager})
    List<TextView> bottomText;


    @Override
    protected int getContentView() {
        return R.layout.activity_main_pager;
    }

    @Override
    protected void updateUI() {
        initPersonalInfo();
        setCurrentPager(0);
        setCurrentNavigator(0);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, new HomePagerFragment()).commit();


    }

    private void initPersonalInfo() {

        UserId mUser = new UserId(mPrefsHelper.getPrefs().getString(Constants.UID, ""));
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
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.getData());
                        UserInforById mData = gson.fromJson(jsonArray,
                                new TypeToken<UserInforById>() {
                                }.getType());
                        if (mData != null) {
                            mPrefsHelper.getPrefs().edit().putString(Constants.PROJECTID,
                                    String.valueOf(mData.getProject_id())).apply();
                            if (!mData.getCompany_name().isEmpty() && mData.getCompany_name() != null) {
                                mPrefsHelper.getPrefs().edit().putString(Constants.COMPANY_NAME,
                                        mData.getCompany_name()).apply();

                            } else {
                                mPrefsHelper.getPrefs().edit().putString(Constants.COMPANY_NAME,
                                        "").apply();
                            }
                            if (!mData.getProject_name().isEmpty() && mData.getProject_name() != null) {
                                mPrefsHelper.getPrefs().edit().putString(Constants.PROJECT_NAME,
                                        mData.getProject_name()).apply();
                            } else {
                                mPrefsHelper.getPrefs().edit().putString(Constants.PROJECT_NAME,
                                        "").apply();
                            }



                        }
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initDatas() {

        mSubscription = mRemoteService.getAttendanceList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()))
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
                        Gson gson = new Gson();
                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {

                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<AttendanceListResponse> attendanceResponse =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<AttendanceListResponse>>() {
                                                }.getType());

                                if (attendanceResponse != null) {
                                    ArrayList<String> mList = new ArrayList<>();
                                    ArrayList<String> mLists = new ArrayList<>();
                                    for (int i = 0; i < attendanceResponse.size(); i++) {
                                        mList.add(attendanceResponse.get(i).getDay());
                                        mLists.add(attendanceResponse.get(i).getService().
                                                getServicesname());
                                    }
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.DATE_LIST, gson.toJson(mList))
                                            .putString(Constants.DATE_THING_LIST, gson.toJson(mLists))
                                            .apply();

                                } else {
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.DATE_LIST, "")
                                            .putString(Constants.DATE_THING_LIST, "")
                                            .apply();
                                }
                            }
                        } else {
                            mPrefsHelper.getPrefs().edit().
                                    putString(Constants.DATE_LIST, "")
                                    .putString(Constants.DATE_THING_LIST, "")
                                    .apply();

                        }

                    }
                });
    }

    private void refreshNavigator() {
        for (int i = 0; i < NAVIGATOR_COUNT; i++) {
            if (i != 2) {

                bottomText.get(i).
                        setCompoundDrawablesWithIntrinsicBounds(0, navigatorMipmapNormal[i], 0, 0);

            } else {
                imgMessage.setImageResource(R.mipmap.ic_home_bottom_tab_unselected_msg);
            }
            bottomText.get(i).setTextColor(ContextCompat.
                    getColor(MainActivity.this, R.color.titleColor));
        }
    }

    private void setCurrentNavigator(int index) {
        refreshNavigator();
        if (index != 2) {
            bottomText.get(index).
                    setCompoundDrawablesWithIntrinsicBounds(0, navigatorMipmapSelect[index], 0, 0);
        } else {
            imgMessage.setImageResource(R.mipmap.ic_home_bottom_tab_msg);

        }
        bottomText.get(index).setTextColor(ContextCompat.getColor
                (MainActivity.this, R.color.colorAccent));



    }



    private void setCurrentPager(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = new HomePagerFragment();
                break;
            case 1:
                fragment = new FragmentContact();

                break;
            case 2:
                fragment = new MessagePagerFragment();
                break;
            case 3:
                fragment = new ApplyPagerFragment();
                break;
            case 4:
                fragment = new PersonalCenterFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }


    @OnClick({R.id.ll_home_pager, R.id.ll_contact_pager, R.id.ll_message_pager, R.id.ll_apply_pager,
            R.id.ll_mine_pager})
    public void OnClick(View view) {
        int pageIndex = 0;
        switch (view.getId()) {

            case R.id.ll_home_pager:
                pageIndex = 0;

                break;

            case R.id.ll_contact_pager:

                pageIndex = 1;
                break;

            case R.id.ll_message_pager:
                pageIndex = 2;

                break;
            case R.id.ll_apply_pager:
                pageIndex = 3;

                break;

            case R.id.ll_mine_pager:

                pageIndex = 4;

                break;
        }
        setCurrentPager(pageIndex);
        setCurrentNavigator(pageIndex);
    }


}
