package org.eenie.wgj.search;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;

import org.eenie.wgj.R;

/**
 * Created by Eenie on 2017/5/24 at 9:02
 * Email: 472279981@qq.com
 * Des:
 */

public class MySearchView extends SearchView {
    private Paint mPaint;

    public MySearchView(Context context) {
        super(context);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //隐藏字体
        this.setQueryHint("签到地址");
        //默认展开
        this.setIconifiedByDefault(false);
        //搜索框的edittext的id
        int searchPlateId = this.getContext().getResources().
                getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText)this .findViewById(searchPlateId);
        //隐藏字体颜色
        searchPlate.setHintTextColor(getResources().getColor(R.color.white));
//        searchPlate.setHintTextColor(ContextCompat.getColor
//                (context, R.color.gray));
        searchPlate.setTextColor(ContextCompat.getColor
                (context, R.color.titleColor));
        searchPlate.setBackgroundResource(R.mipmap.bg_edit_text_light);
        //获取搜索图标的id，用自己的图标代替。因为searchweb没有自带的方法修改图标。id是固定的 android:id/search_mag_icon
        int search_mag_icon_id = this.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        //获取叉号图标的id，用自己的图标代替。
        int search_cha_icon_id = this.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView search_mag_icon = (ImageView) this.findViewById(search_mag_icon_id);
        ImageView search_close_btn = (ImageView) this.findViewById(search_cha_icon_id);
        search_mag_icon.setImageResource(R.mipmap.ic_search_address);
        search_close_btn.setImageResource(R.mipmap.ic_search_address);
    }
}
