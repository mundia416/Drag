package com.nosetrap.draglib

import android.view.View

interface OnDragListener {
    /**
     * is called at the end of onTouch
     * is called only when the draggable onTouchListener is enabled/active
     */
    fun onPostDrag(listenerId: Int?){

    }

    /**
    *executes every time the view is dragged
     * @param id is the id of the object
     */
    fun onDrag(view: View,listenerId: Int?)
}