//package org.eenie.wgj.ui.attendance;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.support.annotation.DrawableRes;
//import android.support.v7.widget.Toolbar;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
///**
// * Created by Eenie on 2017/6/13 at 10:41
// * Email: 472279981@qq.com
// * Des:
// */
//
//public class TopBar   extends FrameLayout {
//    Context mContext;
//    Toolbar mToolbar;
//    TextView tvTitle;
//
//    public TopBar(Context context) {
//        this(context, null);
//    }
//
//    public TopBar(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//        if (attrs != null) {
//            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TopBar);
//            int titleColor = a.getColor(R.styleable.TopBar_titleColor, mContext.getResources().getColor(R.color.color_md_white));
//            tvTitle.setTextColor(titleColor);
//            int backColor = a.getColor(R.styleable.TopBar_backColor, mContext.getResources().getColor(R.color.colorPrimary));
//            mToolbar.setBackgroundColor(backColor);
//            String title = a.getString(R.styleable.TopBar_title);
//            tvTitle.setText(title);
//            a.recycle();
//        }
//    }
//
//    private void init(Context context) {
//        this.mContext = context;
//        LayoutInflater.from(context).inflate(R.layout.view_topbar, this, true);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        mToolbar.setTitle("");
//    }
//
//
//    public void setTitle(String title) {
//        tvTitle.setText(title);
//    }
//
//    public String getTitle() {
//        return tvTitle.getText().toString();
//    }
//
//    public void setLeftIcon(@DrawableRes int resId) {
//        mToolbar.setNavigationIcon(resId);
//    }
//
//
//    public TextView getTitleView() {
//        return tvTitle;
//    }
//
//    public Toolbar getToolbar() {
//        return mToolbar;
//    }