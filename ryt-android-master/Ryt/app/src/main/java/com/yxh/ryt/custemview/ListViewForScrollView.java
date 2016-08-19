package com.yxh.ryt.custemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yxh.ryt.R;

public class ListViewForScrollView extends ListView {
	public ListViewForScrollView(Context context) {
		super(context);
	}
	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public ListViewForScrollView(Context context, AttributeSet attrs,
								 int defStyle) {
		super(context, attrs, defStyle);
	}
	//@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST );
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	private int headerViewHeight;
	private float downY;
	private View headerView;
	/** 下拉刷新状态 */
	private static final int STATE_DOWN_REFRESH = 0;
	/** 松开刷新状态 */
	private static final int STATE_UP_REFRESH = 1;
	/** 正在刷新状态 */
	private static final int STATE_REFRESHING = 2;
	/** 当前刷新的状态 ，默认是下拉刷新状态 */
	private int currentState = STATE_DOWN_REFRESH;
	private TextView tv_state;
	private RotateAnimation upAnim;
	private RotateAnimation downAnim;
	private ImageView iv_arrow;
	private ProgressBar progressBar;
	public View footerView;
	/** 是否已经正在加载更多了 */
	private boolean loadingMore;
	private int footerViewHeight;

	private void init() {
		initFooterView();
		showFooterView();
		setSelection(getCount() - 1);
	}

	public View initFooterView() {
		footerView = View.inflate(getContext(), R.layout.footerview, null);
		footerView.measure(0, 0);	// 手动调用测量方法
		footerViewHeight = footerView.getMeasuredHeight();
		hideFooterView();
		addFooterView(footerView);
		return footerView;
	}

	public void hideFooterView() {
		int paddingTop = -footerViewHeight;
		footerView.setPadding(0, paddingTop, 0, 0);
	}

	public void showFooterView() {
		int paddingTop = 0;
		footerView.setPadding(0, paddingTop, 0, 0);
		footerView.setVisibility(VISIBLE);
	}


	/*@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		int count = adapter.getCount();
		if (count >= 5) {
			showFooterView();
		}else {
			hideFooterView();
		}
	}
*/



}