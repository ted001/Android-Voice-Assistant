package com.ted.module_weather

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.helper.ARouterHelper
import com.ted.lib_base.utils.L
import com.ted.lib_base.utils.SpUtils
import com.ted.lib_network.HttpManager
import com.ted.lib_network.bean.WeatherData
import com.ted.module_weather.tools.WeatherIconTools
import com.ted.module_weather.ui.CitySelectActivity
import kotlinx.android.synthetic.main.activity_weather.mAqi
import kotlinx.android.synthetic.main.activity_weather.mDirect
import kotlinx.android.synthetic.main.activity_weather.mHumidity
import kotlinx.android.synthetic.main.activity_weather.mInfo
import kotlinx.android.synthetic.main.activity_weather.mIvWid
import kotlinx.android.synthetic.main.activity_weather.mLineChart
import kotlinx.android.synthetic.main.activity_weather.mPower
import kotlinx.android.synthetic.main.activity_weather.mTemperature
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity() {

    //当前城市
    private var currentCity = "深圳"

    //跳转
    private val codeSelect = 100

    override fun getLayoutId(): Int {
        return R.layout.activity_weather
    }

    override fun getTitleText(): String {
        return getString(com.ted.lib_base.R.string.app_title_weather)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        intent.run {
            val city = getStringExtra("city")
            if (!TextUtils.isEmpty(city)) {
                loadWeatherData(city!!)
            } else {
                //非语音进入,先查询本地
                val localCity = SpUtils.getString("city")
                if (!TextUtils.isEmpty(localCity)) {
                    loadWeatherData(localCity!!)
                } else {
                    startCitySelectActivity()
                }
            }
        }
    }

    //加载天气数据
    private fun loadWeatherData(city: String) {
        currentCity = city
        SpUtils.putString("city", currentCity)

        initChart()
        loadWeather()
    }

    //加载城市数据
    private fun loadWeather() {
        //设置
        supportActionBar?.title = currentCity

        HttpManager.run {
            queryWeather(currentCity, object : Callback<WeatherData> {
                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    L.i("onFailure:$t")
                    Toast.makeText(this@WeatherActivity, getString(R.string.text_load_fail), Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    L.i("onResponse")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.error_code == 10012) {
                                //超过每日可允许的次数了
                                return
                            }
                            //填充数据
                            it.result.realtime.apply {
                                //设置天气 阴
                                mInfo.text = info
                                //设置图片
                                mIvWid.setImageResource(WeatherIconTools.getIcon(wid))
                                //设置温度
                                mTemperature.text =
                                    String.format(
                                        "%s%s",
                                        temperature,
                                        getString(R.string.app_weather_t)
                                    )
                                //设置湿度
                                mHumidity.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(R.string.app_weather_humidity),
                                        humidity
                                    )
                                //设置风向
                                mDirect.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(R.string.app_weather_direct),
                                        direct
                                    )
                                //设置风力
                                mPower.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(R.string.app_weather_power),
                                        power
                                    )
                                //设置空气质量
                                mAqi.text = String.format(
                                    "%s\t%s",
                                    getString(R.string.app_weather_aqi),
                                    aqi
                                )
                            }

                            val data = ArrayList<Entry>()
                            //绘制图表
                            it.result.future.forEachIndexed { index, future ->
                                val temp = future.temperature.substring(0, 2)
                                data.add(Entry((index + 1).toFloat(), temp.toFloat()))
                            }
                            setLineChartData(data)
                        }
                    }
                }

            })
        }
    }

    //初始化图表
    private fun initChart() {

        //=============================基本配置=============================

        //后台绘制
        mLineChart.setDrawGridBackground(true)
        //开启描述文本
        mLineChart.description.isEnabled = true
        mLineChart.description.text = getString(R.string.text_ui_tips)
        //触摸手势
        mLineChart.setTouchEnabled(true)
        //支持缩放
        mLineChart.setScaleEnabled(true)
        //拖拽
        mLineChart.isDragEnabled = true
        //扩展缩放
        mLineChart.setPinchZoom(true)

        //=============================轴配置=============================

        //平均线
        val xLimitLine = LimitLine(10f, "")
        xLimitLine.lineWidth = 4f
        xLimitLine.enableDashedLine(10f, 10f, 0f)
        xLimitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        xLimitLine.textSize = 10f

        val xAxis = mLineChart.xAxis
        xAxis.enableAxisLineDashedLine(10f, 10f, 0f)
        //最大值
        xAxis.mAxisMaximum = 5f
        //最小值
        xAxis.axisMinimum = 1f

        val axisLeft = mLineChart.axisLeft
        axisLeft.enableAxisLineDashedLine(10f, 10f, 0f)
        //最大值
        axisLeft.mAxisMaximum = 40f
        //最小值
        axisLeft.axisMinimum = 20f

        //禁止右边的Y轴
        mLineChart.axisRight.isEnabled = false
    }

    //设置图标数据
    private fun setLineChartData(values: java.util.ArrayList<Entry>) {
        if (mLineChart.data != null && mLineChart.data.dataSetCount > 0) {
            //获取数据容器
            val set = mLineChart.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            mLineChart.data.notifyDataChanged()
            mLineChart.notifyDataSetChanged()
            //如果存在数据才这样去处理
        } else {
            val set = LineDataSet(values, getString(R.string.text_chart_tips_text,currentCity))

            //=============================UI配置=============================
            set.enableDashedLine(10f, 10f, 0f)
            set.setCircleColor(Color.BLACK)
            set.lineWidth = 1f
            set.circleRadius = 3f
            set.setDrawCircleHole(false)
            set.valueTextSize = 10f
            set.formLineWidth = 1f
            set.setDrawFilled(true)
            set.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set.formSize = 15f

            set.fillColor = Color.YELLOW

            //设置数据
            val dataSet = ArrayList<LineDataSet>()
            dataSet.add(set)
            val data = LineData(dataSet as List<ILineDataSet>?)
            mLineChart.data = data
        }

        //=============================UI配置=============================

        //X轴动画
        mLineChart.animateX(2000)
        //刷新
        mLineChart.invalidate()
        //页眉
        val legend = mLineChart.legend
        legend.form = Legend.LegendForm.LINE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.city_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_setting) {
            //跳转到城市选择中去
            startCitySelectActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    //跳转
    private fun startCitySelectActivity() {
        val intent = Intent(this, CitySelectActivity::class.java)
        startActivityForResult(intent, codeSelect)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == codeSelect) {
                data?.let {
                    val city = it.getStringExtra("city")
                    if (!TextUtils.isEmpty(city)) {
                        loadWeatherData(city!!)
                    }
                }
            }
        }
    }
}