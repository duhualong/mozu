package org.eenie.wgj.ui.project.reportpost;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
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
 * Created by Eenie on 2017/5/24 at 15:17
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostDetailActivity extends BaseActivity {
    @BindView(R.id.select_post)
    TextView selectPost;
    @BindView(R.id.select_time_space)
    TextView selectTimneSpace;
    @BindView(R.id.select_class)
    TextView selectClass;
    @BindView(R.id.recycler_report_post)RecyclerView mRecyclerView;


    public static final String INFO = "info";
    public static final String PROJECT_ID = "project_id";
    private ReportPostList data;
    private String projectId;
    private List<String> time=new ArrayList<>();
    private KeyContactAdapter mAdapter;
    private int id;


    @Override
    protected int getContentView() {
        return R.layout.activity_report_post_detail;
    }

    @Override
    protected void updateUI() {


        data = getIntent().getParcelableExtra(INFO);
        projectId = getIntent().getStringExtra(PROJECT_ID);
        initUI(data);



    }

    private void initUI(ReportPostList data) {
        if (data != null) {
            id=data.getId();
            time = data.getReportingtime();
            selectPost.setText(data.getPostsetting().getPost());
            selectTimneSpace.setText(data.getJetlag());
            selectClass.setText(data.getService().getServicesname());


            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter=new KeyContactAdapter(context,time);
            mRecyclerView.setAdapter(mAdapter);

        }
    }

    @OnClick({R.id.img_back,R.id.button_delete,R.id.button_edit})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.button_delete:
                //删除报岗设置
                deletePost(id);


                break;
            case R.id.button_edit:
                Intent intent=new Intent(context,ReportPostEditActivity.class);
                if (data!=null){
                    intent.putExtra(ReportPostEditActivity.INFO,data);
                    if (!TextUtils.isEmpty(projectId)){
                        intent.putExtra(ReportPostEditActivity.PROJECT_ID,projectId);
                    }
                }
                startActivityForResult(intent, 1);



                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 4) {
            ReportPostList mData = data.getParcelableExtra("report_post");
            initUI(mData);

            }
    }

    private void deletePost(int id) {
        mSubscription=mRemoteService.deleteReportPostItem(
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
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );
                        }

                    }
                });
    }

    class KeyContactAdapter extends RecyclerView.Adapter<KeyContactAdapter.KeyContactViewHolder> {
        private Context context;
        private List<String> mStrings;

        public KeyContactAdapter(Context context, List<String> mStrings) {
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
            private String mString;

            public KeyContactViewHolder(View itemView) {

                super(itemView);

                itemTime = ButterKnife.findById(itemView, R.id.item_time);


            }

            public void setItem(String data) {
                mString = data;
            }

            @Override
            public void onClick(View v) {


            }
        }
    }





}
