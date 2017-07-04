package org.eenie.wgj.ui.attendance.signout;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.ui.attendance.AttendanceTestActivity;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eenie on 2017/7/4 at 11:50
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceTestTwoActivity extends BaseActivity {
    private Gson gson = new Gson();
    ArrayList<String> mList = new ArrayList<>();
    ArrayList<String> mLists = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_work;
    }

    @Override
    protected void updateUI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService fileUploadService = retrofit.create(FileUploadService.class);
        Call<ApiResponse> call = fileUploadService.getAttendanceLists(mPrefsHelper.getPrefs().
                        getString(Constants.TOKEN,""),
                new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getResultCode() == 200) {
                    String jsonArray = gson.toJson(response.body().getData());
                    ArrayList<AttendanceListResponse> mData =
                            gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<AttendanceListResponse>>() {
                                    }.getType());
                    if (mData != null) {

                        for (int i = 0; i < mData.size(); i++) {
                            mList.add(mData.get(i).getDay());
                            mLists.add(mData.get(i).getService().
                                    getServicesname());
                        }
                        Intent intent=new Intent(getApplicationContext(), AttendanceTestActivity.class);
                        intent.setAction(".ui.attendance.signout.AttendanceTestTwoActivity");
                        intent .putStringArrayListExtra("list",mList)
                                .putStringArrayListExtra("lists",mLists);
                        startActivity(intent);
                        finish();
                    }

                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }

}
