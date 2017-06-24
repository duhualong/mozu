package org.eenie.wgj.ui.routinginspection.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.eenie.wgj.R;

/**
 * Created by Eenie on 2017/6/23 at 11:19
 * Email: 472279981@qq.com
 * Des:
 */

public class HintProgressBar  extends FrameLayout {

    private TextView mHintView;
    private Context mContext;
    private ProgressBar mProgressBar;
    private View mParentView;

    private int hintBack;
    private int progressBack;
    private String prefixionText;

    public HintProgressBar(Context context) {
        this(context, null);
    }

    public HintProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        setClipChildren(false);
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.HintProgressBar);
        hintBack = a.getResourceId(R.styleable.HintProgressBar_hint_view_background,
                R.drawable.hint_progress_bg_blue);
        progressBack = a.getResourceId(R.styleable.HintProgressBar_hint_progressbar_progress_style,
                R.drawable.hint_progress_indeterminate_drawable);
        prefixionText = a.getString(R.styleable.HintProgressBar_hint_view_prefixion_text);

        a.recycle();

        mParentView = LayoutInflater.from(mContext).inflate(R.layout.view_hint_progress_layout, this);
        mHintView = (TextView) mParentView.findViewById(R.id.tv_hint);
        mHintView.setBackgroundResource(hintBack);
        mHintView.setText(prefixionText == null ? "" : prefixionText + String.format("%1s", 0) + "%");

        mProgressBar = (ProgressBar) mParentView.findViewById(R.id.pb_progress);
        mProgressBar.setProgressDrawable(mContext.getResources().getDrawable(progressBack));

    }


    public void setProgress(int progress) {
        Log.e("mProgressBar", String.valueOf(progress));
        mProgressBar.setProgress(progress);
        float hintX = progress / 100f * getMeasuredWidth();
        if (hintX > mHintView.getWidth() / 2 && hintX < getWidth() - mHintView.getWidth() / 2) {
            mHintView.setTranslationX(hintX - mHintView.getWidth() / 2);
        } else {
            if (progress > 50) {
                mHintView.setTranslationX(getWidth() - mHintView.getWidth());
            } else {
                mHintView.setTranslationX(0);
            }
        }
        mHintView.setText(prefixionText + String.format("%1s", progress) + "%");
    }
}
