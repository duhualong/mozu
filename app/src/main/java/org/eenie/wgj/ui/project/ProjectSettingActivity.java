package org.eenie.wgj.ui.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.model.response.ProjectList;
import org.eenie.wgj.search.CharacterParser;
import org.eenie.wgj.search.ClearEditText;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.PermissionManager;

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
 * Created by Eenie on 2017/5/16 at 16:07
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectSettingActivity extends BaseActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 0x101;
    @BindView(R.id.root_view)
    View rootView;

    private ProjectAdapter mProjectAdapter;
    @BindView(R.id.recycler_project)
    RecyclerView mRecyclerView;
    private boolean permission;
    @BindView(R.id.filter_edit)ClearEditText mClearEditText;
    private List<ProjectList> projectLists=new ArrayList<>();
    private CharacterParser characterParser;



    @Override
    protected int getContentView() {
        return R.layout.activity_project_setting_new;
    }

    @Override
    protected void updateUI() {

        mProjectAdapter = new ProjectAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mProjectAdapter);
        checkPermission();


    }

    @OnClick({R.id.img_back, R.id.tv_new_rebuild})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_new_rebuild:
                if (permission){
                    startActivity(new Intent(context, NewBuildProjectActivity.class));
                }else {
                    Toast.makeText(context,"请提供拍照和读取文件的权限",Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }


    /**
     * 针对高版本系统检查权限
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPermission = PermissionManager.checkCameraPermission(context)
                    && PermissionManager.checkWriteExternalStoragePermission(context);
            if (hasPermission) {
                permission=true;


            } else {
                showRequestPermissionDialog();
            }
        } else {
          permission=true;

        }
    }


    /**
     * 请求权限Snackbar
     */
    private void showRequestPermissionDialog() {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Snackbar.make(rootView, "请提供摄像头及文件权限，以拍摄和预览相机图片!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v -> {
                        PermissionManager.requestCamera(ProjectSettingActivity.this, REQUEST_CAMERA_PERMISSION);
                    }).show();

        } else {
            PermissionManager.requestCamera(ProjectSettingActivity.this, REQUEST_CAMERA_PERMISSION);
        }
    }


    /**
     * 权限申请回调
     *
     * @param requestCode  请求码
     * @param permissions  请求权限
     * @param grantResults 请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:

                boolean isCanCapturePhoto = true;

                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        isCanCapturePhoto = false;
                    }
                }
                if (isCanCapturePhoto) {
                   permission=true;
                } else {
                    Snackbar.make(rootView, "请完整的权限，以预览裁剪图片!", Snackbar.LENGTH_SHORT).show();
                }

                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
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
            View itemView = inflater.inflate(R.layout.item_project, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                ProjectList data = projectList.get(position);
                holder.setItem(data);
                if (data != null) {
                    switch (position%4){
                        case 0:
                            holder.projectName.setTextColor(ContextCompat.getColor
                                    (context, R.color.text_blue));
                            break;
                        case 1:
                            holder.projectName.setTextColor(ContextCompat.getColor
                                    (context, R.color.text_green));
                            break;
                        case 2:
                            holder.projectName.setTextColor(ContextCompat.getColor
                                    (context, R.color.deep_orange));
                            break;
                        case 3:
                            holder.projectName.setTextColor(ContextCompat.getColor
                                    (context, R.color.amber));
                            break;
                    }
                    if (data.getProjectname()!=null&&!TextUtils.isEmpty(data.getProjectname())){
                        holder.projectName.setText(data.getProjectname());
                        holder.projectName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    if (data.getProject_logo()!=null&&!TextUtils.isEmpty(data.getProject_logo())){
                       String logo=data.getProject_logo();
                        if (logo.startsWith("/images")){
                            Glide.with(context).load(
                                    Constant.DOMIN + data.getProject_logo()).
                                    centerCrop().into(holder.imgProject);
                        }


                    }

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
            private ImageView imgProject;
            private TextView projectName;
            private ImageView projectEdit;
            private ProjectList mProjectList;
            private RelativeLayout rlProject;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                imgProject = ButterKnife.findById(itemView, R.id.item_project_img);
                projectName = ButterKnife.findById(itemView, R.id.item_project_name);
                projectEdit = ButterKnife.findById(itemView, R.id.item_project_edit);
                rlProject=ButterKnife.findById(itemView,R.id.rl_item_project);
                projectEdit.setOnClickListener(this);
                rlProject.setOnClickListener(this);

            }

            public void setItem(ProjectList projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.item_project_edit:
                    Intent intents= new Intent(ProjectSettingActivity.this,
                                ProjectEditActivity.class);

                        if (mProjectList!=null){
                            intents.putExtra(ProjectEditActivity.PROJECT_ID,
                                    mProjectList.getId()+"");
                            intents.putExtra(ProjectEditActivity.PROJECT_NAME,
                                    mProjectList.getProjectname());
                            if (mProjectList.getProject_logo()!=null&&
                                    !TextUtils.isEmpty(mProjectList.getProject_logo())){
                                intents.putExtra(ProjectEditActivity.PROJECT_LOGO,
                                        mProjectList.getProject_logo());
                            }


                        }
                        startActivity(intents);
                        break;
                    case R.id.rl_item_project:
                        Intent intent=new Intent(context,
                                ProjectItemSettingActivity.class);
                        if (mProjectList!=null){
                            System.out.println("项目id："+mProjectList.getId()+"名字："+mProjectList.getProjectname());
                            intent.putExtra(ProjectItemSettingActivity.PROJECT_ID,
                                    mProjectList.getId()+"");
                            intent.putExtra(ProjectItemSettingActivity.PROJECT_NAME,
                                    mProjectList.getProjectname());

                        }
                        startActivity(intent);
                        break;
                }


            }
        }
    }

}
