package org.eenie.wgj.ui.contacts;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.Contacts;
import org.eenie.wgj.search.CharacterParser;
import org.eenie.wgj.search.ClearEditText;
import org.eenie.wgj.search.PinyinComparator;
import org.eenie.wgj.search.SideBar;
import org.eenie.wgj.search.SortContactsAdapter;
import org.eenie.wgj.search.SortModel;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/4/24 at 14:59
 * Email: 472279981@qq.com
 * Des:
 */

public class FragmentContact extends BaseSupportFragment {
    @BindView(R.id.fragment_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.country_lvcountry)
    ListView sortListView;
    @BindView(R.id.sidrbar)
    SideBar sideBar;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.filter_edit)
    ClearEditText mClearEditText;
    @BindView(R.id.root_view)
    View rootView;

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private SortContactsAdapter adapter;
    private List<SortModel> SourceDateList;
    private List<String> mStrArray = new ArrayList<>();
    private List<Integer> mCompanyId = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.test_contacts_list;
    }

    @Override
    protected void updateUI() {
        initData();

    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    private void initData() {

        mSubscription = mRemoteService.getContacts(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""))
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
                    public void onNext(ApiResponse listApiResponse) {
                        if (listApiResponse.getResultCode() == 200) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(listApiResponse.getData());
                            List<Contacts> mData = gson.fromJson(jsonArray,
                                    new TypeToken<List<Contacts>>() {
                                    }.getType());
                            if (mData!=null) {

                                mFrameLayout.setVisibility(View.VISIBLE);
                                initViews(mData);
                            }
                        } else {

                            mFrameLayout.setVisibility(View.GONE);
                            Snackbar.make(rootView, listApiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();


                        }


                        new Thread() {
                            public void run() {

                            }
                        }.start();


                    }
                });

    }

    private void initViews(List<Contacts> mData) {
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);


        sideBar.setOnTouchingLetterChangedListener(s -> {

            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                sortListView.setSelection(position);
            }

        });


        if (mData.size() > 0) {
            SourceDateList = filledDatas(mData);
            Collections.sort(SourceDateList, pinyinComparator);
            adapter = new SortContactsAdapter(context, SourceDateList);
            sortListView.setAdapter(adapter);
        } else {

        }


        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        sortListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent=new Intent(context,ContactsDetailActivity.class);
            intent.putExtra(ContactsDetailActivity.NAME,
                    ((SortModel) adapter.getItem(position)).getName());
            intent.putExtra(ContactsDetailActivity.AVATAR,
                    ((SortModel) adapter.getItem(position)).getAvatar());
            intent.putExtra(ContactsDetailActivity.PHONE,
                    ((SortModel) adapter.getItem(position)).getPhone());
            intent.putExtra(ContactsDetailActivity.DUTY,
                    ((SortModel) adapter.getItem(position)).getDuties());


            startActivity(intent);



        });
    }

    /**
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
    private List<SortModel> filledDatas(List<Contacts> data) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(data.get(i).getName());
            sortModel.setId(data.get(i).getId());
            sortModel.setAvatar(data.get(i).getId_card_head_image());
            sortModel.setDuties(data.get(i).getDuties());
            sortModel.setPhone(data.get(i).getPhone());



            String pinyin = characterParser.getSelling(data.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        Log.d(TAG, "filledData: " + mSortList.toString());
        return mSortList;

    }
}

