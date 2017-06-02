package org.eenie.wgj.ui.project.attendance;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.LocationAddress;
import org.eenie.wgj.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Eenie on 2017/5/23 at 14:33
 * Email: 472279981@qq.com
 * Des:
 */

public class SearchAddressActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        IStaticHandler {

    private Handler mHandler = StaticHandlerFactory.create(this);

    private static final int SEARCH_SUCCESS = 0x101;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.locationTitle)
    TextView mTitleView;
    @BindView(R.id.listView)
    ListView mListView;
    private SearchView mSearchView;
    private View mPopupView;
    private ListView mAddressListView;
    private PopupWindow mPopupAddress;
    private String region;
    private String oldRecoder;
    List<LocationAddress> mList;

    @Override
    protected int getContentView() {
        return R.layout.search_address_activity;
    }

    @Override
    protected void updateUI() {
        mTitleView.setText("当前地理位置");
        mTitleView.setTextSize(14);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

//        mPopupView = View.inflate(context, R.layout.popup_address_list, null);
//        mAddressListView = (ListView) mPopupView.findViewById(R.id.addressList);
//        mAddressListView.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_seach_location, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Set search view search actions
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query) && Utils.isChineseInput(query)) {
                    searchAddress(query);
                }
//                else {
//                    if (TextUtils.isEmpty(oldRecoder)) {
//                        if (mPopupAddress != null && mPopupAddress.isShowing()) {
//                            mPopupAddress.dismiss();
//                        }
//                    } else {
//
//                    }
//                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText) && Utils.isChineseInput(newText)) {
                    searchAddress(newText);
                }
//                else {
//                    if (mPopupAddress != null && mPopupAddress.isShowing()) {
//                        mPopupAddress.dismiss();
//                    }
//                }
                return true;
            }
        });
        return true;
    }

    private void searchAddress(String address) {

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constant.BAIDU_SEARCH)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        FileUploadService userBiz = retrofit.create(FileUploadService.class);
//        Call<ApiResponse> call = userBiz.getAddressDetail("上海", address, "json");
//        call.enqueue(new Callback<ApiResponse>() {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                if (response.body().getStatus() == 0) {
//                    Gson gson = new Gson();
//                    String jsonArray = gson.toJson(response.body().getResult());
//                    List<LocationAddress> data = gson.fromJson(jsonArray,
//                            new TypeToken<List<LocationAddress>>() {
//                            }.getType());
//
//                    if (data != null) {
//                        SearchAddressAdapter
//                                mAdapter = new SearchAddressAdapter(context, data);
//                        mListView.setAdapter(mAdapter);
//
//                    }
//
//
//                    Log.d("test", "onResponse: " + response.body().getResult().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse> call, Throwable t) {
//
//            }
//        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mList != null && !mList.isEmpty()) {
            LocationAddress address = mList.get(position);
            System.out.println("name" + address.getName());

            mSearchView.clearFocus();

            if (mPopupAddress.isShowing()) {
                mPopupAddress.dismiss();
            }


        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SEARCH_SUCCESS:
                // Toast.makeText(context, "解析数据成功", Toast.LENGTH_SHORT).show();
                List<LocationAddress> searchList = (List<LocationAddress>) msg.obj;
                if (searchList != null && !searchList.isEmpty()) {
                    mList = new ArrayList<>();
                    for (LocationAddress address : searchList) {

                        mList.add(address);
                    }
                }
                if (mList != null) {
                    SearchAddressAdapter
                            mAdapter = new SearchAddressAdapter(context, mList);
                    mAddressListView.setAdapter(mAdapter);

                }

                mAddressListView.setOnItemClickListener(this);
                break;
        }
    }
}