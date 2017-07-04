package org.eenie.wgj.model.response.routing;

/**
 * Created by Eenie on 2017/7/4 at 15:31
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingContentResponse {
    private String content;
    private boolean select;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public RoutingContentResponse(String content, boolean select) {
        this.content = content;
        this.select = select;
    }
}
