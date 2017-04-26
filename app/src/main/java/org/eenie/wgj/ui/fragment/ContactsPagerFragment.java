package org.eenie.wgj.ui.fragment;

import android.os.Handler;
import android.widget.ListView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.response.Contacts;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Eenie on 2017/4/10 at 11:14
 * Email: 472279981@qq.com
 * Des:
 */

public class ContactsPagerFragment extends BaseSupportFragment {
    private Handler mHandler = new Handler();

    private List<Contacts> list;

    @BindView(R.id.listView)
    ListView listView;


    @Override
    protected int getContentView() {
        return R.layout.activity_main_contacts;
    }

    @Override
    protected void updateUI() {



        // initData();
       // initDatas();


    }


    //初始化数据
    private void initData() {
//        String tokenUrl = RemoteService.DOMAIN + ApiUrl.CONTACTS_LIST;
//        HttpClient client = new HttpClient();
//        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
//
//        if (!TextUtils.isEmpty(token)) {
//
//            client.getDatas(tokenUrl, token).flatMap(response -> {
//                String result = null;
//                try {
//                    result = response.body().string();
//                } catch (IOException e) {
//                    return Single.error(e);
//                }
//                if (response.isSuccessful() && !TextUtils.isEmpty(result)) {
//                    Gson gson = new Gson();
//                    ContactsData mdata = gson.fromJson(result, ContactsData.class);
//
//                    return Single.just(mdata);
//                }
//                return Single.just("");
//            }).subscribe(s -> {
//                ContactsData data = (ContactsData) s;
//                if (data.getResultCode() == 0 && data.getResultMessage() != null) {
//                    List<Contacts> datas = (List<Contacts>) data.getResultMessage();
//                    for (Contacts contacts : datas) {
//
//                        list.add(contacts);
//                    }
//
//                    new Thread(() -> {
//                        //  Collections.sort(list);
//                      //  SortAdapter adapter = new SortAdapter(list, context);
//                       // listView.setAdapter(adapter);
//                    }).start();
//                }


          //  });


//
//
//            client.getDatas(tokenUrl, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//
//                    String result = response.body().string();
//                    if (response.isSuccessful() && !TextUtils.isEmpty(result)) {
//                        Gson gson = new Gson();
//                        ContactsData mdata = gson.fromJson(result, ContactsData.class);
//                        int code = mdata.getResultCode();
//                        if (code==0&&mdata.getResultMessage()!=null){
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    list= (ArrayList<Contacts>) mdata.getResultMessage();
//
//                                        Collections.sort(list);
//
//                                    SortAdapter adapter = new SortAdapter(list,context);
//                                    listView.setAdapter(adapter);
////                                    mHandler.post(() -> {
////                                        SortAdapter adapter = new SortAdapter(list,context);
////                                        listView.setAdapter(adapter);
////
////                                    });
//
//                                }
//                            }).start();
//                          //  Log.d(TAG, "onResponse: "+mdata.getResultMessage());
//
////                            if (mdata.getResultMessage()!=null) {
////                                for (Contacts data : mdata.getResultMessage()) {
////                                   list.add(data);
////                                    Collections.sort(list);
////                                }
////                                mHandler.post(() -> {
////                                    SortAdapter adapter = new SortAdapter(list,context);
////                                    listView.setAdapter(adapter);
////
////                                });
////                            }
//
//
//                        }else {
//                            Toast.makeText(context,"数据加载错误!",Toast.LENGTH_LONG).show();
//                        }
//                        Log.d(TAG, "onResponse: " + code);
//                    }
//
//
//                }
//            }, token);
//
       // }

    }





}
