package org.eenie.wgj.ui.takephoto;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.model.response.ProjectList;
import org.eenie.wgj.search.CharacterParser;
import org.eenie.wgj.search.ClearEditText;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/8/7 at 16:10
 * Email: 472279981@qq.com
 * Des:
 */

public class SelectProjectTakephotoActivity extends BaseActivity {
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String FIRST_URL = "one";
    public static final String SECOND_URL = "two";
    public static final String THIRD_URL = "three";
    private String content;
    private String title;
    private String firstUrl;
    private String secondUrl;
    private String thirdUrl;


    private ProjectAdapter mProjectAdapter;
    @BindView(R.id.recycler_project)
    RecyclerView mRecyclerView;

    @BindView(R.id.filter_edit)
    ClearEditText mClearEditText;
    private List<ProjectList> projectLists = new ArrayList<>();
    private CharacterParser characterParser;


    @Override
    protected int getContentView() {
        return R.layout.activity_take_photo_project_select;
    }

    @Override
    protected void updateUI() {
        title = getIntent().getStringExtra(TITLE);
        content = getIntent().getStringExtra(CONTENT);
        firstUrl = getIntent().getStringExtra(FIRST_URL);
        secondUrl = getIntent().getStringExtra(SECOND_URL);
        thirdUrl = getIntent().getStringExtra(THIRD_URL);

        mProjectAdapter = new ProjectAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mProjectAdapter);


    }

    @OnClick(R.id.img_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

        }
    }


    public void onRefreshs() {
        mProjectAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getProjectList(token)
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
                            projectLists = gson.fromJson(jsonArray,
                                    new TypeToken<List<ProjectList>>() {
                                    }.getType());

                            if (projectLists != null && !projectLists.isEmpty()) {
                                if (mProjectAdapter != null) {
                                    mProjectAdapter.updateListView(projectLists);
                                }
                            }
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }

    private void initFilter() {


        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void filterData(String inputContent) {
        characterParser = CharacterParser.getInstance();

        List<ProjectList> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(inputContent)) {
            filterDateList = projectLists;
        } else {
            filterDateList.clear();
            for (ProjectList sortModel : projectLists) {
                String name = sortModel.getProjectname();
                if (name.indexOf(inputContent) != -1 ||
                        characterParser.getSelling(name).startsWith(inputContent)) {
                    filterDateList.add(sortModel);
                }
            }
        }

        mProjectAdapter.updateListView(filterDateList);

    }


    @Override
    public void onResume() {
        super.onResume();
        onRefreshs();

        initFilter();

        initData();

    }


    private void initData() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 6) {
            Intent mIntent = new Intent();
            setResult(4, mIntent);
            finish();


        }

    }

    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private List<ProjectList> projectList;

        public ProjectAdapter(Context context, List<ProjectList> projectList) {
            this.context = context;
            this.projectList = projectList;
        }


        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_take_photo_project, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                ProjectList data = projectList.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.projectName.setText(data.getProjectname());


                    // holder.imgProject.setImageURI(data.get);
                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public void addAll(List<ProjectList> projectList) {
            this.projectList.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void updateListView(List<ProjectList> list) {
            this.projectList = list;
            notifyDataSetChanged();
        }

        public void clear() {
            this.projectList.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView projectName;
            private ProjectList mProjectList;
            private RelativeLayout rlProject;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                projectName = ButterKnife.findById(itemView, R.id.item_name);
                rlProject = ButterKnife.findById(itemView, R.id.rl_item);
                rlProject.setOnClickListener(this);

            }

            public void setItem(ProjectList projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.rl_item:
                        Intent intent = new Intent(context,
                                SelectReportPeopleActivity.class);
                        if (mProjectList != null) {
                            intent.putExtra(SelectReportPeopleActivity.PROJECT_ID,
                                    mProjectList.getId() + "");
                            intent.putExtra(SelectReportPeopleActivity.TITLE, title);
                            intent.putExtra(SelectReportPeopleActivity.CONTENT, content);
                            intent.putExtra(SelectReportPeopleActivity.FIRST_URL, firstUrl);
                            intent.putExtra(SelectReportPeopleActivity.SECOND_URL, secondUrl);
                            intent.putExtra(SelectReportPeopleActivity.THIRD_URL, thirdUrl);
                        }
                        startActivityForResult(intent, 2);

                        break;
                }


            }
        }
    }
}
