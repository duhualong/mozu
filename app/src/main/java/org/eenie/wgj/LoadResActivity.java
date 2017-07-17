//package org.eenie.wgj;
//
//import android.app.Activity;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.multidex.MultiDex;
//import android.view.Window;
//import android.view.WindowManager;
//
///**
// * Created by Eenie on 2017/7/16 at 17:18
// * Email: 472279981@qq.com
// * Des:
// */
//
//public class LoadResActivity extends Activity {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super .onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );
//        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
//        setContentView(R.layout.activity_splish_pager);
//        new LoadDexTask().execute();
//    }
//    class LoadDexTask extends AsyncTask {
//        @Override
//        protected Object doInBackground(Object[] params) {
//            try {
//                MultiDex.install(getApplication());
//                ((App) getApplication()).installFinish(getApplication());
//            } catch (Exception e) {
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Object o) {
//            finish();
//            System.exit( 0);
//        }
//    }
//    @Override
//    public void onBackPressed() {
//        //cannot backpress
//    }
//
//
//
//}
