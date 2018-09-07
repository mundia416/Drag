package com.nosetrap.draglib

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import java.util.*

/**
 * a service for an overlay that is draggable
 */
abstract class DragOverlayService : Service() {
    private val onTouchListeners = ArrayList<DragTouchListener>()

    protected var layoutInflater: LayoutInflater? = null

    protected var windowManager: WindowManager? = null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initVars()
        code(intent)
        registerDragTouchListeners()

        return super.onStartCommand(intent, flags, startId)

    }



    /**
     * inflate a view using the layoutInflater
     */
    fun inflateView(@LayoutRes layout:Int): View{
        return layoutInflater?.inflate(layout,null,false)!!
    }

    private fun initVars(){
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(this)
        }
        if(windowManager == null) {
            windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
    }

    /**
     * the code to be executed in the onStartCommand
     */
    abstract fun code(intent: Intent?)

    /**
     * this is where all the draggable onTouchListeners should be registered
     */
    abstract fun registerDragTouchListeners()

    /**
     * all draggable listeners should be registered with the service. this enables for the screen
     * Dimensions value to be updated in onConfigurationChanged
     * this method can also be used to set the draggableOnTouchListener as the onTouchListener for a view
     */
    fun register(dragTouchListener: DragTouchListener){
       if(!onTouchListeners.contains(dragTouchListener)) {
           onTouchListeners.add(dragTouchListener)
           dragTouchListener.activate()
       }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        //update the screen dimensions on the onTouchListeners
        for(listener in onTouchListeners){
            listener.updateScreenDimensions()
        }

    }
}