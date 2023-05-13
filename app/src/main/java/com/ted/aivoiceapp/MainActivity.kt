package com.ted.aivoiceapp

import android.Manifest
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ted.aivoiceapp.data.MainListData
import com.ted.aivoiceapp.service.VoiceService
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.base.adapter.BasePagerAdapter
import com.ted.lib_base.helper.ARouterHelper
import com.ted.lib_base.helper.`fun`.ContactHelper
import com.yanzhenjie.permission.Action
import com.zhy.magicviewpager.transformer.ScaleInTransformer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    //不建议一股脑的申请权限 而是应该根据使用到的场景是让用户同意权限
    private val permission = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.VIBRATE,
        Manifest.permission.CAMERA
    )

    private val mList = ArrayList<MainListData>()
    private val mListView = ArrayList<View>()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getTitleText(): String {
        return getString(R.string.app_name)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun initView() {
        //动态权限
        if (checkPermission(permission)) {
            linkService()
        } else {
            requestPermission(
                permission,
                Action<List<String>> { linkService() })
        }

        //窗口权限
        if (!checkWindowPermission()) {
            requestWindowPermission(packageName)
        }

        initPagerData()
        initPagerView()

    }

    //初始化View
    private fun initPagerView() {
        mViewPager.pageMargin = 20
        mViewPager.offscreenPageLimit = mList.size
        mViewPager.adapter = BasePagerAdapter(mListView)
        mViewPager.setPageTransformer(true, ScaleInTransformer())
    }

    //初始化数据
    private fun initPagerData() {
        val title = resources.getStringArray(R.array.MainTitleArray)
        val color = resources.getIntArray(R.array.MainColorArray)
        val icon = resources.obtainTypedArray(R.array.MainIconArray)

        for ((index, value) in title.withIndex()) {
            mList.add(MainListData(value, icon.getResourceId(index, 0), color[index]))
        }

        //非调试版本去除工程模式
        if (!BuildConfig.DEBUG) {
            mList.removeAt(mList.size - 1)
        }

        val windowHeight = windowManager.defaultDisplay.height
        mList.forEach {
            val view = View.inflate(this, R.layout.layout_main_list, null)
            val mCvMainView = view.findViewById<CardView>(R.id.mCvMainView)
            val mIvMainIcon = view.findViewById<ImageView>(R.id.mIvMainIcon)
            val mTvMainText = view.findViewById<TextView>(R.id.mTvMainText)
            mCvMainView.setBackgroundColor(it.color)
            mIvMainIcon.setImageResource(it.icon)
            mTvMainText.text = it.title

            mCvMainView.layoutParams?.let { lp ->
                lp.height = windowHeight / 5 * 3
            }

            //点击事件
            view.setOnClickListener { _ ->
                when (it.icon) {
                    R.drawable.img_main_weather -> ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)
                    R.drawable.img_mian_contell -> ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
                    R.drawable.img_main_joke_icon -> ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
                    R.drawable.img_main_map_icon -> ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
                    R.drawable.img_main_app_manager -> ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
                    R.drawable.img_main_voice_setting -> ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
                    R.drawable.img_main_system_setting -> ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
                    R.drawable.img_main_developer -> ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)
                }
            }

            mListView.add(view)
        }
    }

    //连接服务
    private fun linkService() {
        //读取联系人
        ContactHelper.initHelper(this)

        startService(Intent(this, VoiceService::class.java))
    }

}