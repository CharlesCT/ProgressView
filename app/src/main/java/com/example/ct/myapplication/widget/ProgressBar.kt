package com.example.ct.myapplication.widget
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.example.ct.myapplication.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mPaint:Paint = Paint() //画笔
    private var mOutRadius :Float
    private val defaultSize:Int = 405
    private val miniRadius = 4f
    private var mWidth: Int = 0
    private var mHeight : Int = 0
    private var mMinAngle : Int = 10 //小球夹角

    /**
     * 初始化操作
     */
    init {
        //设置颜色
        mPaint.color = resources.getColor(R.color.colorPrimaryDark,null)
        mPaint.style = Paint.Style.FILL //设置线形状
        mOutRadius = 200F

    }

    /**
     * dp转px
     */
    private fun dp2px(context: Context, dpVal: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        //保证一样高
        height = measureSize(height,heightMode)
        width = measureSize(width,widthMode)
        if (height>width){
            setMeasuredDimension(height,height)
        }else{
            setMeasuredDimension(width,width)
        }

    }


    private fun measureSize(oldSize: Int,mode: Int): Int{

        when(mode){
            MeasureSpec.UNSPECIFIED,MeasureSpec.AT_MOST ->{
                return defaultSize
            }
            MeasureSpec.EXACTLY->{
                return if (oldSize>defaultSize) oldSize else defaultSize
            }
        }
        return oldSize
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //获取真正的宽度
        mWidth = width - paddingLeft - paddingRight
        mHeight = height - paddingTop - paddingBottom
        val count = 360 / mMinAngle //获取小球的个数
        //原点坐标为
        for ( i in 1..count){
            val minX = sin((i*mMinAngle/180F)* PI) * mOutRadius
            val minY = cos((i*mMinAngle/180F)* PI  ) * mOutRadius
            //开始画小球
            canvas!!.drawCircle((mWidth/2f+ minX).toFloat(), (mHeight/2f + minY).toFloat(),miniRadius,mPaint)
        }
    }









}
