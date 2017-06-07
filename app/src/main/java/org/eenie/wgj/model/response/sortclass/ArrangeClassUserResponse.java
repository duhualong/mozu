package org.eenie.wgj.model.response.sortclass;

/**
 * Created by Eenie on 2017/6/7 at 14:59
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeClassUserResponse {

    /**
     * user_id : 6
     * name : 唐海斌
     * line : {"id":17,"name":"领班"}
     */

    private int user_id;
    private String name;
    private LineBean line;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LineBean getLine() {
        return line;
    }

    public void setLine(LineBean line) {
        this.line = line;
    }

    public static class LineBean {
        /**
         * id : 17
         * name : 领班
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
