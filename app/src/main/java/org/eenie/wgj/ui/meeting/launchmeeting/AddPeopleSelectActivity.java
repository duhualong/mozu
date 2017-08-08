package org.eenie.wgj.ui.meeting.launchmeeting;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.MeetingPeopleNew;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/10 at 16:57
 * Email: 472279981@qq.com
 * Des:
 */

public class AddPeopleSelectActivity extends BaseActivity {

    @BindView(R.id.expand_list)
    ExpandableListView mExpandableListView;
    private SelectPeopleRecordAdapter mAdapter;
    private ArrayList<MeetingPeopleNew> data = new ArrayList<>();
    @BindView(R.id.checkbox_select_all)CheckBox mCheckBoxAll;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_meeting_people;
    }

    @Override
    protected void updateUI() {
        mExpandableListView.setOnChildClickListener(mAdapter);
        onRefresh();

    }

    @OnClick({R.id.img_back, R.id.submit_tv,R.id.rl_item})
    public void onClick(View view) {
        int number = 0;
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.submit_tv:
                ArrayList<MeetingPeopleNew.UserBean>arrayList=new ArrayList<>();
                if (data!=null) {
                    for (int i = 0; i < data.size(); i++) {
                        for (int j = 0; j < data.get(i).getUsers().size(); j++) {
                            if (data.get(i).getUsers().get(j).isCheck()) {
                                if (arrayList.size()>0){
                                    boolean check=true;
                                    for (int k=0;k<arrayList.size();k++){
                                        if (data.get(i).getUsers().get(j).getId()==arrayList.get(k).getId()){
                                            check=false;

                                        }

                                    }
                                    if (check){
                                        arrayList.add(data.get(i).getUsers().get(j));
                                    }
                                }else {
                                    arrayList.add(data.get(i).getUsers().get(j));
                                }

                            }
                        }
                    }
                }

                if (arrayList.size()>0){



                    Intent mIntent = new Intent();
                        mIntent.putParcelableArrayListExtra("mData", arrayList);
                        // 设置结果，并进行传送
                        setResult(RESULT_OK, mIntent);
                        finish();
                }else {
                    Toast.makeText(context, "请选择参会人员", Toast.LENGTH_SHORT).show();

                }

                break;

            case R.id.rl_item:
                if (!mCheckBoxAll.isChecked()) {
                    mCheckBoxAll.setChecked(true);
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setChecked(true);
                            for (int j = 0; j < data.get(i).getUsers().size(); j++) {
                                data.get(i).getUsers().get(j).setCheck(true);
                            }
                        }
                    }
                } else {
                    mCheckBoxAll.setChecked(false);
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setChecked(false);
                            for (int j = 0; j < data.get(i).getUsers().size(); j++) {
                                data.get(i).getUsers().get(j).setCheck(false);
                            }
                        }
                    }

                }
                mAdapter.notifyDataSetChanged();
                break;

        }
    }


    public void onRefresh() {

        mSubscription = mRemoteService.
                getMeetingPeopleList(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                   @Override
                   public void onNext(ApiResponse apiResponse) {
                       if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                           Gson gson = new Gson();
                           String jsonArray = gson.toJson(apiResponse.getData());
                           data = gson.fromJson(jsonArray,
                                   new TypeToken<ArrayList<MeetingPeopleNew>>() {

                                   }.getType());
                           if (data != null) {
                             mAdapter=new SelectPeopleRecordAdapter(context,data);
                               mExpandableListView.setAdapter(mAdapter);
                           }
                       } else {
                           Toast.makeText(context, apiResponse.getResultMessage(),
                                   Toast.LENGTH_SHORT).show();
                       }
                   }
               });


    }





}