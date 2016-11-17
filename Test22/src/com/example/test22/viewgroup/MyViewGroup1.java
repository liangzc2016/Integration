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
	/* (non-Javadoc)计算所有childView的宽度和高度 然后根据childView的计算结果，设置自己的宽和高
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取viewgroup上级容器为其推荐的宽和高，以及计算模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		//计算出所有的childView的宽和高
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		//记录如果是wrap_content 时设置的宽和高
		int width=0;
		int height=0;
		int count=getChildCount();
		int cWidth=0;
		int cHeight=0;
		MarginLayoutParams cParams=null;
		int lHeight=0;//用于保存左边的childview的高度
		int rHeight=0;//用于保存右边的childeView的高度，最终lHeight和rHeight取最大值作为高度
		int tWidth=0;//保存上边的宽
		int bWidth=0;//保存下边的宽,最终取最大值作为宽度
		for(int i=0;i<count;i++){
			View child = getChildAt(i);
			cWidth = child.getMeasuredWidth();
			cHeight = child.getMeasuredHeight();
			cParams = (MarginLayoutParams) child.getLayoutParams();
			//上面两个childview
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
			//如果是wrap_cotent设置为我们计算的值，否则直接设置为父容器计算的值
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
		//遍历所有childView根据其宽和高，以及margin来布局
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
