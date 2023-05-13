package com.ted.lib_base.map

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.baidu.mapapi.search.poi.*
import com.baidu.mapapi.search.route.*
import com.baidu.mapapi.walknavi.WalkNavigateHelper
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam
import com.ted.lib_base.R
import com.ted.lib_base.helper.ARouterHelper
import com.ted.lib_base.map.PoiOverlay
import com.ted.lib_base.utils.L


/**
 * FileName: MapManager
 * Profile: 地图管理类
 */
object MapManager {

    const val TAG = "MapManager"

    //最大缩放 4 - 21
    const val MAX_ZOOM: Float = 17f

    private var mMapView: MapView? = null
    private var mBaiduMap: BaiduMap? = null
    private var mPoiSearch: PoiSearch? = null

    //上下文
    private lateinit var mContext: Context

    //用户的城市
    private var locationCity: String = ""

    //定位客户端
    private lateinit var mLocationClient: LocationClient

    //地理编码对象
    private lateinit var mCoder: GeoCoder

    //步行规划
    private lateinit var mSearch: RoutePlanSearch

    //定位对外的回调
    private var mOnLocationResultListener: OnLocationResultListener? = null

    //POI对外的回调
    private var mOnPoiResultListener: OnPoiResultListener? = null

    //编码对外的回调
    private var mOnCodeResultListener: OnCodeResultListener? = null

    private var mOnNaviResultListener: OnNaviResultListener? = null

    //开始位置
    private var startLa: Double = 0.0
    private var startLo: Double = 0.0

    //结束位置
    private var endCity: String = ""
    private var endAddress: String = ""

    //初始化
    fun initMap(mContext: Context) {
        this.mContext = mContext

        SDKInitializer.initialize(mContext)

        mLocationClient = LocationClient(mContext)
        //POI
        initPoi()
        //定位
        initLocation()
        //编码
        initCode()
    }

    //绑定地图
    fun bindMapView(mMapView: MapView) {
        this.mMapView = mMapView
        mBaiduMap = mMapView.map

        //默认缩放
        zoomMap(MAX_ZOOM)
        //默认卫星地图
        //setMapType(1)
        //默认打开交通图
        //setTrafficEnabled(true)
        //默认打开热力图
        //setBaiduHeatMapEnabled(true)
        //默认开启
        setMyLocationEnabled(true)

        //初始化步行监听器
        initWalkingRoute()

        //初始化监听
        initListener()
        //比例尺
        showScaleControl(true)


    }

    //==========================操作方法===========================

    //缩放地图
    fun zoomMap(value: Float) {
        val builder = MapStatus.Builder()
        builder.zoom(value)
        mBaiduMap?.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
    }

    //设置默认中心点
    fun setCenterMap(la: Double, lo: Double) {
        val latLng = LatLng(la, lo)
        mBaiduMap?.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng))

        //添加定位点
    }

    /**
     * 0:MAP_TYPE_NORMAL	普通地图（包含3D地图）
     * 1:MAP_TYPE_SATELLITE	卫星图
     * 2:MAP_TYPE_NONE	空白地图
     */
    fun setMapType(index: Int) {
        mBaiduMap?.mapType =
            if (index == 0) BaiduMap.MAP_TYPE_NORMAL else BaiduMap.MAP_TYPE_SATELLITE
    }

    //设置实时路况开关
    fun setTrafficEnabled(isOpen: Boolean) {
        mBaiduMap?.isTrafficEnabled = isOpen
    }

    //设置百度热力图
    fun setBaiduHeatMapEnabled(isOpen: Boolean) {
        mBaiduMap?.isBaiduHeatMapEnabled = isOpen
    }

    //设置定位开关
    private fun setMyLocationEnabled(isOpen: Boolean) {
        //mBaiduMap?.isMyLocationEnabled = isOpen
    }

    //定位开关
    fun setLocationSwitch(isOpen: Boolean, mOnLocationResultListener: OnLocationResultListener?) {
        if (isOpen) {
            this.mOnLocationResultListener = mOnLocationResultListener
            mLocationClient.start()
        } else {
            mLocationClient.stop()
        }
    }

    //POI覆盖物
    fun setPoiImage(poiResult: PoiResult) {
        mBaiduMap?.clear()
        //创建PoiOverlay对象
        val poiOverlay = PoiOverlay(mBaiduMap)
        //设置Poi检索数据
        poiOverlay.setData(poiResult)
        poiOverlay.addToMap()
        poiOverlay.zoomToSpan()
    }

    //===========================定位===========================

    //定位初始化
    private fun initLocation() {
        val option = LocationClientOption()
        option.isOpenGps = true
        option.setCoorType("bd09ll")
        option.setScanSpan(1000)
        //高精度
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true)
        //可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(true)
        //可选，设置是否需要设备方向结果
        option.setNeedDeviceDirect(true)
        option.isLocationNotify = true
        option.setIgnoreKillProcess(true)
        option.setIsNeedLocationDescribe(true)
        mLocationClient.locOption = option
        mLocationClient.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onReceiveLocation(location: BDLocation?) {
                if (location == null || mMapView == null) {
                    return
                }

                if (location.locType == 61 || location.locType == 161) {
                    //设置定位默认中心点
                    //setCenterMap(location.latitude, location.longitude)
                    locationCity = location.city
                    mOnLocationResultListener?.result(
                        location.latitude,
                        location.longitude,
                        location.city,
                        location.addrStr,
                        location.locationDescribe
                    )
                } else {
                    L.i("==>定位失败原因：" + location.locType)
                    mOnLocationResultListener?.fail()
                }

                //停止定位
                setLocationSwitch(false, null)
            }
        })
    }

    //===========================生命周期===========================

    fun onResume() {
        mMapView?.onResume()
    }

    fun onPause() {
        mMapView?.onPause()
    }

    fun onDestroy() {
        mMapView?.onDestroy()
        mMapView = null
        mLocationClient.stop()
        mBaiduMap?.isMyLocationEnabled = false
        mPoiSearch?.destroy()
        mSearch.destroy()
    }

    //===========================POI===========================

    private fun initPoi() {
        mPoiSearch = PoiSearch.newInstance()
        mPoiSearch?.setOnGetPoiSearchResultListener(object : OnGetPoiSearchResultListener {
            override fun onGetPoiIndoorResult(result: PoiIndoorResult?) {
                Log.i(TAG, "====onGetPoiIndoorResult")
            }

            override fun onGetPoiResult(result: PoiResult?) {
                result?.let {
                    if (it.error == SearchResult.ERRORNO.NO_ERROR) {
                        mOnPoiResultListener?.result(it)
                        //在地图上绘制覆盖点
                        setPoiImage(result)
                    }
                }
            }

            override fun onGetPoiDetailResult(result: PoiDetailResult?) {
                //废弃
            }

            override fun onGetPoiDetailResult(result: PoiDetailSearchResult?) {
                Log.i(TAG, "====onGetPoiDetailResult")
            }
        })
    }

    private fun poi(keyword: String, city: String,size: Int) {
        Log.e(TAG, "keyword:$keyword" + "city:$city")
        mPoiSearch?.searchInCity(PoiCitySearchOption().city(city).keyword(keyword).pageCapacity(size))
    }

    //POI检索
    fun poiSearch(keyword: String, city: String, size:Int,mOnPoiResultListener: OnPoiResultListener?) {
        this.mOnPoiResultListener = mOnPoiResultListener
        if (!TextUtils.isEmpty(city)) {
            poi(keyword, city,size)
        } else {
            if (!TextUtils.isEmpty(locationCity)) {
                //定位过，有数据
                poi(keyword, locationCity,size)
            } else {
                setLocationSwitch(true, object : OnLocationResultListener {
                    override fun result(
                        la: Double,
                        lo: Double,
                        city: String,
                        address: String,
                        desc: String
                    ) {
                        poi(keyword, city,size)
                    }

                    override fun fail() {

                    }
                })
            }
        }
    }

    //搜索周边
    fun searchNearby(
        keyword: String,
        la: Double,
        lo: Double,
        size:Int,
        mOnPoiResultListener: OnPoiResultListener?
    ) {
        this.mOnPoiResultListener = mOnPoiResultListener
        mPoiSearch?.searchNearby(
            PoiNearbySearchOption()
                .location(LatLng(la, lo))
                .radius(500)
                .keyword(keyword)
                .pageCapacity(size)
        )
    }

    //===========================路线规划===========================

    //初始化步行监听器
    private fun initWalkingRoute() {
        mSearch = RoutePlanSearch.newInstance()
        mSearch.setOnGetRoutePlanResultListener(object : OnGetRoutePlanResultListener {
            override fun onGetIndoorRouteResult(p0: IndoorRouteResult?) {

            }

            override fun onGetTransitRouteResult(p0: TransitRouteResult?) {

            }

            override fun onGetDrivingRouteResult(p0: DrivingRouteResult?) {

            }

            override fun onGetWalkingRouteResult(walkingRouteResult: WalkingRouteResult?) {
                val overlay = WalkingRouteOverlay(mBaiduMap)
                walkingRouteResult?.let {
                    if (it.routeLines != null) {
                        if (it.routeLines.size > 0) {
                            overlay.setData(walkingRouteResult.routeLines[0])
                            overlay.addToMap()
                            overlay.zoomToSpan()
                            //有路线之后通知外部
                            //五秒中后自动开始导航
                            mOnNaviResultListener?.onStartNavi(
                                startLa,
                                startLo,
                                endCity,
                                endAddress
                            )
                        } else {
                            Log.i(TAG, "线路为0")
                        }
                    } else {
                        Log.i(TAG, "线路为空")
                    }
                }
            }

            override fun onGetMassTransitRouteResult(p0: MassTransitRouteResult?) {

            }

            override fun onGetBikingRouteResult(p0: BikingRouteResult?) {

            }

        })
    }

    /**
     * 开始路线规划（步行）
     * 以自身的位置开始去搜索
     */
    fun startLocationWalkingSearch(toAddress: String, mOnNaviResultListener: OnNaviResultListener) {
        this.mOnNaviResultListener = mOnNaviResultListener
        //定位
        setLocationSwitch(true, object : OnLocationResultListener {
            override fun result(
                la: Double,
                lo: Double,
                city: String,
                address: String,
                desc: String
            ) {
                Log.i(TAG, "定位成功")
                startLa = la
                startLo = lo
                endAddress = toAddress
                endCity = city

                setCenterMap(la, lo)
                startWalkingSearch(city, desc, city, toAddress)
            }

            override fun fail() {
                Log.e(TAG, "定位失败")
            }

        })
    }

    //已有数据的情况下开始步行规划
    fun startWalkingSearch(
        fromCity: String,
        fromAddress: String,
        toCity: String,
        toAddress: String
    ) {
        Log.i(
            TAG,
            "fromCity:" + fromCity +
                    "fromAddress:" + fromAddress +
                    "toCity:" + toCity +
                    "toAddress:" + toAddress
        )
        val stNode = PlanNode.withCityNameAndPlaceName(fromCity, fromAddress)
        val enNode = PlanNode.withCityNameAndPlaceName(toCity, toAddress)
        //发起
        mSearch.walkingSearch(
            (WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode)
        )
    }
    //===========================导航===========================

    fun initNaviEngine(
        mActivity: Activity,
        startLa: Double,
        startLo: Double,
        endLa: Double,
        endLo: Double
    ) {
        WalkNavigateHelper.getInstance().initNaviEngine(mActivity, object : IWEngineInitListener {
            override fun engineInitSuccess() {
                //引擎初始化成功的回调
                routeWalkPlanWithParam(startLa, startLo, endLa, endLo)
                Log.i(TAG, "导航引擎初始化成功")
            }

            override fun engineInitFail() {
                //引擎初始化失败的回调
                Log.i(TAG, "导航引擎初始化失败")
            }
        })
    }

    //配置导航参数
    fun routeWalkPlanWithParam(startLa: Double, startLo: Double, endLa: Double, endLo: Double) {
        val startPt = LatLng(startLa, startLo)
        val endPt = LatLng(endLa, endLo)
        val mParam = WalkNaviLaunchParam().stPt(startPt).endPt(endPt)

        //发起算路
        WalkNavigateHelper.getInstance().routePlanWithParams(mParam, object : IWRoutePlanListener {
            override fun onRoutePlanStart() {
                Log.i(TAG, "开始算路的回调")
            }

            override fun onRoutePlanSuccess() {
                Log.i(TAG, "开始算路的回调成功")
                //算路成功
                ARouterHelper.startActivity(ARouterHelper.PATH_MAP_NAVI)
            }

            override fun onRoutePlanFail(walkRoutePlanError: WalkRoutePlanError) {
                Log.i(TAG, "开始算路的回调失败")
            }
        })
    }

    //===========================事件交互===========================

    //设置Logo显示的位置
    fun setLogoPosition() {
        mMapView?.logoPosition = LogoPosition.logoPostionCenterTop
    }

    //比例尺
    fun showScaleControl(enabled: Boolean) {
        mMapView?.showScaleControl(enabled)
    }

    //缩放按钮
    fun showZoomControls(enable: Boolean) {
        mMapView?.showZoomControls(enable)
    }

    //截图
    fun snapshot() {
        mBaiduMap?.snapshot { L.i("===>截图完成") }
    }

    //添加覆盖物
    fun addMarker(lat: LatLng) {
        //创建marker
        val ooA = MarkerOptions().position(lat).icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(mContext.resources, R.drawable.img_my_location)
            )
        )

        mBaiduMap?.let {
            it.clear()
            val mMarkerA = it.addOverlay(ooA)
        }
    }

    private fun initListener() {
        //单击
        mBaiduMap?.setOnMapClickListener(object : BaiduMap.OnMapClickListener {
            override fun onMapClick(la: LatLng?) {
                L.i("单击")
            }

            override fun onMapPoiClick(poi: MapPoi?) {
                L.i("POI 单击")
            }
        })

        //双击
        mBaiduMap?.setOnMapDoubleClickListener(BaiduMap.OnMapDoubleClickListener { L.i("双击") })

        //长按
        mBaiduMap?.setOnMapLongClickListener { L.i("长按") }
    }

    //===========================地理编码===========================

    //初始化地理编码
    private fun initCode() {
        mCoder = GeoCoder.newInstance()
        mCoder.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
            override fun onGetGeoCodeResult(geoCodeResult: GeoCodeResult?) {
                Log.i(TAG, "正编码成功")
                //正编码
                if (null != geoCodeResult && null != geoCodeResult.location) {
                    if (geoCodeResult.error !== SearchResult.ERRORNO.NO_ERROR) {
                        //没有检索到结果
                        return
                    } else {
                        val latitude = geoCodeResult.location.latitude
                        val longitude = geoCodeResult.location.longitude
                        Log.i(TAG, "正编码成功：$latitude:$longitude")
                        mOnCodeResultListener?.result(latitude, longitude)
                    }
                }
            }

            override fun onGetReverseGeoCodeResult(p0: ReverseGeoCodeResult?) {
                //逆编码
            }

        })
    }

    //开始正编码
    fun startCode(city: String, address: String, mOnCodeResultListener: OnCodeResultListener) {
        this.mOnCodeResultListener = mOnCodeResultListener
        mCoder.geocode(
            GeoCodeOption()
                .city(city)
                .address(address)
        )
    }


    //===========================Open impl===========================

    interface OnLocationResultListener {

        fun result(la: Double, lo: Double, city: String, address: String, desc: String)

        fun fail()
    }

    interface OnPoiResultListener {

        fun result(result: PoiResult)
    }

    interface OnCodeResultListener {

        fun result(codeLa: Double, codeLo: Double)
    }

    interface OnNaviResultListener {

        fun onStartNavi(startLa: Double, startLo: Double, endCity: String, address: String)
    }
}