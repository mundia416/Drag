package com.nosetrap.draglib.overlay

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
abstract class DraggableOverlayService : Service() {
    private val onTouchListeners = ArrayList<DraggableOverlayOnTouchListener>()

    protected lateinit var layoutInflater: LayoutInflater
    private var layoutInflaterInitialised = false

    protected lateinit var windowManager: WindowManager
    private var windowManagerInitialised = false


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        initVars()
        code(intent)
        registerDraggableTouchListener()

        return super.onStartCommand(intent, flags, startId)

    }

    /**
     * inflate a view using the layoutInflater
     */
    fun inflateView(@LayoutRes layout:Int): View{
        return layoutInflater.inflate(layout,null,false)
    }

    private fun initVars(){
        if(!layoutInflaterInitialised) {
            layoutInflater = LayoutInflater.from(this)
            layoutInflaterInitialised = true
        }
        if(!windowManagerInitialised) {
            windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManagerInitialised = true
        }
    }

    /**
     * the code to be executed in the onStartCommand
     */
    abstract fun code(intent: Intent)

    /**
     * this is where all the draggable onTouchListeners should be registered
     */
    abstract fun registerDraggableTouchListener()

    /**
     * all draggable listeners should be registered with the service. this enables for the screen
     * Dimensions value to be updated in onConfigurationChanged
     */
    fun registerOnTouchListener(onTouchListener: DraggableOverlayOnTouchListener){
        onTouchListeners.add(onTouchListener)
        onTouchListener.activate()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        //update the screen dimensions on the onTouchListeners
        for(listener in onTouchListeners){
            listener.updateScreenDimensions()
        }

    }
}