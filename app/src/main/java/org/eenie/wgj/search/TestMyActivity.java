package org.eenie.wgj.search;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.JoinCompany;
import org.eenie.wgj.model.response.CompanyList;
import org.eenie.wgj.ui.bean.JsonBean;
import org.eenie.wgj.util.GetJsonDataUtil;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/11 at 9:59
 * Email: 472279981@qq.com
 * Des:
 */

public class TestMyActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;
    /**
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private String city;
    @BindView(R.id.tv_select_city)
    TextView selectCity;
    @BindView(R.id.fragment_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.country_lvcountry)
    ListView sortListView;
    @BindView(R.id.sidrbar)
    SideBar sideBar;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.root_view)
    View rootView;


    /**
     *
     */
    private PinyinComparator pinyinComparator;
    private List<String> mStrArray = new ArrayList<>();
    private List<Integer> mCompanyId = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.test_main_activity;
    }

    @Override
    protected void updateUI() {
        initJsonData();
        // initData();
      //  joinCompany(21,1);

    }

    @OnClick({R.id.img_back, R.id.rl_select_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_select_city:
                if (isLoaded) {
                    ShowPickerView();
                }

                break;


        }
    }

    private void initData() {
        mSubscription = mRemoteService.getCompanyList()
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
                    public void onNext(ApiResponse companyListApiResponse) {
                        if (companyListApiResponse.getResultCode() == 200) {
                            Gson gson = new Gson();
                            String jsonArray=gson.toJson( companyListApiResponse.getData());
                            Log.d(TAG, "json: " + jsonArray);
                            List<CompanyList> mData = gson.fromJson(jsonArray,
                                    new TypeToken<List<CompanyList>>() {
                                    }.getType());
                            if (mData != null && !mData.isEmpty()) {
                                for (CompanyList company : mData) {
                                    mStrArray.add(company.getCompany_name());
                                    mCompanyId.add(company.getId());
                                }

                                //initViews();
                                Log.d(TAG, "公司id" + mCompanyId.toString());
                            }

                        }


                    }

                });


    }


    private void getCity(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://118.178.88.132:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        Call<ApiResponse> call = userBiz.getCityCompanyList(city);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 200) {
                        Gson gson = new Gson();
                        String jsonArray=gson.toJson( response.body().getData());
                        Log.d(TAG, "json: " + jsonArray);
                        List<CompanyList> mData = gson.fromJson(jsonArray,
                                new TypeToken<List<CompanyList>>() {
                                }.getType());

                        mFrameLayout.setVisibility(View.VISIBLE);
                        initViews(mData);

                    } else {
                        mFrameLayout.setVisibility(View.GONE);
                        Snackbar.make(rootView, response.body().getResultMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }


    private void getCityCompanyCty(String city) {
        mSubscription = mRemoteService.getCityCompanyList(city)
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
                    public void onNext(ApiResponse listApiResponse) {
                        if (listApiResponse.getResultCode() == 200) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(listApiResponse.getData());
                            List<CompanyList> mData = gson.fromJson(jsonArray,
                                    new TypeToken<List<CompanyList>>() {
                                    }.getType());
                            mFrameLayout.setVisibility(View.VISIBLE);
                            initViews(mData);
                        } else {

                            mFrameLayout.setVisibility(View.GONE);
                            Snackbar.make(rootView, listApiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();


                        }

                        Log.d(TAG, "data" + listApiResponse.getResultCode());
                    }


                });

    }

    private void initViews(List<CompanyList> mData) {
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);


        sideBar.setOnTouchingLetterChangedListener(s -> {

            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                sortListView.setSelection(position);
            }

        });


        if (mData.size() > 0) {
            SourceDateList = filledDatas(mData);
            Collections.sort(SourceDateList, pinyinComparator);
            adapter = new SortAdapter(this, SourceDateList);
            sortListView.setAdapter(adapter);
        } else {

        }


        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);


        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<SortModel> filledDatas(List<CompanyList> data) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(data.get(i).getCompany_name());

            sortModel.setCompanyId(data.get(i).getId());


            String pinyin = characterParser.getSelling(data.get(i).getCompany_name());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        Log.d(TAG, "filledData: " + mSortList.toString());
        return mSortList;

    }

    /**
     * @param
     * @return
     */
    private List<SortModel> filledData(List<String> data) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(data.get(i));
            if (mCompanyId.size() > 0) {
                sortModel.setCompanyId(mCompanyId.get(i));
            }

            String pinyin = characterParser.getSelling(data.get(i));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        Log.d(TAG, "filledData: " + mSortList.toString());
        return mSortList;

    }

    /**
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_LOAD_SUCCESS:

                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(context, "解析数据失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(context, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context,
                (options1, options2, options3, v) -> {
                    //返回的分别是三个级别的选中位置
                    String tx = options1Items.get(options1).getPickerViewText() +
                            options2Items.get(options1).get(options2) +
                            options3Items.get(options1).get(options2).get(options3);

                    city = options2Items.get(options1).get(options2);
                    selectCity.setText(city);

                    new Thread() {
                        public void run() {
                            getCityCompanyCty(city);

                        }
                    }.start();


                })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(true)// default is true
                .build();

//        pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    private void joinCompany(int userId, int companyId) {
        JoinCompany joinCompany = new JoinCompany(21, 1);

        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTQzODc3MjQsIm5iZiI6MTQ5NDM4NzcyNSwiZXhwIjoxNTI1NDkxNzI1LCJkYXRhIjp7ImlkIjo1MX19.b11u3hIOMu8swf6sVbKJZsYIsk8Zkw1ikXdPj7csLqk";

        mSubscription = mRemoteService.joinCompany(token, joinCompany)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {
//                        Snackbar.make(rootView, "解析错误",
//                                Snackbar.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200) {
                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();


                        } else {
                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}