package com.ted.module_constellation.fragment

import android.widget.Toast
import com.ted.lib_base.base.BaseFragment
import com.ted.lib_base.utils.L
import com.ted.lib_network.HttpManager
import com.ted.lib_network.bean.TodayData
import com.ted.module_constellation.R
import kotlinx.android.synthetic.main.fragment_today.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * FileName: ToDayFragment
 * Profile: 今天/明天 星座运势
 */
class ToDayFragment(private val isToday: Boolean, val name: String) : BaseFragment(),
    Callback<TodayData> {

    override fun getLayoutId(): Int {
        return R.layout.fragment_today
    }

    override fun initView() {
        if (isToday) {
            loadToday()
        } else {
            loadTomorrow()
        }
    }

    //加载今天的数据
    private fun loadToday() {
        HttpManager.queryTodayConsTellInfo(name, this)
    }

    //加载明天的数据
    private fun loadTomorrow() {
        HttpManager.queryTomorrowConsTellInfo(name, this)
    }

    override fun onFailure(call: Call<TodayData>, t: Throwable) {
        Toast.makeText(activity, getString(R.string.text_load_fail), Toast.LENGTH_LONG).show()
    }

    override fun onResponse(call: Call<TodayData>, response: Response<TodayData>) {
        val data = response.body()
        data?.let {

            L.i("it:$it")

            tvName.text = getString(R.string.text_con_tell_name, it.name)
            tvTime.text = getString(R.string.text_con_tell_time, it.datetime)
            tvNumber.text = getString(R.string.text_con_tell_num, it.number)
            tvFriend.text = getString(R.string.text_con_tell_pair, it.QFriend)
            tvColor.text = getString(R.string.text_con_tell_color, it.color)
            tvSummary.text = getString(R.string.text_con_tell_desc, it.summary)

            //pbAll.progress = it.all.substring(0, it.all.length - 1).toInt()
            //pbHealth.progress = it.health.substring(0, it.health.length - 1).toInt()
            //pbLove.progress = it.love.substring(0, it.love.length - 1).toInt()
            //pbMoney.progress = it.money.substring(0, it.money.length - 1).toInt()
            //pbWork.progress = it.work.substring(0, it.work.length - 1).toInt()

            pbAll.progress = it.all
            pbHealth.progress = it.health
            pbLove.progress = it.love
            pbMoney.progress = it.money
            pbWork.progress = it.work
        }
    }
}