package com.example.test22.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



/**
 * 缁樺埗鍦嗙幆涓庡渾寮�
 *
 * @author 缁村己
 */
public class ArcView2 extends View {
    private Context context;
    private Paint paint;

    private int center;// 涓績
    private int ringWidth;// 鍦嗙幆涓庡渾寮х殑瀹藉害

    private int font_normal;// 瀛椾綋棰滆壊
    private int color_grey;// 鐏拌壊
    private int color_green;// 缁胯壊
    private int color_yellow;// 榛勮壊
    private int color_red;// 绾㈣壊
    private final int green = 0xaf93d150;
	private final int blue = 0xff4aadff;
	private final int white = 0xffffffff;
	private final int black = 0xff000000;
    public ArcView2(Context context) {
        this(context, null);
    }

    public ArcView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true); // 娑堥櫎閿娇
        paint.setStyle(Paint.Style.STROKE);  // 缁樺埗绌哄績鍦嗘垨 绌哄績鐭╁舰
        font_normal = black;
        color_grey = Color.GRAY;
        color_green = green;
        color_yellow = blue;
    }

    public ArcView2(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int width = right - left;
            int height = bottom - top;
            center = Math.min(width, height) / 2;
            ringWidth = dip2px(context, 15);   // 鍦嗙幆瀹藉害
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
       
        // 鍐呭渾鐜�
        paint.setColor(color_grey);
        paint.setStrokeWidth(ringWidth);
        canvas.drawCircle(center, center, center - ringWidth - 5, paint);

        // 澶栧渾鐜�
        paint.setColor(color_green);
        paint.setStrokeWidth(ringWidth);
        RectF rectF = new RectF(ringWidth / 2, ringWidth / 2, center * 2 - ringWidth / 2, center * 2 - ringWidth / 2);
        canvas.drawArc(rectF, 0 - 90, 250, false, paint);
        
//        canvas.save();
        // 鏂囨湰
        Paint paint = new Paint();
        String value = "23ppm";
        Rect rect = new Rect();
        paint.getTextBounds(value, 0, value.length(), rect);
        paint.setColor(font_normal);
        paint.setTextSize(16);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(value, center, center, paint);
//        canvas.restore();

    }
    public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
