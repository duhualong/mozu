package org.eenie.wgj.model;

import java.util.List;

/**
 * Created by Eenie on 2017/6/2 at 15:13
 * Email: 472279981@qq.com
 * Des:
 */

public class GaodeResponse<T> {

    /**
     * status : 1
     * count : 870
     * info : OK
     * infocode : 10000
     * suggestion : {"keywords":[],"cities":[]}
     */

    private String status;
    private String count;
    private String info;
    private String infocode;
    private SuggestionBean suggestion;
    private T pois;

    public T getPois() {
        return pois;
    }

    public void setPois(T pois) {
        this.pois = pois;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public SuggestionBean getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestionBean suggestion) {
        this.suggestion = suggestion;
    }

    public static class SuggestionBean {
        private List<?> keywords;
        private List<?> cities;

        public List<?> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<?> keywords) {
            this.keywords = keywords;
        }

        public List<?> getCities() {
            return cities;
        }

        public void setCities(List<?> cities) {
            this.cities = cities;
        }
    }
}
