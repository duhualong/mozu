package org.eenie.wgj.ui.fragment;


import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiRes;
import org.eenie.wgj.model.response.Contacts;
import org.eenie.wgj.ui.news.Cheeses;
import org.eenie.wgj.ui.news.FancyIndexer;
import org.eenie.wgj.ui.news.GoodMan;
import org.eenie.wgj.ui.news.MyAdapter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
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
    @BindView(R.id.bar)FancyIndexer mFancyIndexer;
    @BindView(tv_index_center)TextView mTextView;
    @BindView(lv_content)ListView mListView;


    @Override
    protected int getContentView() {
        return R.layout.fragment_my_data;
    }

    @Override
    protected void updateUI() {
        // 填充数据, 并排序
       // fillAndSortData(persons);
        initDatas(persons);
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
                if(TextUtils.equals(s, letter)){
                    // 匹配成功, 中断循环, 跳转到i位置
                    mListView.setSelection(i);
                    break;
                }
            }
        });
    }


    /**
     * 显示字母提示
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



    private void fillAndSortData(ArrayList<GoodMan> persons) {
        String[] datas = null;
        boolean china = getResources().getConfiguration().locale.getCountry().equals("CN");
        datas = china ? Cheeses.NAMES : Cheeses.sCheeseStrings;
        for (int i = 0; i < datas.length; i++) {
            persons.add(new GoodMan(datas[i]));
        }
        // 排序
        Collections.sort(persons);
    }

    private void initDatas(ArrayList<GoodMan> persons) {
        mSubscription = mRemoteService.getContacts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiRes<List<Contacts>>>() {
                    @Override
                    public void onSuccess(ApiRes<List<Contacts>> value) {
                        JSONObject jsonObject=new JSONObject((Map) value.getResultMessage());
                        String name=jsonObject.optString("name");
                        System.out.println("name:"+name);
                        Log.d(TAG, "onSuccess: " + value.getResultCode());
                        System.out.println("打印：" + value.getResultCode());
                        if (value.getResultCode()==0) {
                            List<Contacts> datas = value.getResultMessage();
                            String[] dataes = null;
                            String[] mString = new String[datas.size() - 1];
                            for (int i = 0; i < datas.size(); i++) {
                                mString[i] = datas.get(i).getName();
                                System.out.println("mString:" + mString);

                            }
                            boolean china = getResources().getConfiguration().locale.getCountry().equals("CN");
                            dataes = china ? mString : Cheeses.sCheeseStrings;
                            for (int i = 0; i < dataes.length; i++) {
                                persons.add(new GoodMan(dataes[i]));
                            }

                            Collections.sort(persons);
                            // SortAdapter adapter = new SortAdapter(datas, context);
                            // listView.setAdapter(adapter);
                        }

                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
    }

}
