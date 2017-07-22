package org.eenie.wgj.ui.project.sortclass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.PersonalWorkDayMonthList;
import org.eenie.wgj.model.response.sortclass.AddPersonalSortClass;
import org.eenie.wgj.model.response.sortclass.ArrangeClassResponse;
import org.eenie.wgj.model.response.sortclass.ArrangeClassUserResponse;
import org.eenie.wgj.model.response.sortclass.ArrangeServiceTotal;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/8 at 14:54
 * Email: 472279981@qq.com
 * Des:
 */

public class AddPersonalClassActivity extends BaseActivity {
    public static final String INFO = "info";
    public static final String PROJECT_ID = "id";
    public static final String GROUND = "ground";
    public static final String DATE = "date";
    public static final String CLASS_ID="class_id";
    public static final String SUB_VALUE="sub";
    private String projectId;
    private ArrayList<ArrangeServiceTotal> data=new ArrayList<>();
    private ArrayList<PersonalWorkDayMonthList> personalData=new ArrayList<>();
    private String ground;
    private String date;
    private String serviceId;
    private int subValue;
    private  ArrayList<ArrangeClassResponse> arrangeDate=new ArrayList<>();
    private AddPersonalAdapter adapter;
    private ArrayList<AddPersonalSortClass> addData=new ArrayList<>();

    @BindView(R.id.recycler_personal)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_alert)
    TextView tvAlert;
    private Gson gson=new Gson();
    private   List<Integer>ids=new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.activity_select_personal;
    }

    @Override
    protected void updateUI() {
        adapter=new AddPersonalAdapter(context,new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        projectId = getIntent().getStringExtra(PROJECT_ID);
        data = getIntent().getParcelableArrayListExtra(INFO);
        if (data!=null){


            for (int i=0;i<data.size();i++){
                if (data.get(i).getUser()!=null&&!data.get(i).getUser().isEmpty()){

                    for (int j=0;j<data.get(i).getUser().size();j++){
                        ids.add(data.get(i).getUser().get(j).getUser_id());
                    }
                }



            }
        }
        Log.d("test", "updateUI: "+new Gson().toJson(data));
        System.out.println("data大小："+data.size());
        ground = getIntent().getStringExtra(GROUND);
        date = getIntent().getStringExtra(DATE);
        serviceId=getIntent().getStringExtra(CLASS_ID);
        subValue=Integer.valueOf(getIntent().getStringExtra(SUB_VALUE));
        getPersonalMonthDay(date);



    }

    private void getPersonalMonthDay(String date) {
        mSubscription = mRemoteService.getMonthDay(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), date, projectId)
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
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0) {
                            Log.d("ssss", "onNext: "+"ddd");
                            String jsonArray = gson.toJson(apiResponse.getData());

                            personalData = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<PersonalWorkDayMonthList>>() {
                                    }.getType());
                            Log.d("ssss", "data: "+gson.toJson(personalData));

                            if (personalData != null) {


                                    if (ids.size()>0){
                                        for (int t=0;t<ids.size();t++){
                                            for (int q=0;q<personalData.size();q++){
                                                if (String.valueOf(ids.get(t)).
                                                        equals(personalData.get(q).getUser_id())) {
                                                    personalData.remove(personalData.get(q));
                                                }
                                            }

                                        }

                                        if (personalData != null) {
                                            getSchedulingList(date, personalData);
                                        }

                                    }else {
                                        getSchedulingList(date, personalData);
                                    }
//                                    for (int p = 0; p < data.size(); p++) {
//                                        if (data.get(p).getUser() != null && !data.get(p).
//                                                getUser().isEmpty()) {
//                                            for (int n = 0; n < data.get(p).getUser().size(); n++) {
//
//                                                for (int q = 0; q < personalData.size(); q++) {
//                                                    if (String.valueOf(data.get(p).getUser().get(n).
//                                                            getUser_id()).
//                                                            equals(personalData.get(q).getUser_id())) {
//                                                        personalData.remove(personalData.get(q));
//                                                    }
//
//
//                                                }
//
//                                            }
//                                        }
//                                    }
//                                    Log.d("data", "personData: " + gson.toJson(personalData));
//
//                                    if (personalData != null) {
//                                        getSchedulingList(date, personalData);
//                                    }
//
//                                } else {
//                                    Log.d("data", "personData: " + gson.toJson(personalData));
//
//                                    if (personalData != null) {
//                                        getSchedulingList(date, personalData);
//                                    }

                            }
//                                    for (int m=0;m<personalData.size();m++){
//
//                                        for (int n=0;n<data.size();n++){
//                                            if (data.get(n).getUser()!=null&&
//                                                    !data.get(n).getUser().isEmpty()){
//                                                for (int l=0;l<data.get(n).getUser().size();l++){
//                                                    if (personalData.get(m).getUser_id().equals
//                                                            (String.valueOf(data.get(n).getUser().
//                                                                    get(l).getUser_id()))){
//                                                        personalData.remove(m);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                if (personalData!=null){
//                                    getSchedulingList(date,personalData);
//                                }



                            //}
                        }

                    }
                });
    }

    private void getSchedulingList(String date,ArrayList<PersonalWorkDayMonthList> monthLists) {
        mSubscription = mRemoteService.getArrangeClassList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), date, projectId)
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
                           arrangeDate = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<ArrangeClassResponse>>() {
                                    }.getType());
                            if (arrangeDate!=null){
                                for (int i=0;i<monthLists.size();i++){
                                    for (int q=0;q<monthLists.get(i).getInfo().size();q++){
                                       if (monthLists.get(i).getInfo().get(q).getService().getId()
                                               ==Integer.valueOf(serviceId)){
                                           int day=0;
                                           for (int j=0;j<arrangeDate.size();j++){

                                               for (int k=0;k<arrangeDate.get(j).getService().size();k++){
                                                   if (String.valueOf(arrangeDate.get(j).getService().
                                                           get(k).getId()).equals(serviceId)){
                                                       for (int p=0;p<arrangeDate.get(j).
                                                               getService().get(k).getUser().size();p++){
                                                           if (arrangeDate.get(j).getService().
                                                                   get(k).getUser().get(p).
                                                                   getUser_id()==Integer.valueOf(
                                                                           monthLists.get(i).
                                                                                   getUser_id())){
                                                               day=day+1;
                                                               monthLists.get(i).getInfo().get(q).
                                                                       setAddDay(day);
                                                           }

                                                       }


                                                   }
                                               }


                                           }

                                       }
                                    }

                                }

                                Log.d("编辑", "循环: "+gson.toJson(monthLists));
                                for (int r=0;r<monthLists.size();r++){
                                    for (int t=0;t<monthLists.get(r).getInfo().size();t++){
                                        if (monthLists.get(r).getInfo().get(t).getService().getId()
                                                ==Integer.valueOf(serviceId)){
                                            AddPersonalSortClass personal=new AddPersonalSortClass(
                                                    Integer.valueOf(monthLists.get(r).getUser_id()),
                                                    monthLists.get(r).getUser_name(),
                                                    monthLists.get(r).
                                                            getInfo().get(t).getAddDay(),
                                                    Integer.valueOf(monthLists.get(r).
                                                    getInfo().get(t).getDay()),
                                                    monthLists.get(r).getInfo().get(t).getService().getId());
                                            if (Integer.valueOf(monthLists.get(r).
                                                    getInfo().get(t).getDay())- monthLists.get(r).
                                                    getInfo().get(t).getAddDay()>0){
                                                addData.add(personal);
                                            }


                                        }
                                    }
                                }
                                Log.d("addData", "List: "+gson.toJson(addData));
//                                adapter=new AddPersonalAdapter(context,addData);
//                                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//                                mRecyclerView.setLayoutManager(layoutManager);
//                                mRecyclerView.setAdapter(adapter);
                                adapter.addAll(addData);
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.tv_apply_ok, R.id.img_back})
    public void onClick(View view) {
        int number=0;
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_apply_ok:
                if (addData!=null){
                    ArrayList<ArrangeClassUserResponse> mUser=new ArrayList<>();
                    for (int g=0;g<addData.size();g++){
                        if (addData.get(g).isChecked()){
                            number++;
                            ArrangeClassUserResponse user=new
                                    ArrangeClassUserResponse(addData.get(g).getUserId(),
                                    addData.get(g).getUserName());
                            mUser.add(user);
                        }
                    }
                    if (number<=0){
                        Toast.makeText(context,"请选择班次人员",Toast.LENGTH_SHORT).show();

                    }else {
                        if (number<=subValue){

                            Intent mIntent = new Intent();
                            mIntent.putParcelableArrayListExtra("user", mUser);
                            mIntent.putExtra("ground",ground);
                            // 设置结果，并进行传送
                            setResult(4, mIntent);
                            finish();
                        }else {
                            Toast.makeText(context,"最多只能选择"+subValue+"个班次人员",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }



                break;

        }
    }



    class AddPersonalAdapter extends RecyclerView.Adapter<AddPersonalAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<AddPersonalSortClass> addPersonalClass;

        public AddPersonalAdapter(Context context, ArrayList<AddPersonalSortClass> addPersonalClass) {
            this.context = context;
            this.addPersonalClass = addPersonalClass;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_select_personal, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (addPersonalClass != null && !addPersonalClass.isEmpty()) {
                AddPersonalSortClass data = addPersonalClass.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.personalName.setText(data.getUserName());
                    holder.tvNumber.setText(String.valueOf(data.getTotalDay()-data.getAddDay()));


                }

            }

        }

        @Override
        public int getItemCount() {
            return addPersonalClass.size();
        }

        public void addAll(ArrayList<AddPersonalSortClass> projectList) {
            this.addPersonalClass.addAll(projectList);
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.addPersonalClass.clear();
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private CheckBox checkBoxSelect;
            private TextView personalName;
            private TextView tvNumber;
            private AddPersonalSortClass mProjectList;
            private RelativeLayout rlItem;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                checkBoxSelect = ButterKnife.findById(itemView, R.id.checkbox_select);
                personalName = ButterKnife.findById(itemView, R.id.tv_personal_name);
                tvNumber = ButterKnife.findById(itemView, R.id.tv_number);
                rlItem=ButterKnife.findById(itemView,R.id.rl_item);
                checkBoxSelect.setClickable(false);
                rlItem.setOnClickListener(this);
            }

            public void setItem(AddPersonalSortClass projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
//                    case R.id.checkbox_select:
//                        if (checkBoxSelect.isChecked()){
//                            mProjectList.setChecked(true);
//
//                        }else {
//                            mProjectList.setChecked(false);
//
//                        }
//                        notifyDataSetChanged();
//
//
//                        break;
                    case R.id.rl_item:
                        if (checkBoxSelect.isChecked()){
                            checkBoxSelect.setChecked(false);
                            mProjectList.setChecked(false);
                        }else {
                            checkBoxSelect.setChecked(true);
                            mProjectList.setChecked(true);

                        }
                        notifyDataSetChanged();
                        break;

                }


            }
        }
    }



}
