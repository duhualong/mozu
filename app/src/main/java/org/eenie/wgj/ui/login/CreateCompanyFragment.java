package org.eenie.wgj.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.ui.bean.JsonBean;
import org.eenie.wgj.util.GetJsonDataUtil;
import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;
import static org.eenie.wgj.R.id.et_company_email;

/**
 * Created by Eenie on 2017/5/8 at 14:57
 * Email: 472279981@qq.com
 * Des:
 */

public class CreateCompanyFragment extends BaseFragment {
    @BindView(R.id.root_view)
    View rootView;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static final int REQUEST_CODE = 0x104;

    private boolean isLoaded = false;


    public static final String UID = "uid";
    public static final String TOKEN = "token";
    private String city;
    private int userId;
    private String token;
    @BindView(R.id.tv_select_city)
    TextView selectCity;
    @BindView(R.id.et_company_name)
    EditText inputCompanyName;
    @BindView(R.id.et_company_personal)
    EditText inputCompanyPersonal;
//    @BindView(R.id.et_company_phone)
//    EditText inputCompanyPhone;
    @BindView(et_company_email)
    EditText inputCompanyEmail;
    @BindView(R.id.et_company_address)
    EditText inputCompanyAddress;
    private String mSelectCity;
    @BindView(R.id.et_company_phone)EditText etCompanyPhone;
    @BindView(R.id.et_my_email)EditText etMyEmail;



    @Override
    protected int getContentView() {
        return R.layout.fragment_create_company;
    }

    @Override
    protected void updateUI() {

        //initJsonData();

    }

    public static CreateCompanyFragment newInstance(int userId, String token) {
        CreateCompanyFragment fragment = new CreateCompanyFragment();
        if (!TextUtils.isEmpty(token)) {
            Bundle args = new Bundle();
            args.putInt(UID, userId);
            args.putString(TOKEN, token);
            fragment.setArguments(args);

        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            userId = getArguments().getInt(UID);
            token = getArguments().getString(TOKEN);

        }
        return super.onCreateView(inflater, container, savedInstanceState);
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


    @OnClick({R.id.rl_select_city, R.id.img_back, R.id.btn_next})
    public void onClick(View view) {
        String companyName = inputCompanyName.getText().toString();
        String companyPersonal = inputCompanyPersonal.getText().toString();
//        String companyPhone = inputCompanyPhone.getText().toString();
        String companyEmail = inputCompanyEmail.getText().toString();
        String companyAddress = inputCompanyAddress.getText().toString();
        String companyPhone=etCompanyPhone.getText().toString();
        String myEmail=etMyEmail.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.rl_select_city:
                startActivityForResult(new Intent(context,SelectCityRegisterActivity.class),REQUEST_CODE);
//                if (isLoaded) {
//                    ShowPickerView();
//                }


                break;
            case R.id.btn_next:
                boolean checked = checkInputContent(mSelectCity, companyName, companyPersonal,
                        companyEmail, companyAddress);
                if (checked) {
                    fragmentMgr.beginTransaction()
                            .addToBackStack(TAG)
                            .replace(R.id.fragment_login_container,
                                    CreateCompanySecondFragment.newInstance(userId, token, city,
                                            companyName, companyPersonal, "",
                                            companyEmail, companyAddress)).commit();

                }


                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 4) {
            if (requestCode == REQUEST_CODE) {
                mSelectCity = data.getStringExtra("city");
                if (!TextUtils.isEmpty(mSelectCity)) {
                    selectCity.setText(mSelectCity);
                }
            }
        }

    }

    private boolean checkInputContent(String city, String companyName, String companyPersonal,
                                      String companyEmail, String companyAddress) {
        boolean result = true;
        if (TextUtils.isEmpty(city)) {
            Snackbar.make(rootView, "请选择公司所在的城市", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(companyName)) {
            Snackbar.make(rootView, "请输入公司名称", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(companyPersonal)) {
            Snackbar.make(rootView, "请输入联系人姓名", Snackbar.LENGTH_LONG).show();
            result = false;
        }

        if (result && TextUtils.isEmpty(companyEmail)) {
            Snackbar.make(rootView, "请输入邮箱", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(companyAddress)) {
            Snackbar.make(rootView, "请输入公司地址", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        return result;

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

}
