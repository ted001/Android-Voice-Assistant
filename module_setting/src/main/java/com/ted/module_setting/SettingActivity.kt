package com.ted.module_setting

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_SETTING)
class SettingActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun getTitleText(): String {
        return getString(com.ted.lib_base.R.string.app_title_system_setting)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

    }
}