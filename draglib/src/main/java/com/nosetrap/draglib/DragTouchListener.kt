package com.nosetrap.draglib

import android.content.Context
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

/**
 * ontouch listener that enables an overlay that it is attached to be dragged by the user on the screen
 * pass this class as an onTouchListener on a view
 * use subclass @Child if you want that dragging the view will result in the entire inflated layout to be dragged
 */
open class DragTouchListener(val inflatedOverlayView: View, val overlayParams: WindowManager.LayoutParams)
    : View.OnTouchListener {

    /**
     * @NOTE its very important that this object is initialised before init{}, otherwise there will
     * be a NullPointerException
     */
    private var windowManager = inflatedOverlayView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    init {
        //get screen dimension when the object is created
        updateScreenDimensions()
    }

    /**
     * enable or disable the dragging ability. { even though this is enabled, it will only be able to
     * drag once the onTouchListener has been registered with the draggableOverlayService}
     */
    var isDragEnabled = true

    /**
     * defines whether clicking is enabled
     */
    var isClickEnabled = true

    /**
     * determines whether the onTouchListener returns true or false
     */
    var isTouchEnabled = true

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

    private var onDragListener: OnDragListener? = null


    /**
     * a gestureDetector used only to add OnClickListener functionality
     */
    private val onClickListenerGestureDetector = GestureDetector(inflatedOverlayView.context,object :GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if(onClickListener != null){
                if(isClickEnabled) {
                    onClickListener?.onClick(inflatedOverlayView)
                }
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
     * set an on drag listener which monitors when the view is dragged
     */
    fun setOnDragListener(onDragListener: OnDragListener){
        this.onDragListener = onDragListener
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
        customGestureDetector = GestureDetector(inflatedOverlayView.context,listener)
    }

    /**
     *
     */
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (isTouchEnabled) {
            onDragListener?.onDrag(view)

            onClickListenerGestureDetector.onTouchEvent(event)
            customGestureDetector?.onTouchEvent(event)

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

                        windowManager.updateViewLayout(inflatedOverlayView, overlayParams)

                        onDragListener?.onPostDrag()
                    }
                }
            }
        }

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
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        screenDimensions = ScreenDimensions(screenWidth, screenHeight)
    }

    /**
     * screen dimension data class
     */
    private data class ScreenDimensions(var screenWidth:Int,var screenHeight:Int)
}