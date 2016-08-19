package com.yxh.ryt.custemview;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ccheng on 3/18/14 避免甩行的 textview
 */
public class JustifyTextView extends TextView {

	private int mLineY;
	private int mViewWidth;

	public JustifyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		TextPaint paint = getPaint();
		paint.setColor(getCurrentTextColor());
		paint.drawableState = getDrawableState();
		mViewWidth = getMeasuredWidth();
		String text = (String) getText();
		mLineY = 0;
		mLineY += getTextSize();
		Layout layout = getLayout();
		for (int i = 0; i < layout.getLineCount(); i++) {
			// 每一行的开头
			int lineStart = layout.getLineStart(i);
			// 每一行的结尾
			int lineEnd = layout.getLineEnd(i);
			// 截取每行的字符串
			String line = text.substring(lineStart, lineEnd);

			float width = StaticLayout.getDesiredWidth(text, lineStart,
					lineEnd, getPaint());

			/*
			 * 自己修改
			 * *********************************************************************
			 * 判断是否是最后一行 最后一行就不用加空格了 直接画出上面的字 就可以了
			 */

			if (i == layout.getLineCount() - 1) {
				canvas.drawText(line, 0, mLineY, paint);
			} else {
				if (needScale(line)) {
					drawScaledText(canvas, lineStart, line, width);
				} else {
					canvas.drawText(line, 0, mLineY, paint);
				}
			}
			/*
			 * *********************************************************************
			 */

			mLineY += getLineHeight();
		}
	}

	private void drawScaledText(Canvas canvas, int lineStart, String line,
			float lineWidth) {
		float x = 0;
		if (isFirstLineOfParagraph(lineStart, line)) {
			String blanks = "  ";
			canvas.drawText(blanks, x, mLineY, getPaint());
			float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
			x += bw;

			line = line.substring(3);
		}

		float d = (mViewWidth - lineWidth) / line.length() - 1;
		for (int i = 0; i < line.length(); i++) {
			String c = String.valueOf(line.charAt(i));
			float cw = StaticLayout.getDesiredWidth(c, getPaint());
			canvas.drawText(c, x, mLineY, getPaint());
			x += cw + d;
		}
	}

	private boolean isFirstLineOfParagraph(int lineStart, String line) {
		return line.length() > 3 && line.charAt(0) == ' '
				&& line.charAt(1) == ' ';
	}

	/*
	 * 是否需要空格
	 */
	private boolean needScale(String line) {
		if (line.length() == 0) {
			return false;
		} else {
			return line.charAt(line.length() - 1) != '\n';
		}
	}

}
