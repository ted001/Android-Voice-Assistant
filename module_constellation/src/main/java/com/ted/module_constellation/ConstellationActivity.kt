package com.ted.module_constellation

import android.graphics.Color
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.helper.ARouterHelper
import com.ted.lib_base.utils.SpUtils
import com.ted.module_constellation.fragment.MonthFragment
import com.ted.module_constellation.fragment.ToDayFragment
import com.ted.module_constellation.fragment.WeekFragment
import com.ted.module_constellation.fragment.YearFragment
import kotlinx.android.synthetic.main.activity_constellation.*

@Route(path = ARouterHelper.PATH_CONSTELLATION)
class ConstellationActivity : BaseActivity() {

    private lateinit var mTodayFragment: ToDayFragment
    private lateinit var mTomorrowFragment: ToDayFragment
    private lateinit var mWeekFragment: WeekFragment
    private lateinit var mMonthFragment: MonthFragment
    private lateinit var mYearFragment: YearFragment

    private val mListFragment = ArrayList<Fragment>()

    override fun getLayoutId(): Int {
        return R.layout.activity_constellation
    }

    override fun getTitleText(): String {
        return getString(com.ted.lib_base.R.string.app_title_constellation)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        val name = intent.getStringExtra("name")
        if (!TextUtils.isEmpty(name)) {
            //语音进来的
            if (name != null) {
                initFragment(name)
            }
        } else {
            //主页进来的,读取历史
            val consTellName = SpUtils.getString("consTell")
            consTellName?.let {
                if (!TextUtils.isEmpty(it)) {
                    initFragment(it)
                } else {
                    initFragment(getString(R.string.text_def_con_tell))
                }
            }
        }

        //View控制
        mTvToday.setOnClickListener {
            checkTab(true, 0)
        }
        mTvTomorrow.setOnClickListener {
            checkTab(true, 1)
        }
        mTvWeek.setOnClickListener {
            checkTab(true, 2)
        }
        mTvMonth.setOnClickListener {
            checkTab(true, 3)
        }
        mTvYear.setOnClickListener {
            checkTab(true, 4)
        }
    }

    //ViewPage + Fragment 实现滑动切换页面
    private fun initFragment(name: String) {

        SpUtils.putString("consTell", name)

        //设置标题
        supportActionBar?.title = name

        mTodayFragment = ToDayFragment(true, name)
        mTomorrowFragment = ToDayFragment(false, name)
        mWeekFragment = WeekFragment(name)
        mMonthFragment = MonthFragment(name)
        mYearFragment = YearFragment(name)

        mListFragment.add(mTodayFragment)
        mListFragment.add(mTomorrowFragment)
        mListFragment.add(mWeekFragment)
        mListFragment.add(mMonthFragment)
        mListFragment.add(mYearFragment)

        //初始化页面
        initViewPager()
    }

    private fun initViewPager() {
        mViewPager.adapter = PageFragmentAdapter(supportFragmentManager)
        mViewPager.offscreenPageLimit = mListFragment.size
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                checkTab(false, position)
            }

        })

        //等待全部初始化之后采取做UI控制操作
        checkTab(false, 0)
    }

    //适配器
    inner class PageFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return mListFragment[position]
        }

        override fun getCount(): Int {
            return mListFragment.size
        }
    }

    //切换选项卡
    private fun checkTab(isClick: Boolean, index: Int) {

        if (isClick) {
            mViewPager.currentItem = index
        }

        mTvToday.setTextColor(if (index == 0) Color.RED else Color.BLACK)
        mTvTomorrow.setTextColor(if (index == 1) Color.RED else Color.BLACK)
        mTvWeek.setTextColor(if (index == 2) Color.RED else Color.BLACK)
        mTvMonth.setTextColor(if (index == 3) Color.RED else Color.BLACK)
        mTvYear.setTextColor(if (index == 4) Color.RED else Color.BLACK)
    }
}