package org.eenie.wgj.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Realm--地区表
 * Created by Zac on 2016/5/17.
 */
public class AreaTable extends RealmObject {
  @PrimaryKey private int id;
  @Required private String area;
  @Required private String city;
  @Required private String province;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }
}
