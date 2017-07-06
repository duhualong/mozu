package org.eenie.wgj.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyydjk.library.BannerLayout;

import org.eenie.wgj.R;
import org.eenie.wgj.adapter.GridItem;
import org.eenie.wgj.adapter.GridViewAdapter;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.ui.attendance.AttendanceActivity;
import org.eenie.wgj.ui.attendancestatistics.AttendanceStatisticsActivity;
import org.eenie.wgj.ui.reportpost.ReportPostSettingUploadActivity;
import org.eenie.wgj.ui.reportpoststatistics.ReportPostStatisticsSettingActivity;
import org.eenie.wgj.ui.routinginspection.RoutingInspectionActivity;
import org.eenie.wgj.ui.routingstatistics.RoutingStatisticsSettingActivity;
import org.eenie.wgj.ui.train.TrainStudySettingActivity;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 */

public class HomePagerFragment extends BaseSupportFragment implements AdapterView.OnItemClickListener {
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<GridItem> mGridData ;

    private Integer[] localGradText={R.string.work_attendance,R.string.work_attendance_statistics,
    R.string.routing_inspection,R.string.routing_inspection_statistics,R.string.train,
            R.string.train_statistics,R.string.report_job,R.string.report_job_statistics,

    };

    private Integer[]localGradImg={R.mipmap.ic_home_attendance,R.mipmap.ic_home_attendance_statistics,
    R.mipmap.ic_inspection,R.mipmap.ic_inspection_statistics,
            R.mipmap.ic_training,R.mipmap.ic_training_statistics,
            R.mipmap.ic_submitted_post,R.mipmap.ic_submitted_post_statistics};
    private Integer[] ids = {R.mipmap.ic_home_banner_one, R.mipmap.ic_home_banner_two,
            R.mipmap.ic_home_banner_three, R.mipmap.home_banner_default_bg3};
    private Integer[]idBottom={R.mipmap.home_banner_default_bg4,R.mipmap.home_banner_default_bg5};
    @BindView(R.id.banner_top)
    BannerLayout bannerTop;
    @BindView(R.id.banner_bottom)
    BannerLayout bannerBottom;
    @BindView(R.id.gradView)GridView mGradView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void updateUI() {

        mGradView.setSaveEnabled(false);

        initData();
        initDatas();
    }

    //初始化数据
    private void initData() {

        bannerTop.setViewRes(Arrays.asList(ids));
        bannerBottom.setViewRes(Arrays.asList(idBottom));


        mGridData = new ArrayList<>();
        for (int i=0; i<8; i++) {
            GridItem item = new GridItem();
            item.setTitle(localGradText[i]);
            item.setImage(localGradImg[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_home, mGridData);
        mGradView.setAdapter(mGridViewAdapter);
        mGradView.setOnItemClickListener(this);

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
                        Gson gson=new Gson();
                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {

                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<AttendanceListResponse>  attendanceResponse =
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
                                            putString(Constants.DATE_LIST,gson.toJson(mList))
                                            .putString(Constants.DATE_THING_LIST,gson.toJson(mLists))
                                            .apply();

                                }else {
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.DATE_LIST,"")
                                            .putString(Constants.DATE_THING_LIST,"")
                                            .apply();
                                }
                            }
                        }else {
                            mPrefsHelper.getPrefs().edit().
                                    putString(Constants.DATE_LIST,"")
                                    .putString(Constants.DATE_THING_LIST,"")
                                    .apply();

                        }

                    }
                });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                initDatas();

                startActivity(new Intent(context, AttendanceActivity.class));

                break;
            case 1:

                startActivity(new Intent(context, AttendanceStatisticsActivity.class));


                break;
            case 2:
                startActivity(new Intent(context, RoutingInspectionActivity.class));

                break;
            case 3:
                startActivity(new Intent(context, RoutingStatisticsSettingActivity.class));


                break;
            case 4:
                startActivity(new Intent(context, TrainStudySettingActivity.class));

                break;
            case 5:

                Toast.makeText(context,"开发中",Toast.LENGTH_SHORT).show();

                break;
            case 6:

                startActivity(new Intent(context, ReportPostSettingUploadActivity.class));


                break;
            case 7:
                startActivity(new Intent(context, ReportPostStatisticsSettingActivity.class));


                break;
        }

    }
}
