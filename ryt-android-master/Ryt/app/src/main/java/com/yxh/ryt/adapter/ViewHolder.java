package com.yxh.ryt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yxh.ryt.R;


public class ViewHolder
{  
	   private final SparseArray<View> mViews;
		private int mPosition;
	    private View mConvertView;
	    private ImageLoadingListener animateFirstListener ;
	    DisplayImageOptions options;
	    private ViewHolder(Context context, ViewGroup parent,int layoutId,  
	            int position,ImageLoadingListener animateFirstListener)
	    {
	        this.mPosition = position;  
	        this.animateFirstListener=animateFirstListener;
	        this.mViews = new SparseArray<View>();  
	        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,  
	                false);
	        // setTag  
	        mConvertView.setTag(this); 
	        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
			builder.showImageOnLoading(R.mipmap.default_icon);
			builder.showImageForEmptyUri(R.mipmap.default_icon);
			builder.showImageOnFail(R.mipmap.default_icon);
	        builder.cacheInMemory(true);
			builder.cacheOnDisk(true);
			builder.considerExifParams(true);
//	        if(id==0){
	        	builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
//	        }
//	        if(id==1){
//	        	builder.imageScaleType(ImageScaleType.EXACTLY);
//	        }
			options=builder.build();
	    }  
	  
	    public static ViewHolder get(Context context, View convertView,
	            ViewGroup parent, int layoutId, int position, ImageLoadingListener animateFirstListener)
	    {  
	        if (convertView == null)  
	        {  
	            return new ViewHolder(context, parent, layoutId, position,animateFirstListener);
	        }else{
				ViewHolder holder = (ViewHolder) convertView.getTag();
				holder.mPosition = position;
				return holder;
			}

	    }  
	  
	    public View getConvertView()  
	    {  
	        return mConvertView;  
	    }  
	  
	    public <T extends View> T getView(int viewId)
	    {  
	        View view = mViews.get(viewId);  
	        if (view == null)  
	        {  
	            view = mConvertView.findViewById(viewId);  
	            mViews.put(viewId, view);  
	        }  
	        return (T) view;  
	    }  
	  
	    public ViewHolder setText(int viewId, String text)
	    {  
	        TextView view = getView(viewId);  
	        view.setText(text);
	        return this;
	    }
		public ViewHolder setColor(int viewId, int color)
		{
			View view = getView(viewId);
			view.setBackgroundColor(color);
			return this;
		}
		public ViewHolder setProgress(int viewId, int value)
		{
			View view = getView(viewId);
			((ProgressBar)view).setProgress(value);
			return this;
		}
	    public ViewHolder setText(int viewId, SpannableStringBuilder text)  
	    {  
	    	TextView view = getView(viewId);  
	    	view.setText(text);  
	    	return this;  
	    }
	public ViewHolder setText(int viewId, SpannableString text)
	{
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}
	public ViewHolder setText(int viewId, StringBuilder text)
	{
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}
	  	public ViewHolder setVisible(int viewId){
			View view=getView(viewId);
			view.setVisibility(View.VISIBLE);
			return this;
		}
	    public ViewHolder setCheck(int viewId,boolean checked){
	    	CheckBox view = getView(viewId);  
	    	view.setChecked(checked);
	    	return this;  
	    }
	    public ViewHolder showjg(int viewId){
	    	View view = getView(viewId);  
	    	view.setVisibility(View.VISIBLE);
	    	return this;  
	    }
	    
	    public ViewHolder setImageResource(int viewId, int drawableId)
	    {  
	        ImageView view = getView(viewId);  
	        view.setImageResource(drawableId);  
	  
	        return this;  
	    }  
	  
	    public ViewHolder setImageBitmap(int viewId, Bitmap bm)
	    {  
	        ImageView view = getView(viewId);  
	        view.setImageBitmap(bm);  
	        return this;  
	    }  
	  
	    public ViewHolder setImageByUrl(int viewId, String url)
	    {  
	    	ImageLoader.getInstance().displayImage(url, (ImageView)getView(viewId), options, animateFirstListener);
	        return this;  
	    }  
	   
	    public int getPosition()  
	    {
			Log.d("getPosition()",mPosition+"");
			return mPosition;
	    }  
	  
	}  