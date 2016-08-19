package com.yxh.ryt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public abstract class CommonAdapter<T> extends BaseAdapter  
{  
    protected LayoutInflater mInflater;  
    protected Context mContext;  
    protected List<T> mDatas=new ArrayList<T>();
    protected final int mItemLayoutId;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public CommonAdapter(Context context, List<T> mDatas ,int itemLayoutId)
    {  
        this.mContext = context;  
        this.mInflater = LayoutInflater.from(mContext);  
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        
    }  
   
    public List<T> getmDatas() {
		return mDatas;
	}

	public void setmDatas(List<T> mDatas) {
		this.mDatas.addAll(mDatas);
	}
	public void clearmDatas() {
		this.mDatas.clear();
		this.notifyDataSetChanged();
	}

	@Override  
    public int getCount()  
    {  
        return mDatas.size();
    }  

    @Override  
    public T getItem(int position)  
    {  
        return mDatas.get(position);  
    }  
  
    @Override  
    public long getItemId(int position)  
    {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent)  
    {  
        final ViewHolder viewHolder = getViewHolder(position, convertView,  
                parent);  
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
  
    }  
  
    public abstract void convert(ViewHolder helper, T item);
  
    private ViewHolder getViewHolder(int position, View convertView,
            ViewGroup parent)  
    {  
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,  
                position,animateFirstListener);
    }  
    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
