package org.eenie.wgj.data.local;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Eenie on 2017/5/8 at 9:21
 * Email: 472279981@qq.com
 * Des:
 */

public class SquareViewGroup extends FrameLayout {



    public SquareViewGroup(Context context) {
        super(context);
    }

    public SquareViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int side = Math.min(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        super.onMeasure(MeasureSpec.makeMeasureSpec(side, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(side, MeasureSpec.getMode(heightMeasureSpec)));
    }
}
