package org.eenie.wgj.ui.meeting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.ui.meeting.fragment.MeetingEndFragment;
import org.eenie.wgj.ui.meeting.fragment.MeetingProgressFragment;
import org.eenie.wgj.ui.meeting.fragment.MeetingUnStartFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/11 at 19:46
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingArrangeActivity extends BaseActivity {
    @BindView(R.id.rg_group)
    RadioGroup mRgGroup;
    @BindView(R.id.page_meeting)
    ViewPager mPageError;
    List<Fragment> mFragments = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.activity_meeting_arrange;
    }

    @Override
    protected void updateUI() {
        mFragments.add(new MeetingUnStartFragment());
        mFragments.add(new MeetingProgressFragment());
        mFragments.add(new MeetingEndFragment());
        mPageError.setAdapter(new ErrorPageAdapter());


        mRgGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_unstart:
                    mPageError.setCurrentItem(0);
                    break;
                case R.id.rb_progress:
                    mPageError.setCurrentItem(1);
                    break;
                case R.id.rb_end:
                    mPageError.setCurrentItem(2);
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
