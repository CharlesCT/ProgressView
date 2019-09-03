package com.example.ct.myapplication.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.example.ct.myapplication.R
import com.example.ct.myapplication.utils.CommonUtils
import com.example.ct.myapplication.widget.bean.Point
import kotlin.math.cos
import kotlin.math.sin

/**
 * 刻度条View
 */
class ScaleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val defaultSize:Int = 405 //默认大小
    private val ALL_ANGLE = 270 //默认总度数为270
    private var ALL_COOUNT: Int //总的刻度数
    private val DISTANCE = 5 //5度为一个间距
    private val mOutPaint = Paint() //未染色的刻度画笔
    private val mInnerPaint = Paint() //绘制染色的刻度画笔
    private val mTextPaint = Paint()//设置文字画笔
    private val SCALE_WIDTH = 40 //刻度的长度
    private var mProgress:Int = 0 //进度
    private var mRadius = 150f//默认圆圈半径
    private var mWidth = 0
    private var mHeight = 0
    private var starValueAnimator:ValueAnimator ?= null
    init {

        mOutPaint.style = Paint.Style.FILL_AND_STROKE //外围是虚线
        mOutPaint.color = resources.getColor(R.color.black_normal,null)
        mInnerPaint.style = Paint.Style.FILL_AND_STROKE
        mInnerPaint.color = resources.getColor(R.color.blue_normal,null)
        mInnerPaint.strokeWidth = 10f //设置线条宽度
        mInnerPaint.isAntiAlias = true
        mOutPaint.strokeWidth = 10f//
        mOutPaint.isAntiAlias = true //抗锯齿不然会很模糊
        ALL_COOUNT = ALL_ANGLE / DISTANCE
        //设置文字颜色
        mTextPaint.color = resources.getColor(R.color.black_blod,null)
        mTextPaint.textSize = CommonUtils.dp2px(context,20f)//设置大小
        mTextPaint.textAlign = Paint.Align.CENTER //中间往两边绘制


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


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mWidth = width - paddingLeft -paddingRight
        mHeight = height - paddingTop - paddingBottom
        val count : Int = ALL_COOUNT * mProgress /100 //进度刻度
        for (i in 0 until count){
            //开始绘制进度 从 135 到 405 间隔270度
            var angel = (135 + i * DISTANCE)* 3.14159f/180//转为π函数
            var startPoint = Point(width/2 + cos(angel) *mRadius,mHeight/2 + sin(angel)*mRadius)
            var endPoint   = Point(width/2+ cos(angel)*(mRadius + SCALE_WIDTH), mHeight/2+ sin(angel)*(mRadius + SCALE_WIDTH))
            canvas?.drawLine(startPoint.x,startPoint.y,endPoint.x,endPoint.y,mInnerPaint)
            //开始画线
        }
        for (i in count until ALL_COOUNT){
            //开始绘制进度 从 135 到 405 间隔270度
            var angel = (135 + i * DISTANCE)* 3.14159f/180//转为π函数
            var startPoint = Point(width/2 + cos(angel) *mRadius,mHeight/2 + sin(angel)*mRadius)
            var endPoint   = Point(width/2+ cos(angel)*(mRadius + SCALE_WIDTH), mHeight/2+ sin(angel)*(mRadius + SCALE_WIDTH))
            canvas?.drawLine(startPoint.x,startPoint.y,endPoint.x,endPoint.y,mOutPaint)
            //开始画线
        }
        //绘制文字
        canvas?.drawText("当前时速",mWidth/2f,(mHeight/2f - mTextPaint.textSize/2),mTextPaint)
        mTextPaint.typeface = Typeface.DEFAULT_BOLD
        //绘制文字
        canvas?.drawText(mProgress.toString(),mWidth/2f,(mHeight/2f + mTextPaint.textSize/2+10),mTextPaint)
        mTextPaint.typeface = Typeface.DEFAULT
        //绘制文字
        canvas?.drawText("km/h",mWidth/2f,(mHeight/2f + 1.5f*mTextPaint.textSize +10),mTextPaint)

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


    fun startProgress(progres:Int){
        starValueAnimator?.cancel()
        starValueAnimator = ValueAnimator.ofInt(0,100)
        starValueAnimator?.addUpdateListener {
            mProgress = it.animatedValue as Int

        }
        mProgress = progres
        postInvalidate() ///进行重绘
    }

}