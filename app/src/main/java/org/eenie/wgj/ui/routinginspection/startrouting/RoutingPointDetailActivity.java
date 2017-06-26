package org.eenie.wgj.ui.routinginspection.startrouting;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.routing.StartRoutingResponse;
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
    public static final String TYPE="type";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.routing_time)TextView tvRoutingTime;
    @BindView(R.id.routing_address)TextView tvRoutingAddress;
    @BindView(R.id.routing_content)TextView tvRoutingContent;
    @BindView(R.id.img_first)ImageView imgFirst;
    @BindView(R.id.img_second)ImageView imgSecond;
    @BindView(R.id.img_third)ImageView imgThird;

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
        type=getIntent().getStringExtra(TYPE);
        if (!TextUtils.isEmpty(position)){
            tvTitle.setText("点位"+position);
        }
        if (data!=null){
            tvRoutingAddress.setText("巡检地点："+data.getInspectionname());
            tvRoutingContent.setText(data.getInspectioncontent());

            switch (type){
                case "1":
                    tvRoutingTime.setText("巡检时间："+data.getInspectiontime());
                    if (data.getImage()!=null){
                        if (data.getImage().size()==1){
                            Glide.with(context).load(Constant.DOMIN+data.
                                    getImage().get(0).getImage()).centerCrop()
                                    .into(imgFirst);
                        }else if (data.getImage().size()==2){
                            Glide.with(context).load(Constant.DOMIN+data.
                                    getImage().get(0).getImage()).centerCrop()
                                    .into(imgFirst);
                            Glide.with(context).load(Constant.DOMIN+data.
                                    getImage().get(1).getImage()).centerCrop()
                                    .into(imgSecond);
                        }else if (data.getImage().size()>=3){
                            Glide.with(context).load(Constant.DOMIN+data.
                                    getImage().get(0).getImage()).centerCrop()
                                    .into(imgFirst);
                            Glide.with(context).load(Constant.DOMIN+data.
                                    getImage().get(1).getImage()).centerCrop()
                                    .into(imgSecond);
                            Glide.with(context).load(Constant.DOMIN+data.
                                    getImage().get(2).getImage()).centerCrop()
                                    .into(imgThird);
                        }
                    }

                    break;
                case "2":
                    if (data.getInspection()!=null&&!TextUtils.isEmpty(data.getInspection().getTime())){
                        tvRoutingTime.setText("巡检时间："+data.getInspection().getTime());
                        if (data.getInspection().getImage()!=null){
                            if (data.getInspection().getImage().size()==1){
                                Glide.with(context).load(Constant.DOMIN+data.getInspection().getImage()
                                        .get(0).getImage()).centerCrop()
                                        .into(imgFirst);
                            }else if (data.getInspection().getImage().size()==2){
                                Glide.with(context).load(Constant.DOMIN+data.getInspection().getImage()
                                        .get(0).getImage()).centerCrop()
                                        .into(imgFirst);
                                Glide.with(context).load(Constant.DOMIN+data.getInspection().getImage()
                                        .get(1).getImage()).centerCrop()
                                        .into(imgSecond);
                            }else if (data.getInspection().getImage().size()>=3){
                                Glide.with(context).load(Constant.DOMIN+data.getInspection().getImage()
                                        .get(0).getImage()).centerCrop()
                                        .into(imgFirst);
                                Glide.with(context).load(Constant.DOMIN+data.getInspection().getImage()
                                        .get(1).getImage()).centerCrop()
                                        .into(imgSecond);
                                Glide.with(context).load(Constant.DOMIN+data.getInspection().getImage()
                                        .get(2).getImage()).centerCrop()
                                        .into(imgThird);
                            }
                        }

                    }

                    break;
            }

        }

    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }

    }
}
