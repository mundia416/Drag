package com.nosetrap.draglib.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.view.LayoutInflater
import android.view.WindowManager
import java.util.*

/**
 * a service for an overlay that is draggable
 */
abstract class DraggableOverlayService : Service() {
    private val onTouchListeners = ArrayList<DraggableOverlayOnTouchListener>()

    private lateinit var layoutInflater: LayoutInflater

    private lateinit var windowManager: WindowManager


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        registerDraggableTouchListener()
        initVars()
        code(intent)

        return super.onStartCommand(intent, flags, startId)

    }

    private fun initVars(){
        layoutInflater = LayoutInflater.from(this)
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
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