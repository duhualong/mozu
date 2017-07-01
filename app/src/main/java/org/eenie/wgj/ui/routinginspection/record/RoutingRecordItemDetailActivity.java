package org.eenie.wgj.ui.routinginspection.record;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.RecordRoutingResponse;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/29 at 12:46
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingRecordItemDetailActivity extends BaseActivity {
    public static final String INFO_ROUTING="info";
    public static final String USERNAME="name";
    public static final String AVATAR_URL="url";
    public static final String PROJECT_ID="project_id";
    private RecordRoutingResponse.InfoBean mData;
    private String userName;
    private String avatarUrl;
    public static final String DATE="date";
    @BindView(R.id.rg_group)
    RadioGroup mRgGroup;
    @BindView(R.id.page_error)
    ViewPager mPageError;
    List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_item_routing_record_detail;
    }

    @Override
    protected void updateUI() {
        mData=getIntent().getParcelableExtra(INFO_ROUTING);
        String date=getIntent().getStringExtra(DATE);
        String avatarUrl=getIntent().getStringExtra(AVATAR_URL);
        String name=getIntent().getStringExtra(USERNAME);
        String projectId=getIntent().getStringExtra(PROJECT_ID);

        mFragments.add(NormalRoutingRecordFragment.newInstance(mData));
        mFragments.add(AbnormalRoutingRecordFragment.newInstance(mData));
        mFragments.add(MissingPointRoutingRecordFragment.newInstance(mData));
        mFragments.add(TestMapFragment.newInstance(date,name,avatarUrl,mPrefsHelper.getPrefs().
                getString(Constants.UID,""),mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),projectId,mData));

        mPageError.setAdapter(new ErrorPageAdapter());


        mRgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_normal:
                        mPageError.setCurrentItem(0);
                        break;
                    case R.id.rb_abnormal:
                        mPageError.setCurrentItem(1);
                        break;
                    case R.id.rb_miss_point:
                        mPageError.setCurrentItem(2);
                        break;
                    case R.id.rb_line:
                        mPageError.setCurrentItem(3);
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
