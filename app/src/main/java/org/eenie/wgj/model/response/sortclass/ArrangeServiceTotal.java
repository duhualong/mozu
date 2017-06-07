package org.eenie.wgj.model.response.sortclass;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/7 at 15:09
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeServiceTotal {
    private int serviceId;
    private String serviceName;
    private String servicePeople;
    private ArrayList<ArrangeClassUserResponse> user;

    public ArrangeServiceTotal(int serviceId, String serviceName, String servicePeople,
                               ArrayList<ArrangeClassUserResponse> user) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePeople = servicePeople;
        this.user = user;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePeople() {
        return servicePeople;
    }

    public void setServicePeople(String servicePeople) {
        this.servicePeople = servicePeople;
    }

    public ArrayList<ArrangeClassUserResponse> getUser() {
        return user;
    }

    public void setUser(ArrayList<ArrangeClassUserResponse> user) {
        this.user = user;
    }
}
