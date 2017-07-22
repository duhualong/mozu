package org.eenie.wgj.ui.workshow;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Eenie on 2017/7/21 at 18:44
 * Email: 472279981@qq.com
 * Des:
 */

public class CustomLinearLayoutManager  extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
