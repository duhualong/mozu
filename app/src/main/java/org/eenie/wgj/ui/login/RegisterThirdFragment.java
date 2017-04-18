package org.eenie.wgj.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.Token;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eenie on 2017/4/18 at 17:26
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterThirdFragment extends BaseFragment {
    @BindView(R.id.tv_height)TextView mHeight;
    @BindView(R.id.tv_qualifications)TextView mQualifications;
    @BindView(R.id.tv_marry_state)TextView mMarryState;
    @BindView(R.id.root_view)View rootView;
    @BindView(R.id.tv_address)TextView mAddressNow;
    @BindView(R.id.tv_contacts)TextView mContacts;
    @BindView(R.id.tv_work_name)TextView mWorkName;
    @BindView(R.id.tv_industry)TextView mIndustry;
    @BindView(R.id.tv_skill)TextView mSkill;
    @BindView(R.id.tv_employment)TextView emEmployment;



    @Override
    protected int getContentView() {
        return R.layout.fragment_register_third;
    }

    @Override
    protected void updateUI() {




    }

    public static RegisterThirdFragment newInstance(String phone,String password,String name) {

        Bundle args = new Bundle();

        RegisterThirdFragment fragment = new RegisterThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @OnClick({R.id.img_back,R.id.btn_apply})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();

                break;

            case R.id.btn_apply:
                //getData();

                break;
        }
    }

    private void getData(File file1,File file2,File file3) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://netocr.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", "1881772456")
                .addFormDataPart("password", "123456")
                .addFormDataPart("name", "杜华龙")
                .addFormDataPart("gender", "男")
                .addFormDataPart("birthday", "1992-03-05")
                .addFormDataPart("address", "上海市浦东新区")
                .addFormDataPart("number","411721199203053436")
                .addFormDataPart("publisher","上海市公安局")
                .addFormDataPart("validate","2013:02:20—2023:02:20")
                .addFormDataPart("id_card_positive","b.jpg", RequestBody.create(MediaType.parse("image/jpg"), file1))
                .addFormDataPart("id_card_negative","c.jpg", RequestBody.create(MediaType.parse("image/jpg"), file2))
                .addFormDataPart("id_card_head_image","d.jpg", RequestBody.create(MediaType.parse("image/jpg"), file3))
                .addFormDataPart("height","170cm")
                .addFormDataPart("graduate","本科")
                .addFormDataPart("telephone","18817772456")
                .addFormDataPart("emergency_contact","sssssddd")
                .addFormDataPart("industry","ssss")
                .addFormDataPart("skill","ssss")
                .addFormDataPart("channel","sss")
                .build();
        Call<ApiResponse<Token>> call = userBiz.applyInformation(requestBody);
        call.enqueue(new Callback<ApiResponse<Token>>() {
            @Override
            public void onResponse(Call<ApiResponse<Token>> call, Response<ApiResponse<Token>> response) {
                System.out.println("code"+response.body().getResultCode());
                if (response.body().getResultCode()==200){
                    System.out.println("测试注册:");
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<Token>> call, Throwable t) {

            }
        });
    }
}
