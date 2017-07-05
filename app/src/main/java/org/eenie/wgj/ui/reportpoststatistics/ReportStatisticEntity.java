package org.eenie.wgj.ui.reportpoststatistics;

import java.util.List;

/**
 * Created by Eenie on 2017/2/23 at 10:41
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportStatisticEntity {
    /**
     * resultCode : 0
     * resultMessage : [{"date":"2017-02","plan":588,"actual":1,"not":587,"rate":0.17},{"date":"2017-01","plan":651,"actual":11,"not":640,"rate":1.69},{"date":"2016-12","plan":651,"actual":3,"not":648,"rate":0.46}]
     */

    private int resultCode;
    private List<ResultMessageBean> resultMessage;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<ResultMessageBean> getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(List<ResultMessageBean> resultMessage) {
        this.resultMessage = resultMessage;
    }

    public static class ResultMessageBean {
        /**
         * date : 2017-02
         * plan : 588
         * actual : 1
         * not : 587
         * rate : 0.17
         */

        private String date;
        private int plan;
        private int actual;
        private int not;
        private double rate;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getPlan() {
            return plan;
        }

        public void setPlan(int plan) {
            this.plan = plan;
        }

        public int getActual() {
            return actual;
        }

        public void setActual(int actual) {
            this.actual = actual;
        }

        public int getNot() {
            return not;
        }

        public void setNot(int not) {
            this.not = not;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }

}
