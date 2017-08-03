package org.eenie.wgj.ui.exchangeclass;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.exchangework.ExchangeWorkHistoryTakeResponse;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.util.Constant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/31 at 17:57
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeHistoryItemDetailActivity extends BaseActivity {
    public static final String INFO = "info";
    public static final String TYPE = "type";
    @BindView(R.id.tv_class_type)
    TextView tvClassType;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_class_type_people)
    TextView tvPeopleType;
    @BindView(R.id.tv_class_people_name)
    TextView tvPeopleName;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    @BindView(R.id.tv_exchange_work_content)
    TextView tvExchangeContent;
    @BindView(R.id.tv_exchange_work_content_illustration)
    TextView tvExchangeIllustration;
    private ExchangeWorkHistoryTakeResponse mData;
    private String type;


    @Override
    protected int getContentView() {
        return R.layout.activity_exchange_work_detail_item;
    }

    @Override
    protected void updateUI() {
        mData = getIntent().getParcelableExtra(INFO);
        type = getIntent().getStringExtra(TYPE);



        if (mData != null) {
            tvClassName.setText(mData.getMattername());

            tvDate.setText(mData.getCreated_at());


            if (mData.getImage() != null) {

                if (mData.getImage().size() == 1) {
                    Glide.with(context).load(Constant.DOMIN+mData.getImage().get(0).getImage()).
                            centerCrop().into(imgFirst);

                } else if (mData.getImage().size() == 2) {
                    imgSecond.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constant.DOMIN+mData.getImage().get(0).getImage()).
                            centerCrop().into(imgFirst);
                    Glide.with(context).load(Constant.DOMIN+mData.getImage().get(1).getImage()).
                            centerCrop().into(imgSecond);


                } else if (mData.getImage().size() >= 3) {
                    imgSecond.setVisibility(View.VISIBLE);
                    imgThird.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constant.DOMIN+mData.getImage().get(0).getImage()).
                            centerCrop().into(imgFirst);
                    Glide.with(context).load(Constant.DOMIN+mData.getImage().get(1).getImage()).
                            centerCrop().into(imgSecond);

                    Glide.with(context).load(Constant.DOMIN+mData.getImage().get(2).getImage()).
                            centerCrop().into(imgThird);


                }

            }


            tvExchangeContent.setText(mData.getMatter());
            tvExchangeIllustration.setText(mData.getExplanation());
            if (!type.equals("1")) {
                tvClassType.setText("接班班次");
                tvPeopleType.setText("交班人员");
                if (mData.getFrom()!=null) {

                        tvPeopleName.setText(mData.getFrom().getName());
                    }


            }else {
                if (mData.getTo()!=null){
                    if (mData.getTo().size() > 0) {
                        String names = "";
                        for (int i = 0; i < mData.getTo().size(); i++) {
                            names = names + mData.getTo().get(i).getName() + " ";

                        }
                        tvPeopleName.setText(names);
                    }
                }

            }

        }

    }

    @OnClick({R.id.img_back, R.id.img_first,R.id.img_second,R.id.img_third})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_first:
                if (mData != null)
                    if (mData.getImage().size() >= 1) {
                        startActivity(
                                new Intent(context, GalleryActivity.class).
                                        putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                                Constant.DOMIN+mData.getImage().
                                                        get(0).getImage()));
                    }
                break;
            case R.id.img_second:
                if (mData.getImage().size() >= 2) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN+mData.getImage().
                                                    get(1).getImage()));
                }
                break;
            case R.id.img_third:
                if (mData.getImage().size() >= 3) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN+mData.getImage().
                                                    get(2).getImage()));
                }
                break;
        }
    }
}
