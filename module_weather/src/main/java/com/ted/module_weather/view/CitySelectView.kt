package com.ted.module_weather.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 * FileName: CitySelectView
 * Profile: 城市导航View
 */
class CitySelectView : View {

    //View宽
    private var viewWidth = 0

    //View高
    private var viewHeight = 0

    //画笔
    private val mPaint by lazy { Paint() }

    //数据源
    private val mList = ArrayList<String>()

    //间距的高
    private var itemHeight = 0

    //选中的下标
    private var checkIndex = -1

    //选中的文本大小
    private val checkTextSize = 28f

    //未选中的文本大小
    private val unCheckTextSize = 18f

    //选中的文本颜色
    private val checkTextColor = Color.RED

    //未选中的文本颜色
    private val unCheckTextColor = Color.BLACK

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    //初始化
    private fun initView() {
        //抗锯齿
        mPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let { drawCity(it) }
    }

    /**
     * 绘制城市
     */
    private fun drawCity(canvas: Canvas) {
        if (mList.size > 0) {
            //得到每个城市文本的高度
            itemHeight = viewHeight / mList.size
            //遍历数据源
            mList.forEachIndexed { index, text ->
                if (checkIndex == index) {
                    mPaint.color = checkTextColor
                    mPaint.textSize = checkTextSize
                } else {
                    mPaint.color = unCheckTextColor
                    mPaint.textSize = unCheckTextSize
                }
                //绘制文本
                val textX = (viewWidth - mPaint.measureText(text)) / 2
                val textY = (itemHeight * index + itemHeight).toFloat()
                canvas.drawText(text, textX, textY, mPaint)
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    mOnViewResultListener?.uiChange(true)
                }
                MotionEvent.ACTION_MOVE -> {
                    //这里做处理 当手指按下去的时候，如何去计算选中的下标
                    val oldCheck = checkIndex
                    val check = event.y / viewHeight * mList.size
                    if (oldCheck != checkTextColor) {
                        var index = check.toInt()
                        //防止数组越界
                        if (index < 0) {
                            index = 0
                        } else if (index >= mList.size) {
                            index = mList.size - 1
                        }
                        mOnViewResultListener?.valueInput(mList[index])
                        //往外传递数据
                        checkIndex = check.toInt()
                        invalidate()
                    } else {
                        //重复
                    }
                }
                MotionEvent.ACTION_UP -> {
                    mOnViewResultListener?.uiChange(false)
                }
                else -> {
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    //=============================Open Fun=============================

    /**
     * 设置数据源
     */
    fun setCity(list: List<String>) {
        if (mList.size > 0) {
            mList.clear()
        }
        mList.addAll(list)
        invalidate()
    }

    /**
     * 设置选中
     */
    fun setCheckIndex(index: Int) {
        checkIndex = index
        invalidate()
    }

    //=============================Open Impl=============================

    interface OnViewResultListener {
        //外部控件隐藏 显示
        fun uiChange(uiShow: Boolean)

        //值传递
        fun valueInput(text: String)
    }

    private var mOnViewResultListener: OnViewResultListener? = null

    fun setOnViewResultListener(mOnViewResultListener: OnViewResultListener) {
        this.mOnViewResultListener = mOnViewResultListener
    }

}