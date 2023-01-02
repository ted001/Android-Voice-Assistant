package com.ted.module_constellation

import android.os.Bundle
import com.ted.lib_base.base.BaseActivity
import kotlinx.android.synthetic.main.activity_constellation.*

class ConstellationActivity : BaseActivity() {
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

    }
}