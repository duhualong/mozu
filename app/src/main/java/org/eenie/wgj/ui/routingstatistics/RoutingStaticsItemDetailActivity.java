package org.eenie.wgj.ui.routingstatistics;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/1 at 19:49
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingStaticsItemDetailActivity extends BaseActivity {
    public static final String PROJECT_ID = "id";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_select_time)
    TextView tvSelectTime;
    private String projectId;


    @Override
    protected int getContentView() {
        return R.layout.activity_routing_statics_item_detail;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);


    }

    @OnClick({R.id.img_back, R.id.tv_select_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.tv_select_time:


                break;
        }
    }
}
