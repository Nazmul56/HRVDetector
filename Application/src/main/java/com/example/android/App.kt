package com.example.android

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.example.android.utils.Lg.d

class App : Application(), LifecycleObserver {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        context = applicationContext
    }

    // region Life-cycle events
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onStateChanged() {
        val curState = ProcessLifecycleOwner.get().lifecycle.currentState
        isAppInFg = curState.isAtLeast(Lifecycle.State.RESUMED)
        //P.setIsAppForegroundPref(getApplicationContext(),isAppInFg);
        d(TAG, "App state has changed: current = $curState")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForeground() {
        d(TAG, "onAppForeground")
        isAppInFg = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackground() {
        d(TAG, "onAppBackground")
        isAppInFg = false
        //P.setIsAppForegroundPref(getApplicationContext(),isAppInFg);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppDestroyed() {
        d(TAG, "onAppDestroyed")
        isAppInFg = false
        //P.setIsAppForegroundPref(getApplicationContext(),isAppInFg);
    }

    // endregion
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        d(TAG, "On Terminate")
    }

    companion object {
        val TAG = App::class.java.simpleName
        var isAppInFg = false
        val instance: App? = null
        var context: Context? = null
            private set
    }
}