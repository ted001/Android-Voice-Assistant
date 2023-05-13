package com.ted.module_constellation.fragment

import android.widget.Toast
import com.ted.lib_base.base.BaseFragment
import com.ted.lib_base.utils.L
import com.ted.lib_network.HttpManager
import com.ted.lib_network.bean.YearData
import com.ted.module_constellation.R
import kotlinx.android.synthetic.main.fragment_year.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * FileName: YearFragment
 * Profile: 今年星座运势
 */
class YearFragment(val name: String) : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_year
    }

    override fun initView() {
        loadYearData()
    }

    //加载年份数据
    private fun loadYearData() {
        HttpManager.queryYearConsTellInfo(name, object : Callback<YearData> {
            override fun onFailure(call: Call<YearData>, t: Throwable) {
                Toast.makeText(activity, getString(R.string.text_load_fail), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<YearData>, response: Response<YearData>) {
                val data = response.body()
                data?.let {
                    L.i("it:$it")

                    tvName.text = it.name
                    tvDate.text = it.date

                    tvInfo.text = it.mima.info
                    tvInfoText.text = it.mima.text[0]

                    tvCareer.text = it.career[0]
                    tvLove.text = it.love[0]
                    tvFinance.text = it.finance[0]
                }
            }

        })
    }
}