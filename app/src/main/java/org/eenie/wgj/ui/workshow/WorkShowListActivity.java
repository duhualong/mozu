package org.eenie.wgj.ui.workshow;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.model.response.WorkShowList;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/9 at 15:57
 * Email: 472279981@qq.com
 * Des:
 */

public class WorkShowListActivity extends BaseActivity {

    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.rl_first_img)
    RelativeLayout rlFirstImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.img_crown)
    ImageView imgCrown;
    @BindView(R.id.img_avatar)
    CircleImageView avatar;
    @BindView(R.id.recycler_my_show)RecyclerView myRecyclerView;
    @BindView(R.id.recycler_other_show)RecyclerView otherRecyclerView;
    @BindView(R.id.img_background)ImageView imgBackground;
    private String avatarUrl;
    private String name;
    private String token;

    private WorkShowAdapter myAdapter;
    private WorkShowAdapter otherAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_work_show_setting;
    }

    @Override
    protected void updateUI() {
        token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        myAdapter=new WorkShowAdapter(context,new ArrayList<>());
        LinearLayoutManager layoutManagerMy = new LinearLayoutManager(context);
        myRecyclerView.setLayoutManager(layoutManagerMy);
        myRecyclerView.setAdapter(myAdapter);
        otherAdapter=new WorkShowAdapter(context,new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        otherRecyclerView.setLayoutManager(layoutManager);
        otherRecyclerView.setAdapter(otherAdapter);



    }
    @OnClick({R.id.img_back,R.id.img_add_show})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
            onBackPressed();
                break;
            case R.id.img_add_show:
                startActivity(new Intent(context,AddWorkShowActivity.class));

                break;

        }
    }



    @Override
    public void onResume() {
        super.onResume();
        getWorkShowList();
    }
    private void getWorkShowList() {
        myAdapter.clear();
        otherAdapter.clear();
        mSubscription = mRemoteService.getWorkShowList(token)
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
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ArrayList<WorkShowList> mDataList = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<WorkShowList>>() {
                                    }.getType());
                            if (mDataList != null) {
                                rlFirstImg.setVisibility(View.VISIBLE);

                                getUserInfo();
                                ArrayList<WorkShowList> otherWorkShowList=new ArrayList<>();
                                ArrayList<WorkShowList> myWorkShowList=new ArrayList<>();
                                for (int i = 0; i < mDataList.size(); i++) {
                                    if (mDataList.get(i).getRank()==1){
                                        if (mDataList.get(i).getMyself()==1){
                                            imgCrown.setVisibility(View.VISIBLE);
                                        }else {
                                            imgCrown.setVisibility(View.INVISIBLE);
                                        }
                                        if (mDataList.get(i).getExtra()!=null){
                                            Glide.with(context).load(Constant.DOMIN +
                                                    mDataList.get(i).getExtra().get(0).getImage()).
                                                    centerCrop().into(imgBackground);
                                        }


                                    }

                                    switch (mDataList.get(i).getMyself()) {
                                        case 0:
                                            otherWorkShowList.add(mDataList.get(i));
                                            break;
                                        case 1:
                                            myWorkShowList.add(mDataList.get(i));

                                            break;
                                    }

                                }
                                if (myWorkShowList.size()>0){
                                    myRecyclerView.setVisibility(View.VISIBLE);
                                    if (myAdapter != null) {
                                        myAdapter.addAll(myWorkShowList);
                                    }


                                }else {
                                    myRecyclerView.setVisibility(View.GONE);
                                }
                                if (otherWorkShowList.size()>0){
                                    if (otherAdapter!=null){
                                        otherAdapter.addAll(otherWorkShowList);
                                    }

                                    otherRecyclerView.setVisibility(View.VISIBLE);




                                }else {
                                    otherRecyclerView.setVisibility(View.GONE);
                                }


                            } else {

                                rlFirstImg.setVisibility(View.GONE);


                            }
                        }else {
                            rlFirstImg.setVisibility(View.GONE);
                            Toast.makeText(context,apiResponse.
                                    getResultMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void getUserInfo() {
        UserId mUser = new UserId(mPrefsHelper.getPrefs().getString(Constants.UID, ""));
        mSubscription = mRemoteService.getUserInfoById(token, mUser)
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
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.getData());
                        UserInforById mData = gson.fromJson(jsonArray,
                                new TypeToken<UserInforById>() {
                                }.getType());
                        if (mData != null) {
                            name = mData.getName();
                            avatarUrl = mData.getAvatar();
                            tvName.setText(name);
                            if (!TextUtils.isEmpty(avatarUrl)) {
                                Glide.with(context).load(Constant.DOMIN +
                                        avatarUrl).
                                        centerCrop().into(avatar);
                            }


                        }
                    }
                });

    }



    class WorkShowAdapter extends RecyclerView.Adapter<WorkShowAdapter.WorkShowViewHolder> {
        private Context context;
        private ArrayList<WorkShowList> workShowList;

        public WorkShowAdapter(Context context, ArrayList<WorkShowList> workShowList) {
            this.context = context;
            this.workShowList = workShowList;
        }

        @Override
        public WorkShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_work_show, parent, false);
            return new WorkShowViewHolder(itemView);
        }
        public void addAll(ArrayList<WorkShowList> workShowList) {
            this.workShowList.addAll(workShowList);
            WorkShowAdapter.this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(WorkShowViewHolder holder, int position) {
            if (workShowList != null && !workShowList.isEmpty()) {
                WorkShowList data = workShowList.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (data.getId_card_head_image() != null) {
                        Glide.with(context).load(Constant.DOMIN +
                                data.getId_card_head_image()).
                                centerCrop().into(holder.itemAvatar);
                    }
                    holder.itemSortNumber.setText("第" + data.getRank() + "名");
                    switch (data.getRank()) {
                        case 1:
                            holder.imgPraiseFirst.setVisibility(View.VISIBLE);
                            holder.imgPraiseSecond.setVisibility(View.VISIBLE);
                            holder.imgPraiseThird.setVisibility(View.VISIBLE);

                            break;
                        case 2:
                            holder.imgPraiseFirst.setVisibility(View.VISIBLE);
                            holder.imgPraiseSecond.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            holder.imgPraiseFirst.setVisibility(View.VISIBLE);
                            break;

                    }
                    holder.itemPraiseNumber.setText(String.valueOf(data.getLike()));
                    switch (data.getPraise()) {
                        case 0:
                            holder.itemPraiseImg.setImageResource(R.mipmap.ic_praise_default);
                            break;
                        case 1:
                            holder.itemPraiseImg.setImageResource(R.mipmap.ic_work_show_praised);
                            break;
                    }
                    if (data.getProjectname() != null) {
                        holder.itemProjectName.setText(data.getProjectname());
                    } else {
                        holder.itemProjectName.setText("无");
                    }
                    if (data.getTheme() != null) {
                        holder.itemContent.setText(data.getTheme());
                    } else {
                        holder.itemContent.setText("无");

                    }

                    if (data.getTime() != null) {
                        holder.itemDate.setText(data.getTime());
                    } else {
                        holder.itemDate.setText("无");

                    }
                    if (data.getExtra() != null) {
                        if (data.getExtra().size() > 0) {
                            switch (data.getExtra().size()) {
                                case 1:
                                    Glide.with(context).load(Constant.DOMIN +
                                            data.getExtra().get(0).getImage()).
                                            centerCrop().into(holder.imgFirst);
                                    break;
                                case 2:
                                    holder.imgSecond.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(Constant.DOMIN +
                                            data.getExtra().get(0).getImage()).
                                            centerCrop().into(holder.imgFirst);
                                    Glide.with(context).load(Constant.DOMIN +
                                            data.getExtra().get(1).getImage()).
                                            centerCrop().into(holder.imgSecond);
                                    break;
                                case 3:
                                    holder.imgSecond.setVisibility(View.VISIBLE);
                                    holder.imgThird.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(Constant.DOMIN +
                                            data.getExtra().get(0).getImage()).
                                            centerCrop().into(holder.imgFirst);
                                    Glide.with(context).load(Constant.DOMIN +
                                            data.getExtra().get(1).getImage()).
                                            centerCrop().into(holder.imgSecond);
                                    Glide.with(context).load(Constant.DOMIN +
                                            data.getExtra().get(2).getImage()).
                                            centerCrop().into(holder.imgThird);
                                    break;
                            }
                        }
                    }
                }

            }

        }

        @Override
        public int getItemCount() {
            return workShowList.size();
        }


        public void clear() {
            this.workShowList.clear();
            WorkShowAdapter.this.notifyDataSetChanged();
        }


        class WorkShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private WorkShowList mWorkShow;
            private CircleImageView itemAvatar;
            private TextView itemProjectName;
            private TextView itemSortNumber;
            private TextView itemContent;
            private ImageView imgPraiseFirst;
            private ImageView imgPraiseSecond;
            private ImageView imgPraiseThird;
            private TextView itemPraiseNumber;
            private ImageView itemPraiseImg;
            private ImageView imgFirst;
            private ImageView imgSecond;
            private ImageView imgThird;
            private TextView itemDate;


            public WorkShowViewHolder(View itemView) {

                super(itemView);
                itemAvatar = ButterKnife.findById(itemView, R.id.item_avatar);
                itemProjectName = ButterKnife.findById(itemView, R.id.item_project_name);
                itemSortNumber = ButterKnife.findById(itemView, R.id.tv_sort_number);
                itemContent = ButterKnife.findById(itemView, R.id.item_content);
                imgPraiseFirst = ButterKnife.findById(itemView, R.id.img_first_praise);
                imgPraiseSecond = ButterKnife.findById(itemView, R.id.img_second_praise);
                imgPraiseThird = ButterKnife.findById(itemView, R.id.img_third_praise);
                itemPraiseNumber = ButterKnife.findById(itemView, R.id.item_praise_number);
                itemPraiseImg = ButterKnife.findById(itemView, R.id.item_praise_img);
                imgFirst = ButterKnife.findById(itemView, R.id.img_first);
                imgSecond = ButterKnife.findById(itemView, R.id.img_second);
                imgThird = ButterKnife.findById(itemView, R.id.img_third);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                itemPraiseImg.setOnClickListener(this);
                itemPraiseNumber.setOnClickListener(this);

            }

            public void setItem(WorkShowList data) {
                mWorkShow = data;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_praise_number:
                    case R.id.item_praise_img:
                        addPraise(mWorkShow);

                        break;

                }


            }
        }
    }

    private void addPraise(WorkShowList data) {
        mSubscription=mRemoteService.thumbUp(token,data.getId())
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
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                            getWorkShowList();






                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

}
