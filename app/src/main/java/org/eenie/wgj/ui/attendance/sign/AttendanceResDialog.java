package org.eenie.wgj.ui.attendance.sign;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.eenie.wgj.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/19 at 10:54
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceResDialog extends DialogFragment {


    @BindView(R.id.tv_attendance_title)
    TextView mTvAttendanceTitle;
    @BindView(R.id.img_attendance_flg)
    ImageView mImgAttendanceFlg;
    @BindView(R.id.tv_attendance_des)
    TextView mTvAttendanceDes;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;


    private String title;
    private String des;
    private String flag;


    OnEnterClickListener mEnterClickListener;

    /**
     * 获取dialog单例
     *
     * @param title 标题
     * @param des   描述
     * @param flg   0成功，1失败
     * @return 单例
     */
    public static AttendanceResDialog newInstance(String title, String des, String flg) {
        AttendanceResDialog fragment = new AttendanceResDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("des", des);
        args.putString("flag", flg);
        fragment.setArguments(args);
        return fragment;
    }

    public void setEnterClickListener(OnEnterClickListener enterClickListener) {
        mEnterClickListener = enterClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            des = getArguments().getString("des");
            flag = getArguments().getString("flag");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_attendance_res, container, false);
        ButterKnife.bind(this, view);
        mTvAttendanceTitle.setText(title);
        mTvAttendanceDes.setText(des);

        switch (flag) {
            case "0":
                mImgAttendanceFlg.setImageResource(R.mipmap.ic_attendance_sign_successful);
                break;
            case "1":
                mImgAttendanceFlg.setImageResource(R.mipmap.ic_attendance_sign_filed);
                break;
            case "2":
                mImgAttendanceFlg.setImageResource(R.mipmap.ic_sign_in_first);
                break;
            case "3":
                mImgAttendanceFlg.setImageResource(R.mipmap.ic_sign_in_last);
                break;
        }

        return view;
    }

    public interface OnEnterClickListener {
        void click();
    }


    @OnClick(R.id.btn_enter)
    public void onClick() {

        if (mEnterClickListener != null) {
            mEnterClickListener.click();
        }
        getDialog().dismiss();
    }
}
