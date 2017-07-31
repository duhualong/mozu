package org.eenie.wgj.ui.attendance;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.newattendancestatistic.UserAttendanceStatisticData;

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

    //    ArrayList<StatisticsInfoEntity.ResultMessageBean> mErrorMessageBeen = new ArrayList<>();
//    Map<String, StatisticsInfoEntity.ResultMessageBean> mMessageBeanMap = new HashMap<>();
    ArrayList<UserAttendanceStatisticData.SchedualBean> mErrorMessageBeen = new ArrayList<>();
    Map<String, UserAttendanceStatisticData.SchedualBean> mMessageBeanMap = new HashMap<>();

    RankItemColorAgent mRankItemColorAgent = new RankItemColorAgent();

    @Override
    public void decorate(CalendarCellView cellView, Date date) {

        TextView textView = (TextView) cellView.findViewById(R.id.tv_sign_state);
        TextView desText = (TextView) cellView.findViewById(tv_state_des);
        RelativeLayout rlBackground = (RelativeLayout) cellView.findViewById(R.id.date_background);
        UserAttendanceStatisticData.SchedualBean bean = getBean(new SimpleDateFormat("yyyy-MM-dd").format(date));
        textView.setVisibility(View.INVISIBLE);

        if (bean != null) {

            if (bean.getAbsent() == 1) {
                textView.setVisibility(View.VISIBLE);
                textView.setText("旷");


            } else if (bean.getAbsent()==0){
                boolean checked = false;
                if (bean.getCheckin() != null) {
                    if (bean.getCheckin().getLate() == 1) {
                        checked = true;
                    }

                }

                if (bean.getSignback() != null) {
                    if (bean.getSignback().getForget() == 1) {
                        checked = true;

                    }
                    if (bean.getSignback().getLate() == 1) {
                        checked = true;
                    }

                }
                if (checked) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("异");
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }

            }

            desText.setText(bean.getServicesname());
            switch (bean.getServicesname()) {
                case "常日班":
                    rlBackground.setBackgroundResource(R.color.class_day_normal);

                    break;
                case "日班":
                    rlBackground.setBackgroundResource(R.color.class_day);

                    break;
                case "实习":
                    rlBackground.setBackgroundResource(R.color.class_practice);

                    break;
                case "夜班":
                    rlBackground.setBackgroundResource(R.color.class_night);
                    break;
                case "休息日":
                    rlBackground.setBackgroundResource(R.color.class_rest);

                    break;
                default:
                    rlBackground.setBackgroundResource(R.color.class_other);
                    break;
            }
            cellView.setHoliday(false);
            cellView.setBackgroundColor(mRankItemColorAgent.
                    getColorByRankName(bean.getServicesname()));

//            if (bean.getStateCode() == 0) {
//                textView.setVisibility(View.INVISIBLE);
//            } else {
//                textView.setText(bean.getStateDes());
//                textView.setVisibility(View.VISIBLE);
//            }
//
//
//            if (bean.getService() != null && bean.getService().getId() != 0) {
//
//                desText.setText(bean.getService().getServicesname());
//                switch (bean.getService().getServicesname()) {
//                    case "日班":
//                        rlBackground.setBackgroundResource(R.color.white_light);
//
//
//                        break;
//                    case "夜班":
//                        rlBackground.setBackgroundResource(R.color.textHintColor);
//                        break;
//                    case "测试数据":
//                        rlBackground.setBackgroundResource(R.color.textHintColor);
//
//                        break;
//                    default:
//                        rlBackground.setBackgroundResource(R.color.background_rest);
//                        break;
//                }
//                cellView.setHoliday(false);
//                cellView.setBackgroundColor(mRankItemColorAgent.
//                        getColorByRankName(bean.getService().getServicesname()));
//
//
//            } else {
//                desText.setText("休息");
//                cellView.setHoliday(true);
//                rlBackground.setBackgroundResource(R.color.background_rest);
////                cellView.setHighlighted(false);
//            }
        } else {
            desText.setText("");
        }


    }


    //    public void setData(List<StatisticsInfoEntity.ResultMessageBean> messageBeanList) {
//        for (StatisticsInfoEntity.ResultMessageBean bean : messageBeanList) {
//            mMessageBeanMap.put(bean.getDate(), bean);
//
//            if (bean.getStateCode() != 0) {
//                mErrorMessageBeen.add(bean);
//            }
//        }
//
//    }
    public void setData(List<UserAttendanceStatisticData.SchedualBean> messageBeanList) {
        for (UserAttendanceStatisticData.SchedualBean bean : messageBeanList) {
            mMessageBeanMap.put(bean.getDay(), bean);
            mErrorMessageBeen.add(bean);

        }

    }

    public ArrayList<UserAttendanceStatisticData.SchedualBean> getSignErrorData() {
        return mErrorMessageBeen;
    }


    public UserAttendanceStatisticData.SchedualBean getBean(String key) {
        return mMessageBeanMap.get(key);
    }
}
