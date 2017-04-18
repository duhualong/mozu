package org.eenie.wgj.model.response;

import java.util.List;

/**
 * Created by Eenie on 2017/4/18 at 8:49
 * Email: 472279981@qq.com
 * Des:
 */

public class Infomation {
    public String type;
    public List<Item> items;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

 public  class Item{
        private String nID;
        private String index;
        private String desc;
        private String content;

        public String getnID() {
            return nID;
        }

        public void setnID(String nID) {
            this.nID = nID;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
