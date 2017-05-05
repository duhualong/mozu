package org.eenie.wgj;


import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.ui.contacts.FragmentContact;
import org.eenie.wgj.ui.fragment.ApplyPagerFragment;
import org.eenie.wgj.ui.fragment.HomePagerFragment;
import org.eenie.wgj.ui.fragment.MessagePagerFragment;
import org.eenie.wgj.ui.fragment.PersonalCenterFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String[] titles = {"苏宁天御", "通讯录", "消息中心", "应用", "我的"};
    private static final int NAVIGATOR_COUNT = 5;
    private static final String TAG ="MainActivity" ;
    int[] navigatorMipmapNormal = {R.mipmap.ic_home_bottom_tab_main,
            R.mipmap.ic_home_bottom_tab_contact, R.mipmap.ic_home_bottom_tab_unselected_msg,
            R.mipmap.ic_home_bottom_tab_app, R.mipmap.ic_home_bottom_tab_me};
    int[] navigatorMipmapSelect = {R.mipmap.ic_home_bottom_tab_main_selected,
            R.mipmap.ic_home_bottom_tab_contact_selected, R.mipmap.ic_home_bottom_tab_msg,
            R.mipmap.ic_home_bottom_tab_app_selected, R.mipmap.ic_home_bottom_tab_me_selected
    };
    @BindView(R.id.img_scan)ImageView imgScan;
    @BindView(R.id.tv_search)TextView search;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_message)
    ImageView imgMessage;

    //    @BindView(R.id.main_container)FrameLayout fragment;
    @BindView(R.id.page_title)
    TextView mPageTitleView;
    @BindViews({R.id.tv_home_pager, R.id.tv_contact_pager, R.id.tv_message_pager, R.id.tv_apply_pager,
            R.id.tv_mine_pager})
    List<TextView> bottomText;


    @Override
    protected int getContentView() {
        return R.layout.activity_main_pager;
    }

    @Override
    protected void updateUI() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }


        setCurrentPager(0);
        setCurrentNavigator(0);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, new HomePagerFragment()).commit();


    }


    private void refreshNavigator() {
        for (int i = 0; i < NAVIGATOR_COUNT; i++) {
            if (i!=2) {

                bottomText.get(i).
                        setCompoundDrawablesWithIntrinsicBounds(0, navigatorMipmapNormal[i], 0, 0);

            }else {
                imgMessage.setImageResource(R.mipmap.ic_home_bottom_tab_unselected_msg);
            }
            bottomText.get(i).setTextColor(ContextCompat.
                    getColor(MainActivity.this, R.color.titleColor));
        }
    }

    private void setCurrentNavigator(int index) {
        refreshNavigator();
        if (index != 2) {
            bottomText.get(index).
                    setCompoundDrawablesWithIntrinsicBounds(0, navigatorMipmapSelect[index], 0, 0);
        } else {
            imgMessage.setImageResource(R.mipmap.ic_home_bottom_tab_msg);

        }
        bottomText.get(index).setTextColor(ContextCompat.getColor
                (MainActivity.this, R.color.colorAccent));


        mPageTitleView.setText(titles[index]);
        refreshTop(index);

    }

    private void refreshTop(int index) {
        if (index == 0) {
        imgScan.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            search.setText("搜索");

        } else {
            if (index==3){
                search.setVisibility(View.VISIBLE);
                search.setText("编辑");
            }else {
                search.setVisibility(View.GONE);
            }
            imgScan.setVisibility(View.GONE);



        }
    }

    private void setCurrentPager(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = new HomePagerFragment();
                break;
            case 1:
                fragment = new FragmentContact();

                break;
            case 2:
                fragment = new MessagePagerFragment();
                break;
            case 3:
                fragment = new ApplyPagerFragment();
                break;
            case 4:
                fragment = new PersonalCenterFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }


    @OnClick({R.id.ll_home_pager, R.id.ll_contact_pager, R.id.ll_message_pager, R.id.ll_apply_pager,
            R.id.ll_mine_pager})
    public void OnClick(View view) {
        int pageIndex = 0;
        switch (view.getId()) {

            case R.id.ll_home_pager:
                pageIndex = 0;

                break;

            case R.id.ll_contact_pager:

                pageIndex = 1;
                break;

            case R.id.ll_message_pager:
                pageIndex = 2;

                break;
            case R.id.ll_apply_pager:
                pageIndex = 3;

                break;

            case R.id.ll_mine_pager:

                pageIndex = 4;

                break;
        }
        setCurrentPager(pageIndex);
        setCurrentNavigator(pageIndex);
    }



}
