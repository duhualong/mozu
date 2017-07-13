package org.eenie.wgj.ui.takephoto;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.AddRoundRequest;
import org.eenie.wgj.model.response.AddReportNumber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.eenie.wgj.R.id.checkbox_select_all;

/**
 * Created by Eenie on 2017/6/11 at 16:20
 * Email: 472279981@qq.com
 * Des:
 */

public class SelectReportPeopleActivity extends BaseActivity {
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String FIRST_URL = "first_url";
    public static final String SECOND_URL = "second_url";
    public static final String THIRD_URL = "third_url";
    private String title;
    private String content;
    private String firstUrl;
    private String secondUrl;
    private String thirdUrl;
    private ArrayList<File> mFiles = new ArrayList<>();
    @BindView(checkbox_select_all)
    CheckBox checkboxSelectAll;
    @BindView(R.id.expend_list_report)
    ExpandableListView mExpandableListView;
    private Gson gson = new Gson();
    private SelectAdapter adapter;
    private ArrayList<AddReportNumber> addReportNumber = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.activity_select_report_people;
    }

    @Override
    protected void updateUI() {
        getData();
        title = getIntent().getStringExtra(TITLE);
        content = getIntent().getStringExtra(CONTENT);
        firstUrl = getIntent().getStringExtra(FIRST_URL);
        secondUrl = getIntent().getStringExtra(SECOND_URL);
        thirdUrl = getIntent().getStringExtra(THIRD_URL);
        if (!TextUtils.isEmpty(firstUrl)) {
            mFiles.add(new File(firstUrl));
        }
        if (!TextUtils.isEmpty(secondUrl)) {
            mFiles.add(new File(secondUrl));
        }
        if (!TextUtils.isEmpty(thirdUrl)) {
            mFiles.add(new File(thirdUrl));
        }
        mExpandableListView.setOnChildClickListener(adapter);
    }

    //获取汇报成员
    private void getData() {
        mSubscription = mRemoteService.getShootNumber(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""))
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
                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                String jsonArray = gson.toJson(apiResponse.getData());
                                addReportNumber =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<AddReportNumber>>() {
                                                }.getType());
                                if (addReportNumber != null) {
                                    for (int i = 0; i < addReportNumber.size(); i++) {
                                        addReportNumber.get(i).setCheckReport(false);
                                        for (int j = 0; j < addReportNumber.get(i).getInfo().size(); j++) {
                                            addReportNumber.get(i).getInfo().get(j).setCheckInfo(false);
                                        }
                                    }
                                    adapter = new SelectAdapter(context, addReportNumber);
                                    mExpandableListView.setAdapter(adapter);

                                }
                           }
                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void addData(RequestBody body, String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        Call<ApiResponse> call = userBiz.addShootPhotoItem(token,body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getResultCode()==200){
                    showRegisterDialog();
                }else {
                    Toast.makeText(context,response.body().getResultMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
    private void showRegisterDialog() {
        View view = View.inflate(context, R.layout.dialog_success_take_photo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {

            Intent mIntent = new Intent();
                setResult(4,mIntent);
            Single.just("").delay(1, TimeUnit.SECONDS).
                    compose(RxUtils.applySchedulers()).
                    subscribe(s ->
                            finish()
                    );


        });
    }
    public static MultipartBody getMultipartBody(ArrayList<File> files, String title,
                                                 String content,ArrayList<Integer>ids){
        AddRoundRequest request=new AddRoundRequest(title,content,ids);
        Gson gson=new Gson();


        MultipartBody.Builder builder=new MultipartBody.Builder();
        for (int i=0;i<files.size();i++){
            RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),files.get(i));
            builder.addFormDataPart("image[]",files.get(i).getName(),requestBody);

        }
        builder.addFormDataPart("data",gson.toJson(request));

        builder.setType(MultipartBody.FORM);
        return builder.build();

    }
    @OnClick({R.id.img_back, R.id.tv_apply_ok, R.id.checkbox_select_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.tv_apply_ok:
                ArrayList<Integer>arrayList=new ArrayList<>();
                if (addReportNumber!=null){
                    for (int i=0;i<addReportNumber.size();i++){
                        for (int j=0;j<addReportNumber.get(i).getInfo().size();j++){
                            if (addReportNumber.get(i).getInfo().get(j).isCheckInfo()){
                                arrayList.add(addReportNumber.get(i).getInfo().get(j).getUser_id());
                            }

                        }
                    }
                    Log.d("test", "onClick: "+gson.toJson(addReportNumber));
                    Log.d("list", "onClick: "+gson.toJson(arrayList));

                    if (arrayList.size()>=1){
                        addData( getMultipartBody(mFiles,title,content,arrayList),
                                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""));


                    }else {
                        Toast.makeText(context,"请选择部门成员！",Toast.LENGTH_SHORT).show();

                    }

                }


                break;

            case R.id.checkbox_select_all:
                if (checkboxSelectAll.isChecked()) {
                    if (addReportNumber != null) {
                        for (int i = 0; i < addReportNumber.size(); i++) {
                            addReportNumber.get(i).setCheckReport(true);
                            for (int j = 0; j < addReportNumber.get(i).getInfo().size(); j++) {
                                addReportNumber.get(i).getInfo().get(j).setCheckInfo(true);
                            }
                        }
                        Log.d("呵呵", "数组数据: " + gson.toJson(addReportNumber));
                    }
                } else {
                    if (addReportNumber != null) {
                        for (int i = 0; i < addReportNumber.size(); i++) {
                            addReportNumber.get(i).setCheckReport(false);
                            for (int j = 0; j < addReportNumber.get(i).getInfo().size(); j++) {
                                addReportNumber.get(i).getInfo().get(j).setCheckInfo(false);
                            }
                        }
                        Log.d("呵呵", "数组数据: " + gson.toJson(addReportNumber));
                    }

                }
                adapter.notifyDataSetChanged();


                break;
        }
    }

//    class ExpandAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener{
//        private Context context;
//
//        private ArrayList<AddReportNumber> mData;
//
//
//        public ExpandAdapter(Context context, ArrayList<AddReportNumber> data) {
//            this.context = context;
//            mData = data;
//        }
//
//        @Override
//        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//            GroupViewHolder gvh;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(context).inflate(R.layout.item_expend_list_view_report, null);
//                gvh = new GroupViewHolder();
//                gvh.groupText = (TextView) convertView.findViewById(R.id.item_report_department);
//                gvh.imgExpend = (ImageView) convertView.findViewById(R.id.item_img_expend);
//                gvh.itemCheckAll = (CheckBox) convertView.findViewById(R.id.item_checkbox_select_all);
//                convertView.setTag(gvh);
//
//            } else {
//                gvh = (GroupViewHolder) convertView.getTag();
//            }
//            gvh.groupText.setText(mData.get(groupPosition).getName());
//            if (mData.get(groupPosition).isCheckReport()) {
//                gvh.itemCheckAll.setChecked(true);
//                for (int i = 0; i < mData.get(groupPosition).getInfo().size(); i++) {
//                    mData.get(groupPosition).getInfo().get(i).setCheckInfo(true);
//                }
//
//
//            } else {
//                gvh.itemCheckAll.setChecked(false);
//                for (int i = 0; i < mData.get(groupPosition).getInfo().size(); i++) {
//                    mData.get(groupPosition).getInfo().get(i).setCheckInfo(false);
//                }
//
//
//            }
//            // 點擊 CheckBox 時，將狀態存起來
//            gvh.itemCheckAll.setOnClickListener(new GroupCheckBoxClick(groupPosition));
//
//            if (isExpanded) {
//
//                gvh.imgExpend.setImageResource(R.mipmap.ic_expand);
//            } else {
//
//                gvh.imgExpend.setImageResource(R.mipmap.ic_collapse);
//            }
//            return convertView;
//        }
//
//        @Override
//        public boolean onChildClick(ExpandableListView parent, View v,
//                                    int groupPosition, int childPosition, long id) {
//            handleClick(childPosition,groupPosition);
//            return true;
//        }
//
//        class GroupCheckBoxClick implements View.OnClickListener {
//            private int groupPosition;
//
//            GroupCheckBoxClick(int groupPosition) {
//                this.groupPosition = groupPosition;
//            }
//
//            public void onClick(View v) {
//                mData.get(groupPosition).toggle();
//                // 將 Children 的 isChecked 全面設成跟 Group 一樣
//                int childrenCount = mData.get(groupPosition).getInfo().size();
//                boolean groupIsChecked = mData.get(groupPosition).isCheckReport();
//                for (int i = 0; i < childrenCount; i++)
//                    mData.get(groupPosition).getInfo().get(i).setCheckInfo(groupIsChecked);
//
//                notifyDataSetChanged();
//            }
//        }
//
//        public class GroupViewHolder {
//            TextView groupText;
//            ImageView imgExpend;
//            CheckBox itemCheckAll;
//        }
//
//
//        @Override
//        public int getGroupCount() {
//            return mData.size();
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
//            return mData.get(groupPosition).getInfo().size();
//        }
//
//        @Override
//        public Object getGroup(int groupPosition) {
//            return mData.get(groupPosition);
//        }
//
//        @Override
//        public Object getChild(int groupPosition, int childPosition) {
//            return mData.get(groupPosition).getInfo().get(childPosition);
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return true;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
//                                 View convertView, ViewGroup parent) {
//            ItemViewHolder ivh;
//
//            if (convertView == null) {
//                convertView = LayoutInflater.from(context).inflate(R.layout.item_expend_item_report, null);
//                ivh = new ItemViewHolder();
//                ivh.itemText = (TextView) convertView.findViewById(R.id.item_report_name);
//                ivh.itemCheckBox = (CheckBox) convertView.findViewById(R.id.item_checkbox_select_item);
//                convertView.setTag(ivh);
//            } else {
//                ivh = (ItemViewHolder) convertView.getTag();
//            }
//            ivh.itemText.setText(mData.get(groupPosition).getInfo().
//                    get(childPosition).getName());
//            if (mData.get(groupPosition).getInfo().get(childPosition).isCheckInfo()) {
//                ivh.itemCheckBox.setChecked(true);
//            } else {
//                ivh.itemCheckBox.setChecked(false);
//            }
//            ivh.itemCheckBox.setOnClickListener(new ChildCheckBoxClick(groupPosition,childPosition));
//
//            return convertView;
//        }
//
//
//
//        /** 勾選 Child CheckBox 時，存 Child CheckBox 的狀態 */
//        class ChildCheckBoxClick implements View.OnClickListener {
//            private int groupPosition;
//            private int childPosition;
//
//            ChildCheckBoxClick(int groupPosition, int childPosition) {
//                this.groupPosition = groupPosition;
//                this.childPosition = childPosition;
//            }
//
//            public void onClick(View v) {
//                handleClick(childPosition, groupPosition);
//            }
//        }
//
//        public void handleClick(int childPosition, int groupPosition) {
//            mData.get(groupPosition).getInfo().get(childPosition).toggle();
//
//            // 檢查 Child CheckBox 是否有全部勾選，以控制 Group CheckBox
//            int childrenCount = mData.get(groupPosition).getInfo().size();
//            boolean childrenAllIsChecked = true;
//            for (int i = 0; i < childrenCount; i++) {
//                if (!mData.get(groupPosition).getInfo().get(i).isCheckInfo())
//                    childrenAllIsChecked = false;
//            }
//
//            mData.get(groupPosition).setCheckReport(childrenAllIsChecked);
//            notifyDataSetChanged();
//        }
//
//        public class ItemViewHolder {
//            TextView itemText;
//            CheckBox itemCheckBox;
//
//        }
//
//
//    }
}
