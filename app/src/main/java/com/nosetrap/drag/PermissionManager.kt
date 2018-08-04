package com.nosetrap.drag

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi


/**
 * main class to deal with permissions. it can be used in two ways,either instantiate it or access
 * te methods statically
 */
class PermissionManager(private val activity: Activity) {

    @RequiresApi(Build.VERSION_CODES.M)
    fun canDrawOverlays(): Boolean{
        return canDrawOverlays(activity)
    }

    fun requestDrawOverlays(requestCode: Int){
        requestDrawOverlays(activity,requestCode)
    }

companion object {

    fun requestDrawOverlays(activity: Activity,requestCode: Int){
        activity.startActivityForResult( Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity?.packageName)),
                requestCode)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun canDrawOverlays(activity: Activity): Boolean {

        return Settings.canDrawOverlays(activity)
        //todo android 26 has a bug which is fixed by a security patch which needs to be downloaded
        //todo to update the OS. figure out a workaround to this bug. right now the correct value
        //todo of this on android 26 can only be retrieved if the app is restarted
        /* if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                return false
            }else{
                //there is a bug with android 26 so here is a workaround to check the permission
                //the workaround is to add an invisible overlay
                try{
                    val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager

                    //window manager can be null
                    if(windowManager == null) return false

                    val invisibleView = View(activity)
                    val params = WindowManager.LayoutParams(WRAP_CONTENT, WRAP_CONTENT,
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) TYPE_APPLICATION_OVERLAY
                                    or FLAG_LAYOUT_NO_LIMITS
                            else TYPE_SYSTEM_ERROR, FLAG_NOT_TOUCH_MODAL or
                            FLAG_NOT_FOCUSABLE or FLAG_LAYOUT_NO_LIMITS,
                            PixelFormat.TRANSLUCENT)
                invisibleView.layoutParams = params
                    windowManager.addView(invisibleView,params)
                    windowManager.removeView(invisibleView)
                    return true
                }catch(e: Exception){
                   e.printStackTrace()
                }
                return false
            }
        }*/
    }
}


}