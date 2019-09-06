package com.nosetrap.draglib.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.nosetrap.draglib.DragTouchListener
import com.nosetrap.draglib.data.ScreenDimensions

internal class ScreenUtil(context: Context) {

    internal val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    fun getScreenDimension(): ScreenDimensions{
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        return ScreenDimensions(screenWidth, screenHeight)
    }

}