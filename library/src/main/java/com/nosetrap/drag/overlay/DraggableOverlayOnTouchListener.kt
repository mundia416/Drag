package com.nosetrap.drag.overlay

import android.content.Context
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

/**
 * ontouch listener that enables an overlay that it is attached to be dragged by the user on the screen
 * pass this class as an onTouchListener on a view
 */
open class DraggableOverlayOnTouchListener(private val overlayView: View, private val overlayParams: WindowManager.LayoutParams)
    : View.OnTouchListener {

    init {
        //get screen dimension when the object is created
        updateScreenDimensions()
    }

    //todo setup class for onClick and gestures

    /**
     * enable or disable the dragging ability. { even though this is enabled, it will only be able to
     * drag once the onTouchListener has been registered with the draggableOverlayService
     */
    var isDragEnabled = true

    /**
     * defines whether this onTouchListener is ready for use, it can only be ready if it has been
     * registered with a dragableOverlayService
     */
    private var isActive = false



    // inverseX is used to determine whether the movement of the x axis should be in the opposite
    // //direction as normal
    private var inverseX = false
    // inverseY is used to determine whether the movement of the x axis should be in the opposite
    // direction as normal,
    private var inverseY = false

    private val params = overlayParams
    private var startingGridX = 0
    private var startingGridY = 0
    private var startingTouchX = 0.0f
    private var startingTouchY = 0.0f

    private lateinit var screenDimensions : ScreenDimensions

    private var onClickListener: View.OnClickListener? = null

    private val windowManager = overlayView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    /**
     * a gestureDetector used only to add OnClickListener functionality
     */
    private val onClickListenerGestureDetector = GestureDetector(overlayView.context,object :GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if(onClickListener != null){
                onClickListener?.onClick(overlayView)
            }else{
                return  false
            }
            return true
        }
    })

    /**
     * is a gesture detector defined by the user to create your own gestures
     */
    private var customGestureDetector: GestureDetector? = null


    fun setOnClickListener(onClickListener: View.OnClickListener){
        this.onClickListener = onClickListener

    }


    /**
     * inverse the movement on the X axis
     */
    fun setInverseX(inverse: Boolean){
        this.inverseX = inverse
    }

    /**
     * inverse the movement on the Y axis
     */
    fun setInverseY(inverse: Boolean){
        this.inverseY = inverse
    }

    /**
     * set a user defined gesture detector
     */
    fun setGestureDetector(gestureDetector: GestureDetector){
        this.customGestureDetector = gestureDetector
    }

    /**
     * set a user defined gesture listener
     */
    fun setOnGestureListener(listener: GestureDetector.OnGestureListener){
        customGestureDetector = GestureDetector(overlayView.context,listener)
    }

    /**
     *
     */
    override fun onTouch(view: View, event: MotionEvent): Boolean {
      onClickListenerGestureDetector.onTouchEvent(event)
            customGestureDetector?.onTouchEvent(event)

        onPreDrag()

        if (isActive && isDragEnabled) {
            val rawX = if (inverseX) (event.rawX * -1) else event.rawX
            val rawY = if (inverseY) (event.rawY * -1) else event.rawY
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startingGridX = params.x
                    startingGridY = params.y
                    startingTouchX = rawX
                    startingTouchY = rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = (startingGridX + (rawX - this.startingTouchX)).toInt()
                    val newY = (this.startingGridY + event.rawY - this.startingTouchY).toInt()
                    overlayParams.x = newX
                    overlayParams.y = newY
                    val inflatedWidth = (overlayView?.width)
                    if ((overlayParams.x + inflatedWidth) <= 0) {
                        overlayParams.x = 1 - inflatedWidth
                    } else if (overlayParams.x >= screenDimensions.screenWidth) {
                        overlayParams.x = screenDimensions.screenWidth - 1
                    }
                    if (overlayParams.y <= 0) {
                        overlayParams.y = 1
                    } else if (overlayParams.y >= screenDimensions.screenHeight) {
                        overlayParams.y = screenDimensions.screenHeight - 1
                    }

                    windowManager.updateViewLayout(overlayView, overlayParams)

                    onPostDrag()
                }
            }
        }
        return true
    }


    /**
     * is called at the start of onTouch
     */
    fun onPreDrag(){

    }

    /**
     * is called at the end of onTouch
     */
    fun onPostDrag(){

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
        val display = this.windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        screenDimensions = ScreenDimensions(screenWidth, screenHeight)
    }

    private data class ScreenDimensions(var screenWidth:Int,var screenHeight:Int)
}