package com.example.test22.view;

import com.example.test22.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**�״�ͼ��֩����ͼ��
 * @author zc
 *
 */
public class NetPicture extends View {
	Paint paint;
	Paint textPaint;
	Paint contentPaint;
	DrawFilter drawFilter;
	int mWidth = 200, mHeight = 200;
	// Ĭ�ϵ���ɫֵ
	private final int green = 0xaf93d150;
	private final int blue = 0xff4aadff;
	private final int white = 0xffffffff;
	private final int black = 0xff000000;
	// �Զ��������ֵ
	private int lineColor;// �ߵ���ɫ
	private int contentColor;// ͼ�ε��ڲ�����ɫ
	private float side;// �����εı߳�
	private float distance;// ��ǰ�����κ���һ�������εľ���
	private int num;// �����ε�����

	private String[] texts;
	// 6����������ֵ
	private float[] abilitys;
	// �����붥��ľ���
	private float textDistance = 10;
	private int textSize = 16;
	private Context context;

	public NetPicture(Context context) {
		this(context, null);
	}

	public NetPicture(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public NetPicture(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		this.context = context;
		// ��ȡ�Զ������Ե�ֵ
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.MyNetPic);
		lineColor = a.getColor(R.styleable.MyNetPic_lineColor, blue);
		contentColor = a.getColor(R.styleable.MyNetPic_cotentColor, green);
		side = a.getDimension(R.styleable.MyNetPic_side, 25);
		distance = a.getDimension(R.styleable.MyNetPic_distance, 25);
		num = a.getInteger(R.styleable.MyNetPic_number, 5);
		a.recycle();
		paint = new Paint();
		textPaint = new Paint();
		contentPaint = new Paint();
		// ��dpת��Ϊpx
		side = dip2px(context, side);
		distance = dip2px(context, distance);
		textDistance = dip2px(context, textDistance);
		textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				textSize, getResources().getDisplayMetrics());
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		texts = new String[] { "����", "�Ŷ�", "��ʶ", "����", "˼ά", "����" };
		abilitys = new float[] { 150, 145, 130, 160, 120, 105 };
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/*
		 * ����Ϊwrap_content�������ô����specMode��AT_MOSTģʽ��������ģʽ�����Ŀ�/��
		 * ����spectSize�����������view��spectSize��parentSize,��parentSize��
		 * ������Ŀǰ����ʹ�ô�С�����Ǹ�������ǰʣ��Ŀռ��С�� ���൱��ʹ��match_parentһ�� ��Ч����������ǿ�������һ��Ĭ�ϵ�ֵ
		 */
		int widthSpectMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpectSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpectMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpectSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSpectMode == MeasureSpec.AT_MOST
				&& heightSpectMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(mWidth, mHeight);
		} else if (widthSpectMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(mWidth, heightSpectSize);
		} else if (heightSpectMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(widthSpectSize, mHeight);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			mWidth = right - left;
			mHeight = bottom - top;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// ��canvas����ȥ������ʱ���
		canvas.setDrawFilter(drawFilter);
		// �Ƶ����������
		canvas.translate(mWidth / 2, mHeight / 2);
		// ��y�ᷭת
		// canvas.scale(1f, -1f);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1f);
		drawBackGroundPic(canvas);
		drawMyText(canvas);
		drawContent(canvas);
	}

	/**
	 * ����Ϊ��������������
	 * 
	 * @param canvas
	 */
	private void drawBackGroundPic(Canvas canvas) {
		paint.setAntiAlias(true);
		paint.setColor(lineColor);
		// �Ȼ�������
		Path path = new Path();
		float x2, x3;
		int AngleCount = 6;
		float xArray[] = new float[num];// �洢x����
		float yArray[] = new float[num];// �洢y����
		for (int j = 0; j < AngleCount; j++) {
			canvas.save();
			canvas.rotate(j * 60);
			// ����ÿ�������ε������������
			for (int i = 0; i < num; i++) {
				x2 = side + i * distance;// �ڶ�����
				xArray[i] = x3 = x2 / 2.0f;// ��������ĺ����꣬��Ϊcos60=1/2;
				// �ù��ɶ��������������y����
				yArray[i] = -(float) Math.sqrt(x2 * x2 - x3 * x3);
			}
			// �Ȼ�����Ǹ������ε�������
			path.moveTo(0, 0);
			path.lineTo(side + (num - 1) * distance, 0);
			path.moveTo(0, 0);
			path.lineTo(xArray[num - 1], yArray[num - 1]);
			// �ٻ�ÿ�������εĵ�������
			for (int i = 0; i < num; i++) {
				path.moveTo(xArray[i], yArray[i]);
				path.lineTo(side + i * distance, 0);
			}
			canvas.drawPath(path, paint);
			canvas.restore();
		}
	}

	/**
	 * ��ʱ�뻭���֣����ұߵ�Ϊ��һ��
	 * 
	 * @param canvas
	 */
	private void drawMyText(Canvas canvas) {
		textPaint.setColor(black);
		textPaint.setTextSize(textSize);
		// ���־���ԭ��Ĵ�С��Ϊ���������α߳�+���־��������εĴ�С
		float d = side + (num - 1) * distance + textDistance;
		// ��Ϊͼ���ǶԳƵģ�����ֱ�Ӽ�������һ���Ƕȵ����֮꣬��Ϳ����ظ�ʹ����
		float dx = (float) (d * Math.cos(60.0 * Math.PI / 180));
		float dy = (float) (d * Math.sin(60.0 * Math.PI / 180));
		Rect textRect = new Rect();
		textPaint.getTextBounds(texts[0], 0, texts[0].length(), textRect);
		canvas.drawText(texts[0], d, textRect.height() / 2, textPaint);
		canvas.drawText(texts[1], dx, -dy, textPaint);
		canvas.drawText(texts[2], -dx - textRect.width(), -dy, textPaint);
		canvas.drawText(texts[3], -d - textRect.width(), textRect.height() / 2,
				textPaint);
		canvas.drawText(texts[4], -dx - textRect.width(),
				dy + textRect.height(), textPaint);
		canvas.drawText(texts[5], dx, dy + textRect.height(), textPaint);
	}

	/**������ֵ�γɵ�ͼ��
	 * @param canvas
	 */
	private void drawContent(Canvas canvas) {
		contentPaint.setColor(contentColor);
		float d =side + (num - 1) * distance;
		//����������������6���������
		float xArray[] = new float[abilitys.length];
		float yArray[] = new float[abilitys.length];
		int count = abilitys.length;
		//����6������ֵ��x,y����
		for (int i = 0; i < count; i++) {
			float conX = (float) (Math.cos(i * 60.0 * Math.PI / 180));
			float conY = (float) (Math.sin(i * 60.0 * Math.PI / 180));
			// Ϊ�˷�ֹ����ֵ�����������εı߳���Ҫ�����������
			xArray[i] = abilitys[i] % d * conX;
			yArray[i] = -abilitys[i] % d * conY;
		}
		//��ͼ��
		Path path = new Path();
		path.moveTo(xArray[0], yArray[0]);
		for(int i=1;i<count;i++){
			path.lineTo(xArray[i], yArray[i]);
		}
		path.close();
		//��6������
		canvas.drawPath(path, contentPaint);
		contentPaint.setColor(black);
		for(int i=0;i<count;i++){
			canvas.drawCircle(xArray[i], yArray[i],dip2px(context, 3),contentPaint);
		}
	}
	public void show(){
		postInvalidate();
	}
	public static class NetPicBuilder {
		private static NetPicture netPicture;
		private static NetPicBuilder netPicBuilder;
		private NetPicBuilder(){
		}
		public static NetPicBuilder createBuilder(NetPicture netPic){
			netPicture = netPic;
			synchronized (NetPicBuilder.class) {
				if(netPicBuilder==null){
					netPicBuilder = new NetPicBuilder();
				}
			}
			return netPicBuilder;
		}
		/**�����ı�������
		 * @param s
		 * @return
		 */
		public static NetPicBuilder setTextContent(String[] s){
			netPicture.setTexts(s);
			return netPicBuilder;
		}
		/**��������ֵ
		 * @param ab
		 * @return
		 */
		public static NetPicBuilder setAbilitys(float[] ab){
			netPicture.setAbilitys(ab);
			return netPicBuilder;
		}
		/**
		 * ��ͼ����ʾ����
		 */
		public static void show(){
			if(netPicture==null){
				throw new NullPointerException("NetPicBuilder is null");
			}
			netPicture.show();
		}
	}

	public void setAbilitys(float[] ab) {
		abilitys = ab;
	}

	public void setTexts(String[] s) {
		this.texts = s;
	}

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
