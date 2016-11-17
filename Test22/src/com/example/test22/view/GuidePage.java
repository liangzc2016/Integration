package com.example.test22.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author zc
 *更新的引导页
 */
public class GuidePage {
	Button mButton;
	Context context;
	ImageView mGuideImage;
	WindowManager mWindowManager;
	WindowManager.LayoutParams mImageViewParams;
	WindowManager.LayoutParams mButtonParams;
	 /**
	  * 
	  * @param context 
	  * @param windowManager the windowManager which will get the guidepage.
	  * @param resId the drawable resources id for the guidepage to be shown.
	  * @param btnText the text that the button of the guidepage will show.
	  * @param btnGravity the gravity attribute of the button ,you can use Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL and so on for this param.
	  * @return the guidepage instance to show.
	  */
	public GuidePage(Context context,WindowManager windowManager,int resId,String btnText,int btnGravity){
		this.context = context;
		this.mButton = new Button(context);
		this.mButton.setText(btnText);
		this.mGuideImage = new ImageView(context);
		this.mGuideImage.setImageResource(resId);
		this.mGuideImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
		this.mWindowManager = windowManager;
		this.mImageViewParams = new LayoutParams();
		this.mImageViewParams.alpha = 0.5f;
		this.mImageViewParams.width = LayoutParams.MATCH_PARENT;
		this.mImageViewParams.height = LayoutParams.MATCH_PARENT;
		this.mImageViewParams.type = 2002;
		this.mButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,2003,40,1);
		this.mButtonParams.gravity = btnGravity;
	}
	public void showGuidePage(){
		mWindowManager.addView(mButton, mButtonParams);
		mWindowManager.addView(mGuideImage, mImageViewParams);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWindowManager.removeView(mButton);
				mWindowManager.removeViewImmediate(mGuideImage);
			}
		});
	}
}
