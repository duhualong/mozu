package org.eenie.wgj.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.data.remote.HttpClient;
import org.eenie.wgj.data.remote.RemoteService;
import org.eenie.wgj.model.ApiUrl;
import org.eenie.wgj.model.requset.ContactsData;
import org.eenie.wgj.util.Constants;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/4/10 at 11:14
 * Email: 472279981@qq.com
 * Des:
 */

public class ContactsPagerFragment extends BaseSupportFragment {
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
