package org.eenie.wgj.realm;

import android.content.Context;
import android.text.TextUtils;

import io.realm.Realm;

/**
 * Area info search helper
 * Created by Zac on 2016/5/20.
 */
public class AreaSearchHelper {

  static class Default {
    static String DEFAULT_PROVINCE = "北京市";
    static String DEFAULT_CITY = "北京市";
    static String DEFAULT_AREA = "海淀区";
    static int DEFAULT_ID = 0;
  }

  private static AreaSearchHelper searchHelper;

  private static Realm realm;

  private AreaSearchHelper(Context context) {
    RealmController controller = new RealmController(context.getApplicationContext());
    realm = controller.getAreaRealm();
  }

  //public static AreaSearchHelper getInstances() {
  //  if (searchHelper == null) {
  //    searchHelper = new AreaSearchHelper();
  //  }
  //  return searchHelper;
  //}
  public static AreaSearchHelper getInstance(Context context) {
    if (searchHelper == null) {
      searchHelper = new AreaSearchHelper(context);
    }
    return searchHelper;
  }

  /**
   * 获取省份信息
   *
   * @param areaId 地区id
   * @return 省份名称
   */
  public String getProvince(int areaId) {
    if (areaId >= 0) {
      return realm.where(AreaTable.class).equalTo("id", areaId).findFirst().getProvince();
    }
    return Default.DEFAULT_PROVINCE;
  }

  /**
   * 获取城市信息
   *
   * @param areaId 地区id
   * @return 城市名称
   */
  public String getCity(int areaId) {
    if (areaId >= 0) {
      return realm.where(AreaTable.class).equalTo("id", areaId).findFirst().getCity();
    }
    return Default.DEFAULT_CITY;
  }

  /**
   * 获取地区信息
   *
   * @param areaId 地区id
   * @return 地区名称
   */
  public String getArea(int areaId) {
    if (areaId >= 0) {
      return realm.where(AreaTable.class).equalTo("id", areaId).findFirst().getArea();
    }
    return Default.DEFAULT_AREA;
  }

  /**
   * 获取地区id
   *
   * @param area 地区名称
   * @return 地区id
   */
  public int getAreaId(String area) {
    if (!TextUtils.isEmpty(area)) {
      return realm.where(AreaTable.class).equalTo("area", area).findFirst().getId();
    }
    return Default.DEFAULT_ID;
  }
}
