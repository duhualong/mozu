package org.eenie.wgj.ui.project.reportpost;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.ReportPost;
import org.eenie.wgj.model.response.ClassMeetingList;
import org.eenie.wgj.model.response.PostWorkList;
import org.eenie.wgj.model.response.ReportPostList;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/25 at 14:27
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostAddActivity extends BaseActivity {
    public static final String PROJECT_ID="project_id";
    private ReportPostList data;
    private String projectId;
    @BindView(R.id.select_post)
    TextView selectPost;
    @BindView(R.id.select_time_space)
    TextView selectTimeSpace;
    @BindView(R.id.select_class)
    TextView selectClass;
    @BindView(R.id.recycler_add_time)
    RecyclerView mRecyclerView;
    private ArrayList<String> mList = new ArrayList<>();

    private KeyContactAdapter mAdapter;
    private PostAdapter postAdapter;
    private TimeAdapter timeApapter;
    private ExchangeWorkAdapter classAdapter;
    private List<PostWorkList> mPostWorkLists;
    private AlertDialog mDialog;
    private AlertDialog mDialogs;
    private AlertDialog dialog;

    private List<ClassMeetingList> mClassList;

    private String timeSpace;
    private int mId;
    private int mClassId;
    private int mPostId;


    @Override
    protected int getContentView() {
        return R.layout.activity_add_report_detail;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        getPostWorkingList();
        getClassList();
    }

    //班次
    private void getClassList() {
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getClassWideList(token, projectId)
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
                                mClassList = gson.fromJson(jsonArray,
                                        new TypeToken<List<ClassMeetingList>>() {
                                        }.getType());

                            }
                        }
                    }

                });
    }

    //岗位
    private void getPostWorkingList() {
        mSubscription = mRemoteService.getPostList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), projectId)
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
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            mPostWorkLists = gson.fromJson(jsonArray,
                                    new TypeToken<List<PostWorkList>>() {
                                    }.getType());

                        }

                    }
                });
    }

    @OnClick({R.id.img_back, R.id.rl_add_time, R.id.rl_select_post, R.id.rl_select_class,
            R.id.rl_select_time_space, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.rl_select_post:
                if (mPostWorkLists != null) {
                    showSelectPostDialog();
                } else {
                    Toast.makeText(context, "暂时没有可选择岗位，请去岗位设置中添加", Toast.LENGTH_LONG).
                            show();
                }
                break;
            case R.id.rl_select_class:
                if (mClassList != null) {
                    showSelectClassDialog();

                } else {
                    Toast.makeText(context, "暂时没有可选择班次，请去排班设置中添加", Toast.LENGTH_LONG).
                            show();
                }
                break;
            case R.id.rl_select_time_space:
                showTimeSpaceDialog();


                break;
            case R.id.rl_add_time:
                showTimeStartDialog();

                break;
            case R.id.tv_save:
                if (mList.size() > 0) {
                    addReportPost();
                } else {
                    Toast.makeText(context, "请添加报岗时间", Toast.LENGTH_LONG).show();
                }


                break;

        }
    }
    private void showSelectClassDialog() {
        View view = View.inflate(context, R.layout.dialog_select_post, null);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText("选择班次");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //自定义的布局文件
        mDialogs = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        mDialogs.show();
        RecyclerView recyclerPost = (RecyclerView) mDialogs.getWindow().findViewById(R.id.recycler_post_select);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerPost.setLayoutManager(layoutManager);
        classAdapter = new ExchangeWorkAdapter(context, mClassList);
        recyclerPost.setAdapter(classAdapter);
    }
    class ExchangeWorkAdapter extends RecyclerView.Adapter<ExchangeWorkAdapter.ExchangeWorkViewHolder> {
        private Context context;
        private List<ClassMeetingList> mClassMeetingLists;

        public ExchangeWorkAdapter(Context context, List<ClassMeetingList> mClassMeetingLists) {
            this.context = context;
            this.mClassMeetingLists = mClassMeetingLists;
        }

        @Override
        public ExchangeWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_select_post, parent, false);
            return new ExchangeWorkViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ExchangeWorkViewHolder holder, int position) {
            if (mClassMeetingLists != null && !mClassMeetingLists.isEmpty()) {
                ClassMeetingList data = mClassMeetingLists.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getServicesname())) {
                        holder.itemPost.setText(data.getServicesname());

                    }
                }


            }

        }

        @Override
        public int getItemCount() {
            return mClassMeetingLists.size();
        }

        public void addAll(List<ClassMeetingList> classMeetingLists) {
            this.mClassMeetingLists.addAll(classMeetingLists);
            ExchangeWorkAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mClassMeetingLists.clear();
            ExchangeWorkAdapter.this.notifyDataSetChanged();
        }

        class ExchangeWorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ClassMeetingList mClassMeetingList;
            private TextView itemPost;
            private RelativeLayout rlPost;

            public ExchangeWorkViewHolder(View itemView) {

                super(itemView);

                itemPost = ButterKnife.findById(itemView, R.id.item_post);
                rlPost = ButterKnife.findById(itemView, R.id.rl_key_personal);
                rlPost.setOnClickListener(this);

            }

            public void setItem(ClassMeetingList data) {
                mClassMeetingList = data;
            }

            @Override
            public void onClick(View v) {
                if (mDialogs != null) {
                    mDialogs.dismiss();
                    selectClass.setText(mClassMeetingList.getServicesname());
                    selectClass.setTextColor(ContextCompat.getColor
                            (context, R.color.titleColor));
                    mClassId = mClassMeetingList.getId();


                }

            }
        }
    }
    private void showSelectPostDialog() {
        View view = View.inflate(context, R.layout.dialog_select_post, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //自定义的布局文件
        mDialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        mDialog.show();
        RecyclerView recyclerPost = (RecyclerView) mDialog.getWindow().findViewById(R.id.recycler_post_select);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerPost.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(context, mPostWorkLists);
        recyclerPost.setAdapter(postAdapter);

    }
    class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
        private Context context;
        private List<PostWorkList> contactsList;

        public PostAdapter(Context context, List<PostWorkList> contactsList) {
            this.context = context;
            this.contactsList = contactsList;
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_select_post, parent, false);
            return new PostViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PostViewHolder holder, int position) {
            if (contactsList != null && !contactsList.isEmpty()) {
                PostWorkList data = contactsList.get(position);
                holder.setItem(data);
                if (data != null) {

                    if (data.getPost() != null && !TextUtils.isEmpty(data.getPost())) {
                        holder.itemPost.setText(data.getPost());
                    }

                }


            }

        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public void addAll(List<PostWorkList> contactsList) {
            this.contactsList.addAll(contactsList);
            PostAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.contactsList.clear();
            PostAdapter.this.notifyDataSetChanged();
        }

        class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView itemPost;

            private RelativeLayout rlPost;
            private PostWorkList mContactList;

            public PostViewHolder(View itemView) {

                super(itemView);
                itemPost = ButterKnife.findById(itemView, R.id.item_post);
                rlPost = ButterKnife.findById(itemView, R.id.rl_key_personal);
                rlPost.setOnClickListener(this);

            }

            public void setItem(PostWorkList data) {
                mContactList = data;
            }

            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    mDialog.dismiss();
                    selectPost.setText(mContactList.getPost());
                    selectPost.setTextColor(ContextCompat.getColor
                            (context, R.color.titleColor));
                    mPostId = mContactList.getId();

                }


            }
        }
    }

    private void addReportPost() {
        ReportPost reportPost = new ReportPost(timeSpace, mPostId + "", projectId, mClassId + "",
                com.alibaba.fastjson.JSON.toJSONString(mList));
        mSubscription = mRemoteService.editReportPostList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), reportPost)
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
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0){

                            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();

                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );
                        }else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

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
        timeApapter = new TimeAdapter(context, timeSpace);
        recyclerTime.setAdapter(timeApapter);

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
                    selectTimeSpace.setText(timeSpace);
                    selectTimeSpace.setTextColor(ContextCompat.getColor
                            (context, R.color.titleColor));

                }


            }
        }
    }

    //选择时间
    private void showTimeStartDialog() {
        View view = View.inflate(context, R.layout.dialog_alert_start_time, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        dialogTitle.setText("报岗时间");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        Button addHour = (Button) dialog.getWindow().findViewById(R.id.btn_add_hour);
        Button subHour = (Button) dialog.getWindow().findViewById(R.id.btn_subtract_hour);
        EditText editHour = (EditText) dialog.getWindow().findViewById(R.id.et_hour);
        Button addMinute = (Button) dialog.getWindow().findViewById(R.id.btn_add_minute);
        Button subMinute = (Button) dialog.getWindow().findViewById(R.id.btn_subtract_minute);
        EditText editMinute = (EditText) dialog.getWindow().findViewById(R.id.edit_minute);
        Button btnOk = (Button) dialog.getWindow().findViewById(R.id.btn_ok);


        addHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            if (TextUtils.isEmpty(hour)) {
                hour = "0";
            }

            if (Integer.parseInt(hour) >= 23) {
                Toast.makeText(context, "不能超过24小时", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(hour) >= 9) {
                    editHour.setText(String.valueOf(Integer.parseInt(hour) + 1));
                } else {
                    editHour.setText("0" + String.valueOf(Integer.parseInt(hour) + 1));
                }
            }
        });


        subHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            if (TextUtils.isEmpty(hour)) {
                hour = "0";
            }
            if (Integer.parseInt(hour) <= 0) {
                Toast.makeText(context, "不能小于0时", Toast.LENGTH_LONG).show();
            } else {
                if (Integer.parseInt(hour) >= 11) {
                    editHour.setText(String.valueOf(Integer.parseInt(hour) - 1));
                } else {
                    editHour.setText("0" + String.valueOf(Integer.parseInt(hour) - 1));
                }
            }

        });


        addMinute.setOnClickListener(v -> {
            String minute = editMinute.getText().toString();

            if (TextUtils.isEmpty(minute)) {
                minute = "0";
            }
            if (Integer.parseInt(minute) >= 59) {
                Toast.makeText(context, "不能超过60分钟", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(minute) >= 9) {
                    editMinute.setText(String.valueOf(Integer.parseInt(minute) + 1));
                } else {
                    editMinute.setText("0" + String.valueOf(Integer.parseInt(minute) + 1));
                }
            }
        });


        subMinute.setOnClickListener(v -> {
            String minute = editMinute.getText().toString();

            if (TextUtils.isEmpty(minute)) {
                minute = "0";
            }

            if (Integer.parseInt(minute) <= 0) {
                Toast.makeText(context, "不能少于0分钟", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(minute) >= 11) {
                    editMinute.setText(String.valueOf(Integer.parseInt(minute) - 1));
                } else {
                    editMinute.setText("0" + String.valueOf(Integer.parseInt(minute) - 1));
                }

            }

        });

        btnOk.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            String minute = editMinute.getText().toString();
            if (!TextUtils.isEmpty(hour) && !TextUtils.isEmpty(minute)) {
                if (Integer.parseInt(hour) >= 24 || Integer.parseInt(hour) < 0) {
                    Toast.makeText(context, "请设置正确的小时！", Toast.LENGTH_LONG).show();
                } else {
                    if (Integer.parseInt(minute) >= 60 || Integer.parseInt(minute) < 0) {
                        Toast.makeText(context, "请设置正确的分钟！", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        mList.add(hour + ":" + minute);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mAdapter = new KeyContactAdapter(context, mList);
                        mRecyclerView.setAdapter(mAdapter);

                    }
                }
            } else {
                Toast.makeText(context, "请设置报岗时间", Toast.LENGTH_LONG).show();
            }

        });
    }

    class KeyContactAdapter extends RecyclerView.Adapter<KeyContactAdapter.KeyContactViewHolder> {
        private Context context;
        private ArrayList<String> mStrings;

        public KeyContactAdapter(Context context, ArrayList<String> mStrings) {
            this.context = context;
            this.mStrings = mStrings;
        }

        @Override
        public KeyContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_report_detail, parent, false);
            return new KeyContactViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(KeyContactViewHolder holder, int position) {
            if (mStrings != null && !mStrings.isEmpty()) {
                String data = mStrings.get(position);
                holder.setItem(data);
                if (!TextUtils.isEmpty(data)) {
                    holder.itemTime.setText(data);
                }
                holder.imgDelete.setVisibility(View.VISIBLE);


            }

        }

        @Override
        public int getItemCount() {
            return mStrings.size();
        }

        public void addAll(ArrayList<String> contactsList) {
            this.mStrings.addAll(contactsList);
            KeyContactAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mStrings.clear();
            KeyContactAdapter.this.notifyDataSetChanged();
        }

        class KeyContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView itemTime;
            private RelativeLayout imgDelete;
            private String mString;

            public KeyContactViewHolder(View itemView) {
                super(itemView);
                itemTime = ButterKnife.findById(itemView, R.id.item_time);
                imgDelete = ButterKnife.findById(itemView, R.id.delete_img);
                imgDelete.setOnClickListener(this);

            }

            public void setItem(String data) {
                mString = data;
            }

            @Override
            public void onClick(View v) {
                mStrings.remove(mString);
                KeyContactAdapter.this.notifyDataSetChanged();


            }
        }
    }
}
