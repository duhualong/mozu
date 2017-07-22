package org.eenie.wgj.ui.attendancestatistics;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceMonthItem;
import org.eenie.wgj.model.response.attendancestatistic.AttendanceMonthItemResponse;
import org.eenie.wgj.ui.reportpoststatistics.CircularProgressBar;
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
    @BindView(R.id.tv_rate2)
    TextView tvRate;
    @BindView(R.id.pro_rate2)
    CircularProgressBar circleProgress;

    @BindView(R.id.rl_sort_all)
    RelativeLayout rlAll;
    @BindView(R.id.rl_first_all_gold)
    RelativeLayout rlAllFirst;
    @BindView(R.id.img_avatar_all_first)
    CircleImageView avatarFirstAll;
    @BindView(R.id.tv_all_first_gold)
    TextView tvFirstNameAll;
    @BindView(R.id.tv_all_second_gold)
    TextView tvSecondNameAll;
    @BindView(R.id.tv_all_third_gold)
    TextView tvThirdNameAll;
    @BindView(R.id.rl_second_all_gold)
    RelativeLayout rlAllSecond;
    @BindView(R.id.img_avatar_all_second)
    CircleImageView avatarSecondAll;
    @BindView(R.id.rl_third_all_gold)
    RelativeLayout rlAllThird;
    @BindView(R.id.img_avatar_all_third)
    CircleImageView avatarThirdAll;


    @BindView(R.id.rl_sort_team)
    RelativeLayout rlSortTeam;
    @BindView(R.id.rl_first_team_gold)
    RelativeLayout rlTeamFirst;
    @BindView(R.id.img_avatar_team_first)
    CircleImageView avatarFirstTeam;
    @BindView(R.id.tv_team_first_gold)
    TextView tvFirstNameTeam;
    @BindView(R.id.tv_team_second_gold)
    TextView tvSecondNameTeam;
    @BindView(R.id.tv_team_third_gold)
    TextView tvThirdNameTeam;
    @BindView(R.id.rl_second_team_gold)
    RelativeLayout rlTeamSecond;

    @BindView(R.id.img_avatar_team_second)
    CircleImageView avatarSecondTeam;

    @BindView(R.id.rl_third_team_gold)
    RelativeLayout rlTeamThird;
    @BindView(R.id.img_avatar_team_third)
    CircleImageView avatarThirdTeam;


    @BindView(R.id.rl_fighting_team)
    RelativeLayout rlFightingSort;
    @BindView(R.id.rl_first_fighting_gold)
    RelativeLayout rlFightingFirst;
    @BindView(R.id.img_avatar_fighting_first)
    CircleImageView avatarFirstFighting;
    @BindView(R.id.tv_fighting_first_gold)
    TextView tvFightingFirst;
    @BindView(R.id.rl_second_fighting_gold)
    RelativeLayout rlFightingSecond;
    @BindView(R.id.img_avatar_fighting_second)
    CircleImageView avatarSecondFighting;
    @BindView(R.id.tv_fighting_second_gold)
    TextView tvFightingSecond;
    @BindView(R.id.rl_third_fighting_gold)
    RelativeLayout rlFightingThird;
    @BindView(R.id.tv_fighting_third_gold)
    TextView tvFightingThird;
    @BindView(R.id.img_avatar_fighting_third)
    CircleImageView avatarThirdFighting;


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

    @OnClick({R.id.img_back, R.id.rl_sort_all, R.id.rl_sort_team, R.id.rl_fighting_team,
            R.id.tv_late_count, R.id.tv_absent_count, R.id.tv_leave_early_count, R.id.rl_add_employees,
            R.id.tv_leave_count, R.id.tv_outside_count, R.id.tv_second_count, R.id.tv_experience_count,
            R.id.tv_overtime_count, R.id.rl_leave_people})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_sort_all:
                startActivity(new Intent(context, AttendanceSortMonthActivity.class)
                        .putExtra(AttendanceSortMonthActivity.DATE, date)
                        .putExtra(AttendanceSortMonthActivity.PROJECT_ID, projectId));


                break;
            case R.id.rl_sort_team:
                startActivity(new Intent(context, AttendanceSortTeamMonthActivity.class)
                        .putExtra(AttendanceSortTeamMonthActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceSortTeamMonthActivity.DATE, date));
                break;
            case R.id.rl_fighting_team:
                startActivity(new Intent(context, AttendanceFightingSortActivity.class)
                        .putExtra(AttendanceFightingSortActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceFightingSortActivity.DATE, date));
                break;
            case R.id.tv_late_count:
                startActivity(new Intent(context, AttendanceCauseActivity.class)
                        .putExtra(AttendanceCauseActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceCauseActivity.DATE, date));
                break;
            case R.id.tv_absent_count:
                //旷工
                startActivity(new Intent(context, AttendanceAbsoluteActivity.class)
                        .putExtra(AttendanceAbsoluteActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceAbsoluteActivity.DATE, date));
                break;
            case R.id.tv_leave_early_count:
                //早退
                startActivity(new Intent(context, AttendanceLeaveEarlyActivity.class)
                        .putExtra(AttendanceLeaveEarlyActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceLeaveEarlyActivity.DATE, date));

                break;
            //新增人员
            case R.id.rl_add_employees:
                startActivity(new Intent(context, AttendanceNewEmplyeesActivity.class)
                        .putExtra(AttendanceNewEmplyeesActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceNewEmplyeesActivity.DATE, date));
                break;
            //请假人员
            case R.id.tv_leave_count:

                startActivity(new Intent(context, AttendanceLeaveActivity.class)
                        .putExtra(AttendanceLeaveActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceLeaveActivity.DATE, date));
                break;
            //外出人员
            case R.id.tv_outside_count:

                startActivity(new Intent(context, AttendanceGoOutActivity.class)
                        .putExtra(AttendanceGoOutActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceGoOutActivity.DATE, date));
                break;

            //借调人员
            case R.id.tv_second_count:
                startActivity(new Intent(context, AttendanceSecondedActivity.class)
                        .putExtra(AttendanceSecondedActivity.PROJECT_ID, projectId)
                        .putExtra(AttendanceSecondedActivity.DATE, date));
                break;
            //实习人员
            case R.id.tv_experience_count:
                startActivity(new Intent(context, AttendancePracticeActivity.class)
                        .putExtra(AttendancePracticeActivity.PROJECT_ID, projectId)
                        .putExtra(AttendancePracticeActivity.DATE, date));
                break;
            case R.id.rl_leave_people:
                Toast.makeText(context, "离职人员暂无数据", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_overtime_count:
                Toast.makeText(context, "加班考勤暂无数据", Toast.LENGTH_SHORT).show();
                break;

        }
    }
//    private void AttendanceData(String projectId, String date){
//        mSubscription = mRemoteService.getMonthSortItem(mPrefsHelper.getPrefs().
//                getString(Constants.TOKEN, ""), projectId, date)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
//                    @Override
//                    public void onNext(ApiResponse apiResponse) {
//                        Gson gson = new Gson();
//                        String jsonArray = gson.toJson(apiResponse.getData());
//                        AttendanceMonthItemResponse data= gson.fromJson(jsonArray,
//                                new TypeToken<AttendanceMonthItemResponse>() {
//                                }.getType());
//
//                        if (data!=null){
//                            tvLateCount.setText(data.getLate()+"");
//                            tvLeaveEarlyCount.setText(
//                                    data.getEarly()+"");
//                            tvOutSideCount.setText(
//                                    data.getGo_out()+"");
//                            tvLeaveCount.setText(
//                                    data.getLeave()+"");
//                            tvAbsentCount.setText(data.getAbsenteeism()+"");
//                            tvOvertimeCount.setText(
//                                    data.getOvertime()+"");
//                            tvExperienceCount.setText(
//                                    data.getPractice()+"");
//                            tvlendCount.setText(
//                                    data.getSeconded()+"");
//                            tvLeavePeople.setText(data.getOutgoing_employee()+"人");
//                            tvAddPeople.setText(data.getNew_employees()+"人");
//                            mDonutProgress.setDonut_progress(
//                                    String.valueOf(data.getTurnover_rate()));
//
//                            mDonutProgress.setProgress((int)data.getTurnover_rate());
//
//                            if (data.getMonth_integrated()!=null&&!data.getMonth_integrated().isEmpty()){
//                                updateAllUIs(data.getMonth_integrated());
//
//                            }else {
//                                rlAll.setClickable(false);
//                                rlAllFirst.setVisibility(View.GONE);
//                                rlAllSecond.setVisibility(View.GONE);
//                                rlAllThird.setVisibility(View.GONE);
//                            }
//
//
//
//                        }
//                    }
//                });
//
//    }

    private void updateAllUIs(List<AttendanceMonthItemResponse.MonthIntegratedBean> monthIntegrated) {
        if (monthIntegrated.size() == 1) {
            rlAllFirst.setVisibility(View.VISIBLE);
            if (monthIntegrated.get(0).
                    getId_card_head_image() != null &&
                    monthIntegrated.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        monthIntegrated.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstAll);
            }
            tvFirstNameAll.setText(monthIntegrated.
                    get(0).getName());
            rlAllSecond.setVisibility(View.GONE);
            rlAllThird.setVisibility(View.GONE);
        } else if (monthIntegrated.size() == 2) {
            rlAllFirst.setVisibility(View.VISIBLE);
            rlAllSecond.setVisibility(View.VISIBLE);
            rlAllThird.setVisibility(View.GONE);

            if (monthIntegrated.get(0).
                    getId_card_head_image() != null &&
                    monthIntegrated.get(0).
                            getId_card_head_image().isEmpty()) {

                Glide.with(context).load(Constant.DOMIN +
                        monthIntegrated.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstAll);
            }
            tvFirstNameAll.setText(monthIntegrated.
                    get(0).getName());
            if (monthIntegrated.get(1).
                    getId_card_head_image() != null &&
                    !monthIntegrated.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        monthIntegrated.get(1).
                                getId_card_head_image())
                        .centerCrop().into(avatarSecondAll);
            }

            tvSecondNameAll.setText(monthIntegrated.
                    get(1).getName());

        } else if (monthIntegrated.size() >= 3) {
            rlAllFirst.setVisibility(View.VISIBLE);
            rlAllSecond.setVisibility(View.VISIBLE);
            rlAllThird.setVisibility(View.VISIBLE);
            if (monthIntegrated.get(0).
                    getId_card_head_image() != null &&
                    !monthIntegrated.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        monthIntegrated.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstAll);
            }
            tvFirstNameAll.setText(monthIntegrated.
                    get(0).getName());
            if (monthIntegrated.get(1).
                    getId_card_head_image() != null &&
                    !monthIntegrated.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        monthIntegrated.get(1).
                                getId_card_head_image())
                        .centerCrop().into(avatarSecondAll);
            }
            tvSecondNameAll.setText(monthIntegrated.
                    get(1).getName());
            if (monthIntegrated.get(2).
                    getId_card_head_image() != null &&
                    !monthIntegrated.get(2).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        monthIntegrated.get(2).
                                getId_card_head_image())
                        .centerCrop().into(avatarThirdAll);
            }

            tvThirdNameAll.setText(monthIntegrated.
                    get(2).getName());
        }


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
                            Log.d("myTest", "onNext: " + gson.toJson(data));

                            if (data != null) {
                                tvLateCount.setText(data.getLate() + "");
                                tvLeaveEarlyCount.setText(
                                        data.getEarly() + "");
                                tvOutSideCount.setText(
                                        data.getGo_out() + "");
                                tvLeaveCount.setText(
                                        data.getLeave() + "");
                                tvAbsentCount.setText(data.getAbsenteeism() + "");
                                tvOvertimeCount.setText(
                                        data.getOvertime() + "");
                                tvExperienceCount.setText(
                                        data.getPractice() + "");
                                tvlendCount.setText(
                                        data.getSeconded() + "");
                                tvLeavePeople.setText(data.getOutgoing_employee() + "人");
                                tvAddPeople.setText(data.getNew_employees() + "人");
                                circleProgress.setProgress((int)data.getTurnover_rate());
                                tvRate.setText(data.getTurnover_rate()+"%");

//                                    mDonutProgress.setDonut_progress(
//                                            String.valueOf(data.getTurnover_rate()));
//
//                                    mDonutProgress.setProgress((int)data.getTurnover_rate());

                                if (data.getMonth_rank() != null) {
                                    updateTeamUI(data.getMonth_rank());
                                } else {
                                    rlSortTeam.setClickable(false);
                                    rlTeamFirst.setVisibility(View.GONE);
                                    rlTeamSecond.setVisibility(View.GONE);
                                    rlTeamThird.setVisibility(View.GONE);

                                }
                                if (data.getMonth_refuel() != null) {
                                    updateFightingUI(data.getMonth_refuel());

                                } else {
                                    rlFightingSort.setClickable(false);
                                    rlFightingFirst.setVisibility(View.GONE);
                                    rlFightingSecond.setVisibility(View.GONE);
                                    rlFightingThird.setVisibility(View.GONE);

                                }

                                if (data.getMonth_integrated() != null) {
                                    Log.d("MyTest", "onNext: " +
                                            gson.toJson(data.getMonth_integrated()));
                                    if (data.getMonth_integrated().size() == 1) {
                                        rlAllFirst.setVisibility(View.VISIBLE);
                                        if (data.getMonth_integrated().get(0).
                                                getId_card_head_image() != null &&
                                                !data.getMonth_integrated().get(0).getId_card_head_image().isEmpty()) {
                                            Glide.with(context).load(Constant.DOMIN +
                                                    data.getMonth_integrated().get(0).
                                                            getId_card_head_image())
                                                    .centerCrop().into(avatarFirstAll);
                                        }
                                        tvFirstNameAll.setText(data.getMonth_integrated().
                                                get(0).getName());
                                        rlAllSecond.setVisibility(View.GONE);
                                        rlAllThird.setVisibility(View.GONE);
                                    } else if (data.getMonth_integrated().size() == 2) {
                                        rlAllFirst.setVisibility(View.VISIBLE);
                                        rlAllSecond.setVisibility(View.VISIBLE);
                                        rlAllThird.setVisibility(View.GONE);
                                        if (data.getMonth_integrated().get(0).
                                                getId_card_head_image() != null &&
                                                !data.getMonth_integrated().get(0).
                                                        getId_card_head_image().isEmpty()) {

                                            Glide.with(context).load(Constant.DOMIN +
                                                    data.getMonth_integrated().get(0).
                                                            getId_card_head_image())
                                                    .centerCrop().into(avatarFirstAll);
                                        }
                                        tvFirstNameAll.setText(data.getMonth_integrated().
                                                get(0).getName());
                                        if (data.getMonth_integrated().get(1).
                                                getId_card_head_image() != null &&
                                                !data.getMonth_integrated().get(1).getId_card_head_image().isEmpty()) {
                                            Glide.with(context).load(Constant.DOMIN +
                                                    data.getMonth_integrated().get(1).
                                                            getId_card_head_image())
                                                    .centerCrop().into(avatarSecondAll);
                                        }

                                        tvSecondNameAll.setText(data.getMonth_integrated().
                                                get(1).getName());

                                    } else if (data.getMonth_integrated().size() >= 3) {
                                        rlAllFirst.setVisibility(View.VISIBLE);
                                        rlAllSecond.setVisibility(View.VISIBLE);
                                        rlAllThird.setVisibility(View.VISIBLE);
                                        if (data.getMonth_integrated().get(0).
                                                getId_card_head_image() != null &&
                                                !data.getMonth_integrated().get(0).getId_card_head_image().isEmpty()) {
                                            Glide.with(context).load(Constant.DOMIN +
                                                    data.getMonth_integrated().get(0).
                                                            getId_card_head_image())
                                                    .centerCrop().into(avatarFirstAll);
                                        }
                                        tvFirstNameAll.setText(data.getMonth_integrated().
                                                get(0).getName());
                                        if (data.getMonth_integrated().get(1).
                                                getId_card_head_image() != null &&
                                                !data.getMonth_integrated().get(1).getId_card_head_image().isEmpty()) {
                                            Glide.with(context).load(Constant.DOMIN +
                                                    data.getMonth_integrated().get(1).
                                                            getId_card_head_image())
                                                    .centerCrop().into(avatarSecondAll);
                                        }
                                        tvSecondNameAll.setText(data.getMonth_integrated().
                                                get(1).getName());
                                        if (data.getMonth_integrated().get(2).
                                                getId_card_head_image() != null &&
                                                !data.getMonth_integrated().get(2).getId_card_head_image().isEmpty()) {
                                            Glide.with(context).load(Constant.DOMIN +
                                                    data.getMonth_integrated().get(2).
                                                            getId_card_head_image())
                                                    .centerCrop().into(avatarThirdAll);
                                        }

                                        tvThirdNameAll.setText(data.getMonth_integrated().
                                                get(2).getName());
                                    }

                                } else {
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

    private void updateFightingUI(List<AttendanceMonthItem.MonthRefuelBean> mData) {
        if (mData.size() == 1) {
            rlFightingFirst.setVisibility(View.VISIBLE);
            rlFightingSecond.setVisibility(View.GONE);
            rlFightingThird.setVisibility(View.GONE);
            if (mData.get(0).getId_card_head_image() != null && !mData.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstFighting);
            }
            tvFightingFirst.setText(mData.get(0).getName());

        } else if (mData.size() == 2) {
            rlFightingFirst.setVisibility(View.VISIBLE);
            rlFightingSecond.setVisibility(View.VISIBLE);
            rlFightingThird.setVisibility(View.GONE);
            if (mData.get(0).getId_card_head_image() != null && !mData.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstFighting);
            }
            tvFightingFirst.setText(mData.get(0).getName());
            if (mData.get(1).getId_card_head_image() != null && !mData.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(1).
                                getId_card_head_image())
                        .centerCrop().into(avatarSecondFighting);
            }
            tvFightingSecond.setText(mData.get(1).getName());

        } else if (mData.size() >= 3) {
            rlFightingFirst.setVisibility(View.VISIBLE);
            rlFightingSecond.setVisibility(View.VISIBLE);
            rlFightingThird.setVisibility(View.VISIBLE);
            if (mData.get(0).getId_card_head_image() != null && !mData.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstFighting);
            }
            tvFightingFirst.setText(mData.get(0).getName());
            if (mData.get(1).getId_card_head_image() != null && !mData.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(1).
                                getId_card_head_image())
                        .centerCrop().into(avatarSecondFighting);
            }
            tvFightingSecond.setText(mData.get(1).getName());
            if (mData.get(2).getId_card_head_image() != null && !mData.get(2).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(2).
                                getId_card_head_image())
                        .centerCrop().into(avatarThirdFighting);
            }
            tvFightingThird.setText(mData.get(2).getName());

        }
    }

    private void updateTeamUI(List<AttendanceMonthItem.MonthRankBean> mData) {
        if (mData.size() == 1) {
            rlTeamFirst.setVisibility(View.VISIBLE);
            rlTeamSecond.setVisibility(View.GONE);
            rlTeamThird.setVisibility(View.GONE);
            if (mData.get(0).
                    getId_card_head_image() != null && !mData.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstTeam);
            }
            tvFirstNameTeam.setText(mData.
                    get(0).getName());
        } else if (mData.size() == 2) {
            rlTeamFirst.setVisibility(View.VISIBLE);
            rlTeamSecond.setVisibility(View.VISIBLE);
            rlTeamThird.setVisibility(View.GONE);
            if (mData.get(0).
                    getId_card_head_image() != null && !mData.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstTeam);
            }
            tvFirstNameTeam.setText(mData.
                    get(0).getName());
            if (mData.get(1).
                    getId_card_head_image() != null && !mData.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(1).
                                getId_card_head_image())
                        .centerCrop().into(avatarSecondTeam);
            }
            tvSecondNameTeam.setText(mData.
                    get(1).getName());
        } else if (mData.size() >= 3) {
            rlTeamFirst.setVisibility(View.VISIBLE);
            rlTeamSecond.setVisibility(View.VISIBLE);
            rlTeamThird.setVisibility(View.VISIBLE);
            if (mData.get(0).
                    getId_card_head_image() != null && !mData.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstTeam);
            }
            tvFirstNameTeam.setText(mData.
                    get(0).getName());
            if (mData.get(1).
                    getId_card_head_image() != null && !mData.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(1).
                                getId_card_head_image())
                        .centerCrop().into(avatarSecondTeam);
            }
            tvSecondNameTeam.setText(mData.
                    get(1).getName());

            if (mData.get(2).
                    getId_card_head_image() != null && !mData.get(2).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN +
                        mData.get(2).
                                getId_card_head_image())
                        .centerCrop().into(avatarThirdTeam);
            }
            tvThirdNameTeam.setText(mData.
                    get(2).getName());
        }


    }

}
