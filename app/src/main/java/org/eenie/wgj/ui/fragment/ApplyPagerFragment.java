package org.eenie.wgj.ui.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.facebook.stetho.common.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.data.local.AppAdapter;
import org.eenie.wgj.data.local.HomeModule;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/4/10 at 11:14
 * Email: 472279981@qq.com
 * Des:
 */

public class ApplyPagerFragment extends BaseSupportFragment
        implements AppAdapter.ItemActionListener {
    @BindView(R.id.tv_sort)TextView sort;
    @BindView(R.id.recycler_home_application)RecyclerView homeRecyclerView;
    @BindView(R.id.recycler_other_application)RecyclerView otherRecycler;
    List<HomeModule> mIndexModules = new ArrayList<>();
    List<HomeModule> mOtherModules = new ArrayList<>();

    AppAdapter mIndexAppAdapter;
    AppAdapter mOtherAppAdapter;


    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    @Override
    protected int getContentView() {
        return R.layout.fragment_application;
    }

    @Override
    protected void updateUI() {
        initData();
        mIndexAppAdapter = new AppAdapter(mIndexModules,  this);

        mOtherAppAdapter = new AppAdapter(mOtherModules, this);


        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        homeRecyclerView.setAdapter(mIndexAppAdapter);
        homeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        otherRecycler.setAdapter(mOtherAppAdapter);
        otherRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));


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
                //EventBus.getDefault().post(new ModuleChangeEvent());
            }
        };


        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mIndexAppAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(homeRecyclerView);


        mIndexAppAdapter.setOnItemDragListener(listener);

        queryIndex();
    }
    private void initData() {
        mSubscription = mRemoteService.getAttendanceList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        Gson gson=new Gson();
                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {

                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<AttendanceListResponse>  attendanceResponse =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<AttendanceListResponse>>() {
                                                }.getType());

                                if (attendanceResponse != null) {
                                    ArrayList<String> mList = new ArrayList<>();
                                    ArrayList<String> mLists = new ArrayList<>();
                                    for (int i = 0; i < attendanceResponse.size(); i++) {
                                        mList.add(attendanceResponse.get(i).getDay());
                                        mLists.add(attendanceResponse.get(i).getService().
                                                getServicesname());
                                    }
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.DATE_LIST,gson.toJson(mList))
                                            .putString(Constants.DATE_THING_LIST,gson.toJson(mLists))
                                            .apply();

                                }else {
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.DATE_LIST,"")
                                            .putString(Constants.DATE_THING_LIST,"")
                                            .apply();
                                }

                            }
                        }else {
                            mPrefsHelper.getPrefs().edit().
                                    putString(Constants.DATE_LIST,"")
                                    .putString(Constants.DATE_THING_LIST,"")
                                    .apply();

                        }

                    }
                });
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
    public void onItemAdd(View itemView, final HomeModule module, final int position) {


        if (mIndexAppAdapter.getData().size() < 8) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    module.setShow(true);
                    mIndexModules.add(module);
                    mOtherModules.remove(module);
                 //   EventBus.getDefault().post(new ModuleChangeEvent());
                    mOtherAppAdapter.notifyDataSetChanged();
                    mIndexAppAdapter.notifyDataSetChanged();
                }
            });
        } else {
            ToastUtil.showToast("首页最多只能摆放8个模块");
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
              //  EventBus.getDefault().post(new ModuleChangeEvent());
                mOtherAppAdapter.notifyDataSetChanged();
                mIndexAppAdapter.notifyDataSetChanged();
            }
        });
    }


}
