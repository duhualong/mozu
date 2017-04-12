package org.eenie.wgj.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.adapter.ContactsAdapter;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.data.remote.HttpClient;
import org.eenie.wgj.data.remote.RemoteService;
import org.eenie.wgj.model.ApiUrl;
import org.eenie.wgj.model.requset.ContactsData;
import org.eenie.wgj.model.response.Contacts;
import org.eenie.wgj.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static org.eenie.wgj.util.PinYin4jUtil.getFirstAlphabet;

/**
 * Created by Eenie on 2017/4/10 at 11:14
 * Email: 472279981@qq.com
 * Des:
 */

public class ContactsPagerFragment extends BaseSupportFragment {
    private Handler mHandler = new Handler();
    private ContactsAdapter mAdapter;

    @BindView(R.id.rv_contacts)RecyclerView mRecyclerView;
    @BindView(R.id.side_bar)WaveSideBar mWaveSideBar;
    @Override
    protected int getContentView() {
        return R.layout.fragment_contacts_pager;
    }

    @Override
    protected void updateUI() {


        initData();


    }
    //初始化数据
    private void initData() {
        String tokenUrl = RemoteService.DOMAIN + ApiUrl.CONTACTS_LIST;
        HttpClient client = new HttpClient();
       String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");
        if (!TextUtils.isEmpty(token)){
            client.getDatas(tokenUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {


                    String result = response.body().string();
                    if (response.isSuccessful() && !TextUtils.isEmpty(result)) {
                        Gson gson = new Gson();
                        ContactsData mdata = gson.fromJson(result, ContactsData.class);
                        int code = mdata.getResultCode();
                        if (code==0){
                            Log.d(TAG, "onResponse: "+mdata.getResultMessage());
                            List<Contacts> datas = new ArrayList<>();
                            if (mdata.getResultMessage()!=null) {
                                for (Contacts data : mdata.getResultMessage()) {

                                    for (int i = 0; i < mdata.getResultMessage().size(); i++) {
                                     String ss=   getFirstAlphabet(mdata.getResultMessage().get(i).getName());
                                        data.setFirstAlphabet(getFirstAlphabet(mdata.getResultMessage().get(i).getName()));
                                        data.setFirstAlphabet(getFirstAlphabet(mdata.getResultMessage().get(i).getName()));

                                    }
                                    datas.add(data);

                                }
//
//
                                mHandler.post(() -> {
                                    mAdapter = new ContactsAdapter(datas, R.layout.item_contacts_right);
                                    mRecyclerView.setAdapter(mAdapter);
                                });
                            }




                        }else {
                            Toast.makeText(context,"数据加载错误!",Toast.LENGTH_LONG).show();
                        }
                        Log.d(TAG, "onResponse: " + code);
                    }


                }
            }, token);
        }

    }


}
