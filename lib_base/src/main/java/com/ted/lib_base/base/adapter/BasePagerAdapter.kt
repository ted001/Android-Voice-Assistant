package com.ted.lib_base.base.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class BasePagerAdapter(private val mList: List<View>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        (container as ViewPager).addView(mList[position])
        return mList[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
        (container as ViewPager).removeView(mList[position])
    }
}