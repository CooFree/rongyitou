package com.yxh.ryt.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Author SunnyCoffee
 * @Date 2014-1-28
 * @version 1.0
 * @Desc 工具类
 */

public class Utils {
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	public static String timeToFormatTemp(String tmpl,long time){
		SimpleDateFormat sdf = new SimpleDateFormat(tmpl);
		return sdf.format(new Date(time));
	}
	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
	//格式化时间
	public static String timeTrans(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date(time));
	}
	public static String timeLottery(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm:ss.fff");
		return sdf.format(new Date(time));
	}
	public static String timeAuction(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("M月d日H时m分");
		return sdf.format(new Date(time));
	}
	public static String timeNew(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("H时m分");
		return sdf.format(new Date(time));
	}
	public static String timeNew1(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
		return sdf.format(new Date(time));
	}
	public static String timeAndIos(long time){
		Date spreadTime = new Date(time);
		String timeDate=dateFormater2.get().format(spreadTime);
		Date today = new Date();
		String nowDate = dateFormater2.get().format(today);
		if (nowDate.equals(timeDate)){
			StringBuilder builder=new StringBuilder();
			builder.append("今天");
			builder.append(timeNew(time));
			return builder.toString();
		}else if (IsYesterday(timeTrans(time))){
                StringBuilder builder=new StringBuilder();
                builder.append("昨天");
                builder.append(timeNew(time));
                return builder.toString();
		}else {
			return timeNew1(time);
		}
	}
	public static boolean IsYesterday(String day)  {
		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);

		Calendar cal = Calendar.getInstance();
		Date date = null;
		try {
			date = getDateFormat().parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == -1) {
				return true;
			}
		}
		return false;
	}
	private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
	public static SimpleDateFormat getDateFormat() {
		if (null == DateLocal.get()) {
			DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
		}
		return DateLocal.get();
	}
	public static String timeTrans1(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(time));
	}
	public static String timeTrans2(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(new Date(time));
	}
	public static String timeTransMonth(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(new Date(time));
	}
	public static String timeTransDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(new Date(time));
	}
	public static Date timeTransfore(long time) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormater2.get().parse(sdf.format(new Date(time)));
	}
	public static String timeTransComment(long time){
			boolean b = false;
		Date currtime = null;
		try {
			currtime = timeTransfore(time);
		} catch (Exception e) {

		}
		Date today = new Date();
			if(currtime != null){
				String nowDate = dateFormater2.get().format(today);
				String timeDate = dateFormater2.get().format(currtime);
				if(nowDate.equals(timeDate)){
					b = true;
				}
			}
		if (b){
			return timeTrans2(time);
		}
		return  timeTrans(time);
		}
	public static String timeTransComment1(long time){
		boolean b = false;
		Date currtime = null;
		try {
			currtime = timeTransfore(time);
		} catch (Exception e) {

		}
		Date today = new Date();
		if(currtime != null){
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(currtime);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		if (b){
			return timeTrans2(time);
		}
		return  timeTrans1(time);
	}
	public static void setListViewHeightBasedOnChildren(GridView listView) {
		// 获取listview的adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		// 固定列宽，有多少列
		int col = 4;// listView.getNumColumns();
		int totalHeight = 0;
		// i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
		// listAdapter.getCount()小于等于8时计算两次高度相加
		for (int i = 0; i < listAdapter.getCount(); i += col) {
			// 获取listview的每一个item
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			// 获取item的高度和
			totalHeight += listItem.getMeasuredHeight();
		}

		// 获取listview的布局参数
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// 设置高度
		params.height = totalHeight+40;
		// 设置margin
		((ViewGroup.MarginLayoutParams) params).setMargins(DisplayUtil.dip2px(6), DisplayUtil.dip2px(10),0, DisplayUtil.dip2px(5));
		// 设置参数
		listView.setLayoutParams(params);
	}
	public static void setListViewHeightBasedOnChildren01(GridView listView) {
		// 获取listview的adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		// 固定列宽，有多少列
		int col = 1;// listView.getNumColumns();
		int totalHeight = 0;
		// i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
		// listAdapter.getCount()小于等于8时计算两次高度相加
		for (int i = 0; i < listAdapter.getCount(); i += col) {
			// 获取listview的每一个item
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			// 获取item的高度和
			totalHeight += listItem.getMeasuredHeight();
		}

		// 获取listview的布局参数
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// 设置高度
		params.height = totalHeight+40;
		// 设置margin
		((ViewGroup.MarginLayoutParams) params).setMargins(DisplayUtil.dip2px(6), DisplayUtil.dip2px(10),0, DisplayUtil.dip2px(5));
		// 设置参数
		listView.setLayoutParams(params);
	}

	public void setListViewHeightBasedOnChildren1(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += 5;//if without this statement,the listview will be a little short
		listView.setLayoutParams(params);
	}
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/**
	 * @desc <pre>旋转图片</pre>
	 * @author Weiliang Hu
	 * @date 2013-9-18
	 * @param
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(String path, Bitmap bitmap) {
		// 旋转图片 动作
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	public static int readPictureDegree1(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();;
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getFilePathFromUri( Uri uri,Context mContext) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(mContext, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(mContext, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(mContext, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(mContext, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	public static String getImageFormat(String url){
		if ((url.endsWith(".jpg"))){
			return ".jpg";
		}else if ((url.endsWith(".jpeg"))){
			return ".jpeg";
		}else if ((url.endsWith(".png"))){
			return ".png";
		}else if ((url.endsWith(".gif"))){
			return ".gif";
		}
		return "";
	}
	public static Bitmap.CompressFormat getImageFormatBig(String url){
		if ((url.endsWith(".jpg"))){
			return Bitmap.CompressFormat.JPEG;
		}else if ((url.endsWith(".jpeg"))){
			return Bitmap.CompressFormat.JPEG;
		}else if ((url.endsWith(".png"))){
			return Bitmap.CompressFormat.PNG;
		}
		return Bitmap.CompressFormat.JPEG;
	}

	/**
	 * 动态测量list view 总高度
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		/*ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += 5;//if without this statement,the listview will be a little short
		listView.setLayoutParams(params);*/
	}

	//按天，时，分来分
	public static String getJudgeDate(long millions){
		if (millions>=1000*60*60*24){
			String s=millions/1000/60/60/24+"天";
			return s;
		}else if (millions>=1000*60*60){
			String s=millions/1000/60/60+"时";
			return s;
		}else if (millions>=1000*60){
			String s=millions/1000/60+"分";
			return s;
		}else {
			String s=millions/1000+"秒";
			return s;
		}
	}
	public static String getJudgeDate1(long millions){
		long currentTime= System.currentTimeMillis();
		if ((millions-currentTime)>=1000*60*60*24){
			String s=(millions-currentTime)/1000/60/60/24+"天";
			return s;
		}else if ((millions-currentTime)>=1000*60*60){
			String s=(millions-currentTime)/1000/60/60+"时";
			return s;
		}else if ((millions-currentTime)>=1000*60){
			String s=(millions-currentTime)/1000/60+"分";
			return s;
		}else {
			String s=(millions-currentTime)/1000+"秒";
			return s;
		}
	}

	/**
	 * 动态测量listview 指定item个数高度
	 */
	public static void setListViewNumHeight(ListView listView,int num) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < num; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (num - 1));
		params.height += 5;//if without this statement,the listview will be a little short
		listView.setLayoutParams(params);
	}


}
