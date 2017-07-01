package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/28 at 13:38
 * Email: 472279981@qq.com
 * Des:
 */

public class UploadPointPatrol  {
    private String location_id;
    private String positions;

    public UploadPointPatrol(String location_id, String positions) {
        this.location_id = location_id;
        this.positions = positions;
    }
}
