package linekong.glass.pk.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Administrator 不能左右滑动的viewpager
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		//此处应该是默认左右侧缓存四页，如果已经创建则不用缓存
		setOffscreenPageLimit(4);
	}

	public NoScrollViewPager(Context context) {
		this(context,null);
	}

	private boolean noScroll = true;

	public void setNoScroll(boolean noScroll) {
		this.noScroll = noScroll;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (noScroll)
			return false;
		else
			return super.onTouchEvent(arg0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (noScroll)
			return false;
		else
			return super.onInterceptTouchEvent(arg0);
	}
}
