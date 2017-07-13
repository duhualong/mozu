package org.eenie.wgj.ui.exchangeclass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
import org.eenie.wgj.model.response.exchangework.AddExchangeWorkResponse;
import org.eenie.wgj.model.response.exchangework.ExchangeClassDetailResponse;
import org.eenie.wgj.model.response.exchangework.ExchangeWorkContactResponse;
import org.eenie.wgj.model.response.exchangework.ExchangeWorkListResponse;
import org.eenie.wgj.model.response.meeting.MeetingData;
import org.eenie.wgj.model.response.routing.RoutingContentResponse;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/13 at 13:21
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkClassDetailActivity extends BaseActivity {
    public static final String INFO = "info";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    private ExchangeWorkListResponse data;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    @BindView(R.id.recycler_view_content)
    RecyclerView mRecyclerViewContent;
    @BindView(R.id.recycler_notice)
    RecyclerView mRecyclerNotice;
    @BindView(R.id.edit_exchange_content)
    EditText etExchangeContent;
    private static final int REQUEST_CODE = 0x102;
    private AddPersonalAdapter mAdapter;
    private String precautionid;


    private AddContentAdapter addAdapter;
    private List<ExchangeClassDetailResponse.ImageBean> imgBean = new ArrayList<>();
    ArrayList<RoutingContentResponse> b = new ArrayList<>();
    private ArrayList<ExchangeWorkContactResponse> mData = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_exchange_work_class_detail;
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(INFO);
        if (data != null) {

            if (!TextUtils.isEmpty(data.getName())) {
                tvTitle.setText(data.getName());
                tvClassName.setText(data.getName());
            }
            if (!TextUtils.isEmpty(data.getId())) {
                precautionid = data.getId();
                getData(data.getId());
            }
        }

    }

    private void getData(String id) {
        mSubscription = mRemoteService.getExchangeWorkDetailById(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {

                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ExchangeClassDetailResponse exchangeWorkLists =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<ExchangeClassDetailResponse>() {
                                            }.getType());
                            if (exchangeWorkLists != null) {

                                if (!TextUtils.isEmpty(exchangeWorkLists.getMatter())) {
                                    String content = exchangeWorkLists.getMatter();
                                    String[] strings = content.split("\n");

                                    for (int i = 0; i < strings.length; i++) {
                                        b.add(new RoutingContentResponse(strings[i], false));
                                    }
                                    mRecyclerViewContent.setNestedScrollingEnabled(false);
                                    addAdapter = new AddContentAdapter(context, b);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                    mRecyclerViewContent.setLayoutManager(layoutManager);
                                    mRecyclerViewContent.setAdapter(addAdapter);

                                }
                                if (exchangeWorkLists.getImage() != null && !
                                        exchangeWorkLists.getImage().isEmpty()) {
                                    imgBean = exchangeWorkLists.getImage();
                                    if (imgBean.size() == 1) {
                                        Glide.with(context).load(Constant.DOMIN + imgBean.
                                                get(0).getImage())
                                                .centerCrop().into(imgFirst);
                                    } else if (imgBean.size() == 2) {
                                        Glide.with(context).load(Constant.DOMIN + imgBean.
                                                get(0).getImage())
                                                .centerCrop().into(imgFirst);
                                        Glide.with(context).load(Constant.DOMIN + imgBean.
                                                get(1).getImage())
                                                .centerCrop().into(imgSecond);
                                    } else if (imgBean.size() >= 3) {
                                        Glide.with(context).load(Constant.DOMIN + imgBean.
                                                get(0).getImage())
                                                .centerCrop().into(imgFirst);
                                        Glide.with(context).load(Constant.DOMIN + imgBean.
                                                get(1).getImage())
                                                .centerCrop().into(imgSecond);
                                        Glide.with(context).load(Constant.DOMIN + imgBean.
                                                get(2).getImage())
                                                .centerCrop().into(imgThird);

                                    }


                                }


                            }

                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @OnClick({R.id.img_back, R.id.img_first, R.id.img_second, R.id.img_third, R.id.tv_notice, R.id.btn_ok})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_ok:
                if (!TextUtils.isEmpty(etExchangeContent.getText().toString())) {
                    int type = 0;
                    boolean checked = true;
                    if (!b.isEmpty() && b.size() > 0) {
                        for (int g = 0; g < b.size(); g++) {
                            if (!b.get(g).isSelect()) {
                                checked = false;
                            }
                        }
                    }
                    if (checked) {
                        type = 1;

                    }
                    if (mData.size() > 0) {
                        List<Integer> userId = new ArrayList<>();
                        for (int i = 0; i < mData.size(); i++) {
                            userId.add(mData.get(i).getId());

                        }
                        if (userId.size() > 0) {
                            AddExchangeWorkResponse request = new
                                    AddExchangeWorkResponse(etExchangeContent.getText().toString(),
                                    precautionid, type, userId);
                            addExchangeClassItem(new Gson().toJson(request));


                        }

                    } else {
                        Toast.makeText(context, "请选择通知人员", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "请输入交接班说明", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.tv_notice:
                Intent intent = new Intent(context, AddManagerExchangeClassPeopleActivity.class);
                startActivityForResult(intent, REQUEST_CODE);


                break;
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.img_third:
                if (imgBean.size() >= 3) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + imgBean.get(2).getImage()));
                }

                break;

            case R.id.img_second:
                if (imgBean.size() >= 2) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + imgBean.get(1).getImage()));
                }

                break;
            case R.id.img_first:
                if (imgBean.size() >= 1) {
                    startActivity(
                            new Intent(context, GalleryActivity.class).
                                    putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                            Constant.DOMIN + imgBean.get(0).getImage()));
                }


                break;
        }
    }

    private void addExchangeClassItem(String data) {
        MeetingData dataRequest = new MeetingData(data);
        mSubscription = mRemoteService.addExchangeWorkItem(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), dataRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                ArrayList<ExchangeWorkContactResponse> mList =
                        data.getParcelableArrayListExtra("mData");
                Gson gson = new Gson();
                Log.d("arraylist", "onActivityResult: " + gson.toJson(mList));
                if (mData.isEmpty() || mData == null) {
                    mData = mList;


                } else {
                    if (mList != null) {
                        for (int j = 0; j < mList.size(); j++) {
                            boolean checked = false;
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).getId() == mList.get(j).getId()) {
                                    checked = true;
                                }
                            }
                            if (!checked) {
                                mData.add(mList.get(j));
                            }
                        }
                    }

                }

                if (mData != null) {
                    mRecyclerNotice.setNestedScrollingEnabled(false);
                    mAdapter = new AddPersonalAdapter(context, mData);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    mRecyclerNotice.setLayoutManager(layoutManager);
                    mRecyclerNotice.setAdapter(mAdapter);
                }
            }
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
            private RelativeLayout mRelativeLayout;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemName = ButterKnife.findById(itemView, R.id.item_content_routing);
                rlItem = ButterKnife.findById(itemView, R.id.checkbox_select_content);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_select_item);
                mRelativeLayout.setOnClickListener(this);


            }

            public void setItem(RoutingContentResponse mRoutingContentResponse) {
                mRoutingContentResponses = mRoutingContentResponse;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.rl_select_item:
                        if (rlItem.isChecked()) {
                            rlItem.setChecked(false);
                            mRoutingContentResponses.setSelect(false);
                        } else {
                            rlItem.setChecked(true);
                            mRoutingContentResponses.setSelect(true);
                        }
                        notifyDataSetChanged();

                        break;

                }


            }
        }
    }


    class AddPersonalAdapter extends RecyclerView.Adapter<AddPersonalAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<ExchangeWorkContactResponse> addPersonalClass;

        public AddPersonalAdapter(Context context, ArrayList<ExchangeWorkContactResponse> addPersonalClass) {
            this.context = context;
            this.addPersonalClass = addPersonalClass;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_notice_people, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (addPersonalClass != null && !addPersonalClass.isEmpty()) {
                ExchangeWorkContactResponse data = addPersonalClass.get(position);
                holder.setItem(data);

                if (data != null) {
                    if (!TextUtils.isEmpty(data.getPosition()) && !data.getPosition().equals("null")) {
                        holder.itemName.setText(data.getName() + " " + data.getPosition());
                    } else {
                        holder.itemName.setText(data.getName());

                    }

                }
            }

        }

        @Override
        public int getItemCount() {
            return addPersonalClass.size();
        }

        public void addAll(ArrayList<ExchangeWorkContactResponse> projectList) {
            this.addPersonalClass.addAll(projectList);
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.addPersonalClass.clear();
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private ExchangeWorkContactResponse mProjectList;
            private TextView itemName;
            private RelativeLayout rlItem;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                rlItem = ButterKnife.findById(itemView, R.id.rl_item);
                rlItem.setOnClickListener(this);


            }

            public void setItem(ExchangeWorkContactResponse projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.rl_item:
                        addPersonalClass.remove(mProjectList);
                        notifyDataSetChanged();
                        break;

                }


            }
        }
    }
}
