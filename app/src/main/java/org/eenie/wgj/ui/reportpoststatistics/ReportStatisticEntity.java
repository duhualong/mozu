package org.eenie.wgj.ui.reportpoststatistics;

/**
 * Created by Eenie on 2017/2/23 at 10:41
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportStatisticEntity {

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
