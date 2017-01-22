package org.mobiletrain.letterindexview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wangsong on 2016/4/25.
 */
public class LetterIndexView extends View {

    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    //手指按下的字母的位置
    private int choosedPosition = -1;
    private Paint paint;
    //正中心显示的TextView
    private TextView textViewDialog;
    //2.声明一个接口变量
    private UpdateListView updateListView;

    public LetterIndexView(Context context) {
        this(context, null);
    }

    public LetterIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(24);
    }

    public void setTextViewDialog(TextView textViewDialog) {
        this.textViewDialog = textViewDialog;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取每一个文本的高度
        int perTextHeight = getHeight() / letters.length;
        for (int i = 0; i < letters.length; i++) {
            //如果要绘制的字母为手指按下的字母，则为红色
            if (choosedPosition == i) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.BLACK);
            }
            canvas.drawText(letters[i], (getWidth() - paint.measureText(letters[i])) / 2, (i + 1) * perTextHeight, paint);
        }
    }

    //处理触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //手指按下位置的y轴坐标
        float y = event.getY();
        int perTextHeight = getHeight() / letters.length;
        int currentPosition = (int) (y / perTextHeight);
//        String letter = letters[currentPosition];
        //event.getAction()
        //1.action_down 按下
        //2.action_move 移动
        //3.action_up 抬起
        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                //手指抬起时，隐藏正中心的TextView
                if (textViewDialog != null) {
                    textViewDialog.setVisibility(View.GONE);
                }
                break;
            default:
                //按下和滑动时，背景变为灰色
                setBackgroundColor(Color.parseColor("#cccccc"));
                if (currentPosition > -1 && currentPosition < letters.length) {
                    if (textViewDialog != null) {
                        textViewDialog.setVisibility(View.VISIBLE);
                        textViewDialog.setText(letters[currentPosition]);
                    }
                    //4.调用接口变量
                    if (updateListView != null) {
                        updateListView.updateListView(letters[currentPosition].charAt(0));
                    }
                    choosedPosition = currentPosition;
                }
                break;
        }
        //刷新控件
        invalidate();
        return true;
    }

    //3.初始化接口变量
    public void setUpdateListView(UpdateListView updateListView) {
        this.updateListView = updateListView;
    }

    //当ListView滚动时，动态更新右边滑动栏的颜色
    public void updateLetterIndexView(int currentChar) {
        for (int i = 0; i < letters.length; i++) {
            if (currentChar == letters[i].charAt(0)) {
                choosedPosition = i;
                invalidate();
                break;
            }
        }
    }

    //1.创建一个接口
    public interface UpdateListView {
        public void updateListView(int currentChar);
    }
}
