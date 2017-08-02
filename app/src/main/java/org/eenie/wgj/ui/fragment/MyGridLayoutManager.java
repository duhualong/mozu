package org.eenie.wgj.ui.fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Eenie on 2017/8/2 at 9:12
 * Email: 472279981@qq.com
 * Des:
 */

public class MyGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }
    public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}

