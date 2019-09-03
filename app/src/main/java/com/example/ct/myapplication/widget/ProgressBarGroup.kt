package com.example.ct.myapplication.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max


/**
 * 用来组装ProgressBar的ViewGroup
 */

class ProgressBarGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {


    private var mMaxView: View? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount //子节点中数量
        //找到子节点中最大的那个高度
        if(count==0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        var height = 0
        var width = 0
        for (i in 0..count){
           var childView = getChildAt(i)
            if (childView!=null){
                measureChild(childView,widthMeasureSpec,heightMeasureSpec)
                if (height<childView.measuredHeight){
                    mMaxView = childView
                }
                height = max(childView.measuredHeight,height)
                width = max(childView.measuredWidth,width)
            }

        }
        setMeasuredDimension( width,height)


    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //要让所有的View都有在同一个圆心 以最大的View开始布局
        val childCount = childCount
        val left = 0+ paddingLeft
        //先对最大的View进行布局
        mMaxView!!.layout(left, paddingTop, left + mMaxView!!.measuredWidth, paddingTop + mMaxView!!.measuredHeight)
        var childView: View?
        for (i in 0..childCount){
            childView = getChildAt(i)
            if (childView != mMaxView){
                //开始做一次减法
                if(childView!=null){
                    var extendLeft = (mMaxView!!.measuredWidth - childView.measuredWidth) /2
                    var extendTop = (mMaxView!!.measuredWidth - childView.measuredWidth) /2
                    childView.layout(left+extendLeft ,paddingTop +extendTop,left + extendLeft+ childView.measuredWidth,paddingTop+extendTop + childView.measuredHeight)
                }
            }

        }

    }





}