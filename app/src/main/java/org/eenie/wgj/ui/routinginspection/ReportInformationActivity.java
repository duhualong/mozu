package org.eenie.wgj.ui.routinginspection;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.ui.routinginspection.report.PatrolErrorFragment;
import org.eenie.wgj.ui.routinginspection.report.PatrolErrorHistoryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/23 at 14:16
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportInformationActivity extends BaseActivity {
    @BindView(R.id.rb_current)
    RadioButton mRbCurrent;
    @BindView(R.id.rb_history)
    RadioButton mRbHistory;
    @BindView(R.id.rg_group)
    RadioGroup mRgGroup;
    @BindView(R.id.page_error)
    ViewPager mPageError;

    List<Fragment> mFragments = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.activity_report_information;
    }

    @Override
    protected void updateUI() {

        mFragments.add(new PatrolErrorFragment());
        mFragments.add(new PatrolErrorHistoryFragment());
        mPageError.setAdapter(new ErrorPageAdapter());


        mRgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_current:
                        mPageError.setCurrentItem(0);
                        break;
                    case R.id.rb_history:
                        mPageError.setCurrentItem(1);
                        break;
                }
            }
        });

        mPageError.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRgGroup.check(mRgGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
    @OnClick({R.id.img_back})public  void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:

                onBackPressed();
                break;
        }
    }


    private class ErrorPageAdapter extends FragmentPagerAdapter {
        public ErrorPageAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
