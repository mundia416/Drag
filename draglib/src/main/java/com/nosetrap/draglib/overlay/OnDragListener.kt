package com.nosetrap.draglib.overlay

import android.view.View

interface OnDragListener {

    /**
     * is called at the start of onTouch
     * is called whether the draggable onTouchListener is enabled/active or not
     */
    fun onPreDrag(){

    }

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