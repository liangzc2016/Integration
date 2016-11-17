package com.example.test22.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.example.test22.R;

public class ClipImageLayout extends RelativeLayout {
	private ClipImageBorderView mClipImageBorderView;
	private ZoomImageView2 mZoomImageView2;
	private int mHorizontalPadding = 20;
	public ClipImageLayout(Context context) {
		this(context,null);
	}

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mClipImageBorderView = new ClipImageBorderView(context, attrs);
		mZoomImageView2 = new ZoomImageView2(context, attrs);
		android.view.ViewGroup.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mZoomImageView2.setImageDrawable(getResources().getDrawable(R.drawable.large));
		this.addView(mZoomImageView2, lp);
		this.addView(mClipImageBorderView, lp);
		//¼ÆËãpaddingµÄpx
		mHorizontalPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
		mClipImageBorderView.setmHorizontalPadding(mHorizontalPadding);
		mZoomImageView2.setmHorizonpadding(mHorizontalPadding);
	}
	

	public int getmHorizontalPadding() {
		return mHorizontalPadding;
	}

	public void setmHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
		mClipImageBorderView.setmHorizontalPadding(mHorizontalPadding);
		mZoomImageView2.setmHorizonpadding(mHorizontalPadding);
		invalidate();
	}

	public ClipImageLayout(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}
	/**½ØÈ¡Í¼Æ¬
	 * @return
	 */
	public Bitmap clip(){
		return mZoomImageView2.clip();
	}
	public void setMyImage(Drawable d){
		mZoomImageView2.setImageDrawable(d);
		invalidate();
	}

}
