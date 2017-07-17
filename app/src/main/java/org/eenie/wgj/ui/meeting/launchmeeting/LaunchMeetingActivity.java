package org.eenie.wgj.ui.meeting.launchmeeting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.AddMeetingRequest;
import org.eenie.wgj.model.response.meeting.MeetingData;
import org.eenie.wgj.model.response.meeting.MeetingPeople;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.eenie.wgj.R.id.edit_month;
import static org.eenie.wgj.R.id.img_add;

/**
 * Created by Eenie on 2017/7/7 at 18:06
 * Email: 472279981@qq.com
 * Des:
 */

public class LaunchMeetingActivity extends BaseActivity {
    private static final int REQUEST_HOST = 0x101;
    private static final int REQUEST_RECORD = 0x102;
    private static final int REQUEST_JOIN = 0x103;
    @BindView(R.id.et_meeting_name)
    EditText editMeetingName;
    @BindView(R.id.et_meeting_address)
    EditText editMeetingAddress;
    @BindView(R.id.checkbox_select_normal)
    CheckBox checkBoxDay;
    @BindView(R.id.checkbox_select_abnormal)
    CheckBox checkBoxSingle;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_master)
    TextView tvMaster;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.edit_meeting_purpose)
    EditText editMeetingPurpose;
    @BindView(R.id.edit_meeting_content)
    EditText editMeetingContent;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.scrollview)ScrollView mScrollView;

    private String mStartTime;
    private String mEndTime;
    int type = 0;
    private String hostId;
    private String recordId;
    private AddPersonalAdapter mAdapter;
    private ArrayList<MeetingPeople> mData = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_launch_meeting_setting;
    }

    @Override
    protected void updateUI() {
        controlKeyboardLayout(mScrollView,LaunchMeetingActivity.this);
    }

    private void controlKeyboardLayout(final ScrollView root, final Activity context) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {


            Rect rect = new Rect();
            root.getWindowVisibleDisplayFrame(rect);
            int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
            //若不可视区域高度大于100，则键盘显示
            if (rootInvisibleHeight > 80) {
                int[] location = new int[2];
                View focus = context.getCurrentFocus();
                if (focus != null) {
                    focus.getLocationInWindow(location);
                    int scrollHeight = (location[1] + focus.getHeight()) - rect.bottom;
                    if (rect.bottom < location[1] + focus.getHeight()) {
                        root.scrollTo(0, scrollHeight);
                    }
                }
            } else {
                //键盘隐藏
                root.scrollTo(0, 0);
            }
        });
    }
    @OnClick({R.id.img_back, R.id.tv_apply_ok, R.id.tv_start_time, R.id.tv_end_time, R.id.rl_master,
            R.id.rl_record, img_add, R.id.checkbox_select_normal, R.id.checkbox_select_abnormal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();


                break;
            case R.id.checkbox_select_normal:
                if (checkBoxDay.isChecked()) {
                    checkBoxSingle.setChecked(false);
                    type = 1;
                } else {
                    checkBoxSingle.setChecked(true);
                    type = 2;
                }
                tvStartTime.setText("开始时间");
                tvEndTime.setText("结束时间");
                mStartTime = "";
                mEndTime = "";
                tvStartTime.setTextSize(14);
                tvEndTime.setTextSize(14);
                break;
            case R.id.checkbox_select_abnormal:
                if (checkBoxSingle.isChecked()) {
                    checkBoxDay.setChecked(false);
                    type = 2;
                } else {
                    checkBoxDay.setChecked(true);
                    type = 1;
                }
                tvStartTime.setText("开始时间");
                tvEndTime.setText("结束时间");
                tvStartTime.setTextSize(14);
                tvEndTime.setTextSize(14);
                mStartTime = "";
                mEndTime = "";

                break;
            case R.id.tv_apply_ok:
                //提交
                List<Integer> userId = new ArrayList<>();

                if (checkInputMeeting()) {
                    for (int i = 0; i < mData.size(); i++) {
                        userId.add(mData.get(i).getId());
                    }
                    String meetingName = editMeetingName.getText().toString();
                    String meetingAddress = editMeetingAddress.getText().toString();
                    String meetingPurpose = editMeetingPurpose.getText().toString();
                    String meetingContent = editMeetingContent.getText().toString();

                    applyMeetingApply(meetingName, meetingAddress, type, mStartTime, mEndTime, hostId, recordId,
                            userId, meetingPurpose, meetingContent);

                }

                break;
            case R.id.tv_start_time:
                //开始时间
                if (type == 1) {
                    showTimeDayPicker("start");
                } else if (type == 2) {
                    showTimeSinglePicker("start");
                } else {
                    Toast.makeText(context, "请先选择会议时间", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.tv_end_time:
                //结束时间
                if (type == 1) {
                    showTimeDayPicker("end");
                } else if (type == 2) {
                    showTimeSinglePicker("end");
                } else {
                    Toast.makeText(context, "请先选择会议时间", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rl_master:
                //添加主持人
                startActivityForResult(new Intent(context, HostPeopleActivity.class).
                        putExtra(HostPeopleActivity.TYPE, "host"), REQUEST_HOST);


                break;
            case R.id.rl_record:
                //添加记录人
                startActivityForResult(new Intent(context, HostPeopleActivity.class).
                        putExtra(HostPeopleActivity.TYPE, "record"), REQUEST_RECORD);


                break;
            case R.id.img_add:
                //添加参会人员
                startActivityForResult(new Intent(context, AddPeopleSelectActivity.class), REQUEST_JOIN);


                break;
        }

    }

    private void applyMeetingApply(String meetingName, String meetingAddress, int type,
                                   String startTime, String endTime, String hostId, String recordId,
                                   List<Integer> userId, String meetingPurpose, String meetingContent) {
        AddMeetingRequest request = new AddMeetingRequest(endTime, startTime, meetingAddress, meetingContent,
                type, Integer.valueOf(hostId), Integer.valueOf(recordId), meetingPurpose, meetingName, userId);
        Gson gson = new Gson();
        mSubscription=mRemoteService.addMeetingContent(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN,""),new MeetingData(gson.toJson(request)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getCode()==0){
                            Toast.makeText(context,apiResponse.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            finish();

                        }else {
                            Toast.makeText(context,apiResponse.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });



    }

    private boolean checkInputMeeting() {
        boolean result = true;
        if (TextUtils.isEmpty(editMeetingName.getText().toString())) {
            Toast.makeText(context, "请输入会议名称", Toast.LENGTH_SHORT).show();
            result = false;

        }
        if (result && TextUtils.isEmpty(editMeetingAddress.getText().toString())) {
            Toast.makeText(context, "请输入会议地址", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (result && type == 0) {
            Toast.makeText(context, "请选择会议时间", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(mStartTime)) {
            Toast.makeText(context, "请选择会议开始时间", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (result && TextUtils.isEmpty(mEndTime)) {
            Toast.makeText(context, "请选择会议结束时间", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(hostId)) {
            Toast.makeText(context, "请选择会议主持人", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(recordId)) {
            Toast.makeText(context, "请选择会议记录人", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (result && mData.isEmpty() || mData == null) {
            Toast.makeText(context, "请选择会议参会人", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(editMeetingPurpose.getText().toString())) {
            Toast.makeText(context, "请输入会议目的", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(editMeetingContent.getText().toString())) {
            Toast.makeText(context, "请输入会议议程", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_HOST:
                    MeetingPeople mDataHost = data.getParcelableExtra("mData");
                    if (mDataHost != null) {
                        tvMaster.setText(mDataHost.getName());
                        hostId = String.valueOf(mDataHost.getId());
                        tvMaster.setTextColor(ContextCompat.getColor
                                (context, R.color.black_light));
                    }

                    break;
                case REQUEST_RECORD:
                    MeetingPeople mDataRecord = data.getParcelableExtra("mData");
                    if (mDataRecord != null) {
                        tvRecord.setText(mDataRecord.getName());
                        recordId = String.valueOf(mDataRecord.getId());
                        tvRecord.setTextColor(ContextCompat.getColor
                                (context, R.color.black_light));
                    }
                    break;
                case REQUEST_JOIN:
                    ArrayList<MeetingPeople> mList =
                            data.getParcelableArrayListExtra("mData");
                    Gson gson = new Gson();
                    Log.d("arraylist", "onActivityResult: " + gson.toJson(mList));
                    if (mData.isEmpty() || mData == null) {
                        mData = mList;
                        System.out.println("ssss");

                    } else {
                        if (mList != null) {
                            for (int j = 0; j < mList.size(); j++) {
                                boolean checked = false;
                                for (int i = 0; i < mData.size(); i++) {
                                    if (mData.get(i).getId() == mList.get(j).getId()) {
                                        checked = true;
                                    }
                                }
                                if (!checked) {
                                    mData.add(mList.get(j));
                                }
                            }
                        }
                        System.out.println("ssss");
                    }

                    Log.d("test", "onActivityResult: " + gson.toJson(mData));
                    if (mData != null) {
                        mRecyclerView.setNestedScrollingEnabled(false);
                        mAdapter = new AddPersonalAdapter(context, mData);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setAdapter(mAdapter);
                    }


                    break;
            }
        }

    }


    class AddPersonalAdapter extends RecyclerView.Adapter<AddPersonalAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<MeetingPeople> addPersonalClass;

        public AddPersonalAdapter(Context context, ArrayList<MeetingPeople> addPersonalClass) {
            this.context = context;
            this.addPersonalClass = addPersonalClass;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_notice_people, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (addPersonalClass != null && !addPersonalClass.isEmpty()) {
                MeetingPeople data = addPersonalClass.get(position);
                holder.setItem(data);

                if (data != null) {
                    if (data.getName() != null) {
                        holder.itemName.setText(data.getName());
                    }
                }
            }

        }

        @Override
        public int getItemCount() {
            return addPersonalClass.size();
        }

        public void addAll(ArrayList<MeetingPeople> projectList) {
            this.addPersonalClass.addAll(projectList);
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.addPersonalClass.clear();
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private MeetingPeople mProjectList;
            private TextView itemName;
            private RelativeLayout rlItem;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                rlItem = ButterKnife.findById(itemView, R.id.rl_item);
                rlItem.setOnClickListener(this);


            }

            public void setItem(MeetingPeople projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.rl_item:
                        addPersonalClass.remove(mProjectList);
                        notifyDataSetChanged();
                        break;

                }


            }
        }
    }

    public void showTimeSinglePicker(String sort) {
        View view = View.inflate(context, R.layout.dialog_meeting_time, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        EditText etYear = (EditText) dialog.getWindow().findViewById(R.id.et_year);
        EditText etMonth = (EditText) dialog.getWindow().findViewById(R.id.edit_month);
        EditText etDay = (EditText) dialog.getWindow().findViewById(R.id.edit_day);
        EditText etHour = (EditText) dialog.getWindow().findViewById(R.id.edit_hour);
        EditText etMinute = (EditText) dialog.getWindow().findViewById(R.id.edit_minute);
        String mYear = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        String mMonth = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        String mDay = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
        String mHour = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        String mMinute = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        etYear.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etMonth.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etDay.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etHour.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etMinute.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        final int[] year = new int[1];
        final int[] month = new int[1];
        final int[] day = new int[1];
        final int[] hour = new int[1];
        final int[] minute = new int[1];
        year[0] = Integer.valueOf(mYear);
        month[0] = Integer.valueOf(mMonth);
        day[0] = Integer.valueOf(mDay);
        hour[0] = Integer.valueOf(mHour);
        minute[0] = Integer.valueOf(mMinute);
        etYear.setText(mYear);
        etMonth.setText(mMonth);
        etDay.setText(mDay);
        etHour.setText(mHour);
        etMinute.setText(mMinute);
        etHour.setSelection(mHour.length());
        etMinute.setSelection(mMinute.length());
        dialog.getWindow().findViewById(R.id.btn_add_year).setOnClickListener(v -> {
            String inputYear = etYear.getText().toString();
            if (!TextUtils.isEmpty(inputYear)) {
                year[0] = Integer.valueOf(inputYear);
                year[0] = year[0] + 1;

            } else {
                year[0] = year[0];

            }
            etYear.setText(String.valueOf(year[0]));

        });
        dialog.getWindow().findViewById(R.id.btn_subtract_year).setOnClickListener(v -> {
            String inputYear = etYear.getText().toString();
            if (!TextUtils.isEmpty(inputYear)) {
                year[0] = Integer.valueOf(inputYear);
                if (year[0] <= Integer.valueOf(new SimpleDateFormat("yyyy").
                        format(Calendar.getInstance().getTime()))) {
                    Toast.makeText(context, "不能选择过去的年份", Toast.LENGTH_SHORT).show();
                    year[0] = year[0];
                } else {
                    year[0] = year[0] - 1;
                }
            } else {
                year[0] = year[0];
            }
            etYear.setText(String.valueOf(year[0]));

        });
        dialog.getWindow().findViewById(R.id.btn_add_month).setOnClickListener(v -> {
            String inputMonth = etMonth.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                month[0] = Integer.valueOf(inputMonth);
                if (month[0] >= 12) {
                    month[0] = Integer.valueOf(new SimpleDateFormat("MM").
                            format(Calendar.getInstance().getTime()));
                } else {
                    month[0] = month[0] + 1;
                }


            } else {
                month[0] = month[0];
            }
            if (month[0] <= 9 && String.valueOf(month[0]).length() == 1) {
                etMonth.setText("0" + String.valueOf(month[0]));

            } else {
                etMonth.setText(String.valueOf(month[0]));

            }

        });
        dialog.getWindow().findViewById(R.id.btn_subtract_month).setOnClickListener(v -> {
            String inputMonth = etMonth.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                month[0] = Integer.valueOf(inputMonth);
                if (month[0] <= Integer.valueOf(new SimpleDateFormat("MM").
                        format(Calendar.getInstance().getTime()))) {
                    month[0] = 12;
                } else {
                    month[0] = month[0] - 1;
                }


            } else {
                month[0] = month[0];
            }
            if (month[0] <= 9 && String.valueOf(month[0]).length() == 1) {
                etMonth.setText("0" + String.valueOf(month[0]));

            } else {
                etMonth.setText(String.valueOf(month[0]));

            }

        });
        dialog.getWindow().findViewById(R.id.btn_add_day).setOnClickListener(v -> {
            String inputDay = etDay.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                int maxDay = getDaysOfMonth(sdf.parse(mYear + "-" + mMonth + "-" + mDay));
                if (!TextUtils.isEmpty(inputDay)) {

                    if (day[0] >= maxDay) {
                        day[0] = 1;
                    } else {
                        day[0] = day[0] + 1;
                    }

                } else {
                    day[0] = day[0] + 1;
                }
                if (day[0] <= 9 && String.valueOf(day[0]).length() == 1) {
                    etDay.setText("0" + String.valueOf(day[0]));
                } else {
                    etDay.setText(String.valueOf(day[0]));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_day).setOnClickListener(v -> {
            String inputDay = etDay.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                int maxDay = getDaysOfMonth(sdf.parse(year[0] + "-" + month[0] + "-" + day[0]));
                if (!TextUtils.isEmpty(inputDay)) {

                    if (day[0] <= 1) {
                        day[0] = maxDay;
                    } else {
                        day[0] = day[0] - 1;
                    }

                } else {
                    day[0] = day[0];
                }
                if (day[0] <= 9 && String.valueOf(day[0]).length() == 1) {
                    etDay.setText("0" + String.valueOf(day[0]));
                } else {
                    etDay.setText(String.valueOf(day[0]));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        });

        dialog.getWindow().findViewById(R.id.btn_add_hour).setOnClickListener(v -> {
            String inputHour = etHour.getText().toString();
            if (!TextUtils.isEmpty(inputHour)) {
                hour[0] = Integer.valueOf(inputHour);
            }
            if (hour[0] >= 23) {
                hour[0] = 0;
            } else {
                hour[0] = hour[0] + 1;
            }
            if (String.valueOf(hour[0]).length() == 1) {
                etHour.setText(String.valueOf("0" + hour[0]));
            } else {
                etHour.setText(String.valueOf(hour[0]));
            }


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_hour).setOnClickListener(v -> {
            String inputHour = etHour.getText().toString();
            if (!TextUtils.isEmpty(inputHour)) {
                hour[0] = Integer.valueOf(inputHour);
            }
            if (hour[0] <= 0) {
                hour[0] = 3;
            } else {
                hour[0] = hour[0] - 1;
            }
            if (String.valueOf(hour[0]).length() == 1) {
                etHour.setText("0" + String.valueOf(hour[0]));
            } else {
                etHour.setText(String.valueOf(hour[0]));
            }


        });


        dialog.getWindow().findViewById(R.id.btn_add_minute).setOnClickListener(v -> {
            String inputMonth = etMinute.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                minute[0] = Integer.valueOf(inputMonth);
            }
            if (minute[0] >= 59) {
                minute[0] = 0;
            } else {
                minute[0] = minute[0] + 1;

            }
            if (minute[0] <= 9 && String.valueOf(minute[0]).length() == 1) {
                etMinute.setText("0" + String.valueOf(minute[0]));
            } else {
                etMinute.setText(String.valueOf(minute[0]));
            }


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_minute).setOnClickListener(v -> {
            String inputMonth = etMinute.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                minute[0] = Integer.valueOf(inputMonth);
            }
            if (minute[0] <= 0) {
                minute[0] = 60;
            } else {
                minute[0] = minute[0] - 1;
            }


            if (minute[0] <= 9 && String.valueOf(minute[0]).length() == 1) {
                etMinute.setText("0" + String.valueOf(minute[0]));
            } else {
                etMinute.setText(String.valueOf(minute[0]));
            }

        });

        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
            String selectYear = etYear.getText().toString();
            String selectMonth = etMonth.getText().toString();
            String selectDay = etDay.getText().toString();
            String selectHour = etHour.getText().toString();
            String selectMinute = etMinute.getText().toString();
            if (!TextUtils.isEmpty(selectYear)) {
                if (Integer.parseInt(selectYear) < Integer.valueOf(mYear)) {
                    selectYear = mYear;
                }

            } else {
                selectYear = String.valueOf(year[0]);
            }
            if (!TextUtils.isEmpty(selectMonth)) {
                if (Integer.parseInt(selectMonth) < Integer.valueOf(mMonth)) {
                    selectMonth = mMonth;
                }

            } else {
                selectMonth = String.valueOf(month[0]);
            }
            if (!TextUtils.isEmpty(selectDay)) {
                if (Integer.parseInt(selectDay) < Integer.valueOf(mDay) ||
                        Integer.parseInt(selectDay) > 31) {
                    selectDay = mDay;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (selectMonth.length() == 1) {
                    selectMonth = "0" + selectMonth;
                }
                try {
                    int maxDay = getDaysOfMonth(sdf.parse(selectYear + "-" +
                            selectMonth + "-" + "01"));
                    if (Integer.valueOf(selectDay) > maxDay) {
                        selectDay = mDay;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }   if (!TextUtils.isEmpty(selectMonth) && !TextUtils.isEmpty(selectYear)) {


            } else {
                selectDay = String.valueOf(day[0]);
            }

            if (TextUtils.isEmpty(selectHour)) {
                selectHour = mHour;
            } else {
                if (Integer.valueOf(selectHour) >= 24) {
                    selectHour = mHour;
                }
            }
            if (TextUtils.isEmpty(selectMinute)) {
                selectMinute = mMinute;
            } else {
                if (Integer.valueOf(selectMinute) >= 60) {
                    selectMinute = mMinute;
                }

            }
            if (selectMonth.length() == 1 && Integer.parseInt(selectMonth) <= 9) {
                selectMonth = "0" + selectMonth;
            }

            if (selectDay.length() == 1 && Integer.parseInt(selectDay) <= 9) {
                selectDay = "0" + selectDay;
            }
            if (selectHour.length() == 1 && Integer.valueOf(selectHour) <= 9) {
                selectHour = "0" + selectHour;
            }
            if (selectMinute.length() == 1 && Integer.valueOf(selectMinute) <= 9) {
                selectMinute = "0" + selectMinute;

            }
            switch (sort) {
                case "start":
                    mStartTime = selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute;
                    tvStartTime.setText(selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute);
                    tvStartTime.setTextSize(12);
                    break;
                case "end":
                    mEndTime = selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute;
                    tvEndTime.setText(selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute);
                    tvEndTime.setTextSize(12);
                    break;

            }

            dialog.dismiss();

        });

    }

    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public void showTimeDayPicker(String sort) {
        View view = View.inflate(context, R.layout.dialog_routing_time, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        EditText etHour = (EditText) dialog.getWindow().findViewById(R.id.et_year);
        EditText etMinute = (EditText) dialog.getWindow().findViewById(edit_month);
        etHour.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etMinute.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        String mHour = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        String mMinute = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());


        final int[] hour = new int[1];
        final int[] minute = new int[1];
        hour[0] = Integer.valueOf(mHour);
        minute[0] = Integer.valueOf(mMinute);
        etHour.setText(mHour);
        etMinute.setText(mMinute);
        etHour.setSelection(mHour.length());
        etMinute.setSelection(mMinute.length());

        dialog.getWindow().findViewById(R.id.btn_add_year).setOnClickListener(v -> {
            String inputHour = etHour.getText().toString();
            if (!TextUtils.isEmpty(inputHour)) {
                hour[0] = Integer.valueOf(inputHour);
            }
            if (hour[0] >= 23) {
                hour[0] = 0;
            } else {
                hour[0] = hour[0] + 1;
            }
            if (String.valueOf(hour[0]).length() == 1) {
                etHour.setText(String.valueOf("0" + hour[0]));
            } else {
                etHour.setText(String.valueOf(hour[0]));
            }


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_year).setOnClickListener(v -> {
            String inputHour = etHour.getText().toString();
            if (!TextUtils.isEmpty(inputHour)) {
                hour[0] = Integer.valueOf(inputHour);
            }
            if (hour[0] <= 0) {
                hour[0] = 3;
            } else {
                hour[0] = hour[0] - 1;
            }
            if (String.valueOf(hour[0]).length() == 1) {
                etHour.setText("0" + String.valueOf(hour[0]));
            } else {
                etHour.setText(String.valueOf(hour[0]));
            }


        });


        dialog.getWindow().findViewById(R.id.btn_add_month).setOnClickListener(v -> {
            String inputMonth = etMinute.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                minute[0] = Integer.valueOf(inputMonth);
            }
            if (minute[0] >= 59) {
                minute[0] = 0;
            } else {
                minute[0] = minute[0] + 1;

            }
            if (minute[0] <= 9 && String.valueOf(minute[0]).length() == 1) {
                etMinute.setText("0" + String.valueOf(minute[0]));
            } else {
                etMinute.setText(String.valueOf(minute[0]));
            }


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_month).setOnClickListener(v -> {
            String inputMonth = etMinute.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                minute[0] = Integer.valueOf(inputMonth);
            }
            if (minute[0] <= 0) {
                minute[0] = 60;
            } else {
                minute[0] = minute[0] - 1;
            }


            if (minute[0] <= 9 && String.valueOf(minute[0]).length() == 1) {
                etMinute.setText("0" + String.valueOf(minute[0]));
            } else {
                etMinute.setText(String.valueOf(minute[0]));
            }

        });


        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
            String selectHour = etHour.getText().toString().trim();
            String selectMinute = etMinute.getText().toString();
            if (TextUtils.isEmpty(selectHour)) {
                selectHour = mHour;
            } else {
                if (Integer.valueOf(selectHour) >= 24) {
                    selectHour = mHour;
                }
            }
            if (TextUtils.isEmpty(selectMinute)) {
                selectMinute = mMinute;
            } else {
                if (Integer.valueOf(selectMinute) >= 60) {
                    selectMinute = mMinute;
                }

            }
            if (selectHour.length() == 1 && Integer.valueOf(selectHour) <= 9) {
                selectHour = "0" + selectHour;
            }
            if (selectMinute.length() == 1 && Integer.valueOf(selectMinute) <= 9) {
                selectMinute = "0" + selectMinute;

            }
            switch (sort) {
                case "start":
                    mStartTime = selectHour + ":" + selectMinute;
                    tvStartTime.setText(selectHour + ":" + selectMinute);
                    tvStartTime.setTextSize(14);
                    break;
                case "end":
                    mEndTime = selectHour + ":" + selectMinute;
                    tvEndTime.setText(selectHour + ":" + selectMinute);
                    tvEndTime.setTextSize(14);
                    break;

            }


//            tvStartTime.setTextColor(ContextCompat.getColor
//                    (context, R.color.titleColor));
            dialog.dismiss();


        });

    }


}
