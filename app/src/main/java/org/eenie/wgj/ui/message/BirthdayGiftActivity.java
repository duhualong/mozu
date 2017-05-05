package org.eenie.wgj.ui.message;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import org.eenie.wgj.R;
import org.eenie.wgj.adapter.BirthdayGiftAdapter;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.BirthdayGift;
import org.eenie.wgj.model.requset.GiveBirthday;
import org.eenie.wgj.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/5 at 10:55
 * Email: 472279981@qq.com
 * Des:
 */

public class BirthdayGiftActivity extends BaseActivity {
    public static final String BIRTHDAY_ID = "birthday_id";
    public static final String BIRTHDAY_NAME = "birthday_name";
    public static final String BIRTHDAY_AVATAR = "birthday_avatar";
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.rv_gift_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.img_avatar)
    CircleImageView avatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.edit_gift)
    EditText editGift;
    private String mBirthdayId;
    private String cake;
    List<BirthdayGift> mBirthdayGifts = new ArrayList<>();
    BirthdayGiftAdapter mBirthdayGiftAdapter;


    @Override
    protected int getContentView() {
        return R.layout.activity_gift_send;
    }

    @Override
    protected void updateUI() {
        mBirthdayId = getIntent().getStringExtra(BIRTHDAY_ID);
        String birthdayAvatar = getIntent().getStringExtra(BIRTHDAY_AVATAR);
        String birthdayName = getIntent().getStringExtra(BIRTHDAY_NAME);
        if (!TextUtils.isEmpty(mBirthdayId) && !TextUtils.isEmpty(birthdayName) &&
                !TextUtils.isEmpty(birthdayAvatar)) {
            Glide.with(context)
                    .load(birthdayAvatar)
                    .centerCrop()
                    .into(avatar);
            tvName.setText(birthdayName);


        }

        for (int i = 0; i < 200; i++) {
            mBirthdayGifts.add(new BirthdayGift(4001, R.mipmap.ic_gift_case1, "巧克力蛋糕"));
            mBirthdayGifts.add(new BirthdayGift(4002, R.mipmap.ic_gift_case2, "草莓蛋糕"));
            mBirthdayGifts.add(new BirthdayGift(4003, R.mipmap.ic_gift_case3, "奶油蛋糕"));
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int pos = mRecyclerView.getChildAdapterPosition(centerView());
                mBirthdayGiftAdapter.setSelected(pos);
                mBirthdayGiftAdapter.notifyDataSetChanged();

            }
        });

        mBirthdayGiftAdapter = new BirthdayGiftAdapter(mBirthdayGifts) {
            @Override
            public void onItemClick(BaseViewHolder holder, BirthdayGift entity) {
                int pos = mRecyclerView.getChildAdapterPosition(centerView());
                int cur = holder.getPosition();
                if (cur < pos) {
                    mRecyclerView.smoothScrollToPosition(cur > 0 ? cur - 1 : 0);
                } else if (cur == pos) {

                } else if (cur > pos) {
                    mRecyclerView.smoothScrollToPosition(cur + 1);
                }
                mBirthdayGiftAdapter.notifyDataSetChanged();
            }
        };

        mRecyclerView.setAdapter(mBirthdayGiftAdapter);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        mBirthdayGiftAdapter.setEnableLoadMore(false);

        mBirthdayGiftAdapter.setSelected(mBirthdayGifts.size() / 2);
        mRecyclerView.scrollToPosition(mBirthdayGifts.size() / 2);

        SnapHelper snapHelperCenter = new LinearSnapHelper();
        snapHelperCenter.attachToRecyclerView(mRecyclerView);

    }
    private View centerView() {
        int w = mRecyclerView.getMeasuredWidth() / 6 * 3;
        int h = mRecyclerView.getMeasuredHeight() / 2;
        return mRecyclerView.findChildViewUnder(w, h);
    }

    @OnClick({R.id.img_back, R.id.btn_ok})
    public void onClick(View view) {
        String blessing = editGift.getText().toString();
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.btn_ok:
                giveBirthdayBlessing(mBirthdayId, blessing);
                break;

        }
    }

    private void giveBirthdayBlessing(String birthdayId, String blessing) {
        BirthdayGift gift = mBirthdayGiftAdapter.getSelectedEntity();
        GiveBirthday birthdayGift=new GiveBirthday(birthdayId,blessing,gift.getId()+"");
        mSubscription=mRemoteService.giveBirthdayBlessing(Constant.TOKEN,birthdayGift)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse value) {
                        if (value.getResultCode()==200){
                            Snackbar.make(rootView,"成功",Snackbar.LENGTH_SHORT).show();
                        }else {
                            Snackbar.make(rootView,value.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });

    }
}
