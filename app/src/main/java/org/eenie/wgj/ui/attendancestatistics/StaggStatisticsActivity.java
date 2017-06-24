//package org.eenie.wgj.ui.attendancestatistics;
//
//import android.content.Intent;
//import android.widget.TextView;
//
//import org.eenie.wgj.R;
//import org.eenie.wgj.base.BaseActivity;
//import org.eenie.wgj.ui.attendance.SignCalendar;
//import org.eenie.wgj.ui.attendance.SignCalendarCellDecorator;
//import org.eenie.wgj.ui.attendance.StatisticsInfoEntity;
//import org.eenie.wgj.util.ToastUtil;
//
//import java.util.Calendar;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import rx.Observer;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Func1;
//import rx.schedulers.Schedulers;
//
///**
// * Created by Eenie on 2017/6/20 at 16:37
// * Email: 472279981@qq.com
// * Des:
// */
//
//public class StaggStatisticsActivity extends BaseActivity {
//
//
//
//    @BindView(R.id.tv_position_name)
//    TextView mTvPositionName;
//    @BindView(R.id.calendar_view)
//    SignCalendar mCalendarView;
//    @BindView(R.id.tv_check_info)
//    TextView mTvCheckInfo;
//
//
//
//    SignCalendarCellDecorator mDecorator;
//
//
//    String date;
//
//
//    @Override
//    public void initUiAndListener() {
//        ButterKnife.bind(this);
//        mTopBar.setTitle(getTitle().toString());
//        mTopBar.setLeftIcon(R.mipmap.ic_back);
//        setSupportActionBar(mTopBar.getToolbar());
//        mDecorator = new SignCalendarCellDecorator();
//        mCalendarView.setSignCalendarCellDecorator(mDecorator);
//        mCalendarView.initMonthView();
//        mTvPositionName.setText(String.format("%1s | %2s",
//                mUserStorage.getUserData().getName(), mUserStorage.getUserData().getPermissions_name()));
//
//        if (entity != null && date != null) {
//            getStatisticInfo(String.valueOf(entity.getId()), date);
//            mTvPositionName.setText(String.format("%1s | %2s", entity.getName(), entity.getPermissions()));
//        } else {
//            getStatisticInfo();
//        }
//
//    }
//
//    @Override
//    protected boolean isApplyStatusBarTranslucency() {
//        return false;
//    }
//
//    @Override
//    protected boolean isApplyStatusBarColor() {
//        return true;
//    }
//
//
//    @OnClick(R.id.tv_check_info)
//    public void onClick() {
//        Intent intent = new Intent(mContext, SignErrorInfoActivity.class);
//        intent.putParcelableArrayListExtra("error", mDecorator.getSignErrorData());
//        startActivity(intent);
//    }
//
//
//    private void getStatisticInfo() {
//        mAttendanceApi.getStatistics()
//                .subscribeOn(Schedulers.io())
//                .map(new Func1<StatisticsInfoEntity, List<StatisticsInfoEntity.ResultMessageBean>>() {
//                    @Override
//                    public List<StatisticsInfoEntity.ResultMessageBean> call(StatisticsInfoEntity entity) {
//                        return entity.getResultMessage();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<StatisticsInfoEntity.ResultMessageBean>>() {
//                    @Override
//                    public void onCompleted() {
//                        mCalendarView.initMonthView();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        ToastUtil.showToast("获取考勤信息失败");
//                    }
//
//                    @Override
//                    public void onNext(List<StatisticsInfoEntity.ResultMessageBean> results) {
//                        mDecorator.setData(results);
//                    }
//                });
//    }
//
//
//    private void getStatisticInfo(String userid, final String date) {
//
//
//        mAttendanceApi.getStatistics(userid, date)
//                .subscribeOn(Schedulers.io())
//                .map(new Func1<StatisticsInfoEntity, List<StatisticsInfoEntity.ResultMessageBean>>() {
//                    @Override
//                    public List<StatisticsInfoEntity.ResultMessageBean> call(StatisticsInfoEntity entity) {
//                        return entity.getResultMessage();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<StatisticsInfoEntity.ResultMessageBean>>() {
//                    @Override
//                    public void onCompleted() {
////                        mCalendarView.initMonthView();
//                        try {
//                            String[] ca = date.split("-");
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set(Calendar.YEAR, Integer.parseInt(ca[0]));
//                            calendar.set(Calendar.MONTH, Integer.parseInt(ca[1]) - 1);
//                            mCalendarView.initMonthView(calendar);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        ToastUtil.showToast("获取考勤信息失败");
//                    }
//
//                    @Override
//                    public void onNext(List<StatisticsInfoEntity.ResultMessageBean> results) {
//                        mDecorator.setData(results);
//                    }
//                });
//    }
//}
