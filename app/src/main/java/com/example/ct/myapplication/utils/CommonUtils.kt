package com.example.ct.myapplication.utils

import android.content.Context
import android.util.TypedValue
import java.lang.Error

class CommonUtils private constructor(){


    init {
        throw Error("Do not need instantiate!")
    }


    /**
     * 使用类名来调用函数
     */
    companion object{
        /**
         * dp转px
         */
        fun dp2px(context: Context, dpVal: Float): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.resources.displayMetrics
            )
        }

    }








}