package org.eenie.wgj.ui.takephoto;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.TakePhotoApiResponse;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.util.Constant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/11 at 15:06
 * Email: 472279981@qq.com
 * Des:
 */

public class TakePhotoDetailActivity extends BaseActivity {
    public static final String INFO = "info";
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    private TakePhotoApiResponse data;
    private String firstUrl;
    private String secondUrl;
    private String thirdUrl;

    @Override
    protected int getContentView() {
        return R.layout.activity_take_photo_detail;
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(INFO);
        if (data != null) {
            mTitle.setText(data.getTitle());
            mContent.setText(data.getText());
            mName.setText(data.getUsername());
            if (data.getImage() != null) {
                if (data.getImage().size()==1){
                    firstUrl = data.getImage().get(0).getImage();
                    Glide.with(context).load(Constant.DOMIN + firstUrl).
                            centerCrop().into(imgFirst);
                }else if (data.getImage().size()==2){
                    imgFirst.setVisibility(View.VISIBLE);
                    imgSecond.setVisibility(View.VISIBLE);
                    firstUrl = data.getImage().get(0).getImage();
                    secondUrl=data.getImage().get(1).getImage();
                    Glide.with(context).load(Constant.DOMIN + firstUrl).
                            centerCrop().into(imgFirst);
                    Glide.with(context).load(Constant.DOMIN + secondUrl).
                            centerCrop().into(imgSecond);

                }else if (data.getImage().size()>=3){
                    imgFirst.setVisibility(View.VISIBLE);
                    imgSecond.setVisibility(View.VISIBLE);
                    imgThird.setVisibility(View.VISIBLE);
                    firstUrl = data.getImage().get(0).getImage();
                    secondUrl=data.getImage().get(1).getImage();
                    thirdUrl=data.getImage().get(2).getImage();
                    Glide.with(context).load(Constant.DOMIN + firstUrl).
                            centerCrop().into(imgFirst);
                    Glide.with(context).load(Constant.DOMIN + secondUrl).
                            centerCrop().into(imgSecond);
                    Glide.with(context).load(Constant.DOMIN + thirdUrl).
                            centerCrop().into(imgThird);
                }

            }

        }

    }

    @OnClick({R.id.img_back, R.id.img_first, R.id.img_second, R.id.img_third})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.img_first:
                if (!TextUtils.isEmpty(firstUrl)){
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN+firstUrl));
                }

                break;

            case R.id.img_second:
                if (!TextUtils.isEmpty(secondUrl)) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + secondUrl));
                }
                break;

            case R.id.img_third:
                if (!TextUtils.isEmpty(thirdUrl)) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + thirdUrl));
                }
                break;
        }
    }
}
