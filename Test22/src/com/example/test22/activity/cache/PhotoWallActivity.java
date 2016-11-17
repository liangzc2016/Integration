package com.example.test22.activity.cache;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.example.test22.R;

/**Ê¹ÓÃÄÚ´æ»º´æºÍÓ²ÅÌ»º´æ
 * @author zc
 *
 */
public class PhotoWallActivity extends Activity {
	GridView gridView;
	PhotoAdapter myAdapter;
	private int mImageThumbSize;
	private int mImageThumSpacing;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photowall);
		mImageThumbSize = 150;
		mImageThumSpacing =5;
		gridView = (GridView)findViewById(R.id.photo_wall);
		myAdapter = new PhotoAdapter(this, 0, Images.imageStrings, gridView);
		gridView.setAdapter(myAdapter);
		gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				final int numColumns = (int)Math.floor(gridView.getWidth())/(mImageThumbSize+mImageThumSpacing);
				if(numColumns>0){
					int columnWidth = gridView.getWidth()/numColumns-mImageThumSpacing;
					myAdapter.setItemHeight(150);
					gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		});
	}
	@Override
	protected void onPause() {
		super.onPause();
		myAdapter.fluchCache();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		myAdapter.cancelAllTasks();
	}
}
