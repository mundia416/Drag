package com.nosetrap.draglib

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.lang.IllegalArgumentException

internal class DragTouchHandler(private val dragTouchListener: DragTouchListener) {

    private val params = dragTouchListener.layoutParams
    private var startingGridX = 0
    private var startingGridY = 0
    private var startingTouchX = 0.0f
    private var startingTouchY = 0.0f

    /**
     * a gestureDetector used only to add OnClickListener functionality
     */
    private val onClickListenerGestureDetector = GestureDetector(dragTouchListener.view.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    if (dragTouchListener.onClickListener != null) {
                        if (dragTouchListener.isClickEnabled) {
                            dragTouchListener.onClickListener?.onClick(dragTouchListener.view)
                        }
                    } else {
                        return false
                    }
                    return true
                }
            })

    fun handleTouch(view: View, event: MotionEvent) {
        onClickListenerGestureDetector.onTouchEvent(event)
        dragTouchListener.gestureDetector?.onTouchEvent(event)

        if (dragTouchListener.isActive && dragTouchListener.isDragEnabled) {

            handleOnDragListener(view)

            val rawX = if (dragTouchListener.inverseX) (event.rawX * -1) else event.rawX
            val rawY = if (dragTouchListener.inverseY) (event.rawY * -1) else event.rawY

            when (event.action) {
                MotionEvent.ACTION_DOWN -> actionDown(rawX, rawY)
                MotionEvent.ACTION_MOVE -> actionMove(rawX, rawY, event)
            }
        }
    }

    private fun handleOnDragListener(view: View){
        dragTouchListener.onDragListener?.onDrag(view)
        dragTouchListener.parent?.onDragListener?.onDrag(view)
        for(child in dragTouchListener.dragTouchListenerChildren){
            child.onDragListener?.onDrag(view)
        }
    }

    private fun handleOnPostDragListener(){
        dragTouchListener.onDragListener?.onPostDrag()
        dragTouchListener.parent?.onDragListener?.onPostDrag()
        for(child in dragTouchListener.dragTouchListenerChildren){
            child.onDragListener?.onPostDrag()
        }
    }

    private fun actionDown(rawX: Float,rawY: Float){
        startingGridX = params.x
        startingGridY = params.y
        startingTouchX = rawX
        startingTouchY = rawY
    }

    private fun actionMove(rawX: Float,rawY: Float,event: MotionEvent){
        val newX = (startingGridX + (rawX - this.startingTouchX)).toInt()
        val newY = (this.startingGridY + event.rawY - this.startingTouchY).toInt()
        dragTouchListener.layoutParams.x = newX
        dragTouchListener.layoutParams.y = newY

        val dragView = if(dragTouchListener.parent != null) dragTouchListener.parent.view else dragTouchListener.view

        try {
            dragTouchListener.screenUtil.windowManager.updateViewLayout(dragView, dragTouchListener.layoutParams)
        }catch (e: IllegalArgumentException){
            throw IllegalArgumentException("the object of ${DragTouchListener::class.java}'s view is not added to the window manager and does not have any parent " +
                    "object of ${DragTouchListener::class.java}. add the object of ${DragTouchListener::class.java} to the window manager in the ${DragOverlayService::class.java}" +
                    " or specify a parent object of ${DragTouchListener::class.java}")
        }

        handleOnPostDragListener()
    }
}