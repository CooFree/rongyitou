package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.widget.AbsListView;
@SuppressLint("ValidFragment")
public abstract class ScrollTabHolderFragment extends BaseFragment implements ScrollTabHolder {

	private int fragmentId;

	protected ScrollTabHolder scrollTabHolder;

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		this.scrollTabHolder = scrollTabHolder;
	}

	public ScrollTabHolderFragment() {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount,
			int pagePosition) {
		// nothing
	}

	@Override
	public void onHeaderScroll(boolean isRefreashing, int value, int pagePosition) {

	}

	public int getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
	}
}