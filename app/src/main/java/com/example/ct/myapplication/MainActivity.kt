package com.example.ct.myapplication

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //
    private var progressBarValueAnimator: ValueAnimator ? = null //更新进度的动画
    private var rotateCircleAnimator: ObjectAnimator ?  = null //这里是旋转动画


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBarValueAnimator = ValueAnimator.ofInt(0,100)//进度条
        progressBarValueAnimator?.addUpdateListener {
            var progress = it.animatedValue as Int

            progress_text.updateProgress(progress)
            scale_view.startProgress(progress)
        }
        progressBarValueAnimator?.repeatCount = ValueAnimator.INFINITE
        progressBarValueAnimator?.duration = 10000
        wavewater_view.star(50)
        progressBarValueAnimator?.interpolator = LinearInterpolator()
        progressBarValueAnimator?.start()

        //外面的小球旋转动画
        rotateCircleAnimator = ObjectAnimator.ofFloat(progress_outview,"rotation",360f)
        rotateCircleAnimator?.interpolator = LinearInterpolator()
        rotateCircleAnimator?.duration = 5000
        rotateCircleAnimator?.repeatCount =ValueAnimator.INFINITE
        rotateCircleAnimator?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        progressBarValueAnimator?.let {
            progressBarValueAnimator?.cancel()
            progressBarValueAnimator = null
        }
        rotateCircleAnimator?.let {
            rotateCircleAnimator?.cancel()
            rotateCircleAnimator = null
        }
        wavewater_view.onRemove()

    }
}
