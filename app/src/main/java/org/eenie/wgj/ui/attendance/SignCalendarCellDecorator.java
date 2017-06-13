package org.eenie.wgj.ui.attendance;

import android.view.View;
import android.widget.TextView;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import org.eenie.wgj.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eenie.wgj.R.id.tv_state_des;

/**
 * Created by Eenie on 2017/6/13 at 15:22
 * Email: 472279981@qq.com
 * Des:
 */

public class SignCalendarCellDecorator implements CalendarCellDecorator {

    ArrayList<StatisticsInfoEntity.ResultMessageBean> mErrorMessageBeen = new ArrayList<>();
    Map<String, StatisticsInfoEntity.ResultMessageBean> mMessageBeanMap = new HashMap<>();


    RankItemColorAgent mRankItemColorAgent = new RankItemColorAgent();

    @Override
    public void decorate(CalendarCellView cellView, Date date) {

        TextView textView = (TextView) cellView.findViewById(R.id.tv_sign_state);
        TextView desText = (TextView) cellView.findViewById(tv_state_des);
        StatisticsInfoEntity.ResultMessageBean bean = getBean(new SimpleDateFormat("yyyy-MM-dd").format(date));


//        cellView.setBackgroundColor(Color.RED);

        if (bean != null) {

            if (bean.getStateCode() == 0) {
                textView.setVisibility(View.INVISIBLE);
            } else {
                textView.setText(bean.getStateDes());
                textView.setVisibility(View.VISIBLE);
            }


            if (bean.getService() != null && bean.getService().getId() != 0) {

//                LogUtil.json(bean);

                desText.setText(bean.getService().getServicesname());
                cellView.setHoliday(false);
                cellView.setBackgroundColor(mRankItemColorAgent.
                        getColorByRankName(bean.getService().getServicesname()));


            } else {
                desText.setText("休息");
                cellView.setHoliday(true);
//                cellView.setHighlighted(false);
            }
        } else {
            desText.setText("");
        }


    }


    public void setData(List<StatisticsInfoEntity.ResultMessageBean> messageBeanList) {
        for (StatisticsInfoEntity.ResultMessageBean bean : messageBeanList) {
            mMessageBeanMap.put(bean.getDate(), bean);

            if (bean.getStateCode() != 0) {
                mErrorMessageBeen.add(bean);
            }
        }

    }


    public ArrayList<StatisticsInfoEntity.ResultMessageBean> getSignErrorData() {
        return mErrorMessageBeen;
    }


    public StatisticsInfoEntity.ResultMessageBean getBean(String key) {
        return mMessageBeanMap.get(key);
    }
}
