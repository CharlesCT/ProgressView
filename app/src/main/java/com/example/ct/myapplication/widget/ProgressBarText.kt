package com.example.ct.myapplication.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.graphics.Rect
import com.example.ct.myapplication.R
import com.example.ct.myapplication.utils.CommonUtils


class ProgressBarText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {



    private var mPaint: Paint = Paint() //绘制圆弧的

    private var defaultSize:Int = 300 //默认大小
    private var mTextPaint: Paint = Paint()//画字体的颜色

    private var mOutRadius: Float = CommonUtils.dp2px(context, 40F) //圆弧大小
    private var mWidth  = 0
    private var mHeight = 0
    private var mStr = "0%"
    private var mAngle  = 0f
    private val mInnerRadius:Float = CommonUtils.dp2px(context, 35F)  //中心的圆设置大小为
    private val mCirclePain: Paint = Paint()
    init {
        mPaint.color = resources.getColor(R.color.colorPrimaryDark,null)
        mPaint.textSize = CommonUtils.dp2px(context,100F)
        mTextPaint.color = resources.getColor(R.color.colorPrimaryDark,null)
        mTextPaint.textSize = CommonUtils.dp2px(context,18F)
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true //开启抗锯齿
        mTextPaint.style = Paint.Style.FILL
        mCirclePain.style = Paint.Style.FILL
        mCirclePain.color = resources.getColor(R.color.normal,null)
        mCirclePain.isAntiAlias = true //开启抗锯齿
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







    override fun onDraw(canvas: Canvas) {
        //开始画一个圆弧
        mWidth = width -paddingLeft - paddingRight
        mHeight = height - paddingBottom - paddingTop
        //绘制扇形区域
        val outRect = RectF()
        outRect.set(mWidth/2 - mOutRadius,mHeight/2 - mOutRadius, mWidth/2 - mOutRadius +2*mOutRadius, mHeight/2 - mOutRadius + 2*mOutRadius)
        canvas.drawArc(outRect,-90f,mAngle,true,mPaint)
        //画一个内圆圈
        canvas.drawCircle(mWidth/2f,mHeight/2f,mInnerRadius,mCirclePain)
        //画字体
        val rect = Rect()
        //测量字体的宽高
        mTextPaint.getTextBounds(mStr, 0, mStr.length, rect)
        val fontWidth = rect.width()
        val fontHeight = rect.height()
        canvas.drawText(mStr,mWidth/2f -fontWidth/2,mHeight/2f + fontHeight/3,mTextPaint)




    }

    fun updateProgress(progress: Int){
        val stringBuilder: StringBuilder = java.lang.StringBuilder()
        //更新进度条
        mStr = stringBuilder.append(progress).append("%").toString()
        //计算比例
        mAngle = progress * 3.6f
        //进行绘制
        postInvalidate()
    }


}