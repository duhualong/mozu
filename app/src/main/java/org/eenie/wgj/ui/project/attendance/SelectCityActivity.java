package org.eenie.wgj.ui.project.attendance;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.search.CharacterParser;
import org.eenie.wgj.search.ClearEditText;
import org.eenie.wgj.search.PinyinComparator;
import org.eenie.wgj.search.SideBar;
import org.eenie.wgj.search.SortModel;
import org.eenie.wgj.ui.bean.JsonBean;
import org.eenie.wgj.util.GetJsonDataUtil;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/22 at 16:42
 * Email: 472279981@qq.com
 * Des:
 */

public class SelectCityActivity extends BaseActivity {
    @BindView(R.id.root_view)
    View rootView;
    private static final int MSG_LOAD_SUCCESS = 0x101;
    private static final int MSG_LOAD_FAILED = 0x102;
    private boolean isLoaded = false;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private CityAdapter adapter;
    private List<SortModel> SourceDateList;
    @BindView(R.id.fragment_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.country_lvcountry)
    ListView sortListView;
    @BindView(R.id.sidrbar)
    SideBar sideBar;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.filter_edit)
    ClearEditText mClearEditText;
    @BindView(R.id.tv_location)
    TextView tvCity;
    private ArrayList<String> mCityList = new ArrayList<>();
    public static final String CITY = "city";
    private String city;


    @Override
    protected int getContentView() {
        return R.layout.activity_select_city;
    }

    @Override
    protected void updateUI() {

        initJsonData();
        city = getIntent().getStringExtra(CITY);
        if (!TextUtils.isEmpty(city)) {
            tvCity.setText(city);

        }


    }

    private void initJsonData() {
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


        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            //该省的城市列表（第二级）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                mCityList.add(CityName);//添加城市


            }
        }

        /**
         * 添加城市数据
         */

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
        mFrameLayout.setVisibility(View.VISIBLE);
        initViews(mCityList);

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

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    // Toast.makeText(context, "解析数据成功", Toast.LENGTH_SHORT).show();

                    break;
                case MSG_LOAD_FAILED:
                    Toast.makeText(context, "暂无城市", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }


    private void initViews(List<String> mData) {
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
            adapter = new CityAdapter(context, SourceDateList);
            sortListView.setAdapter(adapter);
        } else {

        }


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
        sortListView.setOnItemClickListener((parent, view, position, id) -> {
            city = ((SortModel) adapter.getItem(position)).getName();
            Intent mIntent = new Intent();
            mIntent.putExtra("city", city);
            // 设置结果，并进行传送
            setResult(4, mIntent);
            SelectCityActivity.this.finish();
        });
    }

    /**
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

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

    private List<SortModel> filledDatas(List<String> data) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(data.get(i));

            String pinyin = characterParser.getSelling(data.get(i));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }

        return mSortList;

    }

}
