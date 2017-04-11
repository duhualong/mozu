package org.eenie.wgj.ui.fragment;

import android.widget.GridView;

import com.yyydjk.library.BannerLayout;

import org.eenie.wgj.R;
import org.eenie.wgj.adapter.GridItem;
import org.eenie.wgj.adapter.GridViewAdapter;
import org.eenie.wgj.base.BaseSupportFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 */

public class HomePagerFragment extends BaseSupportFragment {
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<GridItem> mGridData ;

    private Integer[] localGradText={R.string.work_attendance,R.string.work_attendance_statistics,
    R.string.routing_inspection,R.string.routing_inspection_statistics,R.string.train,
            R.string.train_statistics,R.string.report_job,R.string.report_job_statistics,

    };

    private Integer[]localGradImg={R.mipmap.ic_home_module_sign,R.mipmap.ic_home_module_stati,
    R.mipmap.ic_home_module_polling,R.mipmap.ic_home_module_polling_stati,
            R.mipmap.ic_home_module_train,R.mipmap.ic_train_statistics,
            R.mipmap.ic_home_module_report,R.mipmap.ic_home_module_reportstatistic};
    private Integer[] ids = {R.mipmap.home_banner_default_bg6, R.mipmap.home_banner_default_bg1,
            R.mipmap.home_banner_default_bg2, R.mipmap.home_banner_default_bg3};
    private Integer[]idBottom={R.mipmap.home_banner_default_bg4,R.mipmap.home_banner_default_bg5};
    @BindView(R.id.banner_top)
    BannerLayout bannerTop;
    @BindView(R.id.banner_bottom)
    BannerLayout bannerBottom;
    @BindView(R.id.gradView)GridView mGradView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void updateUI() {
        mGradView.setSaveEnabled(false);
        initData();


    }

    //初始化数据
    private void initData() {

        bannerTop.setViewRes(Arrays.asList(ids));
        bannerBottom.setViewRes(Arrays.asList(idBottom));


        mGridData = new ArrayList<>();
        for (int i=0; i<8; i++) {
            GridItem item = new GridItem();
            item.setTitle(localGradText[i]);
            item.setImage(localGradImg[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_home, mGridData);
        mGradView.setAdapter(mGridViewAdapter);
    }




}
