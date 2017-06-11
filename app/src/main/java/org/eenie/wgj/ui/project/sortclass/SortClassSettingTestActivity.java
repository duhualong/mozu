package org.eenie.wgj.ui.project.sortclass;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.AddArrangeClass;
import org.eenie.wgj.model.response.MonthTimeWorkingArrange;
import org.eenie.wgj.model.response.RoundWayList;
import org.eenie.wgj.model.response.sortclass.ArrangeClassResponse;
import org.eenie.wgj.model.response.sortclass.ArrangeClassUserResponse;
import org.eenie.wgj.model.response.sortclass.ArrangeProjectDate;
import org.eenie.wgj.model.response.sortclass.ArrangeProjectDateTotal;
import org.eenie.wgj.model.response.sortclass.ArrangeServiceTotal;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/5 at 9:41
 * Email: 472279981@qq.com
 * Des:
 */

public class SortClassSettingTestActivity extends BaseActivity {
    public static final String PROJECT_ID = "id";
    private static final String TAG = "SortClassSettingActivity";
    private Calendar mCalendar;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private String projectId;
    @BindView(R.id.rv_arrange_date)
    RecyclerView recyclerDate;
    @BindView(R.id.recycle_arrange_class)
    ExpandableListView recyclerArrangeClass;
    @BindView(R.id.tv_date_arrange)
    TextView tvArrangeDate;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rl_select_date)
    RelativeLayout rlSelectDate;
    private MyAdapter adapter;
    private TestExpandAdapter mDateAdapter;
    private ArrayList<MonthTimeWorkingArrange> mDayMonthTimes = new ArrayList<>();
    private String token;
    private Gson gson = new Gson();
    private ArrayList<ArrangeServiceTotal> mServiceList = new ArrayList<>();
    private String dateMonth;
    private String selectDate;

    @Override
    protected int getContentView() {
        return R.layout.activity_arrange_class_setting_test;
    }

    @Override
    protected void updateUI() {
        token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        projectId = getIntent().getStringExtra(PROJECT_ID);
        mCalendar = Calendar.getInstance();
        onMonthChange(mCalendar.getTime());

    }

    private void onMonthChange(Date time) {
        String date = new SimpleDateFormat("yyyy年MM月").format(time);
        String mDate = new SimpleDateFormat("yyyy-MM").format(time);
        dateMonth = mDate;
        tvDate.setText(date);

        getArrangeProjectDate(mDate);

    }

    private void getArrangeProjectDate(String date) {
        mSubscription = mRemoteService.getMonthDayTime(token, date, projectId)
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
                        if (apiResponse.getResultCode() == 0 || apiResponse.getResultCode() == 200) {
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ArrayList<ArrangeProjectDate> arrangeProjectDates = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<ArrangeProjectDate>>() {
                                    }.getType());
                            if (arrangeProjectDates != null) {
                                if (arrangeProjectDates.size() > 0 &&
                                        arrangeProjectDates.get(0).getService() != null) {

                                    Log.d("sss:", "onNext: " + gson.toJson(arrangeProjectDates));
                                    getArrangeDates(date, arrangeProjectDates);

                                } else {
                                    if (adapter != null) {
                                        adapter.clear();
                                    }
                                    if (mDateAdapter != null) {
                                        mDateAdapter.clear();
                                        tvArrangeDate.setText("");
                                        tvTip.setText("");
                                    }
                                }


                            } else {
                                Log.d("Test", "onNext: " + "sssss");
                            }


                        } else {
                            if (adapter != null) {
                                adapter.clear();
                            }
                        }

                    }
                });

    }

    private void getArrangeDates(String mDate, ArrayList<ArrangeProjectDate> dateArrayList) {
        mSubscription = mRemoteService.getArrangeClassList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), mDate, projectId)
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

                            String jsonArray = gson.toJson(apiResponse.getData());
                            ArrayList<ArrangeClassResponse> arrangeDate = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<ArrangeClassResponse>>() {
                                    }.getType());
                            ArrayList<ArrangeProjectDateTotal> mData = new ArrayList<>();
                            for (int a = 0; a < dateArrayList.size(); a++) {
                                ArrayList<ArrangeServiceTotal> mServiceList = new ArrayList<>();
                                for (int b = 0; b < dateArrayList.get(a).getService().size(); b++) {
                                    ArrangeServiceTotal arrangeServiceTotal = new ArrangeServiceTotal(
                                            dateArrayList.get(a).getService().get(b).getService_id(),
                                            dateArrayList.get(a).getService().get(b).getServicesname(),
                                            dateArrayList.get(a).getService().get(b).getService_people());
                                    mServiceList.add(arrangeServiceTotal);

                                }
                                ArrangeProjectDateTotal arrangeProjectDateTotal = new
                                        ArrangeProjectDateTotal(dateArrayList.get(a).getDate(),
                                        mServiceList);
                                mData.add(arrangeProjectDateTotal);
                            }

                            Log.d("list:", "onNext: " + gson.toJson(mData));
                            if (arrangeDate != null) {
                                for (int m = 0; m < mData.size(); m++) {
                                    for (int n = 0; n < arrangeDate.size(); n++) {
                                        if (arrangeDate.get(n).getDay().equals(mData.get(m).
                                                getDate())) {
                                            Log.d("date", "onNext: " + arrangeDate.get(n).getDay());
                                            for (int p = 0; p < mData.get(m).getService().size(); p++) {
                                                for (int q = 0; q < arrangeDate.get(n).getService().
                                                        size(); q++) {
                                                    if (mData.get(m).getService().get(p).
                                                            getServiceId() ==
                                                            arrangeDate.get(n).getService().
                                                                    get(q).getId()) {
                                                        mData.get(m).getService().get(p).setUser(
                                                                arrangeDate.get(n).getService().
                                                                        get(q).getUser());
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }

                                Log.d("Arraylist{}", "onNext: " + gson.toJson(mData) + "\n" + "size:" +
                                        mData.size());
                                if (adapter != null) {
                                    adapter.clear();
                                    tvArrangeDate.setText("");
                                    tvTip.setText("");
                                }

                                adapter = new MyAdapter(context, mData);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                recyclerDate.setLayoutManager(layoutManager);
                                recyclerDate.setAdapter(adapter);
                                adapter.setOnItemClickListener((view, position) -> {

                                    for (int i = 0; i < mData.size(); i++) {
                                        mData.get(i).setChecked(false);


                                    }
                                    mData.get(position).setChecked(true);
                                    adapter.notifyDataSetChanged();
                                    selectDate=mData.get(position).getDate();
                                    Log.d("mDayMonthTimes", "onItemClick: " +
                                            gson.toJson(mData.get(position)));

                                    new Thread() {
                                        public void run() {
                                            runOnUiThread(() -> {
                                                if (mData.get(position).getService() != null) {
                                                    tvArrangeDate.setText(mData.get(position).getDate());
                                                    tvTip.setText("班次及巡检线路安排");
                                                    mServiceList = mData.get(position).getService();
                                                    mDateAdapter = new
                                                            TestExpandAdapter(context,
                                                            mServiceList);
                                                    recyclerArrangeClass.setAdapter(mDateAdapter);
                                                }
                                            });

                                        }
                                    }.start();

                                });

                            }

                        }

                    }
                });
    }


    @OnClick({R.id.img_back, R.id.btn_save, R.id.btnPri, R.id.btnNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_save:
                //startActivity(new Intent(context,TestWebView.class));
                if (mServiceList!=null){
                    Log.d("data:", "onClick: "+gson.toJson(mServiceList));

                    ArrayList<AddArrangeClass.InfoArrange.RanksBean> rankList=new ArrayList<>();



                    for (int i=0;i<mServiceList.size();i++){
                        ArrayList <AddArrangeClass.InfoArrange.RanksBean.UserInfo> rankDataUserInfo=new ArrayList<>();
                        if (mServiceList.get(i).getUser()!=null){

                            for (int j=0;j<mServiceList.get(i).getUser().size();j++){

                                if ( mServiceList.get(i).getUser().get(j).getLine()!=null){
                                    AddArrangeClass.InfoArrange.RanksBean.UserInfo   userInfo =new
                                            AddArrangeClass.InfoArrange.RanksBean.UserInfo(
                                            mServiceList.get(i).getUser().get(j).getUser_id(),
                                            mServiceList.get(i).getUser().get(j).getName(),
                                            mServiceList.get(i).getUser().get(j).getLine().getId());
                                    rankDataUserInfo.add(userInfo);
                                }else {
                                    AddArrangeClass.InfoArrange.RanksBean.UserInfo   userInfo =new
                                            AddArrangeClass.InfoArrange.RanksBean.UserInfo(
                                            mServiceList.get(i).getUser().get(j).getUser_id(),
                                            mServiceList.get(i).getUser().get(j).getName(),0);
                                    rankDataUserInfo.add(userInfo);
                                }

                            }
                        }

                        AddArrangeClass.InfoArrange.RanksBean mRankBean=new
                                AddArrangeClass.InfoArrange.RanksBean(mServiceList.get(i).
                                getServiceName(),mServiceList.get(i).getServiceId(),rankDataUserInfo);
                        rankList.add(mRankBean);

                        }
                    AddArrangeClass.InfoArrange info=new AddArrangeClass.InfoArrange(selectDate,rankList);
                    AddArrangeClass addData=new AddArrangeClass(projectId,gson.toJson(info));

                    addDateSelect(addData);
                    }

                break;

            case R.id.btnPri:
                mCalendar.add(Calendar.MONTH, -1);
                if (mDateAdapter != null) {
                    mDateAdapter.clear();
                }
                tvArrangeDate.setText("");
                tvTip.setText("");

                if (adapter != null) {
                    adapter.clear();
                }
                onMonthChange(mCalendar.getTime());


                break;
            case R.id.btnNext:
                mCalendar.add(Calendar.MONTH, 1);
                if (mDateAdapter != null) {
                    mDateAdapter.clear();
                }
                tvArrangeDate.setText("");
                tvTip.setText("");

                if (adapter != null) {
                    adapter.clear();
                }
                onMonthChange(mCalendar.getTime());

                break;
        }


}
    private void addDateSelect(AddArrangeClass data) {
        mSubscription=mRemoteService.addArrangeClassItem(token,data)
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
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            getArrangeProjectDate(selectDate);
                            Toast.makeText(context,"添加成功",Toast.LENGTH_LONG).show();
                            tvArrangeDate.setText(selectDate);
                            tvTip.setText("班次及巡检线路安排");

                        }else {
                            Toast.makeText(context,apiResponse.
                                    getResultMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            RoundWayList mRound = data.getParcelableExtra("info");
            String ground = data.getStringExtra("ground");
            String position = data.getStringExtra("position");
            Log.d("test", "onActivityResult: " + "ssss");
            if (mRound!=null){
                mServiceList.get(Integer.parseInt(ground)).getUser().get(Integer.parseInt(position))
                        .setLine(new ArrangeClassUserResponse.LineBean(mRound.getId(), mRound.getName()));

            }else {
                mServiceList.get(Integer.parseInt(ground)).getUser().get(Integer.parseInt(position))
                        .setLine(null);
            }
            mDateAdapter.notifyDataSetChanged();


        }
        if (requestCode==3&&resultCode==4){
            ArrayList<ArrangeClassUserResponse> addPersonalSortClasses=data.
                    getParcelableArrayListExtra("user");
            String ground=data.getStringExtra("ground");
            Log.d("user", "onActivityResult: "+gson.toJson(addPersonalSortClasses));
            ArrayList<ArrangeClassUserResponse> userResponses=
                    mServiceList.get(Integer.valueOf(ground)).getUser();
            Log.d("muser[] ","onActivityResult: "+gson.toJson(userResponses));
            if (userResponses!=null) {
                if (addPersonalSortClasses != null) {
                    for (int i = 0; i < addPersonalSortClasses.size(); i++) {
                        if (addPersonalSortClasses.get(i) != null) {
                            userResponses.add(addPersonalSortClasses.get(i));
                        }
                    }
                    mServiceList.get(Integer.parseInt(ground)).setUser(userResponses);
                   // mDateAdapter.notifyDataSetChanged();
                }
            }else {
                if (addPersonalSortClasses != null) {
                    mServiceList.get(Integer.parseInt(ground)).setUser(addPersonalSortClasses);
//                    mDateAdapter.notifyDataSetChanged();
                }

            }
            mDateAdapter.notifyDataSetChanged();
            Log.d("muserList[] ","onActivityResult: "+gson.toJson(userResponses));


        }
    }

    class TestExpandAdapter extends BaseExpandableListAdapter {
        private Context context;

        private ArrayList<ArrangeServiceTotal> mData;


        public TestExpandAdapter(Context context, ArrayList<ArrangeServiceTotal> data) {
            this.context = context;
            mData = data;
        }

        public void clear() {
            this.mData.clear();
            TestExpandAdapter.this.notifyDataSetChanged();
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder gvh;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_arrange_list_test, null);
                gvh = new GroupViewHolder();
                gvh.groupText = (TextView) convertView.findViewById(R.id.item_arrange_name);
                gvh.imgAdd = (ImageView) convertView.findViewById(R.id.img_add_contacts);
                convertView.setTag(gvh);
            } else {
                gvh = (GroupViewHolder) convertView.getTag();
            }
            int subValue ;
            if (mData.get(groupPosition).getUser() != null && !mData.get(groupPosition).getUser().isEmpty()) {
                gvh.groupText.setText(mData.get(groupPosition).getServiceName() +
                        "(" + mData.get(groupPosition).getUser().size()
                        + "/" + mData.get(groupPosition).getServicePeople() + ")");
                subValue = Integer.parseInt(mData.get(groupPosition).getServicePeople()) -
                        mData.get(groupPosition).getUser().size();
            } else {
                gvh.groupText.setText(mData.get(groupPosition).getServiceName() +
                        "(" + "0"
                        + "/" + mData.get(groupPosition).getServicePeople() + ")");
                subValue = Integer.parseInt(mData.get(groupPosition).getServicePeople());
            }


                gvh.imgAdd.setOnClickListener(v -> {
                    if (subValue > 0) {
                        Intent intent = new Intent(SortClassSettingTestActivity.this,
                                AddPersonalClassActivity.class);
                        intent.putExtra(AddPersonalClassActivity.GROUND, groupPosition + "");
                        intent.putParcelableArrayListExtra(AddPersonalClassActivity.INFO, mData);
                        intent.putExtra(AddPersonalClassActivity.PROJECT_ID, projectId);
                        intent.putExtra(AddPersonalClassActivity.DATE, dateMonth);
                        intent.putExtra(AddPersonalClassActivity.CLASS_ID, mData.get(groupPosition).
                                getServiceId() + "");
                        intent.putExtra(AddPersonalClassActivity.SUB_VALUE, String.valueOf(subValue));
                        startActivityForResult(intent, 3);
                    }else {
                        Toast.makeText(context,"班次人数已满",Toast.LENGTH_SHORT).show();
                    }



                    //  notifyDataSetChanged();

                });



            return convertView;
        }

        public class GroupViewHolder {
            TextView groupText;
            ImageView imgAdd;
        }

        @Override
        public int getGroupCount() {
            return mData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (mData.get(groupPosition).getUser() != null && !mData.get(groupPosition).getUser().isEmpty()) {
                return mData.get(groupPosition).getUser().size();
            } else {
                return 0;
            }

        }

        @Override
        public Object getGroup(int groupPosition) {
            return mData.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mData.get(groupPosition).getUser().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            ItemViewHolder ivh;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_arrange_personal, null);
                ivh = new ItemViewHolder();
                ivh.itemPersonalName = (TextView) convertView.findViewById(R.id.item_personal_name);
                ivh.tvAddLine = (TextView) convertView.findViewById(R.id.tv_add_round_line);
                ivh.imgDelete = (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(ivh);
            } else {
                ivh = (ItemViewHolder) convertView.getTag();
            }

            ivh.itemPersonalName.setText(mData.get(groupPosition).getUser().
                    get(childPosition).getName());
            if (mData.get(groupPosition).getUser().
                    get(childPosition).getLine()!=null) {

                    ivh.tvAddLine.setText(mData.get(groupPosition).getUser().
                            get(childPosition).getLine().getName());
                    ivh.tvAddLine.setTextColor(ContextCompat.getColor
                            (context, R.color.titleColor));
//                }else {
//                    ivh.tvAddLine.setText(R.string.add_round_line);
//                }

            } else {
                ivh.tvAddLine.setText(R.string.add_round_line);

            }
            ivh.imgDelete.setOnClickListener(v -> {
                mData.get(groupPosition).getUser().remove(childPosition);
                notifyDataSetChanged();
            });

            ivh.tvAddLine.setOnClickListener(v -> {
                Intent intent = new Intent(SortClassSettingTestActivity.this, SelectLineActivity.class);
                intent.putExtra(SelectLineActivity.PROJECT_ID, projectId);
                intent.putExtra(SelectLineActivity.GROUND, groupPosition + "");
                intent.putExtra(SelectLineActivity.POSITION, childPosition + "");
                startActivityForResult(intent, 1);
            });


            return convertView;
        }

        public class ItemViewHolder {
            private TextView itemPersonalName;
            private TextView tvAddLine;
            private ImageView imgDelete;

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

//    private void showLineDialog() {
//        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
//        TextView dialogTitle = (TextView) view.findViewById(R.id.title_dialog);
//        EditText etDialog = (EditText) view.findViewById(R.id.et_input_content);
//        etDialog.setHint("请输入姓名");
//        dialogTitle.setText("设置姓名");
//        if (!TextUtils.isEmpty(mName)) {
//            etDialog.setText(mName);
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        final AlertDialog dialog = builder
//                .setView(view) //自定义的布局文件
//                .setCancelable(true)
//                .create();
//        dialog.show();
//        dialog.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//        dialog.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
//            if (!TextUtils.isEmpty(etDialog.getText().toString())) {
//                dialog.dismiss();
//                Snackbar.make(rootView, "姓名设置成功！", Snackbar.LENGTH_SHORT).show();
//                mName = etDialog.getText().toString();
//                tvName.setText(mName);
//                tvName.setTextColor(ContextCompat.getColor
//                        (context, R.color.titleColor));
//            } else {
//                Toast.makeText(context, "请输入姓名", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//    }

}
