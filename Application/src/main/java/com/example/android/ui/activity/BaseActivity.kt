package com.example.android.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.*
import com.example.android.camera2basic.R
import com.example.android.utils.U


/**
 * Created by
 */
abstract class BaseActivity : AppCompatActivity() {

    //private val cookieManager = SessionCookieManager.instance

    private val TAG = "BaseActivity"

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    protected open fun isFullScreenRequired() = false
    protected open fun isSecurityRequired() = false
    protected open fun isToColorBars() = true
    protected open fun getPageTitle(): String? = getString(R.string.app_name)
    protected open fun getPageSubtitle(): String? = null
    protected open fun getToolbar() = findViewById<Toolbar>(R.id.toolbar) ?: null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFullScreenRequired()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            /*View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);*/
        }
        if (isSecurityRequired())
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE)
        overridePendingTransition(getEntryAnimId(), getExitAnimId())
        //TODO some time crashed here
        setContentView(getLayoutResId())
        if (isToColorBars() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }

        val toolbar = getToolbar()
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.title = getPageTitle()

            val st = getPageSubtitle()
            if (U.notEmpty(st)) supportActionBar?.subtitle = st
            supportActionBar?.setDisplayHomeAsUpEnabled(getDisplayHomeAsUpEnabled())
        }

    }

    /**
     *
     */
    protected open fun getDisplayHomeAsUpEnabled() = false

    protected open fun getEntryAnimId(): Int = R.anim.slide_in_from_right
    protected open fun getExitAnimId(): Int = R.anim.slide_out

    fun setLightStatusBar(activity: Activity, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.visibility = View.VISIBLE
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            activity.window.statusBarColor = Color.WHITE
        }
    }

    protected fun signOut(context: Context) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (getDisplayHomeAsUpEnabled() && item?.itemId == android.R.id.home)
//            finish().also { return true }
//        return super.onOptionsItemSelected(item)
//    }

    // region keyboard open-close listener
    protected open fun getRootLayout(): ViewGroup? = null

    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rootLayout = getRootLayout() ?: return@OnGlobalLayoutListener
        val heightDiff = rootLayout.rootView.height - rootLayout.height
        val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top

        val broadcastManager = androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this@BaseActivity)

        if (heightDiff <= contentViewTop) {
            onHideKeyboard()

            val intent = Intent("KeyboardWillHide")
            broadcastManager.sendBroadcast(intent)
        } else {
            val keyboardHeight = heightDiff - contentViewTop
            onShowKeyboard(keyboardHeight)

            val intent = Intent("KeyboardWillShow")
            intent.putExtra("KeyboardHeight", keyboardHeight)
            broadcastManager.sendBroadcast(intent)
        }
    }

    private var keyboardListenersAttached = false
    protected open fun onShowKeyboard(keyboardHeight: Int) {}
    protected open fun onHideKeyboard() {}

    protected fun attachKeyboardListeners() {
        if (keyboardListenersAttached) return
        val rootLayout = getRootLayout() ?: return

        rootLayout.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
        keyboardListenersAttached = true
    }

    override fun onDestroy() {
        if (keyboardListenersAttached)
            getRootLayout()?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardLayoutListener)
        super.onDestroy()
    }
    // endregion
}