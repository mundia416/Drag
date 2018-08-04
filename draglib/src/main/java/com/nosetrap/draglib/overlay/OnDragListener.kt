package com.nosetrap.draglib.overlay

import android.view.View

interface OnDragListener {

    /**
    *executes every time the view is dragged
     */
    fun onDrag(view: View)
}