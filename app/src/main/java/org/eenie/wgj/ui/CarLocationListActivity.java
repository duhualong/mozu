package org.eenie.wgj.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;


import butterknife.BindView;

/**
 * Created by Eenie on 2017/5/10 at 15:17
 * Email: 472279981@qq.com
 * Des:
 */
public class CarLocationListActivity extends BaseActivity {
    private static final String TAG = CarLocationListActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.tv_emptyView)
    TextView tvEmptyView;
    //数据存储列表

    //当前页数
    int pageNo = 1;
    //每页显示数
    int pageSize = 15;

    @Override
    protected int getContentView() {
        return R.layout.fargment_register_search_pager;
    }

    @Override
    protected void updateUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setEmptyView(tvEmptyView);
    }

    private void GetPageListData(int pageNo, int pageSize) {
        //拿到数据

    }


    @Override
    protected void onStop() {
        super.onStop();
        mRecyclerView = null;
        toolbar = null;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}