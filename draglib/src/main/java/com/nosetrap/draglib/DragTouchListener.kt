package com.nosetrap.draglib

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.core.view.GravityCompat
import com.nosetrap.draglib.data.ScreenDimensions
import com.nosetrap.draglib.util.ScreenUtil

/**
 * ontouch listener that enables [view] to be able to be dragged by the user on the screen
 * @param parent is the parent dragTouchListener to this dragTouchListener. this means that the
 * parent to this drag touch listener is the view which was added to the windowManager and the view
 * attached to this dragTouchListener is a child view of [parent]'s view parameter.
 * always specify [parent] whose view is added to the window manager if this dragTouchListener's view
 * is not added to the windowManager
 */
class DragTouchListener internal constructor(val view: View,val parent: DragTouchListener? = null,
                                                   val layoutParams: WindowManager.LayoutParams)
    : DragTouchActuator(parent) {

    /**
     * @NOTE its very important that this object is initialised before init{}, otherwise there will
     * be a NullPointerException
     */
    internal val screenUtil = ScreenUtil(view.context)

    private val dragTouchHandler = DragTouchHandler(this)

    init {
        //get screen dimension when the object is created
        updateScreenDimensions()
        view.setOnTouchListener(this)
    }

    fun setGravity(gravity: Int){
        layoutParams.gravity = gravity

        val dragView = if(parent != null) parent.view else view

        screenUtil.windowManager.updateViewLayout(dragView,layoutParams)
    }

    /**
     * defines whether this onTouchListener is ready for use, it can only be ready if it has been
    * registered with a dragableOverlayService
    */
    internal var isActive = false

    private lateinit var screenDimensions : ScreenDimensions

     var onClickListener: View.OnClickListener? = null

    var onDragListener: OnDragListener? = null



    /**
     * is a gesture detector defined by the user to create your own gestures
     */
     var gestureDetector: GestureDetector? = null

    fun findView(@IdRes viewId: Int): View {
        return this.view.findViewById(viewId)
    }



        /**
     * set a user defined gesture listener
     */
    fun setOnGestureListener(listener: GestureDetector.OnGestureListener){
        gestureDetector = GestureDetector(view.context,listener)
    }

    /**
     *
     */
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (isTouchEnabled) dragTouchHandler.handleTouch(view, event)

        return isTouchEnabled
    }



    /**
     * internal method which ensures that this onTouchListener will not work until it is registered
     * with the draggableOverlayService, it has to be registered to ensure that the screen dimension
     * is updated in onConfirguration changed
     */
    internal fun activate(){
        isActive = true

    }


    /**
     * should be called whenever the configuration changes
     */
     internal fun updateScreenDimensions(){
        screenDimensions = screenUtil.getScreenDimension()
    }

}