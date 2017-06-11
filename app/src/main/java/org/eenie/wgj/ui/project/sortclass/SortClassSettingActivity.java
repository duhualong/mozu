package org.eenie.wgj.ui.project.sortclass;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.MonthTimeWorkingArrange;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/5 at 9:41
 * Email: 472279981@qq.com
 * Des:
 */

public class SortClassSettingActivity extends BaseActivity {
    public static final String PROJECT_ID = "id";
    private static final String TAG = "SortClassSettingActivity";
    private Calendar mCalendar;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private String projectId;
    @BindView(R.id.rv_arrange_date)
    RecyclerView recyclerDate;
    @BindView(R.id.recycle_arrange_class)
    RecyclerView recyclerArrangeClass;
    @BindView(R.id.tv_date_arrange)
    TextView tvArrangeDate;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rl_select_date)
    RelativeLayout rlSelectDate;
    private MyAdapter adapter;
    private DateMyAdapter mDateAdapter;
    private ArrayList<MonthTimeWorkingArrange> mDayMonthTimes = new ArrayList<>();
    private String token;
    private Gson gson = new Gson();

    @Override
    protected int getContentView() {
        return R.layout.activity_arrange_class_setting;
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
                                            dateArrayList.get(a).getService().get(b).getService_people(), null);
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
                                if (adapter!=null){
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
                                    Log.d("mDayMonthTimes", "onItemClick: " +
                                            gson.toJson(mData.get(position)));

                                    new Thread() {
                                        public void run() {
                                            runOnUiThread(() -> {
                                                if (mData.get(position).getService() != null) {

                                                    tvArrangeDate.setText(mData.get(position).getDate());
                                                    tvTip.setText("班次及巡检线路安排");
                                                    mDateAdapter = new
                                                            DateMyAdapter(context,
                                                            mData.get(position).getService());
                                                    LinearLayoutManager layoutManager1 =
                                                            new LinearLayoutManager(
                                                                    context);
                                                    recyclerArrangeClass.setLayoutManager(layoutManager1);
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


                break;

            case R.id.btnPri:
                mCalendar.add(Calendar.MONTH, -1);
                if (mDateAdapter != null) {
                    mDateAdapter.clear();
                    tvArrangeDate.setText("");
                    tvTip.setText("");
                }
                if (adapter != null) {
                    adapter.clear();
                }
                onMonthChange(mCalendar.getTime());


                break;
            case R.id.btnNext:
                mCalendar.add(Calendar.MONTH, 1);
                if (mDateAdapter != null) {
                    mDateAdapter.clear();
                    tvArrangeDate.setText("");
                    tvTip.setText("");
                }
                if (adapter != null) {
                    adapter.clear();
                }
                onMonthChange(mCalendar.getTime());

                break;
        }
    }


    class DateMyAdapter extends RecyclerView.Adapter<DateMyAdapter.DateViewHolder> {
        private Context context;
        private ArrayList<ArrangeServiceTotal> mData;

        public DateMyAdapter(Context context, ArrayList<ArrangeServiceTotal> data) {
            this.context = context;
            mData = data;
        }

        @Override
        public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_arrange_list, parent, false);
            return new DateViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DateViewHolder holder, int position) {
            if (mData != null) {
                ArrangeServiceTotal data = mData.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (data.getUser() != null && !data.getUser().isEmpty()) {
                        holder.itemName.setText(data.getServiceName() +
                                "(" + data.getUser().size() + "/" + data.getServicePeople() + ")");
                        KeyContactAdapter keyContactAdapter=new KeyContactAdapter(context,data.getUser());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                        holder.itemPersonal.setLayoutManager(layoutManager);
                        holder.itemPersonal.setAdapter(keyContactAdapter);

                    } else {
                        holder.itemName.setText(data.getServiceName() +
                                "(" + "0" + "/" + data.getServicePeople() + ")");
                    }


                }

            }
//设置显示内容

        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void addAll(ArrayList<ArrangeServiceTotal> projectList) {
            this.mData.addAll(projectList);
            DateMyAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mData.clear();
            DateMyAdapter.this.notifyDataSetChanged();
        }

        class DateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView itemName;
            private ImageView imgAdd;
            private RecyclerView itemPersonal;
            private ArrangeServiceTotal mServiceBean;


            public DateViewHolder(View itemView) {

                super(itemView);
                itemName = ButterKnife.findById(itemView, R.id.item_arrange_name);
                imgAdd = ButterKnife.findById(itemView, R.id.img_add_contacts);
                itemPersonal = ButterKnife.findById(itemView, R.id.item_personal);
                imgAdd.setOnClickListener(this);

            }

            public void setItem(ArrangeServiceTotal mServiceBean) {
                mServiceBean = mServiceBean;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_add_contacts:


                        break;

                }


            }
        }
    }


    class KeyContactAdapter extends RecyclerView.Adapter<KeyContactAdapter.KeyContactViewHolder> {
        private Context context;
        private ArrayList<ArrangeClassUserResponse> mArrangeClassUserResponses;

        public KeyContactAdapter(Context context,
                                 ArrayList<ArrangeClassUserResponse> mArrangeClassUserResponses) {
            this.context = context;
            this.mArrangeClassUserResponses = mArrangeClassUserResponses;
        }

        @Override
        public KeyContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_arrange_personal, parent, false);
            return new KeyContactViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(KeyContactViewHolder holder, int position) {
            if (mArrangeClassUserResponses != null && !mArrangeClassUserResponses.isEmpty()) {
                ArrangeClassUserResponse data = mArrangeClassUserResponses.get(position);
                holder.setItem(data);
                if (data!=null){
                    holder.itemPersonalName.setText(data.getName());
                    if (data.getLine()!=null){
                        holder.tvAddRoundlLine.setText(data.getLine().getName());
                    }else {
                        holder.tvAddRoundlLine.setText(R.string.add_round_line);
                    }
                }


            }

        }

        @Override
        public int getItemCount() {
            return mArrangeClassUserResponses.size();
        }

        public void addAll(ArrayList<ArrangeClassUserResponse> contactsList) {
            this.mArrangeClassUserResponses.addAll(contactsList);
            KeyContactAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mArrangeClassUserResponses.clear();
            KeyContactAdapter.this.notifyDataSetChanged();
        }

        class KeyContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView itemPersonalName;
            private TextView tvAddRoundlLine;
            private ImageView imgDelete;

            private ArrangeClassUserResponse mArrangeClassUserResponse;

            public KeyContactViewHolder(View itemView) {
                super(itemView);
                itemPersonalName = ButterKnife.findById(itemView, R.id.item_personal_name);
                tvAddRoundlLine = ButterKnife.findById(itemView, R.id.tv_add_round_line);
                imgDelete = ButterKnife.findById(itemView, R.id.img_delete);
                imgDelete.setOnClickListener(this);

            }

            public void setItem(ArrangeClassUserResponse data) {
                mArrangeClassUserResponse = data;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.img_delete:
                        mArrangeClassUserResponses.remove(mArrangeClassUserResponse);
                        KeyContactAdapter.this.notifyDataSetChanged();

                        break;

                }



            }
        }
    }

}
