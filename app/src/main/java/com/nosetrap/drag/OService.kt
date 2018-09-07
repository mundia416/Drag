package com.nosetrap.drag

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.Button
import com.nosetrap.draglib.DragTouchListener
import com.nosetrap.draglib.DragOverlayService

class OService : DragOverlayService() {
    private lateinit var buttonDragListener : DragTouchListener

    override fun code(intent: Intent?) {
        val view = layoutInflater?.inflate(R.layout.overlay,null,false)
       // val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                else WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT)
        windowManager?.addView(view,params)


        buttonDragListener = DragTouchListener(view!!, params)
        view.setOnTouchListener(buttonDragListener)
        (view.findViewById<Button>(R.id.btn)).setOnTouchListener(buttonDragListener)
    }

    override fun registerDragTouchListeners() {
       register(buttonDragListener)
    }
}