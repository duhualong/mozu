package org.eenie.wgj.ui.contacts;


import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.data.remote.HttpClient;
import org.eenie.wgj.data.remote.RemoteService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.Contacts;
import org.eenie.wgj.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.zhouzhuo.zzletterssidebar.ZzLetterSideBar;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/4/24 at 14:59
 * Email: 472279981@qq.com
 * Des:
 */

public class FragmentContact extends BaseSupportFragment {
    @BindView(R.id.list_view)
    ListView listView;
    private List<PersonEntity> mDatas;
    private PersonRecyclerViewAdapter adapter;
    @BindView(R.id.sidebar)
    ZzLetterSideBar sideBar;
    @BindView(R.id.tv_dialog)
    TextView tvDialog;
    public String[] mSData;
    private List<String> mData;
    private String[] mList;
    private String[] mPersonNames;
    private List<PersonEntity> mPersonEntities;


    @Override
    protected int getContentView() {
        return R.layout.test_contacts_list;
    }

    @Override
    protected void updateUI() {
       // initDatas();


        //set adapter
        mDatas = new ArrayList<>();
        adapter = new PersonRecyclerViewAdapter(context, mDatas);
        listView.setAdapter(adapter);

        String[] personNames = getResources().getStringArray(R.array.persons);
        List<PersonEntity> personEntities = new ArrayList<>();
        for (String name : personNames) {
            PersonEntity entity = new PersonEntity();
            entity.setPersonName(name);
            personEntities.add(entity);
        }
        mDatas = mPersonEntities;
        adapter.updateListView(mDatas);

    }

    private void initDatas() {
        HttpClient client = new HttpClient();
        String tokenUrl = RemoteService.DOMAIN + "contacts/userList";

        client.getDatas(tokenUrl, mPrefsHelper.getPrefs().getString(Constants.TOKEN, "")).
                flatMap(response -> {
            String result = null;
            try {
                result = response.body().string();
            } catch (IOException e) {
                return Single.error(e);
            }
            if (response.isSuccessful() && !TextUtils.isEmpty(result)) {
                Gson gson = new Gson();
                ApiResponse mdata = gson.fromJson(result, ApiResponse.class);

                return Single.just(mdata.getData());
            }
            return Single.just("");
        }).subscribe(s -> {
            mDatas = new ArrayList<>();
            adapter = new PersonRecyclerViewAdapter(context, mDatas);
            listView.setAdapter(adapter);
            List<Contacts> data= (List<Contacts>) s;
            mList = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                mList[i] = data.get(i).getName();
            }

            if (mList!=null) {
                mPersonEntities = new ArrayList<>();
                for (String name : mList) {
                    PersonEntity entity = new PersonEntity();
                    entity.setPersonName(name);
                    mPersonEntities.add(entity);
                }
                //init data
                mDatas = mPersonEntities;
                adapter.updateListView(mDatas);


            }


        });
    }

    private void initData() {
        System.out.println("打印：" + mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""));
        mSubscription = mRemoteService.getContacts(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""))
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
                        System.out.println("打印数组" + listApiResponse.getData().size());
                        List<Contacts> mContacts = listApiResponse.getData();

                        new Thread() {
                            public void run() {
                                //set adapter
                                mDatas = new ArrayList<>();
                                adapter = new PersonRecyclerViewAdapter(context, mDatas);
                                listView.setAdapter(adapter);
                                mList = new String[mContacts.size()];

                                for (int i = 0; i < mContacts.size(); i++) {
                                    mList[i] = mContacts.get(i).getName();
                                }
                                System.out.println("打印list" + mList);


                                if (mList!=null) {
                                    mPersonEntities = new ArrayList<>();
                                    for (String name : mList) {
                                        PersonEntity entity = new PersonEntity();
                                        entity.setPersonName(name);
                                        mPersonEntities.add(entity);
                                    }
                                    //init data
                                    mDatas = mPersonEntities;
                                    adapter.updateListView(mDatas);

                                }
                            }
                        }.start();


                    }
                });

    }

}

