package org.eenie.wgj.ui.message;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.requset.AbnormalMessage;
import org.eenie.wgj.util.Constant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/5 at 15:23
 * Email: 472279981@qq.com
 * Des:
 */

public class AbnormalDetailActivity extends BaseActivity {
    public static final String INFO="info";
    private AbnormalMessage data;
    @BindView(R.id.tv_title)TextView title;
    @BindView(R.id.tv_content)TextView content;
    @BindView(R.id.tv_founder)TextView founder;
    @BindView(R.id.img_first)ImageView firstImg;
    @BindView(R.id.img_second)ImageView secondImg;
    @BindView(R.id.img_third)ImageView thirdImg;
    @Override
    protected int getContentView() {
        return R.layout.activity_abnormal_handle_detail;
    }

    @Override
    protected void updateUI() {
       data= getIntent().getParcelableExtra(INFO);
        if (data!=null){
            if (!TextUtils.isEmpty(data.getTitle())&&!TextUtils.isEmpty(data.getText())&&
                    !TextUtils.isEmpty(data.getUsername())){
                title.setText(data.getTitle());
                content.setText(data.getText());
                founder.setText(data.getUsername());
            }
            System.out.println("imgView："+data.getImage().get(0).getImage());
          switch (data.getImage().size()){
              case 1:
                  Glide.with(context)
                          .load(Constant.DOMIN+data.getImage().get(0).getImage())
                          .centerCrop()
                          .into(firstImg);

                  break;
              case 2:

                  Glide.with(context)
                          .load(Constant.DOMIN+data.getImage().get(0).getImage())
                          .centerCrop()
                          .into(firstImg);
                  secondImg.setVisibility(View.VISIBLE);
                  Glide.with(context)
                          .load(Constant.DOMIN+data.getImage().get(1).getImage())
                          .centerCrop()
                          .into(secondImg);
                  break;
              case 3:
                  Glide.with(context)
                          .load(Constant.DOMIN+data.getImage().get(0).getImage())
                          .centerCrop()
                          .into(firstImg);
                  secondImg.setVisibility(View.VISIBLE);
                  Glide.with(context)
                          .load(Constant.DOMIN+data.getImage().get(1).getImage())
                          .centerCrop()
                          .into(secondImg);
                  thirdImg.setVisibility(View.VISIBLE);
                  Glide.with(context)
                          .load(Constant.DOMIN+data.getImage().get(2).getImage())
                          .centerCrop()
                          .into(thirdImg);
                  break;
              case 0:
                  firstImg.setVisibility(View.INVISIBLE);
                  break;
          }


        }

    }
    @OnClick({R.id.img_back,R.id.img_first,R.id.img_second,R.id.img_third})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:

            onBackPressed();
                break;
            case R.id.img_first:
                startActivity(
                        new Intent(context, GalleryActivity.class).
                                putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN+data.getImage().get(0).getImage()));
                break;
            case R.id.img_second:
                startActivity(
                        new Intent(context, GalleryActivity.class).
                                putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN+data.getImage().get(1).getImage()));
                break;
            case R.id.img_third:
                startActivity(
                        new Intent(context, GalleryActivity.class).
                                putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN+data.getImage().get(2).getImage()));

                break;

        }

    }
}
