package com.hermanowicz.wirelesswhisper

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.work.Configuration
import com.hermanowicz.wirelesswhisper.bluetooth.BluetoothAction
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider, LifecycleEventObserver {

    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                BluetoothAction.goToAction(applicationContext, MyBluetoothService.ACTION_APP_VISIBLE)
            }
            Lifecycle.Event.ON_STOP -> {
                BluetoothAction.goToAction(applicationContext, MyBluetoothService.ACTION_APP_INVISIBLE)
            }
            else -> {}
        }
    }
}
