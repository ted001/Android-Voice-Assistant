package com.ted.module_app_manager

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.helper.ARouterHelper
import kotlinx.android.synthetic.main.activity_app_manager.*

@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManagerActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_app_manager
    }

    override fun getTitleText(): String {
        return getString(com.ted.lib_base.R.string.app_title_app_manager)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        //显示加载
        ll_loading.visibility = View.VISIBLE

        //说明初始化AppView成功
        //waitAppHandler()
    }

}
