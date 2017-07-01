package org.eenie.wgj.model.response;

import java.util.List;

/**
 * Created by Eenie on 2017/6/29 at 15:51
 * Email: 472279981@qq.com
 * Des:
 */

public class FilterRoutingRecord {
    private  int id;
    private int situation;
    private String time;
    private double longitude;
    private double latitude;
    private String location_name;
    private List<ImagesBean> images;
    private String inspectionname;
    private String happening;
    private WarrantyBean warranty;
    private int position;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public String getInspectionname() {
        return inspectionname;
    }

    public void setInspectionname(String inspectionname) {
        this.inspectionname = inspectionname;
    }

    public String getHappening() {
        return happening;
    }

    public void setHappening(String happening) {
        this.happening = happening;
    }

    public WarrantyBean getWarranty() {
        return warranty;
    }

    public void setWarranty(WarrantyBean warranty) {
        this.warranty = warranty;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public  static  class WarrantyBean{
        private int id;
        private String uniqueid;
        private String content;
        private int inspectionpoint_id;
        private List<ImageBean> image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUniqueid() {
            return uniqueid;
        }

        public void setUniqueid(String uniqueid) {
            this.uniqueid = uniqueid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getInspectionpoint_id() {
            return inspectionpoint_id;
        }

        public void setInspectionpoint_id(int inspectionpoint_id) {
            this.inspectionpoint_id = inspectionpoint_id;
        }

        public List<ImageBean> getImage() {
            return image;
        }

        public void setImage(List<ImageBean> image) {
            this.image = image;
        }
    }
    public static class ImageBean{
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
    public static class ImagesBean{

        private int id;
        private int inspection_id;
        private String image;
        private String created_at;
        private String updated_at;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInspection_id() {
            return inspection_id;
        }

        public void setInspection_id(int inspection_id) {
            this.inspection_id = inspection_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

}
