package com.ted.module_developer

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_DEVELOPER)
class DeveloperActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_developer
    }

    override fun getTitleText(): String {
        return getString(com.ted.lib_base.R.string.app_title_developer)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
    }

}