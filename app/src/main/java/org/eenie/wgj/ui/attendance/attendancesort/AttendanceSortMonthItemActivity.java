package org.eenie.wgj.ui.attendance.attendancesort;

import android.content.Intent;
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
import org.eenie.wgj.ui.attendancestatistics.AttendanceSortMonthActivity;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.eenie.wgj.R.id.rl_sort_team;

/**
 * Created by Eenie on 2017/7/5 at 18:19
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceSortMonthItemActivity extends BaseActivity {
    public static final String PROJECT_ID = "id";
    private String projectId;
    private String date;

    @BindView(R.id.tv_date_one)
    TextView tvDateOne;
    @BindView(R.id.tv_date_two)
    TextView tvDateTwo;
    @BindView(R.id.tv_date_three)
    TextView tvDateThree;
    @BindView(R.id.img_avatar_all_first)
    CircleImageView imgFirstAll;
    @BindView(R.id.img_avatar_all_second)
    CircleImageView imgSecondAll;
    @BindView(R.id.img_avatar_all_third)
    CircleImageView imgThirdAll;

    @BindView(R.id.rl_first_all_gold)
    RelativeLayout rlAllFirst;
    @BindView(R.id.rl_second_all_gold)
    RelativeLayout rlAllSecond;
    @BindView(R.id.rl_third_all_gold)
    RelativeLayout rlAllThird;
    @BindView(R.id.rl_sort_all)
    RelativeLayout rllSortAll;
    @BindView(R.id.tv_all_first_gold)
    TextView tvFirstNameAll;
    @BindView(R.id.tv_all_second_gold)
    TextView tvSecondNameAll;
    @BindView(R.id.tv_all_third_gold)
    TextView tvThirdNameAll;


    @BindView(rl_sort_team)
    RelativeLayout rlTeamSort;
    @BindView(R.id.rl_first_team_gold)
    RelativeLayout rlFirstTeam;
    @BindView(R.id.rl_second_team_gold)
    RelativeLayout rlSecondTeam;
    @BindView(R.id.rl_third_team_gold)
    RelativeLayout rlThirdTeam;
    @BindView(R.id.img_avatar_team_first)
    CircleImageView imgTeamFirst;
    @BindView(R.id.img_avatar_team_second)
    CircleImageView imgTeamSecond;
    @BindView(R.id.img_avatar_team_third)
    CircleImageView imgTeamThird;
    @BindView(R.id.tv_team_first_gold)TextView tvFirstNameTeam;
    @BindView(R.id.tv_team_second_gold)TextView tvSecondNameTeam;
    @BindView(R.id.tv_team_third_gold)TextView tvThirdNameTeam;

    @BindView(R.id.rl_fighting_team)RelativeLayout rlFightingSort;
    @BindView(R.id.rl_first_fighting_gold) RelativeLayout rlFightingFirst;
    @BindView(R.id.img_avatar_fighting_first)CircleImageView avatarFirstFighting;
    @BindView(R.id.tv_fighting_first_gold)TextView tvFightingFirst;
    @BindView(R.id.rl_second_fighting_gold)RelativeLayout rlFightingSecond;
    @BindView(R.id.img_avatar_fighting_second)CircleImageView avatarSecondFighting;
    @BindView(R.id.tv_fighting_second_gold)TextView tvFightingSecond;
    @BindView(R.id.rl_third_fighting_gold)RelativeLayout rlFightingThird;
    @BindView(R.id.tv_fighting_third_gold)TextView tvFightingThird;
    @BindView(R.id.img_avatar_fighting_third)CircleImageView avatarThirdFighting;
    private String mDate;

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_month_sort;


    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        date = new SimpleDateFormat("yyyy年MM月").format(Calendar.getInstance().getTime());
        mDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        getData(projectId, mDate);
        tvDateOne.setText(date);
        tvDateTwo.setText(date);
        tvDateThree.setText(date);



    }

    private void getData(String projectId, String date) {
        mSubscription = mRemoteService.getMonthSortItem(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), projectId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            AttendanceMonthItem data = gson.fromJson(jsonArray,
                                    new TypeToken<AttendanceMonthItem>() {
                                    }.getType());
                            if (data != null) {

                                if (data.getMonth_integrated() != null &&
                                        !data.getMonth_integrated().isEmpty()) {
                                    initAllData(data.getMonth_integrated());


                                } else {
                                    rlAllFirst.setVisibility(View.VISIBLE);
                                    rlAllSecond.setVisibility(View.GONE);
                                    rlAllThird.setVisibility(View.GONE);
                                }

                                if (data.getMonth_rank() != null && !data.getMonth_rank().isEmpty()) {
                                    initTeamData(data.getMonth_rank());


                                } else {
                                    rlFirstTeam.setVisibility(View.GONE);
                                    rlSecondTeam.setVisibility(View.GONE);
                                    rlThirdTeam.setVisibility(View.GONE);
                                }

                                if (data.getMonth_refuel()!=null&&!data.getMonth_refuel().isEmpty()){
                                    updateFightingUI(data.getMonth_refuel());
                                }else {
                                    rlFightingFirst.setVisibility(View.GONE);
                                    rlFightingSecond.setVisibility(View.GONE);
                                    rlFightingThird.setVisibility(View.GONE);
                                }


                            } else {
                                rllSortAll.setVisibility(View.GONE);
                                rlTeamSort.setVisibility(View.GONE);
                                rlFightingSort.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                            rllSortAll.setVisibility(View.GONE);
                            rlTeamSort.setVisibility(View.GONE);
                            rlFightingSort.setVisibility(View.GONE);

                        }

                    }

                });
    }


    private void updateFightingUI(List<AttendanceMonthItem.MonthRefuelBean> mData) {
        if (mData.size()==1){
            rlFightingFirst.setVisibility(View.VISIBLE);
            rlFightingSecond.setVisibility(View.INVISIBLE);
            rlFightingThird.setVisibility(View.INVISIBLE);
            if (mData.get(0).getId_card_head_image()!=null&&!mData.get(0).getId_card_head_image().isEmpty()){
                Glide.with(context).load(Constant.DOMIN+
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstFighting);
            }
            tvFightingFirst.setText(mData.get(0).getName());

        }else if (mData.size()==2){
            rlFightingFirst.setVisibility(View.VISIBLE);
            rlFightingSecond.setVisibility(View.VISIBLE);
            rlFightingThird.setVisibility(View.INVISIBLE);
            if (mData.get(0).getId_card_head_image()!=null&&!mData.get(0).getId_card_head_image().isEmpty()){
                Glide.with(context).load(Constant.DOMIN+
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstFighting);
            }
            tvFightingFirst.setText(mData.get(0).getName());
            if (mData.get(1).getId_card_head_image()!=null&&!mData.get(1).getId_card_head_image().isEmpty()){
                Glide.with(context).load(Constant.DOMIN+
                        mData.get(1).
                                getId_card_head_image())
                        .centerCrop().into(avatarSecondFighting);
            }
            tvFightingSecond.setText(mData.get(1).getName());

        }else if (mData.size()>=3){
            rlFightingFirst.setVisibility(View.VISIBLE);
            rlFightingSecond.setVisibility(View.VISIBLE);
            rlFightingThird.setVisibility(View.VISIBLE);
            if (mData.get(0).getId_card_head_image()!=null&&!mData.get(0).getId_card_head_image().isEmpty()){
                Glide.with(context).load(Constant.DOMIN+
                        mData.get(0).
                                getId_card_head_image())
                        .centerCrop().into(avatarFirstFighting);
            }
            tvFightingFirst.setText(mData.get(0).getName());
            if (mData.get(1).getId_card_head_image()!=null&&!mData.get(1).getId_card_head_image().isEmpty()){
                Glide.with(context).load(Constant.DOMIN+
                        mData.get(1).
                                getId_card_head_image())
                        .centerCrop().into(avatarSecondFighting);
            }
            tvFightingSecond.setText(mData.get(1).getName());
            if (mData.get(2).getId_card_head_image()!=null&&!mData.get(2).getId_card_head_image().isEmpty()){
                Glide.with(context).load(Constant.DOMIN+
                        mData.get(2).
                                getId_card_head_image())
                        .centerCrop().into(avatarThirdFighting);
            }
            tvFightingThird.setText(mData.get(2).getName());

        }
    }


    private void initTeamData(List<AttendanceMonthItem.MonthRankBean> data) {

        if (data.size() == 1) {
            rlFirstTeam.setVisibility(View.VISIBLE);
            rlSecondTeam.setVisibility(View.INVISIBLE);
            rlThirdTeam.setVisibility(View.INVISIBLE);
            if (data.get(0).getId_card_head_image() != null &&
                    !data.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(0).getId_card_head_image()).centerCrop()
                        .into(imgTeamFirst);

            }
            tvFirstNameTeam.setText(data.get(0).getName());

        } else if (data.size() == 2) {
            rlFirstTeam.setVisibility(View.VISIBLE);
            rlSecondTeam.setVisibility(View.VISIBLE);
            rlThirdTeam.setVisibility(View.INVISIBLE);
            if (data.get(0).getId_card_head_image() != null &&
                    !data.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(0).getId_card_head_image()).centerCrop()
                        .into(imgTeamFirst);

            }
            if (data.get(1).getId_card_head_image() != null &&
                    !data.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(1).getId_card_head_image()).centerCrop()
                        .into(imgTeamSecond);

            }
            tvFirstNameTeam.setText(data.get(0).getName());
            tvSecondNameTeam.setText(data.get(1).getName());

        } else if (data.size() >= 3) {
            rlFirstTeam.setVisibility(View.VISIBLE);
            rlSecondTeam.setVisibility(View.VISIBLE);
            rlThirdTeam.setVisibility(View.VISIBLE);
            if (data.get(0).getId_card_head_image() != null &&
                    !data.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(0).getId_card_head_image()).centerCrop()
                        .into(imgTeamFirst);

            }
            if (data.get(1).getId_card_head_image() != null &&
                    !data.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(1).getId_card_head_image()).centerCrop()
                        .into(imgTeamSecond);

            }
            if (data.get(2).getId_card_head_image() != null &&
                    !data.get(2).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(2).getId_card_head_image()).centerCrop()
                        .into(imgTeamThird);

            }
            tvFirstNameTeam.setText(data.get(0).getName());
            tvSecondNameTeam.setText(data.get(1).getName());
            tvThirdNameTeam.setText(data.get(2).getName());

        }

    }


    private void initAllData(List<AttendanceMonthItem.MonthIntegratedBean> data) {

        if (data.size() == 1) {
            rlAllFirst.setVisibility(View.VISIBLE);
            rlAllSecond.setVisibility(View.INVISIBLE);
            rlAllThird.setVisibility(View.INVISIBLE);
            if (data.get(0).getId_card_head_image() != null &&
                    !data.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(0).getId_card_head_image()).centerCrop()
                        .into(imgFirstAll);

            }
            tvFirstNameAll.setText(data.get(0).getName());


        } else if (data.size() == 2) {
            rlAllFirst.setVisibility(View.VISIBLE);
            rlAllSecond.setVisibility(View.VISIBLE);
            rlAllThird.setVisibility(View.INVISIBLE);
            tvFirstNameAll.setText(data.get(0).getName());
            tvSecondNameAll.setText(data.get(1).getName());
            if (data.get(0).getId_card_head_image() != null &&
                    !data.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(0).getId_card_head_image()).centerCrop()
                        .into(imgFirstAll);

            }
            if (data.get(1).getId_card_head_image() != null &&
                    !data.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(1).getId_card_head_image()).centerCrop()
                        .into(imgSecondAll);

            }

        } else if (data.size() >= 3) {
            rlAllFirst.setVisibility(View.VISIBLE);
            rlAllSecond.setVisibility(View.VISIBLE);
            rlAllThird.setVisibility(View.VISIBLE);

            tvFirstNameAll.setText(data.get(0).getName());
            tvSecondNameAll.setText(data.get(1).getName());
            tvThirdNameAll.setText(data.get(2).getName());

            if (data.get(0).getId_card_head_image() != null &&
                    !data.get(0).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(0).getId_card_head_image()).centerCrop()
                        .into(imgFirstAll);

            }
            if (data.get(1).getId_card_head_image() != null &&
                    !data.get(1).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(1).getId_card_head_image()).centerCrop()
                        .into(imgSecondAll);

            }
            if (data.get(2).getId_card_head_image() != null &&
                    !data.get(2).getId_card_head_image().isEmpty()) {
                Glide.with(context).load(Constant.DOMIN + data.get(2).getId_card_head_image()).centerCrop()
                        .into(imgThirdAll);

            }

        }


    }

    @OnClick({R.id.img_back, R.id.rl_sort_all, R.id.rl_sort_team,R.id.rl_fighting_team})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sort_all:
                startActivity(new Intent(context,AttendanceSortMonthActivity.class)
                        .putExtra(AttendanceSortMonthActivity.DATE,mDate)
                        .putExtra(AttendanceSortMonthActivity.PROJECT_ID,projectId));
                break;
            case R.id.rl_sort_team:
                startActivity(new Intent(context,AttendanceSortTeamMonthItemActivity.class)
                        .putExtra(AttendanceSortTeamMonthItemActivity.PROJECT_ID,projectId)
                        .putExtra(AttendanceSortTeamMonthItemActivity.DATE,mDate));
                break;
            case R.id.rl_fighting_team:
                startActivity(new Intent(context,AttendanceFightingSortMonthItemActivity.class)
                        .putExtra(AttendanceFightingSortMonthItemActivity.PROJECT_ID,projectId)
                        .putExtra(AttendanceFightingSortMonthItemActivity.DATE,mDate));
                break;
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
