package com.example.hossam.motion.quiz.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.example.hossam.motion.quiz.data.model.Question
import com.example.hossam.motion.quiz.view.QuestionFragment
import android.view.ViewGroup




/* class MyViewPagerAdapter(private val context: Context, private val myObject: MyObject) : FragmentStatePagerAdapter() {
    private val images: List<Image>

    val count: Int
        get() = this.images.size()

    init {
        this.images = myObject.getImages()
    }

    fun instantiateItem( collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val imageUrl = Media.getImageUrl(myObject.getObjectId(), images[position].getImageId())
        val layout = inflater.inflate(R.layout.object_details_image, collection, false) as ViewGroup
        val pagerImage = layout.findViewById(R.id.pagerImage)
        Media.setImageFromUrl(pagerImage, imageUrl)//call to GlideApp or Picasso to load the image into the ImageView
        collection.addView(layout)
        return layout
    }

    fun destroyItem( container: ViewGroup, position: Int,  view: Any) {
        container.removeView(view as View)
    }

    fun isViewFromObject( view: View,  `object`: Any): Boolean {
        return view === `object`
    }



} */

class QuestionsPagerAdapter(fragmentManager: android.support.v4.app.FragmentManager, private val Questions: ArrayList<Question>) :
        FragmentPagerAdapter(fragmentManager) {
    private var mCurrentFragment: Fragment? = null
    // 2
    override fun getItem(position: Int): Fragment {
        return QuestionFragment().newInstance(Questions[position], position == count - 1)
    }

    // 3
    override fun getCount(): Int {
        return Questions.size
    }



    fun getCurrentFragment(): Fragment? {
        return mCurrentFragment
    }

    //...
    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (getCurrentFragment() !== `object`) {
            mCurrentFragment = `object` as Fragment
        }
        super.setPrimaryItem(container, position, `object`)
    }



}