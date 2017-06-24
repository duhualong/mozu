package org.eenie.wgj.ui.routinginspection;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.LineDetailResponse;
import org.eenie.wgj.model.response.RoutingLineListResponse;
import org.eenie.wgj.ui.routinginspection.base.HintProgressBar;
import org.eenie.wgj.ui.routinginspection.startrouting.RoutingStartSettingActivity;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/23 at 10:40
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingInspectionActivity extends BaseActivity {

    @BindView(R.id.tv_turn_total)
    TextView mTvTurnTotal;
    @BindView(R.id.turn_progress)
    HintProgressBar mTurnProgress;
    @BindView(R.id.tv_turn_complete)
    TextView mTvTurnComplete;
    @BindView(R.id.point_progress)
    HintProgressBar mPointProgress;
    @BindView(R.id.tv_point_complete)
    TextView mTvPointComplete;
    @BindView(R.id.tv_routing_line_name)
    TextView tvLineName;
    @BindView(R.id.rl_select_line)
    RelativeLayout rlSelectLine;
    @BindView(R.id.img_right)
    ImageView imgRight;
    boolean checked=false;
    private int lineId;

    @Override
    protected int getContentView() {
        return R.layout.activity_inspection_routing_setting;
    }

    @Override
    protected void updateUI() {
        getLineRoutingList(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""));


    }

    private void getLineRoutingList(String token) {
        mSubscription = mRemoteService.getRoutingLineList(token)
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
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                checked=true;
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<RoutingLineListResponse> data =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<RoutingLineListResponse>>() {
                                                }.getType());
                                if (data != null) {
                                    lineId=data.get(0).getId();

                                    getLineDetail(String.valueOf(data.get(0).getId()));
                                    tvLineName.setText(data.get(0).getName());
                                    if (data.size() > 1) {
                                        rlSelectLine.setClickable(true);
                                        imgRight.setVisibility(View.VISIBLE);

                                    } else {
                                        rlSelectLine.setClickable(false);
                                        imgRight.setVisibility(View.INVISIBLE);
                                    }

                                }
                            }

                        } else {
                            Toast.makeText(context,
                                    apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                            checked=false;
                            tvLineName.setText(apiResponse.getResultMessage());
                        }

                    }
                });
    }

    private void getLineDetail(String lineId) {
        mSubscription = mRemoteService.getLineDetail(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), lineId)
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
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                LineDetailResponse data =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<LineDetailResponse>() {
                                                }.getType());
                                if (data != null) {
                                    mTurnProgress.setProgress((int) data.getTurn());
                                    mPointProgress.setProgress(data.getNumber());


                                    mTvTurnComplete.setText(String.format("圈数完成进度：%s/%s",
                                            data.getTurn_ring(),
                                            data.getRing()));
                                    mTvPointComplete.setText(String.format("点数完成进度：%s/%s",
                                            data.getTurn_actual(), data.getTurn_total()));

                                }


                            }
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @OnClick({R.id.img_back, R.id.rl_select_line, R.id.rl_routing_start, R.id.rl_routing_report,
            R.id.rl_routing_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_select_line:

                break;
            case R.id.rl_routing_start:
                if (checked){
                    startActivity(new Intent(context, RoutingStartSettingActivity.class).
                            putExtra(RoutingStartSettingActivity.LINE_ID,String.valueOf(lineId)));
                }

                break;
            case R.id.rl_routing_report:
                startActivity(new Intent(context, ReportInformationActivity.class));

                break;
            case R.id.rl_routing_record:

                break;
        }
    }
}
