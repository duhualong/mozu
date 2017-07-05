package org.eenie.wgj.ui.reportpost;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.routing.RoutingContentResponse;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.PermissionManager;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eenie on 2017/7/5 at 10:12
 * Email: 472279981@qq.com
 * Des:
 */

public class UploadReportPostInfoActivity extends BaseActivity implements AMapLocationListener {
    public static final String POST_ID = "post_id";
    public static final String ID = "id";
    private static final int REQUEST_LOCATION_CODE = 0x101;
    public static final String PATH = "path";
    public static final String POST = "post";
    public static final String TIME = "time";
    public static final String CONTENT = "content";
    public static final String POSITION = "position";
    private String position;
    private String time;
    private String post;
    private String path;
    private String content;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_report_time)
    TextView tvReportTime;
    @BindView(R.id.tv_post_name)
    TextView tvPostName;
    @BindView(R.id.img_report_post)
    ImageView imgPost;
    @BindView(R.id.rl_img)
    RelativeLayout rlImg;
    @BindView(R.id.rl_recycler_view)
    RelativeLayout rlRecycler;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;
    private String mAddress;
    private double mLat;
    private double mLong;
    private String postId;
    private String id;
    private AddContentAdapter mAddContentAdapter;
    ArrayList<RoutingContentResponse> b = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_upload_point_info;
    }

    @Override
    protected void updateUI() {
        position = getIntent().getStringExtra(POSITION);
        time = getIntent().getStringExtra(TIME);
        post = getIntent().getStringExtra(POST);
        path = getIntent().getStringExtra(PATH);
        content = getIntent().getStringExtra(CONTENT);
        postId = getIntent().getStringExtra(POST_ID);
        id = getIntent().getStringExtra(ID);


        checkLocationPermission();

        if (!TextUtils.isEmpty(position)) {
            if (position.length() <= 1 && Integer.parseInt(position) <= 9) {
                tvTitle.setText("点位0" + position);
            } else {
                tvTitle.setText("点位" + position);

            }

        }
        if (!TextUtils.isEmpty(time)) {
            tvReportTime.setText(time);
        } else {
            tvReportTime.setText("无");
        }
        if (!TextUtils.isEmpty(post)) {
            tvPostName.setText(post);
        } else {
            tvPostName.setText("无");

        }
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            Glide.with(context).load(file).centerCrop().into(imgPost);
        } else {
            rlImg.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(content)) {

            String[] strings = content.split("\n");
            for (int i = 0; i < strings.length; i++) {
                b.add(new RoutingContentResponse(strings[i], false));
            }
            mAddContentAdapter = new AddContentAdapter(context, b);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAddContentAdapter);

        } else {
            rlRecycler.setVisibility(View.GONE);
        }


    }

    @OnClick({R.id.img_back, R.id.img_report_post, R.id.btn_apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_report_post:
                startActivity(new Intent(context, GallerysActivity.class)
                        .putExtra(GallerysActivity.EXTRA_IMAGE_URI, path));
                break;
            case R.id.btn_apply:
                if (TextUtils.isEmpty(mAddress) || mLat == 0 || mLong == 0) {
                    Toast.makeText(context, "请打开GPS，允许定位", Toast.LENGTH_SHORT).show();
                    mlocationClient.startLocation();
                } else {
                    boolean checked = true;
                    if (!b.isEmpty() && b.size() > 0) {
                        for (int g = 0; g < b.size(); g++) {
                            if (!b.get(g).isSelect()) {
                                checked = false;
                            }
                        }
                    }
                    if (checked) {

                        addData( getMultipartBody(path,postId, String.valueOf(mLong),
                                String.valueOf(mLat),time, id,mAddress,"1"),
                                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""));


                    } else {
                        Toast.makeText(context, "请勾选所有的报岗内容", Toast.LENGTH_SHORT).show();

                    }
                }


                break;
        }
    }

    public  MultipartBody getMultipartBody(String path, String postId, String longitude,
                                                 String latitude, String time, String id, String address,
                                                 String completed) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                new File(path));
        builder.addFormDataPart("image", new File(path).getName(),requestBody);

        builder.addFormDataPart("postsettingid", postId);
        builder.addFormDataPart("longitude", longitude);
        builder.addFormDataPart("latitude", latitude);
        builder.addFormDataPart("time", time);
        builder.addFormDataPart("id", id);
        builder.addFormDataPart("location_name", address);
        builder.addFormDataPart("completed", completed);
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }


    private void addData(RequestBody body, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        Call<ApiResponse> call = userBiz.uploadReportPostInfo(token, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getResultCode() == 200) {
                    Toast.makeText(context,"报岗成功！",Toast.LENGTH_SHORT).show();
                  finish();

                } else {
                    Toast.makeText(context, response.body().getResultMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public  File compressior(File file) {
        return new Compressor.Builder(context)
                .setMaxWidth(900)
                .setMaxHeight(900)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
    }


    private void initMapView() {
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();
    }

    /**
     * 检查是否有定位权限
     */
    private void checkLocationPermission() {
        if (PermissionManager.checkLocation(this)) {
            //定位
            initMapView();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v -> {
                            PermissionManager.requestLocation(this, REQUEST_LOCATION_CODE);
                        });
            } else {
                PermissionManager.requestLocation(this, REQUEST_LOCATION_CODE);
            }
        }
    }

    // 请求权限后的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])
                    && PackageManager.PERMISSION_GRANTED == grantResults[0]) { // 请求定位权限被允许
                initMapView();

            } else { // 请求定位权限被拒绝
                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            //定位成功回调信息，设置相关消息
            mAddress = aMapLocation.getAddress();
            mLat = aMapLocation.getLatitude();
            mLong = aMapLocation.getLongitude();

            System.out.println("address" + mAddress + "\n" + "经度" + mLong + "\n纬度：" + mLat);


        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }

    }

    class AddContentAdapter extends RecyclerView.Adapter<AddContentAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<RoutingContentResponse> mList;

        public AddContentAdapter(Context context, ArrayList<RoutingContentResponse> mList) {
            this.context = context;
            this.mList = mList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_recycler_upload_routing_content, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mList != null && !mList.isEmpty()) {
                RoutingContentResponse data = mList.get(position);
                holder.setItem(data);

                if (data != null) {
                    if (data.getContent() != null) {
                        holder.itemName.setText(data.getContent());
                    }
                    if (data.isSelect()) {
                        holder.rlItem.setChecked(true);
                    } else {
                        holder.rlItem.setChecked(false);
                    }
                }
            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void addAll(ArrayList<RoutingContentResponse> projectList) {
            this.mList.addAll(projectList);
            AddContentAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mList.clear();
            AddContentAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private RoutingContentResponse mRoutingContentResponses;
            private TextView itemName;
            private CheckBox rlItem;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemName = ButterKnife.findById(itemView, R.id.item_content_routing);
                rlItem = ButterKnife.findById(itemView, R.id.checkbox_select_content);
                rlItem.setOnClickListener(this);


            }

            public void setItem(RoutingContentResponse mRoutingContentResponse) {
                mRoutingContentResponses = mRoutingContentResponse;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.checkbox_select_content:
                        if (rlItem.isChecked()) {
                            mRoutingContentResponses.setSelect(true);
                        } else {
                            mRoutingContentResponses.setSelect(false);
                        }
                        notifyDataSetChanged();
                        break;

                }


            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.onDestroy(); // 销毁定位客户端
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mlocationClient != null) {
            mlocationClient.stopLocation(); // 停止定位
        }
    }

}
