package com.ted.module_joke

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_JOKE)
class JokeActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_joke
    }

    override fun getTitleText(): String {
        return getString(com.ted.lib_base.R.string.app_title_joke)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
    }

}