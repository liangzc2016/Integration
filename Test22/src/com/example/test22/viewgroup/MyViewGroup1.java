package com.example.test22.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup1 extends ViewGroup {

	public MyViewGroup1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyViewGroup1(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MyViewGroup1(Context context) {
		this(context,null);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(),attrs);
	}
	/* (non-Javadoc)��������childView�Ŀ�Ⱥ͸߶� Ȼ�����childView�ļ������������Լ��Ŀ�͸�
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//��ȡviewgroup�ϼ�����Ϊ���Ƽ��Ŀ�͸ߣ��Լ�����ģʽ
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		//��������е�childView�Ŀ�͸�
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		//��¼�����wrap_content ʱ���õĿ�͸�
		int width=0;
		int height=0;
		int count=getChildCount();
		int cWidth=0;
		int cHeight=0;
		MarginLayoutParams cParams=null;
		int lHeight=0;//���ڱ�����ߵ�childview�ĸ߶�
		int rHeight=0;//���ڱ����ұߵ�childeView�ĸ߶ȣ�����lHeight��rHeightȡ���ֵ��Ϊ�߶�
		int tWidth=0;//�����ϱߵĿ�
		int bWidth=0;//�����±ߵĿ�,����ȡ���ֵ��Ϊ���
		for(int i=0;i<count;i++){
			View child = getChildAt(i);
			cWidth = child.getMeasuredWidth();
			cHeight = child.getMeasuredHeight();
			cParams = (MarginLayoutParams) child.getLayoutParams();
			//��������childview
			if(i==0||i==1){
				tWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
			}
			if(i==2||i==3){
				bWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
			}
			if(i==0||i==2){
				lHeight+=cHeight+cParams.topMargin+cParams.bottomMargin;
			}
			if(i==1||i==3){
				rHeight+=cHeight+cParams.topMargin+cParams.bottomMargin;
			}
			width=Math.max(tWidth, bWidth);
			height=Math.max(lHeight, rHeight);
			//�����wrap_cotent����Ϊ���Ǽ����ֵ������ֱ������Ϊ�����������ֵ
			setMeasuredDimension((widthMode==MeasureSpec.AT_MOST)?width:sizeWidth, (heightMode==MeasureSpec.AT_MOST)?height:sizeHeight);
		}
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onLayout(boolean change, int l, int t, int r, int b) {
		int count = getChildCount();
		int cWidth=0;
		int cHeight=0;
		MarginLayoutParams cParams=null;
		//��������childView�������͸ߣ��Լ�margin������
		for(int i=0;i<count;i++){
			View child = getChildAt(i);
			cWidth = child.getMeasuredWidth();
			cHeight=child.getMeasuredHeight();
			cParams = (MarginLayoutParams)getLayoutParams();
			int cl=0,ct=0,cr=0,cb=0;
			switch(i){
			case 0:
				cl = cParams.leftMargin;
				ct = cParams.topMargin;
				break;
			case 1:
				cl = getWidth()-cWidth-cParams.leftMargin-cParams.rightMargin;
				ct = cParams.topMargin;
				break;
			case 2:
				cl = cParams.leftMargin;  
                ct = getHeight() - cHeight - cParams.bottomMargin;  
				break;
			case 3:
				cl = getWidth() - cWidth - cParams.leftMargin  - cParams.rightMargin;  
                ct = getHeight() - cHeight - cParams.bottomMargin;  
				break;
			}
			cr=cl+cWidth;
			cb=ct+cHeight;
			child.layout(cl, ct, cr, cb);
		}
	}

}
