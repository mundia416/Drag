package com.nosetrap.draglib.overlay

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import java.util.*

/**
 * a service for an overlay that is draggable
 */
abstract class DraggableOverlayService : Service() {
    private val onTouchListeners = ArrayList<DraggableOverlayOnTouchListener>()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        registerDraggableTouchListener()
        code(intent)

        return super.onStartCommand(intent, flags, startId)

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
    fun registerDraggableTouchListener(onTouchListener: DraggableOverlayOnTouchListener){
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