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
	 * ͼƬ���漼���ĺ����࣬���ڻ����������غõ�ͼƬ���ڳ����ڴ�
	 * �ﵽ�趨ֵʱ�Ὣ�������ʹ�õ�ͼƬ����
	 */
	private LruCache<String,Bitmap> mMemoryCache;
	/**
	 * ��¼�����������ػ�ȴ����ص�����
	 */
	private Set<BitmapWorkerTask> taskCollection;
	/**
	 * ͼƬӲ�̻��������
	 */
	private DiskLruCache mDiskLruCache;
	private GridView mPhotoWall;
	//��¼ÿ������ĸ߶�
	private int mItemHeight=0;
	public PhotoAdapter(Context context,int textViewResourceId,
			String[] objects,GridView photoWall) {
		super(context, textViewResourceId, objects);
		mPhotoWall = photoWall;
		taskCollection = new HashSet<BitmapWorkerTask>();
		//��ȡӦ�ó����������ڴ�
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory/8;
		//����ͼƬ�����СΪ�����������ڴ��1/8
		mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
		//����DiskLruCacheʵ������ʼ����������
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
		//��ImageView ����һ��tag����֤�첽����ͼƬʱ��������
		imageView.setTag(url);
		imageView.setImageResource(R.drawable.ic_launcher);
		loadBitmaps(imageView,url);
		return view;
	}
	/**��һ��ͼƬ�洢��LruCache��
	 * @param key ����ͼƬ��url
	 * @param bitmap ����bitmap����
	 */
	public void addBitmapToMemoryCache(String key,Bitmap bitmap){
		if(getBitmapFromMemoryCache(key)==null){
			mMemoryCache.put(key, bitmap);
		}
	}
	/**��LruCache�л�ȡһ��ͼƬ����������ھͷ���null 
	 * @param key LruCache�ļ������ﴫ��ͼƬ��url��ַ
	 * @return ��Ӧ�����bitmap���󣬻���null
	 */
	public Bitmap getBitmapFromMemoryCache(String key){
		return mMemoryCache.get(key);
	}
	/**����bitmap���󣬴˷�������LruCache�м��������Ļ�пɼ���ImageView
	 * �Ķ��������������һ��ImageView��bitmap�����ٻ����У��ͻῪ���첽�߳�����
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
	 * ȡ�������������ػ�ȴ����ص�����
	 */
	public void cancelAllTasks(){
		if(taskCollection!=null){
			for(BitmapWorkerTask task:taskCollection){
				task.cancel(false);
			}
		}
	}
	/**��������߶�
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
	 * �������¼ͬ����journal �ļ���
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
	 *�첽����ͼƬ������
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>{
		/**
		 * ͼƬ��url��ַ
		 */
		private String imageUrl;
		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			Snapshot snapShot = null;
			try{
				//����ͼƬurl��Ӧ��key
				final String key = Myutils.hashKeyForDisk(imageUrl);
				//����key��Ӧ�Ļ���
				snapShot = mDiskLruCache.get(key);
				if(snapShot==null){
					//���û���ҵ���Ӧ�Ļ��棬��׼������������������
					DiskLruCache.Editor editor = mDiskLruCache.edit(key);
					if(editor!=null){
						OutputStream outputStream=editor.newOutputStream(0);
						if(downloadUrlToStream(imageUrl, outputStream)){
							editor.commit();
						}else{
							editor.abort();
						}
					}
					//���挑����ٴβ���key��Ӧ�Ļ���
					snapShot = mDiskLruCache.get(key);
				}
				if(snapShot!=null){
					fileInputStream = (FileInputStream) snapShot.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				//���������ݽ�����bitmap����
				Bitmap bitmap = null;
				if(fileDescriptor!=null){
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				if(bitmap!=null){
					//��bitmap��ӵ��ڴ滺�������
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
			//����tag�ҵ���Ӧ��ImageView �ؼ��������غõ�ͼƬ��ʾ����
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
