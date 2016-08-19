package com.yxh.ryt.DatePicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.yxh.ryt.DatePicker.adapter.NumericWheelAdapter;
import com.yxh.ryt.DatePicker.utils.DisplayUtil;
import com.yxh.ryt.R;

import java.util.Calendar;

/**
 * created by Limxing 2016-3-9
 */
public class DatePickerView  {
	private WheelView year;
	private WheelView month;
	private WheelView day;
	
	private Context mContext;
	private DatePickerListener mListener;
	private Dialog dialog;
	private int fromYear;
	private int toYear;

	private int mYear;
	private int mMonth;
	private int mDay;


	public DatePickerView(Context context,DatePickerListener listener){
		mContext=context;
		mListener=listener;
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;
		int curDate = c.get(Calendar.DATE);
		fromYear=1950;
		mYear=toYear=norYear;
		mMonth=curMonth;
		mDay=curDate;
	}

	/**
	 * 初始化选择器的日期
	 * @param year
	 * @param
	 */
	public void initDate(int year){
		mYear=year;
	}

	public void show(){
		 dialog = new Dialog(mContext, R.style.MMTheme_DatePicker);
		dialog.setCancelable(false);

		dialog.setContentView(getDataPick());
		Window w=dialog.getWindow();
//		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		WindowManager.LayoutParams lp = w.getAttributes();
//		lp.dimAmount=0.4f;

		lp.gravity = Gravity.BOTTOM;
		lp.width = DisplayUtil.getScreenWith(mContext); //设置宽度
		w.setAttributes(lp);

		dialog.show();
	}

	/**
	 * 设置开始结束的年
	 * @param fromYear
	 * @param toYear
	 */
	public void setFromYearAndToYear(int fromYear,int toYear){
		this.fromYear=fromYear;
		this.toYear=toYear;
	}


	private View getDataPick() {
		View view = View.inflate(mContext,R.layout.wheel_date_picker, null);
		view.findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				int n_year = year.getCurrentItem() + fromYear;//
				mListener.finish(n_year+"");
			}
		});

		year = (WheelView) view.findViewById(R.id.year);

		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
				mContext, fromYear, toYear);
		numericWheelAdapter1.setLabel("年");
		numericWheelAdapter1.setTextGravity(Gravity.CENTER);
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(true);//
		year.addScrollingListener(scrollListener);



		year.setVisibleItems(5);

		year.setCurrentItem(mYear - fromYear);

		return view;
	}


	/**
	 *
	 */
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + fromYear;


			mListener.dateChange(n_year+"");
		}
	};


	public void setOnListener(DatePickerListener listener) {

		this.mListener=listener;
		
	}
	
	public interface DatePickerListener{
		 void dateChange(String string);
		 void finish(String string);
	}

}
