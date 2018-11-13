package com.example.hossam.motion.quiz.adapters

import android.support.v4.view.ViewPager
import android.view.View
import android.support.v4.view.ViewCompat.setTranslationX
import android.widget.ImageView
import android.support.v4.view.ViewCompat.setTranslationX
import android.support.v4.view.ViewCompat.setTranslationX


class VerticalFlipTransformation : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {

        page.translationX = -position * page.width
        page.cameraDistance = 12000f

        if (position < 0.5 && position > -0.5) {
            page.visibility = View.VISIBLE
        } else {
            page.visibility = View.INVISIBLE
        }



        if (position < -1) {     // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.alpha = 0f

        } else if (position <= 0) {    // [-1,0]
            page.alpha = 1f
            page.rotationY = 180 * (1 - Math.abs(position) + 1)

        } else if (position <= 1) {    // (0,1]
            page.alpha = 1f
            page.rotationY = -180 * (1 - Math.abs(position) + 1)

        } else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.alpha = 0f

        }


    }
}


class CubeOutRotationTransformation : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {

        if (position < -1) {    // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.alpha = 0f

        } else if (position <= 0) {    // [-1,0]
            page.alpha = 1f
            page.pivotX = page.width.toFloat()
            page.rotationY = -90 * Math.abs(position)

        } else if (position <= 1) {    // (0,1]
            page.alpha = 1f
            page.pivotX = 0f
            page.rotationY = 90 * Math.abs(position)

        } else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.alpha = 0f

        }
    }
}
