package org.eenie.wgj.ui.project.roundway;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.RoundWayList;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/26 at 9:47
 * Email: 472279981@qq.com
 * Des:
 */

public class EditRoundWayActivity extends BaseActivity {
    public static final String PROJECT_ID="id";
    public static final String INFO="ino";
    @BindView(R.id.et_input_work_title)EditText inputName;
    @BindView(R.id.tv_time_space)TextView tvTimeSpace;
    private AlertDialog dialog;
    private TimeAdapter mTimeAdapter;
    private String timeSpace;
    private int mId;
    private RoundWayList data;
    private String projectId;
    @Override
    protected int getContentView() {
        return R.layout.activity_round_way_edit;
    }

    @Override
    protected void updateUI() {
        projectId=getIntent().getStringExtra(PROJECT_ID);
        data=getIntent().getParcelableExtra(INFO);
        if (data!=null){
            inputName.setText(data.getName());
            mId=data.getId();

            timeSpace=data.getDifference();
            if (!TextUtils.isEmpty(timeSpace)){
                tvTimeSpace.setText(timeSpace);
                tvTimeSpace.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));

            }

        }



    }
    @OnClick({R.id.img_back,R.id.tv_save,R.id.rl_set_time_space})public void onClick(View view){
        String input=  inputName.getText().toString().trim();
        switch (view.getId()){

            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(input)){
                    if (!TextUtils.isEmpty(timeSpace)){
                        editRoundWay(input,timeSpace);


                    }else {
                        Toast.makeText(context,"请巡检点时间选择允许差值",Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(context,"请输入巡检线路名称",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.rl_set_time_space:
                showTimeSpaceDialog();

                break;
        }
    }

    private void editRoundWay(String input, String timeSpace) {
        mSubscription=mRemoteService.editLineItem(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),projectId,input,timeSpace,mId)
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
                        Toast.makeText(context,"编辑巡检线路成功",Toast.LENGTH_LONG).show();

                        Single.just("").delay(1, TimeUnit.SECONDS).
                                compose(RxUtils.applySchedulers()).
                                subscribe(s ->
                                        finish()
                                );

                    }
                });
    }

    private void showTimeSpaceDialog() {
        View view = View.inflate(context, R.layout.dialog_select_time, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        RecyclerView recyclerTime = (RecyclerView) dialog.getWindow().
                findViewById(R.id.recycler_time_space);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerTime.setLayoutManager(layoutManager);
        ArrayList<String> timeSpace = new ArrayList<>();
        timeSpace.add("5分钟");
        timeSpace.add("10分钟");
        timeSpace.add("15分钟");
        timeSpace.add("20分钟");
        timeSpace.add("25分钟");
        timeSpace.add("30分钟");
        mTimeAdapter = new TimeAdapter(context, timeSpace);
        recyclerTime.setAdapter(mTimeAdapter);

    }
    class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
        private Context context;
        private ArrayList<String> mStrings;

        public TimeAdapter(Context context, ArrayList<String> mStrings) {
            this.context = context;
            this.mStrings = mStrings;
        }

        @Override
        public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_select_post, parent, false);
            return new TimeViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TimeViewHolder holder, int position) {
            if (mStrings != null && !mStrings.isEmpty()) {
                String data = mStrings.get(position);
                holder.setItem(data);
                if (!TextUtils.isEmpty(data)) {
                    holder.itemTime.setText(data);
                }


            }

        }

        @Override
        public int getItemCount() {
            return mStrings.size();
        }

        public void addAll(ArrayList<String> contactsList) {
            this.mStrings.addAll(contactsList);
            TimeAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mStrings.clear();
            TimeAdapter.this.notifyDataSetChanged();
        }

        class TimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private TextView itemTime;
            private RelativeLayout rlTime;
            private String mString;

            public TimeViewHolder(View itemView) {

                super(itemView);
                itemTime = ButterKnife.findById(itemView, R.id.item_post);
                rlTime = ButterKnife.findById(itemView, R.id.rl_key_personal);
                rlTime.setOnClickListener(this);


            }

            public void setItem(String data) {
                mString = data;
            }

            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    timeSpace = mString;
                    tvTimeSpace.setText(timeSpace);
                    tvTimeSpace.setTextColor(ContextCompat.getColor
                            (context, R.color.titleColor));

                }


            }
        }
    }
}
