package org.eenie.wgj.ui.exchangeclass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/31 at 15:52
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeClassHistoryRecordActivity extends BaseActivity {
    @BindView(R.id.rg_group)
    RadioGroup mRgGroup;
    @BindView(R.id.view_pager)
    ViewPager mPageError;
    List<Fragment> mFragments = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.activity_exchange_class_history_record;
    }

    @Override
    protected void updateUI() {
        mFragments.add(new HandClassFragment());
        mFragments.add(new TakeClassFragment());

        mPageError.setAdapter(new ExchangePageAdapter());


        mRgGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_hand:
                    mPageError.setCurrentItem(0);
                    break;
                case R.id.rb_take:
                    mPageError.setCurrentItem(1);
                    break;

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

    @OnClick({R.id.img_back})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:

                onBackPressed();
                break;
        }
    }
    private class ExchangePageAdapter extends FragmentPagerAdapter {
        public ExchangePageAdapter() {
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
