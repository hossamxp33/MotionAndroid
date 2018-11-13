package com.example.hossam.motion.quiz

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import java.lang.reflect.AccessibleObject.setAccessible
import java.lang.reflect.AccessibleObject.setAccessible





class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    private var mScroller: FixedSpeedScroller? = null
  init {
      try {
          val viewpager = ViewPager::class.java
          val scroller = viewpager.getDeclaredField("mScroller")
          scroller.isAccessible = true
          mScroller = FixedSpeedScroller(context,
                  DecelerateInterpolator())
          scroller.set(this, mScroller)
      } catch (ignored: Exception) {
      }
  }
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }


    /*
 * Set the factor by which the duration will change
 */
    fun setScrollDuration(duration: Int) {
        mScroller?.setScrollDuration(duration)
    }

    private inner class FixedSpeedScroller : Scroller {

        private var mDuration = 500

        constructor(context: Context) : super(context) {}

        constructor(context: Context, interpolator: Interpolator) : super(context, interpolator) {}

        constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(context, interpolator, flywheel) {}

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration)
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration)
        }

        fun setScrollDuration(duration: Int) {
            mDuration = duration
        }
    }

}