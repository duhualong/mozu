package org.eenie.wgj.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.model.response.CompanyList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/10 at 13:19
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterPersonalFirstFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String UID = "user_id";
    private static final String TOKEN = "token";
    private String token;
    private int userId;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.tv_select_city)
    TextView selectCity;
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_company)
    RecyclerView mRecyclerView;
    private CompanyAdapter companyListAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_register_personal_first;
    }

    @Override
    protected void updateUI() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        companyListAdapter = new CompanyAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(companyListAdapter);


    }

    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    //获取所有公司
    private void getCompanyList() {



    }


    public static RegisterPersonalFirstFragment newInstance(int userId, String token) {
        RegisterPersonalFirstFragment fragment = new RegisterPersonalFirstFragment();
        if (!TextUtils.isEmpty(token)) {
            Bundle args = new Bundle();
            args.putInt(UID, userId);
            args.putString(TOKEN, token);
            fragment.setArguments(args);

        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            userId = getArguments().getInt(UID);
            token = getArguments().getString(TOKEN);


        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;
        }
    }

    @Override
    public void onRefresh() {
        companyListAdapter.clear();
        getCompanyList();

    }

    class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {
        private Context context;
        private List<CompanyList> companyList;

        public CompanyAdapter(Context context, List<CompanyList> companyList) {
            this.context = context;
            this.companyList = companyList;
        }

        @Override
        public CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_company_name, parent, false);
            return new CompanyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CompanyViewHolder holder, int position) {
            if (companyList != null && !companyList.isEmpty()) {
                CompanyList data = companyList.get(position);

                String companyName = data.getCompany_name();
                if (!TextUtils.isEmpty(companyName)) {
                    holder.company.setText(companyName);
                }


            }

        }

        @Override
        public int getItemCount() {
            return companyList.size();
        }

        public void addAll(List<CompanyList> companyList) {
            this.companyList.addAll(companyList);
            CompanyAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.companyList.clear();
            CompanyAdapter.this.notifyDataSetChanged();
        }

        class CompanyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView company;
            private LinearLayout lineCompany;

            private CompanyList mCompanyList;


            public CompanyViewHolder(View itemView) {

                super(itemView);
                company = ButterKnife.findById(itemView, R.id.item_company_name);
                lineCompany = ButterKnife.findById(itemView, R.id.item_company);
                lineCompany.setOnClickListener(this);


            }

            public void setItem(CompanyList companyList) {
                mCompanyList = companyList;
            }

            @Override
            public void onClick(View v) {


            }
        }
    }

}
