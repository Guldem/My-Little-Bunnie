package nl.guldem.mylittlebunnie.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.danlew.android.joda.JodaTimeAndroid
import nl.guldem.mylittlebunnie.R
import nl.guldem.mylittlebunnie.services.NotificationService
import org.koin.android.ext.android.inject

const val CHANNEL_ID = "MYLITTLEBUNNIE"

class MainActivity : AppCompatActivity() {

    private val notificationService: NotificationService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        //super ugly but girlfriend wants splash screen to be shown a little longer at all costs..
        Thread.sleep(1500)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JodaTimeAndroid.init(baseContext)
        notificationService.createNotificationChannel()
    }


}
