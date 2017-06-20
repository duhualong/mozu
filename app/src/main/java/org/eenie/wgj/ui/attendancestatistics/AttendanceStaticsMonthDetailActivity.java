package org.eenie.wgj.ui.attendancestatistics;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceMonthItem;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/19 at 17:29
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceStaticsMonthDetailActivity extends BaseActivity {
    public static final String DATE = "date";
    public static final String PROJECT_ID = "id";
    @BindViews({R.id.tv_date_one, R.id.tv_date_two, R.id.tv_date_three,
            R.id.tv_date_four, R.id.tv_date_five})
    List<TextView> mTextViewList;
    private String projectId;
    private String date;
    @BindView(R.id.tv_late_count)
    TextView tvLateCount;
    @BindView(R.id.tv_leave_early_count)
    TextView tvLeaveEarlyCount;
    @BindView(R.id.tv_outside_count)
    TextView tvOutSideCount;
    @BindView(R.id.tv_leave_count)
    TextView tvLeaveCount;
    @BindView(R.id.tv_absent_count)
    TextView tvAbsentCount;
    @BindView(R.id.tv_overtime_count)
    TextView tvOvertimeCount;
    @BindView(R.id.tv_experience_count)
    TextView tvExperienceCount;
    @BindView(R.id.tv_second_count)
    TextView tvlendCount;
    @BindView(R.id.tv_leave_people)
    TextView tvLeavePeople;
    @BindView(R.id.tv_add_people)
    TextView tvAddPeople;
    @BindView(R.id.donut_progress)
    DonutProgress mDonutProgress;

    @BindView(R.id.rl_sort_all)RelativeLayout rlAll;
    @BindView(R.id.rl_first_all_gold)RelativeLayout rlAllFirst;
    @BindView(R.id.img_avatar_all_first)CircleImageView avatarFirstAll;
    @BindView(R.id.tv_all_first_gold)TextView tvFirstNameAll;
    @BindView(R.id.rl_second_all_gold)RelativeLayout rlAllSecond;
    @BindView(R.id.img_avatar_all_second)CircleImageView avatarSecondAll;
    @BindView(R.id.tv_all_second_gold)TextView tvSecondNameAll;
    @BindView(R.id.rl_third_all_gold)RelativeLayout rlAllThird;
    @BindView(R.id.img_avatar_all_third)CircleImageView avatarThirdAll;
    @BindView(R.id.tv_all_third_gold)TextView tvThirdNameAll;

    @BindView(R.id.rl_sort_team)RelativeLayout rlSortTeam;
    @BindView(R.id.rl_first_team_gold)RelativeLayout rlTeamFirst;
    @BindView(R.id.img_avatar_team_first)CircleImageView avatarFirstTeam;
    @BindView(R.id.tv_team_first_gold)TextView tvFirstNameTeam;
    @BindView(R.id.rl_second_team_gold)RelativeLayout rlTeamSecond;
    @BindView(R.id.img_avatar_team_second)CircleImageView avatarSecondTeam;
    @BindView(R.id.tv_team_second_gold)TextView tvSecondNameTeam;
    @BindView(R.id.rl_third_team_gold)RelativeLayout rlTeamThird;
    @BindView(R.id.img_avatar_team_third)CircleImageView avatarThirdTeam;
    @BindView(R.id.tv_team_third_gold)TextView tvThirdNameTeam;

    @BindView(R.id.rl_fighting_team)RelativeLayout rlFightingSort;
    @BindView(R.id.rl_first_fighting_gold) RelativeLayout rlFightingFirst;




    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_static_month_detail;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        date = getIntent().getStringExtra(DATE);
        if (date != null) {
            for (int i = 0; i < mTextViewList.size(); i++) {
                mTextViewList.get(i).setText(date);
            }
        }
        getAttendanceData(projectId, date);

    }

    private void getAttendanceData(String projectId, String date) {
        mSubscription = mRemoteService.getMonthSortItem(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), projectId, date)
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
                        if (apiResponse.getResultCode() == 200) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            AttendanceMonthItem data = gson.fromJson(jsonArray,
                                    new TypeToken<AttendanceMonthItem>() {
                                    }.getType());
                            Log.d("myTest", "onNext: "+gson.toJson(data));

                            if (data != null) {
                                    tvLateCount.setText(Html.fromHtml("<u>"+data.getLate()+"次"+"<u/>"));
                                    tvLeaveEarlyCount.setText(Html.fromHtml("<u>"+
                                            data.getEarly()+"次"+"<u/>"));
                                    tvOutSideCount.setText(Html.fromHtml("<u>"+
                                            data.getGo_out()+"次"+"<u/>"));
                                    tvLeaveCount.setText(Html.fromHtml("<u>"+
                                            data.getLeave()+"次"+"<u/>"));
                                    tvAbsentCount.setText(Html.fromHtml("<u>"+
                                            data.getAbsenteeism()+"次"+"<u/>"));
                                    tvOvertimeCount.setText(Html.fromHtml("<u>"+
                                            data.getOvertime()+"次"+"<u/>"));
                                    tvExperienceCount.setText(Html.fromHtml("<u>"+
                                            data.getPractice()+"次"+"<u/>"));
                                    tvlendCount.setText(Html.fromHtml("<u>"+
                                            data.getSeconded()+"次"+"<u/>"));
                                    tvLeavePeople.setText(data.getOutgoing_employee()+"人");
                                    tvAddPeople.setText(data.getNew_employees()+"人");
                                    mDonutProgress.setDonut_progress(
                                            String.valueOf(data.getTurnover_rate()));
                                if (data.getTurnover_rate()<1){
                                    mDonutProgress.setProgress(data.getTurnover_rate()*100);
                                }else {
                                    mDonutProgress.setProgress(data.getTurnover_rate());
                                }
                                if (data.getMonth_rank()!=null){
                                    updateTeamUI(data.getMonth_rank());
                                }else {
                                    rlTeamFirst.setVisibility(View.GONE);
                                    rlTeamSecond.setVisibility(View.GONE);
                                    rlTeamThird.setVisibility(View.GONE);

                                }
                                if (data.getMonth_refuel()!=null){

                                }else {

                                }


                                    if (data.getMonth_integrated()!=null) {
                                        Log.d("MyTest", "onNext: "+
                                                gson.toJson(data.getMonth_integrated()));
                                        if (data.getMonth_integrated().size()==1){
                                            rlAllFirst.setVisibility(View.VISIBLE);
                                            if (data.getMonth_integrated().get(0).
                                                    getId_card_head_image()!=null){
                                                Glide.with(context).load(Constant.DOMIN+
                                                        data.getMonth_integrated().get(0).
                                                                getId_card_head_image())
                                                        .centerCrop().into(avatarFirstAll);
                                            }
                                            tvFirstNameAll.setText(data.getMonth_integrated().
                                                    get(0).getName());
                                            rlAllSecond.setVisibility(View.GONE);
                                            rlAllThird.setVisibility(View.GONE);
                                        }else if (data.getMonth_integrated().size()==2){
                                            rlAllFirst.setVisibility(View.VISIBLE);
                                            rlAllSecond.setVisibility(View.VISIBLE);
                                            rlAllThird.setVisibility(View.GONE);
                                            if (data.getMonth_integrated().get(0).
                                                    getId_card_head_image()!=null){
                                                Glide.with(context).load(Constant.DOMIN+
                                                        data.getMonth_integrated().get(0).
                                                                getId_card_head_image())
                                                        .centerCrop().into(avatarFirstAll);
                                            }
                                            tvFirstNameAll.setText(data.getMonth_integrated().
                                                    get(0).getName());
                                            if (data.getMonth_integrated().get(1).
                                                    getId_card_head_image()!=null){
                                                Glide.with(context).load(Constant.DOMIN+
                                                        data.getMonth_integrated().get(1).
                                                                getId_card_head_image())
                                                        .centerCrop().into(avatarSecondAll);
                                            }

                                            tvSecondNameAll.setText(data.getMonth_integrated().
                                                    get(1).getName());

                                        }else if (data.getMonth_integrated().size()>=3){
                                            rlAllFirst.setVisibility(View.VISIBLE);
                                            rlAllSecond.setVisibility(View.VISIBLE);
                                            rlAllThird.setVisibility(View.VISIBLE);
                                            if (data.getMonth_integrated().get(0).
                                                    getId_card_head_image()!=null){
                                                Glide.with(context).load(Constant.DOMIN+
                                                        data.getMonth_integrated().get(0).
                                                                getId_card_head_image())
                                                        .centerCrop().into(avatarFirstAll);
                                            }
                                            tvFirstNameAll.setText(data.getMonth_integrated().
                                                    get(0).getName());
                                            if (data.getMonth_integrated().get(1).
                                                    getId_card_head_image()!=null){
                                                Glide.with(context).load(Constant.DOMIN+
                                                        data.getMonth_integrated().get(1).
                                                                getId_card_head_image())
                                                        .centerCrop().into(avatarSecondAll);
                                            }
                                            tvSecondNameAll.setText(data.getMonth_integrated().
                                                    get(1).getName());
                                            if (data.getMonth_integrated().get(2).
                                                    getId_card_head_image()!=null){
                                                Glide.with(context).load(Constant.DOMIN+
                                                        data.getMonth_integrated().get(2).
                                                                getId_card_head_image())
                                                        .centerCrop().into(avatarThirdAll);
                                            }

                                            tvThirdNameAll.setText(data.getMonth_integrated().
                                                    get(2).getName());
                                        }

                                    }else {
                                        rlAll.setClickable(false);
                                        rlAllFirst.setVisibility(View.GONE);
                                        rlAllSecond.setVisibility(View.GONE);
                                        rlAllThird.setVisibility(View.GONE);

                                    }
                                }
                            }
                        }

                });

    }

    private void updateTeamUI(List<AttendanceMonthItem.MonthRankBean> mData) {
        if (mData!=null){
            if (mData.size()==1){
                rlTeamFirst.setVisibility(View.VISIBLE);
                rlTeamSecond.setVisibility(View.GONE);
                rlTeamThird.setVisibility(View.GONE);
                if (mData.get(0).
                        getId_card_head_image()!=null){
                    Glide.with(context).load(Constant.DOMIN+
                            mData.get(0).
                                    getId_card_head_image())
                            .centerCrop().into(avatarFirstTeam);
                }
                tvFirstNameTeam.setText(mData.
                        get(0).getName());
            }else if (mData.size()==2){
                rlTeamFirst.setVisibility(View.VISIBLE);
                rlTeamSecond.setVisibility(View.VISIBLE);
                rlTeamThird.setVisibility(View.GONE);
                if (mData.get(0).
                        getId_card_head_image()!=null){
                    Glide.with(context).load(Constant.DOMIN+
                            mData.get(0).
                                    getId_card_head_image())
                            .centerCrop().into(avatarFirstTeam);
                }
                tvFirstNameTeam.setText(mData.
                        get(0).getName());
                if (mData.get(1).
                        getId_card_head_image()!=null){
                    Glide.with(context).load(Constant.DOMIN+
                            mData.get(1).
                                    getId_card_head_image())
                            .centerCrop().into(avatarSecondTeam);
                }
                tvSecondNameTeam.setText(mData.
                        get(1).getName());
            }else if (mData.size()>=3){
                rlTeamFirst.setVisibility(View.VISIBLE);
                rlTeamSecond.setVisibility(View.VISIBLE);
                rlTeamThird.setVisibility(View.VISIBLE);
                if (mData.get(0).
                        getId_card_head_image()!=null){
                    Glide.with(context).load(Constant.DOMIN+
                            mData.get(0).
                                    getId_card_head_image())
                            .centerCrop().into(avatarFirstTeam);
                }
                tvFirstNameTeam.setText(mData.
                        get(0).getName());
                if (mData.get(1).
                        getId_card_head_image()!=null){
                    Glide.with(context).load(Constant.DOMIN+
                            mData.get(1).
                                    getId_card_head_image())
                            .centerCrop().into(avatarSecondTeam);
                }
                tvSecondNameTeam.setText(mData.
                        get(1).getName());

                if (mData.get(2).
                        getId_card_head_image()!=null){
                    Glide.with(context).load(Constant.DOMIN+
                            mData.get(2).
                                    getId_card_head_image())
                            .centerCrop().into(avatarThirdTeam);
                }
                tvThirdNameTeam.setText(mData.
                        get(2).getName());
            }

        }else {

        }
    }

    @OnClick({R.id.img_back, R.id.rl_sort_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.rl_sort_all:


                break;
        }
    }
}
