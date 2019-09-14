package com.nosetrap.draglib

import android.view.View

abstract class DragTouchActuator(parent: DragTouchActuator?) : View.OnTouchListener {


    init {
        parent?.addChild(this)
    }

    /**
     * the id of this object
     */
    var id: Int? = null

    /**
     * a list of children where this dragTouchListener has been passed a parent
     */
    internal val dragTouchListenerChildren by lazy { ArrayList<DragTouchActuator>() }

    /**
     * when a child is dragged, its parent's onDragListener is alse called.
     * this is true for when a parent's onDragListener is called, its childrens onDrag listener is also called
     */
    var onDragListener: OnDragListener? = null


    // inverseX is used to determine whether the movement of the x axis should be in the opposite
    // //direction as normal
    var inverseX = false
        set(value) {
            field = value

            for(item in dragTouchListenerChildren){
                item.inverseX = value
            }
        }

    // inverseY is used to determine whether the movement of the x axis should be in the opposite
    // direction as normal,
    var inverseY = false
        set(value) {
            field = value

            for(item in dragTouchListenerChildren){
                item.inverseY = value
            }
        }

    /**
     * enable or disable the dragging ability. { even though this is enabled, it will only be able to
     * drag once the onTouchListener has been registered with the draggableOverlayService}
     */
    var isDragEnabled = true
    set(value) {
        field = value

        for(item in dragTouchListenerChildren){
            item.isDragEnabled = value
        }
    }


    /**
     * defines whether clicking is enabled
     */
    var isClickEnabled = true
        set(value) {
            field = value

            for(item in dragTouchListenerChildren){
                item.isClickEnabled = value
            }
        }

    /**
     * determines whether the onTouchListener returns true or false
     */
    var isTouchEnabled = true
        set(value) {
            field = value

            for(item in dragTouchListenerChildren){
                item.isTouchEnabled = value
            }
        }

    /**
     * when this dragTouchListener gets specified as a parent, the dragTouchListener (dragTouchActuator)
     * that specified this dragTouchListener as a parent is passed as a child to this method as [dragTouchActuator]
     */
    private fun addChild(dragTouchActuator: DragTouchActuator){
        dragTouchListenerChildren.add(dragTouchActuator)
    }


}