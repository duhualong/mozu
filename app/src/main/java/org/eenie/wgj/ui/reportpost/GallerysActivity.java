package org.eenie.wgj.ui.reportpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.eenie.wgj.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/5 at 16:19
 * Email: 472279981@qq.com
 * Des:
 */

public class GallerysActivity extends AppCompatActivity{

    public static final String EXTRA_IMAGE_URI = "image_uri";
    public static final String EXTRA_GALLERY_URI = "gallery_uri";

    @BindView(R.id.photo_container)
    ImageView mPhotoContainer;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);

        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.VISIBLE);
        String uri = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        if (!TextUtils.isEmpty(uri)) {
            Glide.with(this).load(new File(uri)).fitCenter().listener(new RequestListener<File, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, File model, Target<GlideDrawable> target,
                                           boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, File model,
                                               Target<GlideDrawable> target,
                                               boolean isFromMemoryCache, boolean isFirstResource) {
                    mProgressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(mPhotoContainer);
//            Glide.with(this).load(uri).fitCenter().listener(new RequestListener<String, GlideDrawable>() {
//                @Override
//                public boolean onException(Exception e, String model, Target<GlideDrawable> target,
//                                           boolean isFirstResource) {
//                    return false;
//                }
//
//                @Override public boolean onResourceReady(GlideDrawable resource, String model,
//                                                         Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    mProgressBar.setVisibility(View.GONE);
//                    return false;
//                }
//            }).into(mPhotoContainer);
        }
    }

    @OnClick(R.id.photo_container) public void onClick() {
        GallerysActivity.this.finish();
    }
}


