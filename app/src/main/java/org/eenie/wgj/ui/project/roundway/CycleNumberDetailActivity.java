package org.eenie.wgj.ui.project.roundway;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.CycleNumber;
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
 * Created by Eenie on 2017/5/26 at 14:13
 * Email: 472279981@qq.com
 * Des:
 */

public class CycleNumberDetailActivity extends BaseActivity {
    public static final String INFO="info";
    public static final String PROJECT_ID="project_id";
    public static final String LINE_ID="line";
    private CycleNumber data;
    private RoundWayAdapter mAdapter;

    private int mId;
    private String projectId;
    private  String lineId;

    @BindView(R.id.recycler_project_contacts)RecyclerView mRecyclerView;

    @Override
    protected int getContentView() {
        return R.layout.activity_cycle_round_edit;
    }

    @Override
    protected void updateUI() {
        data=getIntent().getParcelableExtra(INFO);
        projectId=getIntent().getStringExtra(PROJECT_ID);
         lineId=getIntent().getStringExtra(LINE_ID);
        initUI(data);


    }

    private void initUI(CycleNumber data) {
        ArrayList<CycleNumber.Info>mInfoBeen=new ArrayList<>();
        if (data!=null){
            mId=data.getInspectiondayid();

            if (data.getInfo().size()>0){
                for (int i=0;i<data.getInfo().size();i++){
                    mInfoBeen.add(data.getInfo().get(i));
                }

            }
        }

        if (mAdapter!=null){
            mAdapter.clear();

        }
        Log.d("数据：", "initUI: "+new Gson().toJson(data));
        mAdapter = new RoundWayAdapter(context, mInfoBeen);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.img_back,R.id.button_edit,R.id.button_delete})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.button_edit:
                Intent intent = new Intent(context, EditCycleRoundActivity.class);
                if (data != null) {
                    intent.putExtra(EditCycleRoundActivity.INFO, data);
                    intent.putExtra(EditCycleRoundActivity.PROJECT_ID, projectId);
                    intent.putExtra(EditCycleRoundActivity.LINE_ID,lineId);

                }
                startActivityForResult(intent, 1);

                break;
            case R.id.button_delete:
                //删除圈数
                deleteCycleNumber(mId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 6) {
          CycleNumber mdata= data.getParcelableExtra("info");
            getLineWay(mdata.getInspectiondayid());
//            if (!TextUtils.isEmpty(data.getStringExtra(lineId))){
//                lineId= data.getStringExtra("lineId");
//                System.out.println("lineId:"+lineId);
//
//            }else {
//                System.out.println("sbsbsbsbbb");
//            }

        }
    }


    private void getLineWay(int pointId) {
        mSubscription = mRemoteService.getCycleNumber(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""), projectId, Integer.parseInt(lineId))
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
                                List<CycleNumber> postWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<List<CycleNumber>>() {
                                        }.getType());
                                if (postWorkLists.size() > 0) {
                                    for (int i = 0; i < postWorkLists.size(); i++) {

                                        if (postWorkLists.get(i).getInfo() == null ||
                                                postWorkLists.get(i).getInfo().isEmpty()) {
                                            postWorkLists.remove(i);

                                        }

                                        if (postWorkLists.get(i).getInspectiondayid()==pointId){
                                            int finalI = i;
                                            new Thread() {
                                                public void run() {
                                                    runOnUiThread(() -> {
                                                        initUI(postWorkLists.get(finalI));
                                                    });
                                                }
                                            }.start();

                                        }
                                    }

                                }

                            }
                        }
                    }

                });
    }

    private void deleteCycleNumber(int id) {
        mSubscription=mRemoteService.deleteCycleNumber(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),id)
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
                        if (apiResponse.getResultCode()==0||apiResponse.getResultCode()==200){
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_LONG).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );


                        }

                    }
                });
    }


    class RoundWayAdapter extends RecyclerView.Adapter<RoundWayAdapter.RoundWayViewHolder> {
        private Context context;
        private ArrayList<CycleNumber.Info> contactsList;

        public RoundWayAdapter(Context context, ArrayList<CycleNumber.Info> contactsList) {
            this.context = context;
            this.contactsList = contactsList;
        }

        @Override
        public RoundWayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_cycle_round, parent, false);
            return new RoundWayViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RoundWayViewHolder holder, int position) {
            if (contactsList != null && !contactsList.isEmpty()) {
                CycleNumber.Info data = contactsList.get(position);
                holder.setItem(data);
                if (data != null) {
                    position = position + 1;
                  if (position<10){
                      holder.tvRound.setText("点位0" + position);
                  }else {
                      holder.tvRound.setText("点位" + position);
                  }

                    holder.tvRoundName.setText(data.getInspectionname());
                    holder.tvTime.setText(data.getInspectiontime());
                }

            }

        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public void addAll(ArrayList<CycleNumber.Info> contactsList) {
            this.contactsList.addAll(contactsList);
            RoundWayAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.contactsList.clear();
            RoundWayAdapter.this.notifyDataSetChanged();
        }

        class RoundWayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView tvRound;
            private TextView tvTime;
            private TextView tvRoundName;

            private CycleNumber.Info mCycleNumber;

            public RoundWayViewHolder(View itemView) {

                super(itemView);

                tvRound = ButterKnife.findById(itemView, R.id.item_post);

                tvRoundName = ButterKnife.findById(itemView, R.id.item_class);
                tvTime = ButterKnife.findById(itemView, R.id.item_round_time);


            }

            public void setItem(CycleNumber.Info data) {
                mCycleNumber = data;
            }

            @Override
            public void onClick(View v) {


                }


            }
        }

}
