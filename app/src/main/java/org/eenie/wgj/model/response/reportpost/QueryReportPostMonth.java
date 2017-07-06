package org.eenie.wgj.model.response.reportpost;

/**
 * Created by Eenie on 2017/7/6 at 11:57
 * Email: 472279981@qq.com
 * Des:
 */

public class QueryReportPostMonth {
    private String end_date;
    private String project_id;
    private String start_date;

    public QueryReportPostMonth(String end_date, String project_id, String start_date) {
        this.end_date = end_date;
        this.project_id = project_id;
        this.start_date = start_date;
    }
}
