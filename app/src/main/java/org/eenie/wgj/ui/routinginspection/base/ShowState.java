package org.eenie.wgj.ui.routinginspection.base;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by Eenie on 2017/6/23 at 14:51
 * Email: 472279981@qq.com
 * Des:
 */

public interface ShowState {

    /**
     * 显示该状态
     *
     * @param animate 是否动画
     */
    void show(boolean animate);

    /**
     * 隐藏该状态
     *
     * @param animate 是否动画
     */
    void dismiss(boolean animate);

    /**
     * 设置FragmentView
     */
    void setFragmentView(View fragmentView);

    /**
     * 进入动画
     */
    void setAnimIn(Animation in);

    /**
     * 退出动画
     */
    void setAnimOut(Animation out);
}