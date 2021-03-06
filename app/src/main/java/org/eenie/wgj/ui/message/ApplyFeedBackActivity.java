package org.eenie.wgj.ui.message;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.MessageDetail;
import org.eenie.wgj.model.response.meeting.AuditMeetingRequest;
import org.eenie.wgj.model.response.message.MessageRequestData;
import org.eenie.wgj.model.response.message.MessageStatus;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/4 at 14:21
 * Email: 472279981@qq.com
 * Des:
 */

public class ApplyFeedBackActivity extends BaseActivity {
    @BindView(R.id.root_view)View rootView;
    public static final String APPLY_INFO = "apply_info";
    private MessageRequestData.DataBean mData;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_apply_ok)
    TextView tvApplyOk;
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.apply_name)
    TextView applyName;
    @BindView(R.id.item_meeting_name_content)
    TextView meetingTitle;
    @BindView(R.id.item_meeting_time_content)
    TextView meetingTime;
    @BindView(R.id.item_meeting_address_content)
    TextView meetingAddress;
    @BindView(R.id.item_meeting_goal_content)
    TextView meetingGoal;
    @BindView(R.id.rl_apply_status)
    RelativeLayout rlApplyStatus;
    @BindView(R.id.apply_result)
    TextView applyResult;
    @BindView(R.id.rl_apply_personal)
    RelativeLayout rlApplyPersonal;
    @BindView(R.id.img_setting)
    CircleImageView imgAvatarApply;
    @BindView(R.id.item_apply_information)
    TextView applyReason;
    @BindView(R.id.apply_personal)
    TextView applyPersonal;
    @BindView(R.id.rl_apply_reason)
    RelativeLayout rlApplyReason;
    @BindView(R.id.et_apply_reason)
    EditText etReason;
    @BindView(R.id.rl_apply_checkbox)
    LinearLayout lyApplyCherckBox;
    @BindView(R.id.checkbox_refuse)
    ImageView imgRefuse;
    @BindView(R.id.checkbox_agree)
    ImageView imgAgree;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    int type;


    @Override
    protected int getContentView() {
        return R.layout.activity_meeting_apply_detail;
    }

    @Override
    protected void updateUI() {
        mData = getIntent().getParcelableExtra(APPLY_INFO);
        if (mData.getParameter()!=null){
            getMessageById(mData.getParameter().getId());
        }
        changeStatus(mData.getId());


    }

    private void changeStatus( int id) {
        MessageStatus request=new MessageStatus(1,id);
        mSubscription=mRemoteService.changeMessageStatus(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),request)
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
                    public void onNext(ApiResponse apiResponse) {

                    }
                });
    }

    private void getMessageById(int id) {
        String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");


        mSubscription=mRemoteService.getMessageById(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(listApiResponse -> {
                    MessageDetail mData = null;
                    if (listApiResponse.getCode() == 0) {
                        Gson gson=new Gson();
                        String jsonArray= gson.toJson(listApiResponse.getData());
                       mData = gson.fromJson(jsonArray,
                                new TypeToken<MessageDetail>() {
                                }.getType());


                    }
                        return Single.just(mData);
                })
                .subscribe(meeting -> {
                    if (meeting != null) {


                            switch (meeting.getCheckstatus()) {
                                case 2:
                                    rlApplyPersonal.setVisibility(View.GONE);
                                    rlApplyStatus.setVisibility(View.GONE);
                                    lyApplyCherckBox.setVisibility(View.VISIBLE);
                                    rlApplyReason.setVisibility(View.VISIBLE);
                                    tvApplyOk.setVisibility(View.VISIBLE);
                                    controlKeyboardLayout(mScrollView, ApplyFeedBackActivity.this);
                                    break;
                                case 3:
                                    applyResult.setText(meeting.getCheckstatus_name());
                                    applyPersonal.setText(meeting.getCheckstatus_name());
                                    applyResult.setTextColor(ContextCompat.
                                            getColor(context, R.color.text_red));
                                    applyPersonal.setTextColor(ContextCompat.
                                            getColor(context, R.color.text_red));
                                    break;
                                case 1:
                                    applyResult.setText(meeting.getCheckstatus_name());
                                    applyPersonal.setText(meeting.getCheckstatus_name());

                                    break;
                            }
                            if (!TextUtils.isEmpty(meeting.getName())) {
                                meetingTitle.setText(meeting.getName());

                            }
                            if (!TextUtils.isEmpty(meeting.getUsername())) {
                                applyName.setText(meeting.getUsername());
                            }
                            if (!TextUtils.isEmpty(meeting.getRoom_name())) {
                                meetingAddress.setText(meeting.getRoom_name());
                            }
                            if (!TextUtils.isEmpty(meeting.getStart()) && !TextUtils.isEmpty(meeting.getEnd())) {
                                meetingTime.setText(meeting.getStart() + "至\n" + meeting.getEnd());
                            }


                        meetingGoal.setText(meeting.getDetail());
                        applyReason.setText(meeting.getCheck_feedback());
                        if (!TextUtils.isEmpty(meeting.getAvatar())){
                            String url=Constant.DOMIN+meeting.getAvatar();
                            Glide.with(context)
                                    .load(url)
                                    .centerCrop()
                                    .into(imgAvatar);
                        }

                        if (!TextUtils.isEmpty(meeting.getOperator_avatar())){
                            String urls=Constant.DOMIN+meeting.getOperator_avatar();
                            Glide.with(context)
                                    .load(urls)
                                    .centerCrop()
                                    .into(imgAvatarApply);
                        }


                    }
                });
//        mSubscription=mRemoteService.getMessageById(Constant.TOKEN,id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ApiResponse>() {
//                    @Override
//                    public void onCompleted() {
//                       // Snackbar.make(rootView,"网络请求错误",Snackbar.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                       // Snackbar.make(rootView,"请求错误",Snackbar.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onNext(ApiResponse messageDetailApiResponse) {
//                        if (messageDetailApiResponse.getResultCode()==200){
//                            System.out.println("打印"+messageDetailApiResponse.getResultMessage());
//                           // MessageDetail mData=messageDetailApiResponse.getData();
//                            Gson gson = new Gson();
//                            Type objectType = new TypeToken<MessageDetail>() {
//                            }.getType();
//                            MessageDetail mData = gson.fromJson(messageDetailApiResponse.getData().toString(), objectType);
//
//                            if (mData!=null){
//                                meetingGoal.setText(mData.getDetail());
//                                applyReason.setText(mData.getCheck_feedback());
//
//                               // imgAvatar.setImageURI();
//
//                            }
//
//
//                        }else {
//
//                        }
//
//                    }
//                });
    }

    @OnClick({R.id.img_back, R.id.tv_apply_ok, R.id.ly_check_agree, R.id.ly_check_refuse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_apply_ok:
                if (!TextUtils.isEmpty(etReason.getText().toString())){
                    if (type==0){
                        Toast.makeText(context,"请选择审核状态",Toast.LENGTH_SHORT).show();
                    }else {
                        applyMeeting(etReason.getText().toString(),type,mData.getParameter().getId());
                    }

                }else {
                    Toast.makeText(context,"请输入处理原因",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.ly_check_agree:
                type=1;
                imgAgree.setImageResource(R.mipmap.ic_apply_true);
                imgRefuse.setImageResource(R.mipmap.ic_apply_false);


                break;
            case R.id.ly_check_refuse:
                type=3;
                imgAgree.setImageResource(R.mipmap.ic_apply_false);
                imgRefuse.setImageResource(R.mipmap.ic_apply_true);


                break;
        }
    }

    private void applyMeeting(String cause, int type, int id) {
        AuditMeetingRequest request=new AuditMeetingRequest(cause,type,id);
        mSubscription=mRemoteService.auditMeeting(mPrefsHelper.getPrefs().
                        getString(Constants.TOKEN,""), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getCode()==0){
                            Toast.makeText(context,apiResponse.getMessage(),Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(context,apiResponse.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void controlKeyboardLayout(final ScrollView root, final Activity context) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {


            Rect rect = new Rect();
            root.getWindowVisibleDisplayFrame(rect);
            int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
            //若不可视区域高度大于100，则键盘显示
            if (rootInvisibleHeight > 80) {
                int[] location = new int[2];
                View focus = context.getCurrentFocus();
                if (focus != null) {
                    focus.getLocationInWindow(location);
                    int scrollHeight = (location[1] + focus.getHeight()) - rect.bottom;
                    if (rect.bottom < location[1] + focus.getHeight()) {
                        root.scrollTo(0, scrollHeight);
                    }
                }
            } else {
                //键盘隐藏
                root.scrollTo(0, 0);
            }
        });
    }
}
