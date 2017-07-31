package org.eenie.wgj.ui.exchangeclass;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.exchangework.ExchangeWorkHistoryResponse;
import org.eenie.wgj.ui.message.GalleryActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/31 at 17:57
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeHistoryItemDetailActivity extends BaseActivity {
    public static final String INFO = "info";
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
    @BindView(R.id.tv_exchange_work_content)
    TextView tvExchangeContent;
    @BindView(R.id.tv_exchange_work_content_illustration)
    TextView tvExchangeIllustration;
    private ExchangeWorkHistoryResponse mData;


    @Override
    protected int getContentView() {
        return R.layout.activity_exchange_work_detail_item;
    }

    @Override
    protected void updateUI() {
        mData = getIntent().getParcelableExtra(INFO);
        if (mData != null) {
            tvClassName.setText(mData.getClassname());

            tvDate.setText(mData.getDate());

            if (mData.getImage().size() > 0) {
                Glide.with(context).load(mData.getImage().get(0).getImage()).centerCrop().into(imgFirst);

            }
            if (mData.getPeople().size() > 0) {
                String names = "";
                for (int i = 0; i < mData.getPeople().size(); i++) {
                    names = names + mData.getPeople().get(i).getName() + " ";

                }
                tvPeopleName.setText(names);

            }
            tvExchangeContent.setText(mData.getContent());
            tvExchangeIllustration.setText(mData.getInstruction());

            if (mData.getType() == 1) {
                tvClassType.setText("接班班次");
                tvPeopleType.setText("接班人员");
            }

        }

    }

    @OnClick({R.id.img_back, R.id.img_first})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.img_first:
                if (mData != null)
                    if (mData.getImage().size() > 0) {
                        startActivity(
                                new Intent(context, GalleryActivity.class).
                                        putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                                mData.getImage().
                                                        get(0).getImage()));
                    }


                break;
        }
    }
}
