package org.eenie.wgj.ui.camera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;

import org.eenie.wgj.R;

/**
 * Created by Eenie on 2017/7/19 at 16:09
 * Email: 472279981@qq.com
 * Des:
 */

public class TakeCameraTestActivity extends AppCompatActivity {

    private Button button;
    private CameraSurfaceView mCameraSurfaceView;
    boolean checked = false;
    //    private RectOnCamera rectOnCamera;
    private String mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 全屏显示
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test_camera);

        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
        button = (Button) findViewById(R.id.takePic);

        button.setOnClickListener(view -> {
            mCameraSurfaceView.takePicture();
            SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
            mFile = sp.getString("fileurl", "");
            if (!TextUtils.isEmpty(mFile)) {
                Intent intent = new Intent();
                intent.putExtra("test", mFile);
                setResult(RESULT_OK, intent);
                System.out.println("sss:" + mFile);
                finish();
            } else {


            }

        });



    }


}
