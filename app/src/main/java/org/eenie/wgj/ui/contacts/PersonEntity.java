package org.eenie.wgj.ui.contacts;

import me.zhouzhuo.zzletterssidebar.anotation.Letter;
import me.zhouzhuo.zzletterssidebar.entity.SortModel;

/**
 * Created by Eenie on 2017/4/24 at 14:50
 * Email: 472279981@qq.com
 * Des:
 */
public class PersonEntity extends SortModel {
    @Letter(isSortField = true)
    private String personName;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}