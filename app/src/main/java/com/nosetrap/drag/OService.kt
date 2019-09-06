package com.nosetrap.drag

import android.content.Intent
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import com.nosetrap.draglib.DragTouchListener
import com.nosetrap.draglib.DragOverlayService

class OService : DragOverlayService() {
    private val controllerDragListener : DragTouchListener by lazy { createDragTouchListener(R.layout.overlay) }

    override fun code(intent: Intent?) {
        addViewToWindow(controllerDragListener)
        val btnDragListener = createDragTouchListener(controllerDragListener.findView(R.id.btn),
                controllerDragListener,controllerDragListener.layoutParams)
        val exitListener = createDragTouchListener(controllerDragListener.findView(R.id.exit),
                controllerDragListener,controllerDragListener.layoutParams)

        btnDragListener.onClickListener = View.OnClickListener {
           // controllerDragListener.isDragEnabled = !btnDragListener.isDragEnabled
            btnDragListener.setGravity(Gravity.CENTER_VERTICAL.or(GravityCompat.START))
        }

        exitListener.onClickListener = View.OnClickListener {
            removeViewFromWindow(controllerDragListener)
        }
    }
}