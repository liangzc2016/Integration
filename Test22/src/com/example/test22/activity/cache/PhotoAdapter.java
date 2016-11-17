package com.example.test22.activity.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import libcore.io.DiskLruCache;
import libcore.io.DiskLruCache.Snapshot;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.test22.R;
import com.example.test22.utils.Myutils;

public class PhotoAdapter extends ArrayAdapter<String>{

	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存
	 * 达到设定值时会将最少最近使用的图片移走
	 */
	private LruCache<String,Bitmap> mMemoryCache;
	/**
	 * 记录所有正在下载或等待下载的任务
	 */
	private Set<BitmapWorkerTask> taskCollection;
	/**
	 * 图片硬盘缓存核心类
	 */
	private DiskLruCache mDiskLruCache;
	private GridView mPhotoWall;
	//记录每个子项的高度
	private int mItemHeight=0;
	public PhotoAdapter(Context context,int textViewResourceId,
			String[] objects,GridView photoWall) {
		super(context, textViewResourceId, objects);
		mPhotoWall = photoWall;
		taskCollection = new HashSet<BitmapWorkerTask>();
		//获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory/8;
		//设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
		//创建DiskLruCache实例，初始化缓存数据
		try {
			File cacheDir = Myutils.getDiskCacheDir(context, "thumb");
			if(!cacheDir.exists()){
				cacheDir.mkdirs();
			}
			mDiskLruCache = DiskLruCache.open(cacheDir, Myutils.getAppVersion(context), 1, 10*1024*1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String url = getItem(position);
		View view ;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(R.layout.item_gridview, null);
		}else{
			view = convertView;
		}
		final ImageView imageView = (ImageView)view.findViewById(R.id.photo);
		if(imageView.getLayoutParams().height!=mItemHeight){
			imageView.getLayoutParams().height = mItemHeight;
		}
		//给ImageView 设置一个tag，保证异步加载图片时不会乱序
		imageView.setTag(url);
		imageView.setImageResource(R.drawable.ic_launcher);
		loadBitmaps(imageView,url);
		return view;
	}
	/**将一张图片存储到LruCache中
	 * @param key 传入图片的url
	 * @param bitmap 传入bitmap对象
	 */
	public void addBitmapToMemoryCache(String key,Bitmap bitmap){
		if(getBitmapFromMemoryCache(key)==null){
			mMemoryCache.put(key, bitmap);
		}
	}
	/**从LruCache中获取一张图片，如果不存在就返回null 
	 * @param key LruCache的键，这里传进图片的url地址
	 * @return 对应传入的bitmap对象，或者null
	 */
	public Bitmap getBitmapFromMemoryCache(String key){
		return mMemoryCache.get(key);
	}
	/**加载bitmap对象，此方法会在LruCache中检查所有屏幕中可见的ImageView
	 * 的对象，如果发现任务一个ImageView的bitmap对象不再缓存中，就会开启异步线程下载
	 * @param imageView
	 * @param url
	 */
	private void loadBitmaps(ImageView imageView, String url) {
		try {
			Bitmap bitmap = getBitmapFromMemoryCache(url);
			if(bitmap==null){
				BitmapWorkerTask task = new BitmapWorkerTask();
				taskCollection.add(task);
				task.execute(url);
			}else{
				if(imageView!=null&&bitmap!=null){
					imageView.setImageBitmap(bitmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 取消所有正在下载或等待下载的任务
	 */
	public void cancelAllTasks(){
		if(taskCollection!=null){
			for(BitmapWorkerTask task:taskCollection){
				task.cancel(false);
			}
		}
	}
	/**设置子项高度
	 * @param height
	 */
	public void setItemHeight(int height){
		if(height==mItemHeight){
			return;
		}
		mItemHeight = height;
		notifyDataSetChanged();
	}
	/**
	 * 将缓存记录同步到journal 文件中
	 */
	public void fluchCache(){
		if(mDiskLruCache!=null){
			try {
				mDiskLruCache.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @author zc
	 *异步下载图片的任务
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>{
		/**
		 * 图片的url地址
		 */
		private String imageUrl;
		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			Snapshot snapShot = null;
			try{
				//生成图片url对应的key
				final String key = Myutils.hashKeyForDisk(imageUrl);
				//查找key对应的缓存
				snapShot = mDiskLruCache.get(key);
				if(snapShot==null){
					//如果没有找到对应的缓存，则准备从网络上请求数据
					DiskLruCache.Editor editor = mDiskLruCache.edit(key);
					if(editor!=null){
						OutputStream outputStream=editor.newOutputStream(0);
						if(downloadUrlToStream(imageUrl, outputStream)){
							editor.commit();
						}else{
							editor.abort();
						}
					}
					//緩存寫入后，再次查找key对应的缓存
					snapShot = mDiskLruCache.get(key);
				}
				if(snapShot!=null){
					fileInputStream = (FileInputStream) snapShot.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				//将缓存数据解析成bitmap对象
				Bitmap bitmap = null;
				if(fileDescriptor!=null){
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				if(bitmap!=null){
					//将bitmap添加到内存缓存对象当中
					addBitmapToMemoryCache(params[0], bitmap);
				}
				return bitmap;
			}catch(Exception e){
			    e.printStackTrace();	
			}finally{
				if(fileDescriptor==null&&fileInputStream!=null){
					try {
						fileInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			//根据tag找到相应的ImageView 控件，将下载好的图片显示出来
			ImageView imageView = (ImageView)mPhotoWall.findViewWithTag(imageUrl);
			if(imageView!=null&&result!=null){
				imageView.setImageBitmap(result);
			}
			taskCollection.remove(this);
		}
	}
	private boolean downloadUrlToStream(String urlString,OutputStream outputStream){
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try{
			final URL url = new URL(urlString);
			urlConnection =  (HttpURLConnection)url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(),8*1024);
			out = new BufferedOutputStream(outputStream,8*1024);
			int b;
			while((b=in.read())!=-1){
				out.write(b);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(urlConnection!=null){
				urlConnection.disconnect();
			}
			try {
			if(out!=null){
				out.close();
			}
			if(in!=null){
				in.close();
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
