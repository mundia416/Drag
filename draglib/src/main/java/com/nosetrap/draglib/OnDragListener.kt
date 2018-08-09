package com.nosetrap.draglib

import android.view.View

interface OnDragListener {
    /**
     * is called at the end of onTouch
     * is called only when the draggable onTouchListener is enabled/active
     */
    fun onPostDrag(){

    }

    /**
    *executes every time the view is dragged
     */
    fun onDrag(view: View)
}