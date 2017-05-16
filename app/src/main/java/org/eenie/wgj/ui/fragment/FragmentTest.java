package org.eenie.wgj.ui.fragment;


import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.response.Contacts;
import org.eenie.wgj.ui.news.Cheeses;
import org.eenie.wgj.ui.news.FancyIndexer;
import org.eenie.wgj.ui.news.GoodMan;
import org.eenie.wgj.ui.news.MyAdapter;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static org.eenie.wgj.R.id.lv_content;
import static org.eenie.wgj.R.id.tv_index_center;

/**
 * Created by Eenie on 2017/4/13 at 13:59
 * Email: 472279981@qq.com
 * Des:
 */

public class FragmentTest extends BaseSupportFragment {
    ArrayList<GoodMan> persons = new ArrayList<>();
    private Handler mHandler = new Handler();
    @BindView(R.id.bar)
    FancyIndexer mFancyIndexer;
    @BindView(tv_index_center)
    TextView mTextView;
    @BindView(lv_content)
    ListView mListView;
    public List<Contacts> mContacts;
    public String[] mSData;


    @Override
    protected int getContentView() {
        return R.layout.fragment_my_data;
    }

    @Override
    protected void updateUI() {
        // 填充数据, 并排序
        initData();

        //initDatas();
        initUi();

    }

    private void initUi() {
        mListView.setAdapter(new MyAdapter(persons, context));


        mFancyIndexer.setOnTouchLetterChangedListener(letter -> {
            System.out.println("letter: " + letter);
//				Util.showToast(getApplicationContext(), letter);

//				showLetter(letter);

            // 从集合中查找第一个拼音首字母为letter的索引, 进行跳转
            for (int i = 0; i < persons.size(); i++) {
                GoodMan goodMan = persons.get(i);
                String s = goodMan.getPinyin().charAt(0) + "";
                if (TextUtils.equals(s, letter)) {
                    // 匹配成功, 中断循环, 跳转到i位置
                    mListView.setSelection(i);
                    break;
                }
            }
        });
    }


    /**
     * 显示字母提示
     *
     * @param letter
     */
    protected void showLetter(String letter) {
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(letter);

        // 取消掉刚刚所有的演示操作
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 隐藏
                mTextView.setVisibility(View.GONE);
            }
        }, 1000);

    }


    private void fillAndSortData(ArrayList<GoodMan> persons, String[] str) {

        String[] datas;
        boolean china = getResources().getConfiguration().locale.getCountry().equals("CN");
        datas = china ? str: Cheeses.sCheeseStrings;
        for (int i = 0; i < datas.length; i++) {
            persons.add(new GoodMan(datas[i]));
        }
        // 排序
        Collections.sort(persons);
    }

    private void initData() {
        System.out.println("打印："+mPrefsHelper.getPrefs().getString(Constants.TOKEN,""));
//        mSubscription = mRemoteService.getContacts(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<ApiResponse<List<Contacts>>>() {
//
//
//
//                    @Override
//                    public void onSuccess(ApiResponse<List<Contacts>> data) {
//
//                        mContacts = data.getData();
//                        mSData = new String[mContacts.size()];
//
//                        for (int i = 0; i < mSData.length; i++) {
//                            String str = mContacts.get(i).getName();
//                            mSData[i] = str;
//                        }
//
//                        fillAndSortData(persons, mSData);
//                        System.out.println("str[]:" + Arrays.toString(mSData));
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//
//                    }
//                });
    }

}
