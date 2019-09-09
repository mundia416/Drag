package com.nosetrap.drag

import android.content.Intent
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import com.nosetrap.draglib.DragTouchListener
import com.nosetrap.draglib.DragOverlayService
import timber.log.Timber

class OService : DragOverlayService() {
    private val controllerDragListener : DragTouchListener by lazy { createDragTouchListener(R.layout.overlay) }

    override fun code(intent: Intent?) {
        Timber.d("overlay started")
        addViewToWindow(controllerDragListener)
        val btnDragListener = createDragTouchListener(controllerDragListener.findView(R.id.btn), controllerDragListener)
        val exitListener = createDragTouchListener(controllerDragListener.findView(R.id.exit), controllerDragListener)
        val middleListener = createDragTouchListener(controllerDragListener.findView(R.id.btnMiddle), controllerDragListener)


        btnDragListener.onClickListener = View.OnClickListener {
            Timber.d("btn Clicked")
            middleListener.view.visibility = View.GONE

        }

        exitListener.onClickListener = View.OnClickListener {
            Timber.d("exit Clicked")
            middleListener.view.visibility = View.VISIBLE

        }
    }
}