package org.eenie.wgj.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RadioGroup;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Eenie on 2017/7/21 at 17:40
 * Email: 472279981@qq.com
 * Des:
 */

public class NewMessagePageFragment  extends BaseSupportFragment {
    @BindView(R.id.rg_group)
    RadioGroup mRgGroup;

    List<Fragment> mFragments = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.fragment_new_message_pager;
    }

    @Override
    protected void updateUI() {

        mFragments.add(new UnRedMessageFragment());
        mFragments.add(new UnRedMessageFragment());
        getFragmentManager().beginTransaction()
                .add(R.id.message_container, new UnRedMessageFragment()).commit();
//        mPageError.setAdapter(new ErrorPageAdapter(getFragmentManager()));
//
        mRgGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_unread:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.message_container,new  UnRedMessageFragment())
                            .commit();                        break;
                case R.id.rb_read:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.message_container,new RedMessageFragment())
                            .commit();                                break;
            }
        });




    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class ErrorPageAdapter extends FragmentPagerAdapter {

//        public ErrorPageAdapter(FragmentManager fm) {
//            super(fm);
//        }


        public ErrorPageAdapter(FragmentManager fm) {
            super(fm);
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
