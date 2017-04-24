package org.eenie.wgj.ui.contacts;


import android.widget.TextView;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.Contacts;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.zhouzhuo.zzletterssidebar.ZzLetterSideBar;
import me.zhouzhuo.zzletterssidebar.interf.OnLetterTouchListener;
import me.zhouzhuo.zzletterssidebar.widget.ZzRecyclerView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/4/24 at 14:59
 * Email: 472279981@qq.com
 * Des:
 */

public class FragmentContact extends BaseSupportFragment {
    @BindView(R.id.rv)
    ZzRecyclerView rv;
    private List<PersonEntity> mDatas;
    private PersonRecyclerViewAdapter adapter;
    @BindView(R.id.sidebar)
    ZzLetterSideBar sideBar;
    @BindView(R.id.tv_dialog)
    TextView tvDialog;
    public String[] mSData;
    private List<String> mData;


    @Override
    protected int getContentView() {
        return R.layout.test_contacts_list;
    }

    @Override
    protected void updateUI() {

        //set adapter
        mDatas = new ArrayList<>();
        adapter = new PersonRecyclerViewAdapter(context, mDatas);
        //set click event, optional
        adapter.setRecyclerViewClickListener((itemView, pos) ->
                Toast.makeText(context, mDatas.get(pos).getPersonName(), Toast.LENGTH_SHORT).show());
        rv.setAdapter(adapter);

        //init data
        //init data
       // String[] personNames = getResources().getStringArray(R.array.persons);
        initData();



    }

    private void initData() {
        System.out.println("打印：" + mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""));
        mSubscription=mRemoteService.getContacts(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<List<Contacts>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse<List<Contacts>> listApiResponse) {
                        System.out.println("打印数组"+listApiResponse.getData().size());
                        List<Contacts> mContacts = listApiResponse.getData();
                        String[] list =new String[mContacts.size()-1];

                        for (int i=0;i<mContacts.size();i++){
                            list[i]=mContacts.get(i).getName();
                        }
                        System.out.println("打印list"+list);
                        //init data
                        String[]  personNames={"张三","王五","赵四"};
                        //String[] personNames = getResources().getStringArray(R.array.persons);
                        List<PersonEntity> personEntities = new ArrayList<>();
                        for (String name : personNames) {
                            PersonEntity entity = new PersonEntity();
                            entity.setPersonName(name);
                            personEntities.add(entity);
                        }

                        //update data
                        mDatas = personEntities;
                        adapter.updateRecyclerView(mDatas);
                        adapter.notifyDataSetChanged();

                        //set touch event, must add
                        sideBar.setLetterTouchListener(rv, adapter, tvDialog, new OnLetterTouchListener() {
                            @Override
                            public void onLetterTouch(String letter, int position) {
                            }

                            @Override
                            public void onActionUp() {
                            }
                        });

                    }
                });

    }

}

