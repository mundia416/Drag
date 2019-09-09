package com.nosetrap.draglib

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import com.nosetrap.draglib.util.DragUtil
import java.util.*

/**
 * a service for an overlay that is draggable
 */
abstract class DragOverlayService : Service() {
    private val onTouchListeners = ArrayList<DragTouchListener>()

    private val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(this) }
    private val windowManager: WindowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        code(intent)

        return super.onStartCommand(intent, flags, startId)
    }

    fun createDragTouchListener(@LayoutRes inflateLayout: Int,parent: DragTouchListener? = null,
                                overlayParams: WindowManager.LayoutParams? = null): DragTouchListener {
        return createDragTouchListener(inflateView(inflateLayout),parent,overlayParams)
    }

    fun createDragTouchListener(inflatedOverlayView: View,parent: DragTouchListener? = null,
                                overlayParams: WindowManager.LayoutParams? = null): DragTouchListener{
        val params = if(parent == null) {
            overlayParams ?:  DragUtil.getLayoutParams()
        }else{
            overlayParams ?: parent.layoutParams
        }
        val dragTouchListener = DragTouchListener(inflatedOverlayView,parent,params)
        registerDragTouchListener(dragTouchListener)
        return dragTouchListener
    }

    /**
     * inflate a view using the layoutInflater
     */
    protected fun inflateView(@LayoutRes layout:Int): View{
        return layoutInflater.inflate(layout,null,false)!!
    }

    protected fun addViewToWindow(dragTouchListener: DragTouchListener){
        windowManager.addView(dragTouchListener.view,dragTouchListener.layoutParams)
    }

    protected fun removeViewFromWindow(dragTouchListener: DragTouchListener){
        windowManager.removeView(dragTouchListener.view)
    }

    /**
     * this updates the changes to the layout params of the view attached to the [dragTouchListener]
     * on the [windowManager]
     */
    fun updateViewOnWindow(dragTouchListener: DragTouchListener){
        windowManager.updateViewLayout(dragTouchListener.view,dragTouchListener.layoutParams)
    }


    /**
     * the code to be executed in the onStartCommand
     */
    abstract fun code(intent: Intent?)


    /**
     * all draggable listeners should be registered with the service. this enables for the screen
     * Dimensions value to be updated in onConfigurationChanged
     */
    private fun registerDragTouchListener(dragTouchListener: DragTouchListener){
       if(!onTouchListeners.contains(dragTouchListener)) {
           onTouchListeners.add(dragTouchListener)
           dragTouchListener.activate()
       }
    }

    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        //update the screen dimensions on the onTouchListeners
        for(listener in onTouchListeners){
            listener.updateScreenDimensions()
        }

    }
}