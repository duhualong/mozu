package org.eenie.wgj.ui.routinginspection.record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.response.RecordRoutingResponse;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eenie on 2017/6/29 at 13:39
 * Email: 472279981@qq.com
 * Des:
 */

public class NormalRoutingRecordFragment extends BaseSupportFragment {
    public static final String INFO = "info";
    private NormalRoutingAdapter mAdapter;
    @BindView(R.id.rv_routing_normal)
    RecyclerView mRecyclerView;
    private RecordRoutingResponse.InfoBean mInfoBean;
    private List<RecordRoutingResponse.InspectionBean> mInspectionBeanList = new ArrayList<>();
    private List<Integer> mIntegers = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.fragment_routing_normal;
    }

    @Override
    protected void updateUI() {

        if (mInfoBean != null) {
            initData(mInfoBean);
        }


    }

    private void initData(RecordRoutingResponse.InfoBean data) {
        mInspectionBeanList = new ArrayList<>();
        mIntegers = new ArrayList<>();
        for (int i = 0; i < data.getInspection().size(); i++) {
            if (data.getInspection().get(i).getSituation() == 1) {
                mInspectionBeanList.add(data.getInspection().get(i));
                mIntegers.add(i + 1);

            }

        }

        mAdapter = new NormalRoutingAdapter(context, mInspectionBeanList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mInspectionBeanList != null || mIntegers != null) {
            mIntegers = new ArrayList<>();
            mInspectionBeanList = new ArrayList<>();
        }
    }

    public static NormalRoutingRecordFragment newInstance(RecordRoutingResponse.InfoBean data) {

        NormalRoutingRecordFragment fragment = new NormalRoutingRecordFragment();
        if (data != null) {
            Bundle args = new Bundle();
            args.putParcelable(INFO, data);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mInfoBean = getArguments().getParcelable(INFO);

        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    class NormalRoutingAdapter extends RecyclerView.Adapter<NormalRoutingAdapter.ProjectViewHolder> {
        private Context context;
        private List<RecordRoutingResponse.InspectionBean> mInspectionBeanList;


        public NormalRoutingAdapter(Context context, List<RecordRoutingResponse.InspectionBean>
                mInspectionBeanList) {
            this.context = context;
            this.mInspectionBeanList = mInspectionBeanList;

        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_routing_normal_fragment, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mInspectionBeanList != null && !mInspectionBeanList.isEmpty()) {


                holder.setItem(mInspectionBeanList.get(position));
                RecordRoutingResponse.InspectionBean data = mInspectionBeanList.get(position);
                holder.setItem(data);

                if (mIntegers != null) {
                    if (mIntegers.get(position) <= 9) {
                        holder.routingPosition.setText("点位0" + mIntegers.get(position));

                    } else {
                        holder.routingPosition.setText("点位" + mIntegers.get(position));
                    }
                }

                holder.itemRoutingTime.setText("巡检时间：" + data.getTime());
                holder.itemRoutingAddress.setText("巡检地点：" + data.getInspectionname());
                if (data.getImages() != null) {
                    holder.mLinearLayout.setVisibility(View.VISIBLE);
                    if (data.getImages().size() == 1) {
                        Glide.with(context).load(Constant.DOMIN + data.getImages().get(0).getImage())
                                .centerCrop().into(holder.imgFirst);
                    } else if (data.getImages().size() == 2) {
                        Glide.with(context).load(Constant.DOMIN + data.getImages().get(0).getImage())
                                .centerCrop().into(holder.imgFirst);
                        Glide.with(context).load(Constant.DOMIN + data.getImages().get(1).getImage())
                                .centerCrop().into(holder.imgSecond);
                    } else if (data.getImages().size() >= 3) {
                        Glide.with(context).load(Constant.DOMIN + data.getImages().get(0).getImage())
                                .centerCrop().into(holder.imgFirst);
                        Glide.with(context).load(Constant.DOMIN + data.getImages().get(1).getImage())
                                .centerCrop().into(holder.imgSecond);
                        Glide.with(context).load(Constant.DOMIN + data.getImages().get(2).getImage())
                                .centerCrop().into(holder.imgThird);

                    }
                } else {
                    holder.mLinearLayout.setVisibility(View.GONE);
                }


            }

        }

        @Override
        public int getItemCount() {
            return mInspectionBeanList.size();
        }

        public void addAll(List<RecordRoutingResponse.InspectionBean> projectList) {
            this.mInspectionBeanList.addAll(projectList);
            NormalRoutingAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mInspectionBeanList.clear();
            NormalRoutingAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private RecordRoutingResponse.InspectionBean mRoutingReponse;
            private LinearLayout mLinearLayout;

            private TextView routingPosition;
            private TextView itemRoutingTime;
            private TextView itemRoutingStatus;
            private TextView itemRoutingAddress;
            private ImageView imgFirst;
            private ImageView imgSecond;
            private ImageView imgThird;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                routingPosition = ButterKnife.findById(itemView, R.id.item_routing_position);
                itemRoutingTime = ButterKnife.findById(itemView, R.id.item_routing_time);
                itemRoutingStatus = ButterKnife.findById(itemView, R.id.item_routing_status);
                itemRoutingAddress = ButterKnife.findById(itemView, R.id.item_routing_address);
                mLinearLayout = ButterKnife.findById(itemView, R.id.line_normal_img);
                imgFirst = ButterKnife.findById(itemView, R.id.img_first);
                imgSecond = ButterKnife.findById(itemView, R.id.img_second);
                imgThird = ButterKnife.findById(itemView, R.id.img_third);
                imgFirst.setOnClickListener(this);
                imgSecond.setOnClickListener(this);
                imgThird.setOnClickListener(this);
            }

            public void setItem(RecordRoutingResponse.InspectionBean projectList) {
                mRoutingReponse = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_first:
                        if (mRoutingReponse.getImages() != null && mRoutingReponse.getImages().size() >= 1) {
                            startActivity(
                                    new Intent(context, GalleryActivity.class).
                                            putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                                    Constant.DOMIN + mRoutingReponse.getImages().
                                                            get(0).getImage()));
                        }

                        break;
                    case R.id.img_second:
                        if (mRoutingReponse.getImages() != null && mRoutingReponse.getImages().size() >= 2) {
                            startActivity(
                                    new Intent(context, GalleryActivity.class).
                                            putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                                    Constant.DOMIN + mRoutingReponse.getImages().
                                                            get(1).getImage()));
                        }

                        break;
                    case R.id.img_third:

                        if (mRoutingReponse.getImages() != null && mRoutingReponse.getImages().size() >= 3) {
                            startActivity(
                                    new Intent(context, GalleryActivity.class).
                                            putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                                    Constant.DOMIN + mRoutingReponse.getImages().
                                                            get(2).getImage()));
                        }
                        break;


                }

            }


        }
    }

}
