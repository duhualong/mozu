//package org.eenie.wgj.ui.workshow;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//
//import org.eenie.wgj.R;
//import org.eenie.wgj.model.ApiResponse;
//import org.eenie.wgj.model.response.WorkShowList;
//import org.eenie.wgj.ui.message.GalleryActivity;
//import org.eenie.wgj.util.Constant;
//import org.eenie.wgj.util.Constants;
//
//import java.util.ArrayList;
//
//import butterknife.ButterKnife;
//import de.hdodenhof.circleimageview.CircleImageView;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * Created by Eenie on 2017/7/22 at 14:23
// * Email: 472279981@qq.com
// * Des:
// */
//
//public class WorkShowListAdapter extends RecyclerView.Adapter<WorkShowAdapter.WorkShowViewHolder> {
//    private Context context;
//    private ArrayList<WorkShowList> workShowList;
//
//    public WorkShowAdapter(Context context, ArrayList<WorkShowList> workShowList) {
//        this.context = context;
//        this.workShowList = workShowList;
//    }
//
//    @Override
//    public WorkShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View itemView = inflater.inflate(R.layout.item_work_show, parent, false);
//        return new WorkShowViewHolder(itemView);
//    }
//
//    public void addAll(ArrayList<WorkShowList> workShowList) {
//        this.workShowList.addAll(workShowList);
//        WorkShowAdapter.this.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onBindViewHolder(WorkShowViewHolder holder, int position) {
//        if (workShowList != null && !workShowList.isEmpty()) {
//            WorkShowList data = workShowList.get(position);
//            holder.setItem(data);
//            if (data != null) {
//                if (data.getId_card_head_image() != null) {
//                    Glide.with(context).load(Constant.DOMIN +
//                            data.getId_card_head_image()).
//                            centerCrop().into(holder.itemAvatar);
//                }
//                holder.itemSortNumber.setText("第" + data.getRank() + "名");
//                if (data.getRank()==1){
//                    holder.imgPraiseFirst.setVisibility(View.VISIBLE);
//                    holder.imgPraiseSecond.setVisibility(View.VISIBLE);
//                    holder.imgPraiseThird.setVisibility(View.VISIBLE);
//
//                }else if (data.getRank()==2){
//                    holder.imgPraiseFirst.setVisibility(View.VISIBLE);
//                    holder.imgPraiseSecond.setVisibility(View.VISIBLE);
//                }else if (data.getRank()==3){
//                    holder.imgPraiseFirst.setVisibility(View.VISIBLE);
//
//                }else if (data.getRank()>=4){
//                    holder.imgPraiseFirst.setVisibility(View.INVISIBLE);
//                    holder.imgPraiseSecond.setVisibility(View.INVISIBLE);
//                    holder.imgPraiseThird.setVisibility(View.INVISIBLE);
//                }
//
//                holder.itemPraiseNumber.setText(String.valueOf(data.getLike()));
//                switch (data.getPraise()) {
//                    case 0:
//                        holder.itemPraiseImg.setImageResource(R.mipmap.ic_praise_default);
//                        break;
//                    case 1:
//                        holder.itemPraiseImg.setImageResource(R.mipmap.ic_work_show_praised);
//                        break;
//                }
//                if (data.getProjectname() != null) {
//                    holder.itemProjectName.setText(data.getProjectname());
//                } else {
//                    holder.itemProjectName.setText("无");
//                }
//                if (data.getTheme() != null) {
//                    holder.itemContent.setText(data.getTheme());
//                } else {
//                    holder.itemContent.setText("无");
//
//                }
//
//                if (data.getTime() != null) {
//                    holder.itemDate.setText(data.getTime());
//                } else {
//                    holder.itemDate.setText("无");
//
//                }
//                if (data.getExtra() != null) {
//                    if (data.getExtra().size() > 0) {
//                        switch (data.getExtra().size()) {
//                            case 1:
//                                Glide.with(context).load(Constant.DOMIN +
//                                        data.getExtra().get(0).getImage()).
//                                        centerCrop().into(holder.imgFirst);
//                                break;
//                            case 2:
//                                holder.imgSecond.setVisibility(View.VISIBLE);
//                                Glide.with(context).load(Constant.DOMIN +
//                                        data.getExtra().get(0).getImage()).
//                                        centerCrop().into(holder.imgFirst);
//                                Glide.with(context).load(Constant.DOMIN +
//                                        data.getExtra().get(1).getImage()).
//                                        centerCrop().into(holder.imgSecond);
//                                break;
//                            default:
//                                holder.imgSecond.setVisibility(View.VISIBLE);
//                                holder.imgThird.setVisibility(View.VISIBLE);
//                                Glide.with(context).load(Constant.DOMIN +
//                                        data.getExtra().get(0).getImage()).
//                                        centerCrop().into(holder.imgFirst);
//                                Glide.with(context).load(Constant.DOMIN +
//                                        data.getExtra().get(1).getImage()).
//                                        centerCrop().into(holder.imgSecond);
//                                Glide.with(context).load(Constant.DOMIN +
//                                        data.getExtra().get(2).getImage()).
//                                        centerCrop().into(holder.imgThird);
//                                break;
//                        }
//                    }
//                }
//            }
//
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return workShowList.size();
//    }
//
//
//    public void clear() {
//        this.workShowList.clear();
//        WorkShowAdapter.this.notifyDataSetChanged();
//    }
//
//
//    class WorkShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private WorkShowList mWorkShow;
//        private CircleImageView itemAvatar;
//        private TextView itemProjectName;
//        private TextView itemSortNumber;
//        private TextView itemContent;
//        private ImageView imgPraiseFirst;
//        private ImageView imgPraiseSecond;
//        private ImageView imgPraiseThird;
//        private TextView itemPraiseNumber;
//        private ImageView itemPraiseImg;
//        private ImageView imgFirst;
//        private ImageView imgSecond;
//        private ImageView imgThird;
//        private TextView itemDate;
//
//
//        public WorkShowViewHolder(View itemView) {
//
//            super(itemView);
//            itemAvatar = ButterKnife.findById(itemView, R.id.item_avatar);
//            itemProjectName = ButterKnife.findById(itemView, R.id.item_project_name);
//            itemSortNumber = ButterKnife.findById(itemView, R.id.tv_sort_number);
//            itemContent = ButterKnife.findById(itemView, R.id.item_content);
//            imgPraiseFirst = ButterKnife.findById(itemView, R.id.img_first_praise);
//            imgPraiseSecond = ButterKnife.findById(itemView, R.id.img_second_praise);
//            imgPraiseThird = ButterKnife.findById(itemView, R.id.img_third_praise);
//            itemPraiseNumber = ButterKnife.findById(itemView, R.id.item_praise_number);
//            itemPraiseImg = ButterKnife.findById(itemView, R.id.item_praise_img);
//            imgFirst = ButterKnife.findById(itemView, R.id.img_first);
//            imgSecond = ButterKnife.findById(itemView, R.id.img_second);
//            imgThird = ButterKnife.findById(itemView, R.id.img_third);
//            itemDate = ButterKnife.findById(itemView, R.id.item_date);
//            itemPraiseImg.setOnClickListener(this);
//            itemPraiseNumber.setOnClickListener(this);
//            imgFirst.setOnClickListener(this);
//            imgSecond.setOnClickListener(this);
//            imgThird.setOnClickListener(this);
//
//        }
//
//        public void setItem(WorkShowList data) {
//            mWorkShow = data;
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.item_praise_number:
//                case R.id.item_praise_img:
//                    addPraise(mWorkShow);
//                    break;
//                case R.id.img_first:
//                    if (mWorkShow.getExtra() != null && !mWorkShow.getExtra().isEmpty()) {
//
//                        if (mWorkShow.getExtra().size() >= 1) {
//
//                            startActivity(
//                                    new Intent(context, GalleryActivity.class).
//                                            putExtra(GalleryActivity.EXTRA_IMAGE_URI,
//                                                    Constant.DOMIN + mWorkShow.getExtra().get(0).getImage()));
//                        }
//                    }
//
//                    break;
//
//                case R.id.img_second:
//                    if (mWorkShow.getExtra() != null && !mWorkShow.getExtra().isEmpty()) {
//
//                        if (mWorkShow.getExtra().size() >= 2) {
//
//                            startActivity(
//                                    new Intent(context, GalleryActivity.class).
//                                            putExtra(GalleryActivity.EXTRA_IMAGE_URI,
//                                                    Constant.DOMIN + mWorkShow.getExtra().get(1).getImage()));
//                        }
//                    }
//
//                    break;
//
//                case R.id.img_third:
//
//                    if (mWorkShow.getExtra() != null && !mWorkShow.getExtra().isEmpty()) {
//                        if (mWorkShow.getExtra().size() >= 3) {
//                            startActivity(
//                                    new Intent(context, GalleryActivity.class).
//                                            putExtra(GalleryActivity.EXTRA_IMAGE_URI,
//                                                    Constant.DOMIN + mWorkShow.getExtra().
//                                                            get(2).getImage()));
//                        }
//                    }
//                    break;
//            }
//        }
//    }
//}
//
//    private void addPraise(WorkShowList data) {
//        mSubscription = mRemoteService.thumbUp(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
//                data.getId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ApiResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(ApiResponse apiResponse) {
//                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
//                            Toast.makeText(context, apiResponse.getResultMessage(),
//                                    Toast.LENGTH_SHORT).show();
//
//                            getWorkShowList();
//                            myAdapter.notifyDataSetChanged();
//
//                        } else {
//                            Toast.makeText(context, apiResponse.getResultMessage(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//}
