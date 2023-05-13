package com.ted.module_map

import android.Manifest
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.poi.PoiResult
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.base.adapter.CommonAdapter
import com.ted.lib_base.base.adapter.CommonViewHolder
import com.ted.lib_base.helper.ARouterHelper
import com.ted.lib_base.map.MapManager
import com.ted.lib_base.utils.L
import com.ted.lib_voice.manager.VoiceManager
import com.yanzhenjie.permission.Action
import kotlinx.android.synthetic.main.activity_map.etSearchPoi
import kotlinx.android.synthetic.main.activity_map.mMapView
import kotlinx.android.synthetic.main.activity_map.ryPoiView

/**
 * FileName: MapActivity
 * Profile: 地图
 */
@Route(path = ARouterHelper.PATH_MAP)
class MapActivity : BaseActivity() {

    //权限
    private val permission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val mHandler by lazy { Handler() }

    override fun getLayoutId(): Int {
        return R.layout.activity_map
    }

    override fun getTitleText(): String {
        return getString(com.ted.lib_base.R.string.app_title_map)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

        MapManager.bindMapView(mMapView)

        initPoiList()

        //判断
        //动态权限
        if (checkPermission(permission)) {
            //Option
            startLocation()
        } else {
            requestPermission(permission,
                Action<List<String>> {
                    //Option
                    startLocation()
                })
        }

        //监听输入事件
        etSearchPoi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty()) {
                        ryPoiView.visibility = View.VISIBLE

                        if (mList.size > 0) {
                            mList.clear()
                            mPoiAdapter.notifyDataSetChanged()
                        }

                        poiSearch(it.toString())
                    } else {
                        ryPoiView.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

    }

    private val mList = ArrayList<PoiInfo>()
    private lateinit var mPoiAdapter: CommonAdapter<PoiInfo>

    //初始化列表
    private fun initPoiList() {
        ryPoiView.layoutManager = LinearLayoutManager(this)
        ryPoiView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mPoiAdapter = CommonAdapter(mList, object : CommonAdapter.OnBindDataListener<PoiInfo> {
            override fun getLayoutId(type: Int): Int {
                return R.layout.layout_poi_item
            }

            override fun onBindViewHolder(
                model: PoiInfo,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                viewHolder.setText(R.id.tvTitle, model.name)
                viewHolder.setText(R.id.tvContent, model.address)

                viewHolder.getView(R.id.ivStartNavi).setOnClickListener {
                    MapManager.setCenterMap(model.location.latitude,model.location.longitude)
                    MapManager.zoomMap(MapManager.MAX_ZOOM)
                }
            }

        })
        ryPoiView.adapter = mPoiAdapter
    }

    //搜索
    private fun poiSearch(poi: String) {
        MapManager.poiSearch(poi, "", 3, object : MapManager.OnPoiResultListener {
            override fun result(result: PoiResult) {
                if (mList.size > 0) {
                    mList.clear()
                }
                mList.addAll(result.allPoi)
                mPoiAdapter.notifyDataSetChanged()
            }

        })
    }

    //开启定位
    private fun startLocation() {
        //获取关键字
        val keyword = intent.getStringExtra("keyword")
        when (intent.getStringExtra("type")) {
            "poi" -> keyword?.let { searchNearByPoi(it) }
            "route" -> keyword?.let { route(it) }
            else -> showMyLocation() //直接点进来的话，显示自身的位置
        }
    }

    //路线规划
    private fun route(address: String) {
        L.i("开始路线规划")
        MapManager.startLocationWalkingSearch(address, object : MapManager.OnNaviResultListener {
            override fun onStartNavi(
                startLa: Double,
                startLo: Double,
                endCity: String,
                address: String
            ) {
                //5S
                VoiceManager.ttsStart(getString(R.string.text_start_navi_tts))
                mHandler.postDelayed({
                    MapManager.startCode(
                        endCity,
                        address,
                        object : MapManager.OnCodeResultListener {
                            override fun result(codeLa: Double, codeLo: Double) {
                                L.i("编码成功")
                                MapManager.initNaviEngine(
                                    this@MapActivity,
                                    startLa, startLo,
                                    codeLa, codeLo
                                )
                            }

                        })
                }, 5 * 1000)
            }

        })
    }

    //显示自身的位置
    private fun showMyLocation() {
        MapManager.setLocationSwitch(true, object : MapManager.OnLocationResultListener {
            override fun result(
                la: Double,
                lo: Double,
                city: String,
                address: String,
                desc: String
            ) {
                //设置中心点
                MapManager.setCenterMap(la, lo)
                L.i("定位成功：" + address + "desc:" + desc)
                //添加个性化覆盖物
                MapManager.addMarker(LatLng(la, lo))
            }

            override fun fail() {
                L.i("定位失败")
            }

        })
    }

    //查找周边POI
    private fun searchNearByPoi(keyword: String) {
        L.i("searchNearByPoi$keyword")
        MapManager.setLocationSwitch(true, object : MapManager.OnLocationResultListener {
            override fun result(
                la: Double,
                lo: Double,
                city: String,
                address: String,
                desc: String
            ) {
                //设置中心点
                MapManager.setCenterMap(la, lo)
                MapManager.searchNearby(
                    keyword,
                    la,
                    lo,
                    10,
                    object : MapManager.OnPoiResultListener {
                        override fun result(result: PoiResult) {
                            //在UI上绘制视图
                        }
                    })
                L.i("定位成功：" + address + "desc:" + desc)
            }

            override fun fail() {
                L.i("定位失败")
            }

        })
    }

    override fun onResume() {
        super.onResume()
        MapManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        MapManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        MapManager.onDestroy()
    }
}