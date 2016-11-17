package com.example.test22.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * @author zc ͼƬ������
 */
public class Myutils {
	/**
	 * ���ݴ����uniqueName��ȡӲ�̻����·����ַ
	 * 
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * ��ȡӦ�ó���İ汾��
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * ʹ��MD5�㷨�Դ����key���м���
	 * 
	 * @param key
	 * @return
	 */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
			e.printStackTrace();
		}
		return cacheKey;
	}

	/**
	 * byte����ת��16���Ƶ�string
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * �ı��Զ����С�����
	 * 
	 * @param string
	 * @param textPaint
	 *            ����
	 * @param canvas
	 * @param point
	 *            ���ĵ�
	 * @param width
	 *            ���
	 * @param align
	 * @param spacingmult
	 * @param spacingadd
	 * @param includepad
	 */
	public static void textCenter(String string, TextPaint textPaint,
			Canvas canvas, Point point, int width, Layout.Alignment align,
			float spacingmult, float spacingadd, boolean includepad) {
		StaticLayout staticLayout = new StaticLayout(string, textPaint, width,
				align, spacingmult, spacingadd, includepad);
		canvas.save();
		canvas.translate(-staticLayout.getWidth() / 2 + point.x,
				-staticLayout.getHeight() / 2 + point.y);
		staticLayout.draw(canvas);
		canvas.restore();
		// demo
		// String mString = "Idtk��һ��Сѧ��";
		// TextPaint tp = new TextPaint();
		// tp.setColor(Color.BLUE);
		// tp.setStyle(Paint.Style.FILL);
		// tp.setTextSize(50);
		// Point point = new Point(0,0);
		// textCenter(mString,tp,canvas,point,150,Layout.Alignment.ALIGN_CENTER,1.5f,0,false);

	}

}
