package com.example.ct.myapplication.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.example.ct.myapplication.widget.bean.Point
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.ct.myapplication.R


/**
 * 水波纹的View
 */
class WaveWaterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val defaultSize = 405//默认宽高
    private var mProgress = 0.5f //进度
    private var mCirclePaint: Paint  = Paint()//外部圆
    private var mLightPaint: Paint = Paint() //第一条曲线
    private var mDarkPaint : Paint = Paint() //第二条曲线
    private val mAllPointCount:Int = 2 //由两个点确定波浪线
    private var offsetX = 0 //在X轴的偏移量，产生波动效果
    private var lightPath = Path()
    private val darkPath =  Path()
    private var points = arrayOfNulls<Point>(mAllPointCount)
    private var mRadius = 200f//默认半径
    private var mWidth = 0
    private var mHeight = 0
    private var mWaveAnimator : ValueAnimator? = null //波动的动画

    init {
        //初始化颜色
        mCirclePaint.color = resources.getColor(R.color.empty_bg,null)//颜色
        mLightPaint.color = resources.getColor(R.color.colorPrimary,null)
        mDarkPaint.color = resources.getColor(R.color.colorPrimaryDark,null)
        //初始化线条
        mCirclePaint.isAntiAlias = true
        mLightPaint.isAntiAlias = true

        mWaveAnimator = ValueAnimator.ofInt(0,400)
        mWaveAnimator!!.interpolator = LinearInterpolator()
        mWaveAnimator!!.addUpdateListener {
            offsetX = it!!.animatedValue as Int
            postInvalidate()
        }
        mWaveAnimator!!.repeatCount = ValueAnimator.INFINITE
        mWaveAnimator!!.duration = 2500
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


    /**
     * 使用贝塞尔曲线
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mWidth = width - paddingLeft -paddingRight
        mHeight = height -  paddingTop -paddingBottom
        val bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val bitmapCanvas = Canvas(bitmap)
        bitmapCanvas.drawCircle(mWidth/2f,mHeight/2f,mRadius,mCirclePaint) //先画圆

        //开始绘制波浪线
        getPoints()
        bitmapCanvas.drawPath(lightPath,mLightPaint)
        bitmapCanvas.drawPath(darkPath,mDarkPaint)

        //这里是设置绘制图像的Mode方式
         canvas!!.drawBitmap(bitmap,0f,0f,null)

    }


    private fun getPoints() {
        //获取需要绘制的坐标数组
        //先初始化中间节点的坐标
        mLightPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN ) //图层相交模式
        mDarkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP ) //图层相交模式
        lightPath.reset()
        darkPath.reset()
        //设置P0
        points[0] = Point(-2*mRadius,mHeight-mHeight * mProgress )
        points[1] = Point(4*mRadius,mHeight-mHeight * mProgress )


        lightPath.moveTo(points[1]!!.x+offsetX,points[1]!!.y)
        lightPath.lineTo(points[1]!!.x +offsetX,mHeight*1f)
        lightPath.lineTo(points[0]!!.x+offsetX,mHeight*1f)
        lightPath.lineTo(points[0]!!.x +offsetX,points[0]!!.y)
        for (i in 1..3){
            lightPath.rQuadTo(100f,30f,200f,0f)
            lightPath.rQuadTo(100f,-30f,200f,0f)
        }
        lightPath.close()
        darkPath.moveTo(points[0]!!.x -offsetX,points[0]!!.y)
        darkPath.lineTo(points[0]!!.x - offsetX,mHeight*1f)
        darkPath.lineTo(points[1]!!.x -offsetX,mHeight*1f)
        darkPath.lineTo(points[1]!!.x - offsetX,points[1]!!.y)
        for (i in 1..3){
            darkPath.rQuadTo(-100f,30f,-200f,0f)
            darkPath.rQuadTo(-100f,-30f,-200f,0f)
        }
        darkPath.close()

    }

    fun star( progress: Int){
        mProgress = progress /100f
        if (mWaveAnimator!=null) {
            if (!mWaveAnimator!!.isRunning){
                mWaveAnimator!!.cancel()
                mWaveAnimator!!.start()
            }
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




    fun onRemove(){
        mWaveAnimator?.let {
            it.cancel()
        }
        mWaveAnimator = null
    }



}