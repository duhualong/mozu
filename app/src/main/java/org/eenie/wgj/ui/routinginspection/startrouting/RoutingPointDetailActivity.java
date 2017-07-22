package org.eenie.wgj.ui.routinginspection.startrouting;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.routing.StartRoutingResponse;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.util.Constant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/26 at 14:48
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingPointDetailActivity extends BaseActivity {
    public static final String INFO = "info";
    public static final String POSITION = "position";
    public static final String TYPE = "type";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.routing_time)
    TextView tvRoutingTime;
    @BindView(R.id.routing_address)
    TextView tvRoutingAddress;
    @BindView(R.id.routing_content)
    TextView tvRoutingContent;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    @BindView(R.id.line_abnormal_detail)
    LinearLayout mLineAbnormal;
    @BindView(R.id.abnormal_number)
    TextView abnormalNumber;
    @BindView(R.id.routing_abnormal_content)
    TextView abnormalContent;
    @BindView(R.id.img_one)
    ImageView imgOne;
    @BindView(R.id.img_two)
    ImageView imgTwo;
    @BindView(R.id.img_three)
    ImageView imgThree;

    private String position;
    private StartRoutingResponse.InfoBean data;
    private String type;

    @Override
    protected int getContentView() {
        return R.layout.activity_routing_point_detail;
    }

    @Override
    protected void updateUI() {
        position = getIntent().getStringExtra(POSITION);
        data = getIntent().getParcelableExtra(INFO);
        type = getIntent().getStringExtra(TYPE);
        if (!TextUtils.isEmpty(position)) {
            tvTitle.setText("点位" + position);
        }
        if (data != null) {

            tvRoutingAddress.setText("巡检地点：" + data.getInspectionname());
            tvRoutingContent.setText(data.getInspectioncontent());
            Log.d("testData[]", "updateUI: "+new Gson().toJson(data));
            switch (type) {

                case "1":
                    tvRoutingTime.setText("巡检时间：" + data.getInspectiontime());
                    if (data.getImage() != null) {
                        if (data.getImage().size() == 1) {
                            Glide.with(context).load(Constant.DOMIN + data.
                                    getImage().get(0).getImage()).centerCrop()
                                    .into(imgFirst);
                        } else if (data.getImage().size() == 2) {
                            Glide.with(context).load(Constant.DOMIN + data.
                                    getImage().get(0).getImage()).centerCrop()
                                    .into(imgFirst);
                            Glide.with(context).load(Constant.DOMIN + data.
                                    getImage().get(1).getImage()).centerCrop()
                                    .into(imgSecond);
                        } else if (data.getImage().size() >= 3) {
                            Glide.with(context).load(Constant.DOMIN + data.
                                    getImage().get(0).getImage()).centerCrop()
                                    .into(imgFirst);
                            Glide.with(context).load(Constant.DOMIN + data.
                                    getImage().get(1).getImage()).centerCrop()
                                    .into(imgSecond);
                            Glide.with(context).load(Constant.DOMIN + data.
                                    getImage().get(2).getImage()).centerCrop()
                                    .into(imgThird);
                        }
                    }

                    break;
                case "2":
                    if (data.getInspection() != null && !TextUtils.isEmpty(data.getInspection().getTime())) {
                        tvRoutingTime.setText("巡检时间：" + data.getInspection().getTime());
                        if (data.getInspection().getImage() != null) {
                            if (data.getInspection().getImage().size() == 1) {
                                Glide.with(context).load(Constant.DOMIN + data.getInspection().getImage()
                                        .get(0).getImage()).centerCrop()
                                        .into(imgFirst);
                            } else if (data.getInspection().getImage().size() == 2) {
                                Glide.with(context).load(Constant.DOMIN + data.getInspection().getImage()
                                        .get(0).getImage()).centerCrop()
                                        .into(imgFirst);
                                Glide.with(context).load(Constant.DOMIN + data.getInspection().getImage()
                                        .get(1).getImage()).centerCrop()
                                        .into(imgSecond);
                            } else if (data.getInspection().getImage().size() >= 3) {
                                Glide.with(context).load(Constant.DOMIN + data.getInspection().getImage()
                                        .get(0).getImage()).centerCrop()
                                        .into(imgFirst);
                                Glide.with(context).load(Constant.DOMIN + data.getInspection().getImage()
                                        .get(1).getImage()).centerCrop()
                                        .into(imgSecond);
                                Glide.with(context).load(Constant.DOMIN + data.getInspection().getImage()
                                        .get(2).getImage()).centerCrop()
                                        .into(imgThird);
                            }
                        }
                        if (data.getInspection().getWarranty() != null &&!TextUtils.isEmpty(data.
                                getInspection().getWarranty().getUniqueid())) {
                            mLineAbnormal.setVisibility(View.VISIBLE);

                            if (data.getInspection().getWarranty().getUniqueid()!= null){
                                abnormalNumber.setText("异常单号："+data.getInspection().getWarranty().getUniqueid());
                            }
                            if (data.getInspection().getWarranty().getContent()!=null){
                                abnormalContent.setText(data.getInspection().getWarranty().getContent());
                            }
                            if (data.getInspection().getWarranty().getImage() != null) {
                                if (data.getInspection().getWarranty().getImage().size() == 1) {
                                    Glide.with(context).load(Constant.DOMIN + data.getInspection().getWarranty().getImage()
                                            .get(0).getImage()).centerCrop()
                                            .into(imgOne);
                                } else if (data.getInspection().getWarranty().getImage().size() == 2) {
                                    Glide.with(context).load(Constant.DOMIN + data.getInspection().getWarranty().getImage()
                                            .get(0).getImage()).centerCrop()
                                            .into(imgOne);
                                    Glide.with(context).load(Constant.DOMIN + data.getInspection().getWarranty().getImage()
                                            .get(1).getImage()).centerCrop()
                                            .into(imgTwo);
                                } else if (data.getInspection().getWarranty().getImage().size() >= 3) {
                                    Glide.with(context).load(Constant.DOMIN + data.getInspection().getWarranty().getImage()
                                            .get(0).getImage()).centerCrop()
                                            .into(imgOne);
                                    Glide.with(context).load(Constant.DOMIN + data.getInspection().getWarranty().getImage()
                                            .get(1).getImage()).centerCrop()
                                            .into(imgTwo);
                                    Glide.with(context).load(Constant.DOMIN + data.getInspection().getWarranty().getImage()
                                            .get(2).getImage()).centerCrop()
                                            .into(imgThree);
                                }
                            }
                        }else {
                            mLineAbnormal.setVisibility(View.INVISIBLE);
                            System.out.println("line");

                        }

                    }


                    break;
            }

        }

    }

    @OnClick({R.id.img_back,R.id.img_first,R.id.img_second,R.id.img_third,R.id.img_one,
    R.id.img_two,R.id.img_three})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_first:
                if (data.getImage()!=null){
                    if (data.getImage().size()>=1){
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getImage().get(0).getImage()));
                    }

                }


                break;

            case R.id.img_second:
                if (data.getImage()!=null){
                    if (data.getImage().size()>=2){
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getImage().get(1).getImage()));
                    }

                }

                break;
            case R.id.img_third:
                if (data.getImage()!=null){
                    if (data.getImage().size()>=3){
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getImage().get(2).getImage()));
                    }

                }

                break;
            case R.id.img_one:
                if (data.getInspection().getWarranty().getImage()!=null){
                    if (data.getInspection().getWarranty().getImage().size()>=1){
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getInspection().
                                                getWarranty().getImage().get(0).getImage()));
                    }

                }

                break;
            case R.id.img_two:
                if (data.getInspection().getWarranty().getImage()!=null){
                    if (data.getInspection().getWarranty().getImage().size()>=2){
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getInspection().
                                                getWarranty().getImage().get(1).getImage()));
                    }

                }

                break;
            case R.id.img_three:
                if (data.getInspection().getWarranty().getImage()!=null){
                    if (data.getInspection().getWarranty().getImage().size()>=3){
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getInspection().
                                                getWarranty().getImage().get(2).getImage()));
                    }
                }
                break;
        }

    }
}
