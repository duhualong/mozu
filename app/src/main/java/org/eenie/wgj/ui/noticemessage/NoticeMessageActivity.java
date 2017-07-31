package org.eenie.wgj.ui.noticemessage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.noticemessage.NoticeMessageResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/13 at 17:38
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeMessageActivity extends BaseActivity {
    private List<NoticeMessageResponse> mData = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private NoticeAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_notice_module;
    }

    @Override
    protected void updateUI() {
        adapter = new NoticeAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        //添加本地的数据
        getData();

    }

    private void getData() {
        adapter.clear();
        for (int i = 0; i < 3; i++) {
            NoticeMessageResponse notice =
                    new NoticeMessageResponse("员工放假通知" + i, "08月01日", "通知内容" + i, "张三", i);
            mData.add(notice);
        }
        if (mData.size()>0){

            adapter.addAll(mData);
        }


    }

    @OnClick({R.id.img_back, R.id.img_add})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_add:
                startActivity(new Intent(getApplicationContext(),AddNoticeMessageActivity.class));


                break;
        }
    }

    class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ProjectViewHolder> {
        private Context context;
        private List<NoticeMessageResponse> projectList;

        public NoticeAdapter(Context context, List<NoticeMessageResponse> projectList) {
            this.context = context;
            this.projectList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_notice_message, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                NoticeMessageResponse data = projectList.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getTitle())){
                        holder.itemTitle.setText(data.getTitle());

                    }
                    if (!TextUtils.isEmpty(data.getDate())){
                        holder.itemDate.setText(data.getDate());
                    }


                    // holder.imgProject.setImageURI(data.get);
                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public void addAll(List<NoticeMessageResponse> projectList) {
            this.projectList.addAll(projectList);
            NoticeAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectList.clear();
            NoticeAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView itemTitle;
            private TextView itemDate;
            private NoticeMessageResponse mProjectList;
            private RelativeLayout mRelativeLayout;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemTitle = ButterKnife.findById(itemView, R.id.item_title);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_item);
                mRelativeLayout.setOnClickListener(this);

            }

            public void setItem(NoticeMessageResponse projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_item:
                        startActivity(new Intent(context, NoticeMessageDetailActivity.class)
                        .putExtra(NoticeMessageDetailActivity.INFO,mProjectList));


                        break;
                }


            }
        }
    }

}

