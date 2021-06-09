package com.example.android.ui.activity

import android.os.Bundle
import com.example.android.camera2basic.R

class AboutActivity : BaseActivity() {

    companion object {
        const val TAG = "About"
    }

    override fun getLayoutResId(): Int = R.layout.activity_about
    override fun getDisplayHomeAsUpEnabled(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.title = "About ${C.APP_NAME}"

        }

}