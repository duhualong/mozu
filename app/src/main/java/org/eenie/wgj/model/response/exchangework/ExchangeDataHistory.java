package org.eenie.wgj.model.response.exchangework;

import java.util.List;

/**
 * Created by Eenie on 2017/8/2 at 18:12
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeDataHistory {
    private int current_page;
    private List<ExchangeWorkHistoryTakeResponse> data;
    private int last_page;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<ExchangeWorkHistoryTakeResponse> getData() {
        return data;
    }

    public void setData(List<ExchangeWorkHistoryTakeResponse> data) {
        this.data = data;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }
}
