package com.nosetrap.drag

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.nosetrap.applib.Activity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    override fun code() {


        btn.setOnClickListener {
            if(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PermissionManager.canDrawOverlays(this)
                    } else {
                       true
                    }) {
                startService(Intent(this, OService::class.java))
            }else{
                PermissionManager.requestDrawOverlays(this,100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        startService(Intent(this, OService::class.java))
    }

    override fun initVariables() {

    }

    override fun setView(): Int {
        return R.layout.activity_main
    }
}
