package com.yxh.ryt.custemview;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.custemview.widget.OnWheelChangedListener;
import com.yxh.ryt.custemview.widget.WheelView;
import com.yxh.ryt.custemview.widget.XmlParserHandler;
import com.yxh.ryt.custemview.widget.adapters.ArrayWheelAdapter;
import com.yxh.ryt.custemview.widget.adapters.CityWheelAdapter;
import com.yxh.ryt.custemview.widget.adapters.DiQuWheelAdapter;
import com.yxh.ryt.custemview.widget.adapters.ProvinceWheelAdapter;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.CityModel;
import com.yxh.ryt.vo.DistrictModel;
import com.yxh.ryt.vo.ProvinceModel;
import com.yxh.ryt.vo.RongZi;
import com.yxh.ryt.vo.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;

public class WheelSheetDialog implements OnWheelChangedListener {

	/**
	 * 所有省
	 */
	protected List<ProvinceModel> mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String,List<CityModel>> mCitisDatasMap = new HashMap<String, List<CityModel>>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, List<DistrictModel>> mDistrictDatasMap = new HashMap<String,  List<DistrictModel>>();


	/**
	 * 当前省的名称
	 */
	protected ProvinceModel mCurrentProvice;
	/**
	 * 当前市的名称
	 */
	protected CityModel mCurrentCity;
	/**
	 * 当前区的名称
	 */
	protected DistrictModel mCurrentDistrict;

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
		AssetManager asset = AppApplication.getSingleContext().getAssets();
		try {
			InputStream input = asset.open("address.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			mProvinceDatas = handler.getDataList();
			//*/ 初始化默认选中的省、市、区
			if (mProvinceDatas!= null && !mProvinceDatas.isEmpty()) {
				mCurrentProvice = mProvinceDatas.get(0);
				List<CityModel> cityList = mProvinceDatas.get(0).getCityList();
				if (cityList!= null && !cityList.isEmpty()) {
					mCurrentCity = cityList.get(0);
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrict = districtList.get(0);
				}
			}
			Iterator<ProvinceModel> iterator = mProvinceDatas.iterator();
			while (iterator.hasNext()){
				ProvinceModel provinceModel = iterator.next();
				mCitisDatasMap.put(provinceModel.getId(), provinceModel.getCityList());
				Iterator<CityModel> iterator1 = provinceModel.getCityList().iterator();
				while (iterator1.hasNext()){
					CityModel cityModel = iterator1.next();
					mDistrictDatasMap.put(cityModel.getId(),cityModel.getDistrictList());
				}
			}
			//*/
//			mProvinceDatas = new ArrayList<>();
//			for (int i=0; i< provinceList.size(); i++) {
//				// 遍历所有省的数据
//				mProvinceDatas[i] = provinceList.get(i).getName();
//				List<CityModel> cityList = provinceList.get(i).getCityList();
//				String[] cityNames = new String[cityList.size()];
//				for (int j=0; j< cityList.size(); j++) {
//					// 遍历省下面的所有市的数据
//					cityNames[j] = cityList.get(j).getName();
//					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
//					String[] distrinctNameArray = new String[districtList.size()];
//					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
//					for (int k=0; k<districtList.size(); k++) {
//						// 遍历市下面所有区/县的数据
//						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getId());
//						// 区/县对于的邮编，保存到mZipcodeDatasMap
//						mIdDatasMap.put(districtList.get(k).getName(), districtList.get(k).getId());
//						distrinctArray[k] = districtModel;
//						distrinctNameArray[k] = districtModel.getName();
//					}
//					// 市-区/县的数据，保存到mDistrictDatasMap
//					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
//				}
//				// 省-市的数据，保存到mCitisDatasMap
//				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
//			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	private Context context;
	private Dialog dialog;
	private TextView txt_title;
	private TextView txt_cancel,txt_ok;
	private LinearLayout lLayout_content;
	private ScrollView sLayout_content;
	private boolean showTitle = false;
	private List<SheetItem> sheetItemList;
	private Display display;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	OkClickLinster okClickLinster;

	public OkClickLinster getOkClickLinster() {
		return okClickLinster;
	}

	public void setOkClickLinster(OkClickLinster okClickLinster) {
		this.okClickLinster = okClickLinster;
	}

	public WheelSheetDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public WheelSheetDialog builder() {

		View view = LayoutInflater.from(context).inflate(
				R.layout.view_wheelsheet, null);


		view.setMinimumWidth(display.getWidth());

		mViewProvince = (WheelView) view.findViewById(R.id.id_province);
		mViewCity = (WheelView) view.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
		mViewProvince.addChangingListener(this);
		mViewCity.addChangingListener(this);
		mViewDistrict.addChangingListener(this);
		setUpData();
		sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
		lLayout_content = (LinearLayout) view
				.findViewById(R.id.lLayout_content);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
		txt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		txt_ok = (TextView) view.findViewById(R.id.txt_ok);
		txt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				okClickLinster.click(mCurrentProvice,mCurrentCity,mCurrentDistrict);
			}
		});

		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ProvinceWheelAdapter(AppApplication.getSingleContext(), mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrict = mDistrictDatasMap.get(mCurrentCity.getId()).get(newValue);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCity = mCitisDatasMap.get(mCurrentProvice.getId()).get(pCurrent);
		mViewDistrict.setViewAdapter(new DiQuWheelAdapter(AppApplication.getSingleContext(),  mDistrictDatasMap.get(mCurrentCity.getId())));
		mViewDistrict.setCurrentItem(0);
	}
public interface OkClickLinster{
	void click(ProvinceModel p,CityModel c,DistrictModel d);
}
	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProvice = mProvinceDatas.get(pCurrent);
		mViewCity.setViewAdapter(new CityWheelAdapter(AppApplication.getSingleContext(),  mCitisDatasMap.get(mCurrentProvice.getId())));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
	public WheelSheetDialog setTitle(String title) {
		showTitle = true;
		txt_title.setVisibility(View.VISIBLE);
		txt_title.setText(title);
		return this;
	}

	public WheelSheetDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public WheelSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}


	public WheelSheetDialog addSheetItem(String strItem, SheetItemColor color,
			OnSheetItemClickListener listener) {
		if (sheetItemList == null) {
			sheetItemList = new ArrayList<SheetItem>();
		}
		sheetItemList.add(new SheetItem(strItem, color, listener));
		return this;
	}


	private void setSheetItems() {
		if (sheetItemList == null || sheetItemList.size() <= 0) {
			return;
		}

		int size = sheetItemList.size();


		if (size >= 7) {
			LayoutParams params = (LayoutParams) sLayout_content
					.getLayoutParams();
			params.height = display.getHeight() / 2;
			sLayout_content.setLayoutParams(params);
		}

		for (int i = 1; i <= size; i++) {
			final int index = i;
			SheetItem sheetItem = sheetItemList.get(i - 1);
			String strItem = sheetItem.name;
			SheetItemColor color = sheetItem.color;
			final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;

			TextView textView = new TextView(context);
			textView.setText(strItem);
			textView.setTextSize(18);
			textView.setGravity(Gravity.CENTER);


			if (size == 1) {
				if (showTitle) {
					textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
				} else {
					textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
				}
			} else {
				if (showTitle) {
					if (i >= 1 && i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				} else {
					if (i == 1) {
						textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
					} else if (i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				}
			}


			if (color == null) {
				textView.setTextColor(Color.parseColor(SheetItemColor.Blue
						.getName()));
			} else {
				textView.setTextColor(Color.parseColor(color.getName()));
			}


			float scale = context.getResources().getDisplayMetrics().density;
			int height = (int) (45 * scale + 0.5f);
			textView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, height));


			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(index);
					dialog.dismiss();
				}
			});

			lLayout_content.addView(textView);
		}
	}

	public void show() {
		setSheetItems();
		dialog.show();
	}

	public interface OnSheetItemClickListener {
		void onClick(int which);
	}

	public class SheetItem {
		String name;
		OnSheetItemClickListener itemClickListener;
		SheetItemColor color;

		public SheetItem(String name, SheetItemColor color,
				OnSheetItemClickListener itemClickListener) {
			this.name = name;
			this.color = color;
			this.itemClickListener = itemClickListener;
		}
	}
	public enum SheetItemColor {
		Blue("#037BFF"), Red("#FD4A2E");

		private String name;

		private SheetItemColor(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
