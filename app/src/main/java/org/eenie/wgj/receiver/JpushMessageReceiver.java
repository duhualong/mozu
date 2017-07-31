package org.eenie.wgj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.common.LogUtil;
import com.google.gson.Gson;

import org.eenie.wgj.model.requset.NoticeMessage;

import cn.jpush.android.api.JPushInterface;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/5/16 at 11:32
 * Email: 472279981@qq.com
 * Des:
 */

public class JpushMessageReceiver  extends BroadcastReceiver {
    public JpushMessageReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());


        if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            //收到通知栏消息
            receivingNotification(intent.getExtras(),context);
            Log.d(TAG, "handleMessage: "+intent.getExtras().toString());

        } else if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
            //收到自定义消息
            handleMessage(intent.getStringExtra(JPushInterface.EXTRA_MESSAGE));
        }

    }


    private void handleMessage(String extra_msg) {
        try {
            Uri uri = Uri.parse(extra_msg);
            Log.d(TAG, "handleMessage: "+uri.toString());
//            if (uri.getScheme().equals("meet") && uri.getAuthority().equals("checkin")) {
//               // EventBus.getDefault().post(new MeetCheckInEvent());
//            } else {
//                LogUtil.e("消息不符合规范 = " + uri.toString());
//            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("消息不符合规范 = " + extra_msg);
        }
    }

    private void receivingNotification(Bundle bundle,Context context) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!TextUtils.isEmpty(extras)){
            Intent mIntent = new Intent(context, NoticeMessage.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
        System.out.println("title:"+title+"\n"+"message:"+message+"\n"+"extra"+extras);
        Log.d(TAG, "receivingNotification: "+ new Gson().toJson(extras)+
                "\n,message::"+ new Gson().toJson(message)+"\n,“title”"+new Gson().toJson(title));

//        SampleNotice notice = new SampleNotice();
//
//
//        try {
//            if (!TextUtils.isEmpty(extras)) {
//                JSONObject key = new JSONObject(extras);
//                if (key.has("key")) {
//                    notice.setKey(key.getInt("key"));
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        notice.setTime(System.currentTimeMillis());
//        notice.setTitle(title);
//        notice.setContent(message);
//        notice.setReaded(false);
//
//        storageMsg(notice);
    }

//
//    private void storageMsg(final SampleNotice notice) {
//        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.insertOrUpdate(notice);
//                EventBus.getDefault().post(new NoticeStateChange());
//            }
//        });
//
//
//    }


}
