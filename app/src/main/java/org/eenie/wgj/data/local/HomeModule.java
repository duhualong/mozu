package org.eenie.wgj.data.local;

import android.text.TextUtils;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Eenie on 2017/5/8 at 9:17
 * Email: 472279981@qq.com
 * Des:
 */

public class HomeModule extends RealmObject {


    private boolean isVIP;
    @Ignore
    private Class target;
    private String iconRes;
    private String moduleName;
    private String className;

    private int index;

    private boolean show;


    public HomeModule() {

    }



    public HomeModule(Class targetActivity, String iconRes, String moduleName, boolean vip, int index,boolean show) {
        this(targetActivity.getName(), iconRes, moduleName, vip);
        this.index = index;
        this.show = show;
    }
    public HomeModule(String iconRes, String moduleName, boolean vip, int index,boolean show) {
        this("", iconRes, moduleName, vip);
        this.index = index;
        this.show = show;
    }

    public HomeModule(String targetActivity, String iconRes, String moduleName, boolean vip) {
        this.iconRes = iconRes;
        this.moduleName = moduleName;
        isVIP = vip;
        className = targetActivity;
        Class c = null;
        try {
            c = Class.forName(targetActivity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            target = c;
        }
    }



    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public Class getTarget() {
        Class c = null;
        if (!TextUtils.isEmpty(className)) {
            try {
                c = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    public void setTarget(Class target) {
        this.target = target;
    }


    public String getIconRes() {
        return iconRes;
    }

    public void setIconRes(String iconRes) {
        this.iconRes = iconRes;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}