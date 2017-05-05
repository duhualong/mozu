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

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.requset.MeetingNotice;
import org.eenie.wgj.model.requset.MessageDetail;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Single;
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
    private MeetingNotice data;
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


    @Override
    protected int getContentView() {
        return R.layout.activity_meeting_apply_detail;
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(APPLY_INFO);
        if (data != null) {
            switch (data.getCheckstatus()) {
                case 2:
                    rlApplyPersonal.setVisibility(View.GONE);
                    rlApplyStatus.setVisibility(View.GONE);
                    lyApplyCherckBox.setVisibility(View.VISIBLE);
                    rlApplyReason.setVisibility(View.VISIBLE);
                    tvApplyOk.setVisibility(View.VISIBLE);
                    controlKeyboardLayout(mScrollView, ApplyFeedBackActivity.this);
                    break;
                case 3:
                    applyResult.setText("拒绝");
                    applyResult.setTextColor(ContextCompat.
                            getColor(context, R.color.text_red));
                    applyPersonal.setText("拒绝");
                    applyPersonal.setTextColor(ContextCompat.
                            getColor(context, R.color.text_red));
                    break;
            }
            if (!TextUtils.isEmpty(data.getName())) {
                meetingTitle.setText(data.getName());

            }
            if (!TextUtils.isEmpty(data.getUsername())) {
                applyName.setText(data.getUsername());
            }
            if (!TextUtils.isEmpty(data.getRoom_name())) {
                meetingAddress.setText(data.getRoom_name());
            }
            if (!TextUtils.isEmpty(data.getStart()) && !TextUtils.isEmpty(data.getEnd())) {
                meetingTime.setText(data.getStart() + "至\n" + data.getEnd());
            }

            getMessageById(data.getId());
        }


    }

    private void getMessageById(int id) {
        String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");


        mSubscription=mRemoteService.getMessageById(Constant.TOKEN,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(listApiResponse -> {
                    MessageDetail mData = null;
                    if (listApiResponse.getResultCode() == 200) {
                         mData = listApiResponse.getData();
                    }
                        return Single.just(mData);
                })
                .subscribe(meeting -> {
                    if (meeting != null) {
                        meetingGoal.setText(meeting.getDetail());
                        applyReason.setText(meeting.getCheck_feedback());
                        String url="http://118.178.88.132:8000"+meeting.getAvatar();
                        Glide.with(context)
                                .load(url)
                                .centerCrop()
                                .into(imgAvatar);

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


                break;
            case R.id.ly_check_agree:

                break;
            case R.id.ly_check_refuse:


                break;
        }
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
