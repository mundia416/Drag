package com.nosetrap.draglib.util

import android.graphics.PixelFormat
import android.os.Build
import android.view.WindowManager

class DragUtil {

    companion object{

        /**
         *
         */
        fun getLayoutParams(): WindowManager.LayoutParams {
            return WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                            or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    else WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT)
        }
    }
}