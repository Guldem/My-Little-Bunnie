package nl.guldem.mylittlebunnie

import android.app.Application
import nl.guldem.mylittlebunnie.di.modules.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyBunnieApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyBunnieApplication)
            modules(appModules)
        }
    }
}