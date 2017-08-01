package org.eenie.wgj.ui.application;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.facebook.stetho.common.LogUtil;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.data.local.HomeModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Eenie on 2017/8/1 at 9:20
 * Email: 472279981@qq.com
 * Des:
 */

public class ApplicationFragment extends BaseSupportFragment implements MenuItem.OnMenuItemClickListener,
        AppAdapter.ItemActionListener{
    @BindView(R.id.topBar)
    TopBar mTopBar;
    @BindView(R.id.rv_index_app)
    RecyclerView mRvIndexApp;
    @BindView(R.id.rv_other_app)
    RecyclerView mRvOtherApp;

    MenuItem mMenuItem;
    MenuItem mQueryMenuItem;

    //    List<HomeModule> mModules = new ArrayList<>();
    List<HomeModule> mIndexModules = new ArrayList<>();
    List<HomeModule> mOtherModules = new ArrayList<>();

    AppAdapter mIndexAppAdapter;
    AppAdapter mOtherAppAdapter;
    @BindView(R.id.tv_drag_hint)
    TextView mTvDragHint;


    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    @Override
    protected int getContentView() {
        return R.layout.fragment_application_new;
    }

    @Override
    protected void updateUI() {
        initUI();

    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    private void initUI() {
        mTopBar.setTitle("应用");
        //showContent(false);

        mMenuItem = mTopBar.getToolbar().getMenu().add("编辑");
        mMenuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mMenuItem.setOnMenuItemClickListener(this);



        mIndexAppAdapter = new AppAdapter(mIndexModules, this);
        mOtherAppAdapter = new AppAdapter(mOtherModules, this);
        mRvIndexApp.setItemAnimator(new DefaultItemAnimator());
        mRvIndexApp.setAdapter(mIndexAppAdapter);
        mRvIndexApp.setLayoutManager(new GridLayoutManager(getContext(), 4));

        mRvOtherApp.setAdapter(mOtherAppAdapter);
        mRvOtherApp.setLayoutManager(new GridLayoutManager(getContext(), 4));


        OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder holder, int i) {
                LogUtil.e("Drag Start");
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder holder, int i, RecyclerView.ViewHolder viewHolder1, int i1) {
                LogUtil.e("Drag Move");
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder holder, int i) {
                LogUtil.e("Drag End");
              //  EventBus.getDefault().post(new ModuleChangeEvent());
            }
        };


        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mIndexAppAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRvIndexApp);


        mIndexAppAdapter.setOnItemDragListener(listener);

        queryIndex();

    }

    private void queryIndex() {
        mIndexModules.clear();
        mOtherModules.clear();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<HomeModule> results = realm.where(HomeModule.class).findAllSorted("index");

        for (HomeModule m : results) {
            if (m.isShow()) {
                mIndexModules.add(m);
            } else {
                mOtherModules.add(m);
            }
        }

        mOtherAppAdapter.notifyDataSetChanged();
        mIndexAppAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item == mQueryMenuItem) {

//            saveModule();


        } else {
            if (item.getTitle().equals("编辑")) {
                item.setTitle("完成");
                mTvDragHint.setText("长按拖动可排序");
                mTvDragHint.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
                mIndexAppAdapter.enableDragItem(mItemTouchHelper);
                mIndexAppAdapter.startEditMode();
                mOtherAppAdapter.startEditMode();

            } else {
                item.setTitle("编辑");
                mTvDragHint.setText("");
                mIndexAppAdapter.disableDragItem();
                mIndexAppAdapter.stopEditMode();
                mOtherAppAdapter.stopEditMode();
            }
        }

        return true;
    }

//    @Override
//    public void onItemAdd(View itemView, HomeModule module, int position) {
//
//    }
//
//    @Override
//    public void onItemRemove(View itemView, HomeModule module, int position) {
//
//    }


    @Override
    public void onItemAdd(View itemView, final HomeModule module, final int position) {


        if (mIndexAppAdapter.getData().size() < 8) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    module.setShow(true);
                    mIndexModules.add(module);
                    mOtherModules.remove(module);
                    //EventBus.getDefault().post(new ModuleChangeEvent());
                    mOtherAppAdapter.notifyDataSetChanged();
                    mIndexAppAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Toast.makeText(context,"首页最多只能摆放8个模块",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemRemove(View itemView, final HomeModule module, int position) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                module.setShow(false);
                mOtherModules.add(0, module);
                mIndexModules.remove(module);
                //EventBus.getDefault().post(new ModuleChangeEvent());
                mOtherAppAdapter.notifyDataSetChanged();
                mIndexAppAdapter.notifyDataSetChanged();
            }
        });
    }


}
