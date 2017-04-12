package org.eenie.wgj.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**

 */
public class Sidebar extends View{
    public static String[] alphabets = new String[]{ "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
    private int selectedPosition; //选中字母的位置
    private Paint mPaint;
    private int cellHeight; //每一个字母的高度
    private int alphabetDefaultColor;
    private int alphabetSelectedColor;
    private float textSize;
    private OnAlphabetChangeListener onAlphabetChangeListener;

    public Sidebar(Context context) {
        super(context);
        init();
    }

    public Sidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Sidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        alphabetDefaultColor = Color.GRAY;
        alphabetSelectedColor = Color.RED;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        cellHeight = getHeight() / alphabets.length;
        for (int i=0; i< alphabets.length; i++){
            drawAlphabet(canvas,i);
        }

    }

    private void drawAlphabet(Canvas canvas,int positon) {
        String alphabet = alphabets[positon];

        mPaint.setColor(alphabetDefaultColor);
        if (isPressed()){
            if (positon == selectedPosition){
                mPaint.setColor(alphabetSelectedColor);
                if (onAlphabetChangeListener != null){
                    onAlphabetChangeListener.alphabetChangeListener(this,alphabet,positon);
                }
            }
        }

        int baseLine = (positon+1) * cellHeight;
        canvas.drawText(alphabet, (getWidth() - mPaint.measureText(alphabet)) / 2, baseLine, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                selectedPosition = (int)(Math.ceil((y / cellHeight)) - 1);
                break;
            case MotionEvent.ACTION_UP:
                setPressed(false);
                break;
        }
        invalidate();
        return true;
    }

    public interface OnAlphabetChangeListener{
        void alphabetChangeListener(View v, String alphabet, int position);
    }

    public OnAlphabetChangeListener getOnAlphabetChangeListener() {
        return onAlphabetChangeListener;
    }

    public void setOnAlphabetChangeListener(OnAlphabetChangeListener onAlphabetChangeListener) {
        this.onAlphabetChangeListener = onAlphabetChangeListener;
    }

    public int getAlphabetSelectedColor() {
        return alphabetSelectedColor;
    }

    public void setAlphabetSelectedColor(int alphabetSelectedColor) {
        this.alphabetSelectedColor = alphabetSelectedColor;
    }

    public int getAlphabetDefaultColor() {
        return alphabetDefaultColor;
    }

    public void setAlphabetDefaultColor(int alphabetDefaultColor) {
        this.alphabetDefaultColor = alphabetDefaultColor;
    }

    public float getTextSize() {
        if (mPaint == null) return 0;
        return mPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        if (mPaint == null) return ;
        mPaint.setTextSize(textSize);
    }
}
