package org.eenie.wgj.ui.meeting.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.MeetingEndDetail;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.RxUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/12 at 16:22
 * Email: 472279981@qq.com
 * Des:
 */

public class SignInMeetingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String APPLY_ID = "id";
    private String id;
    @BindView(R.id.scanner_iv_barcode)
    ImageView barcodeHolder;
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private SignNumberAdapter mAdapter;
    @BindView(R.id.tv_sign_number)
    TextView tvSignNumber;

    @Override
    protected int getContentView() {
        return R.layout.activity_sign_in_meeting;
    }

    @Override
    protected void updateUI() {
        id = getIntent().getStringExtra(APPLY_ID);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new SignNumberAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

        if (!TextUtils.isEmpty(id)) {
            String encryptedData = "meet://checkin?meetId=" + id;
            Single.create((Single.OnSubscribe<Bitmap>) subscriber -> {
                try {
                    Bitmap bitmap = ImageUtils.getBarcodeBitmap(encryptedData, 450, 450);
                    subscriber.onSuccess(bitmap);
                } catch (WriterException e) {
                    subscriber.onError(e);
                }
            }).compose(RxUtils.applySchedulers()).subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();

                    Toast.makeText(SignInMeetingActivity.this,
                            "生成二维码失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(Bitmap bitmap) {

                    barcodeHolder.setImageBitmap(bitmap);
                }
            });
        }


    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }


    @Override
    public void onRefresh() {
        mAdapter.clear();
        mSubscription = mRemoteService.getMeetingArrangeDetail(mPrefsHelper.getPrefs().
                        getString(Constants.TOKEN, ""),
                id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            MeetingEndDetail mData =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<MeetingEndDetail>() {
                                            }.getType());

                            if (mData != null) {
                                if (mData.getNumber() != null && !mData.getNumber().isEmpty()) {
                                    if (mData.getNumber().size() > 0 && !TextUtils.isEmpty(mData.
                                            getNumber().get(0).getUsername())) {
                                        tvSignNumber.setText("签到人数(" +
                                                mData.getNumber().size() + "人)");
                                        if (mAdapter != null) {
                                            mAdapter.clear();
                                        }
                                        mAdapter.addAll(mData.getNumber());

                                    }
                                }
                            }
                        }
                    }
                });


    }

    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    class SignNumberAdapter extends RecyclerView.Adapter<SignNumberAdapter.ProjectViewHolder> {
        private Context context;
        private List<MeetingEndDetail.NumberBean> projectList;

        public SignNumberAdapter(Context context, List<MeetingEndDetail.NumberBean> projectList) {
            this.context = context;
            this.projectList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_sign_in_people, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                MeetingEndDetail.NumberBean data = projectList.get(position);

                if (data != null) {
                    if (!TextUtils.isEmpty(data.getUsername())) {
                        holder.tvSignIn.setText(data.getUsername());

                    }
                    if (data.getId_card_head_image()!=null&&!data.getId_card_head_image().isEmpty()){
                        if (!TextUtils.isEmpty(data.getId_card_head_image())) {
                            Glide.with(context).load(Constant.DOMIN + data.getId_card_head_image())
                                    .centerCrop().into(holder.imgSignin);
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

        public void addAll(List<MeetingEndDetail.NumberBean> projectList) {
            this.projectList.addAll(projectList);
            SignNumberAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectList.clear();
            SignNumberAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder {
            private CircleImageView imgSignin;
            private TextView tvSignIn;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                imgSignin = ButterKnife.findById(itemView, R.id.id_index_gallery_item_image);
                tvSignIn = ButterKnife.findById(itemView, R.id.id_index_gallery_item_text);

            }


        }
    }
}
